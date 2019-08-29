package core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import core.RedisClientWindow;

/**
 * @author liu_wp
 * @date 2018年1月3日
 * @see
 */
public class PropertiesUtil {
	private static Logger logger = Logger.getLogger(PropertiesUtil.class);

	public static String fileReplacePath(String filePath) {
		if (isWindowSystem()) {
			filePath = filePath.replace("/", "\\");
		} else {
			filePath = filePath.replace("\\", "/");
		}
		return filePath;
	}

	/**
	 * 初始化redis连接文件
	 */
	public static void initReisHostProperties(String filekey) {
		InputStream in = null;
		OutputStream out = null;
		Properties properties = new Properties();
		try {
			String fileUrl = CacheConstant.fileMap.get(filekey);
			File file = new File(fileUrl);
			if (!file.exists()) {
				file.createNewFile();
			}
			in = new FileInputStream(file);
			properties.load(in);
			Enumeration<?> enum1 = properties.propertyNames();
			while (enum1.hasMoreElements()) {
				String strKey = (String) enum1.nextElement();
				String value = properties.getProperty(strKey);
				CacheConstant.redisConfigMap.put(strKey, value);
			}
		} catch (Exception e) {
			logger.error(e.getClass() + "==>" + e.getMessage());
			messageOpen("初始化redis连接错误： " + e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 初始化 redis命名空间和key前缀文件
	 */
	public static void initReisKeyProperties() {
		InputStream in = null;
		OutputStream out = null;
		Properties properties = new Properties();
		try {
			String fileUrl = CacheConstant.fileMap.get(CacheConstant.REDIS_PREFIX_FILE);
			fileUrl = fileReplacePath(fileUrl);
			File file = new File(fileUrl);
			if (!file.exists()) {
				file.createNewFile();
			}
			in = new FileInputStream(file);
			properties.load(in);
			Enumeration<?> enum1 = properties.propertyNames();
			Set<String> preSet = new HashSet<>();
			Set<String> namespaceSet = new HashSet<>();
			while (enum1.hasMoreElements()) {
				String strKey = (String) enum1.nextElement();
				String value = properties.getProperty(strKey);
				if (strKey.startsWith(CacheConstant.PREFIX)) {
					preSet.add(value);
				} else if (strKey.startsWith(CacheConstant.NAMESPACE)) {
					namespaceSet.add(value);
				}

			}
			String[] namespaceArr = new String[namespaceSet.size()];
			namespaceSet.toArray(namespaceArr);
			CacheConstant.ppsMap.put(CacheConstant.NAMESPACE, namespaceArr);
			String[] prefixArr = new String[preSet.size()];
			preSet.toArray(prefixArr);
			CacheConstant.ppsMap.put(CacheConstant.PREFIX, prefixArr);
		} catch (Exception e) {
			logger.error(e.getClass() + "==>" + e.getMessage());
			messageOpen("初始化redis模板错误： " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public static void messageOpen(String message) {
		MessageDialog.openWarning(RedisClientWindow.getRedisClientWindow().getShell(), "断点", message);
	}

	public static Map<String, String> readPropertiesByFile(String fileKey) {
		Map<String, String> propertiesMap = new HashMap<>();
		InputStream in = null;
		Properties properties = new Properties();
		try {
			String url = CacheConstant.fileMap.get(fileKey);
			url = fileReplacePath(url);
			URL fileURL = PropertiesUtil.class.getResource(url);
			File file = null;
			if (fileURL == null) {
				file = new File(url);
			} else {
				file = new File(fileURL.getPath());
			}
			if (!file.exists()) {
				file.createNewFile();
				in = PropertiesUtil.class.getResourceAsStream(url);
			} else {
				in = new FileInputStream(file);
			}
			properties.load(in);
			Enumeration<?> enum1 = properties.propertyNames();
			while (enum1.hasMoreElements()) {
				String strKey = (String) enum1.nextElement();
				String value = properties.getProperty(strKey);
				propertiesMap.put(strKey, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return propertiesMap;
	}

	public static boolean writeAppendProperties(Map<String, String> map, String fileKey) {
		return writeProperties(map, fileKey, true);
	}

	public static boolean writeNonAppendProperties(Map<String, String> map, String fileKey) {
		return writeProperties(map, fileKey, false);
	}

	/**
	 * 写入 Properties文件
	 *
	 * @param pMap
	 * @param fileKey
	 * @param isAppend
	 */
	public static boolean writeProperties(Map<String, String> pMap, String fileKey, boolean isAppend) {
		OutputStream out = null;
		InputStream in = null;
		Properties properties = new Properties();
		try {
			String fileUrl = CacheConstant.fileMap.get(fileKey);
			fileUrl = fileReplacePath(fileUrl);
			out = new FileOutputStream(fileUrl, isAppend);
			boolean isPrefix = fileKey.equals(CacheConstant.REDIS_PREFIX_FILE);
			boolean isHost = fileKey.equals(CacheConstant.REDIS_HOST_FILE);
			String describe = null;
			if (isPrefix) {
				describe = "redis-key template";
				splitRedisFileValue(properties, pMap);
			} else {
				if (isHost) {
					describe = "redis-connection server";
				}
				for (Map.Entry<String, String> entry : pMap.entrySet()) {
					properties.setProperty(entry.getKey(), entry.getValue());
					if (isHost) {
						CacheConstant.redisConfigMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
			properties.store(out, describe);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private static boolean isWindowSystem() {
		return System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
	}

	private static void splitRedisFileValue(Properties properties, Map<String, String> pMap) {
		Set<String> preSet = new HashSet<>();
		Set<String> namespaceSet = new HashSet<>();
		for (Map.Entry<String, String> entry : pMap.entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
			if (entry.getKey().startsWith(CacheConstant.PREFIX)) {
				preSet.add(entry.getValue());
			} else if (entry.getKey().startsWith(CacheConstant.NAMESPACE)) {
				namespaceSet.add(entry.getValue());
			}

		}
		if (namespaceSet.size() > 0) {
			String[] namespaceArr = CacheConstant.ppsMap.get(CacheConstant.NAMESPACE);
			String[] addNamespaceArr = new String[namespaceSet.size()];
			namespaceSet.toArray(addNamespaceArr);
			String[] newNamespaceArr = new String[namespaceArr.length + addNamespaceArr.length];
			System.arraycopy(addNamespaceArr, 0, newNamespaceArr, 0, addNamespaceArr.length);
			System.arraycopy(namespaceArr, 0, newNamespaceArr, addNamespaceArr.length, namespaceArr.length);
			CacheConstant.ppsMap.put(CacheConstant.NAMESPACE, newNamespaceArr);
		}
		if (preSet.size() > 0) {
			String[] prefixArr = CacheConstant.ppsMap.get(CacheConstant.PREFIX);
			String[] addPrefixArr = new String[preSet.size()];
			preSet.toArray(addPrefixArr);
			String[] newPrefixArr = new String[prefixArr.length + addPrefixArr.length];
			System.arraycopy(addPrefixArr, 0, newPrefixArr, 0, addPrefixArr.length);
			System.arraycopy(prefixArr, 0, newPrefixArr, addPrefixArr.length, prefixArr.length);
			CacheConstant.ppsMap.put(CacheConstant.PREFIX, newPrefixArr);
		}

	}
}
