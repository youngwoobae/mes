package daedan.mes.haccp.project.pre_rec_mgmt.p0060.service;

import java.sql.SQLException;
import java.util.Map;

public interface P0060Service {
    Map<String, Object> selectDefaultCheckInfo(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectInfo(Map<String, Object> param) throws SQLException;

    Map<String, Object> insert(Map<String, Object> param) throws SQLException;

    Map<String, Object> update(Map<String, Object> param) throws SQLException;

    Map<String, Object> delete(Map<String, Object> param) throws SQLException;

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
