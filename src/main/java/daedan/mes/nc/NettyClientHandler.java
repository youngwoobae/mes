package daedan.mes.nc;

import daedan.mes.common.service.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private Log logger = LogFactory.getLog(this.getClass());


    private String[] msgArr;
    private ByteBuffer bb; //AddOn By KMj
    private int idx = 0;
    public NettyClientHandler(String[] msgArr) {
        this.msgArr = msgArr;
    }

    public NettyClientHandler(ByteBuffer bb) { //AddOn By KMj
        this.bb = bb;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("채널 등록");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("채널 연결이 종료됨.");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("채널이 메시지 발송할 준비가 됨.");
        sendStrMsg(ctx, this.idx);
        //sendByteBufferMsg(ctx, this.idx); //AddOn By KMj
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("메시지를 받는 메소드.");
        ByteBuf buf = (ByteBuf)msg;
        int n = buf.readableBytes();
        if( n > 0 ) {
            byte[] b = new byte[n];
            buf.readBytes(b);
            //수신메시지 출력
            String receiveMsg = new String( b );
            logger.info("수신된 메시지 >" + receiveMsg);


            //byte[] bytes = new byte[buf.readableBytes()];
            //ctx.writeAndFlush( bytes ); //메시지를 발송하고 flush처리
            //final StringBuilder sb = new StringBuilder();
            //buf.readBytes(bytes);
            //sb.append(" [buffer=").append(new String(bytes));
            //sb.append(']');
            //buf.release();
            //logger.info("바이트 수신 메시지 >" + sb);




            //보낼 메시지가 없으면 연결 종료
            if(msgArr.length ==  ++this.idx) {
                channelClose(ctx);
            }else {
                sendStrMsg(ctx, this.idx);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        logger.info("메시지를 받는 동작이 끝나면 동작하는 메소드.");
    }

    private void sendStrMsg(ChannelHandlerContext ctx, int i) {

        long beforeTime = System.currentTimeMillis();

        Charset charset = Charset.forName("utf-8");
        String msg = msgArr[idx];
        ByteBuf messageBuffer = Unpooled.buffer();
        ByteBuf bb = messageBuffer.writeBytes(msg.getBytes());
        byte[] bytes = new byte[bb.readableBytes()];
        //logger.info("스트링 발송 메시지 >" + msg);

        final StringBuilder sb = new StringBuilder();
        bb.readBytes(bytes);
        sb.append(" [buffer=").append(new String(bytes));
        sb.append(']');
        bb.release();
        logger.info("바이트 발송 메시지 >" + sb);

        ctx.writeAndFlush( bytes ); //메시지를 발송하고 flush처리

    }

    private void sendByteBufferMsg(ChannelHandlerContext ctx, int i) { //AddOn By KMj
        ByteBuf messageBuffer = Unpooled.buffer();

        messageBuffer.writeBytes(bb);
        ctx.writeAndFlush( messageBuffer ); //메시지를 발송하고 flush처리
        logger.info("바이트 발송 메시지 ==> " + bb.toString());
    }

    private void channelClose(ChannelHandlerContext ctx) {
        logger.error("채널 연결 종료");
        ctx.close();
    }
}
