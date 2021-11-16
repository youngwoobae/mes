package daedan.mes.dash.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dash.service.Dash06Service;
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

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/dash/ddkor")
public class Dash06Controller {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;


    @Autowired
    private Dash06Service dashService;

    @Autowired
    private UserService userService;
    /*배합실유량계*/
    @PostMapping(value="/flowMeterList")
    public Result flowMeterList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(dashService.getFlowMeterList(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveHstrEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    /*전력량계*/
    @PostMapping(value="/powerList")
    public Result powerList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        Map<String,Object> rmap = new HashMap<String,Object>();
        paraMap.put("equipFr",Integer.parseInt(env.getProperty("equip.mix.fr")));
        paraMap.put("equipTo",Integer.parseInt(env.getProperty("equip.mix.to")));
        rmap.put("mix_pwr",dashService.getPowerList(paraMap)); //배합실전력

        paraMap.put("equipFr",Integer.parseInt(env.getProperty("equip.fil.fr")));
        paraMap.put("equipTo",Integer.parseInt(env.getProperty("equip.fil.to")));
        rmap.put("fil_pwr",dashService.getPowerList(paraMap)); //충전실전력

        paraMap.put("equipFr",Integer.parseInt(env.getProperty("equip.pkg.fr")));
        paraMap.put("equipTo",Integer.parseInt(env.getProperty("equip.pkg.to")));
        rmap.put("pkg_pwr",dashService.getPowerList(paraMap)); //포장실전력
        result.setData(rmap);

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveHstrEvnt(custNo, acvo.getAccNo(), EvntType.READ, 3);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }




    @PostMapping(value="/getDashSpotTmpr")
    public Result getDashSpotTmpr(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(dashService.getDashSpotTmpr(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveHstrEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }


    @PostMapping(value="/getDashMainData")
    public Result getDashMainData(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(dashService.getDashMainData(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveHstrEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    @PostMapping(value="/getDashWrapRoomData")
    public Result getDashWrapRoomData(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(dashService.getDashWrapRoomData(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveHstrEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }



}
