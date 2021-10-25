package daedan.mes.haccp.project.pre_rec_mgmt.p0020.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface P0020Service {
    Map<String, Object> selectList(Map<String, Object> param);

    Map<String, Object> selectInfo(Map<String, Object> param);

    Map<String, Object> insert(Map<String, Object> param);

    Map<String, Object> update(Map<String, Object> param);

    Map<String, Object> delete(Map<String, Object> param);

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;

    List<Map<String, Object>> selectCheckList(Map<String, Object> param) throws SQLException;
}
