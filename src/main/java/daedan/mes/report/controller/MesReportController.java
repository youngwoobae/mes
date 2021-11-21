package daedan.mes.report.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.report.service.MesReportService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/mesreport")
public class MesReportController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;


    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MesReportService mesReportService;

    @Autowired
    private UserService userService;
    /**
     * 제품입고이력 검색조건 설정
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditions1044")
    public Result conditions1044(@RequestBody Map<String, Object> paraMap,HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboWh",cmmnService.getComboWh(paraMap));
        result.setData(rmap);
        return result;
    }
    /**
     * 근태현황
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value = "/conditions1092")
    public Result conditionsUser1092(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String,Object> rmap = new HashMap<String,Object>();

        paraMap.put("selectStr","채용구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("base_empl_kind"))); ///작업자구분(내국인,외국인,용역)
        rmap.put("comboEmplKind", codeService.getComboCodeList(paraMap));

        result.setData(rmap);
        return result;
    }
    /**
     * 제품생산
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getProdIwhHstr")
    public Result getProdIwhHstr(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(mesReportService.getProdIwhHstr(paraMap));
        result.setTotalCount(mesReportService.getProdIwhHstrCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    /**
     * 제품출고이력 검색조건 설정
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditions1045")
    public Result conditionsEmbPmsOrd(@RequestBody Map<String, Object> paraMap,HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboWh",cmmnService.getComboWh(paraMap));
        result.setData(rmap);
        return result;
    }

    /**
     * 제품출고이력 검색조건 설정
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditions1050")
    public Result conditions1050(@RequestBody Map<String, Object> paraMap,HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboWh",cmmnService.getComboWh(paraMap));
        result.setData(rmap);
        return result;
    }


    /**
     *
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getProdOwhHstr")
    public Result getProdOwhHstr(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(mesReportService.getProdOwhHstr(paraMap));
        result.setTotalCount(mesReportService.getProdOwhHstrCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    /**
     * 금속검출이력 추출
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getMetalDetectHstr")
    public Result getMetalDetectHstr(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(mesReportService.getMetalDetectHstr(paraMap));
        result.setTotalCount(mesReportService.getMetalDetectHstrCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }


}
