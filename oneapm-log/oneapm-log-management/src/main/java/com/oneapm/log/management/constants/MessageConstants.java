package com.oneapm.log.management.constants;

public interface MessageConstants {

	// ********错误信息***********//
	/** 文件没找到 **/
	public final static String FILE_NOT_FOUND = "File not found";
	/** 读取配置失败 **/
	public final static String READS_THE_PROPERTIES_FAILED = "Reads the properties failed";

	// ********校验信息***********//
	/** 文件不存在 **/
	public final static String FILE_DOES_NOT_EXIST = "File does not exist";
	/** 文件后缀必须为.tar.gz **/
	public final static String MUST_FILE_SUFFIX_TAR_GZ = "Must file suffix .tar.gz";
	/** 参数数量最少为 **/
	public final static String PARAMETERS_OF_AT_LEAST = "Please check the number of parameters , the parameters of at least ";
	/** 所有参数不能为空 **/
	public final static String ALL_THE_PARAMETERS_CAN_NOT_BE_NULL = "Please check the parameters ,all the parameters can not be null";
	/** 参数不全，不能部署 **/
	public final static String PARAMETERS_INCOMPLETE_CAN_NOT_BE_DEPLOYED = "Parameters incomplete, can not be deployed";
	/**JSON不能解析 **/
	public final static String JSON_CAN_NOT_BE_RESOLVED = "Json can not be resolved";
	/**JSON格式不正确 **/
	public final static String JSON_FORMAT_IS_INCORRECT = "Json format is incorrect, it must '[' start, and with ']' to end";
	/** request info不能为空 **/
	public final static String REQUEST_CAN_NOT_BE_NULL = "Request info can not be null";
	
	// ********部署信息***********//
	/** 部署成功在 **/
	public final static String SUCCESSFULLY_DEPLOY_THR_AGENT_TO = "Successfully deploy the agent to ";
	/** 部署失败在 **/
	public final static String FAILED_DEPLOY_THE_AGENT_TO = "Failed deploy the agent to ";

}
