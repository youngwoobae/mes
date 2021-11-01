package daedan.mes.imp.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.imp.service.sf.SfImpService;
import daedan.mes.user.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
@RestController
@RequestMapping("/api/daedan/mes/imp/sf/")
public class SfImpController {

    @Autowired
    private SfImpService impService;

    @Autowired
    private Environment env;

    @PostMapping(value="/chkProdExist")
    public Result chkProdExist(@RequestBody HashMap<String, Object> paraMap , HttpServletRequest request, HttpSession session)  throws Exception {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("session", session);

        result.setData(impService.chkProdExist(paraMap));
        return result;
    }
}
