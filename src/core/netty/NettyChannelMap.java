package core.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class NettyChannelMap {
	private static Map<String, SocketChannel> nettyChannelMap = new ConcurrentHashMap<>();

	public static void add(String clientId, SocketChannel socketChannel) {
		nettyChannelMap.put(clientId, socketChannel);
	}

	public static Channel getChannel(String clientId) {
		return nettyChannelMap.get(clientId);
	}

	@SuppressWarnings("rawtypes")
	public static void removeChannel(SocketChannel socketChannel) {
		for (Map.Entry entry : nettyChannelMap.entrySet()) {
			if (entry.getValue() == socketChannel) {
				nettyChannelMap.remove(entry.getKey());
			}
		}
	}
}
