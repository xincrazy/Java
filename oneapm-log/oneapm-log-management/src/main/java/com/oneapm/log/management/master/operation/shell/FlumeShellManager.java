package com.oneapm.log.management.master.operation.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.oneapm.log.management.constants.MessageConstants;
import com.oneapm.log.management.constants.ShellConstants;
import com.oneapm.log.management.util.LogUtil;

/**
 * 部署flume
 */
public class FlumeShellManager extends ShellManager implements ShellConstants,MessageConstants {

	/** 脚本成功执行标示 **/
	private final static String SUCCESS_STR = "#0";
	/** agent文件后缀 **/
	private final static String FILE_SUFFIX = ".tar.gz";
	/** 参数agentSourcePath下标 **/
	private final static Integer AGENT_BINARY_PATH_INDEX = 3;

	/**
	 * 脚本开始执行
	 * @param shellStr shell脚本执行字符串
	 * @return 脚本运行状态
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected Boolean execShell(String shellStr) throws IOException, InterruptedException {
		List<String> strList = new ArrayList<String>();
		Process process = null;
		try {
			Runtime.getRuntime().exec("chmod +x " + AGENT_SHELL_PATH).waitFor();//设置可执行权限
			process = Runtime.getRuntime().exec(new String[] { SHELL_TYPE_BIN_SH, _C, shellStr });
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line;
			while ((line = input.readLine()) != null) {
				strList.add(line);
			}
			int exitValue = process.waitFor();// 退出状态
			if (0 == exitValue && strList.size() > 0 && SUCCESS_STR.equals(strList.get(0))) {
				return true;
			}
		} finally {
			process.destroy();
		}
		return false;
	}

	/**
	 * 参数检测
	 * @param parameters 参数数组(agentHostName,agentUserName,agentPassword,agentBinaryPath,agentSourceName,agentInstallationPath......)
	 * @param minSize 参数最小数量
	 * @return 参数串
	 */
	@Override
	protected String checkParameters(String[] parameters, int minSize) {
		if (parameters.length >= minSize) {
			String agentBinaryPath = parameters[AGENT_BINARY_PATH_INDEX];
			File file = new File(agentBinaryPath);
			if (!file.exists()) {
				String errorMessage = FILE_DOES_NOT_EXIST + COLONS_STR + agentBinaryPath;
				LogUtil.getLogger().error(errorMessage);
				throw new RuntimeException(errorMessage);
			}
			if (!file.getName().endsWith(FILE_SUFFIX)) {
				String errorMessage = MUST_FILE_SUFFIX_TAR_GZ + COLONS_STR + agentBinaryPath;
				LogUtil.getLogger().error(errorMessage);
				throw new RuntimeException(errorMessage);
			}
		}
		return super.checkParameters(parameters, minSize);
	}

}
