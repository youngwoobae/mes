package daedan.mes.prod.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.prod.service.ProdService;
import daedan.mes.prod.service.sf.SfProdService;
import daedan.mes.stock.service.StockService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/prod")

public class ProdController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProdService prodService;

    @Autowired
    private SfProdService sfProdService;

    @Autowired
    private UserService userService;

    @PostMapping(value="/conditions121")
    public Result WhContitions(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object>  rmap = new HashMap<String,Object>();
        paraMap.put("selectStr","보관온도");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd")));
        rmap.put("comboSaveTmpr",codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","판매유형");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.ord_tp")));
        rmap.put("comboProdTp",codeService.getComboCodeList(paraMap));

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

    @PostMapping(value="/conditionEmbProdInfo")
    public Result conditionEmbProdInfo(@RequestBody Map<String, Object> paraMap,HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        paraMap.put("selectStr","제품분류선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.proc_brnch")));
        rmap.put("comboBrnchNo",prodService.getComboProcBrnch(paraMap));


        paraMap.put("selectStr","판매단위선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.saleunit")));
        rmap.put("comboSaleUnit", codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","보관온도선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd")));
        rmap.put("comboSaveTmpr",codeService.getComboCodeList(paraMap));


        paraMap.put("selectStr","제품형태선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.prod_shape")));
        rmap.put("comboProdShape",codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","CCP유형선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.ccp_tp")));
        rmap.put("comboCcpTp",codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","관리단위선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.mngrbase")));
        rmap.put("comboMngrUnit",codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","살균유형선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.ccp_heat")));
        rmap.put("comboHeatTp",codeService.getComboCodeList(paraMap));

        /*KMJ 21.11.06 : 불필요할 것으로 보임 ,
           1. code.base.used_tp(2900) 이 파렛트유형과 혼용되고 있음. :
           2. 상품정보에서도 동일한 코드가 존재함.
        paraMap.put("selectStr","제품구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.used_tp"))); //원료구분
        rmap.put("comboUsedTp",codeService.getComboCodeList(paraMap));
        */
        paraMap.put("selectStr","속성구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.prod_attr_tp"))); //원료구분
        rmap.put("comboAttrTp",codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","연근선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.how_old_tp"))); //원료구분
        rmap.put("comboHowOld",codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","판매구분선택"); //AddOn By KMJ At 21.10.09 OEM,ODM --주문구분과 같은코드 사용중
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.ord_tp")));
        rmap.put("comboProdTp",codeService.getComboCodeList(paraMap));

        rmap.put("saveWhNos",codeService.getComboProdWhList(paraMap));

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
    @PostMapping(value="/comboEmbProdStk")
    public Result comboEmbProdStk(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base_lose_cd")));
        paraMap.put("selectStr","재고변경사유선택");
        rmap.put("comboChngResn",codeService.getComboCodeList(paraMap));

        paraMap.put("whTp",Long.parseLong(env.getProperty("code.whgb.prod")));
        rmap.put("comboWh", stockService.getComboWh(paraMap));
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

    @PostMapping(value="/prodInfo")
    public Result prodInfo(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.getProdInfo(paraMap));

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
    /*상품목록*/
    @PostMapping(value="/prodList")
    public Result prodList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        String tag = "ProdController.prodList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Result result = Result.successInstance();
        result.setData(prodService.getProdList(paraMap));
        result.setTotalCount(prodService.getProdListCount(paraMap));

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

    @PostMapping(value="/sendInfoToElecticTagUrlList")
    public Result sendInfoToElecticTagUrlList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(prodService.getSendInfoToElecticTagUrlList(paraMap));
        result.setTotalCount(prodService.getSendInfoToElecticTagUrlListCount(paraMap));

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

    /*상품목록 - 엑셀출력용*/
    @PostMapping(value="/prodExcelList")
    public Result prodExcelList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Result result = Result.successInstance();
        result.setData(prodService.getProdExcelList(paraMap));

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

    /*BOM목록 - 엑셀출력용*/
    @PostMapping(value="/bomExcelList")
    public Result bomExcelList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        List<Map<String, Object>> list = prodService.getBomExcelList(paraMap);
        result.setData(list);

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

    /*상품주문이력*/
    @PostMapping(value="/prodOrdList")
    public Result prodOrdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(prodService.getProdOrdList(paraMap));
        result.setTotalCount(prodService.getProdOrdListCount(paraMap));

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
    @PostMapping(value="/prodBomList")
    public Result prodBomList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Result result = Result.successInstance();
        if (uvo.getCustInfo().getCustNo() == 3){
            result.setData(prodService.getHdfdProdBomList(paraMap));
            result.setTotalCount(prodService.getHdfdProdBomListCount(paraMap));
        }else{
            result.setData(prodService.getProdBomList(paraMap));
            result.setTotalCount(prodService.getProdBomListCount(paraMap));
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

    @PostMapping(value="/prodBom")
    public Result prodBom(@RequestBody Map<String, Object> paraMap , HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Result result = Result.successInstance();
        result.setData(prodService.getProdBom(paraMap));
        result.setTotalCount(prodService.getProdBomCount(paraMap));

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


    @PostMapping(value="/makeSeoulFoodMakeIndcByExcel") //서울식품 작업지시
    public Result makeSeoulFoodMakeIndcByExcel(@RequestBody HashMap<String, Object> paraMap,HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));
        paraMap.put("session", session);

        try {
            sfProdService.makeSeoulFoodMakeIndcByExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping(value="/makeSeoulFoodMakeIndcByExcelLoading")
    public Result makeSeoulFoodMakeIndcByExcelLoading(HttpSession session) {
        Result result = Result.successInstance();
        Map<String, Object> paraMap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(session.getAttribute("indcRate"));

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

    @PostMapping(value="/makeSeoulFoodProdBomByExcel") //서울식품 상품 / 원자재 등록
    public Result makeSeoulFoodProdBomByExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        String tag = "ProdController.makeSeoulFoodProdBomByExcel => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            sfProdService.seoulProdBomByExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
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


    @PostMapping(value="/seoulProdBomByExcel") //서울식품 상품 BOM 엑셀 업로드
    public Result seoulProdBomByExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        paraMap.put("session", session);
        try {
            sfProdService.seoulProdBomByExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping(value="/seoulProdBomByExcelLoading")
    public Result makeStkByExcelLoading(HttpSession session) {
        Result result = Result.successInstance();
        Map<String,Object> paraMap = new HashMap<String,Object>();
        result.setData(session.getAttribute("rate"));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

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

    @PostMapping(value="/ProdIndcByExcel") //하담푸드 작업지시 생성
    public Result ProdIndcByExcel(@RequestBody HashMap<String, Object> paraMap,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        try {
            prodService.ProdIndcByExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
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
    @PostMapping(value="/daedongIndcByExcel") //대동고려삼 작업지시 생성
    public Result daedongIndcByExcel(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            prodService.daedongIndcByExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
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




    @PostMapping(value="/ableOnProdBomList")
    public Result ableOnProdBomList(@RequestBody Map<String, Object> paraMap,HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(prodService.getAbleOnProdBomList(paraMap));
        result.setTotalCount(prodService.getAbleOnProdBomListCount(paraMap));
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

    /*상품거래처목록*/
    @PostMapping(value="/prodCmpyList") //원,부자재 구매처리스트
    public Result prodCmpyList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(prodService.getProdCmpyList(paraMap));
        result.setTotalCount(prodService.getProdCmpyListCount(paraMap));
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

    /*거래처별 원간주문량*/
    @PostMapping(value="/monthlyOrderQty")
    public Result monthlyOrderQty(@RequestBody Map<String, Object> paraMap,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.getMonthlyOrderQty(paraMap));

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
    @PostMapping(value="/apndProdBom")
    public Result apndProdBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        prodService.apndProdBom(paraMap);
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

    @PostMapping(value="/copyProdBom")
    public Result copyProdBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        prodService.copyProdBom(paraMap);

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

    /*엑셀업로드로 처리하는 경우로 보임 : 혼돈을 피하기 위하여 아래 saveProdBomByInput api 추가함.(By KMJ 2021.04.20)*/
    @PostMapping(value="/saveProdBom")
    public Result saveProdBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        prodService.saveProdBom(paraMap);

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

    @PostMapping(value="/saveProdBomByInput")
    public Result saveProdBomByInput(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        prodService.saveProdBomByInput(paraMap);

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
    @PostMapping(value="/dropProdBom")
    public Result dropProdBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        prodService.dropProdBom(paraMap);

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

    @PostMapping(value="/deleteProdBom")
    public Result deleteProdBom(@RequestBody Map<String, Object> paraMap , HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String, Object> rmap = new HashMap<String,Object>();
        Result result = Result.successInstance();
        prodService.dropProd(paraMap);

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


    @PostMapping(value="/saveProd")
    public Result saveProd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));

        result.setData(prodService.saveProd(paraMap));

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

    @PostMapping(value="/dropProd")
    public Result droProd(@RequestBody Map<String, Object> paraMap,HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr",NetworkUtil.getClientIp(request));
        paraMap.put("usedYn","N");

        prodService.dropProdInfo(paraMap);

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
    @PostMapping(value="/prodBomInfo")
    public Result prodBomInfo(@RequestBody Map<String, Object> paraMap,HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.getProdBomInfo(paraMap));

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




    @PostMapping(value="/makeProdByExcel") //부자재정보저장
    public Result makeProdByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        prodService.makeProdByExcel(paraMap);

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

    @PostMapping(value="/prodStkCond")
    public Result prodStkCond(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd")));
        rmap.put("comboSaveTmpr",codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("base_matr_cd")));
        rmap.put("comboSaveMatrTp",codeService.getComboCodeList(paraMap));
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
    /*상품재고*/
    @PostMapping(value="/prodStkList")
    public Result prodStkList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(prodService.getProdStkList(paraMap));
        result.setTotalCount(prodService.getProdStkListCount(paraMap));

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

    @PostMapping(value="/prodStatCond")
    public Result prodStatCond(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.save_tmpr_cd")));
        rmap.put("comboSaveTmpr",codeService.getComboCodeList(paraMap));
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
    /*제품현황(창고별로 분기하지 않고 제품별로 합계처리됨.*/
    @PostMapping(value="/prodStatList")
    public Result prodStatList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(prodService.getProdStatList(paraMap));
        result.setTotalCount(prodService.getProdStatListCount(paraMap));

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

    @PostMapping(value="/getAnaprodList")
    public Result getAnaprodList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(prodService.getAnaprodList(paraMap));

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

    //prod_info에 brnch_no를 넣기위한 임시
    @PostMapping(value = "/matchBrnch")
    public Result matchBrnch(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        String tag = "vsvc.ProdController.matchBrnch => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));

        prodService.matchBrnch(paraMap);

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

    /*제품 입고 처리 (Excel) */
    @PostMapping(value="/stsProdExcelIwh")
    public Result stsProdExcelIwh(@RequestBody HashMap<String, Object> paraMap , HttpSession session)throws Exception {
        String tag = "ProdController.stsprodExcelIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.stsProdExcelIwh(paraMap));

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

    /*제품 출고 처리 (Excel)*/
    @PostMapping(value="/prodExcelOwh")
    public Result prodExcelOwh(@RequestBody HashMap<String, Object> paraMap  , HttpSession session)throws Exception {
        String tag = "ProdController.prodExcelOwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("fileRoot",uvo.getCustInfo().getCustNo());
        result.setData(prodService.prodExcelOwh(paraMap));

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

    /*자재 입고 처리 (Excel) */
    @PostMapping(value="/stsMatrExcelIwh")
    public Result stsMatrExcelIwh(@RequestBody HashMap<String, Object> paraMap  , HttpSession session)throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.stsMatrExcelIwh(paraMap));

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

    /*자재 출고 처리 (Excel) */
    @PostMapping(value="/stsMatrExcelOwh")
    public Result stsMatrExcelOwh(@RequestBody HashMap<String, Object> paraMap  , HttpSession session )throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.stsMatrExcelOwh(paraMap));

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

    @PostMapping(value="/prodStkInfo")
    public Result prodStkInfo(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.getProdStkInfo(paraMap));

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

    @PostMapping(value="/getProdInspUser")
    public Result getProdInspUser(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(prodService.getProdInspUser(paraMap));

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
}
