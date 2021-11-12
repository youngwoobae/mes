package daedan.mes.cmmn.service;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CmmnService {
    List<Map<String, Object>> getCalDateList(Map<String, Object> Map);
    Date getIntervalDate(Map<String, Object> paraMap);
    byte[] encryptStr(Long custNo, String plainText);

    String decryptStr(Long custNo, byte[] encStr) throws UnsupportedEncodingException;

    Map<String, Object>  getAuthUserMenuInfo(Map<String, Object> paraMap);

    String getBrowser(HttpServletRequest request);

    String getDisposition(String filename, String browser)
            throws UnsupportedEncodingException;

//    String getBlobToBin(String filename, String browser);

    byte[] getFileToBin(String filePath);


    int getWeekIdx(Map<String, Object> paraMap);

    int getWeekIdxByUnixTime(long iValue);

    Map<String, Object> getMakeHstrPreVal(Map<String, Object> paraMap);

    List<Map<String, Object>>  getComboWh(Map<String, Object> paraMap);
    List<Map<String, Object>>  getComboSpot(Map<String, Object> paraMap);

    JSONObject getRestApiData(Map<String, Object> paraMap);

    JSONArray getRestApiList(Map<String, Object> paraMap);

}
