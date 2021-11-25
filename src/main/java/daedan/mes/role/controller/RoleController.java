package daedan.mes.role.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.role.service.RoleService;
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
@RequestMapping("/api/daedan/mes/role")
public class RoleController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PostMapping(value="/getRoleMenuList")
    public Result roleMenuList(@RequestBody HashMap<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        Long roleCdAdmin = Long.valueOf(env.getProperty("roleCdAdmin"));
        paraMap.put("roleCdAdmin", roleCdAdmin);
        Long roleCdSale = Long.valueOf(env.getProperty("roleCdSale"));
        paraMap.put("roleCdSale", roleCdSale);
        Long roleCdMake = Long.valueOf(env.getProperty("roleCdMake"));
        paraMap.put("roleCdMake", roleCdMake);
        Long roleCdSupt = Long.valueOf(env.getProperty("roleCdSupt"));
        paraMap.put("roleCdSupt", roleCdSupt);


        result.setTotalCount(roleService.getRoleMenuListCount(paraMap));
        result.setData(roleService.getRoleMenuList(paraMap));

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

    @PostMapping(value="/getRoleMenuInfo")
    public Result roleMenuInfo(@RequestBody Map<String, Object> paraMap,HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        try {
            paraMap.put("sysMenuNo",Long.parseLong(paraMap.get("sys_menu_no").toString()));
        } catch ( NullPointerException ne) {
//            paraMap.put("sysMenuNo", 0L);
        }
        Long sysMenuNo = 0L;
        try {
            sysMenuNo = Long.parseLong(paraMap.get("sysMenuNo").toString());
        }
        catch (NullPointerException ne) {
            sysMenuNo = 1L;
        }
        result.setData(roleService.getRoleMenuInfo(sysMenuNo));

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

    @PostMapping(value="/saveRoleInfo")
    public Result saveRoleInfo(@RequestBody Map<String,Object> paraMap, HttpServletRequest request,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        roleService.saveRoleInfo(paraMap);

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

