package daedan.mes.modbus.tcp.service;

import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.digitalpetri.modbus.master.ModbusTcpMasterConfig;
import com.digitalpetri.modbus.requests.ReadCoilsRequest;
import com.digitalpetri.modbus.requests.ReadDiscreteInputsRequest;
import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.responses.ReadCoilsResponse;
import com.digitalpetri.modbus.responses.ReadDiscreteInputsResponse;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;
import daedan.mes.equip.repository.EquipMngrHstrRepository;
import daedan.mes.spot.service.SpotEquipService;
import de.re.easymodbus.exceptions.ConnectionException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service("tcpService")
public class TcpServiceImpl implements  TcpService{
    private Log log = LogFactory.getLog(this.getClass());
    private boolean isOper = false;
    private int failCnt = 0;
    @Autowired
    private Environment env;



    @Override
    public Map<String, Object> modbusTcpRead(Map<String, Object> paraMap) {
       return this.modbusDataRead(paraMap);
    }
    /*DATA 수신*/
    private Map<String,Object> modbusDataRead(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.modbusRead => ";
        int startAddr  = Integer.parseInt(paraMap.get("startAddr").toString());//0
        int dataLength = Integer.parseInt(paraMap.get("dataLength").toString());//2

        //log.info(tag + "startAddr = " + startAddr);
        //log.info(tag + "dataLength = " + dataLength);
        //startAddr = 40000;
        //int modbusReply[] = new int[dataLength + 1]; //포장기에서 보내는 데이터배열ƒ√
        Map<String,Object> rmap = this.setModbusConnection(paraMap);
        if (rmap.get("status").toString().equals("FAIL")) {
            rmap.put("msg", "설비가 비기동중 입니다.");
        }
        else {
            ModbusClient modbusClient = (ModbusClient) rmap.get("tcpClient");
            if ( modbusClient != null) {
                try {
                    int modbusReply[] = modbusClient.ReadHoldingRegisters(1, 2); //ModbusTcp Data Read
                    for (int idx = 0; idx < modbusReply.length; idx++) {
                        log.info(tag + "modbusTcpReply[{" + idx + " ]  = " + modbusReply[idx]);
                    }
                    rmap.put("status", "SUCCESS");
                    rmap.put("rcvData", modbusReply);
                    rmap.put("msg", "");
                    this.failCnt = 0;
                } catch (ConnectException ce) {
                    if (++this.failCnt > 10) {
                        this.failCnt = 0;
                        rmap.put("status", "REFUSED");
                        rmap.put("rcvData", null);
                        rmap.put("msg", "통신 상태가 불안정하여 접속이 차단 되었습니다");
                        log.error(tag + ce.getLocalizedMessage());
                    }
                } catch (IOException ie) {
                    if (++this.failCnt > 10) {
                        this.failCnt = 0;
                        rmap.put("status", "FAIL");
                        rmap.put("rcvData", null);
                        rmap.put("msg", "데이터 송수신이 불안정 합니다.");
                        log.error(tag + ie.getLocalizedMessage());
                    }
                } catch (Exception e) {
                    log.error(tag + e.getLocalizedMessage());
                } finally {
                    this.isOper = false;
                    if (modbusClient.isConnected()) {
                        try {
                            modbusClient.Disconnect();
                            log.info("modbus disconnmected.....");
                        } catch (IOException e) {
                            //log.error(tag + "modbusClient.Disconnect Exception" + e.getLocalizedMessage());
                        }
                    }
                }
            }
        }
        return rmap;
    }




