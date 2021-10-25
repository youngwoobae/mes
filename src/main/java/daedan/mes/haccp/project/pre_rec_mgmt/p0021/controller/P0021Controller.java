package daedan.mes.haccp.project.pre_rec_mgmt.p0021.controller;

import daedan.mes.haccp.project.pre_rec_mgmt.p0021.service.P0021ServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0010.controller</li>
 * <li>설 명 : P0010Controller.java</li>
 * <li>작성일 : 2020. 7. 23.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/daedan/mes/haccp/project/prc_rec_mgmt/p0010")
public class P0021Controller {
	private Log log = LogFactory.getLog(this.getClass());
	@Autowired
	private P0021ServiceImpl p0021ServiceImpl;

}
