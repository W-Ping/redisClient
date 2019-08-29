package core.netty.message;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class ReplyClientBody extends ReplyBody {
	/** */
	private static final long serialVersionUID = 1L;
	private String clientInfo;

	public ReplyClientBody(String clientInfo) {
		this.clientInfo = clientInfo;
	}

	public String getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}
}
