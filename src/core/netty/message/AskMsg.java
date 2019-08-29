package core.netty.message;

import core.utils.MsgType;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class AskMsg extends BaseMsg {
	/** */
	private static final long serialVersionUID = 1L;
	private AskParams params;

	public AskMsg() {
		super();
		setMsgType(MsgType.ASK);
	}

	public AskParams getParams() {
		return params;
	}

	public void setParams(AskParams params) {
		this.params = params;
	}
}
