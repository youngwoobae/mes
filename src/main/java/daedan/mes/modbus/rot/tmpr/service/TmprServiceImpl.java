package daedan.mes.modbus.rot.tmpr.service;

import daedan.mes.common.service.util.StringUtil;
import daedan.mes.modbus.socket.service.SocketService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("tmprService")
public class TmprServiceImpl implements TmprService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    SocketService socketService;

    @Override
    public Map<String, Object> connect(Map<String, Object> paraMap)  {
        String tag = "TmprService.connect => ";
        Map<String,Object> rmap = new HashMap<String,Object>();

        String rotServer = paraMap.get("serverIp").toString();
        int rotServerPort = Integer.valueOf(paraMap.get("serverPort").toString());
        StringBuffer buf = new StringBuffer();
        buf.setLength(0);
        String unitId = paraMap.get("unitId").toString();
        String funCd = paraMap.get("funCd").toString();
        String offSet = paraMap.get("offSet").toString();
        String dataSz = paraMap.get("dataSz").toString();

        int[] data = new int[4];
        data[0] = Integer.parseInt(unitId);
        data[1] = Integer.parseInt(funCd);
        data[2] = Integer.parseInt(offSet);
        data[3] = Integer.parseInt(dataSz);

        String hexUnitId = StringUtil.fixLenString(Integer.toHexString(data[0]),2);
        String hexFunCd = StringUtil.fixLenString(Integer.toHexString(data[1]),2);
        String hexOffSet = StringUtil.fixLenString(Integer.toHexString(data[2]),4);
        String hexDataSz = StringUtil.fixLenString(Integer.toHexString(data[3]),4);
        buf.append(hexUnitId)
           .append(hexFunCd)
           .append(hexOffSet)
           .append(hexDataSz)
           .append(StringUtil.getCrc(buf.toString())); //식별자+함수코드+시작번지+데이터길이 + CrcCode

        String makeMsg = buf.toString().toUpperCase(); //대문자로 변경
        log.info(tag + "HEXA String = " + makeMsg); //01 03 00 00 00 0A C5  CD ->01 03 00 00 0A 0C 05 0C 05로 분개해야 함.

        /*
        byte[] rbytes = new byte[10];
        rbytes[0] = 0x1;
        rbytes[1] = 0x3;

        rbytes[2] = 0x0;
        rbytes[3] = 0x0;

        rbytes[4] = 0x0;
        rbytes[5] = 0xA;

        rbytes[6] = 0xC;
        rbytes[7] = 0x5;
        rbytes[8] = 0xC;
        rbytes[9] = 0xD;
        */

        int idx = 0;
        int byteSize = 0;
        while(idx < makeMsg.length()) {
            String hexStr = Integer.toHexString(StringUtil.hexToDecimal(makeMsg.substring(idx, idx + 2)));
            byteSize += hexStr.length();
            idx += 2;
        }
        byte[] rbytes = new byte[byteSize];
        log.info(tag + "rbytes.length = " + rbytes.length);

        idx = 0;
        int jdx = -1;
        while(idx < makeMsg.length()) {
            String hexStr = Integer.toHexString(StringUtil.hexToDecimal(makeMsg.substring(idx, idx + 2)));
            for (int kdx = 0; kdx < hexStr.length(); kdx++) {
                rbytes[++jdx] = (byte) Integer.parseInt(hexStr.substring(kdx, kdx+1),16);
                log.info("rbyte[" + jdx + "] = " + rbytes[jdx]);
            }
            idx +=2;
        }
        String recvMsg = socketService.sendByteMsgType1(rotServer,rotServerPort, rbytes);

        //여기서 수신받은 recvMsg를 jsonObject로 변환하여 rmap에 싫어 보낼 것.
        return rmap;
    }

}

