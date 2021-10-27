package daedan.mes.qc.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.qc.service.QcService;
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
@RequestMapping("/api/daedan/mes/qc")
public class QcController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private QcService qcService;
    @PostMapping(value="/conditionsEmbMatrIwhChk")
    public Result conditionsEmbMatrIwhChk(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Map<String,Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.matr_insp")));
        rmap.put("chkTps", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.insp_meth")));
        rmap.put("chkMths", codeService.getComboCodeList(paraMap));

        Result result = Result.successInstance();
        return result.setData(rmap);
    }
    @PostMapping(value="/matrIwhChkList")
    public Result matrIwhChkList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("pageSz", 1000);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(qcService.getMatrIwhChkList(paraMap));
        result.setTotalCount(qcService.getMatrIwhChkListCount(paraMap));
        return result;
    }
    @PostMapping(value="/matrIwhChkDocList")
    public Result matrIwhChkDocList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(qcService.getMatrIwhChkDocList(paraMap));
        return result;
    }
    @PostMapping(value="/matrIwhChkInfo")
    public Result matrIwhChkInfo(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(qcService.matrIwhChkInfo(paraMap));
        return result;
    }
    @PostMapping(value="/saveMatrIwhChk")
    public Result saveIwhChk(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        qcService.saveMatrIwhChk(paraMap);
        return result;
    }
    @PostMapping(value="/dropMatrIwhChk")
    public Result dropIwhChk(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        qcService.dropMatrIwhChk(paraMap);
        return result;
    }
    /**
     * 상품출고검수화면 콤보구성
     *
     * @param paraMap
     * @param session
     * @return void
     */

    @PostMapping(value="/conditionsEmbProdOwhChk")
    public Result conditionsEmbProdOwhChk(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Map<String,Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.prod_insp")));
        rmap.put("chkTps", codeService.getComboCodeList(paraMap));

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.insp_meth")));
        rmap.put("chkMths", codeService.getComboCodeList(paraMap));

        Result result = Result.successInstance();
        return result.setData(rmap);
    }
    /**
     * 상품출고검수정보
     *
     * @param paraMap
     * @param session
     * @return void
     */
    @PostMapping(value="/prodOwhChkInfo")
    public Result prodOwhChkInfo(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(qcService.prodOwhChkInfo(paraMap));
        return result;
    }
    /**
     * 상품출고검수항목 목록 추출
     *
     * @param paraMap
     * @param session
     * @return void
     */
    @PostMapping(value="/prodOwhChkList")
    public Result prodOwhChkList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(qcService.getProdOwhChkList(paraMap));
        result.setTotalCount(qcService.getProdOwhChkListCount(paraMap));
        return result;
    }
    /**
     * 상품출고검수자료 저장
     *
     * @param paraMap
     * @param request
     * @param session
     * @return void
     */
    @PostMapping(value="/saveProdOwhChk")
    public Result saveProdOwhChk(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        qcService.saveProdOwhChk(paraMap);
        return result;
    }
    /**
     * 상품출고검수자료 삭제
     *
     * @param paraMap
     * @param request
     * @param session
     * @return void
     */
    @PostMapping(value="/dropProdOwhChk")
    public Result dropProdOwhChk(@RequestBody Map<String, Object> paraMap , HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        qcService.dropProdOwhChk(paraMap);
        return result;
    }

}
