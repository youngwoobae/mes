package daedan.mes.modbus.sample.service;

import daedan.mes.modbus.rtu.domain.RtuInfo;

import java.util.Map;

public interface SampleService {
    Map<String, Object> getSampleTempData(Map<String, Object> paraMap);
    Map<String, Object> getSampleHumygData(Map<String, Object> paraMap);
    Map<String, Object> getSampleHumypData(Map<String, Object> paraMap);
}
