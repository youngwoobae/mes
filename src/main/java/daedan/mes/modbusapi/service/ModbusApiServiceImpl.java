package daedan.mes.modbusapi.service;

import daedan.mes.equip.domain.EquipMngrHstr;
import daedan.mes.equip.repository.EquipMngrHstrRepository;
import daedan.mes.modbusapi.mapper.ModbusApiMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.dialect.LobMergeStrategy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("modbusapiService")
public class ModbusApiServiceImpl implements ModbusApiService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private ModbusApiMapper mapper;


    @Autowired
    EquipMngrHstrRepository equipMngrHstrRepo;

    @Override
    public Map<String, Object> getModbusApiData(Map<String, Object> paraMap) {
        String tag = "vsvc. => ";
        log.info(tag + "paraMap = " + paraMap.toString());

//        Long eqNo = Long.parseLong(paraMap.get("spot_equip_no").toString());

        URL url = null;
        String uri = env.getProperty("modbusapi.uri");
        JSONParser parser = new JSONParser();
        Map<String, Object> rmap = new HashMap<String, Object>();
        try {
            url = new URL(uri);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            org.json.simple.JSONArray jsonArr = (org.json.simple.JSONArray) parser.parse(br.readLine());
            log.info("jsonArr ???"+jsonArr);

            boolean catchData = false;

            for(Object el : jsonArr) {
                Object obj = parser.parse(el.toString());;
                JSONObject jsonObj = (JSONObject) obj;
                int apiEqNo = Integer.parseInt(jsonObj.get("eqno").toString());
                int spotEquipNo = Integer.parseInt(paraMap.get("spotEquipNo").toString());
                rmap.put("eqno", apiEqNo);
                if(apiEqNo == spotEquipNo){
                    switch (apiEqNo) {
                        case 2: //????????? co2?????????
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("temp", jsonObj.get("temp").toString()); //??????????????????(Y/N)
                            rmap.put("co2", Integer.parseInt(jsonObj.get("co2").toString())); //????????????
                            rmap.put("humy", jsonObj.get("hum").toString()); //??????????????????(Y/N)
                            catchData = true;
                            break;

                        case 7:  //????????? ??????1?????? ?????????
                        case 9:  //????????? ??????2?????? ?????????
                        case 11:  //????????? ??????3?????? ?????????
                        case 13:  //????????? ??????4?????? ?????????
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("oper", jsonObj.get("oper").toString()); //??????????????????(Y/N)
                            rmap.put("ea", Integer.parseInt(jsonObj.get("ea").toString())); //????????????
                            rmap.put("alarm", jsonObj.get("alarm").toString()); //??????????????????(Y/N)
                            log.info(tag + "????????????????????????????????? = " + rmap.toString());
                            catchData = true;
                            break;

                        case 8: //????????? ??????????????? 1???
                        case 10: //????????? ??????????????? 2???
                        case 12: //????????? ??????????????? 3???
                        case 14: //????????? ??????????????? 4???
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("oper", jsonObj.get("oper").toString()); //??????????????????(Y/N)
                            rmap.put("alarm", jsonObj.get("alarm").toString()); //??????????????????(Y/N)
                            rmap.put("ok_ea", Integer.parseInt(jsonObj.get("ok_ea").toString())); //????????????
                            rmap.put("add_ea", Integer.parseInt(jsonObj.get("add_ea").toString())); //????????????
                            catchData = true;
                            break;

                        case 15: //????????? solpac
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("oper", jsonObj.get("oper").toString()); //??????????????????(Y/N)
                            rmap.put("ea", Integer.parseInt(jsonObj.get("ea").toString())); //????????????
                            rmap.put("err_ea", Integer.parseInt(jsonObj.get("err_ea").toString())); //????????????
                            rmap.put("alarm", jsonObj.get("alarm").toString()); //??????????????????(Y/N)
                            catchData = true;
                            break;

                        case 16: //????????? leepac
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("oper", jsonObj.get("oper").toString()); //??????????????????(Y/N)
                            rmap.put("ea", Integer.parseInt(jsonObj.get("ea").toString())); //????????????
                            rmap.put("add_ea", Integer.parseInt(jsonObj.get("add_ea").toString())); //????????????
                            rmap.put("alarm", jsonObj.get("alarm").toString()); //??????????????????(Y/N)
                            catchData = true;
                            break;

                        case 17: //????????? ????????????1???
                        case 18: //????????? ????????????2???
//                            log.info(jsonObj+"jsonObjVal");
                            rmap.put("txt", jsonObj.get("txt").toString());
//                            rmap.put("oper", jsonObj.get("oper").toString()); //??????????????????(Y/N)
                            rmap.put("temp", jsonObj.get("temp").toString()); //??????
                            rmap.put("alarm", jsonObj.get("alarm").toString()); //??????????????????
                            rmap.put("prss", Integer.parseInt(jsonObj.get("prss").toString())); //??????
                            catchData = true;
                            break;

                        case 52: //????????? ???????????????
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("temp", jsonObj.get("temp").toString()); //??????
                            catchData = true;
                            break;

                        case 53:    //????????? ??????????????? 1???
                        case 54:    //????????? ??????????????? 2???
                        case 55:    //????????? ??????????????? 3???
                        case 56:    //????????? ??????????????? 4???
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("ea", Integer.parseInt(jsonObj.get("ea").toString())); //????????????
                            catchData = true;
                            break;
                        case 400:
                        case 401:
                        case 402:
                        case 403:
                        case 404:
                        case 405:
                        case 406:
                        case 407:
                        case 408:
                        case 409:
                        case 410:
                            rmap.put("txt", jsonObj.get("txt").toString());
//                            rmap.put("oper", jsonObj.get("oper").toString()); //??????????????????(Y/N)
                            rmap.put("temp", jsonObj.get("temp").toString()); //??????
                            rmap.put("alarm", jsonObj.get("alarm").toString()); //??????????????????
                            rmap.put("hz", jsonObj.get("hz").toString()); //hz
                            catchData = true;
                            break;
                        case 414:
                            rmap.put("txt", jsonObj.get("txt").toString());
                            rmap.put("val", jsonObj.get("val").toString());
                            break;
                    }
                }
                if (catchData) break;
            }
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (org.json.simple.parser.ParseException parseException) {
            parseException.printStackTrace();
        }
        log.info(tag + "result map = " + rmap.toString());
        return rmap;
    }

    @Override
    public List<Map<String, Object>> getModbusApiListData(Map<String, Object> paraMap) {
        String tag = "vsvc.modubsApiService.saveModbusApiData => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        URL url = null;
        String uri = env.getProperty("modbusapi.uri");
        JSONParser parser = new JSONParser();
        List<Map<String,Object>> rList = new ArrayList<>();
        try {
            url = new URL(uri);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            org.json.simple.JSONArray jsonArr = (org.json.simple.JSONArray) parser.parse(br.readLine());
            boolean catchData = false;

            for (Object el : jsonArr) {
                Object obj = parser.parse(el.toString());
                JSONObject jsonObj = (JSONObject) obj;
                int apiEqNo = Integer.parseInt(jsonObj.get("eqno").toString());
//                rmap.put("eqno", apiEqNo); //??????????????????(Y/N)
                switch (apiEqNo) {
                    //????????? 1~10?????? ??????
                    case 400:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 401:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 402:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 403:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 404:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 405:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 406:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 407:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 408:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 409:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 410:
                        rList.add(jsonObj);
                        catchData = true;
                        continue;
                    case 414:
                        rList.add(jsonObj);
                        catchData = true;
                        break;
                }
                if (catchData) break;
            }
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (org.json.simple.parser.ParseException parseException) {
            parseException.printStackTrace();
        }
        return rList;
    }
}
