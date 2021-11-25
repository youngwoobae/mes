package daedan.mes.matr.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.matr.service.MatrService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserWork;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/matr")
public class MatrController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MatrService matrService;

    @Autowired
    private UserService userService;

    @PostMapping(value="/initMatrCmpyCart") //원자재구매처카트 초기화:2020.08.14 사용중
    public Result initOrtCart(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        matrService.initMatrCmpyCart(paraMap);
        return result;
    }


    @PostMapping(value="/matrList/{path}")
    public Result matrList(@PathVariable(name = "path") String szPath , @RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        List<Map<String, Object>> list = null;
        int count = 0;
        try {
            if (szPath.equals("matr")) {
                list = matrService.getMatrList(paraMap);
                count = matrService.getMatrListCount(paraMap);
            }
            else if (szPath.equals("cmpy")) {
                list = matrService.getMatrWithCmpyList(paraMap);
                count = matrService.getMatrWithCmpyListCount(paraMap);
            }
        }
        catch(NullPointerException ne) {
            list = matrService.getMatrList(paraMap);
            count = matrService.getMatrListCount(paraMap);
        }

        result.setData(list);
        result.setTotalCount(count);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/condition200Matr")
    public Result condition200Matr(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Integer.parseInt(env.getProperty("code.base.save_tmpr_cd")));

        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboSaveTmpr", codeService.getComboCodeList(paraMap));
        result.setData(rmap);

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

    @PostMapping(value="/popMatrCond")
    public Result popMatrCond(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Integer.parseInt(env.getProperty("base.matr_type_cd")));

        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboMatrTp", codeService.getComboCodeList(paraMap));

        result.setData(rmap);
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
    @PostMapping(value="/matrCurrStkCond")
    public Result matrCurrStkCond(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Integer.parseInt(env.getProperty("base.matr_type_cd")));

        Map<String, Object> rmap = new HashMap<String,Object>();

        rmap.put("comboMatrTp", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd")));
        rmap.put("comboSaveTmpr",codeService.getComboCodeList(paraMap));
        result.setData(rmap);

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/matrCurrStkList")
    public Result matrCurrStkList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        List <Map<String,Object>> rmap = matrService.getMatrCurrStkList(paraMap);
 
        result.setData(rmap);
        result.setTotalCount(matrService.getMatrCurrStkListCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value="/rawmatrList") //원자재
    public Result rawmatrList(@RequestBody Map<String, Object> paraMap,HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("matrTp",Integer.parseInt(env.getProperty("code.matrtp.matr")));

        result.setData(matrService.getMatrList(paraMap));
        result.setTotalCount(matrService.getMatrListCount(paraMap));
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

    @PostMapping(value="/rawMatrExcelList") //원자재 - 엑셀출력용
    public Result rawMatrExcelList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("matrTp",Integer.parseInt(env.getProperty("code.matrtp.matr")));

        result.setData(matrService.getMatrExcelList(paraMap));
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

    @PostMapping(value="/subMatrList") //부자재
    public Result subMatrList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("matrTp",Integer.parseInt(env.getProperty("code.matrtp.submatr")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getMatrList(paraMap));
        result.setTotalCount(matrService.getMatrListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/subMatrExcelList") //부자재 -- 엑셀출력용
    public Result subMatrExcelList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("matrTp",Integer.parseInt(env.getProperty("code.matrtp.submatr")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getMatrExcelList(paraMap));
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


    @PostMapping(value="/seasoningList") //부자재
    public Result seasoningList(@RequestBody Map<String, Object> paraMap,HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("matrTp",Integer.parseInt(env.getProperty("code.matrtp.submatr")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getMatrList(paraMap));
        result.setTotalCount(matrService.getMatrListCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }


    @PostMapping(value="/matrCmpyList") //원,부자재 구매처리스트
    public Result matrCmpyList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getMatrCmpyList(paraMap));
        result.setTotalCount(matrService.getMatrCmpyListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        AccHstr vo = (AccHstr)session.getAttribute("acchstr");
        userService.saveAccLogEvnt(custNo,vo.getAccNo(), EvntType.READ,2);
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    @PostMapping(value="/matrCmpyComboList") //콤보박스용 원,부자재 구매처트 리스트
    public Result matrCmpyComboList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(matrService.getMatrCmpyComboList(paraMap));
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
    @PostMapping(value="/comboEmbMatr") //자재정보관리 콤보박스 처리용
    public Result comboEmbMatr(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();

        Map<String, Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd"))); //보관온도
        rmap.put("saveTmpr",codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.unit"))); //구매단위
        rmap.put("pursUnit",codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.madein"))); //원산지
        rmap.put("madein",codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.madeby"))); //소재
        rmap.put("madeby",codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.mngrbase"))); //관리기준
        rmap.put("comboMngrBase",codeService.getComboCodeList(paraMap));

        rmap.put("saveWhNos",codeService.getComboMatrWhList(paraMap));
        /*KMJ 21.11.06 : 불필요할 것으로 보임 ,
           1. code.base.used_tp(2900) 이 파렛트유형과 혼용되고 있음. :
           2. 상품정보에서도 동일한 코드가 존재함.
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.used_tp"))); //원료구분
        rmap.put("usedTp",codeService.getComboCodeList(paraMap));
        */
        result.setData(rmap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 5);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/cmpyMatrCombo") //콤보박스용 구매처별 취급 자재 리스
    public Result cmpyMatrComboList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(matrService.getCmpyMatrCombo(paraMap));
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

    @PostMapping(value="/matrInfo") //원,부자재정보
    public Result matInfo(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String, Object> info = matrService.getMatrInfo((paraMap));
        result.setData(info);
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

    @PostMapping(value="/saveMatrCmpies")
    public Result saveMatrCmpy(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        matrService.saveMatrCmpy(paraMap);
        result.setData(matrService.getMatrCmpyList(paraMap));
        result.setTotalCount(matrService.getMatrCmpyListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/saveMatrCmpyCart") //선택된원부자재구매처 카트에 추가
    public Result matrCompanies(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        matrService.saveMatrCmpyCart(paraMap);

        result.setData(matrService.getMatrCmpyList(paraMap));
        result.setTotalCount(matrService.getMatrCmpyListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    /*상품별 bom정보저장(엑셀연도시 사용중인 듯)*/
    @PostMapping(value="/saveProdBom")
    public Result saveProdBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("fileRoot",uvo.getCustInfo().getCustNo());
        try {
            Integer bomLvl = Integer.parseInt(paraMap.get("bomLvl").toString());
        }
        catch (NullPointerException ne) {
            paraMap.put("bomLvl",1);
        }
        matrService.saveProdBom(paraMap);
        result.setData(matrService.getMatrProdList(paraMap));
        result.setTotalCount(matrService.getMatrProdListCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    @PostMapping(value="/saveMatr") //원부재정보저장
    public Result saveMatr(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        Map<String,Object> matrMap = new HashMap<String,Object>();
        matrMap = (Map<String,Object>)paraMap.get("matrInfo");
        log.info("matrInfo 내용 ==> " + matrMap);
        matrMap.put("ipaddr", NetworkUtil.getClientIp(request));
        matrMap.put("userId", paraMap.get("userId"));
        paraMap.put("matrInfo",matrMap);
        paraMap.put("savePath","matr");

        matrService.saveMatr(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        UserWork uhvo = (UserWork)session.getAttribute("uhvo");
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/saveSafeStk") //자재별안전재고정보 저장
    public Result saveSafeStk(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String, Object> passMap = (Map<String, Object>)paraMap.get("safeStk");
        passMap.put("custNo", custNo);
        matrService.saveSafeStk(passMap);

        passMap.put("matrNo",passMap.get("matrNo"));
        result.setData(matrService.getSafeStk(passMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/safeStk") //안전재고정보
    public Result safeStk(@RequestBody HashMap<String, Object> paraMap,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        result.setData(matrService.getSafeStk(paraMap));
        return result;
    }

    @PostMapping(value="/deleteMatr") //자재정보삭제
    public Result dropMatr(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        matrService.deleteMatr(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }


    @PostMapping(value="/makeRawMatByExcel") //부자재정보저장
    public Result loadRawMatByExcel(@RequestBody HashMap<String, Object> paraMap,HttpSession session) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        matrService.loadRawMatByExcel(paraMap);
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

    @PostMapping(value="/matrProdList") //자재사용제품목록
    public Result matrProdList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getMatrProdList(paraMap));
        result.setTotalCount(matrService.getMatrProdListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/cmpyMatrList") //자재취급구매처목록
    public Result cmpyMatrList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(matrService.getCmpyMatrList(paraMap));
        result.setTotalCount(matrService.getCmpyMatrListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/dropMatrCmpy") //선택된 품명을 기준하여 품목 bom 추가
    public Result dropMatrCmpy(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);


        Map<String, Object> passMap = new HashMap<String, Object>();
        passMap.put("cmpyList",paraMap.get("cmpyList"));
        passMap.put("ipaddr",NetworkUtil.getClientIp(request));
        passMap.put("custNo",paraMap.get("custNo"));

        passMap.put("matrNo",paraMap.get("matrNo"));
        passMap.put("userId",paraMap.get("userId"));

        matrService.dropMatrCmpy(passMap);
        result.setData(matrService.getMatrCmpyList(passMap));
        result.setTotalCount(matrService.getMatrCmpyListCount(passMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value="/dropMatrProd") //선택된 품명을 기준하여 품목 bom 추가
    public Result dropMatrProd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String, Object> passMap = new HashMap<String, Object>();
        passMap.put("ipaddr", NetworkUtil.getClientIp(request));
        passMap.put("prodList",paraMap.get("prodList"));
        passMap.put("matrNo",paraMap.get("matrNo"));
        passMap.put("userId",paraMap.get("userId"));
        passMap.put("custNo",paraMap.get("custNo"));
        matrService.dropMatrProd(passMap);
        result.setData(matrService.getMatrProdList(paraMap));
        result.setTotalCount(matrService.getMatrProdListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value="/prodMatrList")
    public Result prodMatrList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getProdMatrList(paraMap));
        result.setTotalCount(matrService.getProdMatrListCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/embCalendarSave")
    public Result embCalendarSave(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        matrService.embCalendarSave(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/stockList") //자재사용제품목록
    public Result getStockList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        result.setData(matrService.getStockList(paraMap));
        return result;
    }

    @PostMapping(value="/matrIwhList") //자재, 보유자재 개수 리스트
    public Result matrIwhList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getMatrIwhList(paraMap));
        result.setTotalCount(matrService.getMatrIwhListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value="/saveMatrStk") //자재별 창고지정해서 입고처리
    public Result saveMatrStk(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(matrService.saveMatrStkInfo(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/procList")
    public Result procList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(matrService.getProcList(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value="/getAnaMatrList")
    public Result getAnaMatrList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(matrService.getAnaMatrList(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value = "/matrStatExcel") //자재현황 엑셀 다운로드 기능.
    public void matrStatExcel(@RequestBody Map<String, Object> paraMap, HttpServletResponse response  , HttpSession session)throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        matrService.matrStatExcel(paraMap, response);
    }

    @PostMapping(value="/matrExcelIwh")
    public Result matrExcelIwh( @RequestBody HashMap<String, Object> paraMap
                              , HttpSession session) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        result.setData( matrService.matrExcelIwh(paraMap));
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

    @PostMapping(value = "/checkRetnList")
    public Result checkRetnList(@RequestBody Map<String,Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(matrService.getCheckRetnList(paraMap));
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

    @PostMapping(value = "/comeDateMatrList")
    public Result getComeDateMatrList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("dateFr", paraMap.get("findValidFrDt").toString().substring(0,10));
        paraMap.put("dateTo", paraMap.get("findValidToDt").toString().substring(0,10));

        result.setData(matrService.getComeDateMatrList(paraMap));
        result.setTotalCount(matrService.getComeDateMatrListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }



    @PostMapping(value="/svMatrSaveExcelUpLoad") //부자재정보저장
    public Result svMatrSaveExcelUpLoad(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        paraMap.put("userId",Long.parseLong(paraMap.get("userId").toString()));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        matrService.svMatrSaveExcelUpLoad(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        result.setData("OK");
        return result;
    }

    @PostMapping(value = "/matrPursUnit")
    public Result matrPursUnit(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("parCodeNo" ,Long.parseLong(env.getProperty("code.base.unit").toString()));
        result.setData(matrService.getmatrPursUnit(paraMap));
        result.setTotalCount(matrService.getmatrPursUnitCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/saveMatrPursUnit") //원부재정보저장
    public Result saveMatrPursUnit(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId",Long.parseLong(paraMap.get("userId").toString()));

        matrService.saveMatrPursUnit(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    //구매단위삭제
    @PostMapping(value="/dropPursUnit")
    public Result dropPursUnit(@RequestBody Map<String,Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        matrService.dropPursUnit(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value = "/safeStkMatrList")
    public Result getSafeStkMatrList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(matrService.getSafeStkMatrList(paraMap));
        result.setTotalCount(matrService.getSafeStkMatrListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value = "/getMatrInspUser")
    public Result getMatrInspUser(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(matrService.getMatrInspUser(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 2);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
}
