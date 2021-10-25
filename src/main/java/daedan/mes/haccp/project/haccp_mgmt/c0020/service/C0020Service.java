package daedan.mes.haccp.project.haccp_mgmt.c0020.service;

import java.sql.SQLException;
import java.util.Map;

public interface C0020Service {
    Map<String, Object> getCcpCleRead(Map<String, Object> param) throws SQLException;

    Map<String, Object> getCcpCleList(Map<String, Object> param) throws SQLException;

    Map<String, Object> recImprvChk(Map<String, Object> param) throws SQLException;

    Map<String, Object> equipImprvMessage(Map<String, Object> param) throws SQLException;

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
