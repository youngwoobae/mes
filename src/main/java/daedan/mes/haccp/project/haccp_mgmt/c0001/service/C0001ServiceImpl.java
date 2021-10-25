package daedan.mes.haccp.project.haccp_mgmt.c0001.service;

import daedan.mes.haccp.project.haccp_mgmt.c0001.mapper.C0001Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class C0001ServiceImpl implements  C0001Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    C0001Mapper c0001Mapper;

    /**
     * 인증원으로 보낼 데이터들을 Map에 담음
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        Map<String, Object> lcnsInfo = (Map<String, Object>) param.get("lcnsInfo");
        @SuppressWarnings("unchecked")
        Map<String, Object> recMainInfo = (Map<String, Object>) param.get("recMainInfo");

        param.put("lcnsNo", lcnsInfo.get("lcnsNo"));
        param.put("companyNm", lcnsInfo.get("companyNm"));
        param.put("lCodeNm", recMainInfo.get("lCodeNm"));

        Map<String, Object> selectFinalCheckMap = c0001Mapper.selectFinalCheckList(param);
        String lcnsNo = (String) selectFinalCheckMap.get("LCNS_NO");
        String createDate = (String) selectFinalCheckMap.get("CREATE_DATE_FOR_FILE");
        String recNm = (String) selectFinalCheckMap.get("REC_NM");
        String orgFileNm = (String) selectFinalCheckMap.get("FILE_NM");
        String fileExt = orgFileNm.substring(orgFileNm.lastIndexOf("."),orgFileNm.length());

        String fileNm = lcnsNo + "_"+ createDate + "_" + recNm + fileExt;
        selectFinalCheckMap.put("FILE_NM", fileNm);
        log.debug("selectFinalCheckMap : " + selectFinalCheckMap.toString());

        result.put(param.get("lCode").toString(), selectFinalCheckMap);

        return result;
    }
}
