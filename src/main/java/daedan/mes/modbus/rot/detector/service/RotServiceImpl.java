package daedan.mes.modbus.rot.detector.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.domain.EquipMngrHstr;
import daedan.mes.equip.repository.EquipMngrHstrRepository;
import daedan.mes.modbus.socket.service.SocketService;

import daedan.mes.spot.domain.SpotEquip;
import daedan.mes.spot.service.SpotEquipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("dectorService")
public class RotServiceImpl implements RotService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    SocketService socketService;

    @Autowired
    SpotEquipService spotEquipService;

    @Autowired
    EquipMngrHstrRepository equipMngrHstrRepo;

    /*하담푸드 포장기*/
    @Override
    public String modbusRotRead(Map<String, Object> paraMap) {
        String tag = "RotService.modbusRotRead => ";
        //여기서 수신받은 recvMsg를 jsonObject로 변환하여 rmap에 싫어 보낼 것.
        return this.modbusRotReadByType1(paraMap);
    }



    /*하담푸드 금속검출기*/
    private String modbusRotReadByType1(Map<String, Object> paraMap) {
        String tag = "RotService.modbusRotReadByType1 => ";

        String rotServer = paraMap.get("deviceIp").toString();
        int rotServerPort = Integer.valueOf(paraMap.get("devicePort").toString());
        //log.info(tag + "rotServer = " + rotServer);
        //log.info(tag + "rotServerPort = " + rotServerPort);

        String curDate = DateUtils.getCurrentBaseDateString();
        int yy = Integer.parseInt(curDate.substring(2,4));
        int mm = Integer.parseInt(curDate.substring(4,6));
        int dd = Integer.parseInt(curDate.substring(6,8));
        String hexY = StringUtil.fixLenString(Integer.toHexString(yy),2);
        String hexM = StringUtil.fixLenString(Integer.toHexString(mm),2);
        String hexD = StringUtil.fixLenString(Integer.toHexString(dd),2);
        byte[] termBytes = new byte[3];
        termBytes[0] = (byte) Integer.parseInt(hexY,16);
        termBytes[1] = (byte) Integer.parseInt(hexM,16);
        termBytes[2] = (byte) Integer.parseInt(hexD,16);

        StringBuffer buf = new StringBuffer();
        buf.append(StringUtil.fixLenString(Integer.toHexString(2),2))//stx : 2 =  0x02
                .append(StringUtil.fixLenString(Integer.toHexString(12),4))//length-last bit
                .append(StringUtil.fixLenString(Integer.toHexString(58),2))//cmd : 58 = 0x3a
                .append(StringUtil.fixLenString(Integer.toHexString(yy),2)) //startYY : 현재년(2자리)
                .append(StringUtil.fixLenString(Integer.toHexString(mm),2)) //startMN : 현재년(2자리)
                .append(StringUtil.fixLenString(Integer.toHexString(dd),2)) //startDD : 현재년(2자리)
                .append(StringUtil.fixLenString(Integer.toHexString(yy),2)) //endYY : 현재년(2자리)
                .append(StringUtil.fixLenString(Integer.toHexString(mm),2)) //endMM : 현재년(2자리)
                .append(StringUtil.fixLenString(Integer.toHexString(dd),2)) //endDD : 현재년(2자리)
                .append(StringUtil.fixLenString(Integer.toHexString(3),2)); //etx : 3 =  0x03
        String makeMsg = buf.toString().toUpperCase();
        log.info(tag + "HEXA SENDING STR WITH LRC = " + makeMsg);

        //LRC 생성 시작...
        int idx = 0;
        int lrc = 0;
        while(true) {
            try {
                String hexStr = StringUtil.fixLenString(Integer.toHexString(StringUtil.hexToDecimal(makeMsg.substring(idx, idx + 2))),2);
                //log.info(tag + "HEX STRING FOR MAKE :IDX = " + idx + " :HEXSTR = " + hexStr);
                if (idx == 0) {
                    lrc = Integer.parseInt(hexStr, 16);
                }
                else {
                    lrc ^= Integer.parseInt(hexStr, 16);
                }
                idx += 2;
            }
            catch(StringIndexOutOfBoundsException se ) {
                break;
            }
        }
        //LRC 생성 끝...
        //buf.append(StringUtil.fixLenString(Integer.toHexString(lrc),2));
        buf.append(StringUtil.fixLenString(Integer.toHexString(52),2)); //lrc : 52 =  0x034
        makeMsg = buf.toString().toUpperCase();
        //log.info(tag + "HEXA WITH LRC = " + makeMsg);

        // log.info(tag + "전송 byte array 크기계산 시작");
        int byteSize = 0;
        String hexStr = "";
        idx = 0;
        while(true) {
            try {
                hexStr = Integer.toHexString(StringUtil.hexToDecimal(makeMsg.substring(idx, idx + 2)));
                byteSize += hexStr.length();
                idx += 2;
            }
            catch (StringIndexOutOfBoundsException se) {
                break;
            }
        }
        byte[] rbytes = new byte[byteSize];
        //log.info(tag + "전송 byte array 크기계산 종료");
        //log.info(tag + "rbytes.length = " + rbytes.length);

        //log.info(tag + "전송 byte array 생성시작");
        idx = 0;
        int jdx = -1;
        while(true) {
            try {
                hexStr = Integer.toHexString(StringUtil.hexToDecimal(makeMsg.substring(idx, idx + 2)));
                for (int kdx = 0; kdx < hexStr.length(); kdx++) {
                    rbytes[++jdx] = (byte) Integer.parseInt(hexStr.substring(kdx, kdx + 1), 16);
                    //log.info("rbytesWithLRC[" + jdx + "] = " + rbytes[jdx]);
                }
                idx += 2;
            }
            catch (StringIndexOutOfBoundsException se) {
                break;
            }
        }
        //log.info(tag + "전송 byte array 생성종료");








        /*
        rBytes[0] = (byte) Integer.toHexString(2); //stx : 2 =  0x02
        rBytes[1] = (byte) Integer.toHexString(0,2); //length-first
        rBytes[2] = (byte) Integer.toHexString(12,2); //length-last
        rBytes[3] = (byte) Integer.toHexString(58,16); //cmd : 58 = 0x3a
        rBytes[4] = (byte) Integer.toHexString(hexY,16); //startYY : 현재년(2자리)
        rBytes[5] = (byte) Integer.toHexString(hexM,16); //startMM : 현재월(2자리)
        rBytes[6] = (byte) Integer.toHexString(hexD,16); //startDD : 현재일(2자리)
        rBytes[7] = (byte) Integer.toHexString(hexY,16); //endYY : 현재년(2자리)
        rBytes[8] = (byte) Integer.toHexString(hexM,16); //endMM : 현재월(2자리)
        rBytes[9] = (byte) Integer.toHexString(hexD,16); //endDD : 현재일(2자리)
        rBytes[10] = (byte) Integer.toHexString("3",2); //etx : 3 =  0x03
        */

        //log.info (tag + "Final Sending Msg length ===>  " +  rbytes.length);
        // log.info(tag + "금속검출기 데이터 수신 시작....");
        return socketService.sendByteMsgType1(rotServer,rotServerPort, rbytes);
    }

    /*하담푸드 온도센서*/
    @Override
    public Map<String, Object> modbusRotReadByType2(Map<String, Object> paraMap) {
        String tag = "RotService.modbusRotReadByType2 => ";

        String rotServer = paraMap.get("deviceIp").toString();
        int rotServerPort = Integer.valueOf(paraMap.get("devicePort").toString());
        //log.info(tag + "rotServer = " + rotServer);
        //log.info(tag + "rotServerPort = " + rotServerPort);

        StringBuffer buf = new StringBuffer();

        buf.append(StringUtil.fixLenString(Integer.toHexString(1),2))//장비번 0x00
                .append(StringUtil.fixLenString(Integer.toHexString(3),2))//Modbus Function Code (0x03)
                .append(StringUtil.fixLenString(Integer.toHexString(0),4))//시작주소 (0x54)
                .append(StringUtil.fixLenString(Integer.toHexString(2),4)); //읽을갯수

        String crc  = StringUtil.getCrc(buf.toString());
        buf.append(crc);

        //buf.append(StringUtil.fixLenString(crc,4)); //CRC16
        //buf.append(StringUtil.fixLenString(Integer.toHexString(31),2)); //CRC16  31---임시사용
        //buf.append(StringUtil.fixLenString(Integer.toHexString(221),2)); //CRC16 dd --임시사용중

        String makeMsg = buf.toString().toUpperCase();
        //01    03            0022      0001    6078
        //장비번호 FunctionCode 시작주소     읽을갯수   CRC
        //makeMsg = "0103022100016078";

        log.info(tag + "HEXA SENDING STR WITH CRC = " + makeMsg);

        /*log.info(tag + "전송 byte array 크기계산 시작");*/
        int byteSize = 0;
        String hexStr = "";
        int idx = 0;
        while(true) {
            try {
                hexStr = Integer.toHexString(StringUtil.hexToDecimal(makeMsg.substring(idx, idx + 2)));
                byteSize += hexStr.length();
                idx += 2;
            }
            catch (StringIndexOutOfBoundsException se) {
                break;
            }
        }
        byte[] rbytes = new byte[byteSize];
        //log.info(tag + "전송 byte array 크기계산 종료");
        //log.info(tag + "rbytes.length = " + rbytes.length);
        //log.info(tag + "전송 byte array 생성시작");
        idx = -1;
        int decimal = 0;
        int jdx = -1;

        hexStr = makeMsg.substring(0, 2); //장비번호(01)
        rbytes[0] = (byte) Integer.parseInt(hexStr,16);

        hexStr = makeMsg.substring(2, 4); //FunctionCode(03)
        rbytes[1] = (byte) Integer.parseInt(hexStr,16);

        hexStr = makeMsg.substring(4, 6); //시작번지-상(00)
        rbytes[2] = (byte) Integer.parseInt(hexStr ,16);

        hexStr = makeMsg.substring(6, 8); //시작번지-하
        rbytes[3] = (byte) Integer.parseInt(hexStr,16);

        hexStr = makeMsg.substring(8, 10); //Read Counter-상
        rbytes[4] = (byte) Integer.parseInt(hexStr,16);

        hexStr = makeMsg.substring(10, 12); //Read Counter-하
        rbytes[5] = (byte) Integer.parseInt(hexStr,16); //FunctionCode

        hexStr = makeMsg.substring(12, 14); //CRC-상
        rbytes[6] = (byte) Integer.parseInt(hexStr,16); //C4
        //rbytes[6] = (byte) Integer.parseInt("C4",16); //FunctionCode

        hexStr = makeMsg.substring(14); //CRC-하
        rbytes[7] = (byte) Integer.parseInt(hexStr,16); //FunctionCode
        //rbytes[7] = (byte) Integer.parseInt("0B",16); //FunctionCode

        // log.info(tag + "전송 byte array 생성종료");

        //전송 byte array 크기계산 시작
        //log.info (tag + "Final Sending Msg length ===>  " +  rbytes.length + "Bytes = " +  Arrays.toString(rbytes));
        log.info(tag + " 데이터 수신 중....");
        Map<String,Object> rmap = new HashMap<String,Object>();
        int naRslt[] =  socketService.sendByteMsgType2(rotServer,rotServerPort, rbytes);
        if (naRslt[1] == 3) {
            float tmpr = (float) ((naRslt[3] * 255 + naRslt[4]) * 0.1);
            float humy = (float) ((naRslt[5] * 255 + naRslt[6]) * 0.1);
            rmap.put("tmpr", tmpr);
            rmap.put("humy", humy);

            EquipMngrHstr hvo = new EquipMngrHstr();
            hvo.setUnixHms(System.currentTimeMillis() / 1000);
            hvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
            SpotEquip sevo = new SpotEquip();
            Map<String,Object> semap = new HashMap<String,Object>();
            semap.put("spotEquipNo", Long.parseLong(paraMap.get("spotEquipNo").toString()));
            semap = spotEquipService.getSpotEquipInfo(paraMap);
            hvo.setUsedYn("Y");
            hvo.setFeYn("N");
            hvo.setSusYn("N");
            hvo.setProdYn("N");
            hvo.setMeasVal(tmpr);
            hvo.setMaxLmtVal(Float.parseFloat(semap.get("max_lmt_val").toString()));
            hvo.setMinLmtVal(Float.parseFloat(semap.get("min_lmt_val").toString()));
            EquipMngrHstr chkvo =equipMngrHstrRepo.findBySpotEquipNoAndUnixHms(hvo.getSpotEquipNo(), hvo.getUnixHms());
            if (chkvo != null) {
                hvo.setMngrHstrNo(chkvo.getMngrHstrNo());
            }
            else {
                hvo.setMngrHstrNo(0L);
            }
            equipMngrHstrRepo.save(hvo);
            return rmap;
        }
        else {
            rmap.put("tmpr", 0f);
            rmap.put("most", 0f);
        }
        return rmap;
    }
}
