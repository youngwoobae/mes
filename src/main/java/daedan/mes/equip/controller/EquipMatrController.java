package daedan.mes.equip.controller;

import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.service.EquipBomService;
import daedan.mes.equip.service.EquipMatrService;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/equipmatr")
public class EquipMatrController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private EquipMatrService emService;

    @Autowired
    private EquipBomService bomService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private UserService userService;


    @PostMapping(value = "/equipMatrList")
    public Result equipMatList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        String tag = "EquipMatController.equipMatrList =>";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "findTp = " + paraMap.get("findTp"));
        log.info(tag + "findSz = " + paraMap.get("findSz"));
        log.info(tag + "pageNo = " + paraMap.get("pageNo"));
        log.info(tag + "pageSz = " + paraMap.get("pageSz"));

        result.setData(emService.getEquipMatrList(paraMap));
        result.setTotalCount(emService.getEquipMatrListCount(paraMap));

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

    @PostMapping(value = "/emInfo")
    public Result EquipMat(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        String tag = "EquipMatController.emInfo=>";
        log.info(tag + "matr_no = " + paraMap.get("matr_no"));
        Long matrNo = Long.parseLong(paraMap.get("matr_no").toString());
        result.setData(emService.getEquipMatr(matrNo));

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


    @PostMapping(value = "/comboMatrTypeList")
    public Result comboMatTypeList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "EquipMatController.comboMatTypeList=>";
        log.info(tag + paraMap);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        String resource = "config/mes.properties";
        Properties properties = new Properties();
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            paraMap.put("parCodeNo", Long.parseLong(properties.getProperty("base_matr_cd")));
            List<Map<String, Object>> list = codeService.getComboCodeList(paraMap);
            result.setData(list);
        } catch (IOException e) {
            e.printStackTrace();
            Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));

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

    @PostMapping(value = "/comboMadeinList")
    public Result comboMadeinList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "EquipMatController.comboMadeinList=>";
        log.info(tag + paraMap);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        String resource = "config/mes.properties";
        Properties properties = new Properties();
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            paraMap.put("parCodeNo", Long.parseLong(properties.getProperty("base_madein_cd")));
            List<Map<String, Object>> list = codeService.getComboCodeList(paraMap);
            result.setData(list);
        } catch (IOException e) {
            e.printStackTrace();
            Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));
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

    //구매단위코드
    @PostMapping(value = "/comboPursUnitList")
    public Result comboPursUnitList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "EquipMatController.comboPursUnitList=>";
        log.info(tag);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        String resource = "config/mes.properties";
        Properties properties = new Properties();
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            paraMap.put("parCodeNo", Long.parseLong(properties.getProperty("base_purs_unit_cd")));
            List<Map<String, Object>> list = codeService.getComboCodeList(paraMap);
            result.setData(list);
        } catch (IOException e) {
            e.printStackTrace();
            Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));
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

    @PostMapping(value = "/saveEquipMatr")
    public Result saveEmInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "EquipMatController.saveEquipMatr=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> emMap = (Map<String, Object>) paraMap.get("equipMatr");
        emMap.put("ipaddr", NetworkUtil.getClientIp(request));
        emMap.put("userId", paraMap.get("userId"));
        log.info(tag + "emMap = " + emMap);
        emService.saveEquipMatr(emMap);
        result.setData(resultMap);
        log.info("EquipMatrController - resultMap === "  + resultMap);

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

    @PostMapping(value = "/apndEquipBomCart")
    public Result saveEquipBomCart(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "EquipMatController.apndEquipBomCart=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        log.info(tag + "paraMap = " + paraMap);
        bomService.apndEquipBomCart(paraMap);

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

    @PostMapping(value = "/dropEmInfo")
    public Result dropEmInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "EquipMatController.dropEmInfo=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        log.info(tag + "paraMap = " + paraMap);

        emService.dropEquipMatr(paraMap);
        result.setData(paraMap);

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

    @PostMapping(value = "/getEquipMatrList")
    public Result getEquipBomList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        String tag = "EquipMatController.getEquipBomList =>";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        log.info(tag + "findTp = " + paraMap.get("findTp"));
        log.info(tag + "findSz = " + paraMap.get("findSz"));
        log.info(tag + "pageNo = " + paraMap.get("pageNo"));
        log.info(tag + "pageSz = " + paraMap.get("pageSz"));

        result.setData(emService.getEquipBomList(paraMap));
        result.setTotalCount(emService.getEquipMatrListCount(paraMap));

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

    @PostMapping(value = "/getEquipMatrInfo")
    public Result equipMatrInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        String tag = "EquipMatController.getEquipMatrInfo=>";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        log.info(tag + "paraMap = " + paraMap);

        Map<String, Object> map = (Map<String, Object>) paraMap.get("equipMatr");
        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap = emService.equipMatrInfo(map);
        result.setData(resultMap);

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
