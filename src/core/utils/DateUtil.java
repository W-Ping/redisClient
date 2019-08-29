package core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liu_wp
 * @date 2018年1月4日
 * @see
 */
public class DateUtil {
	private static String defaultTimeDateFmt = "YYYY-MM-DD HH:mm:ss";

	/**
	 * 获取当前日期 格式：YYYY-MM-DD HH:mm:ss
	 *
	 * @return
	 */
	public static String getLocalDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.format(DateTimeFormatter.ofPattern(defaultTimeDateFmt));

	}
}
