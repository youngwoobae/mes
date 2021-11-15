package daedan.mes.make.controller.hdfd;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.make.service.hdfd.HdfdService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/hdfd/make")

public class HdfdController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private HdfdService hdfdService;

    @PostMapping(value = "/hdfdIndcList")
    public Result getHdfdIndcList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "vsvc.HdfdController.getHdfdIndcList =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(hdfdService.getHdfdIndcList(paraMap));

        return result;
    }


    @PostMapping(value = "/planList")
    public Result planList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "vsvc.HdfdController.getHdfdIndcList =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(hdfdService.planList(paraMap));

        return result;
    }


}