    /*포장기 PLC 데이터 송신 : 현재 구조로는 보낼일이 없음*/
    @Override
    public Map<String,Object> modbusWrite(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.modbusWrite => ";
        int startAddr  = Integer.parseInt(paraMap.get("startAddr").toString());//0
        int dataLength = Integer.parseInt(paraMap.get("dataLength").toString());//2

        log.info(tag + "startAddr = " + startAddr);
        log.info(tag + "dataLength = " + dataLength);

        Map<String,Object> rmap = this.setModbusConnection(paraMap);
        if (rmap.get("status").toString().equals("FAIL")) {
            rmap.put("status", "FAIL");
            rmap.put("msg", "설비가 비기동중 입니다..");
        }
        else {
            ModbusClient modbusClient = (ModbusClient) rmap.get("tcpClient");
            try {
                int value = Integer.parseInt(paraMap.get("writeVal").toString());
                log.info(tag + "value = " + value);
                modbusClient.WriteSingleRegister(startAddr, value);
            } catch (ModbusException e) {
                rmap.put("status", "FAIL");
                rmap.put("msg", e.getLocalizedMessage());
                //e.printStackTrace();
            } catch (IOException e) {
                rmap.put("status", "IOFAIL");
                rmap.put("msg", e.getLocalizedMessage());
                e.printStackTrace();
                // } catch (NullPointerException e) {
                rmap.put("status", "FAIL");
                rmap.put("msg", e.getLocalizedMessage());
                //e.printStackTrace();
            }
        }
        return rmap;
    }
    /*PLC IP 및 PORT 추출*/
    private Map<String,Object> setModbusConnection(Map<String,Object> paraMap ) {
        String tag = "MakeIndcService.setModbusConnection => ";
        log.info(tag + "params = " + paraMap.toString());
        ModbusClient modbusClient = new ModbusClient();
        Map<String,Object> rmap = new HashMap<String,Object>();
        modbusClient.setUnitIdentifier(Byte.parseByte(paraMap.get("deviceNo").toString()));
        String deviceIp = paraMap.get("deviceIp").toString();
        int devicePort = Integer.parseInt(paraMap.get("devicePort").toString());

        try {
            if (modbusClient.isConnected()) {
                try {
                    modbusClient.Disconnect();
                } catch (IOException ioException) {
                   // ioException.printStackTrace();
                }
            }
            modbusClient.Connect(deviceIp,devicePort);
            log.info(tag + " Conection Success ^^^^^^^ .......");
            rmap.put("tcpClient",modbusClient);
            rmap.put("status","SUCCESS");
            rmap.put("msg","");
        }
        catch (Exception e) {
            log.error(tag + " Conection Fail OTL OTL OTL....." + e.getMessage());
            rmap.put("status","FAIL");
            if (modbusClient.isConnected()) {
                try {
                    modbusClient.Disconnect();
                } catch (IOException ioException) {
                    //log.error(tag  + "Occurs DisConnection Exception..." + e.getLocalizedMessage()) ;
                }
            }
        }
        return rmap;
    }


    //이하 비사용 method
    @Override
    public ModbusTcpMaster initModbus() {
        ModbusTcpMasterConfig config = new ModbusTcpMasterConfig.Builder("localhost").setPort(502).build();
        return new ModbusTcpMaster(config);
    }

    @Override
    public void closeModbus(ModbusTcpMaster master) {
        master.disconnect();
        Modbus.releaseSharedResources();
    }
    @Override
    public Number readHoldingRegisters(ModbusTcpMaster master, int address, int quantity, int unitId) throws InterruptedException, ExecutionException {
        Number result = null;
        CompletableFuture<ReadHoldingRegistersResponse> future = master.sendRequest(new ReadHoldingRegistersRequest(address, quantity), unitId);
        ReadHoldingRegistersResponse readHoldingRegistersResponse = future.get();
        if (readHoldingRegistersResponse != null) {
            ByteBuf buf = readHoldingRegistersResponse.getRegisters();
            result = buf.readFloat();
            ReferenceCountUtil.release(readHoldingRegistersResponse);
        }
        return result;
    }
    /*출력포트 상태를 비트 단위로 읽어냄*/
    @Override
    public Boolean readCoils(ModbusTcpMaster master, int address, int quantity, int unitId)
            throws InterruptedException, ExecutionException {
        Boolean result = null;
        CompletableFuture<ReadCoilsResponse> future = master.sendRequest(new ReadCoilsRequest(address, quantity),
                unitId);
        ReadCoilsResponse readCoilsResponse = future.get();
        if (readCoilsResponse != null) {
            ByteBuf buf = readCoilsResponse.getCoilStatus();
            result = buf.readBoolean();
            ReferenceCountUtil.release(readCoilsResponse);
        }
        return result;
    }
    @Override
    public Boolean readDiscreteInputs(ModbusTcpMaster master, int address, int quantity, int unitId)
            throws InterruptedException, ExecutionException {
        Boolean result = null;
        CompletableFuture<ReadDiscreteInputsResponse> future = master.sendRequest(new ReadDiscreteInputsRequest(address, quantity), unitId);
        ReadDiscreteInputsResponse discreteInputsResponse = future.get();// 工具类做的同步返回.实际使用推荐结合业务进行异步处理
        if (discreteInputsResponse != null) {
            ByteBuf buf = discreteInputsResponse.getInputStatus();
            result = buf.readBoolean();
            ReferenceCountUtil.release(discreteInputsResponse);
        }
        return result;
    }


}
