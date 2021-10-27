package daedan.mes.cmpy.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.cmpy.service.CmpyService;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
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
@RequestMapping("/api/daedan/mes/cmpy")
public class CmpyController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CmpyService cmpyService;


    @Autowired
    private CodeService codeService;

    @Autowired
    private CmmnService cmmnService;


    @Autowired
    private CmpyRepository cmpyRepository;

    @PostMapping(value="/cmpyList") //거래처목
    public Result cmpyList(@RequestBody HashMap<String, Object> paraMap  , HttpSession session){
        String tag = "cmpyController.cmpyList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getCmpyList(paraMap));
        result.setTotalCount(cmpyService.getCmpyListCount(paraMap));
        return result;
    }

    @PostMapping(value="/cmpyExcelList") //거래처목록 - 엑셀출력용
    public Result cmpyExcelList(@RequestBody Map<String,Object> paraMap  , HttpSession session){
        String tag = "vsvc.CmpyService.cmpyExcelList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getCmpyExcelList(paraMap));
        return result;
    }

    @PostMapping(value="/Cust800Condition") //거래처목
    public Result Cust800Condition(@RequestBody HashMap<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String,Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.cmpytp")));
        rmap.put("comboSaleUnit", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    //구매거래처저장
    @PostMapping(value="/savePursCmpy")
    public Result savePursCmpy(@RequestBody Map<String,Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.saveSaleCmpy => ";

        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String,Object> cmpyMap = (Map<String,Object>) paraMap.get("cmpyInfo");
        cmpyMap.put("ipaddr", NetworkUtil.getClientIp(request));
        cmpyMap.put("userId", paraMap.get("userId"));
        cmpyMap.put("custNo",paraMap.get("custNo"));
        cmpyService.saveCmpy(cmpyMap);
        return result;
    }
    //판매거래처저장
    @PostMapping(value="/saveSaleCmpy")
    public Result saveSaleCmpy(@RequestBody Map<String,Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.saveSaleCmpy => ";

        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String,Object> cmpyMap = (Map<String,Object>) paraMap.get("cmpyInfo");
        cmpyMap.put("ipaddr", NetworkUtil.getClientIp(request));
        cmpyMap.put("userId", paraMap.get("userId"));
        cmpyMap.put("custNo",paraMap.get("custNo"));
        cmpyService.saveCmpy(cmpyMap);
        return result;
    }
    //거래처삭제
    @PostMapping(value="/dropCmpy")
    public Result dropCmpy(@RequestBody Map<String,Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.dropCmpy => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        cmpyService.setUnusedCmpy(paraMap);
        return result;
    }
    //원료 거래처 삭제
    @PostMapping(value = "/dropMatrCmpy")
    public Result dropMatrCmpy(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.dropMatrCmpy => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        cmpyService.dropMatrCmpy(paraMap);
        return result;
    }
    //거래처추출
    @PostMapping(value="/cmpyInfo")
    public Result cmpyInfo(@RequestBody Map<String,Object> paraMap  , HttpSession session){
        String tag = "cmpyController.cmpyInfo => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getCmpyInfo(paraMap));
        return result;
    }
    //구매거래처 자재목록
    @PostMapping(value="/cmpyPursData")
    public Result cmpyMatrList(@RequestBody Map<String,Object> paraMap  , HttpSession session){
        String tag = "cmpyController.cmpyPursData => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("pursInfo",cmpyService.getCmpyPursInfo(paraMap));
        rmap.put("pursMatrList",cmpyService.getCmpyPursMatrList(paraMap));
        result.setData(rmap);
        return result;
    }

    /**
     * 축적할 데이터가 담긴 map을 db저장, 파일저장 함수에 호출
     *
     * @param paraMap(plcNo)
     * @return return result;
     */
    @PostMapping(value="/getDlvPlcInfo") //AddOn By KMJ AT 21.10.19
    public Result getDlvPlcInfo(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getDlvPlcInfo(paraMap));
        return result;
    }

    @PostMapping(value="/cmpyDlvPlcList") //판매거래처 납품장소목록
    public Result getCmpyDlvPlcList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "cmpyController.cmpyDlvPlcList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getCmpyDlvPlcList(paraMap));
        result.setTotalCount(cmpyService.getCmpyDlvPlcListCount(paraMap));
        return result;
    }

    //거래처 납품장소 등록
    @PostMapping(value="/saveDlvPlc")
    public Result saveDlvPlc(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.saveDlvPlc => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> passMap = new HashMap<String,Object>();
        passMap = (Map<String, Object>) paraMap.get("dlvPlcInfo");
        passMap.put("userId",paraMap.get("userId"));
        passMap.put("ipaddr",NetworkUtil.getClientIp(request));
        cmpyService.saveDlvPlc(passMap);
        return result;
    }

    //거래처 납품장소 일괄삭제
    @PostMapping(value="/dropDlvPlc")
    public Result dropDlvPlc(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.dropDlvPlc => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        cmpyService.deleteDlvPlc(paraMap);
        return result;
    }

    //거래처 납품장소 개별 삭제
    @PostMapping(value="/dropDlvPlcInfo")
    public Result dropDlvPlcInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        String tag = "cmpyController.dropDlvPlcInfo => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        cmpyService.deleteDlvPlcInfo(paraMap);
        return result;
    }



    @PostMapping(value="/getCmpyIpList") //테블릿 리스트
    public Result getCmpyPursMatrList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "cmpyController.getCmpyIpList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getCmpyIpList(paraMap));
        result.setTotalCount(cmpyService.getCmpyIpListCount((HashMap<String, Object>) paraMap));
        return result;
    }

    @PostMapping(value="/getOptionsList") //
    public Result getOptionsList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "cmpyController.getCmpyIpList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setEqOptions(cmpyService.getEqOption(paraMap));
        result.setSysOptions(cmpyService.getSysOption(paraMap));
        return result;
    }

    @PostMapping(value = "/matrCmpyList")
    public Result getMatrCmpyList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(cmpyService.getMatrCmpyList(paraMap));

        return result;
    }

}

