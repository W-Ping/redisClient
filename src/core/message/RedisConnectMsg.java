package core.message;

/**
 * @author liu_wp
 * @date 2018年1月8日
 * @see
 */
public class RedisConnectMsg {
	private String resultMsg;

	private String resultCode;

	private int dbIndex;

	public RedisConnectMsg() {

	}

	public int getDbIndex() {
		return dbIndex;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
}
