package daedan.mes.moniter.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.moniter.service.MonCust18Service;
import daedan.mes.sysmenu.user.domain.AccHstr;
import daedan.mes.sysmenu.user.domain.EvntType;
import daedan.mes.sysmenu.user.domain.UserInfo;
import daedan.mes.sysmenu.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/moniter")
public class Cust18Controller {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private MonCust18Service cust18;

    @Autowired
    private UserService userService;

    @PostMapping(value="/getHeatLogHstr") //찜기모니터링
    public Result getHeatLogHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "MoniterControlelr.getHeatLogHstr => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        //result.setData(cust18.getHeatLogHstr(paraMap));
        paraMap.put("orderBy", "graph"); //그래프는 시간순으로
        List<Map<String,Object>> ds = cust18.getHeatLogHstr(paraMap);
        Map<String, Object> dmap = new HashMap<String,Object>();
        Map<String, Object> rmap = new HashMap<String,Object>();
        int idx = -1;

        ArrayList<String> operYnHstr = new ArrayList<>();
        ArrayList<String> rcvTm = new ArrayList<String>();
        while(++idx < ds.size()) {
            dmap = ds.get(idx);
            operYnHstr.add(dmap.get("operYn").toString());
            rcvTm.add(dmap.get("rcvTm").toString());
        }
        rmap.put("grapHstr",operYnHstr); //그래프 작동축(Y)
        rmap.put("rcvTm", rcvTm); //그래프 시간축(X)

        paraMap.put("orderBy", "grid"); //그리드는 최신순으로
        rmap.put("gridHstr",cust18.getHeatLogHstr(paraMap)); //그리드

        result.setData(rmap);
        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/heaterOn")
    public Result heaterOn(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        cust18.heaterOn(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value="/heaterOff")
    public Result heaterOff(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        cust18.heaterOff(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

}
