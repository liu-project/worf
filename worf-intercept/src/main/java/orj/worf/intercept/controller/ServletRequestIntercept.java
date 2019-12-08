package orj.worf.intercept.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.core.util.ApplicationContextHolder;
import orj.worf.intercept.constant.InterceptResultEnum;
import orj.worf.intercept.dto.ResultDTO;
import orj.worf.intercept.service.InterceptService;
import orj.worf.intercept.util.JsonUtil;
/**
 * servlet 自定义拦截器
 * @author linruzhou
 *
 */
public class ServletRequestIntercept implements Filter {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private InterceptService interceptService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			interceptService = ApplicationContextHolder.get().getBean(InterceptService.class);
			//拦截处理
			ResultDTO resultDTO = interceptService.intercept((HttpServletRequest)request);
			//结果判段
			if(resultDTO.getStatus() == InterceptResultEnum.success.getStatus()) {
				chain.doFilter(request, response);
			} else {
				HttpServletResponse httpResponse = (HttpServletResponse)response;
				PrintWriter printWriter = null;
				try {
					httpResponse.setContentType("application/json; charset=utf-8");
					printWriter = httpResponse.getWriter();
					httpResponse.getWriter().print(JsonUtil.objectToJsonStr(resultDTO));
					return;
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(printWriter != null) {
						printWriter.close();
					}
				}
			}
		} catch (Throwable e) {
//			chain.doFilter(request, response);
			writeErrorResp(response);
			logger.error("{} got an unhandled exception", ((HttpServletRequest)request).getRequestURI(), e);
		}
		
	}

	private void writeErrorResp(ServletResponse response) {
		ResultDTO resultDTO = new ResultDTO(9999, "系统正在加载中……", null);
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		PrintWriter printWriter = null;
		try {
			httpResponse.setContentType("application/json; charset=utf-8");
			printWriter = httpResponse.getWriter();
			httpResponse.getWriter().print(JsonUtil.objectToJsonStr(resultDTO));
			return;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if(printWriter != null) {
				printWriter.close();
			}
		}
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
