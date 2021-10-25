package daedan.mes.modbus.rtu.service;

import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import daedan.mes.modbus.rtu.domain.RtuInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("rtuService")
public class RtuServiceImpl implements  RtuService{
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public Map<String, Object> connectRtu(RtuInfo vo)  {
        String tag = "RtuService.connetRtu ==> ";
        Map<String,Object> rmap = null;
        SerialConnection con = null; // the connection
        ModbusSerialTransaction trans = null; // the transaction
        ReadMultipleRegistersRequest req = null;
        try {
            //ModbusCoupler.getReference().setUnitID(unitId); //통신장비 아이디 set
            SerialParameters params = new SerialParameters();
            params.setPortName(vo.getPortNm()); //통신장비와 연결되어 있는 COM PORT 번호.
            params.setBaudRate(vo.getBaudRt()); //통신속도( 9600 )
            params.setDatabits(vo.getDataLen()); //데이터비트( 기본 8 )
            params.setParity(vo.getParity()); //패러티비트( 기본 0 )
            params.setStopbits(vo.getStopBit()); //정지비트( 기본 1 )
            params.setEncoding(vo.getEncoding()); // RTU - 여기서는 RTU

            // Open the connection
            log.info(tag + "try Open the serial Connection .........");
            con = new SerialConnection(params);
            con.open();
            log.info(tag + "open then serial connection is success...");

            req = new ReadMultipleRegistersRequest(vo.getOffSet(),vo.getDataLen());
            req.setUnitID(vo.getUnitId());
            req.setHeadless();

            trans = new ModbusSerialTransaction(con);
            trans.setRequest(req);

            /* use thread...
            SerialReader sr = new SerialReader(con, trans,repeat);
            try {
                Thread threadR = new Thread(sr);
                threadR.start();

            } catch(Exception e) {
                log.info(e);
            }
             */

            /*by general...*/
            int idx = 0;
            do {
                trans.execute();
                ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();
                log.info(tag + "res.getWordCount() =" + res.getWordCount());
                for (int n = 0; n < res.getWordCount(); n++) {
                    log.info("SerialReader.Word " + n + "=" + res.getRegisterValue(n));
                }
            }
            while(++idx < vo.getRepeat());
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
            log.info(e);
        }
        return rmap;
    }

    private class SerialReader implements Runnable {
        SerialConnection con = null;
        ModbusSerialTransaction trans = null;
        ReadMultipleRegistersResponse res = null;
        int repeat = 1;
        int idx = 0;
        public SerialReader (SerialConnection con, ModbusSerialTransaction trans ,int repeat) {
            this.con = con;
            this.trans = trans;
            this.repeat = repeat;
        }
        public void run () {
            try {
                //Thread.sleep(1000);
                while (++idx < repeat) {
                    trans.execute();
                    res = (ReadMultipleRegistersResponse) trans.getResponse();
                    for (int n = 0; n < res.getWordCount(); n++) {
                        log.info("SerialReader.Word " + n + "=" + res.getRegisterValue(n));
                    }
                    //여기서 db 작업할 것....
                }
            } catch (Exception e) {
                log.info(e);
            }
        }

    }
}
