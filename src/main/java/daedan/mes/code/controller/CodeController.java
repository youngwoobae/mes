package daedan.mes.code.controller;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/code")
public class CodeController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;


    @PostMapping(value="/codeList")
    public Result codeList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "CodeController=>";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(codeService.getCodeList(paraMap));
        result.setTotalCount(codeService.getCodeListCount(paraMap));
        return result;
    }

    @PostMapping(value="/comboCodeList")
    public Result comboCodeList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        List<Map<String,Object>> list = codeService.getComboCodeList(paraMap);
        result.setData(list);
        return result;
    }
    @PostMapping(value="/comboWithoutChoiceCodeList")
    public Result getComboWithoutChoiceCodeList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(codeService.getComboWithoutChoiceCodeList(paraMap));
        return result;
    }

    @PostMapping(value="/codeInfo")
    public Result codeInfo(@RequestBody Map<String, Object> paraMap   , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(codeService.getCodeInfo(paraMap));
        return result;
    }

    @PostMapping(value="/saveCodeByName")
    public Result saveCodeByName(@RequestBody Map<String, Object> paraMap , HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(codeService.saveCodeByName(paraMap));
        return result;
    }


    @PostMapping(value="/saveInspCode/{path}")
    public Result saveInspCode(@PathVariable(name = "path") String szPath ,@RequestBody Map<String, Object> paraMap , HttpServletRequest request  , HttpSession session){
        String tag = "codeController.saveInspCode ==> ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> passMap = (Map<String, Object>) paraMap.get("codeInfo");
        if (szPath.equals("matr")) {
            passMap.put("par_code_no", Long.parseLong(env.getProperty("code.base.matr_insp")));
        }
        else if (szPath.equals("prod")) {
            passMap.put("par_code_no", Long.parseLong(env.getProperty("code.base.prod_insp")));
        }
        passMap.put("ipaddr", NetworkUtil.getClientIp(request));
        passMap.put("userId",paraMap.get("userId"));
        log.info(tag + "par_code_no = " + passMap.get("par_code_no"));
        result.setData(codeService.saveInspCode(passMap));
        return result;
    }

    @PostMapping(value="/dropCode")
    public Result dropCode(@RequestBody Map<String, Object> paraMap , HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        codeService.dropCode(paraMap);
        return result;
    }

    @PostMapping(value="/saveCode")
    public Result saveCode(@RequestBody Map<String, Object> paraMap , HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        codeService.saveCode(paraMap);
        return result;
    }

    @PostMapping(value = "/codeTree")
    public Result codeTree(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "vsvc.CodeController.codeTree => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(codeService.getCodeTree(paraMap));

        return result;
    }

    @PostMapping(value="/codeModify")
    public Result codeModify(@RequestBody Map<String, Object> paraMap , HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        codeService.codeModify(paraMap);
        return result;
    }
}
