package daedan.mes.common.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.*;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.config</li>
 * <li>설 명 : JsonUtil.java</li>
 * <li>작성일 : 2020. 6. 18.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
//https://zzznara2.tistory.com/687
public class JsonUtil {

    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    public static JSONObject getJsonStringFromMap(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map);

        return jsonObject;
    }

    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     */
    public static JSONArray getJsonArrayFromList(List<Map<String, Object>> list) {
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : list) {
            jsonArray.add(getJsonStringFromMap(map));
        }

        return jsonArray;
    }

    /**
     * List<Map>을 jsonString으로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return String.
     */
    public static String getJsonStringFromList(List<Map<String, Object>> list) {
        JSONArray jsonArray = getJsonArrayFromList(list);
        return jsonArray.toJSONString();
    }

    /**
     * JsonObject를 Map<String, String>으로 변환한다.
     *
     * @param jsonObj JSONObject.
     * @return Map<String, Object>
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj) throws JsonMappingException, JsonProcessingException {
        Map<String, Object> map = null;
        map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class);

        return map;
    }

    /**
     * JsonArray를 List<Map<String, String>>으로 변환한다.
     *
     * @param jsonArray JSONArray.
     * @return List<Map<String, Object>>
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    public static List<Map<String, Object>> getListMapFromJsonArray(JSONArray jsonArray) throws JsonMappingException, JsonProcessingException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if (jsonArray != null) {
            int jsonSize = jsonArray.size();
            for (int i = 0; i < jsonSize; i++) {
                Map<String, Object> map = JsonUtil.getMapFromJsonObject((JSONObject) jsonArray.get(i));
                list.add(map);
            }
        }

        return list;
    }

    /**
     * JSON 형태의 String을 Map<String, Object> 로 바꾼다.
     *
     * @param String jsonString.
     * @return Map<String, Object>
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    public static Map<String, Object> getMapFromJsonString(String jsonString) throws ParseException, JsonMappingException, JsonProcessingException {
        Object obj = null;
        obj = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(jsonString);
        JSONObject jsonObj = (JSONObject) obj;
        Map<String, Object> requestResult = JsonUtil.getMapFromJsonObject(jsonObj);

        return requestResult;
    }
    
    /**
     * DevExtreme에서 받아온 데이터를 Map 형태로 변환한다.
     *
     * @param String jsonString.
     * @return Map<String, Object>
     */
    public static Map<String, Object> getMapFromJsonString2(String jsonString) throws ParseException {
        Object obj = null;
        Map<String, Object> requestResult = new HashMap<String, Object>();
        obj = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(jsonString);
        JSONArray jsonArr = (JSONArray) obj;
        String key = null;
        String value = null;

        for (int i = 0; i < jsonArr.size(); i++) {
            String jsonStr = jsonArr.get(i).toString();
            Object obj2 = null;
            obj2 = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(jsonStr);
            JSONObject jsonObj = (JSONObject) obj2;
            Set<String> keySet = jsonObj.keySet();
            Iterator<String> it = keySet.iterator();
            while(it.hasNext()) {
                key = it.next();
                value = jsonObj.get(key).toString();
                requestResult.put(key, value);
            }
        }

        return requestResult;
    }
}
