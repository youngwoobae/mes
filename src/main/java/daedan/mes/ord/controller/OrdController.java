package daedan.mes.ord.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.ord.service.OrdService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/ord")
public class OrdController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private OrdService ordService;

    @Autowired
    private OrdRepository ordRepository;

    @PostMapping(value = "/conditionsPopProdFind")
    public Result conditionsPopProdFind(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.ord_gb")));
        paraMap.put("selectStr", "주문구분선택");
        rmap.put("comboOrdTp", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/conditions1020")
    public Result conditions1020(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String,Object>();
        rmap.put("comboOrdTp", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/conditions410")
    public Result conditions410(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.ordstat")));
        paraMap.put("selectStr", "주문상태선택");

        Map<String, Object> rmap = new HashMap<String,Object>();
        rmap.put("comboOrdStat", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/conditions1030")
    public Result conditions1030(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String,Object>();
        rmap.put("comboFindYy", ordService.geOwhDateList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/ordInfoControl")
    public Result OrdInfoControl(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String,Object>();
        if (paraMap.get("cmpyNo") != null) { //AddOn By KMJ AT 21.07.13 - client length문제 제거
            rmap.put("dlvPlcList", ordService.getComboDlvPlc(paraMap));
        }

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.ordstat")));
        paraMap.put("selectStr", "주문상태선택");
        rmap.put("comboOrdSts", codeService.getComboCodeList(paraMap));

        /*
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_trk_cmpy"))); //운송업
        paraMap.put("selectStr","운송사선택");
        rmap.put("trkCmpyList",codeService.getComboCodeList(paraMap));
        */
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_ord_path")));
        paraMap.put("selectStr","주문경로선택");
        rmap.put("ordPathList",codeService.getComboCodeList(paraMap));

        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/initOrdCart") //주문정보
    public Result initOrtCart(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.initOrdCart(paraMap);
        return result;
    }

    @PostMapping(value = "/moveOrdProdToCart") //주문상품을 임시주문상품으로 이관
    public Result moveOrdProdToCart(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.moveOrdProdToCart(paraMap);
        return result;
    }
    @PostMapping(value = "/prodStockList") //전체(oem,odm)주문목록
    public Result prodStockList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getProcStockList(paraMap));
        result.setTotalCount(ordService.getProcStockListCount(paraMap));
        return result;
    }

    @PostMapping(value = "/ordList") //주문자생산
    public Result ordList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getOrdList(paraMap));
        result.setTotalCount(ordService.getOrdListCount(paraMap));
        return result;
    }

    @PostMapping(value="/exOrdProdList")
    public Result exOrdProdList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        int pageSz = 10;
        try {
            paraMap.get("pageSz").toString();
        }
        catch (NullPointerException ne) {
            paraMap.put("pageSz",10);
        }
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getExOrdProdList(paraMap));
        result.setTotalCount(ordService.getExOrdProdListCount(paraMap));
        return result;
    }
    @PostMapping(value="/ordProdList")
    public Result ordProdList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getOrdProdList(paraMap));
        result.setTotalCount(ordService.getOrdProdListCount(paraMap));
        return result;
    }



    @PostMapping(value = "/initOrdProdCart") //주문품목카드이동
    public Result initOrdProdCart(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        ordService.initOrdProdCart(paraMap);
        return result;
    }

    @PostMapping(value = "/ordProdInfo") //주문품목
    public Result ordProdInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getOrdProdInfo(paraMap));

        return result;
    }
    @PostMapping(value="/ordProdBomList") //주문품목별 BOM LIST
    public Result ordProdBomList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getOrdProdBomList(paraMap));
        result.setTotalCount(ordService.getOrdProdBomListCount(paraMap));
        return result;
    }

    @PostMapping(value="/ordProdCartList") //카트주문품목
    public Result ordProdCartList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        List<Map<String, Object>> list = ordService.getOrdProdCartList(paraMap);
        int count = ordService.getOrdProdCartListCount(paraMap);
        result.setData(list);
        result.setTotalCount(count);
        return result;
    }

    @PostMapping(value="/ordProdCartBomList") //카트주문품목별 BOM LIST
    public Result ordProdCartBomList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        List<Map<String,Object>>list = ordService.getOrdProdCartBomList(paraMap);
        int    count = ordService.getOrdProdCartBomListCount(paraMap);
        result.setData(list);
        result.setTotalCount(count);
        return result;
    }
    @PostMapping(value = "/ordInfo") //oem주문정보
    public Result ordInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<>();

        /*주문정보 바코드 이미지 출력할 것.*/
