package com.oneapm.log.management.master.flume.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.alibaba.fastjson.JSON;
import com.oneapm.log.management.constants.HtmlConstants;
import com.oneapm.log.management.constants.MessageConstants;
import com.oneapm.log.management.constants.ShellConstants;
import com.oneapm.log.management.master.operation.shell.FlumeShellManager;
import com.oneapm.log.management.master.operation.shell.AgentsParameter;
import com.oneapm.log.management.util.LogUtil;
import com.oneapm.log.management.util.ShellConfigUtil;

public class DeployAgentsHandler extends AbstractHandler implements ShellConstants,MessageConstants,HtmlConstants {
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		
		String requestStr = ShellConfigUtil.getRequestInfo(baseRequest);//获取request输入流信息
		if(null == requestStr || "".equals(requestStr)){
			String errorMessage = REQUEST_CAN_NOT_BE_NULL;
			LogUtil.getLogger().error(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		String jsonStr = ShellConfigUtil.grep(requestStr.replaceAll(_N, ""), AGENTS_GREP_PAIRS);//获取json串
		if(null == jsonStr || "".equals(jsonStr)){
			String errorMessage = JSON_FORMAT_IS_INCORRECT;
			LogUtil.getLogger().error(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		List<AgentsParameter> parameterList = JSON.parseArray(jsonStr, AgentsParameter.class);
		if (null == parameterList) {
			String errorMessage = JSON_CAN_NOT_BE_RESOLVED;
			LogUtil.getLogger().error(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		for (AgentsParameter agents : parameterList) {
			if (null == agents.getAgentHostName() || null == agents.getAgentInstallationPath()
					|| null == agents.getAgentPassword() || null == agents.getAgentUserName()
					|| "".equals(agents.getAgentHostName()) || "".equals(agents.getAgentInstallationPath())
					|| "".equals(agents.getAgentPassword()) || "".equals(agents.getAgentUserName())) {
				String errorMessage = PARAMETERS_INCOMPLETE_CAN_NOT_BE_DEPLOYED;
				LogUtil.getLogger().error(errorMessage);
				throw new RuntimeException(errorMessage);
			}
		}
		List<String> resultList = ShellConfigUtil.runThread(new FlumeShellManager(), AGENT_SHELL_PATH,
				SHELL_EXECUTION_TIMEOUT, parameterList, POOL_MAX_SIZE, AGENT_PARAMETER_SIZE);
		for (String result : resultList) {
			out.write(result + _N);
		}
		out.flush();
		out.close();
	}

}
