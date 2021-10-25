package daedan.mes.haccp.project.admin.alert_period.service;

import daedan.mes.haccp.common.error_handle.CustomErrorException;
import daedan.mes.haccp.common.error_handle.CustomErrorHandler;
import daedan.mes.haccp.common.generic.GenericService;
import daedan.mes.haccp.project.admin.alert_period.mapper.ApMgmtMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApMgmtService extends GenericService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    ApMgmtMapper apMgmtMapper;
    /**
     * 리스트 검색
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> selectList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> cntInfo = apMgmtMapper.gridPagingCnt(param);
        String totalCount = cntInfo.get("cnt").toString();
        result.put("totalCount", totalCount);
        result.put("data", apMgmtMapper.gridPaging(param));

        return result;
    }

    /**
     * 특정 검색
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> selectInfo(Map<String, Object> param) throws SQLException {
        return apMgmtMapper.getInfo(param);
    }

    /**
     * 등록
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     * @throws Exception
     */
    public Map<String, Object> insertData(Map<String, Object> param) throws CustomErrorException {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> checkMap = apMgmtMapper.checkDuplicate(param);
        int cnt = Integer.parseInt(checkMap.get("cnt").toString());
        if (cnt == 1) {
            throw new CustomErrorException("중복된 관리 항목이 존재합니다. 다른 항목으로 선택해주세요.");
        }
        if (param.get("refCommCode") == null) {
            Map<String, Object> refCommCodeMap = apMgmtMapper.getRefCommCode(param);
            if (refCommCodeMap != null) {
                param.put("refCommCode", refCommCodeMap.get("refCommCode").toString());
            }
        }
        apMgmtMapper.insertData(param);
        return result;
    }

    /**
     * 수정
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     * @throws Exception
     */
    public Map<String, Object> updateData(Map<String, Object> param) throws CustomErrorException {
        Map<String, Object> result = new HashMap<String, Object>();
        logger.debug("param : " + param.toString());
        Map<String, Object> getInfoMap = apMgmtMapper.getInfo(param);
        Map<String, Object> checkMap = apMgmtMapper.checkDuplicate(param);
        int cnt = Integer.parseInt(checkMap.get("cnt").toString());

        String origanlItemCode = "";
        String paramItemCode = "";
        String origanlLcode = getInfoMap.get("lCode").toString();
        String paramLcode = param.get("lCode").toString();

        if (param.get("refCommCode") == null) {
            Map<String, Object> refCommCodeMap = apMgmtMapper.getRefCommCode(param);
            if (refCommCodeMap != null) {
                param.put("refCommCode", refCommCodeMap.get("refCommCode").toString());
            }
        }
        if (cnt == 0) {
            apMgmtMapper.updateData(param);
        } else {
            if (origanlLcode.equals(paramLcode)) {
                if (param.get("itemCode") != null) {
                    origanlItemCode = getInfoMap.get("itemCode").toString();
                    paramItemCode = param.get("itemCode").toString();
                    if (origanlItemCode.equals(paramItemCode)) {
                        apMgmtMapper.updateData(param);
                    } else {
                        throw new CustomErrorException("해당 관리 항목에 중복된 세부 항목이 존재합니다. 다른 항목으로 선택해주세요.");
                    }
                }
            } else {
                throw new CustomErrorException("중복된 관리 항목이 존재합니다. 다른 항목으로 선택해주세요.");
            }
        }

        apMgmtMapper.updateData(param);
        return result;
    }

    /**
     * 삭제
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    public Map<String, Object> deleteData(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        apMgmtMapper.deleteData(param);
        return result;
    }

    /**
     * 관리 항목 선택 대분류 내용 가져오기
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     */
    public List<Map<String, Object>> selectLcodeList(Map<String, Object> param) throws SQLException {
        return apMgmtMapper.selectLcodeList(param);
    }

    /**
     * 관리 항목 선택 세부항목 내용 가져오기
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     */
    public List<Map<String, Object>> selectItemList(Map<String, Object> param) throws SQLException {
        return apMgmtMapper.selectItemList(param);
    }

}
