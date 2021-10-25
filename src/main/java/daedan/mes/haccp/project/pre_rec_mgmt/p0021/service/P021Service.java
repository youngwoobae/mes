package daedan.mes.haccp.project.pre_rec_mgmt.p0021.service;

import daedan.mes.haccp.common.error_handle.CustomErrorException;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface P021Service {
    Map<String, Object> selectHealthCardList(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectHealthCardInfo(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectUsersList(Map<String, Object> param) throws SQLException;

    Map<String, Object> selectHealthCardNoCnt(Map<String, Object> param) throws SQLException;

    Map<String, Object> insertHealthCard(Map<String, Object> param) throws CustomErrorException, SQLException;

    void mergeRecFile(String hlthCardNo, MultipartFile attach_file, String userId) throws IOException, CustomErrorException, ParseException, SQLException;

    Map<String, Object> updateHealthCard(Map<String, Object> param) throws CustomErrorException, ClientProtocolException, IOException, ParseException, SQLException;

    Map<String, Object> deleteHealthCard(Map<String, Object> param) throws SQLException;
}
