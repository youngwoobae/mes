package daedan.mes.haccp.project.admin.comMng.service;


import daedan.mes.common.service.util.CommonUtils;
import daedan.mes.common.service.util.JsonUtil;
import daedan.mes.haccp.common.error_handle.CustomErrorException;
import daedan.mes.haccp.project.admin.comMng.mapper.ComMngMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComMngServiceImpl implements ComMngService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    ComMngMapper comMngMapper;


    /**
     * 공통코드 리스트
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     */
    public List<Map<String, Object>> selectCodeList(Map<String, Object> param) throws SQLException {
        return comMngMapper.selectCodeList(param);
    }

    /**
     * 사업장 정보 가져오기
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> companyCnt(Map<String, Object> param)throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", comMngMapper.companyCnt(param));
        return result;
    }

    /**
     * 사업장 정보 가져오기
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public Map<String, Object> selectCompanyInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", comMngMapper.selectCompanyInfo(param));
        return result;
    }


    /**
     * 사업장 등록
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     * @throws Exception
     */
    public Map<String, Object> insertCompany(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        comMngMapper.insertCompany(param);
        return result;
    }


    /**
     * 사업장 수정
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws Exception
     */
    public Map<String, Object> updateCompany(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("updateCnt", comMngMapper.updateCompany(param));
        result.put("userId", param.get("userId"));
        return result;
    }

    /**
     * 품목유형 중
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public List<Map<String, Object>> selectItmtypM(Map<String, Object> param) throws SQLException {
        return comMngMapper.selectItmtypM(param);
    }

    /**
     * 품목유형 소
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     */
    public List<Map<String, Object>> selectItmtypS(Map<String, Object> param)throws SQLException {
        return comMngMapper.selectItmtypS(param);
    }

    /**
     * 품목유형 리스트
     *
     * @param  lcnsNo 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> selectCompanyItemList(String lcnsNo) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", comMngMapper.selectCompanyItemList(lcnsNo));
        return result;
    }

    /**
     * 품목유형 등록
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws Exception
     */
    public Map<String, Object> insertCompanyItem(Map<String, Object> param)throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> selectItemInfo =  comMngMapper.checkItemInfo(param);
        if(selectItemInfo == null) {
            comMngMapper.insertCompanyItem(param);
        } else {
            comMngMapper.updateCompanyItem(param);
        }

        return result;
    }

    /**
     * 품목유형 상세
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> selectItemInfo(Map<String, Object> param)throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", comMngMapper.selectItemInfo(param));
        return result;
    }

    /**
     * 품목유형 삭제
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> deleteItemInfo(Map<String, Object> param)throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        comMngMapper.deleteItemInfo(param);
        return result;
    }

    /**
     * 인증원으로부터 데이터 받고 처리한다.
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws CustomErrorException
     * @throws ParseException
     * @throws IOException
     * @throws ClientProtocolException
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> infoFromFresh(Map<String, Object> param) throws ClientProtocolException, IOException, ParseException, CustomErrorException, SQLException {
        Map<String, Object> requestResult = null;
        Map<String, Object> totalMap = new HashMap<String, Object>();
        Map<String, Object> prevLcnsMap = new HashMap<String, Object>();
        Map<String, Object> resultLcnsMap = new HashMap<String, Object>();
        prevLcnsMap = comMngMapper.selectCompanyInfo(param);
        String lcnsNo = param.get("lcnsNo").toString();
        param.put("LCNS_NO", lcnsNo);
        totalMap.put("COMPANY_INFO", param);

        // 나중에 URL 변경 가능할 것임.
        String url = env.getProperty("SMART_HACCP_MANAGEMENT.PATH") + "/shm/api/company";

        // Map을 JSON 형태로 만든다.
        JSONObject jobj = JsonUtil.getJsonStringFromMap(totalMap);
        String totalMapString = jobj.toJSONString();
        Map<String, String> paramStringMap = new HashMap<String, String>();
        paramStringMap.put("jsonData", totalMapString);

        requestResult = CommonUtils.sendHttpPost(url, paramStringMap);

        log.debug("requestResult : " + requestResult.toString());

        int statusCode = Integer.parseInt(requestResult.get("code").toString());
        if (statusCode == 200) {
            resultLcnsMap = (Map<String, Object>) requestResult.get("info");
            resultLcnsMap.put("userId",param.get("userId"));
            log.debug("resultLcnsMap : " + resultLcnsMap.toString());
            comMngMapper.mergeCompanyInfo(resultLcnsMap);
            if(prevLcnsMap.get("lcnsNo") != null) {
                if(!prevLcnsMap.get("lcnsNo").toString().equals(lcnsNo)) {
                    comMngMapper.deleteOldCompanyInfo(prevLcnsMap.get("lcnsNo").toString());
                    comMngMapper.deleteOldCompanyItemInfo(prevLcnsMap.get("lcnsNo").toString());
                }
            }

        }
        return null;
    }
}
