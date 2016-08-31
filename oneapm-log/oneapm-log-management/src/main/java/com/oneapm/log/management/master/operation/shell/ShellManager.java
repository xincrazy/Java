package com.oneapm.log.management.master.operation.shell;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.oneapm.log.management.constants.MessageConstants;
import com.oneapm.log.management.constants.ShellConstants;
import com.oneapm.log.management.util.LogUtil;

/**
 * 
 */
public abstract class ShellManager implements ShellConstants,MessageConstants {

	/**
	 * 执行脚本
	 * @param shellPath shell 脚本路径
	 * @param parameters 参数数组
	 * @param parameteSize 参数数量
	 * @return 脚本运行状态
	 */
	public Boolean runShell(String shellPath, String[] parameters, Integer parameteSize) {
		String parameter = checkParameters(parameters, parameteSize);
		String shellStr = shellPath + EMPTY_STR + parameter;// 拼接参数
		try {
			return execShell(shellStr);// 执行脚本
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 执行脚本（控制脚本执行时间）
	 * @param shellPath shell脚本路径
	 * @param timeOut 超时等待时间（单位：秒）（默认两分钟）
	 * @param parameters 参数数组
	 * @param parameteSize 参数数量
	 * @return 脚本运行状态
	 */
	public Boolean runShell(String shellPath, Long timeOut, String[] parameters, Integer parameteSize) {
		String parameter = checkParameters(parameters, parameteSize);// 参数检测
		if (timeOut == null || timeOut <= 0) {
			timeOut = DEFAULT_SHELL_EXECUTION_TIMEOUT;
		}
		String shellStr = shellPath + EMPTY_STR + parameter;// 拼接参数
		ExecutorService exec = Executors.newFixedThreadPool(1);
		Callable<Boolean> call = new Callable<Boolean>() {
			public Boolean call() {
				try {
					return execShell(shellStr);// 执行脚本
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}
		};
		try {
			Future<Boolean> future = exec.submit(call);
			Boolean obj = future.get(timeOut, TimeUnit.SECONDS); // 任务处理超时时间
			return obj;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		} finally {
			if (exec != null) {
				exec.shutdown();// 关闭线程池
			}
		}
		return false;
	}

	/**
	 * 参数检测
	 * @param parameters  参数数组
	 * @param minSize  参数最小数量
	 * @return 参数串
	 */
	protected String checkParameters(String[] parameters, int minSize) {
		if (parameters.length < minSize) {
			String errorMeassage = PARAMETERS_OF_AT_LEAST + minSize;
			LogUtil.getLogger().error(errorMeassage);
			throw new RuntimeException(errorMeassage);
		}
		StringBuffer sbf = new StringBuffer();
		for (String str : parameters) {
			if (null == str || "".equals(str)) {
				String errorMeassage = ALL_THE_PARAMETERS_CAN_NOT_BE_NULL;
				LogUtil.getLogger().error(errorMeassage);
				throw new RuntimeException(errorMeassage);
			}
			sbf.append(str + EMPTY_STR);
		}
		return sbf.toString();
	}

	/**
	 * 执行脚本
	 * @param shellStr shell脚本执行字符串
	 * @return 脚本运行状态
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected abstract Boolean execShell(String shellStr) throws IOException, InterruptedException;
}
