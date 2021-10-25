package daedan.mes.modbus.udp.service;

import com.ghgande.j2mod.modbus.io.ModbusUDPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import com.ghgande.j2mod.modbus.net.UDPMasterConnection;
import daedan.mes.modbus.udp.domain.UdpInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service("udpService")
public class UdpServiceImpl implements  UdpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Override
    public ReadInputDiscretesResponse[] getModbusUdp(UdpInfo vo) {
        /* The important instances of the classes mentioned before */
        UDPMasterConnection con = null; //the connection
        ModbusUDPTransaction trans = null; //the transaction
        ReadInputDiscretesRequest req = null; //the request
        ReadInputDiscretesResponse[] res = null; //the response
        /* Variables for storing the parameters */

        InetAddress addr = null; //the slave's address
        try {
            //1. Setup the parameters
            addr = InetAddress.getByName(vo.getInetAddr());
            res = new ReadInputDiscretesResponse[vo.getRepeat()];

            //2. Open the connection
            con = new UDPMasterConnection(addr);
            con.setPort(vo.getDevicePort());
            con.connect();

            //3. Prepare the request
            req = new ReadInputDiscretesRequest(vo.getOffSet(), vo.getCount());

            //4. Prepare the transaction
            trans = new ModbusUDPTransaction(con);
            trans.setRequest(req);

            //5. Execute the transaction repeat times
            for (int idx = 0; idx < vo.getRepeat(); idx++) {
                trans.execute();
                res[idx] = (ReadInputDiscretesResponse) trans.getResponse();
                log.info("Digital Inputs Status[ " + idx + "]=" + res[idx].getDiscretes().toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        finally {
            if (con != null) {
                //6. Close the connection
                con.close();
            }
            return res;
        }
    }
}
