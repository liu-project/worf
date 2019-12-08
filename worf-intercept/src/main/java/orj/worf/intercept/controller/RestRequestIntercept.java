package orj.worf.intercept.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.core.util.ApplicationContextHolder;
import orj.worf.intercept.constant.InterceptResultEnum;
import orj.worf.intercept.dto.ResultDTO;
import orj.worf.intercept.service.InterceptService;
import orj.worf.intercept.util.JsonUtil;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;

/**
 * dubbox 自定义拦截器
 * @author linruzhou
 *
 */
public class RestRequestIntercept implements Filter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private InterceptService interceptService;
	
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {

		Result result = new RpcResult();
		try {	
			HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest();
			
			interceptService = ApplicationContextHolder.get().getBean(InterceptService.class);
			//拦截处理
			ResultDTO resultDTO = interceptService.intercept(request);
			//结果判段
			if(resultDTO.getStatus() == InterceptResultEnum.success.getStatus()) {
				result = invoker.invoke(invocation);
			} else {
				HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse();
				PrintWriter printWriter = null;
				try {
					response.setContentType("application/json; charset=utf-8");
					printWriter = response.getWriter();
					response.getWriter().print(JsonUtil.objectToJsonStr(resultDTO));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(printWriter != null) {
						printWriter.close();
					}
				}
			}
		} catch (Throwable e) {
//			result = invoker.invoke(invocation);
			writeErrorResp();
			HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest();
			logger.error("{} got an unhandled exception", request.getRequestURI(), e);
		}
		return result;
	}
	
	private void writeErrorResp() {
		ResultDTO resultDTO = new ResultDTO(9999, "系统正在加载中……", null);
		HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse();
		PrintWriter printWriter = null;
		try {
			response.setContentType("application/json; charset=utf-8");
			printWriter = response.getWriter();
			response.getWriter().print(JsonUtil.objectToJsonStr(resultDTO));
			return;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if(printWriter != null) {
				printWriter.close();
			}
		}
	}

}
