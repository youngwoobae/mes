package daedan.mes.kpi.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dash.service.DashService;
import daedan.mes.kpi.service.KpiService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/kpi")
public class KpiController {
    private Log log = LogFactory.getLog(this.getClass());


    @Autowired
    private KpiService kpiService;


    @PostMapping(value="/operPerformRead")
    public Result operPerformRead(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("kpiSessId",session.getAttribute("kpiSessId"));
        result.setData(kpiService.operPerformRead(paraMap));
        return result;
    }
    @PostMapping(value="/kpiRead")
    public Result kpiRead(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("kpiSessId",session.getAttribute("kpiSessId"));
        result.setData(kpiService.kpiRead(paraMap));
        return result;
    }


}
