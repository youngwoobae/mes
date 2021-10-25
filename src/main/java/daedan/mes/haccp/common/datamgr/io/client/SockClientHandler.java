package daedan.mes.haccp.common.datamgr.io.client;

import daedan.mes.haccp.common.datamgr.DataMgrVO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SockClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SockClientHandler.class);
    @Autowired
    private DataMgrVO dataMgrVo;

    // 클라이언트에서 패킷을 수신 하였을 경우
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//	    dataMgrVO.packetDataMgrQ.offer(msg);
    }
}
