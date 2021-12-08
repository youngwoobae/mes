package daedan.mes.user.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.jwt.JwtService;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.dept.service.DeptService;
import daedan.mes.sys.service.SysService;
import daedan.mes.user.domain.*;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.repository.UserRepository;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/user")
public class UserController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;
    @Autowired
    private DeptService deptService;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private UserService userService;
    @Autowired
    CustInfoRepository custInfoRepo;
    @Autowired
    CodeRepository codeRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value="/entryin")
    public Result entryin(HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = null;
        try {
            uvo = (UserInfo) session.getAttribute("userInfo");
        }
        catch (NullPointerException ne) {

        }

        Long custNo = uvo.getCustInfo().getCustNo();
        result.setData(userService.getEntryInfo());

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

    @PostMapping(value="/custin")
    public Result custin(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(Integer.parseInt(env.getProperty("cust_no")));

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
    @PostMapping(value="/chkEncStr")
    public Result chkEncStr(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        String tag = "userController.chkEncStr =>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");

        String accStr = paraMap.get("accStr").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        log.info("인크립트 스트링 = " + cmmnService.encryptStr(custNo,accStr));

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

    @PostMapping(value="/autoSignin")
    public Result autoSignin(HttpServletRequest request, HttpServletResponse response  , HttpSession session) {
        String tag = "userController.autoSignIn =>";
        String token = "";
        Result result = Result.successInstance();

        String szUserId = request.getParameter("userId");
        Long userId = Long.parseLong(szUserId);
        UserInfo uservo = userService.getUserInfoById(userId);
        Long custNo = uservo.getCustInfo().getCustNo();
        uservo.setCustInfo(custInfoRepo.findByCustNo(custNo));

        token = jwtService.create("member", uservo, "user");
        log.info(tag + "created user token = " + token);

        // 세션 생성 : AddOn By KMJ At 21.10.21
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        if (uvo == null) {
            session.setAttribute("autoUserInfo", uservo); //사용자 및 고객사 특성정보
        }
        response.setHeader("authorization", token);
        result.setData(uservo);

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
    @PostMapping(value="/signin")
    public Result signin(@RequestBody HashMap<String, Object> paraMap, HttpServletResponse response ,HttpSession session) {
        String tag = "UserController.signin => ";
        String token = "";
        Result result = Result.successInstance();

        log.info("encpswd=" + BCrypt.hashpw(paraMap.get("secrtNo").toString(), BCrypt.gensalt()));
        String orgLcnsCd = paraMap.get("lcnsCd").toString();
        log.info("orgLcnsCd=" + orgLcnsCd);
        String encLcnsCd = BCrypt.hashpw(orgLcnsCd, BCrypt.gensalt());
        log.info("encLcnsCd=" + encLcnsCd);
        UserInfo uservo = userService.signin(paraMap.get("mailAddr").toString(), paraMap.get("secrtNo").toString());
        if (uservo != null) {
            if (BCrypt.checkpw(orgLcnsCd, encLcnsCd)) {
                String strCustNo = orgLcnsCd.replaceAll("[^0-9]", "");
                Long custNo = Long.parseLong(strCustNo);

                uservo.setCustInfo(custInfoRepo.findByCustNo(custNo));
                log.info(tag + " uservo = " + StringUtil.voToMap(uservo));
                if (uservo.getCustInfo().getKpiYn().equals("Y")) { /* KPI API 세션 생성 */
                    Map<String, Object> amap = new HashMap<String, Object>();
                    JSONObject jsonData = new JSONObject();
                    try {
                        jsonData.put("companyCode", uservo.getCustInfo().getSaupNo());
                        amap.put("apiURL", env.getProperty("kpi.sessionURL"));
                        amap.put("jsonStr", jsonData);
                        jsonData = cmmnService.getRestApiData(amap);
                        //if (jsonData.get("error") == null) {
                        session.setAttribute("kpiSessId", jsonData.get("sessionId"));
                        //}
                    } catch (NullPointerException ne) {

                    }
                }
                // MES 세션 생성
                session.setAttribute("userInfo",uservo);//사용자 및 고객사 특성정보
                //log.info(tag + "userInfo.custInfo = " + StringUtil.voToMap(uservo.getCustInfo()));
            }
        }
        uservo.setToken(null);
        token = jwtService.create("member", uservo, "user");
        //log.info("created user token = " + token);
        response.setHeader("authorization", token);

        //sysService.invokeChatServer();
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("userInfo",StringUtil.voToMap(uservo));
        rmap.put("prodUnitWgtNm",(String) session.getAttribute("prodUnitWgtNm"));
        result.setData(rmap);


        return result;
    }
    @PostMapping(value="/tabletSignIn")
    public Result tabletSignin(@RequestBody HashMap<String, Object> paraMap, HttpServletResponse response ,HttpSession session) {
        String tag = "UserController.tabletSignIn => ";
        String token = "";
        Result result = Result.successInstance();
        UserInfo uvo = null;
        Long custNo = 0L;
        //try {
        //    uvo = (UserInfo) session.getAttribute("userInfo"); //AddOn By KMJ At 21.10.21
        //    custNo = uvo.getCustInfo().getCustNo();
        //}
        //catch (NullPointerException ne) {
            String secrtNo = "adm";
            log.info(tag + "encpswd=" + BCrypt.hashpw(secrtNo, BCrypt.gensalt()));

            String orgLcnsCd = paraMap.get("lcnsCd").toString();
            log.info("orgLcnsCd=" + orgLcnsCd);

            String encLcnsCd = BCrypt.hashpw(orgLcnsCd, BCrypt.gensalt()); //단방향 암호화
            String strCustNo = orgLcnsCd.replaceAll("[^0-9]", "");
            paraMap.put("custNo",Long.parseLong(strCustNo));
            CustInfo custInfo = userService.getCustInfoByCustNo(paraMap);
            custNo = custInfo.getCustNo();
            if (custInfo != null) {
                String mailAddr = custInfo.getAutoSignId();
                String autoPswd = "adm";
                uvo = userService.signin(mailAddr, autoPswd);
                if (uvo != null) {
                    if (BCrypt.checkpw(orgLcnsCd, encLcnsCd)) {
                        uvo.setCustInfo(custInfo);
                        log.info(tag + " 테블릿.AutoSignIn.UserInfo => " + StringUtil.voToMap(uvo));
                    }
                }
            }
            uvo.setToken(null);
            token = jwtService.create("member", uvo, "user");
            //log.info("created user token = " + token);
            response.setHeader("authorization", token);
            // 세션 생성
            session.setAttribute("userInfo", uvo); //AddOn By KMJ At 21.10.21
            log.info(tag + "userInfo.custInfo = " + StringUtil.voToMap(uvo.getCustInfo()));

            //sysService.invokeChatServer();
            result.setData(StringUtil.voToMap(uvo));
        //}

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne1) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/loginByToken")
    public Result loginByToken(@RequestBody HashMap<String, Object> paraMap, HttpServletResponse response, HttpSession session) {
        String token = "";
        Result result = Result.successInstance();
        UserInfo uservo = userService.getUserInfByToken(paraMap);
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", uservo.getCustInfo().getCustNo());
        if (uservo == null) {

        }
        response.setHeader("authorization", token);

        result.setData(uservo);

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

    @PostMapping(value = "/conditionsUser9020")
    public Result conditionsUser9020(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
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

    @PostMapping(value = "/deleteAuthUser")
    public Result deleteAuthUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        /*체크리스트 삭제*/
        String tag = "UserController.deletechkList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        userService.deleteAuthUser(paraMap);
        //        //SOL AddOn By KMJ AT 21.11.16
//        try {
//            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
//            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
//        }
//        catch(NullPointerException ne) {
//        }
        //EOL AddON By KMJ AT 21.11.26
//        result.setData();
        return result;
    }

    @PostMapping(value = "/conditionsEmpUser")
    public Result conditionsEmpUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
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
        rmap.put("comboUserDept", deptService.getComboDeptList(paraMap));

        result.setData(rmap);
        result.setTotalCount(userService.getUserListCount(paraMap));

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
    /**
     * 사용이력관리용 사용자목록
     *
     * @param paraMap
     * @return Result
     */

    @PostMapping(value = "/getUserList")
    public Result getUserList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        String accEvtLogYn = uvo.getCustInfo().getActEvtLogYn();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("accEvtLogYn", accEvtLogYn);

        Result result = Result.successInstance();
        result.setData(userService.getUserList(paraMap));
        result.setTotalCount(userService.getUserListCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (accEvtLogYn.equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value = "/authUserList")
    public Result authUserList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(userService.getAuthUserList(paraMap));
        result.setTotalCount(userService.getAuthUserListCount(paraMap));

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

    @PostMapping(value = "/deletechkList")
    public Result deletechkList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        /*체크리스트 삭제*/
        String tag = "UserController.deletechkList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        System.out.println("%%%%%%%" + paraMap.get("chkList2"));
        List<Map<String, Object>> list = (List<Map<String, Object>>) paraMap.get("chkList2");
//        result.setData(userService.getAuthUserList(paraMap));
//        result.setTotalCount(userService.getAuthUserListCount(paraMap));
//        List<Map<String, Object>> deleteList  =  userService.deleteAuthUser(paraMap);
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


    @PostMapping(value = "/renewalAuthUser")
    public Result renewalAuthUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        userService.renewalAuthUser(paraMap);

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

    @PostMapping(value = "/userInfo")
    public Result userInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(userService.getUserInfo(paraMap));

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
    @PostMapping(value = "/saveUser")
    public Result saveUser(@RequestBody Map<String, Object> paraMap,HttpServletRequest request, HttpSession session) {
        String tag = "UserController.saveUser => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        userService.saveUser(paraMap);

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
    @PostMapping(value = "/dropUser")
    public Result dropUser(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        userService.dropUser(paraMap);

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

    @PostMapping(value="/userData")
    public Result userData(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(userService.getUserData(paraMap.get("email").toString(), paraMap.get("password").toString(),paraMap.get("newPassword").toString()));

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

    //사용자 권한변경
    @PostMapping(value="/renewalUserData")
    public Result renewalUserData(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        userService.renewalUserData(paraMap);

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

    @PostMapping(value = "/comboDeptList")
    public Result getComboDeptList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(userService.getComboDeptList(paraMap));
        result.setTotalCount(userService.getComboDeptListCount(paraMap));

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

    @PostMapping(value = "/tabletWorkerList")
    public Result getTabletWorkerList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(userService.getTabletWorkerList(paraMap));
        result.setTotalCount(userService.getTabletWorkerListCount(paraMap));

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
    @PostMapping(value = "/makeAccHstr")
    public Result makeAccHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        userService.makeAccHstr(paraMap);

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
    @PostMapping(value = "/accHstrSummaryList")
    public Result accHstrSummaryList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getHstrSummaryList(paraMap));
        result.setTotalCount(userService.getHstrSummaryListCount(paraMap));

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
    @PostMapping(value = "/getHstrList")
    public Result getHstrList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        String actEvtLogYn = uvo.getCustInfo().getActEvtLogYn();
        paraMap.put("custNo", custNo);
        paraMap.put("actEvtLogYn", actEvtLogYn);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getHstrList(paraMap));
        result.setTotalCount(userService.getHstrListCount(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (actEvtLogYn.equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    @PostMapping(value = "/authUserMenuList")
    public Result authUserMenuList(@RequestBody Map<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getAuthUserMenuList(paraMap));
        result.setTotalCount(userService.getAuthUserMenuListCount(paraMap));

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

    @PostMapping(value="/authUserMenuInfo") //사용자별 상세메뉴권한
    public Result authUserMenuInfo(@RequestBody HashMap<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String,Object> rmap = new HashMap<String,Object>();
        result.setData(cmmnService.getAuthUserMenuInfo(paraMap)); //메뉴별상세접근권한

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


    @PostMapping(value = "/saveAuthUserMenu") /*사용자별 세부 메뉴 권한 처리*/
    public Result saveAuthUserMenu(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        userService.saveAuthUserMenu(paraMap);

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
    @PostMapping(value = "/saveToken") /*사용자별 세부 메뉴 권한 처리*/
    public Result saveToken(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(userService.saveToken(paraMap));

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

    @PostMapping(value="/getWorkList")
    public Result getWorkList(@RequestBody HashMap<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            paraMap.get("dateFr").toString().substring(0, 10);
        }
        catch (NullPointerException ne) {
            paraMap.put("dateFr", sdf.format(DateUtils.getCurrentDateString().substring(0, 10)));
            paraMap.put("dateTo", sdf.format(DateUtils.getCurrentDateString().substring(0, 10)));
        }
        ArrayList<Map<String, Object>> kpiList = new ArrayList<>();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        paraMap.put("ocpnKind",Long.parseLong(env.getProperty("ocpn_kind_blue")));

        result.setData(userService.getWorkList(paraMap));
        result.setTotalCount(userService.getWorkListCount(paraMap));

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
    /**
     * 생산직 근태목록
     *
     * @param paraMap
     * @return Result
     */
    @PostMapping(value="/getWorkerList")
    public Result getWorkerList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            paraMap.get("dateFr").toString();
        }
        catch (NullPointerException ne) {
            paraMap.put("dateFr",sdf.format(DateUtils.getCurrentDate()));
            paraMap.put("dateTo",sdf.format(DateUtils.getCurrentDate()));
        }
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(userService.getWorkerList(paraMap));
        result.setTotalCount(userService.getWorkerListCount(paraMap));

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

    /**
     * 개별근무정보저장
     *
     * @param paraMap
     * @param request
     * @param session
     * @return void
     */
    @PostMapping(value = "/saveWorkInfo")
    public Result saveWorkInfo(@RequestBody Map<String, Object> paraMap,HttpServletRequest request, HttpSession session) {
        String tag = "UserController.saveWorkInfo => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("sessId", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        userService.saveWorkInfo(paraMap);

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
    /**
     * 일괄근무정보저장
     *
     * @param paraMap
     * @param request
     * @param session
     * @return void
     */
    @PostMapping(value = "/saveWorkList")
    public Result saveWorkList(@RequestBody Map<String, Object> paraMap,HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("sessId", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        userService.saveWorkList(paraMap);

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
    /**
     * 사용자별 접속 상세이력
     *
     * @param paraMap
     * @param session
     * @return void
     */
    @PostMapping(value="/getUserAccLogList")
    public Result getUserAccLogList(@RequestBody HashMap<String, Object> paraMap, HttpSession session ){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(userService.getUserAccLogList(paraMap));
        result.setTotalCount(userService.getUserAccLogListCount(paraMap));

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

    @PostMapping(value = "/dropWorkInfo") /*사용자별 세부 메뉴 권한 처리*/
    public Result dropWorkInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        userService.dropWorkInfo(paraMap);

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

    /**
     * 근태관리 일괄삭제기능
     *
     * @param paraMap
     * @return void
     */
    @PostMapping(value="/dropWorkerList")
    public Result dropWorkerList(@RequestBody Map<String, Object> paraMap,HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", custNo);
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        userService.dropWorkerList(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
            } catch (NullPointerException ne) {
            }
        }


        return result;
    }

    @PostMapping(value="/getUserGroup")
    public Result getUserGroup(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(userService.getUserGroup(paraMap));

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

    @PostMapping(value="/tabletMenu/{path}")
    public Result tableMenu(@PathVariable(name = "path") Integer nPath , HttpServletResponse response , HttpServletRequest request  , HttpSession session) {
        String tag = "TableMenu => ";
        log.info(tag+"!!!!!@@@@@#####");
        Result result = Result.successInstance();
        CustInfo civo = custInfoRepo.findByCustNo(Long.valueOf(nPath));
        if (civo != null) {
            UserInfo uservo = userRepo.findByMailAddrAndUsedYn(civo.getAutoSignId(),"Y");
            Long custNo = uservo.getCustInfo().getCustNo();
            uservo.setCustInfo(custInfoRepo.findByCustNo(custNo));
            String token = jwtService.create("member", uservo, "user");
            log.info(tag + "created user token = " + token);

            // 세션 생성 : AddOn By KMJ At 21.10.21
            UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
            if (uvo == null) {
                uvo = uservo;
                session.setAttribute("autoUserInfo", uservo); //사용자 및 고객사 특성정보
                session.setAttribute("userInfo", uservo); //사용자 및 고객사 특성정보
            }

            response.setHeader("authorization", token);
            result.setData(uservo);

            //SOL AddOn By KMJ AT 21.11.16
            if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
                try {
                    AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                    userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
                } catch (NullPointerException ne) {
                }
            }
            //EOL AddON By KMJ AT 21.11.26
        }
        return result;
    }

}
