package daedan.mes.haccp.common.datamgr.io.server;

import daedan.mes.haccp.common.datamgr.io.SocketIoMapper;
import daedan.mes.haccp.common.datamgr.utils.BeanUtils;
import daedan.mes.haccp.common.error_handle.CustomErrorHandler;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.mapper.UserMapper;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.repository.UserRepository;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpSession;

public class SockServer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SockServer.class);

    private Environment environment;
    private Integer chatServerPort;
    private int   custNo;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();


    /**
     * 지정된 포트로 serversocket실행 (netty 라이브러리 사용)
     * @return void
     */
    @Override
    public void run() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 핸들러 설정
                            pipeline.addLast(new SockServerHandler());
                        }
                    });
            //serverPort = 19080;
            environment = (Environment) BeanUtils.getBean("environment");
            //int custNo = Integer.parseInt(environment.getProperty("cust_no").toString());
            custNo = 10;

            switch( custNo) {
                case 1: //ES연구소
                case 2: //서울식품
                case 3: //하담푸드
                case 4: //인삼명가
                case 5: //천년홍삼
                case 6: //대동고려삼
                case 7: //대단
                case 8: //영양제과
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.local").toString());
                    break;
                case 9: //미홍
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.mihong").toString());
                    break;
                case 10: //유진물산
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.youjin").toString());
                    break;
                case 11: //푸르온
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.puruon").toString());
                    break;
                case 12: //후레쉬에그푸드
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.fef").toString());
                    break;
                case 13: //한맥소프트
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.hanmac").toString());
                    break;
                case 14: //우천식품
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.ucfd").toString());
                    break;
                case 15: //정원식품
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.jwfd").toString());
                    break;
                case 16: //김치나라
                    chatServerPort = Integer.parseInt(environment.getProperty("chat.port.kimch").toString());
                    break;
                default :
                    break;
            }
            ChannelFuture future = bootstrap.bind(chatServerPort).sync(); // Chat Server Port 지정
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
