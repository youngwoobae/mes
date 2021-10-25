package daedan.mes.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RoleService {
    List<Map<String, Object>> getRoleMenuList (Map<String, Object> map);
    int getRoleMenuListCount(HashMap<String, Object> paraMap);

    Object getRoleMenuInfo(Long sysMenuNo);

    void saveRoleInfo(Map<String, Object> paraMap);

}
