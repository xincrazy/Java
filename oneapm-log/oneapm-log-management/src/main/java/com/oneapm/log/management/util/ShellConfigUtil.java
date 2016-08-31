package com.oneapm.log.management.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;

import org.eclipse.jetty.server.Request;

import com.oneapm.log.management.constants.MessageConstants;
import com.oneapm.log.management.constants.ShellConstants;
import com.oneapm.log.management.master.operation.shell.AgentsParameter;
import com.oneapm.log.management.master.operation.shell.ShellManager;

public class ShellConfigUtil implements ShellConstants,MessageConstants {

	/**
	 * 读配置文件
	 * @param configUrl 配置文件url
	 * @return 存储配置信息的Properties
	 * @throws IOException
	 */
	public static Properties readConfig(String configUrl) {
		// 生成文件对象
		File pf = new File(USER_DIR + configUrl);
		// 生成文件输入流
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(pf);
		} catch (FileNotFoundException e) {
			LogUtil.getLogger().error(FILE_NOT_FOUND + COLONS_STR + configUrl);
			e.printStackTrace();
		}
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			LogUtil.getLogger().error(READS_THE_PROPERTIES_FAILED + COLONS_STR + configUrl);
			e.printStackTrace();
		}
		return properties;
	}

	public static String getAgentBinaryPath() {
		Properties properties = ShellConfigUtil.readConfig(DEPLOYMENT_URL);// 获取配置信息
		// 取配置值
		String agentBinaryPath = properties.getProperty(AGENT_BINARY_PATH_KEY);
		// 参数检测
		if (null == agentBinaryPath || "".equals(agentBinaryPath)) {
			String errorMessage = ALL_THE_PARAMETERS_CAN_NOT_BE_NULL;
			LogUtil.getLogger().error(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		return agentBinaryPath;
	}
	
	/**
	 * 执行线程
	 * @param shell ShellManager实例
	 * @param shellPath shell脚本路径
	 * @param timeOut 脚本超时等待时间
	 * @param parametersList shell参数集 {[agentHostName,agentUserName,agentPassword,agentBinaryPath,agentBinaryName,agentInstallationPath......]}
	 * @param poolSize 线程池容量
	 * @return 执行返回结果集
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	@SuppressWarnings("finally")
	public static List<String> runThread(ShellManager shell, String shellPath, Long timeOut,
			List<AgentsParameter> parametersList, Integer poolSize, Integer parameterSize) {
		ExecutorService exec = Executors.newFixedThreadPool(poolSize);
		List<String> results = new ArrayList<String>();// 返回结果集
		List<Future<String>> futures = new ArrayList<Future<String>>();// 充当future缓冲区
		//Set<String> hostNameSet = Collections.synchronizedSet(new HashSet<String>());
		/* 遍历参数 执行脚本 */
		for (AgentsParameter agents : parametersList) {
			String agentHostName = agents.getAgentHostName();
			String[] parameters = new String[] { agentHostName, agents.getAgentUserName(), agents.getAgentPassword(), AGENT_BINARY_PATH,
					AGENT_BINARY_NAME, agents.getAgentInstallationPath() };
			final String pathOnHost = agents.getAgentInstallationPath() + ON + agentHostName;
			Future<String> future = exec.submit(new Callable<String>() {
				public String call() throws InterruptedException {
//					if(hostNameSet.contains(agentHostName)){
//						Thread.sleep(10000);
//					}else{
//						hostNameSet.add(agentHostName);
//					}
					
					boolean result = false;
					if (null == timeOut) {
						result =  shell.runShell(shellPath, parameters, parameterSize);// 执行脚本
					}else {
						result = shell.runShell(shellPath, timeOut, parameters, parameterSize);// 执行脚本
					}
					if (result) {// 部署成功
						LogUtil.getLogger().info(SUCCESSFULLY_DEPLOY_THR_AGENT_TO + pathOnHost);//输出到日志
						return SUCCESSFULLY_DEPLOY_THR_AGENT_TO + pathOnHost;
					} else {// 部署失败
						LogUtil.getLogger().error(FAILED_DEPLOY_THE_AGENT_TO + pathOnHost);//输出到日志
						return FAILED_DEPLOY_THE_AGENT_TO + pathOnHost;
					}
				}
			});
			futures.add(future);// 防止阻塞，先存入list继续执行下一future
		}
		/* 获取Future返回值 */
		for (Future<String> f : futures) {
			while (true) {
				if (f.isDone()) {// 检查future是否执行完
					try {
						results.add(f.get());
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					} finally {
						break;
					}
				}
			}
		}
		exec.shutdown();// 关闭线程池
		return results;
	}

	/**
	 * 执行线程
	 * @param shell ShellBase实例
	 * @param shellPath shell脚本路径
	 * @param timeOut 脚本超时等待时间
	 * @param parameters shell参数 [agentHostName,agentUserName,agentPassword,agentBinaryPath,agentBinaryName,agentInstallationPath......]
	 * @param poolSize 线程池容量
	 * @return 执行返回结果
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	@SuppressWarnings("finally")
	public static Boolean runThread(ShellManager shell, String shellPath, Long timeOut, String[] parameters,
			Integer poolSize, Integer parameterSize) {
		ExecutorService exec = Executors.newFixedThreadPool(poolSize);
		Boolean result = null;// 返回结果
		/* 遍历参数 执行脚本 */
		Future<Boolean> future = exec.submit(new Callable<Boolean>() {
			public Boolean call() {
				if (null == timeOut) {
					return shell.runShell(shellPath, parameters, parameterSize);// 执行脚本
				}
				return shell.runShell(shellPath, timeOut, parameters, parameterSize);// 执行脚本
			}
		});
		/* 获取Future返回值 */
		while (true) {
			if (future.isDone()) {// 检查future是否执行完
				try {
					result = future.get();
				} catch (InterruptedException | ExecutionException e) {
					result = false;
					e.printStackTrace();
				} finally {
					break;
				}
			}
		}
		exec.shutdown();// 关闭线程池
		return result;
	}
	
	/**
	 * 获取request输入流信息
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestInfo(Request request) throws IOException {
		ServletInputStream inputStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inputStream.close();
		return outSteam.toString();
	}

	/**
	 * 正则匹配
	 * @param str  源字符串
	 * @param reg 匹配规则
	 * @return 匹配字符串
	 */
	public static String grep(String str, String reg) {
		Pattern pattern = Pattern.compile(reg);
		Matcher m = pattern.matcher(str);
		String key = null;
		while (m.find()) {
			key = m.group();
		}
		return key;
	}

}