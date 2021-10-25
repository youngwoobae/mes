package daedan.mes.dash.controller.yyjg;

import daedan.mes.dash.service.Yyjg.YyjgDashService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/daedan/mes/dash/yyjg")
public class YyjgDashController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;


    @Autowired
    private YyjgDashService dashService;




}
