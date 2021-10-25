package daedan.mes.modbus.rtu.service;
import daedan.mes.modbus.rtu.domain.RtuInfo;

import java.util.Map;

public interface RtuService {
    Map<String, Object> connectRtu(RtuInfo vo);
}
