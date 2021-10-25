package daedan.mes.haccp.project.haccp_mgmt.c0002.service;

import java.sql.SQLException;
import java.util.Map;

public interface C0002Service {

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
