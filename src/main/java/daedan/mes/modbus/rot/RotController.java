package daedan.mes.modbus.rot;

import daedan.mes.common.domain.Result;
import daedan.mes.modbus.rot.detector.service.RotService;
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
@RequestMapping("/api/daedan/mes/modbus/rot")
public class RotController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    RotService rotService;

    @PostMapping(value="/modbusRotReadByType2")
    public Result modbusTcpRead(@RequestBody Map<String, Object> paraMap){
        String tag = "makeController.modbusRotRead => ";
        Result result = Result.successInstance();

        result.setData(rotService.modbusRotReadByType2(paraMap));
        log.info(tag + "modbusRotRead " + result.getData());
        return result;
    }
}

