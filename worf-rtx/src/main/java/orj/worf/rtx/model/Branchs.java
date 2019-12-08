package orj.worf.rtx.model;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import orj.worf.rtx.util.BeanFactoryAdapter;

public class Branchs implements Serializable {

	private static final long serialVersionUID = 2190041601782169250L;

	//事务ID
	private String id;
	//项目名称      项目名称是当前项目则在此项目调用，防止当前节点调不到此分支的方法
	private String projectName;
	//RMC 事务时， 只需要此方法， 不需要 cancelMethod
	private InvokeMethod confirmMethod;
	
	private InvokeMethod cancelMethod;
	
	public void commit() {
		invoke(confirmMethod);
	}
	
	public void rollback() {
		invoke(cancelMethod);
	}
	
	private Object invoke(InvokeMethod methodObj) {
		// TODO Auto-generated method stub
        if (!StringUtils.isBlank(methodObj.getMethodName())) {
            try {
                Object target = BeanFactoryAdapter.getBean(Class.forName(methodObj.getClassName()));
                Method method = null;
                // 找到要调用的目标方法
                method = target.getClass().getMethod(methodObj.getMethodName(), methodObj.getParameterTypes());
                // 调用服务方法，被再次被TccTransactionContextAspect和ResourceCoordinatorInterceptor拦截，但因为事务状态已经不再是TRYING了，所以直接执行远程服务
                return method.invoke(target, methodObj.getArgs()); // 调用服务方法
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public InvokeMethod getConfirmMethod() {
		return confirmMethod;
	}

	public void setConfirmMethod(InvokeMethod confirmMethod) {
		this.confirmMethod = confirmMethod;
	}

	public InvokeMethod getCancelMethod() {
		return cancelMethod;
	}

	public void setCancelMethod(InvokeMethod cancelMethod) {
		this.cancelMethod = cancelMethod;
	}
	
}
