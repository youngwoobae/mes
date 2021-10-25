package daedan.mes.haccp.project.haccp_mgmt.c0001.service;

import java.sql.SQLException;
import java.util.Map;

public interface C0001Service {
    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
