package daedan.mes.moniter.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.moniter.service.DdkorService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/moniter/ddkor")
public class DdKorController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private DdkorService ddkorService;


    @PostMapping(value="/moniterHstr") //
    public Result moniterHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "DdkorController.moniterHstr => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ddkorService.getMoniterHstr(paraMap));
        result.setTotalCount(ddkorService.getMoniterHstrCount(paraMap));
        return result;
    }

    @PostMapping(value="/moniterHstrH24") //
    public Result moniterHstrH24(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ddkorService.getMoniterHstrH24(paraMap));
        return result;
    }
    @PostMapping(value="/operStat") //가동현황
    public Result opeerStat(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ddkorService.getOpeerStat(paraMap));
        return result;
    }
}
