package daedan.mes.haccp.project.haccp_mgmt.c0010.controller;

import daedan.mes.haccp.project.haccp_mgmt.c0010.service.C0010ServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.haccp_mgnt.c0010.controller</li>
 * <li>설 명 : C010Controller.java</li>
 * <li>작성일 : 2021. 10. 04.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Controller
@RequestMapping("/api/daedan/mes/haccp/project/haccp_mgmt/C0010")
public class C0010Controller {
	private Log log = LogFactory.getLog(this.getClass());
	@Autowired
	private C0010ServiceImpl c0010ServiceImpl;
}
