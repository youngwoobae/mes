package daedan.mes.dept.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dept.service.DeptService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
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

    @Autowired
    private UserService userService;

    @PostMapping(value = "/comboDeptList")
    public Result comboDeptList(@RequestBody Map<String, Object> paraMap  , HttpSession session) {
        String tag = "DeptController.deptList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(deptService.getComboDeptList(paraMap));

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

    @PostMapping(value = "/deptList")
    public Result deptList(@RequestBody Map<String, Object> paraMap  , HttpSession session) {
        String tag = "DeptController.deptList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(deptService.getDeptList(paraMap));

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

    @PostMapping(value="/deptTree")
    public Result sysMenuTree(@RequestBody HashMap<String, Object> paraMap  , HttpSession session){
        String tag = "DeptController/deptTree=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(deptService.deptTree(paraMap));

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
