package daedan.mes.equip.controller;


import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.service.EquipBomService;
import daedan.mes.equip.service.EquipService;
import daedan.mes.user.domain.UserInfo;
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
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping("/api/daedan/mes/equipbom")
public class EquipBomController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private EquipBomService bomService;

    @Autowired
    private EquipService equipService;

    @PostMapping(value="/equipBomList")
    public Result equipBomList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(bomService.getEquipBomList(paraMap));
        result.setTotalCount(bomService.getEquipBomListCount(paraMap));
        return result;
    }
    @PostMapping(value="/equipBomInfo")
    public Result equipBomInfo(@RequestBody Map<String, Object> paraMap  , HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = bomService.getEquipBomInfo(paraMap);
        result.setData(rmap);
        return result;
    }

    @PostMapping(value="/saveEquipBomCart")
    public Result saveEquipBomCart(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        bomService.saveEquipBomCart(paraMap);
        return result;
    }
    @PostMapping(value="/saveEquipBom")
    public Result saveEquipBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> bomMap = (Map<String, Object> )paraMap.get("bomInfo");
        bomMap.put("ipaddr", NetworkUtil.getClientIp(request));
        bomMap.put("userId", paraMap.get("userId"));
        bomService.saveEquipBom(bomMap);
        return result;
    }
    @PostMapping(value="/dropEquipBom")
    public Result dropEquipBom(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        bomService.dropEquipBom(paraMap);
        return result;
    }

    @PostMapping(value="/addEquipBomList")
    public Result addEquipBomList(@RequestBody Map<String, Object> paraMap , HttpServletRequest request  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("matr_no", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        String resource = "config/mes.properties";
        Properties properties = new Properties();

        try {
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            bomService.addEquipBomList(paraMap);

        } catch (IOException e) {
            e.printStackTrace();
            Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));
        }
        return result;
    }

}
