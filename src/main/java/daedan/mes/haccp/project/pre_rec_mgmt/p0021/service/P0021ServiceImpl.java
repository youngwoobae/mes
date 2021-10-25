package daedan.mes.haccp.project.pre_rec_mgmt.p0021.service;

import daedan.mes.common.service.util.CommonUtils;
import daedan.mes.common.service.util.JsonUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.haccp.common.error_handle.CustomErrorException;
import daedan.mes.haccp.project.pre_rec_mgmt.p0021.mapper.P0021Mapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0021.service</li>
 * <li>설 명 : P0021Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Service("p021Service")
@Transactional
public class P0021ServiceImpl implements P021Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0021Mapper p0021Mapper;

    /**
     * 보건증목록 검색
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectHealthCardList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> cntInfo = p0021Mapper.gridPagingCnt(param);
        result.put("totalCount", Integer.parseInt(cntInfo.get("cnt").toString()));
        result.put("data", p0021Mapper.gridPaging(param));

        return result;
    }

    /**
     * 보건증 정보 검색
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectHealthCardInfo(Map<String, Object> param) throws SQLException {

        return p0021Mapper.selectHealthCardInfo(param);
    }

    /**
     * 유저들가져오기
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectUsersList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> cntInfo = p0021Mapper.selectUserListCnt(param);
        result.put("totalCount", Integer.parseInt(cntInfo.get("cnt").toString()));
        result.put("data", p0021Mapper.selectUserList(param));

        return result;
    }

    /**
     * 보건증 등록여부 카운트
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectHealthCardNoCnt(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = p0021Mapper.selectHealthCardNoCnt(param);
        return result;
    }

    /**
     * 보건증 등록
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws CustomErrorException
     * @throws SQLException
     * @
     */
    @Override
    public Map<String, Object> insertHealthCard(Map<String, Object> param) throws CustomErrorException, SQLException {
        Map<String, Object> lcnsInfo = p0021Mapper.selectLcnsInfoAll(param);
        if (lcnsInfo == null) {
            throw new CustomErrorException("인증원에 전송 전 사업장 정보를 반드시 등록해주세요.");
        }
        p0021Mapper.insertHealthCard(param);
        return param;
    }

    /**
     * 파일 등록하기
     *
     * @param hlthCardNo
     * @param userId
     * @@param        Map<String, Object> param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws IOException
     * @throws CustomErrorException
     * @throws ParseException
     * @throws SQLException
     * @
     */
    @Override
    public void mergeRecFile(String hlthCardNo, MultipartFile attach_file, String userId) throws IOException, CustomErrorException, ParseException, SQLException {

        String docFileName = null;
        String docFileNameBak = null;

        docFileNameBak = attach_file.getOriginalFilename().toLowerCase();
        String ext = StringUtil.getExt(docFileNameBak);

        int size = (int) attach_file.getSize();
        if (size >= 1000000000) {
            throw new CustomErrorException("업로드 하고자 하는 파일의 용량이 1000MB 이상이면 안됩니다.");
        }
        if (/** 압축, 문서 확장자들 */
                !ext.equals("zip") && !ext.equals("doc") && !ext.equals("docx") && !ext.equals("hwp")
                        && !ext.equals("xls") && !ext.equals("xlsx") && !ext.equals("pdf")
                        /** 이미지 확장자들 */
                        && !ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png")
                        && !ext.equals("gif") && !ext.equals("tiff") && !ext.equals("raw") )
        {
            throw new CustomErrorException(ext + "는 올바르지 않은 파일 형식입니다.");
        }

        docFileName = attach_file.getOriginalFilename();

        File sendFile = null;
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> companyInfo = p0021Mapper.selectLcnsInfoAll(param);
        if (companyInfo == null) {
            throw new CustomErrorException("인증원에 전송 전 사업장 정보를 반드시 등록해주세요.");
        }

        String lcnsNo = companyInfo.get("lcnsNo").toString(); // 회사의 인가번호
        String filePath = env.getProperty("FILE.UPLOAD.PATH") + "/" + lcnsNo + "/P0021/";

        param.put("hlthCardNo", hlthCardNo);
        param.put("fileNm", hlthCardNo);
        param.put("orginalFileName", docFileName);
        param.put("userId", userId);
        param.put("filePath", filePath);
        sendFile = CommonUtils.convertToFileFromMultipartWithCustomName(attach_file, filePath, hlthCardNo);

        Map<String, Object> recFileInfo = p0021Mapper.selectInfoFromHlthCard(param);
        if (Integer.parseInt(recFileInfo.get("fileKey").toString()) == -1) {
            p0021Mapper.insertFileInfo(param);
            Map<String, Object> fileInfo = p0021Mapper.getLatestFileKey(param);
            param.put("fileKey", fileInfo.get("fileKey"));
            p0021Mapper.updateFileInfoIntoRec(param);
        } else {
            param.put("fileKey", recFileInfo.get("fileKey"));
            p0021Mapper.updateFileInfo(param);
        }

        log.debug("param : " + param.toString());
        /** 전송 부분 */
        Map<String, Object> totalMap = new HashMap<String, Object>();
        Map<String, Object> requestResult = null;
        String sendToFresh = null;
        if (companyInfo.get("crtfOnlineYn") != null) {
            sendToFresh = companyInfo.get("crtfOnlineYn").toString();
        } else {
            sendToFresh = "N";
        }
        param.putAll(companyInfo);
        List<Map<String, Object>> selectFinalCheckList = p0021Mapper.selectFinalCheckList(param);

        for(Map<String, Object> selectFinalCheck : selectFinalCheckList) {
            lcnsNo = (String) selectFinalCheck.get("LCNS_NO");
            String createDate = (String) selectFinalCheck.get("CREATE_DATE_FOR_FILE");
            String recNm = (String) selectFinalCheck.get("REC_NM");
            String userName = (String) selectFinalCheck.get("USER_NAME");
            String orgFileNm = (String) selectFinalCheck.get("ORG_FILE_NM");
            String fileExt = orgFileNm.substring(orgFileNm.lastIndexOf("."),orgFileNm.length());

            String fileNm = lcnsNo + "_"+ createDate + "_" + recNm + "_" +  userName + fileExt;
            selectFinalCheck.put("FILE_NM", fileNm);
        }

        totalMap.put("P0021", selectFinalCheckList);

        // 나중에 URL 변경 가능할 것임.
        String url = env.getProperty("SMART_HACCP_MANAGEMENT.PATH") + "/shm/api/dp";

        // Map을 JSON 형태로 만든다.
        JSONObject jobj = JsonUtil.getJsonStringFromMap(totalMap);
        String totalMapString = jobj.toJSONString();
        Map<String, String> paramStringMap = new HashMap<String, String>();
        paramStringMap.put("jsonData", totalMapString);

        Map<String, File> paramFileMap = new HashMap<String, File>();
        paramFileMap.put("attached_file", sendFile);

        log.debug("paramStringMap : " + paramStringMap.toString());
        switch (sendToFresh) {
            case "Y":
                if (paramFileMap.get("attached_file") != null) {
                    // 추후에 수정 필요
                    requestResult = CommonUtils.sendHttpPostWithFile(url, paramStringMap, paramFileMap);
                } else {
                    requestResult = CommonUtils.sendHttpPost(url, paramStringMap);
                }
                log.debug("requestResult : " + requestResult.toString());

                int statusCode = Integer.parseInt(requestResult.get("code").toString());
                if (statusCode == 200) {
                    /**
                     * 승인자의 승인 처리. 인증원으로 파라미터 보내기 테스트 할 때 주석 처리 하면서 해도 됨. 테스트 완료 후 주석 해제 필수.
                     */
                    log.debug("before final : " + param.toString());
                }
                break;
            case "N":

                break;
        }
    }

    /**
     * 보건증 수정
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
    @Override
    public Map<String, Object> updateHealthCard(Map<String, Object> param) throws CustomErrorException, ClientProtocolException, IOException, ParseException, SQLException {
        p0021Mapper.updateHealthCard(param);
        if(param.get("noFileUpdate") != null) {
            String lcnsNo = null;
            String noFileUpdate = param.get("noFileUpdate").toString();
            if(noFileUpdate.equals("Y")) {
                Map<String, Object> companyInfo = p0021Mapper.selectLcnsInfoAll(param);
                if (companyInfo == null) {
                    throw new CustomErrorException("인증원에 전송 전 사업장 정보를 반드시 등록해주세요.");
                }
                Map<String, Object> recFileInfo = p0021Mapper.selectInfoFromHlthCard(param);
                Map<String, Object> selectFileInfo = p0021Mapper.selectFileInfo(recFileInfo);
                File sendFile = new File(selectFileInfo.get("filePath").toString() + selectFileInfo.get("fileNm").toString());
                if(sendFile.exists()) {
                    log.debug("can work");
                    Map<String, Object> totalMap = new HashMap<String, Object>();
                    Map<String, Object> requestResult = null;
                    String sendToFresh = null;
                    if (companyInfo.get("crtfOnlineYn") != null) {
                        sendToFresh = companyInfo.get("crtfOnlineYn").toString();
                    } else {
                        sendToFresh = "N";
                    }
                    param.putAll(companyInfo);
                    List<Map<String, Object>> selectFinalCheckList = p0021Mapper.selectFinalCheckList(param);

                    for(Map<String, Object> selectFinalCheck : selectFinalCheckList) {
                        lcnsNo = (String) selectFinalCheck.get("LCNS_NO");
                        String createDate = (String) selectFinalCheck.get("CREATE_DATE_FOR_FILE");
                        String recNm = (String) selectFinalCheck.get("REC_NM");
                        String userName = (String) selectFinalCheck.get("USER_NAME");
                        String orgFileNm = (String) selectFinalCheck.get("ORG_FILE_NM");
                        String fileExt = orgFileNm.substring(orgFileNm.lastIndexOf("."),orgFileNm.length());

                        String fileNm = lcnsNo + "_"+ createDate + "_" + recNm + "_" +  userName + fileExt;
                        selectFinalCheck.put("FILE_NM", fileNm);
                    }

                    totalMap.put("P0021", selectFinalCheckList);

                    // 나중에 URL 변경 가능할 것임.
                    String url = env.getProperty("SMART_HACCP_MANAGEMENT.PATH") + "/shm/api/dp";

                    // Map을 JSON 형태로 만든다.
                    JSONObject jobj = JsonUtil.getJsonStringFromMap(totalMap);
                    String totalMapString = jobj.toJSONString();
                    Map<String, String> paramStringMap = new HashMap<String, String>();
                    paramStringMap.put("jsonData", totalMapString);

                    Map<String, File> paramFileMap = new HashMap<String, File>();
                    paramFileMap.put("attached_file", sendFile);

                    log.debug("paramStringMap : " + paramStringMap.toString());
                    switch (sendToFresh) {
                        case "Y":
                            if (paramFileMap.get("attached_file") != null) {
                                // 추후에 수정 필요
                                requestResult = CommonUtils.sendHttpPostWithFile(url, paramStringMap, paramFileMap);
                            } else {
                                requestResult = CommonUtils.sendHttpPost(url, paramStringMap);
                            }
                            log.debug("requestResult : " + requestResult.toString());

                            int statusCode = Integer.parseInt(requestResult.get("code").toString());
                            if (statusCode == 200) {
                                /**
                                 * 승인자의 승인 처리. 인증원으로 파라미터 보내기 테스트 할 때 주석 처리 하면서 해도 됨. 테스트 완료 후 주석 해제 필수.
                                 */
                                log.debug("before final : " + param.toString());
                            }
                            break;
                        case "N":

                            break;
                    }
                }
            } else {
                throw new CustomErrorException("파일이 존재하지 않아 인증원으로 보낼수가 없습니다.");
            }
        }

        return param;
    }

    /**
     * 보건증 삭제
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> deleteHealthCard(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        p0021Mapper.deleteHealthCard(param);
        return result;
    }
}
