package daedan.mes.product.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;

import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.product.service.ProductService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/product")
public class ProductController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;


    @Autowired
    private CodeService codeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;
    /**
     * 생산계획수립
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/saveProductPlan")
    public Result saveProductPlan(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        productService.saveProductPlan(paraMap);
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
    @PostMapping(value = "/saveProductIndc")
    public Result saveProductIndc(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        productService.saveProductIndc(paraMap);
        return result;
    }
    /**
     * 신규생산계획 컨트롤 설정
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditionEmbIndcPlan")
    public Result conditionEmbIndcPlan(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base_ord.recv")));
        paraMap.put("selectStr", "처리상태선택");
        rmap.put("comboStat", codeService.getComboCodeList(paraMap));

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


    /**
     * 간략생산계획 월간 캘린러 추출
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getProductPlanList")
    public Result getProductPlanList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(productService.getProductPlanList(paraMap));
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
     * 간략생산계획 정보 추출
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getProductPlanInfo")
    public Result getProductPlanInfo(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();

        result.setData(productService.getProductPlanInfo(paraMap));

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
     * 간략생산지시 월간 캘린러 추출
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getProductIndcList")
    public Result getProductIndcList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(productService.getProductIndcList(paraMap));
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
     * 하담푸드 전용 주문정보 없이 생산지시 생성
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/expectMakeIndc")
    public Result expectMakeIndc(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        String tag = "makeController.expectMakeIndc => ";
        Result result = Result.successInstance();;
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr" , NetworkUtil.getClientIp(request));
        paraMap.put("userId",paraMap.get("userId"));
        paraMap.put("custInfo", uvo.getCustInfo());
        log.info(tag + "procMap = " + paraMap.toString());
        result.setData(productService.expectMakeIndc(paraMap));

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
