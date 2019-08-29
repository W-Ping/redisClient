package core.message;

/**
 * @author liu_wp
 * @date 2018年1月8日
 * @see
 */
public class RedisError {
	private String errorMsg;
	private String errorType;

	public RedisError(String errorMsg) {
		super();
		this.errorMsg = errorMsg;
	}

	public RedisError(String errorMsg, String errorType) {
		super();
		this.errorMsg = errorMsg;
		this.errorType = errorType;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}
