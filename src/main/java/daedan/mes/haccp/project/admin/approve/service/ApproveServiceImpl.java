package daedan.mes.haccp.project.admin.approve.service;

import daedan.mes.haccp.project.admin.alert_period.mapper.ApMgmtMapper;
import daedan.mes.haccp.project.admin.approve.mapper.ApproveMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApproveServiceImpl implements ApproveService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    ApproveMapper approveMapper;
}
