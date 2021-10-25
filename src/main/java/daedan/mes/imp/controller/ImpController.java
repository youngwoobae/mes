package daedan.mes.imp.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.imp.service.ImpService;
import daedan.mes.imp.service.daedan.DaedanImpService;
import daedan.mes.imp.service.mihong.MhImpService;
import daedan.mes.imp.service.sf.SfImpService;
import daedan.mes.imp.service.yyjg.YyjgImpService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/imp")
public class ImpController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private ImpService impService;

    @Autowired
    private DaedanImpService daedanImpService;

    @Autowired
    private SfImpService sfService;

    @Autowired
    private MhImpService mhService;
    


    @PostMapping(value="/procRate")
    public Result getProcRate(@RequestBody HashMap<String, Object> paraMap , HttpSession session ) {
        String tag = "ImpController.procRate => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);
        result.setData(impService.getProcRate(paraMap));
        return result;
    }

    @PostMapping(value="/makeCodeInfoByExcel")
    public Result makeCodeInfoByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeCodeInfoByExcel(paraMap);
        return result;
    }
    /*0: 거래처 생성
        {"fileNm":"cmpy.xlsx"}
    */
    @PostMapping(value="/makeCmpyByExcel")
    public Result makempyByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);

        impService.makeCmpyByExcel(paraMap);
        return result;
    }

    /*1: 판매거래처 생성
        {"fileNm":"purs_cmpy.xlsx","cmpyTp":21}
    */
    @PostMapping(value="/makeSaleCmpyByExcel")
    public Result makeSaleCmpyByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("cmpyTp",Long.valueOf(env.getProperty("code.cmpytp.sale"))); //매출
        paraMap.put("mngrGbnCd", env.getProperty("code.mngrgb.cmpy")); //법인
        paraMap.put("session", session);


        impService.makeCmpyByExcel(paraMap);
        return result;
    }
    /*2: 구매거래처 생성
        {"fileNm":"purs_cmpy.xlsx","cmpyTp":20}
    */
    @PostMapping(value="/makePursCmpyByExcel")
    public Result makePursCmpyByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("cmpyTp",env.getProperty("code.cmpytp.purs")); //매입
        paraMap.put("mngrGbnCd", env.getProperty("code.mngrgb.cmpy")); //법인
        paraMap.put("session", session);
        impService.makeCmpyByExcel(paraMap);
        return result;
    }
    /*3-1 : 원자재성생 생성
      3-2 : 원자재거래처 생성
        {"fileNm":"purs_cmpy.xlsx"}
    */
    @PostMapping(value="{path}/makeMatrByExcel")
    public Result makeMatrByExcel(
            @PathVariable(name = "path") String szPath
          , @RequestBody HashMap<String, Object> paraMap
          , HttpSession session
          , HttpServletRequest request) throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        if (szPath.equals("brief")) {
            impService.makeMatrByBriefExcel(paraMap);
        }
        else if (szPath.equals("normal")) {
            impService.makeMatrByNormalExcel(paraMap);
        }
        return result;
    }

    /*4. 원자재구매정보생성
        {"fileNm":"purs_cmpy.xlsx"}
    */
    @PostMapping(value="makePursByExcel")
    public Result makePursByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makePursByExcel(paraMap);
        return result;
    }

    /*5. 상품정보생성
        {"fileNm":"sale_cmpy.xlsx"}
    */
    @PostMapping(value="/makeProdByExcel")
    public Result makeProdByExcel(@RequestBody Map<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeProdByExcel(paraMap);
        return result;
    }


    @PostMapping(value="/makeStkByExcel")
    public Result makeStkByExcel(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session)throws Exception {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);

        result.setData(daedanImpService.makeStkByExcel(paraMap));
        return result;
    }

    @PostMapping(value="/makeStkByExcelLoading")
    public Result makeStkByExcelLoading(HttpSession session) {
        Result result = Result.successInstance();
        result.setData(session.getAttribute("rate"));
        return result;
    }


    @PostMapping(value="/prodIndcExcel")
    public Result prodIndcExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            impService.prodIndcExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");
        return result;
    }

    @PostMapping(value="/prodInfoExcel")
    public Result prodInfoExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        String tag = "ProdController.prodInfoExcel => ";
        log.info(tag + "ipaddr  = " +  paraMap.get("ipaddr"));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        try {
            impService.prodInfoExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");
        return result;
    }

    @PostMapping(value="/prodBomExcel")
    public Result prodBomExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            impService.prodBomExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");
        return result;
    }

    @PostMapping(value="/prodCodeBomExcel")
    public Result prodCodeBomExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            impService.prodCodeBomExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");
        return result;
    }



    /*6. 상품BOM생성
        {"fileNm":"prodbom.xlsx"}
    */
    @PostMapping(value="/makeProdBomByExcel")
    public Result makeProdBomByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeProdBomByExcel(paraMap);
        return result;
    }


    /*7. 주문정보생성
         {"fileNm":"sale_cmpy.xlsx"}
    */
    @PostMapping(value="/makeOrdByExcel")
    public Result makeOrdByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeOrdByExcel(paraMap);
        return result;
    }


    @PostMapping(value="/prodExcel")
    public Result prodExcel(@RequestBody HashMap<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        try {
            impService.prodExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");
        return result;
    }

    /*0: 사용자생성
        {"fileNm":"user.xlsx"}
    */
    @PostMapping(value="/makeUserByExcel")
    public Result makUserByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) throws IOException {
        String tag = "EsrImpController.makUserByExcel => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeUserByExcel(paraMap);
        return result;
    }


    @PostMapping(value="/makeDefaultMatrCmpy")
    public Result makeDefaultMatrCmpy(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeDefaultMatrCmpy(paraMap);
        return result;
    }
    // 일일생산일보
    @PostMapping(value="/indcExcelList") //원자재 - 엑셀출력용
    public Result indcExcelList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(impService.indcExcelList(paraMap));
        return result;
    }
    // 일일생산실적
    @PostMapping(value="/makeIndcExcelList") //원자재 - 엑셀출력용
    public Result makeIndcExcelList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(impService.makeIndcExcelList(paraMap));
        return result;
    }

    @PostMapping(value="/chkProdExist")
    public Result chkProdExist(@RequestBody HashMap<String, Object> paraMap , HttpServletRequest request, HttpSession session)  throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);

        result.setData(sfService.chkProdExist(paraMap));
        return result;
    }

    @PostMapping(value="/makeMatrStkByExcel")
    public Result makeMatrStkByExcel(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session)throws Exception {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);
        result.setData(daedanImpService.makeMatrStkByExcel(paraMap));
        return result;
    }

    @PostMapping(value="/ordInfoByExcel")
    public Result ordInfoByExcel(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session)throws Exception {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);
        mhService.ordInfoByExcel(paraMap);
        return result;
    }
}




