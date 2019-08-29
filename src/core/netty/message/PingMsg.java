package core.netty.message;

import core.utils.MsgType;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class PingMsg extends BaseMsg {
	/** */
	private static final long serialVersionUID = 1L;

	public PingMsg() {
		super();
		setMsgType(MsgType.PING);
	}
}
