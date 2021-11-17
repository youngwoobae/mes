package daedan.mes.cmmn.io.socket.client;

import daedan.mes.common.error_handle.CustomErrorHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("sockClient")
public class SockClient implements Runnable {
    //private Log log = LogFactory.getLog(this.getClass());
    private static final Logger log = LoggerFactory.getLogger(SockClient.class);
    private final EventLoopGroup group = new NioEventLoopGroup();;
    private final Bootstrap bootstrap = new Bootstrap();;
    private String host;
    private Integer port;

    // 클라이언트 소켓 정보 설정

    public void makeSockClient(String host, Integer port) {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new SockClientHandler());
                    }
                });
        this.host = host;
        this.port = port;
    }

    // 서버 소켓으로 연결 시도
    public void connect(final String host, final int port) {
        String tag = "SockClient.connect=>";
        try {
            log.info(tag + "connect to try.. {}:{}", host, port);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info(tag + "connect success!! {}:{}", host, port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            try {
                log.error("connect to fail.. {}:{}", host, port);
                Thread.sleep(10000);
                connect(host, port);
            } catch (InterruptedException e1) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e1);
            }

        }
    }

    // 클라이언트 소켓이 종료 되었을 경우
    public void close() {
        log.info("close client...");
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    // 서버와 클라이언트간의 소켓 연결 쓰레드
    public void run() {

        while(true) {
            connect(host, port);
        }
    }
}
