package daedan.mes.dept.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dept.service.DeptService;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/dept")
public class DeptController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private DeptService deptService;

    @PostMapping(value = "/comboDeptList")
    public Result comboDeptList(@RequestBody Map<String, Object> paraMap  , HttpSession session) {
        String tag = "DeptController.deptList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(deptService.getComboDeptList(paraMap));
        return result;
    }

    @PostMapping(value = "/deptList")
    public Result deptList(@RequestBody Map<String, Object> paraMap  , HttpSession session) {
        String tag = "DeptController.deptList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(deptService.getDeptList(paraMap));
        return result;
    }

    @PostMapping(value="/deptTree")
    public Result sysMenuTree(@RequestBody HashMap<String, Object> paraMap  , HttpSession session){
        String tag = "DeptController/deptTree=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(deptService.deptTree(paraMap));
        return result;
    }

}
