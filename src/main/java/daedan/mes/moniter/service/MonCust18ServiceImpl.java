package daedan.mes.moniter.service;

import daedan.mes.moniter.domain.cust18.HeaterStat;
import daedan.mes.moniter.mapper.Cust18Mapper;
import daedan.mes.moniter.repository.HeaterStatRepository;
import daedan.mes.spot.domain.SpotInfo;
import daedan.mes.spot.repository.SpotInfoRepository;
import net.minidev.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.JSONParser;
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
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service("monCust18Service")
public class MonCust18ServiceImpl implements MonCust18Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private Cust18Mapper mapper;

    @Autowired
    private HeaterStatRepository heaterStatRepo;

    @Autowired
    private SpotInfoRepository spotInfoRepo;

    @Override
    public List<Map<String,Object>> getHeatLogHstr(Map<String, Object> paraMap) {
        String tag = "cust18Service.getHeatLogHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getHeatLogHstr(paraMap);
    }

    @Override
    public void heaterOn(Map<String, Object> paraMap) {
        String tag = "Batch0018.operHeaterYn => ";
        log.info(tag);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long ccpNone = Long.parseLong(env.getProperty("ccp.none"));
        Long operOn = Long.parseLong(env.getProperty("code.sens_tp.oper.on"));
        JSONParser parser = new JSONParser();
        String scadaApi = null;
        String uri = null;
        URL url = null;
        try {
            SpotInfo spotVo = spotInfoRepo.findByCustNoAndCcpTpAndSensTpAndUsedYn(custNo, ccpNone, operOn, "Y");
            scadaApi = spotVo.getScadaApiIng();
            url = new URL(scadaApi);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = br.readLine();
            JSONObject jsonStr = (JSONObject) parser.parse(line);

            log.info(tag + " 찜기운전시작=" + jsonStr.toString());
            //{"res":"ok","msg":""}
            String res = (String) jsonStr.get("msg");
            HeaterStat vo = new HeaterStat();
            vo.setCustNo(custNo);
            vo.setSpotNo(spotVo.getSpotNo());
            vo.setMsg(res);
            vo.setOperYn(res.equals("ok") ? "Y" : "X");
            if (!res.equals("ok")) {
                log.error(tag + "수신데이터 오류");
            }
            String val = jsonStr.get("val").toString();
            if (val.equals("off")) {
                log.info(tag + "장비 비가동중...");
            } else if (val.equals("on")) {
                log.info(tag + "장비 가동중...");
            }
            vo.setUnixHms(System.currentTimeMillis() / 1000);
            vo.setUsedYn("Y");
            heaterStatRepo.save(vo);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void heaterOff(Map<String, Object> paraMap) {
        String tag = "Batch0018.heaterOff => ";
        log.info(tag);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long ccpNone = Long.parseLong(env.getProperty("ccp.none"));
        Long operOff = Long.parseLong(env.getProperty("code.sens_tp.oper.off"));
        JSONParser parser = new JSONParser();
        String scadaApi = null;
        String uri = null;
        URL url = null;
        try {
            SpotInfo spotVo = spotInfoRepo.findByCustNoAndCcpTpAndSensTpAndUsedYn(custNo, ccpNone, operOff, "Y");
            scadaApi = spotVo.getScadaApiIng();
            url = new URL(scadaApi);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = br.readLine();
            JSONObject jsonStr = (JSONObject) parser.parse(line);

            log.info(tag + " 찜기운전종료=" + jsonStr.toString());
            //{"res":"ok","msg":""}
            String res = (String) jsonStr.get("msg");
            HeaterStat vo = new HeaterStat();
            vo.setCustNo(custNo);
            vo.setSpotNo(spotVo.getSpotNo());
            vo.setMsg(res);
            vo.setOperYn(res.equals("ok") ? "N" : "X");
            if (!res.equals("ok")) {
                log.error(tag + "수신데이터 오류");
            }
            String val = jsonStr.get("val").toString();
            if (val.equals("off")) {
                log.info(tag + "장비 비가동중...");
            } else if (val.equals("on")) {
                log.info(tag + "장비 가동중...");
            }
            vo.setUnixHms(System.currentTimeMillis() / 1000);
            vo.setUsedYn("Y");
            heaterStatRepo.save(vo);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}
