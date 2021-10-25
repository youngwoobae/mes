package daedan.mes.modbus.rot.detector.service;

import java.util.Map;

public interface RotService {
    String modbusRotRead(Map<String, Object> paraMap);
    Map<String, Object> modbusRotReadByType2(Map<String, Object> paraMap);
}
