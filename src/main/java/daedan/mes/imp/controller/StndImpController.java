package daedan.mes.imp.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.imp.service.stnd.StndImpService;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/imp/stnd/")
public class StndImpController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private StndImpService impService;

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @PostMapping(value="/procRate")
    public Result getProcRate(@RequestBody HashMap<String, Object> paraMap , HttpSession session ) {
        String tag = "ImpController.procRate => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);
        result.setData(impService.getProcRate(paraMap));

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

    @PostMapping(value="/makeCodeInfoByExcel")
    public Result makeCodeInfoByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeCodeInfoByExcel(paraMap);

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
    /*0: 거래처 생성
        {"fileNm":"cmpy.xlsx"}
    */
    @PostMapping(value="/makeCmpyByExcel")
    public Result makempyByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);

        impService.makeCmpyByExcel(paraMap);

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

    /*1: 판매거래처 생성
        {"fileNm":"purs_cmpy.xlsx","cmpyTp":21}
    */
    @PostMapping(value="/makeSaleCmpyByExcel")
    public Result makeSaleCmpyByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("cmpyTp",Long.valueOf(env.getProperty("code.cmpytp.cmpy"))); //법인
        paraMap.put("mngrGbnCd", env.getProperty("code.mngrgbn.sale")); //매출
        paraMap.put("session", session);


        impService.makeCmpyByExcel(paraMap);

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
    /*2: 구매거래처 생성
        {"fileNm":"purs_cmpy.xlsx","cmpyTp":20}
    */
    @PostMapping(value="/makePursCmpyByExcel")
    public Result makePursCmpyByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("mngrGbnCd",env.getProperty("code.mngrgbn.purs")); //매입
        paraMap.put("cmpyTp", env.getProperty("code.cmpytp.cmpy")); //법인
        paraMap.put("session", session);
        impService.makeCmpyByExcel(paraMap);

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
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        if (szPath.equals("brief")) {
            impService.makeMatrByBriefExcel(paraMap);
        }
        else if (szPath.equals("normal")) {
            impService.makeMatrByNormalExcel(paraMap);
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

    /*4. 원자재구매정보생성
        {"fileNm":"purs_cmpy.xlsx"}
    */
    @PostMapping(value="makePursByExcel")
    public Result makePursByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makePursByExcel(paraMap);

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

    /*5. 상품정보생성
        {"fileNm":"sale_cmpy.xlsx"}
    */
    /**
     * 엑셀로 제품정보 업로드 처리
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value="/makeProdByExcel")
    public Result makeProdByExcel(@RequestBody Map<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeProdByExcel(paraMap);

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

    @PostMapping(value="/makeStkByExcelLoading")
    public Result makeStkByExcelLoading(HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        result.setData(session.getAttribute("rate"));

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


    @PostMapping(value="/prodIndcExcel")
    public Result prodIndcExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            impService.prodIndcExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");

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

    @PostMapping(value="/prodInfoExcel")
    public Result prodInfoExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        String tag = "ProdController.prodInfoExcel => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        try {
            impService.prodInfoExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");

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

    @PostMapping(value="/prodBomExcel")
    public Result prodBomExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            impService.prodBomExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");

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

    @PostMapping(value="/prodCodeBomExcel")
    public Result prodCodeBomExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            impService.prodCodeBomExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");

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



    /*6. 상품BOM생성
        {"fileNm":"prodbom.xlsx"}
    */
    @PostMapping(value="/makeProdBomByExcel")
    public Result makeProdBomByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeProdBomByExcel(paraMap);

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


    /*7. 주문정보생성
         {"fileNm":"sale_cmpy.xlsx"}
    */
    @PostMapping(value="/makeOrdByExcel")
    public Result makeOrdByExcel(@RequestBody HashMap<String, Object> paraMap , HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeOrdByExcel(paraMap);

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


    /*0: 사용자생성
        {"fileNm":"user.xlsx"}
    */
    @PostMapping(value="/makeUserByExcel")
    public Result makUserByExcel(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) throws IOException {
        String tag = "EsrImpController.makUserByExcel => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeUserByExcel(paraMap);

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


    @PostMapping(value="/makeDefaultMatrCmpy")
    public Result makeDefaultMatrCmpy(@RequestBody HashMap<String, Object> paraMap ,  HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);
        impService.makeDefaultMatrCmpy(paraMap);

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
    // 일일생산일보
    @PostMapping(value="/indcExcelList") //원자재 - 엑셀출력용
    public Result indcExcelList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(impService.indcExcelList(paraMap));

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
    // 일일생산실적
    @PostMapping(value="/makeIndcExcelList") //원자재 - 엑셀출력용
    public Result makeIndcExcelList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(impService.makeIndcExcelList(paraMap));

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
