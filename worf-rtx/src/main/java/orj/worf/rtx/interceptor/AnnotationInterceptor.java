package orj.worf.rtx.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import orj.worf.rtx.annotation.TCCTransactional;
import orj.worf.rtx.constant.TransactionConstant;
import orj.worf.rtx.constant.TransactionStatus;
import orj.worf.rtx.exception.RTXCheckException;
import orj.worf.rtx.manage.TransactionManager;
import orj.worf.rtx.model.Branchs;
import orj.worf.rtx.model.InvokeMethod;
import orj.worf.rtx.model.Transaction;
import orj.worf.rtx.model.TransactionConfig;
import orj.worf.rtx.model.TransactionContext;
import orj.worf.rtx.util.RTXUtil;

@Service("rtxannotationInterceptor")
public class AnnotationInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AnnotationInterceptor.class);

	@Autowired
	private TransactionManager transactionManager;
	
	/**
	 * @Description 拦截入口
	 * @param pjp
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月17日
	 */
	public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
		
		//获取注解
		TCCTransactional tcc = RTXUtil.getTCCAnnotation(pjp);
		//获取注解参数
		TransactionConfig config = getAnnotationConfig(tcc);
		
		return tccProcessing(pjp, config);
	}

	private Object tccProcessing(ProceedingJoinPoint pjp, TransactionConfig config) throws Throwable {
		
		boolean hasContextParam = RTXUtil.hasTransactionContextParam(pjp);
		TransactionContext context = null;
		if(hasContextParam) {
			context = RTXUtil.getRTXtransactionContext(pjp.getArgs());
		}
		
		Deque<Transaction> transDeque = TransactionManager.threadLocalTransaction.get();
		
		if(isRootNode(context, config, hasContextParam)) {
			if(transDeque != null && transDeque.size() >0) {
				throw new RTXCheckException("", "TCC事务中不允许再嵌套新的TCC事务，请检查："
						+ pjp.getTarget().getClass().getCanonicalName() + "." 
						+ pjp.getSignature().getName());
			}
			return rootProcess(pjp, config, context);
		} else if (isConsumer(context, config, hasContextParam)) {
			return consumerProcess(transDeque, context, pjp);
		} else if(isProvider(context, config, hasContextParam)){
			if(context == null) {
				/**
				 * context为null有两种情况
				 * 1、同服务内的调用
				 * 2、无主事务存在时的调用
				 * 
				 * transDeque如果也为空
				 * 1、同服务内的异步调用
				 * 2、无主事务存在时的调用
				 * 以上两种情况，直接跳过，不纳入TCC事务管理
				 */
				if(transDeque == null || transDeque.size() <= 0) {
					return pjp.proceed();
				}
				context = new TransactionContext();
				context.setId(transDeque.peek().getId());
				context.setStatus(TransactionStatus.TRYING);
			}
			return providerProcess(transDeque, context, pjp, config);
		} else { 
			//有值，无注解且在trying阶段
			if(context != null && TransactionStatus.TRYING.equals(context.getStatus()) 
					&& config.getTransactional() == null) {
				//rpc调用给provider创建的对象本身就是一个代理对象，pjp.getTarget()可能就是代理对象
				throw new RTXCheckException("", "TransactionContext参数值必须为null，请检查："
						+ pjp.getTarget().getClass().getCanonicalName() + "." 
						+ pjp.getSignature().getName());
			}
			return pjp.proceed();
		}
	}
	
	/**
	 * @Description 获取注解参数
	 * @param tcc
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月17日
	 */
	private TransactionConfig getAnnotationConfig(TCCTransactional tcc) {
		TransactionConfig config = new TransactionConfig();
		if(tcc != null) {
			config.setPropagation(tcc.propagation());
			//如果注解上无特别指定，以下用默认值
			if(tcc.timeout() != -1) {
				config.setTimeOut(tcc.timeout());
			}
			config.setAsyncConfirm(tcc.asyncConfirm());
			config.setAsyncCancel(tcc.asyncCancel());
			config.setTransactional(tcc);
		}
		return config;
	}
	
	/**
	 * @Description  无参无值有注解
	 * @param context
	 * @param config
	 * @param hasContextParam
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月23日
	 */
	public boolean isRootNode(TransactionContext context, TransactionConfig config,
			boolean hasContextParam) {
		if(config.getTransactional() != null && context == null && !hasContextParam) {
			return true;  
		}
		return false;
	}
	
	/**
	 * @Description 有参无值无注解
	 * @param context
	 * @param config
	 * @param hasContextParam
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月23日
	 */
	public boolean isConsumer(TransactionContext context, TransactionConfig config,
			boolean hasContextParam) {
		if(context == null && config.getTransactional() == null && hasContextParam) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 有参无值/有参有值， 有注解
	 * @param context
	 * @param config
	 * @param hasContextParam
	 * @return
	 * @author linruzhou 
	 * @date 2018年10月23日
	 */
	public boolean isProvider(TransactionContext context, TransactionConfig config,
			boolean hasContextParam) {
		//在跨服务的情况下context有值，
		//在同服务内的TCC事务，context无值
		if(hasContextParam && config.getTransactional() != null) {
			return true;
		}
		return false;
	}
	
	private Object rootProcess(ProceedingJoinPoint pjp, TransactionConfig config,
			TransactionContext context) throws Throwable {
		Transaction currentTransaction = null;
		Object returnValue = null;
		try {
			currentTransaction = transactionManager.startTrans(config, context, false);
			addBranch(pjp, context, currentTransaction, config);
			returnValue = pjp.proceed();
		} catch (Throwable e) {
			transactionManager.rollbackTrans(currentTransaction, true);
			throw e;
		} finally {
		    Deque<Transaction> transactionDeque = TransactionManager.threadLocalTransaction.get();
		    if(transactionDeque != null) {
			    transactionDeque.pop();
			    if(transactionDeque.isEmpty()) {
			    	TransactionManager.threadLocalTransaction.remove();
			    }
		    }
		}
		transactionManager.commitTrans(currentTransaction, true);
		return returnValue;
	}
	
	private void addBranch(ProceedingJoinPoint pjp, TransactionContext context,
			Transaction currentTransaction, TransactionConfig config) {
		List<Branchs> branchs = currentTransaction.getBranchsList();
		if(branchs == null) {
			branchs = new ArrayList<Branchs>();
		}
		if(context == null) {
			context = new TransactionContext();
		}
		context.setId(currentTransaction.getId());
		context.setSlaveId(RTXUtil.getTransactionId(TransactionConstant.SERVER_NAME));
		Branchs branch = getBranch(pjp, currentTransaction, config.getTransactional().confirmMethod(),
				config.getTransactional().cancelMethod(), branchs, context);
		branchs.add(branch);
		currentTransaction.setBranchsList(branchs);
		transactionManager.getTransactionPersist().updateTrans(currentTransaction);
	}
	
	private Branchs getBranch(ProceedingJoinPoint pjp, Transaction transaction, String confirmMethodName,
			String cancelMethodName, List<Branchs> branchs, TransactionContext context) {
		
		Method method = RTXUtil.getAnnotationMethod(pjp);
		Branchs branch = new Branchs();
		branch.setId(transaction.getId());
		
		Object[] args = pjp.getArgs();
		Object[] confirmArgs = new Object[args.length];
		Object[] cancelArgs = new Object[args.length];
	    System.arraycopy(args, 0, confirmArgs, 0, args.length);
	    System.arraycopy(args, 0, cancelArgs, 0, args.length);
	    
	    if(context != null && args.length >0 
	    		&& ((MethodSignature)pjp.getSignature()).getParameterTypes()[0].
	    				equals(TransactionContext.class)) {
	    	TransactionContext conformContext = new TransactionContext();
		    conformContext.setId(context.getId());
		    conformContext.setStatus(TransactionStatus.CONFIRMING);
		    conformContext.setSlaveId(context.getSlaveId());
		    confirmArgs[0] = conformContext;
		    
		    TransactionContext cancelContext = new TransactionContext();
		    cancelContext.setId(context.getId());
		    cancelContext.setStatus(TransactionStatus.CANCELING);
		    cancelContext.setSlaveId(context.getSlaveId());
		    cancelArgs[0] = cancelContext;
	    }
	    context.setStatus(TransactionStatus.TRYING);
	    
	    InvokeMethod confirmMethod = new InvokeMethod();
		confirmMethod.setArgs(confirmArgs);
		confirmMethod.setMethodName(confirmMethodName);
		confirmMethod.setParameterTypes(method.getParameterTypes());
		confirmMethod.setClassName(RTXUtil.getTargetClassName(pjp.getTarget().getClass(),
				confirmMethod.getMethodName(), confirmMethod.getParameterTypes()));
		branch.setConfirmMethod(confirmMethod);
		
		InvokeMethod cancelMethod = new InvokeMethod();
		cancelMethod.setArgs(cancelArgs);
		cancelMethod.setMethodName(cancelMethodName);
		cancelMethod.setParameterTypes(method.getParameterTypes());
		cancelMethod.setClassName(RTXUtil.getTargetClassName(pjp.getTarget().getClass(),
				cancelMethod.getMethodName(), cancelMethod.getParameterTypes()));
		branch.setCancelMethod(cancelMethod);
		
		branch.setProjectName(TransactionConstant.SERVER_NAME);
		return branch;
	}
	
	private Object consumerProcess(Deque<Transaction> transDeque, 
			TransactionContext context, ProceedingJoinPoint pjp) throws Throwable {
		if(transDeque != null && transDeque.peek() != null) {
			Transaction tccTransaction = transDeque.peek();
			if(TransactionStatus.TRYING.equals(tccTransaction.getStatus())) {
				if(context == null) {
					context = new TransactionContext();
				}
				context.setId(tccTransaction.getId());
				context.setSlaveId(RTXUtil.getTransactionId(TransactionConstant.SERVER_NAME));
				List<Branchs> branchs = tccTransaction.getBranchsList();
				if(branchs == null) {
					branchs = new ArrayList<Branchs>();
				}
				Method method = ((MethodSignature) pjp.getSignature()).getMethod();
				Branchs branch = getBranch(pjp, tccTransaction, method.getName(),
						method.getName(), branchs, context);
				//把事务上下文参数赋给第一个参数
				pjp.getArgs()[0] = context;
				branchs.add(branch);
				tccTransaction.setBranchsList(branchs);
	
				transactionManager.getTransactionPersist().updateTrans(tccTransaction);
			}
		} 
		return pjp.proceed(pjp.getArgs());
	}
	
	
	private Object providerProcess(Deque<Transaction> stackRTX, TransactionContext context,
			ProceedingJoinPoint pjp, TransactionConfig config) throws Throwable {
		Transaction rTXTransaction = null;
		//被调用的从事务是否在新的应用节点上
		boolean isNewAPPNode = false;
		if(stackRTX == null) {
			isNewAPPNode = true;
		} else {
			rTXTransaction = stackRTX.peek();
			if(rTXTransaction == null) {
				isNewAPPNode = true;
			}
		}
		try {
			if(isNewAPPNode) {
				//根据事务ID获取事务
				//从节点的事务存储是以主事务ID+从节点ID
				String xid = context.getId();
				if(!StringUtils.isBlank(context.getSlaveId())) {
					xid = xid + context.getSlaveId();
				}
				rTXTransaction = transactionManager.getTransactionPersist().queryTrans(xid);
				// 在CONFIRMING、CANCELING状态下新节点查不到事务，直接返回（说明已被执行过）
				// 一个节点在操作成功的前提下做一次提交或者回滚，如果提交或回滚的时间长，rpc调用的超时时间必须设置长些
				if(!TransactionStatus.TRYING.equals(context.getStatus())
						&& rTXTransaction == null) {
					return null;
				}
				if(rTXTransaction == null) {
					//从节点中事务
					rTXTransaction = transactionManager.startTrans(config, context, true);
				} else {
					transactionManager.initQueue();
					TransactionManager.threadLocalTransaction.get().push(rTXTransaction);
				}
			}
			//在try的阶段才拦截
			if(rTXTransaction != null && TransactionStatus.TRYING.equals(context.getStatus())) {
				//拦截加载事务参与者，执行try方法
				return branchProcess(pjp,context,rTXTransaction, config);
			} else if(rTXTransaction != null && TransactionStatus.CONFIRMING.equals(context.getStatus())) {
				transactionManager.commitTrans(rTXTransaction, false);
			} else if(rTXTransaction != null && TransactionStatus.CANCELING.equals(context.getStatus())) {
				transactionManager.rollbackTrans(rTXTransaction, false);
			} else {
				//2、查询不到事务存在时，即 rTXTransaction 为空时
				return pjp.proceed();
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			 //执行完后从栈中把当前事务对象删除
			 if(isNewAPPNode) {
				 Deque<Transaction> transactionDeque = TransactionManager.threadLocalTransaction.get();
				 if(transactionDeque != null) {
					 transactionDeque.pop();
					 if(transactionDeque.isEmpty()) {
						 TransactionManager.threadLocalTransaction.remove();
					 }
				 }
			 }
		}
		
		return null;
	}
	
	private Object branchProcess(ProceedingJoinPoint pjp, TransactionContext context,
			Transaction currentTransaction, TransactionConfig config) throws Throwable {
		if(currentTransaction != null) {
			addBranch(pjp, context, currentTransaction, config);
		}
		return pjp.proceed();
	}
}
