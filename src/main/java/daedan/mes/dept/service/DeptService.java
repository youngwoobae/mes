package daedan.mes.dept.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DeptService {
    List<Map<String,Object>> getDeptList(Map<String, Object> paraMap);
    List<Map<String,Object>> getComboDeptList(Map<String, Object> paraMap);

    List<Object> deptTree(Map<String, Object> paraMap);
}
