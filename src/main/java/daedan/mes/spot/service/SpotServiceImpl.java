package daedan.mes.spot.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.spot.domain.SpotInfo;
import daedan.mes.spot.mapper.SpotMapper;
import daedan.mes.spot.repository.SpotEquipRepository;
import daedan.mes.spot.repository.SpotInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


/*설비관리*/
@Service("SpotService")
public class SpotServiceImpl implements SpotService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private SpotInfoRepository spotInfoRepo;

    @Autowired
    private SpotEquipRepository esr;

    @Autowired
    private SpotEquipService spotEquipService;


    @Override
    public List<Map<String, Object>> getSpots(Map<String, Object> paraMap) { //다중선택용-페이징기능 없음.
        String tag = "SpotService.getSpots =>";
        log.info(tag + "params = " + paraMap.toString());
        return spotMapper.getSpots(paraMap);
    }

    @Override
    public List<Map<String,Object>> getSpotList(Map<String, Object> paraMap) {
        String tag = "SpotService.getSpotList =>";
        log.info(tag + "params = " + paraMap.toString());
        return spotMapper.getSpotList(paraMap);
    }

    @Override
    public int getSpotListCount(Map<String, Object> paraMap) {
        return spotMapper.getSpotListCount(paraMap);
    }

    @Transactional
    @Override
    public void dropSpot(Map<String, Object> paraMap) {
        String tag = "SpotService.dropSpot =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SpotInfo vo = spotInfoRepo.findByCustNoAndSpotNoAndUsedYn(custNo,Long.parseLong(paraMap.get("spotNo").toString()),"Y");
        if(vo != null){
            vo.setUsedYn("N");
            vo.setModDt(DateUtils.getCurrentDate());
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setModIp(paraMap.get("ipaddr").toString());
            spotInfoRepo.save(vo);
        }
    }

    @Transactional
    @Override
    public void saveSpotInfo(Map<String, Object> paraMap) {
        String tag = "SpotService.saveSpotInfo =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SpotInfo spotIn = new SpotInfo();

        try {
            spotIn.setSpotNo(Long.parseLong(paraMap.get("spotNo").toString()));
        }
        catch (NullPointerException ne) {
            spotIn.setSpotNo(0L);
            spotIn.setRegDt(DateUtils.getCurrentDate());
            spotIn.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            spotIn.setRegIp(paraMap.get("ipaddr").toString());

        }
        spotIn.setCcpTp(Long.parseLong(paraMap.get("ccpTp").toString()));
        spotIn.setSpotNm((String) paraMap.get("spotNm"));
        spotIn.setSpotRmk((String) paraMap.get("spotRmk"));
        try {
            spotIn.setScadaApiInv(paraMap.get("scadaApiInv").toString());
        }
        catch (NullPointerException ne) {

        }
        try {
            spotIn.setScadaApiIng(paraMap.get("scadaApiIng").toString());
        }
        catch (NullPointerException ne) {

        }
        try {
            spotIn.setScadaApiEnd(paraMap.get("scadaApiEnd").toString());
        }
        catch (NullPointerException ne) {

        }

        spotIn.setModDt(DateUtils.getCurrentDate());
        spotIn.setModId(Long.parseLong(paraMap.get("userId").toString()));
        spotIn.setModIp(paraMap.get("ipaddr").toString());

        try {
            spotIn.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
        }
        catch (NullPointerException ne) {

        }
        log.info(tag + " fileNo = " + spotIn.getFileNo());//kill
        spotIn.setSvcTp(paraMap.get("svcTp").toString());
        spotIn.setCustNo(custNo);
        spotIn.setUsedYn("Y");
        SpotInfo chkvo =  spotInfoRepo.findByCustNoAndSpotNoAndUsedYn(custNo,spotIn.getSpotNo(),"Y");
        if (chkvo != null) {
            spotIn.setSpotNo(chkvo.getSpotNo());
            spotIn.setRegDt(chkvo.getRegDt());
            spotIn.setRegId(chkvo.getRegId());
            spotIn.setRegDt(chkvo.getRegDt());
        }
        else {
            spotIn.setSpotNo(0L);
            spotIn.setRegDt(DateUtils.getCurrentDate());
            spotIn.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            spotIn.setRegIp(paraMap.get("ipaddr").toString());
        }
        chkvo = spotInfoRepo.save(spotIn);

        /*작업장별 설비정보 처리*/
        paraMap.put("spot_no", chkvo.getSpotNo());
        spotEquipService.saveSpotEquip(paraMap);

    }

    @Override
    public List<Map<String, Object>> getSpotEquipList(Map<String, Object> paraMap) {
        String tag = "SpotService.getSpotEquipList =>";
        log.info(tag + "params = " + paraMap.toString());
        return spotMapper.getSpotEquipList(paraMap);
    }

    @Override
    public Map<String, Object> getSpotInfo(Map<String, Object> paraMap) {
        String tag = "SpotService.getSpotInfo =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long spotNo = Long.parseLong(paraMap.get("spotNo").toString());
        return StringUtil.voToMap(spotInfoRepo.findByCustNoAndSpotNoAndUsedYn(custNo,spotNo,"Y"));
    }

    @Override
    public List<Map<String, Object>> getComboSpotEquip(Map<String, Object> paraMap) {
        String tag = "SpotService.getComboSpotEquip =>";
        log.info(tag + "params = " + paraMap.toString());

        return spotMapper.getComboSpotEquip(paraMap);
    }


}
