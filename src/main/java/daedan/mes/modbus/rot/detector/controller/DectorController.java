package daedan.mes.modbus.rot.detector.controller;

import daedan.mes.modbus.rot.detector.service.RotService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*-------------------------------*/
/*금속검출 : 비표준 with eathernet    *
/*-------------------------------*/
@RestController
@RequestMapping("/api/daedan/mes/modbus/rot/dector")
public class DectorController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    RotService dectorService;
}
