package daedan.mes.report.service;

import daedan.mes.qc.mapper.QcMapper;
import daedan.mes.report.mapper.ReportMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("reportService")
public class ReportServiceImpl implements  ReportService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ReportMapper mapper;

    @Override
    public List<Map<String, Object>> getMetalDetectHstr(Map<String, Object> paraMap) {
        String tag = "reportService.getMetalDetectHstr => ";
        return mapper.getMetalDetectHstr(paraMap);
    }

    @Override
    public int getMetalDetectHstrCount(Map<String, Object> paraMap) {
        String tag = "reportService.getMetalDetectHstrCount => ";
        return mapper.getMetalDetectHstrCount(paraMap);
    }
}
