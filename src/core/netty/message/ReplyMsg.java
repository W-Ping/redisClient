package core.netty.message;

import core.utils.MsgType;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class ReplyMsg extends BaseMsg {
	/** */
	private static final long serialVersionUID = 1L;
	private ReplyBody body;

	public ReplyMsg() {
		super();
		setMsgType(MsgType.REPLY);
	}

	public ReplyBody getBody() {
		return body;
	}

	public void setBody(ReplyBody body) {
		this.body = body;
	}
}
