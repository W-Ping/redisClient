package core.netty.message;

import core.utils.MsgType;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class RedisLoginMsg extends BaseMsg {
	/** */
	private static final long serialVersionUID = 1L;

	private String host;
	private String passwrod;
	private String hostName;
	private int port;

	public RedisLoginMsg() {
		super();
	}

	public RedisLoginMsg(String host, String hostName, int port) {
		super();
		setMsgType(MsgType.LOGIN);
		this.host = host;
		this.hostName = hostName;
		this.port = port;
	}

	public RedisLoginMsg(String host, String passwrod, String hostName, int port) {
		super();
		setMsgType(MsgType.LOGIN);
		this.host = host;
		this.passwrod = passwrod;
		this.hostName = hostName;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public String getHostName() {
		return hostName;
	}

	public String getPasswrod() {
		return passwrod;
	}

	public int getPort() {
		return port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setPasswrod(String passwrod) {
		this.passwrod = passwrod;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
