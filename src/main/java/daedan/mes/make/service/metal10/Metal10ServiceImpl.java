package daedan.mes.make.service.metal10;

import daedan.mes.make.domain.youjin.MetalLog;
import daedan.mes.make.repository.RcvMetalRepository;
import daedan.mes.spot.domain.SpotInfo;
import daedan.mes.spot.repository.SpotInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

@Service("metal10")
public class Metal10ServiceImpl implements Metal10Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private SpotInfoRepository spotInfoRepo;

    @Autowired
    private RcvMetalRepository rcvMetalRepo;
    @Transactional
    @Override
    public void saveMetalLog(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long spotNo = Long.parseLong(paraMap.get("spotNo").toString());
        Long prodNo = 0L;
        try {
            prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        }
        catch (NullPointerException ne) {
            prodNo = 0L;
        }
        Long workEr = Long.parseLong(paraMap.get("workEr").toString());
        Integer stepNo = Integer.parseInt(paraMap.get("operTp").toString());

        JSONParser parser = new JSONParser();
        SpotInfo vo = spotInfoRepo.findByCustNoAndSpotNoAndUsedYn(custNo,spotNo,"Y");
        String scadaApi = vo.getScadaApi();
        URL url = null;
        try {
            url = new URL(scadaApi);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = br.readLine();
            JSONObject jsonStr = (JSONObject) parser.parse(line);
            MetalLog metalvo = new MetalLog();
            metalvo.setCustNo(custNo);
            metalvo.setSpotNo(spotNo);
            metalvo.setStepNo(stepNo); /*작업구분 1 : FE, 2 : SUS , 3 : 제품 , 4 : 제품+FE , 5 : 제품+SUS */
            metalvo.setProdNo(prodNo);
            metalvo.setWorkEr(workEr);
            try {
                metalvo.setRcvMsg(jsonStr.get("msg").toString());
            }
            catch (NullPointerException ne) {

            }
            metalvo.setPassQty(Integer.parseInt(jsonStr.get("run_ea").toString()));
            metalvo.setErrQty(Integer.parseInt(jsonStr.get("err_ea").toString()));
            metalvo.setUnixHms(System.currentTimeMillis() / 1000);
            rcvMetalRepo.save(metalvo);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
