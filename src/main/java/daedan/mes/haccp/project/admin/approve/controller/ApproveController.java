package daedan.mes.haccp.project.admin.approve.controller;

import daedan.mes.haccp.project.admin.approve.service.ApproveService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/daedan/mes/haccp/project/admin/approve")
public class ApproveController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ApproveService approveService;
}
