package daedan.mes.modbusapi.service;

import java.util.List;
import java.util.Map;

public interface ModbusApiService {
    Map<String, Object> getModbusApiData(Map<String, Object> paraMap);

    List<Map<String, Object>> getModbusApiListData(Map<String, Object> paraMap);
}
