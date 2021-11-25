package daedan.mes.moniter.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.moniter.service.Cust10Service;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
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
public class Cust10Controller {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Cust10Service cust10;

    @Autowired
    private UserService userService;

    @PostMapping(value="/metalDetectHstr") //
    public Result moniterHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(cust10.getMetalLogHstr(paraMap));
        result.setTotalCount(cust10.getMetalLogHstrCount(paraMap));

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

    /**
     * 금속검출모니터링
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getMetalLogHstr")
    public Result getMetalLogHstr(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();
        Map<String, Object> dmap = new HashMap<String,Object>();
        List<Map<String,Object>> ds = (cust10.getMetalLogHstr(paraMap));
        int idx = -1;
        ArrayList<String> rcvTm = new ArrayList<String>();
        ArrayList<Integer> errList = new ArrayList<Integer>();
        ArrayList<Integer> passList = new ArrayList<Integer>();
        while(++idx < ds.size()) {
            dmap = ds.get(idx);
            rcvTm.add(dmap.get("rcvTm").toString());
            errList.add(Integer.parseInt(dmap.get("errQty").toString()));
            passList.add(Integer.parseInt(dmap.get("passQty").toString()));
        }
        rmap.put("metalList",ds);
        rmap.put("labels",rcvTm);
        rmap.put("passList",passList);
        rmap.put("errList",errList);
        result.setData(rmap);
        result.setTotalCount(cust10.getMetalLogHstrCount(paraMap));

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
