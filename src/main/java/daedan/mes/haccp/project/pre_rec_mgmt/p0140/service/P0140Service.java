package daedan.mes.haccp.project.pre_rec_mgmt.p0140.service;

import java.sql.SQLException;
import java.util.Map;

public interface P0140Service {
    Map<String, Object> insert(Map<String, Object> param);

    Map<String, Object> update(Map<String, Object> param);

    Map<String, Object> delete(Map<String, Object> param);

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
