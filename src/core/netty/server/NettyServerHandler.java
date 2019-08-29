package core.netty.server;

import core.netty.NettyChannelMap;
import core.netty.message.AskMsg;
import core.netty.message.BaseMsg;
import core.netty.message.PingMsg;
import core.netty.message.RedisLoginMsg;
import core.netty.message.ReplyClientBody;
import core.netty.message.ReplyMsg;
import core.netty.message.ReplyServerBody;
import core.utils.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// channel失效，从Map中移除
		NettyChannelMap.removeChannel((SocketChannel) ctx.channel());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {

		if (MsgType.LOGIN.equals(baseMsg.getMsgType())) {
			RedisLoginMsg loginMsg = (RedisLoginMsg) baseMsg;
			// if ("robin".equals(loginMsg.getUserName()) &&
			// "yao".equals(loginMsg.getPassword())) {
			// 登录成功,把channel存到服务端的map中
			NettyChannelMap.add(loginMsg.getClientId(), (SocketChannel) channelHandlerContext.channel());
			System.out.println("client" + loginMsg.getClientId() + " 登录成功");
			// }
		} else {
			if (NettyChannelMap.getChannel(baseMsg.getClientId()) == null) {
				// 说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
				RedisLoginMsg loginMsg = new RedisLoginMsg();
				channelHandlerContext.channel().writeAndFlush(loginMsg);
			}
		}
		switch (baseMsg.getMsgType()) {
		case PING: {
			PingMsg pingMsg = (PingMsg) baseMsg;
			PingMsg replyPing = new PingMsg();
			NettyChannelMap.getChannel(pingMsg.getClientId()).writeAndFlush(replyPing);
		}
			break;
		case ASK: {
			// 收到客户端的请求
			AskMsg askMsg = (AskMsg) baseMsg;
			if ("authToken".equals(askMsg.getParams().getAuth())) {
				ReplyServerBody replyBody = new ReplyServerBody("server info $$$$ !!!");
				ReplyMsg replyMsg = new ReplyMsg();
				replyMsg.setBody(replyBody);
				NettyChannelMap.getChannel(askMsg.getClientId()).writeAndFlush(replyMsg);
			}
		}
			break;
		case REPLY: {
			// 收到客户端
			ReplyMsg replyMsg = (ReplyMsg) baseMsg;
			ReplyClientBody clientBody = (ReplyClientBody) replyMsg.getBody();
			System.out.println("receive client msg: " + clientBody.getClientInfo());
		}
			break;
		default:
			break;
		}
		ReferenceCountUtil.release(baseMsg);
	}
}
