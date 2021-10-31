package daedan.mes.imp.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.imp.service.DaedanImpService;
import daedan.mes.imp.service.StndImpService;
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
import java.util.Map;
@RestController
@RequestMapping("/api/daedan/mes/imp/daedan/")
public class DaedanImpController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private DaedanImpService impService;

    @Autowired
    private Environment env;

    @PostMapping(value="/makeStkByExcel")
    public Result makeStkByExcel(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session)throws Exception {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);

        result.setData(impService.makeStkByExcel(paraMap));
        return result;
    }

    @PostMapping(value="/makeMatrStkByExcel")
    public Result makeMatrStkByExcel(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session)throws Exception {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("session", session);
        result.setData(impService.makeMatrStkByExcel(paraMap));
        return result;
    }
}
