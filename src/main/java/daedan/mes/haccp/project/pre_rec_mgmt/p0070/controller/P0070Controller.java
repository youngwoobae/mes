package daedan.mes.haccp.project.pre_rec_mgmt.p0070.controller;

import daedan.mes.haccp.project.pre_rec_mgmt.p0070.service.P0070ServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0070.controller</li>
 * <li>설 명 : P0070Controller.java</li>
 * <li>작성일 : 2021. 10. 04.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/daedan/mes/haccp/project/prc_rec_mgmt/p0070")
public class P0070Controller {
	private Log log = LogFactory.getLog(this.getClass());
	/**
	 * 일지 기록 서비스
	 */
	@Autowired
	private P0070ServiceImpl p0070ServiceImpl;

}
