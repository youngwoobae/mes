package daedan.mes.sysmenu.service;

import daedan.mes.sysmenu.domain.SysMenu;
import daedan.mes.user.domain.AccHstr;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface SysMenuService {
    List<Map<String, Object>> getSysMenuList(Map<String, Object> map);
    int getSysMenuListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getActMenuList(Map<String, Object> map);
    SysMenu getSysMenuToVo(Map<String, Object> map);
    List<Map<String, Object>> saveHotMenu(Map<String, Object> map);


    @Transactional
    void saveSysMenuInfo(Map<String, Object> passMap);

    List<Object> getSysMenuTree(Map<String, Object> paraMap);

    int getMenuListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMenuList(Map<String, Object> paraMap);

    List<Map<String, Object>> getParSysMenus(Map<String, Object> paraMap);


    @Transactional
    void initAuthMenu(Map<String, Object> paraMap);

    void renewalAuthMenu(Map<String, Object> paraMap);


    List<Map<String, Object>> getAuthMenuList(Map<String, Object> paraMap);

    String getMenuPosList(Map<String, Object> paraMap);

    List<Map<String, Object>> routerList(Map<String, Object> paraMap);

    AccHstr makeAccHstr(Map<String, Object> paraMap);

    List<Map<String, Object>> getCircleMenuList(Map<String, Object> paraMap);

    int getCircleMenuListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getCustMenuList(Map<String, Object> paraMap);


    List<Map<String, Object>> getCustMenu(Map<String, Object> paraMap);

    void deleteCustMenu(Map<String, Object> paraMap);
}
