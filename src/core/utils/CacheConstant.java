package core.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import core.message.RedisConnectMsg;
import core.pojo.RedisConnectPo;

/**
 * @author liu_wp
 * @date 2018年1月3日
 * @see
 */
public class CacheConstant {
	public static final String PREFIX = "prefix";
	public static final String NAMESPACE = "namespace";
	public static final String REDIS_HOST_FILE = "redis_host";
	public static final String REDIS_PREFIX_FILE = "redis_prefix";
	public static final String REDIS_SERVER_FILE = "redis_server";
	public static final String REDIS_HOST = "redis.host";
	public static final String REDIS_NAME = "redis.name";
	public static final String REDIS_PORT = "redis.port";
	public static final String REDIS_PASSWORD = "redis.password";
	public static final String REDIS_DB = "redis.db";
	public static final String REDIS_CONNECT_RESULT_SUCCESS = "success";
	public static final String REDIS_CONNECT_RESULT_FAIL = "fail";
	public static LinkedHashMap<String, String[]> ppsMap = new LinkedHashMap<>();
	public static Map<String, String> fileMap = new HashMap<>();
	public static Map<String, String> redisConfigMap = new HashMap<>();
	public static LinkedHashMap<String, RedisConnectPo> redisPoMap = new LinkedHashMap<>();
	public static String baseUrl = System.getProperty("user.dir");
	public static Map<String, RedisConnectMsg> redisConnectMsgMap = new HashMap<>();
	static {
		fileMap.put(REDIS_HOST_FILE, baseUrl + "/redis_host.properties");
		fileMap.put(REDIS_PREFIX_FILE, baseUrl + "/redis_key.properties");
		fileMap.put(REDIS_SERVER_FILE, baseUrl + "/redis_server.properties");
	}
}
