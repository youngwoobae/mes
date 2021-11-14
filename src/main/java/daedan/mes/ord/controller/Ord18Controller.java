package daedan.mes.ord.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.ord.service.Ord18Service;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/ord/18")
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
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ord18Service.getOrd18List(paraMap));
        result.setTotalCount(ord18Service.getOrd18ListCount(paraMap));
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
