package com.oneapm.log.management.constants;

import com.oneapm.log.management.util.ShellConfigUtil;

public interface ShellConstants {
	// ********脚本相关***********//
	/** 脚本执行默认超时时间 **/
	public final static Long DEFAULT_SHELL_EXECUTION_TIMEOUT = 120L;
	/** 脚本执行方式 **/
	public final static String SHELL_TYPE_BIN_SH = "/bin/sh";
	/** 脚本执行参数-c **/
	public final static String _C = "-c";
	/** 当前目录 **/
	public final static String USER_DIR = System.getProperty("user.dir");
	/** agentshell可执行脚本路径 **/
	public final static String AGENT_SHELL_PATH = USER_DIR + "/sbin/agentshell/agentDeploymentMain.sh";
	/** 脚本执行最大并发数 **/
	public final static Integer POOL_MAX_SIZE = 10;
	/** agent脚本参数个数 **/
	public final static Integer AGENT_PARAMETER_SIZE = 6;
	/** 脚本执行超时时间 **/
	public final static Long SHELL_EXECUTION_TIMEOUT  = 120L;

	// ********配置相关***********//
	/** deployment url **/
	public final static String DEPLOYMENT_URL = "/conf/deployment.properties";
	/** agent.parameters url **/
	public final static String AGENT_PARAMETERS_URL = "/conf/agent.parameters.properties";
	/** agentBinaryPath value **/
	public final static String AGENT_BINARY_PATH = ShellConfigUtil.getAgentBinaryPath();
	/** agentBinaryName value **/
	public final static String AGENT_BINARY_NAME = AGENT_BINARY_PATH.substring(AGENT_BINARY_PATH.lastIndexOf("/") + 1);// 获取文件名

	// ********参数相关***********//
	/** 半角冒号字符串（:） **/
	public final static String COLONS_STR = ":";
	/** 空字符的字符串（" "） **/
	public final static String EMPTY_STR = " ";
	/**换行符\n**/
	public final static String _N = "\n";
	/**换行符\n**/
	public final static String ON = " on ";
	/** agentSourcePath key **/
	public final static String AGENT_BINARY_PATH_KEY = "agentBinaryPath";
	/** 参数名agentHostName **/
	public final static String PARAMETER_AGENT_HOST_NAME = "agentHostName";
	/** 参数名agentUserName **/
	public final static String PARAMETER_AGENT_USER_NAME = "agentUserName";
	/** 参数名agentPassword **/
	public final static String PARAMETER_AGENT_PASS_WORD = "agentPassword";
	/** 参数名agentInstallationPath **/
	public final static String PARAMETER_AGENT_INSTALLATION_PATH = "agentInstallationPath";
	/** agents 匹配规则（多对） **/
	public final static String AGENTS_GREP_PAIRS = "\\[\\{.*\\}\\]";
	/** agents 匹配规则（单个） **/
	public final static String AGENTS_GREP_SINGLE = "\\{.*\\}";

}
