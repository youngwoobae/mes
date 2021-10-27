package daedan.mes.make.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.make.service.MakeIndcService;
import daedan.mes.modbus.tcp.service.TcpService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping("/api/daedan/mes/make")
public class MakeController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;
    @Autowired
    private MakeIndcService moService;

    @Autowired
    private TcpService tcpService;


    @PostMapping(value="/conditionEmpIndcMp")
    public Result conditionEmpIndcMp(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.conditionEmpIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_proc")));
        rmap.put("comboProc", codeService.getComboCodeList(paraMap));

        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/conditions620")
    public Result conditions620(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.conditionEmpIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.indcTp")));
        paraMap.put("selectStr", "지시경로선택");
        rmap.put("comboIndcTp", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.indcStsTp")));
        paraMap.put("selectStr", "지시상태선택");
        rmap.put("comboIndcStses", codeService.getComboCodeList(paraMap));

        result.setData(rmap);
        return result;
    }


    @PostMapping(value="/comboWorkDay")
    public Result comboWorkDay(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.comboWorkDay => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        log.info(tag + "params = " + paraMap.toString());
        result.setData(moService.getComboWorkDay(paraMap));
        return result;
    }
    @PostMapping(value="/conditionEmbIndc")
    public Result comboEmbIndc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.comboEmbIndc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();
        String typeCd = paraMap.get("typeCd").toString();
        log.info("123 paraMap : " + paraMap);
        try {
            if (typeCd.equals("date")) {
                rmap.put("workDays", moService.getComboWorkDay(paraMap));
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
            rmap.put("startProc", moService.getStartProc(paraMap)); //최초공정
        }
        result.setData(rmap);
        return result;
    }

    /*생산계획에서 사용중인 콤보주문거래서 생성용*/
    @PostMapping(value="/comboOrdrCmpyList")
    public Result comboOrdrCmpyList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "makeController.comboOrdrCmpyList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getComboOrdrCmpyList(paraMap));
        return result;
    }

    @PostMapping(value="/makeIndcList")
    public Result makeIndcList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        try {
            paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
            paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        }
        catch (NullPointerException ne) {

        }
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(moService.getMakeIndcList(paraMap));
        result.setTotalCount(moService.getMakeIndcListCount(paraMap));
        return result;
    }
    @PostMapping(value="/makeIndcInfo")
    public Result makeIndcInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMakeIndcInfo(paraMap));
        return result;
    }

    @PostMapping(value="/autoSaveIndcMp")
    public Result autoSaveIndcMp(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "makeController.autoSaveIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.autoSaveIndcMp(paraMap);
        return result;
    }

    /*공정별 하루 최대 생산캐퍼 추출*/
    @PostMapping(value="/maxMakeCapacity")
    public Result maxMakeCapacity(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.maxMakeCapacity => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMaxMakeCapacity(paraMap));
        return result;
    }


    /*테블릿 오퍼레이션시 선택가능한 상품목록 추출 (작업지시를 기반으로 함)*/
    @PostMapping(value="/operProdList")
    public Result operProdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.operProdList => ";
        log.info(tag);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("baseUrl",env.getProperty("base_file_url"));
        result.setData(moService.getOperProdList(paraMap));
        return result;
    }

    /*작업시시별 하루 최대 생산캐퍼 추출*/
    @PostMapping(value="/maxMakeCapacityPerDay")
    public Result maxMakeCapacityPerDay(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.maxMakeCapacityPerDay => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMaxMakeCapacityPerDay(paraMap));
        return result;
    }
    /*작업현황에서 drag & drop으로 작업기간을 옮긴경*/
    @PostMapping(value="/resetMakeTermByDragDrop")
    public Result resetIndcDt(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "makeController.resetMakeTermByDragDrop => ";
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        moService.resetMakeTermByDragDrop(paraMap);
        return result;
    }

    /*상품정보를 기반한 제고공정 전체 작업지시 일괄생성 - 생산계획*/
    @PostMapping(value="/saveMakeIndcFullByPlan")
    public Result saveMakeIndcFullByPlan(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcFullByPlan => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        moService.saveMakeIndcFullByPlan(paraMap);
        return result;
    }

    /*상품정보를 기반한 제고공정 전체 작업지시 일괄생성*/
    @PostMapping(value="/saveMakeIndcFull")
    public Result saveMakeIndcFull(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcFull => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        result.setData(moService.saveMakeIndcFull(paraMap));
        return result;
    }

    /**/
    @PostMapping(value="/getProdIndcNo")
    public Result getProdIndcNo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.getProdIndcNo => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        result.setData(moService.getProdIndcNo(paraMap));
        return result;
    }

    /*작업지시 기본정보 저장*/
    @PostMapping(value="/saveMakeIndc")
    public Result saveMakeIndc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndc => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        log.info(tag + "procMap = " + paraMap.toString());
        moService.saveMakeIndc(paraMap);
        return result;
    }
    @PostMapping(value="/dropMakeIndc")
    public Result dropMakeIndc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.dropMakeIndc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.dropMakeIndc(paraMap);
        return result;
    }

    /*투입인력생성*/
    @PostMapping(value="/saveMakeIndcMp")
    public Result saveMakeIndcMp(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveMakeIndcMp => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.saveMakeIndcMp(paraMap);
        return result;
    }

    /*작업지시상세-투입인력정*/
    @PostMapping(value="/makeIndcMpInfo")
    public Result makeIndcMpInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcMpInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());

        result.setData(moService.getMakeIndcMpInfo(paraMap));
        return result;
    }
    /*작업지시상세-투입인력목록*/
    @PostMapping(value="/makeIndcMpList")
    public Result makeIndcMpList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcMpList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ocpnKind", Long.parseLong(env.getProperty("ocpn_kind_blue").toString()));
        log.info(tag + "paraMap = " + paraMap.toString());

        result.setData(moService.getMakeIndcMpList(paraMap));
        result.setTotalCount(moService.getMakeIndcMpListCount(paraMap));
        return result;
    }
    @PostMapping(value="/makeStatusList")
    public Result makeStatusList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeStatusList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(moService.getMakeStatusList(paraMap));
        result.setTotalCount(moService.getMakeStatusListCount(paraMap));
        return result;
    }

    /*시간(h*60 + m -> 시,분 스트링으로 변환)*/
    @PostMapping(value="/timeString")
    public Result cnvHmToTimeStr(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.timeStrinfg => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(moService.getTimeString(paraMap));
        return result;
    }


    /*작업지시상세-작업지시관련 자재목록 추출*/
    @PostMapping(value="/reqMatrList")
    public Result reqMatrList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.reqMatrList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(moService.getReqMatrList(paraMap));
        result.setTotalCount(moService.getReqMatrListCount(paraMap));
        return result;
    }

    /*작업지시상세-작업지시관련 자재목록 추출*/
    @PostMapping(value="/saveReqMatr")
    public Result saveReqMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) throws IOException, NullPointerException  {
        String tag = "makeController.saveReqMatr => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.saveReqMatr(paraMap);
        return result;
    }

    /*공정별 작업처리결과 추출*/
    @PostMapping(value="/makeIndcRsltInfo")
    public Result makeIndcRsltInfo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.makeIndcRsltInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMakeIndcRsltInfo(paraMap));;
        return result;

    }

    /*공번번호로 공정 추출*/
    @PostMapping(value="/makeIndcProc")
    public Result makeIndcProc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.makeIndcProc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("indcNo",Long.parseLong(paraMap.get("indc_no").toString()));
        result.setData(moService.makeIndcProc(paraMap));;
        return result;
    }
    /*공정별 작업처리결과 저장*/
    @PostMapping(value="/saveIndcRslt")
    public Result saveMakeIndcRslt(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.saveIndcRslt => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.saveIndcRslt(paraMap);
        return result;
    }
    @PostMapping(value="/dropIndcRslt")
    public Result dropIndcRslt(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.dropIndcRslt => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.dropIndcRslt(paraMap);
        return result;
    }

    @PostMapping(value="/exportPlanList")
    public Result exportPlanList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeIndcPlanList => ";
