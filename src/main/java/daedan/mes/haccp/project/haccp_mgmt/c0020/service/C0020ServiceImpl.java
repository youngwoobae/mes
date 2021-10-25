package daedan.mes.haccp.project.haccp_mgmt.c0020.service;

import daedan.mes.haccp.project.haccp_mgmt.c0002.service.C0002Service;
import daedan.mes.haccp.project.haccp_mgmt.c0020.mapper.C0020Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class C0020ServiceImpl implements C0020Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    C0020Mapper c0020Mapper;

    /**
     * 이탈 정보 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getCcpCleRead(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", c0020Mapper.getCcpCleRead(param));

        return result;
    }

    /**
     * 냉장, 냉동기 목록 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getCcpCleList(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("data", c0020Mapper.getCcpCleList(param));

        return result;
    }

    /**
     * 설비별 개선조치 미 처리 건수 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> recImprvChk(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("data", c0020Mapper.recImprvChk(param));

        return result;
    }

    /**
     * 설비별 코드 및 미처리 건수 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> equipImprvMessage(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", c0020Mapper.equipImprvMessage(param));

        return result;
    }

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

        List<Map<String, Object>> selectFinalCheckList = c0020Mapper.selectFinalCheckList(param);
        result.put(param.get("lCode").toString(), selectFinalCheckList);

        return result;
    }
}
