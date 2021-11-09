package daedan.mes.report.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.report.service.MesReportService;
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
    private CodeService codeService;
    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private MesReportService mesReportService;
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboWh",cmmnService.getComboWh(paraMap));
        //paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base.proc_stat")));
        //rmap.put("comboProcStat", codeService.getComboCodeList(paraMap));
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(mesReportService.getProdIwhHstr(paraMap));
        result.setTotalCount(mesReportService.getProdIwhHstrCount(paraMap));
        System.out.print("****" + result);
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboWh",cmmnService.getComboWh(paraMap));
        //paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base.proc_stat")));
        //rmap.put("comboProcStat", codeService.getComboCodeList(paraMap));
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboWh",cmmnService.getComboWh(paraMap));
        //paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base.proc_stat")));
        //rmap.put("comboProcStat", codeService.getComboCodeList(paraMap));
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(mesReportService.getProdOwhHstr(paraMap));
        result.setTotalCount(mesReportService.getProdOwhHstrCount(paraMap));
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(mesReportService.getMetalDetectHstr(paraMap));
        result.setTotalCount(mesReportService.getMetalDetectHstrCount(paraMap));
        //System.out.print("****" + result);
        return result;
    }


}
