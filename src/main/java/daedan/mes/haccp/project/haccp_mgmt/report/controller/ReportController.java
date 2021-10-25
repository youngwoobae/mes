package daedan.mes.haccp.project.haccp_mgmt.report.controller;


import daedan.mes.common.domain.Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.haccp_mgnt.c0001.controller</li>
 * <li>설 명 : C001Controller.java</li>
 * <li>작성일 : 2021. 10. 04.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/daedan/mes/haccp/project/haccp_mgmt/report")
public class ReportController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;


    @PostMapping(value="/startJob")
    public Result startJob(@RequestBody Map<String, Object> paraMap){
        String tag = "BoardController.conditions110Notice => ";
        Result result = Result.successInstance();
        paraMap.put("term",env.getProperty("boardInterval"));
        //result.setData(bordService.getFindBoardTerm(paraMap));
        return result;
    }
    @PostMapping(value="/endJob")
    public Result endJob(@RequestBody Map<String, Object> paraMap){
        String tag = "BoardController.conditions110Notice => ";
        Result result = Result.successInstance();
        paraMap.put("term",env.getProperty("boardInterval"));
        //result.setData(bordService.getFindBoardTerm(paraMap));
        return result;
    }
    @PostMapping(value="/detectForigeMatr")
    public Result detectForignMatr(@RequestBody Map<String, Object> paraMap){
        String tag = "BoardController.detectForignMatr => ";
        Result result = Result.successInstance();
        paraMap.put("term",env.getProperty("boardInterval"));
        //result.setData(bordService.getFindBoardTerm(paraMap));
        return result;
    }


}
