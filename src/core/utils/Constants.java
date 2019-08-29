package core.utils;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class Constants {

	private static String clientId;

	private static boolean ignore;
	private static boolean queryLike;

	public static String getClientId() {
		return clientId;
	}

	public static boolean isIgnore() {
		return ignore;
	}

	public static boolean isQueryLike() {
		return queryLike;
	}

	public static void setClientId(String clientId) {
		Constants.clientId = clientId;
	}

	public static void setIgnore(boolean ignore) {
		Constants.ignore = ignore;
	}

	public static void setQueryLike(boolean queryLike) {
		Constants.queryLike = queryLike;
	}

}
