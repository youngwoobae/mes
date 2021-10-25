package daedan.mes.modbus.rot.tmpr.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.modbus.rot.tmpr.service.TmprService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/*-------------------------------*/
/*온도연동 : 비표준 with eathernet    *
/*-------------------------------*/

@RestController
@RequestMapping("/api/daedan/mes/modbus/rot/temperature")
public class TmprController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    TmprService tmprService;


    @PostMapping(value="/connect")
    public Result connect(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        String tag = "RotController.connect =>";
        log.info(tag + paraMap);

        result.setData(tmprService.connect(paraMap));

        return result;
    }

}
