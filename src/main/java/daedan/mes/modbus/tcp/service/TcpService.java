package daedan.mes.modbus.tcp.service;

import com.digitalpetri.modbus.master.ModbusTcpMaster;
import de.re.easymodbus.modbusclient.ModbusClient;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface TcpService {
    /*PLC DATA 수신*/
    Map<String, Object> modbusTcpRead(Map<String, Object> paraMap);

    /*포장기 PLC 데이터 송신 : 현재 구조로는 보낼일이 없음*/
    Map<String,Object> modbusWrite(Map<String, Object> paraMap);

    //이하 비사용 method
    ModbusTcpMaster initModbus();

    void closeModbus(ModbusTcpMaster master);

    Number readHoldingRegisters(ModbusTcpMaster master, int address, int quantity, int unitId) throws InterruptedException, ExecutionException;

    /*출력포트 상태를 비트 단위로 읽어냄*/
    Boolean readCoils(ModbusTcpMaster master, int address, int quantity, int unitId) throws InterruptedException, ExecutionException;

    Boolean readDiscreteInputs(ModbusTcpMaster master, int address, int quantity, int unitId) throws InterruptedException, ExecutionException;

}
