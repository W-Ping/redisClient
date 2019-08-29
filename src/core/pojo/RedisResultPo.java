package core.pojo;

/**
 * @author liu_wp
 * @date 2018年1月18日
 * @see
 */
public class RedisResultPo {
	private Integer dbIndex;
	private Object result;
	private Long expireTime;
	private String type;
	private String host;

	public Integer getDbIndex() {
		return dbIndex;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public String getHost() {
		return host;
	}

	public Object getResult() {
		return result;
	}

	public String getType() {
		return type;
	}

	public void setDbIndex(Integer dbIndex) {
		this.dbIndex = dbIndex;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setType(String type) {
		this.type = type;
	}

}
