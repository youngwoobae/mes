package daedan.mes.imp.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.imp.service.MhImpService;
import daedan.mes.imp.service.StndImpService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/imp/mh/")
public class MhImpController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private MhImpService impService;

    @PostMapping(value="/ordInfoByExcel")
    public Result ordInfoByExcel(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session)throws Exception {
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("session", session);
        impService.ordInfoByExcel(paraMap);
        return result;
    }
}
