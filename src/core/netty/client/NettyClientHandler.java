package core.netty.client;

import core.netty.message.BaseMsg;
import core.netty.message.PingMsg;
import core.netty.message.RedisLoginMsg;
import core.netty.message.ReplyClientBody;
import core.netty.message.ReplyMsg;
import core.netty.message.ReplyServerBody;
import core.utils.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * @author liu_wp
 * @date 2018年1月7日
 * @see
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case WRITER_IDLE:
				PingMsg pingMsg = new PingMsg();
				ctx.writeAndFlush(pingMsg);
				System.out.println("send ping to server-");
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
		MsgType msgType = baseMsg.getMsgType();
		switch (msgType) {
		case LOGIN: {
			// 向服务器发起登录
			RedisLoginMsg loginMsg = new RedisLoginMsg();
			// loginMsg.setPassword("yao");
			// loginMsg.setUserName("robin");
			channelHandlerContext.writeAndFlush(loginMsg);
		}
			break;
		case PING: {
			System.out.println("receive ping from server-");
		}
			break;
		case ASK: {
			ReplyClientBody replyClientBody = new ReplyClientBody("client info **** !!!");
			ReplyMsg replyMsg = new ReplyMsg();
			replyMsg.setBody(replyClientBody);
			channelHandlerContext.writeAndFlush(replyMsg);
		}
			break;
		case REPLY: {
			ReplyMsg replyMsg = (ReplyMsg) baseMsg;
			ReplyServerBody replyServerBody = (ReplyServerBody) replyMsg.getBody();
			System.out.println("receive client msg: " + replyServerBody.getServerInfo());
		}
		default:
			break;
		}
		ReferenceCountUtil.release(msgType);

	}
}
