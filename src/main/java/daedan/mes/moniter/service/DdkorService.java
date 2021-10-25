package daedan.mes.moniter.service;

import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

public interface DdkorService {
    List<Map<String, Object>> getMoniterHstr(Map<String, Object> paraMap);
    int getMoniterHstrCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMoniterHstrH24(Map<String, Object> paraMap);

    List<Map<String,Object>> getOpeerStat(Map<String, Object> paraMap);
}
