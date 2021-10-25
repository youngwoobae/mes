package daedan.mes.haccp.project.pre_rec_mgmt.p0030.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface P0030Service {
    Map<String, Object> insert(Map<String, Object> param) throws SQLException;

    Map<String, Object> update(Map<String, Object> param) throws SQLException;

    Map<String, Object> delete(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectLightList(Map<String, Object> param) throws SQLException;

    List<Map<String, Object>> selectColList(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectInfo(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectColInfo(Map<String, Object> param) throws SQLException;

    Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException;

    List<Map<String, Object>> selectPlaceList(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectList(Map<String, Object> param) throws SQLException;
}
