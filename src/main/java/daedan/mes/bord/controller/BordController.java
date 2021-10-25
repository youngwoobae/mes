package daedan.mes.bord.controller;

import daedan.mes.bord.service.BordService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping("/api/daedan/mes/bord")
public class

BordController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private BordService bordService;

    @PostMapping(value="/conditions110Notice") //게시판 목록 - 공지사항
    public Result conditions110Notice(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("term",env.getProperty("boardInterval"));
        result.setData(bordService.getFindBoardTerm(paraMap));
        return result;
    }

    @PostMapping(value="/bordList") //게시판 목록 - 공지사항
    public Result bordList(@RequestBody Map<String, Object> paraMap, HttpSession session){

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(bordService.getBordList(paraMap));
        result.setTotalCount(bordService.getBordListCount(paraMap));
        return result;
    }

    @PostMapping(value="/saveBord") //공지사항 리스트 출력, 저장, 삭제
    public Result saveBord(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        Result result = Result.successInstance();
        bordService.saveBord(paraMap);
        return result;
    }

    @PostMapping(value="/deleteBord") //공지사항 삭제 후 보관
    public Result DeleteBord(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        result.setData(bordService.getBordInfo(paraMap,session));
        bordService.deleteBord(paraMap);

        return result;
    }
    @PostMapping(value="/bordInfo") //게시판 목록 - 공지사항
    public Result BordInfo(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "BoardController.BordInfo => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(bordService.getBordInfo(paraMap,session));
        return result;
    }
}
