package daedan.mes.modbus.udp.service;

import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import daedan.mes.modbus.udp.domain.UdpInfo;

import java.util.Map;

public interface UdpService {
    ReadInputDiscretesResponse[] getModbusUdp(UdpInfo vo);
}
