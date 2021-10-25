package daedan.mes.adm.service;

import net.sf.json.JSONObject;

import java.util.Map;

public interface AdmService {
    JSONObject getMenuList(Map<String, Object> cond);
}
