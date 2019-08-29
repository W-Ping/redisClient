package core.action;

import org.eclipse.jface.action.Action;

import core.pojo.RedisConnectPo;

/**
 * @author liu_wp
 * @date 2018年1月17日
 * @see
 */
public class RedisServerAction extends Action {
	private String linkTest;
	private RedisConnectPo redisConnectPo;

	public RedisServerAction(String linkText, RedisConnectPo redisConnectPo) {
		this.linkTest = linkText;
		this.redisConnectPo = redisConnectPo;
	}

	public String getLinkTest() {
		return linkTest;
	}

	public RedisConnectPo getRedisConnectPo() {
		return redisConnectPo;
	}

	public void setLinkTest(String linkTest) {
		this.linkTest = linkTest;
	}

	public void setRedisConnectPo(RedisConnectPo redisConnectPo) {
		this.redisConnectPo = redisConnectPo;
	}
}
