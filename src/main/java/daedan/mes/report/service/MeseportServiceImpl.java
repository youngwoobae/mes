package daedan.mes.report.service;

import daedan.mes.report.mapper.MesReportMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("mwaReportService")
public class MeseportServiceImpl implements MesReportService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MesReportMapper mapper;

    @Override
    public List<Map<String, Object>> getMetalDetectHstr(Map<String, Object> paraMap) {
        String tag = "reportService.getMetalDetectHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMetalDetectHstr(paraMap);
    }

    @Override
    public int getMetalDetectHstrCount(Map<String, Object> paraMap) {
        String tag = "reportService.getMetalDetectHstrCount => ";
        return mapper.getMetalDetectHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdOwhHstr(Map<String, Object> paraMap) {
        String tag = "reportService.getProdOwhHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdOwhHstr(paraMap);
    }

    @Override
    public int getProdOwhHstrCount(Map<String, Object> paraMap) {;
        return mapper.getProdOwhHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdIwhHstr(Map<String, Object> paraMap) {
        String tag = "reportService.getProdIwhHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdIwhHstr(paraMap);
    }

    @Override
    public int getProdIwhHstrCount(Map<String, Object> paraMap) {
        return mapper.getProdIwhHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getTmprLogHstr(Map<String, Object> paraMap) {
        String tag = "reportService.getTmprLogHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getTmprLogHstr(paraMap);
    }
}
