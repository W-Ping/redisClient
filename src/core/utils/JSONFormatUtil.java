package core.utils;

/**
 * @author liu_wp
 * @date 2018年1月3日
 * @see
 */
public class JSONFormatUtil {
	/**
	 * 格式化json
	 *
	 * @param json
	 * @return
	 */
	public static String jsonFormat(String json) {
		if (json != null && json != "[]") {
			json = json.replaceAll("\\\\", "");
		} else {
			return null;
		}
		StringBuffer jsonForMatStr = new StringBuffer();
		int level = 0;
		for (int index = 0; index < json.length(); index++)// 将字符串中的字符逐个按行输出
		{
			// 获取s中的每个字符
			char c = json.charAt(index);

			// level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
			if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
				jsonForMatStr.append(getLevelStr(level));
			}
			// 遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
			switch (c) {
			case '{':
			case '[':
				jsonForMatStr.append(c + "\n");
				level++;
				break;
			case ',':
				int preIndex = index - 1;
				if (preIndex >= 0 && !"\"".equals(String.valueOf(json.charAt(preIndex)))) {
					jsonForMatStr.append(c);
				} else {
					jsonForMatStr.append(c + "\n");
				}
				break;
			case '}':
			case ']':
				jsonForMatStr.append("\n");
				level--;
				jsonForMatStr.append(getLevelStr(level));
				jsonForMatStr.append(c);
				break;
			default:
				jsonForMatStr.append(c);
				break;
			}
		}
		String result = jsonForMatStr.toString();
		if (result == null) {
			return result;
		}
		result = result.substring(1, result.lastIndexOf("\""));
		return result;
	}

	private static String getLevelStr(int level) {
		StringBuffer levelStr = new StringBuffer();
		for (int levelI = 0; levelI < level; levelI++) {
			levelStr.append("\t");
		}
		return levelStr.toString();
	}

}
