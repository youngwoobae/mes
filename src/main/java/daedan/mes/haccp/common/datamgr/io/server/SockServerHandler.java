package daedan.mes.haccp.common.datamgr.io.server;

import daedan.mes.haccp.common.datamgr.DataMgrVO;
import daedan.mes.haccp.common.datamgr.utils.BeanUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class SockServerHandler extends ChannelInboundHandlerAdapter {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    //@Autowired
    private DataMgrVO dataMgrVo;

    /**
     * 각 클라이언트서버에서 수신한 메세지를 공통 Queue에 저장
     *
     * param Object msg
     * return void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String tag = "SocketServerHandler.channelRead => ";
        Object[] obj = new Object[2];
        obj[0] = ctx;
        obj[1] = msg;
        dataMgrVo = (DataMgrVO) BeanUtils.getBean("dataMgrVO");
        log.info(tag + "received data.ctx = " + ctx);
        log.info(tag + "received data.msg = " + msg);
        log.info(tag +"---------------------------------------------------------------------------------------------");
        dataMgrVo.packetDataMgrQ.offer(obj);
    }
}
