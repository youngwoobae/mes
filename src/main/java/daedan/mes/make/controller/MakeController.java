package daedan.mes.make.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.make.service.MakeIndcService;
import daedan.mes.make.service.metal10.Metal10Service;
import daedan.mes.modbus.service.ModbusService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/make")
public class MakeController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MakeIndcService makeIndcService;

    @Autowired
    private Metal10Service metal10;

    @Autowired
    private ModbusService modbusService;

    @Autowired
    private UserService userService;

    @PostMapping(value="/conditionEmpIndcMp")
    public Result conditionEmpIndcMp(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.conditionEmpIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_proc")));
        rmap.put("comboProc", codeService.getComboCodeList(paraMap));

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
    @PostMapping(value="/conditions620")
    public Result conditions620(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.conditionEmpIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.indcTp")));
        paraMap.put("selectStr", "지시경로선택");
        rmap.put("comboIndcTp", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.indcStsTp")));
        paraMap.put("selectStr", "지시상태선택");
        rmap.put("comboIndcStses", codeService.getComboCodeList(paraMap));

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


    @PostMapping(value="/comboWorkDay")
    public Result comboWorkDay(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.comboWorkDay => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        log.info(tag + "params = " + paraMap.toString());
        result.setData(makeIndcService.getComboWorkDay(paraMap));

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
    @PostMapping(value="/conditionEmbIndc")
    public Result comboEmbIndc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.comboEmbIndc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();
        String typeCd = paraMap.get("typeCd").toString();
        log.info("123 paraMap : " + paraMap);
        try {
            if (typeCd.equals("date")) {
                rmap.put("workDays", makeIndcService.getComboWorkDay(paraMap));
            }

            else if(typeCd.equals("code")){
                paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code_base_proc")));
                rmap.put("unitCodes",codeService.getComboCodeList(paraMap));
            }
        }
        catch (NullPointerException ne) {
            paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.sale_unit"))); //판매단위
//            paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code_base_proc")));
//            rmap.put("unitCodes",codeService.getComboCodeList(paraMap));
            rmap.put("startProc", makeIndcService.getStartProc(paraMap)); //최초공정
        }
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

    /*생산계획에서 사용중인 콤보주문거래서 생성용*/
    @PostMapping(value="/comboOrdrCmpyList")
    public Result comboOrdrCmpyList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "makeController.comboOrdrCmpyList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getComboOrdrCmpyList(paraMap));

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

    @PostMapping(value="/makeIndcList")
    public Result makeIndcList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        try {
            paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
            paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        }
        catch (NullPointerException ne) {

        }
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(makeIndcService.getMakeIndcList(paraMap));
        result.setTotalCount(makeIndcService.getMakeIndcListCount(paraMap));

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
    @PostMapping(value="/makeIndcInfo")
    public Result makeIndcInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMakeIndcInfo(paraMap));

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

    @PostMapping(value="/autoSaveIndcMp")
    public Result autoSaveIndcMp(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "makeController.autoSaveIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.autoSaveIndcMp(paraMap);

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

    /*공정별 하루 최대 생산캐퍼 추출*/
    @PostMapping(value="/maxMakeCapacity")
    public Result maxMakeCapacity(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.maxMakeCapacity => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMaxMakeCapacity(paraMap));

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


    /*테블릿 오퍼레이션시 선택가능한 상품목록 추출 (작업지시를 기반으로 함)*/
    @PostMapping(value="/operProdList")
    public Result operProdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.operProdList => ";
        log.info(tag);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("baseUrl",env.getProperty("base_file_url"));
        result.setData(makeIndcService.getOperProdList(paraMap));

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

    /*작업시시별 하루 최대 생산캐퍼 추출*/
    @PostMapping(value="/maxMakeCapacityPerDay")
    public Result maxMakeCapacityPerDay(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.maxMakeCapacityPerDay => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMaxMakeCapacityPerDay(paraMap));

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
    /*작업현황에서 drag & drop으로 작업기간을 옮긴경*/
    @PostMapping(value="/resetMakeTermByDragDrop")
    public Result resetIndcDt(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "makeController.resetMakeTermByDragDrop => ";
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        makeIndcService.resetMakeTermByDragDrop(paraMap);

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

    /*상품정보를 기반한 제고공정 전체 작업지시 일괄생성 - 생산계획*/
    @PostMapping(value="/saveMakeIndcFullByPlan")
    public Result saveMakeIndcFullByPlan(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcFullByPlan => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        makeIndcService.saveMakeIndcFullByPlan(paraMap);

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

    /*상품정보를 기반한 제고공정 전체 작업지시 일괄생성*/
    @PostMapping(value="/saveMakeIndcFull")
    public Result saveMakeIndcFull(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcFull => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        result.setData(makeIndcService.saveMakeIndcFull(paraMap));

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
     * 기능 : 생산(계획) 및 생산제품에 설정된 생산공정별 작업지시 데이터 생성(2021.12.05 신규작업)
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */

    @PostMapping(value="/saveMakeIndcProc")
    public Result saveMakeIndcProc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcProc => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        makeIndcService.saveMakeIndcProc(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    /**/
    @PostMapping(value="/getProdIndcNo")
    public Result getProdIndcNo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.getProdIndcNo => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        result.setData(makeIndcService.getProdIndcNo(paraMap));

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

    /*작업지시 기본정보 저장*/
    @PostMapping(value="/saveMakeIndc")
    public Result saveMakeIndc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndc => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        makeIndcService.saveMakeIndc(paraMap);

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
    @PostMapping(value="/dropMakeIndc")
    public Result dropMakeIndc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.dropMakeIndc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.dropMakeIndc(paraMap);

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

    /*투입인력생성*/
    @PostMapping(value="/saveMakeIndcMp")
    public Result saveMakeIndcMp(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.saveMakeIndcMp(paraMap);

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

    /*작업지시상세-투입인력정*/
    @PostMapping(value="/makeIndcMpInfo")
    public Result makeIndcMpInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcMpInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());

        result.setData(makeIndcService.getMakeIndcMpInfo(paraMap));

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
    /*작업지시상세-투입인력목록*/
    @PostMapping(value="/makeIndcMpList")
    public Result makeIndcMpList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcMpList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ocpnKind", Long.parseLong(env.getProperty("ocpn_kind_blue").toString()));
        log.info(tag + "paraMap = " + paraMap.toString());

        result.setData(makeIndcService.getMakeIndcMpList(paraMap));
        result.setTotalCount(makeIndcService.getMakeIndcMpListCount(paraMap));

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
    @PostMapping(value="/makeStatusList")
    public Result makeStatusList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeStatusList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(makeIndcService.getMakeStatusList(paraMap));
        result.setTotalCount(makeIndcService.getMakeStatusListCount(paraMap));

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

    /*시간(h*60 + m -> 시,분 스트링으로 변환)*/
    @PostMapping(value="/timeString")
    public Result cnvHmToTimeStr(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.timeStrinfg => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(makeIndcService.getTimeString(paraMap));

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


    /*작업지시상세-작업지시관련 자재목록 추출*/
    @PostMapping(value="/reqMatrList")
    public Result reqMatrList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.reqMatrList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(makeIndcService.getReqMatrList(paraMap));
        result.setTotalCount(makeIndcService.getReqMatrListCount(paraMap));

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

    /*작업지시상세-작업지시관련 자재목록 추출*/
    @PostMapping(value="/saveReqMatr")
    public Result saveReqMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) throws IOException, NullPointerException  {
        String tag = "makeController.saveReqMatr => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.saveReqMatr(paraMap);

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

    /*공정별 작업처리결과 추출*/
    @PostMapping(value="/makeIndcRsltInfo")
    public Result makeIndcRsltInfo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.makeIndcRsltInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMakeIndcRsltInfo(paraMap));;

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

    /*공번번호로 공정 추출*/
    @PostMapping(value="/makeIndcProc")
    public Result makeIndcProc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.makeIndcProc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("indcNo",Long.parseLong(paraMap.get("indc_no").toString()));
        result.setData(makeIndcService.makeIndcProc(paraMap));;

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
    /*공정별 작업처리결과 저장*/
    @PostMapping(value="/saveIndcRslt")
    public Result saveMakeIndcRslt(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveIndcRslt => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.saveIndcRslt(paraMap);

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
    @PostMapping(value="/dropIndcRslt")
    public Result dropIndcRslt(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.dropIndcRslt => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.dropIndcRslt(paraMap);

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

    @PostMapping(value="/exportPlanList")
    public Result exportPlanList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcPlanList => ";
//        paraMap.put("ordSts",Long.parseLong(env.getProperty("ord_status.wait.owh"))); //출고대
        paraMap.put("ordSts",null); //출고대
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getExportPlanList(paraMap));

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
    /*생산현황:입고상태인자료만 대상으로 함.*/
    @PostMapping(value="/makeStatList")
    public Result MakeStatList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info("maekStatListController.paraMap = " + paraMap.toString());//kill
        result.setData(makeIndcService.getMakeStatList(paraMap));
        result.setTotalCount(makeIndcService.getMakeStatListCount(paraMap));

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

    @PostMapping(value="/makeStatMoreList")
    public Result makeStatMoreList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info("maekStatListController.paraMap = " + paraMap.toString());//kill
        result.setData(makeIndcService.getMakeStatMoreList(paraMap));
        result.setTotalCount(makeIndcService.getMakeStatMoreListCount(paraMap));

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

    /*생산일보*/
    @PostMapping(value="/makeDailyReportList")
    public Result MakeDailyReportList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.MakeDailyReportList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("procCd",Long.parseLong(env.getProperty("proc.code.complete")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(makeIndcService.getMakeDailyReportList(paraMap));
        result.setTotalCount(makeIndcService.getMakeDailyReportListCount(paraMap));

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
    /*생산결과*/
    @PostMapping(value="/makeIndcRsltList")
    public Result makeIndcRsltList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.MakeDailyReportList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(makeIndcService.getMakeIndcRsltList(paraMap));
        result.setTotalCount(makeIndcService.getMakeIndcRsltListCount(paraMap));

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
    /*생산직목록*/
    @PostMapping(value = "/workerList")
    public Result userList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "makeController.workerList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        //paraMap.put("ocpnKind", Long.parseLong(env.getProperty("ocpn_kind_blue").toString()));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getWorkerList(paraMap));

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
    /*투입인력총괄*/
    @PostMapping(value="/mpUsedList")
    public Result mpUsedList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.mpUsedList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ocpnKind", Long.parseLong(env.getProperty("ocpn_kind_blue").toString()));
        log.info(tag + "paraMap = " + paraMap.toString());

        result.setData(makeIndcService.getMpUsedList(paraMap));
        result.setTotalCount(makeIndcService.getMpUsedListCount(paraMap));

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
    /*투입인력상세*/
    @PostMapping(value="/mpUsedDetlList")
    public Result mpUsedDetlList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.mpUsedDetlList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        log.info("emplKind = " + paraMap.get("emplKind"));
        result.setData(makeIndcService.getMpUsedDetlList(paraMap));
        result.setTotalCount(makeIndcService.getMpUsedDetlListCount(paraMap));

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
    /*투입인력정보저장*/
    @PostMapping(value="/saveMpInfo")
    public Result saveMpInfo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.mpInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.saveMpInfo(paraMap);

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
    /*투입인력 삭제*/
    @PostMapping(value="/mpdropInfo")
    public Result mpdropInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        makeIndcService.getMpDropInfo(paraMap);

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
    /*불량품목록*/
    @PostMapping(value="/faultList")
    public Result faultList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.faultList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        try {
            paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
            paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        }
        catch (NullPointerException ne) {

        }
        result.setData(makeIndcService.getFaultList(paraMap));
        result.setTotalCount(makeIndcService.getFaultListCount(paraMap));

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

    /*엑셀기반 부자재생성*/
    @PostMapping(value="/mpByExcel") //부자재정보저장
    public Result MpByExcel(@RequestBody HashMap<String, Object> paraMap , HttpServletRequest request, HttpSession session) {
        String tag = "MatrController.makeRawMatByExcel => ";
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Result result = Result.successInstance();
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        String resource = "config/mes.properties";
        Properties properties = new Properties();
        try {
            String fileRoot = uvo.getCustInfo().getFileRoot();
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            paraMap.put("matrTp", Integer.parseInt(properties.getProperty("rawmatr_cd")));
            paraMap.put(fileRoot,fileRoot);
            makeIndcService.loadMpByExcel(paraMap);
             } catch (Exception e) {
            e.printStackTrace();
            Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));
        }
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
    /*작업현황*/
    @PostMapping(value="/makeStatusReport")
    public Result makeStatusReport(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeStatusReport => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("baseProcNo",Long.parseLong(env.getProperty("code_base_proc")));
        log.info(tag + "params = " + paraMap.toString());
        Result result = Result.successInstance();
        result.setData(makeIndcService.getMakeStatusReport(paraMap));
        result.setTotalCount(makeIndcService.getMakeStatusReportCount(paraMap));

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


    /*작업시시별 하루 최대 생산캐퍼 추출*/
    @PostMapping(value="/makePlanList") //생산계획
    public Result makePlanList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makePlanList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        if (uvo.getCustInfo().getProcYn().equals("Y")) { //상세버전(대동고려삼,하담푸드,서울식품)
            result.setData(makeIndcService.getMakePlanList(paraMap));
        }
        else {
            result.setData(makeIndcService.getProductionPlan(paraMap)); //간략버전
        }

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

    @PostMapping(value="/makePlanOrdList") //생산계획 반영용 주문상목목록
    public Result makePlanOrdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makePlanOrdList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageSz", 10);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(makeIndcService.getMakePlanProdList(paraMap));
        result.setTotalCount(makeIndcService.getMakePlanProdListCount(paraMap));

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

    @PostMapping(value="/comboProdProcList") //제품생산공정목록
    public Result comboProdProcList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.prodProdList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getComboProdProcList(paraMap));

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
    @PostMapping(value="/makeMainProc") //초기(메인)_공정 추출
    public Result makeMainProc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.getMakeMainProc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMakeMainProc(paraMap));

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
    @PostMapping(value="/makeExtraProc") //상품별 제조공정 이외의 추가 추출용
    public Result makeExtraProc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.getMakeExtraProc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMakeExtraProc(paraMap));

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
    @PostMapping(value="/condition104ProcStd")
    public Result condition104ProcStd(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.condition104ProcStd => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("procList", makeIndcService.getMakeProcList(paraMap)); //최초공정
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
    @PostMapping(value="/getMoniterItemList")
    public Result getMoniterItemList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.condition104ProcStd => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("procList", makeIndcService.getMoniterItemList(paraMap)); //최초공정

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/manPowerSummaryList")
    public Result manPowerSummaryList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.manPowerSummaryList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String, Object>();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
        paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(makeIndcService.getManPowerSummaryList(paraMap));
        result.setTotalCount(makeIndcService.getManPowerSummaryListCount(paraMap));

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
    @PostMapping(value="/manPowerList")
    public Result manPowerList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.manPowerList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String, Object>();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("mpUsedDt", paraMap.get("mpUsedDt").toString().substring(0,10));

        result.setData(makeIndcService.getManPowerList(paraMap));
        result.setTotalCount(makeIndcService.getManPowerListCount(paraMap));

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
    @PostMapping(value="/wgtchkSummaryList")
    public Result wgtchkSummaryList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.wgtchkSummaryList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(makeIndcService.getWgtchkSummaryList(paraMap));
        result.setTotalCount(makeIndcService.getWgtchkSummaryListCount(paraMap));

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
    @PostMapping(value="/wgtchkDiareyList")
    public Result wgtchkDiaryList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.wgtchkDiaryList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(makeIndcService.getWgtchkDiaryList(paraMap));
        result.setTotalCount(makeIndcService.getWgtchkDiaryListCount(paraMap));

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

    @PostMapping(value="/makeIndcPrintList")
    public Result makeIndcPrintList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "vsvc.MakeController.makeIndcPrintList => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMakeIndcPrintList(paraMap));

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

    @PostMapping(value="/makeIndcBfPrintList")
    public Result makeIndcBfPrintList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "vsvc.MakeController.makeIndcBfPrintList => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMakeIndcBfPrintList(paraMap));

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

    @PostMapping(value ="/procCtntList")
    public Result getProcCtntList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "vsvc..MakeController.getProcCtntList => ";
        log.info(tag + "paraMap = " +paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getProcCtntList(paraMap));

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

    @PostMapping(value = "/matchIndcList")
    public Result getMatchIndcList(@RequestBody Map<String,Object> paraMap , HttpSession session){
        String tag = "vsvc..MakeController.getMatchIndcList => ";
        log.info(tag + "paraMap = " +paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getMatchIndcList(paraMap));

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
    /*생신계획에서 자재필요량 검색시 사용*/
    @PostMapping(value = "/needProdBomList")
    public Result needProdBomList(@RequestBody Map<String,Object> paraMap , HttpSession session){
        String tag = "vsvc..MakeController.needProdBomList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getNeedProdBomList(paraMap));

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

    @PostMapping(value = "/indcListByProc")
    public Result getIndcListByProc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "MakeController.getIndcListByProc => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(makeIndcService.getIndcListByProc(paraMap));

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

    @PostMapping(value = "/indcSaltList")
    public Result getIndcSaltList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "MakeController.getIndcSaltList => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(makeIndcService.getIndcSaltList(paraMap));
        result.setTotalCount(makeIndcService.getIndcSaltListCount(paraMap));

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

    @PostMapping(value = "/saveIndcSaltList")
    public Result saveIndcSaltList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "MakeController.saveIndcSaltList => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.saveIndcSaltList(paraMap);

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

    @PostMapping(value = "/saveIndcPrintText")
    public Result saveIndcPrintText(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "MakeController.saveIndcSaltList => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        makeIndcService.saveIndcPrintText(paraMap);

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

    @PostMapping(value="/planSave")
    public Result planSave(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.mpInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        makeIndcService.planSave(paraMap);

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
     * 금속검출기 시작버튼 클릭
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/metalDetectOper")
    public Result startMetal(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        String scadaApiTp = paraMap.get("scadaApiTp").toString();
        String scadaApi = "";
        Map<String,Object> rmap = null;
        int liCustNo = custNo.intValue();
        switch (liCustNo) {
            case 10 : rmap = metal10.metalDetectOper(paraMap); break;
        }
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

    @PostMapping(value = "/getMetalData")
    public Result getMetalData(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        result.setData(metal10.getMetalData(paraMap));
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
     * 금속검출기 시작버튼 클릭
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/saveMetalLog")
    public Result saveMetalLog(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "MakeController.startMetalOper => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        switch (custNo.intValue()) {
            case 2 : //서울식품
                modbusService.startOper(paraMap);
                makeIndcService.saveIndcPrintText(paraMap);
                break;
        }
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
     * 금속검출기 시작버튼 클릭
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/getCurMetalLog")
    public Result getCurMetalLog(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "MakeController.getCurMetalLog => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        Map<String,Object> rmap = new HashMap<String,Object>();

        switch (custNo.intValue()) {
            case 2 : //서울식품
                break;
            case 10 : //유진물산
                rmap = metal10.getCurMetalLog(paraMap);
                break;
        }

        //rmap = metal10.getCurMetalLog(paraMap);
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


    /**
     * 생산계획수립
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/saveMakePlan")
    public Result saveMakePlan(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "MakeController.saveMakePlan => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        makeIndcService.saveMakePlan(paraMap);
        return result;
    }
}
