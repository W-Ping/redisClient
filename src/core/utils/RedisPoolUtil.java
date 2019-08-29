package core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import core.RedisClientWindow;
import core.message.RedisConnectMsg;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author liu_wp
 * @date 2018年1月3日
 * @see
 */
public class RedisPoolUtil {
	// Redis服务器IP
	public static String host = "192.168.1.102";
	// Redis的端口号
	public static int port = 6379;
	// 访问密码
	private static String password = "";

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 10000;

	private static int TIMEOUT = 10000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的； 设置密码时要设置为false
	private static boolean TEST_ON_BORROW = false;

	private static JedisPool jedisPool = null;
	private static RedisConnectMsg redisConnectMsg = null;

	/**
	 * 初始化Redis连接池
	 */
	static {
		init();
	}

	/**
	 * 获取Jedis实例
	 *
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		Jedis jedis = null;
		try {
			if (jedisPool != null) {
				jedis = jedisPool.getResource();
				if (StringUtils.isNotBlank(password)) {
					jedis.auth(password);
				}
				redisConnectMsg.setResultCode(CacheConstant.REDIS_CONNECT_RESULT_SUCCESS);
				redisConnectMsg.setResultMsg("redis 连接成功！");
				return jedis;
			}
		} catch (Exception e) {
			redisConnectMsg.setResultCode(CacheConstant.REDIS_CONNECT_RESULT_FAIL);
			redisConnectMsg.setResultMsg("获取redis连接失败！");
		} finally {
			returnResource(jedis);
			String msg = redisConnectMsg.getResultMsg() + "【" + host + ":" + port + "】";
			RedisClientWindow.getRedisClientWindow().getStatusLineManager().setMessage(msg);
			CacheConstant.redisConnectMsgMap.put(host, redisConnectMsg);
		}
		return null;
	}

	public static boolean init() {
		try {
			redisConnectMsg = new RedisConnectMsg();
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMaxWaitMillis(MAX_WAIT);
			config.setMaxIdle(MAX_IDLE);
			config.setTestOnBorrow(TEST_ON_BORROW);
			host = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_HOST);
			port = Integer.valueOf(CacheConstant.redisConfigMap.get(CacheConstant.REDIS_PORT));
			password = CacheConstant.redisConfigMap.get(CacheConstant.REDIS_PASSWORD);
			jedisPool = new JedisPool(config, host, port, TIMEOUT);
			return true;
		} catch (Exception e) {
			redisConnectMsg.setResultCode(CacheConstant.REDIS_CONNECT_RESULT_FAIL);
			redisConnectMsg.setResultMsg("redis配置连接错误！");
			CacheConstant.redisConnectMsgMap.put(host, redisConnectMsg);
		}
		return false;

	}

	/**
	 * 释放jedis资源
	 *
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}

	public static boolean testRedis() {
		if (redisConnectMsg == null) {
			redisConnectMsg = new RedisConnectMsg();
		}
		Jedis jedis = null;
		try {
			if (jedisPool != null) {
				jedis = jedisPool.getResource();
				if (StringUtils.isNotBlank(password)) {
					jedis.auth(password);
				}
				if ("PONG".equals(jedis.ping())) {
					redisConnectMsg.setResultCode(CacheConstant.REDIS_CONNECT_RESULT_SUCCESS);
					redisConnectMsg.setResultMsg("redis连接成功！");
					return true;
				} else {
					throw new JedisConnectionException("redis连接失败！");
				}
			}
		} catch (JedisConnectionException e) {
			redisConnectMsg.setResultCode(CacheConstant.REDIS_CONNECT_RESULT_FAIL);
			redisConnectMsg.setResultMsg(e.getMessage());
		} catch (Exception e) {
			redisConnectMsg.setResultCode(CacheConstant.REDIS_CONNECT_RESULT_FAIL);
			redisConnectMsg.setResultMsg("redis连接错误！");
		} finally {
			returnResource(jedis);
			String msg = redisConnectMsg.getResultMsg() + "【" + host + ":" + port + "】";
			RedisClientWindow.getRedisClientWindow().getStatusLineManager().setMessage(msg);
			CacheConstant.redisConnectMsgMap.put(host, redisConnectMsg);
		}
		return false;
	}

}
