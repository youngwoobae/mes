package daedan.mes.make.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.make.service.ExcelMakeService;
import daedan.mes.sysmenu.user.domain.AccHstr;
import daedan.mes.sysmenu.user.domain.EvntType;
import daedan.mes.sysmenu.user.domain.UserInfo;
import daedan.mes.sysmenu.user.service.UserService;
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

@RestController
@RequestMapping("/api/daedan/mes/excel/make")
public class ExcelMakeController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeSer;

    @Autowired
    private ExcelMakeService excelservice;

    @Autowired
    private UserService userService;



    @PostMapping(value="/makeBomByExcel") //하담식품 상품 / 원자재 등록
    public Result makeBomByExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        String tag = "ProdController.makeBomByExcel => ";
        log.info(tag + "ipaddr  = " +  paraMap.get("ipaddr"));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot",uvo.getCustInfo().getFileRoot());
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            excelservice.makeBomByExcel(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");
        return result;
    }

    @PostMapping(value="/makeMakeIndcMpByExcel") //하담식품 상품 / 원자재 등록
    public Result makeMakeIndcMpByExcel(@RequestBody HashMap<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("fileRoot", uvo.getCustInfo().getFileRoot());
        paraMap.put("matrTp", Integer.parseInt(env.getProperty("code.matrtp.matr")));
        try {
            excelservice.makeMakeIndcMpByExcel(paraMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData("OK");

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
