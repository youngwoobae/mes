package daedan.mes.modbus.tcp.controller;


import daedan.mes.common.domain.Result;
import daedan.mes.equip.domain.EquipMngrHstr;
import daedan.mes.equip.repository.EquipMngrHstrRepository;
import daedan.mes.equip.service.EquipService;
import daedan.mes.modbus.tcp.service.TcpService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/modbus/tcp")
public class TcpController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    TcpService tcpService;

    @Autowired
    EquipMngrHstrRepository equipMngrHstrRepo;

    @PostMapping(value="/modbusTcpRead")
    public Result modbusTcpRead(@RequestBody Map<String, Object> paraMap){
        String tag = "tcpController.modbusTcpRead => ";
        Result result = Result.successInstance();

        result.setData(tcpService.modbusTcpRead(paraMap));
        log.info(tag + "ReadData = " + result.getData());
        return result;
    }

    @PostMapping(value="/modbusTcpWrite")
    public Result modbusTcpWrite(@RequestBody Map<String, Object> paraMap){
        String tag = "tcpController.modbusTcpWRite => ";
        Result result = Result.successInstance();

        result.setData(tcpService.modbusWrite(paraMap));
        log.info(tag + "modbusTcpWrite " + result.getData());
        return result;
    }


}
