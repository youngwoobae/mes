package daedan.mes.ord.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.ord.service.Ord18Service;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.core.util.SystemNanoClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/ord/")
public class Ord18Controller {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private Ord18Service ord18Service;

    /**
     * 미홍 주문장 입력 컨트롤 생성
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value = "/conditionsEmbOrdInfo")
    public Result conditionsEmpOrdInfoMh(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.pkg_tp")));
        paraMap.put("selectStr", "포장방법선택");
        rmap.put("comboPkgTp", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.dlv_tp")));
        paraMap.put("selectStr", "배송방법선택");
        rmap.put("comboRcvTp", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.ordstat")));
        paraMap.put("selectStr", "주문상태선택");
        rmap.put("comboOrdSts", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/ord18List") //주문자생산
    public Result ord18List(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "Ord18Controller.ord18List =>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        HashMap<String ,Object> rmap = new HashMap<String,Object>();

        List<Map<String, Object>> ds = ord18Service.getOrd18List(paraMap);
        ArrayList<Map<String, Object>> result22 = new ArrayList<>();
//        StringBuffer sbuffer = new StringBuffer();

        int i;
        boolean isFillRight = false;
        int count = 0;
        HashMap<String,Object> dmap = new HashMap<String,Object>();
        for( i = 0; i < ds.size(); i++) {
            rmap = (HashMap<String, Object>) ds.get(i);
            log.info(tag + "rmap = " + rmap.toString());
            switch (i % 2) {
                case 0:
                    count++;
                    dmap.put("ordNoL", rmap.get("ordNo"));
                    dmap.put("ordNo2L" , rmap.get("ordNo"));
                    dmap.put("ordCustNmL" , rmap.get("ordCustNm"));
                    dmap.put("ordCustCellNoL" , rmap.get("ordCustCellNo"));
                    dmap.put("rcvNmL" , rmap.get("rcvNm"));
                    dmap.put("rcvCellNoL", rmap.get("rcvCellNo"));
                    dmap.put("rcvAddrL" , rmap.get("rcvAddr"));
                    dmap.put("rcvTpL" , rmap.get("rcvTp"));
                    dmap.put("rcvHrL" , rmap.get("rcvHr"));
                    dmap.put("sheetRmkL" , rmap.get("sheetRmk"));
                    dmap.put("txtRmkL" , rmap.get("txtRmk"));
                    dmap.put("noteRmkL" , rmap.get("noteRmk"));
                    dmap.put("prodNoL" , rmap.get("prodNo"));
                    isFillRight = false;
                    if(count%3 == 0){
                        result22.add(dmap);
                    }
                    log.info(tag + "dmap = " + dmap.toString());
                    break;
                case 1:
                    count++;
                    dmap.put("ordNoR", rmap.get("ordNo"));
                    dmap.put("ordNoR", rmap.get("ordNo"));
                    dmap.put("ordNo2R", rmap.get("ordNo"));
                    dmap.put("ordCustNmR", rmap.get("ordCustidxNm"));
                    dmap.put("ordCustCellNoR", rmap.get("ordCustCellNo"));
                    dmap.put("rcvNmR", rmap.get("rcvNm"));
                    dmap.put("rcvCellNoR", rmap.get("rcvCellNo"));
                    dmap.put("rcvAddrR", rmap.get("rcvAddr"));
                    dmap.put("rcvTpR", rmap.get("rcvTp"));
                    dmap.put("rcvHrR", rmap.get("rcvHr"));
                    dmap.put("sheetRmkR", rmap.get("sheetRmk"));
                    dmap.put("txtRmkR", rmap.get("txtRmk"));
                    dmap.put("noteRmkR", rmap.get("noteRmk"));
                    dmap.put("prodNoR", rmap.get("prodNo"));
                    log.info(tag + "dmap = " + dmap.toString());
                    result22.add(dmap);
                    dmap = new HashMap<String,Object>();
                    isFillRight = true;
                    break;
            }
            if (!isFillRight) {
                dmap.put("ordNoR", " ");
                dmap.put("ordNoR2", " ");
                dmap.put("ordCustNmR", " ");
                dmap.put("ordCustCellNoR", " ");
                dmap.put("rcvNmR", " ");
                dmap.put("rcvCellNoR", " ");
                dmap.put("rcvAddrR", " ");
                dmap.put("rcvTpR", " ");
                dmap.put("rcvHrR ", " ");
                dmap.put("sheetRmkR", " ");
                dmap.put("txtRmkR", " ");
                dmap.put("noteRmkR", " ");
                dmap.put("prodNoR", " ");
            }
        }
        result.setData(result22);
        return result;
    }

    @PostMapping(value = "/getOrdInfo18") //미홍주문정보
    public Result getOrd18Info(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ord18Service.getOrd18Info(paraMap));
        return result;
    }
    @PostMapping(value = "/saveOrdInfo18") //미홍주문정보
    public Result saveOrdInfo(@RequestBody Map<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        ord18Service.saveOrdInfo18(paraMap);
        return result;
    }
    @PostMapping(value = "/dropOrdInfo18") //미홍주문정보
    public Result dropOrdInfo(@RequestBody Map<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        ord18Service.dropOrdInfo18(paraMap);
        return result;
    }

}
