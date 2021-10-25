package daedan.mes.modbus.rtu.controller;

import com.ghgande.j2mod.modbus.Modbus;
import daedan.mes.common.domain.Result;
import daedan.mes.modbus.rtu.domain.RtuInfo;
import daedan.mes.modbus.rtu.service.RtuService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/modbus/rtu")
public class RtuController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    RtuService rtuService;

    @PostMapping(value="/connect")
    public Result connect(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        String tag = "sRtuController.connect =>";
        log.info(tag + paraMap);
        RtuInfo vo = new RtuInfo();


        vo.setPortNm(paraMap.get("portNm").toString()); //통신장비와 연결되어 있는 COM PORT 번호.
        vo.setBaudRt(Integer.valueOf(paraMap.get("baudRt"). toString())); //통신속도( 9600 )
        vo.setOffSet(Integer.valueOf(paraMap.get("offset"). toString())); //start read pointer
        vo.setDataLen(Integer.valueOf(paraMap.get("dataLen").toString())); //데이터비트( 기본 8 )
        vo.setParity(Integer.valueOf(paraMap.get("parity").toString())); //패러티비트( 기본 0 )
        vo.setStopBit(Integer.valueOf(paraMap.get("stopBit").toString())); //정지비트( 기본 1 )
        vo.setUnitId(Integer.valueOf(paraMap.get("unitId").toString())); //장치번호
        vo.setEncoding(Modbus.SERIAL_ENCODING_RTU); //RTU
        vo.setRepeat(1);
        result.setData(rtuService.connectRtu(vo));
        return result;
    }
}
