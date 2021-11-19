package daedan.mes.purs.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.purs.service.PursService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/api/daedan/mes/purs")

public class PursController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private PursService pursService;

    @Autowired
    private UserService userService;

    @PostMapping(value="/matrPursCond")
    public Result matrPursCond(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        pursService.initTempPursMatr(paraMap);
        return result;
    }

    @PostMapping(value="/embPursCond")
    public Result embPursCond(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_purs_cd")));
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("comboPurStat", codeService.getComboCodeList(paraMap));


        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result.setData(rmap);
    }

    @PostMapping(value="/pursCmpyConditions") //구매처별원자재구매목록
    public Result pursCmpyConditions(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        Map<String,Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.matrst")));
        rmap.put("comboMatrStat",codeService.getComboCodeList(paraMap));
        rmap.put("comboWh",pursService.getWhList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value="/pursList") //구매처별원자재구매목록
    public Result pursList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        try {
            String pageSz = paraMap.get("pageSz").toString();
        }
        catch (NullPointerException ne) {
            paraMap.put("pageSz",10);
        }
        List<Map<String,Object>> list = null;
        int count = 0;
        result.setData(pursService.getPursList(paraMap));
        result.setTotalCount(pursService.getPursListCount(paraMap));
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
    @PostMapping(value="/pursRawMatrList") //원자재구매목록
    public Result pursRawMatrList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        paraMap.put("matrTp",Integer.parseInt(env.getProperty("code.matrtp.matr"))); //원자재
        result.setData(pursService.getPursList(paraMap));
        result.setTotalCount(pursService.getPursListCount(paraMap));
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

    @PostMapping(value="/pursSubMatrList") //원자재구매목록
    public Result pursSubMatrList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        List<Map<String,Object>> list = null;
        int count = 0;


        paraMap.put("matrTp",Integer.parseInt(env.getProperty("submatr_cd"))); //부자재

        list = pursService.getPursList(paraMap);
        count = pursService.getPursListCount(paraMap);
        result.setData(list);
        result.setTotalCount(count);
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

    @PostMapping(value="/condition210_MatrPurs") //구매정보치리용
    public Result condition210_MatrPurs(HashMap<String, Object> paraMap,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.purs.sts"))); //직종구분(사무직,생산직)
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("comboPursSts", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }


    @PostMapping(value="/pursInfo") //구매정보
    public Result pursInfo(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(pursService.getPursInfo(paraMap));
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

    @PostMapping(value="/pursInfoList") //구매현황
    public Result pursInfoList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("pursNo",Long.parseLong(env.getProperty("purs.sts.end"))); //입고 추출
        result.setData(pursService.pursInfoList(paraMap));
        result.setTotalCount(pursService.pursInfoListCount(paraMap));
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

    @PostMapping(value="/pursInfoMatrList") //구매현황 상세 리스트
    public Result pursInfoMatrList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("pursNo",paraMap.get("purs_no"));
        result.setData(pursService.pursInfoMatrList(paraMap));
        result.setTotalCount(pursService.pursInfoMatrListCount(paraMap));
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


    @PostMapping(value="/pursMatrInfo") //구매자재정보
    public Result pursMatrInfo(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(pursService.getPursMatrInfo(paraMap));
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
    //구매정보 저장
    @PostMapping(value="/savePursMatrTemp") //구매처별원자재구매목록
    public Result savePursMatrTemp(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        pursService.savePursMatrTemp(paraMap);

        result.setData(pursService.getPursList(paraMap));
        result.setTotalCount(pursService.getPursListCount(paraMap));
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
    //구매정보 저장
    @PostMapping(value="/savePursInfo")
    public Result savePursInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(pursService.savePursInfo(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    //구매정보 삭제
    @PostMapping(value="/dropPursInfo")
    public Result dropPursInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pursService.dropPursInfo(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    //구매자재정보 저장
    @PostMapping(value="/savePursMatr")
    public Result savePursMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pursService.savePursMatr(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    //일괄구매자재정보 저장
    @PostMapping(value="/savePursMatrList")
    public Result savePursMatrList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pursService.savePursMatrList(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    //구매자재정보 저장
    @PostMapping(value="/savePursReqMatr")
    public Result savePursReqMatr(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request,HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        pursService.savePursReqMatr(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    //구매자재정보 삭
    @PostMapping(value="/dropPursReqMatr")
    public Result dropPursReqMatr(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        pursService.dropPursReqMatr(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/needPursList")
    public Result needPursList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(pursService.getNeedPursList(paraMap));
        result.setTotalCount(pursService.getNeedPursListCount(paraMap));
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
    @PostMapping(value="/needMatrOrderList")
    public Result getNeedPursMatrList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        paraMap.put("ordSts", Long.parseLong(env.getProperty("ord_status_acpt")));
        result.setData(pursService.getNeedMatrOrderList(paraMap));
        result.setTotalCount(pursService.getNeedMatrOrderListCount(paraMap));
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
    @PostMapping(value="/getPursMatrList")
    public Result getPursMatrList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(pursService.getPursMatrList(paraMap));
        result.setTotalCount(pursService.getPursMatrListCount(paraMap));
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
    @PostMapping(value="/getPursMatrIwhList")
    public Result getPursMatrOwhList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pursService.getPursMatrIwhList(paraMap));
        result.setTotalCount(pursService.getPursMatrIwhListCount(paraMap));
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

    @PostMapping(value="/comboPursCmpy")
    public Result getComboPursCmpy(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        return result.setData(pursService.getComboPursCmpy(paraMap));

    }

    @PostMapping(value="/comboMatrCmpy")
    public Result comboMatrCmpyList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("mngrGbnCd", Long.parseLong(env.getProperty("code.mngrgbn.purs")));
        result.setData(pursService.getComboMatrCmpy(paraMap));
        return result;
    }

    @PostMapping(value="/isAbleCnfmPurs")
    public Result isAbleCnfmPurs(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pursSts", Long.parseLong(env.getProperty("purs.sts.req")));
        result.setData(pursService.isAbleCnfmPurs(paraMap));
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

    @PostMapping(value="/pursReqList")
    public Result getPursReqList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        // -> 자재구매 리스트에 발주 리스트가 출력 되지 않아 변경 21.07.07(KTH)
        paraMap.put("pursStsInsp",Long.parseLong(env.getProperty("purs.sts.insp")));
        paraMap.put("pursStsEnd",Long.parseLong(env.getProperty("purs.sts.end"))); //164
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("baseUrl", env.getProperty("base_file_url"));
        result.setData(pursService.getPursReqList(paraMap));
        result.setTotalCount(pursService.getPursReqListCount(paraMap));
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
    @PostMapping(value="/pursReqIndcList")
    public Result getPursReqIndcList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pursService.getPursReqIndcList(paraMap));
        result.setTotalCount(pursService.getPursReqIndcListCount(paraMap));
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
    @PostMapping(value="/pursReqMatrInfo")
    public Result pursReqMatrInfo(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(pursService.getPursReqMatrInfo(paraMap));
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

    @PostMapping(value="/pursReqOrdList")
    public Result pursReqOrdList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pursService.getPursReqOrdList(paraMap));
        result.setTotalCount(pursService.getPursReqOrdListCount(paraMap));
        return result;
    }

    @PostMapping(value="/setPursComplete")
    public Result setPursComplete(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pursSts", Long.parseLong(env.getProperty("purs.sts.end")));
        pursService.resetPursStatusEnd(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    /*구매의뢰 리스트에서 특정 구매처 선택후 일괄 구매처 설정*/
    @PostMapping(value="/resetPursCmpyByList")
    public Result resetPursCmpyByList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pursService.resetPursCmpyByList(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
       return result;
    }
    /*구매의뢰 리스트에서 특정 구매처 선택후 일괄 구매처 및 구매수 설정*/
    @PostMapping(value="/resetPursCmpyQtyByList")
    public Result resetPursCmpyByQtyList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        pursService.resetPursCmpyQtyByList(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    @PostMapping(value="/pursHstrList") //구매처별원자재구매목록
    public Result pursHstrList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("dateFr", paraMap.get("dateFr").toString().substring(0, 10));
        paraMap.put("dateTo", paraMap.get("dateTo").toString().substring(0, 10));
        List<Map<String,Object>> list = null;
        int count = 0;
        result.setData(pursService.getPursHstrList(paraMap));
        result.setTotalCount(pursService.getPursHstrListCount(paraMap));
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