//        paraMap.put("ordSts",Long.parseLong(env.getProperty("ord_status.wait.owh"))); //출고대
        paraMap.put("ordSts",null); //출고대
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getExportPlanList(paraMap));
        return result;
    }
    /*생산현황:입고상태인자료만 대상으로 함.*/
    @PostMapping(value="/makeStatList")
    public Result MakeStatList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info("maekStatListController.paraMap = " + paraMap.toString());//kill
        result.setData(moService.getMakeStatList(paraMap));
        result.setTotalCount(moService.getMakeStatListCount(paraMap));
        return result;
    }

    /*생산일보*/
    @PostMapping(value="/makeDailyReportList")
    public Result MakeDailyReportList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.MakeDailyReportList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("procCd",Long.parseLong(env.getProperty("proc.code.complete")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moService.getMakeDailyReportList(paraMap));
        result.setTotalCount(moService.getMakeDailyReportListCount(paraMap));
        return result;
    }
    /*생산결과*/
    @PostMapping(value="/makeIndcRsltList")
    public Result makeIndcRsltList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.MakeDailyReportList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moService.getMakeIndcRsltList(paraMap));
        result.setTotalCount(moService.getMakeIndcRsltListCount(paraMap));
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
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getWorkerList(paraMap));

        return result;
    }
    /*투입인력총괄*/
    @PostMapping(value="/mpUsedList")
    public Result mpUsedList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.mpUsedList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ocpnKind", Long.parseLong(env.getProperty("ocpn_kind_blue").toString()));
        log.info(tag + "paraMap = " + paraMap.toString());

        result.setData(moService.getMpUsedList(paraMap));
        result.setTotalCount(moService.getMpUsedListCount(paraMap));
        return result;
    }
    /*투입인력상세*/
    @PostMapping(value="/mpUsedDetlList")
    public Result mpUsedDetlList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.mpUsedDetlList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        log.info("emplKind = " + paraMap.get("emplKind"));
        result.setData(moService.getMpUsedDetlList(paraMap));
        result.setTotalCount(moService.getMpUsedDetlListCount(paraMap));
        return result;
    }
    /*투입인력정보저장*/
    @PostMapping(value="/saveMpInfo")
    public Result saveMpInfo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.mpInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.saveMpInfo(paraMap);
        return result;
    }
    /*투입인력 삭제*/
    @PostMapping(value="/mpdropInfo")
    public Result mpdropInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        moService.getMpDropInfo(paraMap);

        return result;
    }
    /*불량품목록*/
    @PostMapping(value="/faultList")
    public Result faultList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.faultList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        try {
            paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
            paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        }
        catch (NullPointerException ne) {

        }
        result.setData(moService.getFaultList(paraMap));
        result.setTotalCount(moService.getFaultListCount(paraMap));
        return result;
    }

    /*엑셀기반 부자재생성*/
    @PostMapping(value="/mpByExcel") //부자재정보저장
    public Result MpByExcel(@RequestBody HashMap<String, Object> paraMap , HttpServletRequest request, HttpSession session) {
        String tag = "MatrController.makeRawMatByExcel => ";
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Result result = Result.successInstance();
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String resource = "config/mes.properties";
        Properties properties = new Properties();
        try {
            String fileRoot = uvo.getCustInfo().getFileRoot();
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            paraMap.put("matrTp", Integer.parseInt(properties.getProperty("rawmatr_cd")));
            paraMap.put(fileRoot,fileRoot);
            moService.loadMpByExcel(paraMap);
             } catch (Exception e) {
            e.printStackTrace();
            Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));
        }
        return result;
    }
    /*작업현황*/
    @PostMapping(value="/makeStatusReport")
    public Result makeStatusReport(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makeStatusReport => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("baseProcNo",Long.parseLong(env.getProperty("code_base_proc")));
        log.info(tag + "params = " + paraMap.toString());
        Result result = Result.successInstance();
        result.setData(moService.getMakeStatusReport(paraMap));
        result.setTotalCount(moService.getMakeStatusReportCount(paraMap));
        return result;
    }


    /*작업시시별 하루 최대 생산캐퍼 추출*/
    @PostMapping(value="/makePlanList") //생산계획
    public Result makePlanList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makePlanList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moService.getMakePlanList(paraMap));
        return result;
    }

    @PostMapping(value="/makePlanOrdList") //생산계획 반영용 주문상목목록
    public Result makePlanOrdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.makePlanOrdList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageSz", 10);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moService.getMakePlanProdList(paraMap));
        result.setTotalCount(moService.getMakePlanProdListCount(paraMap));
        return result;
    }

    @PostMapping(value="/comboProdProcList") //제품생산공정목록
    public Result comboProdProcList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.prodProdList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getComboProdProcList(paraMap));
        return result;
    }
    @PostMapping(value="/makeMainProc") //초기(메인)_공정 추출
    public Result makeMainProc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.getMakeMainProc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMakeMainProc(paraMap));
        return result;
    }
    @PostMapping(value="/makeExtraProc") //상품별 제조공정 이외의 추가 추출용
    public Result makeExtraProc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.getMakeExtraProc => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMakeExtraProc(paraMap));
        return result;
    }
    @PostMapping(value="/condition104ProcStd")
    public Result condition104ProcStd(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.condition104ProcStd => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("procList", moService.getMakeProcList(paraMap)); //최초공정
        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/getMoniterItemList")
    public Result getMoniterItemList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "makeController.condition104ProcStd => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("procList", moService.getMoniterItemList(paraMap)); //최초공정
        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/manPowerSummaryList")
    public Result manPowerSummaryList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.manPowerSummaryList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String, Object>();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
        paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(moService.getManPowerSummaryList(paraMap));
        result.setTotalCount(moService.getManPowerSummaryListCount(paraMap));
        return result;
    }
    @PostMapping(value="/manPowerList")
    public Result manPowerList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.manPowerList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String, Object>();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("mpUsedDt", paraMap.get("mpUsedDt").toString().substring(0,10));

        result.setData(moService.getManPowerList(paraMap));
        result.setTotalCount(moService.getManPowerListCount(paraMap));
        return result;
    }
    @PostMapping(value="/wgtchkSummaryList")
    public Result wgtchkSummaryList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.wgtchkSummaryList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(moService.getWgtchkSummaryList(paraMap));
        result.setTotalCount(moService.getWgtchkSummaryListCount(paraMap));
        return result;
    }
    @PostMapping(value="/wgtchkDiareyList")
    public Result wgtchkDiaryList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "makeController.wgtchkDiaryList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(moService.getWgtchkDiaryList(paraMap));
        result.setTotalCount(moService.getWgtchkDiaryListCount(paraMap));
        return result;
    }

    @PostMapping(value="/makeIndcPrintList")
    public Result makeIndcPrintList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "vsvc.MakeController.makeIndcPrintList => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMakeIndcPrintList(paraMap));

        return result;
    }

    @PostMapping(value="/makeIndcBfPrintList")
    public Result makeIndcBfPrintList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "vsvc.MakeController.makeIndcBfPrintList => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMakeIndcBfPrintList(paraMap));

        return result;
    }

    @PostMapping(value ="/procCtntList")
    public Result getProcCtntList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "vsvc..MakeController.getProcCtntList => ";
        log.info(tag + "paraMap = " +paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getProcCtntList(paraMap));

        return result;
    }

    @PostMapping(value = "/matchIndcList")
    public Result getMatchIndcList(@RequestBody Map<String,Object> paraMap , HttpSession session){
        String tag = "vsvc..MakeController.getMatchIndcList => ";
        log.info(tag + "paraMap = " +paraMap.toString());

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getMatchIndcList(paraMap));

        return result;
    }
    /*생신계획에서 자재필요량 검색시 사용*/
    @PostMapping(value = "/needProdBomList")
    public Result needProdBomList(@RequestBody Map<String,Object> paraMap , HttpSession session){
        String tag = "vsvc..MakeController.needProdBomList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        if (Long.parseLong(env.getProperty("cust_no")) == 3){
            result.setData(moService.getHdfdNeedProdBomList(paraMap));
        }else if(Long.parseLong(env.getProperty("cust_no")) ==2){
            result.setData(moService.getSfNeedProdBomList(paraMap));
        }else{
            result.setData(moService.getNeedProdBomList(paraMap));
        }
        return result;
    }

    @PostMapping(value = "/indcListByProc")
    public Result getIndcListByProc(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "MakeController.getIndcListByProc => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(moService.getIndcListByProc(paraMap));
        return result;
    }

    @PostMapping(value = "/indcSaltList")
    public Result getIndcSaltList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        String tag = "MakeController.getIndcSaltList => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moService.getIndcSaltList(paraMap));
        result.setTotalCount(moService.getIndcSaltListCount(paraMap));
        return result;
    }

    @PostMapping(value = "/saveIndcSaltList")
    public Result saveIndcSaltList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "MakeController.saveIndcSaltList => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.saveIndcSaltList(paraMap);
        return result;
    }

    @PostMapping(value = "/saveIndcPrintText")
    public Result saveIndcPrintText(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "MakeController.saveIndcSaltList => ";
        log.info(tag + "paraMap = " +paraMap.toString());
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        moService.saveIndcPrintText(paraMap);
        return result;
    }



}
