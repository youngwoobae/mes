package daedan.mes.haccp.project.haccp_mgmt.c0010.service;

import java.sql.SQLException;
import java.util.Map;

public interface C0010Service {
    Map<String, Object> getCcpHteRead(Map<String, Object> param) throws SQLException;

    Map<String, Object> getCcpHteList(Map<String, Object> param) throws SQLException;

    Map<String, Object> recImprvChk(Map<String, Object> param) throws SQLException;

    Map<String, Object> equipImprvMessage(Map<String, Object> param) throws SQLException;

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
