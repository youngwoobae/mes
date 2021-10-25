package daedan.mes.user.service;


import daedan.mes.user.domain.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by vivie on 2017-06-08.
 */

public interface UserService {

    void loadRawMatByExcel() throws Exception;

    UserInfo signin(String mailAddr, String password);

    UserInfo signup(UserInfo UserMast);

    boolean isExist(String mailAddr);

    void validate(String email);
    
    void saveUser(Map<String, Object> map);

    Map<String, Object> getUserInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserList(Map<String, Object> paraMap);
    int getUserListCount(Map<String, Object> paraMap);
    void dropUser(Map<String, Object> paraMap);

    Map<String, Object> getEntryInfo();

    Map<String, Object> getCustInfo(Map<java.lang.String,java.lang.Object> paraMap);

    UserInfo getUserData( String mailAddr, String password, String newPassword);

    List<Map<String, Object>> getComboDeptList(Map<String, Object> paraMap);
    int getComboDeptListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getTabletWorkerList(Map<String, Object> paraMap);
    int getTabletWorkerListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getHstrSummaryList(Map<String, Object> paraMap);
    int getHstrSummaryListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getHstrList(Map<String, Object> paraMap);
    int getHstrListCount(Map<String, Object> paraMap);

    void makeAccHstr(Map<String, Object> paraMap);

    List<Map<String, Object>> getAuthUserList(Map<String, Object> paraMap);
    int getAuthUserListCount(Map<String, Object> paraMap);

    void renewalAuthUser(Map<String, Object> paraMap);

    //사용자별 메뉴접근 상세권한
    List<Map<String, Object>> getAuthUserMenuList(Map<String, Object> paraMap);
    int getAuthUserMenuListCount(Map<String, Object> paraMap);

    void saveAuthUserMenu(Map<String, Object> paraMap);

    void initToken(Map<String, Object> paraMap);

    UserInfo saveToken(Map<String, Object> paraMap);

    void renewalUserData(Map<String, Object> paraMap);
    void setLastAccPath(Map<String, Object> paraMap);

    UserInfo getUserInfByToken(HashMap<String, Object> paraMap);
}
