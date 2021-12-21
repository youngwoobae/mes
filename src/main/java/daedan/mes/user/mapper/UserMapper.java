package daedan.mes.sysmenu.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface UserMapper {
    List<Map<String, Object>> getUserList(Map<String, Object> paraMap);
    int getUserListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> dropUser(Map<String, Object> paraMap);

    Map<String, Object> getEntryInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserData(Map<String, Object> paraMap);

    Map<String, Object> getUserInfo(Map<String, Object> paraMap);

    Map<String, Object> getCustInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboDeptList(Map<String, Object> paraMap);
    int getComboDeptListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getTabletWorkerList(Map<String, Object> paraMap);
    int getTabletWorkerListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getHstrEvtLogList(Map<String, Object> paraMap);
    int getHstrEvtLogListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getHstrList(Map<String, Object> paraMap);
    int getHstrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getHstrSummaryList(Map<String, Object> paraMap);

    int getHstrSummaryListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getAuthUserList(Map<String, Object> paraMap);
    int getAuthUserListCount(Map<String, Object> paraMap);

    //사용자별 메뉴접근 상세권한
    List<Map<String, Object>> getAuthUserMenuList(Map<String, Object> paraMap);
    int getAuthUserMenuListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getWorkerList(Map<String, Object> paraMap);

    int getWorkerListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getWorkList(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserGroup(Map<String, Object> paraMap);

    int getWorkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserAccLogList(Map<String, Object> paraMap);

    int getUserAccLogListCount(Map<String, Object> paraMap);
}
