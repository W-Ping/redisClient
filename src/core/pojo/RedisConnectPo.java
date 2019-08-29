package core.pojo;

/**
 * @author liu_wp
 * @date 2018年1月17日
 * @see
 */
public class RedisConnectPo {
	private String host;
	private Integer port;
	private Integer dbIndex;
	private String password;

	public Integer getDbIndex() {
		return dbIndex;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public Integer getPort() {
		return port;
	}

	public void setDbIndex(Integer dbIndex) {
		this.dbIndex = dbIndex;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
