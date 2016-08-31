package com.oneapm.log.management.master.flume.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.oneapm.log.management.constants.HtmlConstants;
import com.oneapm.log.management.constants.MessageConstants;
import com.oneapm.log.management.constants.ShellConstants;
import com.oneapm.log.management.master.operation.shell.FlumeShellManager;
import com.oneapm.log.management.util.LogUtil;
import com.oneapm.log.management.util.ShellConfigUtil;

public class DeployAgentHandler extends AbstractHandler implements ShellConstants,MessageConstants,HtmlConstants {
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// 获取http参数值
		String agentHostName = baseRequest.getParameter(PARAMETER_AGENT_HOST_NAME);
		String agentUserName = baseRequest.getParameter(PARAMETER_AGENT_USER_NAME);
		String agentPassword = baseRequest.getParameter(PARAMETER_AGENT_PASS_WORD);
		String agentInstallationPath = baseRequest.getParameter(PARAMETER_AGENT_INSTALLATION_PATH);

		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		// 添加参数
		if (null == agentHostName || null == agentUserName || null == agentPassword || null == agentInstallationPath
				|| "".equals(agentHostName) || "".equals(agentUserName) || "".equals(agentPassword)
				|| "".equals(agentInstallationPath)) {
			String errorMessage = PARAMETERS_INCOMPLETE_CAN_NOT_BE_DEPLOYED;
			LogUtil.getLogger().error(errorMessage);
			out.write(errorMessage + HTML_BR);
		} else {
			// 脚本参数
			String[] parameters = new String[] { agentHostName, agentUserName, agentPassword, AGENT_BINARY_PATH,
					AGENT_BINARY_NAME, agentInstallationPath };
			// 执行脚本，获取执行结果
			Boolean result = ShellConfigUtil.runThread(new FlumeShellManager(), AGENT_SHELL_PATH, SHELL_EXECUTION_TIMEOUT, parameters,
					POOL_MAX_SIZE, AGENT_PARAMETER_SIZE);
			// 输出到日志
			final String pathOnHost = agentInstallationPath +ON +agentHostName;
			// 输出到前端
			if (result) {// 部署成功
				LogUtil.getLogger().info(SUCCESSFULLY_DEPLOY_THR_AGENT_TO + pathOnHost);
				out.write(SUCCESSFULLY_DEPLOY_THR_AGENT_TO + pathOnHost + HTML_BR);
			} else {// 部署失败
				LogUtil.getLogger().error(FAILED_DEPLOY_THE_AGENT_TO + pathOnHost);
				out.write(FAILED_DEPLOY_THE_AGENT_TO + pathOnHost + HTML_BR);
			}
		}
		out.flush();
		out.close();
	}
}
