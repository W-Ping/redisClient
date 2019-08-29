package core.netty.message;

import java.io.Serializable;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class AskParams implements Serializable {
	private static final long serialVersionUID = 1L;
	private String auth;

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
}
