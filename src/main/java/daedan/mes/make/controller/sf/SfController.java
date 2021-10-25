package daedan.mes.make.controller.sf;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.make.service.hdfd.HdfdService;
import daedan.mes.make.service.sf.SfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/make/sf")
public class SfController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private SfService sfService;

    @PostMapping(value = "/getXsl2Hmtl")
    public Result getDailyIndcHtml(@RequestBody Map<String, Object> paraMap){
        String tag = "vsvc.SfController.getXsl2Hmtl =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Result result = Result.successInstance();
        result.setData(sfService.getXsl2Hmtl(paraMap));
        return result;
    }
    @PostMapping(value = "/makeDailyIndc")
    public Result makeDailyIndc(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        Result result = Result.successInstance();
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        sfService.makeDailyIndc(paraMap);
        return result;
    }

    @PostMapping(value = "/getDailyIndc")
    public Result getDailyIndc(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        result.setData(sfService.getDailyIndc(paraMap));
        return result;
    }

}
