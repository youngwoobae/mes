package daedan.mes.make.service.metal10;

import daedan.mes.make.domain.youjin.MetalLog;
import daedan.mes.make.mapper.youjin.Metal10Mapper;
import daedan.mes.make.repository.MetalLogRepository;
import daedan.mes.spot.repository.SpotInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service("metal10")
public class Metal10ServiceImpl implements Metal10Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private SpotInfoRepository spotInfoRepo;

    @Autowired
    private MetalLogRepository metalLogRepo;

    @Autowired
    private Metal10Mapper mapper;


    @Override
    public Map<String, Object> getCurMetalLog(Map<String, Object> paraMap) {
        String tag = "metal10Service.getCurMetalLog =>";
        log.info(tag = "paraMap = " + paraMap.toString());
        return  mapper.getCurMetalLog(paraMap);
    }


    @Override
    public Map<String, Object> metalDetectOper(Map<String, Object> paraMap) {
        String tag = "metal10Srvice.metalDetectOper = > ";
        log.info(tag + "paraMap = " + paraMap.toString());
        paraMap.put("ccpTp",Long.parseLong(env.getProperty("code.ccp_tp.metal")));
        String scadaApi = mapper.getMetalScadaApi(paraMap);
        Map rsltMap = new HashMap<String, Object>();
        Long custNo =  Long.parseLong(paraMap.get("custNo").toString());
        int inCustNo = custNo.intValue();
        switch (inCustNo) {
            case 10 :
                if (scadaApi.equals("http://118.67.133.123/api/metal/start/9150")) {
                    URL url = null; //시작
                    try {
                        url = new URL(scadaApi);
                        JSONParser parser = new JSONParser();
                        URLConnection conn = url.openConnection();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String line = br.readLine();
                        JSONObject jsonStr = (JSONObject) parser.parse(line);
                        String rslt = (String) jsonStr.get("res");
                        if (rslt.equals("ok")) {
                            rsltMap  = this.fetchAndReadMetalLog(paraMap);
                        }
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
                else if (scadaApi.equals("http://118.67.133.123/api/metal/getea/9150")) { //현재수량요청
                    rsltMap = this.fetchAndReadMetalLog(paraMap);
                }
                else if (scadaApi.equals("http://118.67.133.123/api/metal/stop/9150")) { //종료
                    rsltMap = this.closeJob(paraMap);
                }
                break;
        }
        return rsltMap;
    }
    private Map<String, Object> fetchAndReadMetalLog(Map<String, Object> paraMap) {
        String tag = " Metal10Service.fetchAndReadMetalLog => ";
        log.info(tag + "paraMap = " + paraMap.toString());//kill
        URL url = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        JSONParser parser = new JSONParser();
        Map<String,Object> rmap = new HashMap<String,Object>();
        try {

            url = new URL("http://118.67.133.123/api/metal/getea/9150"); //현재수량요청
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = br.readLine();
            JSONObject jsonStr = (JSONObject) parser.parse(line);
            rmap.put("passQty",jsonStr.get("run_ea"));
            rmap.put("failQty",jsonStr.get("err_ea"));
            rmap.put("gwId",jsonStr.get("gw_id"));
            rmap.put("rsltMsg",jsonStr.get("msg"));
            rmap.put("rsltStat",jsonStr.get("res"));
            MetalLog mlvo = new MetalLog();
            mlvo.setCustNo(custNo);
            mlvo.setUnixHms(System.currentTimeMillis() / 1000);
            mlvo.setWorkEr(Long.parseLong(paraMap.get("workEr").toString()));
            mlvo.setPassQty(Integer.parseInt((String) jsonStr.get("run_ea")));
            mlvo.setErrQty(Integer.parseInt((String) jsonStr.get("err_ea")));
            mlvo.setSpotNo(Long.parseLong(paraMap.get("spotNo").toString()));
            mlvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
            mlvo.setStepNo(Integer.parseInt(paraMap.get("stepNo").toString()));
            mlvo.setRcvMsg((String) jsonStr.get("msg"));

            mlvo.setMetalHstrNo(0L);
            metalLogRepo.save(mlvo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rmap;
    }
    private Map<String, Object> closeJob(Map<String, Object> paraMap) {
        URL url = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        JSONParser parser = new JSONParser();
        Map<String,Object> rmap = new HashMap<String,Object>();
        try {
            url = new URL("http://118.67.133.123/api/metal/stop/9150"); //작업종료
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = br.readLine();
            JSONObject jsonStr = (JSONObject) parser.parse(line);

            rmap.put("passQty",jsonStr.get("run_ea"));
            rmap.put("failQty",jsonStr.get("err_ea"));
            rmap.put("gwId",jsonStr.get("gw_id"));
            rmap.put("rsltMsg",jsonStr.get("msg"));
            rmap.put("rsltStat",jsonStr.get("res"));

            MetalLog mlvo = new MetalLog();
            mlvo.setCustNo(custNo);
            mlvo.setUnixHms(System.currentTimeMillis() / 1000);
            mlvo.setWorkEr(Long.parseLong(paraMap.get("workEr").toString()));
            mlvo.setPassQty(Integer.parseInt((String) jsonStr.get("run_ea")));
            mlvo.setErrQty(Integer.parseInt((String) jsonStr.get("err_ea")));
            mlvo.setSpotNo(Long.parseLong(paraMap.get("spotNo").toString()));
            mlvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
            mlvo.setStepNo(Integer.parseInt(paraMap.get("stepNo").toString()));
            mlvo.setRcvMsg((String) jsonStr.get("msg"));

            mlvo.setMetalHstrNo(0L);
            metalLogRepo.save(mlvo);
            rmap.put("rsltMsg",jsonStr.get("msg"));
            rmap.put("rsltStat",jsonStr.get("res"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rmap;
    }
}
