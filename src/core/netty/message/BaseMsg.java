package core.netty.message;

import java.io.Serializable;

import core.utils.MsgType;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class BaseMsg implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private MsgType msgType;

	public String getClientId() {
		return clientId;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
}