//        rmap.put("ordMap",ordService.getOrdInfo(paraMap));
//        rmap.put("barImg",)

        result.setData(ordService.getOrdInfo(paraMap));


        return result;
    }

    @PostMapping(value = "/oemOrdInfo") //oem주문정보
    public Result oemOrdInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getOemOrdInfo(paraMap));
        return result;
    }

    @PostMapping(value = "/prjOrdInfo") //자체영업주문정보
    public Result prjOrdInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getPrjOrdInfo(paraMap));
        return result;
    }
    @PostMapping(value = "/saveOrd") //신규주문저장
    public Result saveOrd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        ordService.saveOrd(paraMap);
        return result;
    }
    @PostMapping(value = "/updateOrd") //기존주문저장
    public Result updateOrd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.updateOrd(paraMap);
        return result;
    }


    @PostMapping(value = "/saveOrdProdCart") //카트에 주문품목추가(사용중지)
    public Result saveOrdProdCart(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ordService.saveOrdProdCart(paraMap);

        return result;
    }
    @PostMapping(value = "/apndOrdProd") //주문품목추가
    public Result apndOrdProd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.apndOrdProd(paraMap);
        return result;
    }


    @PostMapping(value = "/apndOrdProdToCart") //카트에 주문품목추가(사용중지)
    public Result apndOrdProdToCart(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.apndOrdProdToCart(paraMap);
        return result;
    }
    @PostMapping(value = "/addOnProdToCart") //주문품목카트추가(사용중지)
    public Result addOnProdToCart(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.addOnProdToCart(paraMap);
        return result;
    }

    @PostMapping(value = "/saveOrdProd") //주문품목정보수정(주문수량 및 단위)
    public Result saveOrdProd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.saveOrdProd(paraMap);
        result.setData(ordService.getOrdProdList(paraMap));
        result.setTotalCount(ordService.getOrdProdListCount(paraMap));

        return result;
    }

    @PostMapping(value = "/OrdProdBomList") //주문품목카트정보수정(주문수량 및 단위)
    public Result OrdProdBomList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.saveOrdProdCart(paraMap);
        result.setData(ordService.getOrdProdBomList(paraMap));
//        result.setTotalCount(ordService.getOrdProdBomListCount(paraMap));

        return result;
    }


    @PostMapping(value = "/dropOrdInfo") //주문삭제
    public Result dropOrdInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.dropOrdInfo(paraMap);
        return result;
    }

    @PostMapping(value = "/dropOrdProdCarts") //주문품목카트삭제(사용중지됨)
    public Result dropOrdProdCarts(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.dropOrdProdCarts(paraMap);
        return result;
    }
    @PostMapping(value = "/dropOrdProds") //주문품목삭제
    public Result dropOrdProds(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.dropOrdProds(paraMap);
        return result;
    }


    @PostMapping(value = "/dropOrdProdCart") //주문품목카트삭제
    public Result dropOrdProdCart(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.dropOrdProdCart(paraMap);
        return result;
    }


    @PostMapping(value = "/comboDlvPlc") //거래처별 납품장소 콤보구성
    public Result comboDlvPlc(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getComboDlvPlc(paraMap));
        return result;
    }

    @PostMapping(value = "/comboDlvPlcList") //거래처별 납품장소 콤보구성
    public Result comboDlvPlcList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        List<Map<String, Object>> list = ordService.getComboDlvPlcList(paraMap);
        result.setData(list);
        return result;
    }

    @PostMapping(value = "/firstHalfPerformList") //월실적보고서 - 상반기
    public Result firstHalfPerformList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getFirstHalfPerformList(paraMap));
        result.setTotalCount(ordService.getFirstHalfPerformListCount(paraMap));
        return result;
    }

    @PostMapping(value = "/secondHalfPerformList") //월실적보고서 - 상반기
    public Result secondHalfPerformList(@RequestBody Map<String, Object> paraMap,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(ordService.getSecondHalfPerformList(paraMap));
        result.setTotalCount(ordService.getSecondHalfPerformListCount(paraMap));

        return result;
    }

    @PostMapping(value="/ordIndfoByExcel")
    public Result ordIndfoByExcel(@RequestBody Map<String, Object> paraMap
                               , HttpServletRequest request, HttpSession session) throws Exception{
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("mngrGbnCd",env.getProperty("code.mngrgbn.purs")); //매입
        paraMap.put("cmpyTp", env.getProperty("code.cmpytp.cmpy")); //법인
        paraMap.put("session", session);
        Result result = Result.successInstance();
        ordService.ordIndfoByExcel(paraMap);
        return result;
    }

    /*부자재확인*/
    @PostMapping(value="/chkSubMatr")
    public Result chkSubMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ordService.chkSubMatr(paraMap);
        return result;
    }

    /*주문 제품 생산 가능 여부 저장*/
    @PostMapping(value ="/saveMakeAbleYn")
    public Result saveMakeAbleYn(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        ordService.saveMakeAbleYn(paraMap);

        return result;
    }

    /*생산계획 페이지에서 납품요청일자 변경*/
    @PostMapping(value = "/updateDlvReqDt")
    public Result updateDlvReqDt(@RequestBody Map<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ordService.updateDlvReqDt(paraMap);
        Result result = Result.successInstance();

        return result;
    }

    /*주문장 정보*/
    @PostMapping(value ="orderBookList")
    public Result getOrderBookList(@RequestBody Map<String, Object> paraMap, HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ordService.getOrderBookList(paraMap));

        return result;
    }

}
