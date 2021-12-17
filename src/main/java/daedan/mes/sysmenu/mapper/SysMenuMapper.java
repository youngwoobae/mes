package daedan.mes.sysmenu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SysMenuMapper {
    List<Map<String,Object>> getSysMenuList(Map<String,Object> map);
    int getSysMenuListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getActMeuList(Map<String,Object> map);
    List<Map<String, Object>> getHotMenuList(Map<String,Object> map);

    List<Map<String, Object>> getsaveSysMenuInfo(Map<String, Object> map);

    int getHotMenuCounter(Map<String, Object> map);

    void dropHotMenu(Map<String, Object> map);
    List<Map<String, Object>> getSysMenuTree(Map<String, Object> paraMap);
    List<Map<String, Object>> getAuthSysMenuTree(HashMap<String, Object> paraMap);
    int getSysMenuTreeDepth(Map<String, Object> paraMap);

    List<Map<String, Object>> getMenuList(Map<String, Object> paraMap);
    int getMenuListCount(Map<String, Object> paraMap);

    void appendSysMenu(Map<String, Object> paraMap);

    List<Map<String, Object>> getParSysMenus(Map<String, Object> map);

    List<Map<String, Object>> getAuthMenuList(Map<String, Object> paraMap);

    String getMenuPosList(Map<String, Object> paraMap);

    List<Map<String, Object>> routerList(Map<String, Object> paraMap);


    List<Map<String, Object>> getCircleMenuList(Map<String, Object> paraMap);
    int getCircleMenuListCount(Map<String, Object> paraMap);

    void getMenuNumberList(Map<String, Object> paraMap);

    List<Map<String, Object>> getCustMenuList(Map<String, Object> paraMap);

    List<Map<String, Object>> getCustMenu();
}
