package daedan.mes.io.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.io.service.IoService;
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
@RequestMapping("/api/daedan/mes/io")
public class IoController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private IoService ioService;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/matrWhList")
    public Result matrWhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.whList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());

        paraMap.put("whTp", Long.parseLong(env.getProperty("code.whgb.matr")));//????????????
        result.setData(ioService.getWhList(paraMap));
        result.setTotalCount(ioService.getWhListCount(paraMap));

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

    @PostMapping(value = "/prodWhList")
    public Result prodWhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.prodWhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());

        paraMap.put("whTp", Long.parseLong(env.getProperty("code.whgb.prod")));//????????????
        result.setData(ioService.getWhList(paraMap));
        result.setTotalCount(ioService.getWhListCount(paraMap));

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

    @PostMapping(value = "/whInfo")
    public Result whInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.whInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(ioService.getWhInfo(paraMap));

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

     @PostMapping(value = "/dropWhInfo")
    public Result dropWhInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.dropWhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.dropWhInfo(paraMap);

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

    @PostMapping(value = "/condition320_ProdOwh")
    public Result condition320_ProdOwh(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboWhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = new HashMap<String,Object>();
        paraMap.put("whTp", Long.parseLong(env.getProperty("code_whgb_matr")));
        rmap.put("whList", ioService.getComboWhList(paraMap)); //????????????
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

    @PostMapping(value = "/comboWhList")
    public Result comboWhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboWhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(ioService.getComboWhList(paraMap));

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

    /*????????????????????????*/
    @PostMapping(value = "/comboEmbIwh")
    public Result comboEmbIwh(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboEmbIwh => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("cmpyList", ioService.getComboPursCmpy(paraMap)); //?????????????????? ????????? ???????????????

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("base_retn")));
        rmap.put("retnList", codeService.getComboCodeList(paraMap)); //??????????????????


        paraMap.put("selectStr","??????????????????");
        rmap.put("whList", ioService.getComboWhList(paraMap)); //????????????


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

    /*????????????????????????*/
    @PostMapping(value = "/comboEmbIwhWhInfo")
    public Result comboEmbIwhWhInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboEmbIwhWhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String, Object>();
        paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_prod"))); // ?????? ??????
        rmap.put("whList", ioService.comboEmbIwhWhInfo(paraMap)); //????????????
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

    /*????????????????????????*/
    @PostMapping(value = "/inspItemList")
    public Result inspItemList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.inspItemList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("inspList", codeService.getCodeList(paraMap)); //????????????????????????
        rmap.put("inspListCount", codeService.getCodeListCount(paraMap)); //?????????????????????????????????
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

    /*????????????????????????*/
    @PostMapping(value = "/comboPursCmpy")
    public Result comboPursCmpy(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboEmbIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = new HashMap<String, Object>();
        rmap.put("cmpyList", ioService.getComboPursCmpy(paraMap)); //?????????????????? ????????? ???????????????
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

    /*???????????????????????????*/
    @PostMapping(value = "/comboOwhReqDtList")
    public Result comboOwhReqDtList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboOwhReqDtList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getComboOwhReqDtList(paraMap));

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


    @PostMapping(value = "/waitMatrIwhList")
    public Result waitMatrIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.waitMatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        paraMap.put("whTp", Long.parseLong(env.getProperty("code_whgb_matr")));//????????????
        result.setData(ioService.getWaitMatrIwhList(paraMap));
        result.setTotalCount(ioService.getWaitMatrIwhListCount(paraMap));

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

    @PostMapping(value = "/MatrIwhList")
    public Result MatrIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.MatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "paraMap = " + paraMap.toString());
        result.setData(ioService.getMatrIwhList(paraMap));
        result.setTotalCount(ioService.getMatrIwhListCount(paraMap));

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

    @PostMapping(value = "/MatrIwhInfo")
    public Result MatrIwhInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.MatrIwhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getMatrIwhInfo(paraMap));

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

    /*????????????*/
    @PostMapping(value = "/saveMatrStk")
    public Result saveMatrStk(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.saveMatrStk(paraMap);

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

    /*???????????? --- 21.06.01 ???????????? ????????? ??????*/
//    @PostMapping(value = "/retnMatrStk")
//    public Result retnMatrStk(@RequestBody Map<String, Object> paraMap, HttpServletRequest request) {
//        String tag = "ioController.retnMatrStk => ";
//        Result result = Result.successInstance();
//        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
//        paraMap.put("userId", paraMap.get("userId"));
//        ioService.retnMatrStk(paraMap);


//        return result;
//    }

    /*???????????? ???????????? ??????*/
    @PostMapping(value = "/cnfmAbleIwh")
    public Result CnfmAbleIwh(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.ableCnfmIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getCnfmAbleIwh(paraMap));

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

    /*????????????*/
    @PostMapping(value = "/cnfmIwh")
    public Result cnfmIwh(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.ableIwhCnfm => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        ioService.cnfmIwh(paraMap);

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


    /*???????????? ????????????*/
    @PostMapping(value = "/matrStkList")
    public Result matrStkList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrStkList => ";
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getMatrStkList(paraMap));
        result.setTotalCount(ioService.getMatrStkListCount(paraMap));

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

    /*????????????*/
    @PostMapping(value = "/matrWhStkList")
    public Result matrWhStkList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrWhStkList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrWhStkList(paraMap));
        result.setTotalCount(ioService.getMatrWhStkListCount(paraMap));

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

    @PostMapping(value = "/prodWhStkList")
    public Result prodWhStkList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.prodWhStkList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getProdWhStkList(paraMap));
        result.setTotalCount(ioService.getProdWhStkListCount(paraMap));

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



    /*???????????? ????????????-????????? ????????????*/
    @PostMapping(value = "/matrStkIwhList")
    public Result matrStkIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrStkIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info("?????????????" + paraMap.get("matrNo"));

        result.setData(ioService.getMatrStkIwhList(paraMap));
        result.setTotalCount(ioService.getMatrStkIwhListCount(paraMap));

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

    /*???????????? ????????????-????????? ????????????*/
    @PostMapping(value = "/matrStkOwhList")
    public Result matrStkOwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrStkOwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info("?????????????" + paraMap.get("matrNo"));

        result.setData(ioService.getMatrStkOwhList(paraMap));
        result.setTotalCount(ioService.getMatrStkOwhListCount(paraMap));

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

    /*??????????????????*/
    @PostMapping(value = "/matrOwhList")
    public Result matrOwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrOwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getReqOwhList(paraMap));
        result.setTotalCount(ioService.getReqOwhListCount(paraMap));

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

    /*??????????????? ??????*/
    @PostMapping(value = "/matrOwhInfo")
    public Result matrOwhInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrOwhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.matrOwhInfo(paraMap));

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

    /*??????????????? ??????*/
    @PostMapping(value = "/saveMatrOwh")
    public Result saveMatrOwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.saveMatrOwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.saveMatrOwh(paraMap);

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

    /*??????????????? ??????*/
    @PostMapping(value = "/dropMatrIwh")
    public Result dropMatrIwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.dropMatrIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.dropMatrIwh(paraMap);

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

    /*????????????????????????*/
    @PostMapping(value = "/dropMatrOwh")
    public Result dropMatrOwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.dropMatrOwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.dropMatrOwh(paraMap);

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
    @PostMapping(value="/conditions225")
    public Result condition225(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_matr")));
        rmap.put("comboWhNo", cmmnService.getComboWh(paraMap));

        paraMap.put("selectStr", "???????????????");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.palet")));
        rmap.put("comboPalet", codeService.getComboCodeList(paraMap));

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
    @PostMapping(value="/conditions325")
    public Result condition235(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_prod")));
        rmap.put("comboWhNo", cmmnService.getComboWh(paraMap));

        paraMap.put("selectStr", "???????????????");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.palet")));
        rmap.put("comboPalet", codeService.getComboCodeList(paraMap));

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

    @PostMapping(value="/conditions315")
    public Result condition315(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_prod")));
        rmap.put("comboWhNo", cmmnService.getComboWh(paraMap));
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


    /*???????????? ????????????-??????????????? ???????????????*/
    @PostMapping(value = "/reqMatrIwhSumList")
    public Result getReqMatrIwhSumList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.reqMatrIwhSumList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("whNo", 0); //???????????? ????????? ?????????????????? ??????.
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "params = " + paraMap.toString());//kill
        result.setData(ioService.getReqMatrIwhSumList(paraMap));
        result.setTotalCount(ioService.getReqMatrIwhSumListCount(paraMap));

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

    /*???????????? ????????????-????????? ???????????????*/
    @PostMapping(value = "/reqMatrIwhList")
    public Result reqMatrIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.getReqMatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("whNo", 0); //???????????? ????????? ?????????????????? ??????.
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getReqMatrIwhList(paraMap));
        result.setTotalCount(ioService.getReqMatrIwhListCount(paraMap));

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
    @PostMapping(value = "/getPursMatrInfo")
    public Result getPursMatrInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.getPursMatrInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = ioService.getPursMatrInfo(paraMap);
        result.setData(rmap);
        /*
        StringBuffer buf = new StringBuffer();
        buf.append(env.getProperty("img.root.path")).append("matr/iwh/").append(rmap.get("iwh_no")).append(".png");
        rmap.put("bar_code_url", buf.toString());
        log.info(tag + "bar_code_url => " + rmap.get("bar_code_url"));
        */

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
    @PostMapping(value = "/reqMatrIwhInfo")
    public Result reqMatrIwhInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.reqMatrIwhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = ioService.getReqMatrIwhInfo(paraMap);
        result.setData(rmap);
        /*
        StringBuffer buf = new StringBuffer();
        buf.append(env.getProperty("img.root.path")).append("matr/iwh/").append(rmap.get("iwh_no")).append(".png");
        rmap.put("bar_code_url", buf.toString());
        log.info(tag + "bar_code_url => " + rmap.get("bar_code_url"));
        */

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

    /*????????? ???????????? ????????? ??????????????? ?????????*/
    @PostMapping(value = "/reqMatrOwhSumList")
    public Result reqMatrOwhSumList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.reqMatrOwhSumList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("indcSts", Long.parseLong(env.getProperty("code.base.indcsts")));
        log.info(tag + "params = " + paraMap.toString());//kill
        result.setData(ioService.getReqMatrOwhSumList(paraMap));
        result.setTotalCount(ioService.getReqMatrOwhSumListCount(paraMap));

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

    /*????????? ???????????? ????????? ??????????????? ?????????*/
    @PostMapping(value = "/reqMatrOwhList")
    public Result reqMatrOwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("indcSts", Long.parseLong(env.getProperty("code.base.indcsts")));
        result.setData(ioService.getReqMatrOwhList(paraMap));
        result.setTotalCount(ioService.getReqMatrOwhListCount(paraMap));

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

    /*????????? ???????????? ????????? ?????????????????? ?????????*/
    @PostMapping(value = "/matrPosList")
    public Result matrPosList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrPosList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrPosList(paraMap));
        result.setTotalCount(ioService.getMatrPosListCount(paraMap));

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

    /*????????? ???????????? ????????? ?????????????????? ?????????*/
    @PostMapping(value = "/matrPosInfo")
    public Result matrPosInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.matrPosInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getMatrPosInfo(paraMap));

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

    /*????????????,???????????? ????????????*/
    @PostMapping(value = "/conditionsEmbExportExec")
    public Result conditionsEmbExportExec(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.conditionsEmbExportExec => ";
        Map<String, Object> rmap = new HashMap<String, Object>();
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        //paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.saleunit")));
        //paraMap.put("selectStr","????????????");
        //rmap.put("owhUnit", codeService.getComboCodeList(paraMap)); //????????????

        //paraMap.put("selectStr","????????????");
        //paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_matr")));
        //rmap.put("whList",ioService.getComboWhList(paraMap)); //????????????

        //SOL AddOn By KMJ AT 21.10.19
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.prodTp")));
        paraMap.put("selectStr","??????????????????");
        rmap.put("comboProdTp",codeService.getComboCodeList(paraMap));
        //EOL AddOn By KMJ AT 21.10.19

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
    /*????????????????????????*/
    @PostMapping(value = "/forImportProdList")
    public Result forImportProdList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.forImportProdList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(ioService.getForImportProdList(paraMap));
        result.setTotalCount(ioService.getForImportProdListCount(paraMap));

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

    /*????????????????????????*/
    @PostMapping(value = "/exportPlanInfo")
    public Result exportPlanInfo(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.exportPlanInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String, Object> rmap = ioService.getExportPlanInfo(paraMap);
        StringBuffer buf = new StringBuffer();
        buf.append(env.getProperty("img.root.path")).append("ord/").append(rmap.get("ord_no")).append(".png");
        rmap.put("bar_code_url",buf.toString());
        log.info(tag + "bar_code_url => " + rmap.get("bar_code_url"));
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

    /*???????????? ????????????*/
    @PostMapping(value = "/exportProdList")
    public Result exportProdList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.exportProdInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getExportProdList(paraMap));

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
    /*??????????????????*/
    @PostMapping(value = "/exportExecInfo")
    public Result exportExecInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.exportExecInfo => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getExportExecInfo(paraMap));

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
    /*?????????????????? ????????????*/
    @PostMapping(value = "/saveExportProdInfo")
    public Result saveExportProdInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.saveExportProdInfo => ";
        Map<String, Object> rmap = new HashMap<String, Object>();
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.saveExportProdInfo(paraMap);
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

    /*????????? ????????? ???????????? ?????? ????????????*/
    @PostMapping(value = "/comboMatrWhList")
    public Result comboMatrWhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.comboMatrWhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getComboMatrWhList(paraMap));

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

    /*?????????????????? ???????????? - prodStk*/
    @PostMapping(value = "/extrProdStk")
    public Result extrProdStk(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.extrMatrStk => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.extrProdStk(paraMap);

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

    /*?????????????????? ???????????? - prodOwh*/
    @PostMapping(value = "/setProdOwh")
    public Result setProdOwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.setProdOwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.setProdOwh(paraMap);

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

    /*?????? ????????????-??????????????? ??????????????? 123123*/
    @PostMapping(value = "/reqProdIwhSumList")
    public Result reqProdIwhSumList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        String tag = "ioController.reqProdIwhSumList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        if(custNo == 6) {
            result.setData(ioService.getDaedongReqProdIwhSumList(paraMap));
            result.setTotalCount(ioService.getDaedongReqProdIwhSumListCount(paraMap));
        }else{
            result.setData(ioService.getReqProdIwhSumList(paraMap));
            result.setTotalCount(ioService.getReqProdIwhSumListCount(paraMap));
        }

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

    @PostMapping(value = "/prodIwhList")
    public Result prodIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session) {
        paraMap.put("pageSz", 1000);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Result result = Result.successInstance();
        result.setData(ioService.getProdIwhList(paraMap));
        result.setTotalCount(ioService.getProdIwhListCount(paraMap));

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

    @PostMapping(value = "/saveProdIwh")
    public Result saveProdIwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        ioService.saveProdIwh(paraMap);

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

    @PostMapping(value = "/dropProdIwh")
    public Result dropProdIwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        ioService.dropProdIwh(paraMap);

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

    @PostMapping(value = "/prodIwhInfo")
    public Result prodIwhInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodIwhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(ioService.getProdIwhInfo(paraMap));

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

    /*?????? ???????????? ????????? ??????????????? ?????????*/
    @PostMapping(value = "/reqProdOwhSumList")
    public Result reqProdOwhSumList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.reqProdOwhSumList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.wait.owh"))); //????????????
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        log.info(tag + "params = " + paraMap.toString());//kill
        result.setData(ioService.getReqProdOwhSumList(paraMap));
        result.setTotalCount(ioService.getReqProdOwhSumListCount(paraMap));

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

    /*?????? ???????????? ????????? ??????????????? ?????????*/
    @PostMapping(value = "/reqProdOwhList")
    public Result reqProdOwhList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.reqProdOwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap)); //offset??? ????????? ??? 2021.03.31
        //paraMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.wait.owh"))); //????????????
        result.setData(ioService.getReqProdOwhList(paraMap));
        result.setTotalCount(ioService.getReqProdOwhListCount(paraMap));

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

    /*???????????? ???????????? ?????????*/
    @PostMapping(value = "/makeIndcList")
    public Result makeIndcList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.makeIndcList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMakeIndcList(paraMap));
        result.setTotalCount(ioService.getMakeIndcListCount(paraMap));

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

    /*???????????? ???????????? ?????????*/
    @PostMapping(value = "/kioMatrIwhList")
    public Result kioMatrIwhList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.kioMatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getKioMatrIwhList(paraMap));
        result.setTotalCount(ioService.getKioMatrIwhListCount(paraMap));

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

    /*???????????? ???????????? ??????*/
    @PostMapping(value = "/saveKioMatrIwh")
    public Result saveKioMatrIwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.saveKioMatrIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.saveKioMatrIwh(paraMap);

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

    /*???????????? ???????????? ????????? ??????*/
    @PostMapping(value = "/saveMatrStkPosList")
    public Result saveMatrStkPosList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.saveMatrStkPosList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        log.info(tag + "paraMap == " + paraMap);

        Result result = Result.successInstance();
        ioService.saveMatrStkPosList(paraMap);

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

    @PostMapping(value = "/matrStkPosList")
    public Result matrStkPosList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.matrStkPosList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrStkPosList(paraMap));
        result.setTotalCount(ioService.getMatrStkPosListCount(paraMap));

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

    /*????????????*/
    @PostMapping(value = "/prodOutPlanList")
    public Result prodOutPlanList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(ioService.getProdOutPlanList(paraMap));

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

    @PostMapping(value = "/matchPosList")
    public Result matchPosList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.matchPosList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatchPosList(paraMap));
        result.setTotalCount(ioService.getMatchPosListCount(paraMap));

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

    @PostMapping(value = "/renewalMatrPos")
    public Result renewalMatrPos(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.renewalMatrPos => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.renewalMatrPos(paraMap);

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

    @PostMapping(value = "/renewalMatrIwh")
    public Result renewalMatrIwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.renewalMatrIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ioService.renewalMatrIwh(paraMap);

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

    @PostMapping(value = "/renewalPursMatr")
    public Result renewalPursMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.renewalPursMatr => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ioService.renewalPursMatr(paraMap);

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
    @PostMapping(value = "/getReqMatrIwhInfo")
    public Result getReqMatrIwhInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.getReqMatrIwhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(ioService.getReqMatrIwhInfo(paraMap));

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

    @PostMapping(value = "/MatrStkImpoList")
    public Result MatrStkImpoList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "IoController.MatrStkImpoList => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        result.setData(ioService.getMatrStkImpoList(paraMap));
        result.setTotalCount(ioService.getMatrStkImpoListCount(paraMap));

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

    @PostMapping(value = "/outMatrPos")
    public Result outMatrPos(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "IoController.outMatrPos => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.outMatrPos(paraMap);

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

    @PostMapping(value = "/updateMatrOwhStk")
    public Result updateMatrOwhStk(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "IwhController.updateMatrOwhStk => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ioService.updateMatrOwhStk(paraMap);

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

    @PostMapping(value = "/updateMatrOwh")
    public Result updateMatrOwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "IoController.updateMatrOwh => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.updateMatrOwh(paraMap);

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

    @PostMapping(value = "/updateMakeIndcMatr")
    public Result updateMakeIndcMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "IoController.updateMatrIndcMatr => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.updateMakeIndcMatr(paraMap);

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

    @PostMapping(value = "/tabletMatrOwh")
    public Result tabletMatrStk(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "IoController.tabletMatrOwh => ";

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.tabletMatrStk(paraMap);

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

    @PostMapping(value ="/tabletReqMatrIwhList")
    public Result tabletReqMatrIwhList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "IoController.tabletReqMatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("whNo", 0); //???????????? ????????? ?????????????????? ??????.
        paraMap.put("pursSts",Long.parseLong(env.getProperty("purs.sts.insp")));
        result.setData(ioService.getTabletReqMatrIwhList(paraMap));
        result.setTotalCount(ioService.getTabletReqMatrIwhListCount(paraMap));

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

    @PostMapping(value ="/getIwhHsrt")
    public Result getIwhHsrt(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "IoController.getIwhHsrt => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("whNo", 0); //???????????? ????????? ?????????????????? ??????.
        paraMap.put("pursSts",Long.parseLong(env.getProperty("purs.sts.insp")));
        result.setData(ioService.getIwhHsrt(paraMap));
        result.setTotalCount(ioService.getIwhHsrtCount(paraMap));

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

    @PostMapping(value = "/tabletMatrIwh")
    public Result tabletMatrIwh(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "IoController.tabletMatrIwh => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        ioService.tabletMatrIwh(paraMap);

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

    @PostMapping(value = "/getTabletWhNoAndMatrUnit")
    public Result getTabletWhNoAndMatrUnit(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "IoController.getTabletWhNoAndMatrUnit => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(ioService.getTabletWhNoAndMatrUnit(paraMap));

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
    // ?????? ?????????
    @PostMapping(value = "/OwhWhStkList")
    public Result OwhWhStkList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.comboOwhReqDtList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.get("ordNo").toString();
        paraMap.get("prodNo").toString();

        ioService.OwhWhStkList(paraMap);

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

    @PostMapping(value = "/owhMatrList")
    public Result owhMatrList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "ioController.owhMatrList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.owhMatrList(paraMap);

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

    /*?????????????????? ?????????????????? : ?????? MES???*/
    @PostMapping(value = "/matrForIwhList")
    public Result matrForIwhList(@RequestBody Map<String, Object> paraMap,  HttpSession session) {
        String tag = "ioController.matrForIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        //result.setData(ioService.getMatrForIwhList(paraMap));
        //result.setTotalCount(ioService.getMatrForIwhListCount(paraMap));
        result.setData(ioService.getMatrForIwhListByT(paraMap));
        result.setTotalCount(ioService.getMatrForIwhListByTCount(paraMap));

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

    /*?????????????????? ?????????????????? : ?????? MES???*/
    @PostMapping(value = "/matrForOwhList")
    public Result matrForOwhList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.matrForIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("procYn",uvo.getCustInfo().getProcYn()); //???????????????????????? --??????????????? ?????????.
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrForOwhList(paraMap));
        result.setTotalCount(ioService.getMatrForOwhListCount(paraMap));

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

    /*?????????????????? ?????????????????? : ?????? MES???*/
    @PostMapping(value = "/prodForIwhList")
    public Result prodForIwhList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodForIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        String procYn =  uvo.getCustInfo().getProcYn(); //??????????????????
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        if (procYn.equals("N")) {
            result.setData(ioService.getProdForIwhList(paraMap));
            result.setTotalCount(ioService.getProdForIwhListCount(paraMap));
        }
        else {
            result.setData(ioService.getMadeProdForIwhList(paraMap));
            result.setTotalCount(ioService.getMadeProdForIwhListCount(paraMap));
        }
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

    /*?????????????????? ?????????????????? : ?????? MES???*/
    @PostMapping(value = "/prodForOwhList")
    public Result prodForOwhList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodForOwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getProdForOwhList(paraMap));
        result.setTotalCount(ioService.getProdForOwhListCount(paraMap));

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


    /*???????????????????????????*/
    @PostMapping(value = "/saveMatrIwhList")
    public Result saveMatrIwhList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.saveMatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("iwhDt", paraMap.get("iwhDt").toString().substring(0, 10));
        paraMap.put("createDt", paraMap.get("createDt").toString().substring(0, 10));
        ioService.saveMatrIwhList(paraMap);

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

    /*???????????????????????????*/
    @PostMapping(value = "/saveMatrOwhList")
    public Result saveMatrOwhList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.saveMatrOwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("owhDt", paraMap.get("owhDt").toString().substring(0, 10));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.saveMatrOwhList(paraMap);

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
    /*???????????????????????????*/
    @PostMapping(value = "/saveProdIwhList")
    public Result saveProdIwhList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.saveProdIwhList => ";
        Result result = Result.successInstance();
        //UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        UserInfo uvo = userService.getUserInfoById(Long.parseLong(paraMap.get("userId").toString()));
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.saveProdIwhList(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    /*???????????????????????????*/
    @PostMapping(value = "/saveProdOwhList")
    public Result saveProdOwhList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.saveProdOwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("owhDt", paraMap.get("owhDt").toString().substring(0, 10));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.saveProdOwhList(paraMap);

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
     * ?????????????????? ?????? ??????
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/dropProdIwhList")
    public Result dropProdIwhList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("procYn", uvo.getCustInfo().getProcYn());
        ioService.dropProdIwhList(paraMap);

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    /*?????? ???????????? ????????? */
    @PostMapping(value="/stsMatrIwhList") //????????????
    public Result stsMatrIwhList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.stsMatrIwhList(paraMap));
        result.setTotalCount(ioService.stsMatrIwhListCount(paraMap));

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
    /*?????? ???????????? ????????? */
    @PostMapping(value="/stsMatrOwhList") //????????????
    public Result stsMatrOwhList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.stsMatrOwhList(paraMap));
        result.setTotalCount(ioService.stsMatrOwhListCount(paraMap));

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
    /*?????? ???????????? ????????? */
    @PostMapping(value="/stsProdIwhList") //????????????
    public Result stsProdIwhList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.stsProdIwhList(paraMap));
        result.setTotalCount(ioService.stsProdIwhListCount(paraMap));

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

    /*?????? ???????????? ????????? */
    @PostMapping(value="/stsProdOwhList")
    public Result stsProdOwhList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.stsProdOwhList(paraMap));
        result.setTotalCount(ioService.stsProdOwhListCount(paraMap));

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

    /*???????????? ?????? ????????? (?????????) */
    @PostMapping(value="/matrOwhWhNm") //????????????
    public Result matrOwhWhNm(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrOwhWhNm(paraMap));

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

    /*???????????? ?????? ????????? (?????????) */
    @PostMapping(value="/matrWhNm") //????????????
    public Result matrWhNm(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_matr")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.matrWhNm(paraMap));
        result.setTotalCount(ioService.matrWhNmCount(paraMap));

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

    /*???????????? ?????? ????????? (?????????) */
    @PostMapping(value="/prodWhNm") //????????????
    public Result prodWhNm(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("whTp",Long.parseLong(env.getProperty("wh_type_prod")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.prodWhNm(paraMap));
        result.setTotalCount(ioService.prodWhNmCount(paraMap));

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

    /*???????????? ????????? ????????? (??????????????? ????????? ?????? ??????) */
    @PostMapping(value="/matrOwhlist") //????????????
    public Result matrOwhlist(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrOwhList(paraMap));
        result.setTotalCount(ioService.getMatrOwhListCount(paraMap));

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

    @PostMapping(value = "/owhInfo")
    public Result owhInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.owhInfo => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(ioService.getOwhInfo(paraMap));

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

    @PostMapping(value = "/dropOwhInfo")
    public Result dropOwhInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "ioController.dropOwhInfo => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();
        ioService.dropOwhInfo(paraMap);

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

    @PostMapping(value="/IwhMatrList") //????????????
    public Result IwhMatrList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getIwhMatrList(paraMap));
        result.setTotalCount(ioService.getIwhMatrListCount(paraMap));

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

    @PostMapping(value="/OwhMatrList") //????????????
    public Result OwhMatrList(@RequestBody HashMap<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getOwhMatrList(paraMap));
        result.setTotalCount(ioService.getOwhMatrListCount(paraMap));

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

    @PostMapping(value="/IwhProdList") //????????????
    public Result IwhProdList(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getIwhProdList(paraMap));
        result.setTotalCount(ioService.getIwhProdListCount(paraMap));
        System.out.println("****************"+result);

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

    @PostMapping(value="/OwhProdList") //????????????
    public Result OwhProdList(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getOwhProdList(paraMap));
        result.setTotalCount(ioService.getOwhProdListCount(paraMap));

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

    @PostMapping(value = "/matrIwHstrList")
    public Result matrIwHstrList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodOwHstrList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrIwHstrList(paraMap));
        result.setTotalCount(ioService.getMatrIwHstrListCount(paraMap));

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

    @PostMapping(value = "/matrOwHstrList")
    public Result matrOwHstrList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodOwHstrList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getMatrOwHstrList(paraMap));
        result.setTotalCount(ioService.getMatrOwHstrListCount(paraMap));

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
    @PostMapping(value = "/prodIwHstrList")
    public Result prodIwHstrList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodOwHstrList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getProdIwHstrList(paraMap));
        result.setTotalCount(ioService.getProdIwHstrListCount(paraMap));

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
    @PostMapping(value = "/prodOwHstrList")
    public Result prodOwHstrList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.prodOwHstrList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(ioService.getProdOwHstrList(paraMap));
        result.setTotalCount(ioService.getProdOwHstrListCount(paraMap));

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

    @PostMapping(value="/getOwhHstr")
    public Result makeAccHstr(@RequestBody Map<String,Object> paraMap,HttpServletRequest request, HttpSession session){
        String tag = "SysMenuController/makeAccHstr=>";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        Result result = Result.successInstance();

        ioService.getOwhHstr(paraMap);

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
    /*?????????????????????*/
    @PostMapping(value = "/getMatrOwhHistList")
    public Result getMatrOwhHistList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "ioController.getMatrOwhHistList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Result result = Result.successInstance();
        ioService.getMatrOwhHistList(paraMap);

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
    @PostMapping(value = "/changeStkData")
    public Result changeStkData(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.changeStkData => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.changeStkData(paraMap);

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
    @PostMapping(value = "/dropStkData")
    public Result dropStkData(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session) {
        String tag = "ioController.dropStkData => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        ioService.dropStkData(paraMap);

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
     * ?????????????????? (custInfo.procYn????????? ?????? ???????????? ?????????.
     * custInfo.procYn = Y (??????????????????) ??? ??????????????? make_indc_matr  ???.
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value = "/dropMatrIwhList")
    public Result dropMatrIwhList(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session) {
        String tag = "ioController.dropMatrIwhList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("procYn",uvo.getCustInfo().getProcYn()); //??????????????????
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        ioService.dropMatrIwhList(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

}
