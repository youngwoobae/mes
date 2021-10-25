package daedan.mes.nc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class NettyClient {
    private Log logger = LogFactory.getLog(this.getClass());

    private Bootstrap bs = new Bootstrap();
    private SocketAddress addr_;
    private String[] msgArr;
    private ByteBuffer bb;

    public NettyClient(SocketAddress addr, String[] msgArr) {
        this.addr_ = addr;
        this.msgArr = msgArr;
    }

    public NettyClient(SocketAddress addr, ByteBuffer bb) {
        this.addr_ = addr;
        this.bb = bb;
    }

    public NettyClient(String host, int port, String[] msgArr) {
        this(new InetSocketAddress(host, port), msgArr);
    }

    public NettyClient(String host, int port, ByteBuffer bb) {
        this(new InetSocketAddress(host, port), bb);
    }


    //실제로 동작시킬 메소드 Bootstrap 연결 옵션 설정 및 연결 처리
    public void run() {
        if(this.addr_ == null) {
            logger.error("주소 정보가 없습니다.");
        }
        //else if(this.bb == null ) { //AddOn By KMJ
        //    logger.error("보낼 메시지가 없습니다.");
        //}

        else if(this.msgArr == null || this.msgArr.length == 0) {
            logger.error("보낼 메시지가 없습니다.");
        }

        bs.group(new NioEventLoopGroup(3))
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("clientHandler", new daedan.mes.nc.NettyClientHandler(msgArr));
                    }
                });

        ChannelFuture f = bs.connect(addr_);
        f.channel();
        logger.info(addr_ + " connect()");
    }
}
