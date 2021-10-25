package daedan.mes.haccp.project.pre_rec_mgmt.p0180.service;

import java.sql.SQLException;
import java.util.Map;

public abstract class P0180Service {
    public abstract Map<String, Object> insert(Map<String, Object> param);

    public abstract Map<String, Object> update(Map<String, Object> param);

    public abstract Map<String, Object> delete(Map<String, Object> param);

    public abstract Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;
}
