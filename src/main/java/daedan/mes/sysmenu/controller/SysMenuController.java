package daedan.mes.sysmenu.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.sysmenu.service.SysMenuService;
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
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/sysmenu")
public class SysMenuController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private SysMenuService sysMenuService;


    @PostMapping(value="/condition9040Auth")
    public Result condition9040Auth(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.auth")));
        paraMap.put("selectStr","접근권한선택");
        result.setData(codeService.getComboCodeList(paraMap));
        return result;
    }

    @PostMapping(value="/topMenuList")
    public Result topMenuList(@RequestBody HashMap<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
//        paraMap.put("level",2);
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("authYn",(String) env.getProperty("auth_yn")); //권한연동
        List<Map<String,Object>> list = sysMenuService.getSysMenuList(paraMap);
        result.setData(list);
        return result;
    }
    @PostMapping(value="/subMenuList")
    public Result subMenuList(@RequestBody HashMap<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("authYn",(String) env.getProperty("auth_yn")); //권한연동
        result.setData(sysMenuService.getSysMenuList(paraMap));
        result.setTotalCount(sysMenuService.getSysMenuListCount(paraMap));

        return result;
    }

    @PostMapping(value="/sysMenuTree")
    public Result sysMenuTree(@RequestBody HashMap<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(sysMenuService.getSysMenuTree(paraMap));
        return result;
    }

    @PostMapping(value="/circleMenuList")
    public Result circleMenuList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(sysMenuService.getCircleMenuList(paraMap));
        result.setTotalCount(sysMenuService.getCircleMenuListCount(paraMap));
        return result;
    }

    @PostMapping(value="/initAuthMenu")
    public Result initAuthMenu(@RequestBody Map<String,Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        sysMenuService.initAuthMenu(paraMap);
        return result;
    }

    @PostMapping(value="/renewalAuthMenu")
    public Result renewalAuthMenu(@RequestBody Map<String,Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        sysMenuService.renewalAuthMenu(paraMap);
        return result;
    }

    @PostMapping(value="/saveHotMenu")
    public Result saveHotMenu(@RequestBody Map<String,Object> paraMap, HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(sysMenuService.saveHotMenu(paraMap));
        return result;
    }

    @PostMapping(value="/saveSysMenuInfo")
    public Result saveSysMenuInfo(@RequestBody Map<String,Object> passMap, HttpServletRequest request , HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        passMap.put("custNo", uvo.getCustInfo().getCustNo());
        passMap.put("pageNo", StringUtil.convertPageNo(passMap));
        passMap.put("ipaddr", NetworkUtil.getClientIp(request));

        Result result = Result.successInstance();
        sysMenuService.saveSysMenuInfo(passMap);

        return result;
    }
    @PostMapping(value="/menuList")
    public Result menuList(@RequestBody HashMap<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setTotalCount(sysMenuService.getMenuListCount(paraMap));
        result.setData(sysMenuService.getMenuList(paraMap));
        return result;
    }
    @PostMapping(value="/getParSysMenus")
    public Result getParSysMenus(@RequestBody HashMap<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(sysMenuService.getParSysMenus(paraMap));
        return result;
    }

    //사용자에 따른 권한메뉴 리스트
    @PostMapping(value = "/authMenuList")
    public Result getAuthMenuList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.auth")));

        result.setData(sysMenuService.getAuthMenuList(paraMap));
        return result;
    }

    //메뉴 위치 보기
    @PostMapping(value = "/menuPosList")
    public Result getMenuPosList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysMenuService.getMenuPosList(paraMap));
        return result;
    }

    @PostMapping(value="/routerList")
    public Result routerList(@RequestBody HashMap<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        List<Map<String,Object>> list = sysMenuService.routerList(paraMap);
        result.setData(list);
        return result;
    }

    @PostMapping(value="/makeAccHstr")
    public Result makeAccHstr(@RequestBody Map<String,Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        sysMenuService.makeAccHstr(paraMap);
        return result;
    }
}
