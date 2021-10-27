package daedan.mes.user.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.jwt.JwtService;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.sys.service.SysService;
import daedan.mes.user.domain.IndsType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/user")
public class UserController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private SysService sysService;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private UserService userService;
    @Autowired
    CustInfoRepository custInfoRepo;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value="/entryin")
    public Result entryin() {
        Result result = Result.successInstance();
        result.setData(userService.getEntryInfo());

        return result;
    }

    @PostMapping(value="/custin")
    public Result custin(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(Integer.parseInt(env.getProperty("cust_no")));
        return result;
    }
    @PostMapping(value="/autoSignin")
    public Result autoSignin(HttpServletResponse response  , HttpSession session) {
        String token = "";
        Result result = Result.successInstance();
        Map<String,Object> paraMap = new HashMap<String,Object>();


        paraMap.put("mailAddr",env.getProperty("automail"));
        paraMap.put("secrtNo",env.getProperty("autoword"));
        //paraMap.put("mailAddr","aa@aa.com");
        //paraMap.put("secrtNo","adm");
        log.info("encpswd=" + BCrypt.hashpw(paraMap.get("secrtNo").toString(), BCrypt.gensalt()));
        UserInfo uservo = userService.signin(paraMap.get("mailAddr").toString(), paraMap.get("secrtNo").toString());
        uservo.setIndsTp(IndsType.valueOf(env.getProperty("industry_type"))); //산업구분(산업구분에따라 표시되는 필드조정됨: 매우중요)
        log.info("userIndsTp = " + uservo.getIndsTp());
        token = jwtService.create("member", uservo, "user");
        log.info("created user token = " + token);

        // 세션 생성
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        if (uvo == null) {
            session.setAttribute("autoUserInfo", uservo); //AddOn By KMJ At 21.10.21
        }


        response.setHeader("authorization", token);
        result.setData(uservo);
        return result;
    }
    @PostMapping(value="/signin")
    public Result signin(@RequestBody HashMap<String, Object> paraMap, HttpServletResponse response ,HttpSession session) {
        String tag = "UserController.signin => ";
        String token = "";
        Result result = Result.successInstance();
        log.info("encpswd=" + BCrypt.hashpw(paraMap.get("secrtNo").toString(), BCrypt.gensalt()));

        UserInfo uservo = userService.signin(paraMap.get("mailAddr").toString(), paraMap.get("secrtNo").toString());
        if (uservo != null) {
            String orgLcnsCd = paraMap.get("lcnsCd").toString();
            log.info("orgLcnsCd=" + orgLcnsCd);
            String encLcnsCd = BCrypt.hashpw(orgLcnsCd, BCrypt.gensalt());
            log.info("encLcnsCd=" + encLcnsCd);
            if (BCrypt.checkpw(orgLcnsCd, encLcnsCd)) {
                String strCustNo = orgLcnsCd.substring( orgLcnsCd.length()-3, orgLcnsCd.length()-1);
                Long custNo = Long.parseLong(strCustNo);
                uservo.setCustInfo(custInfoRepo.findByCustNo(custNo));
                log.info(tag + " uservo = " + StringUtil.voToMap(uservo));
            }
        }
        uservo.setToken(null);

        token = jwtService.create("member", uservo, "user");
        //log.info("created user token = " + token);
        response.setHeader("authorization", token);

        // 세션 생성
        session.setAttribute("userInfo",uservo); //AddOn By KMJ At 21.10.21
        log.info(tag + "userInfo.custInfo = " + StringUtil.voToMap(uservo.getCustInfo()));
        sysService.invokeChatServer();
        result.setData(StringUtil.voToMap(uservo));
        return result;
    }
    @PostMapping(value="/loginByToken")
    public Result loginByToken(@RequestBody HashMap<String, Object> paraMap, HttpServletResponse response, HttpSession session) {
        String token = "";
        Result result = Result.successInstance();
        UserInfo uservo = userService.getUserInfByToken(paraMap);
        paraMap.put("custNo", uservo.getCustInfo().getCustNo());
        if (uservo == null) {

        }
        response.setHeader("authorization", token);

        result.setData(uservo);
        return result;
    }

    @PostMapping(value = "/conditionsUser9020")
    public Result conditionsUser9020(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = new HashMap<String,Object>();

        paraMap.put("selectStr","직종구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("base_ocpn_kind"))); //직종구분(사무직,생산직)
        rmap.put("comboOcpnKind", codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","채용구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("base_empl_kind"))); ///작업자구분(내국인,외국인,용역)
        rmap.put("comboEmplKind", codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","근무상태선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.user_stat")));
        rmap.put("comboUserStat", codeService.getComboCodeList(paraMap));
        result.setData(rmap);

        return result;
    }
    @PostMapping(value = "/conditionsEmpUser")
    public Result conditionsEmpUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = new HashMap<String,Object>();

        paraMap.put("selectStr","직종구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("base_ocpn_kind"))); //직종구분(사무직,생산직)
        rmap.put("comboOcpnKind", codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","채용구분선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("base_empl_kind"))); ///채용구분(내국인,외국인,용역)
        rmap.put("comboEmplKind", codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","직책선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.user_posn")));
        rmap.put("comboUserPosn", codeService.getComboCodeList(paraMap));

        paraMap.put("selectStr","근무상태선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.user_stat")));
        rmap.put("comboUserStat", codeService.getComboCodeList(paraMap));


        rmap.put("comboUserDept", codeService.getComboUserDeptList(paraMap));

        result.setData(rmap);
        result.setTotalCount(userService.getUserListCount(paraMap));
        return result;
    }
    @PostMapping(value = "/userList")
    public Result userList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(userService.getUserList(paraMap));
        result.setTotalCount(userService.getUserListCount(paraMap));
        return result;
    }
    @PostMapping(value = "/authUserList")
    public Result authUserList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(userService.getAuthUserList(paraMap));
        result.setTotalCount(userService.getAuthUserListCount(paraMap));
        return result;
    }
    @PostMapping(value = "/renewalAuthUser")
    public Result renewalAuthUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        userService.renewalAuthUser(paraMap);
        return result;
    }

    @PostMapping(value = "/userInfo")
    public Result userInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(userService.getUserInfo(paraMap));
        return result;
    }
    @PostMapping(value = "/saveUser")
    public Result saveUser(@RequestBody Map<String, Object> paraMap,HttpServletRequest request, HttpSession session) {
        String tag = "UserController.saveUser => ";


        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        userService.saveUser(paraMap);
        return result;
    }
    @PostMapping(value = "/dropUser")
    public Result dropUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        userService.dropUser(paraMap);
        return result;
    }

    @PostMapping(value="/userData")
    public Result userData(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(userService.getUserData(paraMap.get("email").toString(), paraMap.get("password").toString(),paraMap.get("newPassword").toString()));
        return result;
    }

    //사용자 권한변경
    @PostMapping(value="/renewalUserData")
    public Result renewalUserData(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        userService.renewalUserData(paraMap);
        return result;
    }

    @PostMapping(value = "/comboDeptList")
    public Result getComboDeptList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(userService.getComboDeptList(paraMap));
        result.setTotalCount(userService.getComboDeptListCount(paraMap));
        return result;
    }

    @PostMapping(value = "/tabletWorkerList")
    public Result getTabletWorkerList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(userService.getTabletWorkerList(paraMap));
        result.setTotalCount(userService.getTabletWorkerListCount(paraMap));

        return result;
    }
    @PostMapping(value = "/makeAccHstr")
    public Result makeAccHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        userService.makeAccHstr(paraMap);
        return result;
    }
    @PostMapping(value = "/accHstrSummaryList")
    public Result accHstrSummaryList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getHstrSummaryList(paraMap));
        result.setTotalCount(userService.getHstrSummaryListCount(paraMap));

        return result;
    }
    @PostMapping(value = "/accHstrList")
    public Result getHstrList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getHstrList(paraMap));
        result.setTotalCount(userService.getHstrListCount(paraMap));

        return result;
    }
    @PostMapping(value = "/authUserMenuList")
    public Result authUserMenuList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getAuthUserMenuList(paraMap));
        result.setTotalCount(userService.getAuthUserMenuListCount(paraMap));
        return result;
    }

    @PostMapping(value="/authUserMenuInfo") //사용자별 상세메뉴권한
    public Result authUserMenuInfo(@RequestBody HashMap<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String,Object> rmap = new HashMap<String,Object>();
        result.setData(cmmnService.getAuthUserMenuInfo(paraMap)); //메뉴별상세접근권한
        return result;
    }


    @PostMapping(value = "/saveAuthUserMenu") /*사용자별 세부 메뉴 권한 처리*/
    public Result saveAuthUserMenu(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        userService.saveAuthUserMenu(paraMap);
        return result;
    }
    @PostMapping(value = "/saveToken") /*사용자별 세부 메뉴 권한 처리*/
    public Result saveToken(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(userService.saveToken(paraMap));
        return result;
    }


}
