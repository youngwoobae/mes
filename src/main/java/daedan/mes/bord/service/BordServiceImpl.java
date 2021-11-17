package daedan.mes.bord.service;

import daedan.mes.bord.domain.BordInfo;
import daedan.mes.bord.domain.BordRead;
import daedan.mes.bord.mapper.BordMapper;
import daedan.mes.bord.repository.BordInfoRepository;
import daedan.mes.bord.repository.BordReadRepository;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.user.domain.AccHstrEvnt;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bordService")
public class BordServiceImpl implements BordService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private BordInfoRepository bordInfoRepo;

    @Autowired
    private BordReadRepository brRepository;

    @Autowired
    private BordReadRepository brr;

    @Autowired
    private BordService bordService;

    @Autowired
    private UserService userService;

    @Autowired
    private BordMapper mapper;

    @Autowired
    private FileRepository fileInfoRepo;

    @Transactional
    @Override
    public void saveBord(Map<String, Object> paraMap) {
        String tag = "BordService.saveBord => ";
        BordInfo bivo = new BordInfo();
        log.info(tag + "paraMap = " + paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        paraMap.put("userId", paraMap.get("userId"));
        paraMap.put("ipaddr", paraMap.get("ipaddr"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            bivo.setBordNo(Long.parseLong(paraMap.get("bordNo").toString())); // 게시번호
        }
        catch (NullPointerException ne) {
            bivo.setBordNo(0L); // 게시
        }
        try {
            bivo.setParBordNo(Long.valueOf(paraMap.get("parBbordNo").toString())); // 모 게시번호
        } catch (NullPointerException ne) {
            bivo.setParBordNo(0L); // 게시
        }
        bivo.setBordCont(paraMap.get("bordCont").toString()); // 글 내용
        bivo.setBordTp(Long.parseLong(paraMap.get("bordTp").toString())); // 글유형
        bivo.setBordSubj((String) paraMap.get("bordSubj")); // 글 제목
//        bivo.setFileNo(Long.parseLong(paraMap.get("file_no").toString()));
        try {
            bivo.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
        } catch (NullPointerException ne) {
            bivo.setFileNo(0L);
        }
        try {
            bivo.setFrDt(sdf.parse((String) paraMap.get("frDt").toString().substring(0, 10))); // 게시시작일자
            bivo.setToDt(sdf.parse((String) paraMap.get("toDt").toString().substring(0, 10))); // 게시종료일자
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bivo.setUsedYn("Y"); // 사용여부
        BordInfo chkvo = bordInfoRepo.findByCustNoAndBordNo(custNo,bivo.getBordNo());
        if (chkvo != null) {
            bivo.setBordNo(chkvo.getBordNo());
            bivo.setModIp(paraMap.get("ipaddr").toString()); // 수정IP
            bivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            bivo.setModDt(DateUtils.getCurrentDateTime()); // 수정일자
            bivo.setRegDt(chkvo.getRegDt());
            bivo.setRegId(chkvo.getRegId());
            bivo.setRegIp(chkvo.getRegIp());
        }
        else {
            bivo.setBordNo(0L);
            bivo.setRegDt(DateUtils.getCurrentDateTime());
            bivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            bivo.setRegIp(paraMap.get("ipaddr").toString());
        }
        bivo.setCustNo(custNo);
        bordInfoRepo.save(bivo);
    }

    @Transactional
    @Override
    public void deleteBord(Map<String, Object> paraMap) {
        String tag = "BordService.deleteBord => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long bordNo = Long.parseLong(paraMap.get("bordNo").toString());
        BordInfo infovo = bordInfoRepo.findByCustNoAndBordNo(custNo,bordNo);
        if (infovo != null) {
            infovo.setUsedYn("N");
            infovo.setModDt(DateUtils.getCurrentDate());
            infovo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            infovo.setModIp(paraMap.get("ipaddr").toString());
            bordInfoRepo.save(infovo);
        }
    }

    @Override
    public int getBordListCount(Map<String, Object> paraMap) {
        String tag = "BordService.getBordListCount => ";
        return mapper.getBordListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getBordList(Map<String, Object> paraMap) {
        return mapper.getBordList(paraMap);
    }

    @Override
    public Map<String, Object> getBordInfo(Map<String, Object> paraMap, HttpSession session) {
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Long custNo = uvo.getCustInfo().getCustNo();
        Map<String,Object> rmap = new HashMap<String,Object>();
        BordRead vo = new BordRead();
        vo.setBordNo(Long.parseLong(paraMap.get("bordNo").toString()));

        vo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
        vo.setCnfmDt(DateUtils.getCurrentDate());
        BordRead chkvo = brr.findByCustNoAndBordNoAndUserId(custNo,vo.getUserId(),vo.getBordNo());
        if (chkvo == null) {
            vo.setReadNo(0L);
            brr.save(vo);
        }
        Long bordNo = Long.parseLong(paraMap.get("bordNo").toString());
        BordInfo infovo = bordInfoRepo.findByCustNoAndBordNo(custNo,bordNo);
        if (infovo != null) {
            rmap = StringUtil.voToMap(infovo);
            if (infovo.getFileNo() > 0) {
                FileInfo fivo = fileInfoRepo.findByCustNoAndFileNoAndUsedYn(custNo,infovo.getFileNo(), "Y");
                if (fivo != null) {
                    rmap.put("orgFileNm", fivo.getOrgFileNm());
                }
            }
        }
        return rmap;
    }

    @Override
    public Map<String, Object> getFindBoardTerm(Map<String, Object> paraMap) {
        return mapper.getFindBoardTerm(paraMap);
    }
}




