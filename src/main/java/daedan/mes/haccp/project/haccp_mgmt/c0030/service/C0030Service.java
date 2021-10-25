package daedan.mes.haccp.project.haccp_mgmt.c0030.service;

import java.sql.SQLException;
import java.util.Map;

public interface C0030Service {
    Map<String, Object> getCcpDteList(Map<String, Object> param) throws SQLException;

    Map<String, Object> getMdTestProdList(Map<String, Object> param) throws SQLException;

    void insertMdProdImprv(Map<String, Object> param) throws SQLException;

    void updateRecDataCnt(Map<String, Object> param) throws SQLException;

    Map<String, Object> recImprvChk(Map<String, Object> param) throws SQLException;

    Map<String, Object> equipImprvMessage(Map<String, Object> param) throws SQLException;

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
