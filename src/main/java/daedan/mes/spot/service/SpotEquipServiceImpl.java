package daedan.mes.spot.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.spot.domain.SpotEquip;
import daedan.mes.spot.domain.SpotInfo;
import daedan.mes.spot.mapper.SpotEquipMapper;
import daedan.mes.spot.repository.SpotEquipRepository;
import daedan.mes.spot.repository.SpotInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("spotEquipService")
public class SpotEquipServiceImpl implements SpotEquipService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private SpotInfoRepository spopInfoRepo;

    @Autowired
    private SpotEquipRepository spopEquipRepo;

    @Autowired
    private SpotEquipMapper mapper;

    @Override
    public List<Map<String, Object>> getSpotEquipList(Map<String, Object> paraMap) {
        String tag = "SpotEquipService.getSpotEquipList =>";
        log.info(tag + "params = " + paraMap.toString());
        return mapper.getSpotEquipList(paraMap);
    }

    @Override
    public int getSpotEquipListCount(Map<String, Object> paraMap) {
        return mapper.getSpotEquipListCount(paraMap);
    }

    @Transactional
    @Override
    public void addSpotEquip(Map<String, Object> paraMap) {
        String tag = "SpotEquipService.addSpotEquip =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SpotEquip esvo = new SpotEquip();
        esvo.setEquipNo(Long.parseLong(paraMap.get("equipNo").toString()));
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("equipSpotList");
        for (Map<String, Object> el : ds) {

            esvo.setSpotNo(Long.parseLong(el.get("spot_no").toString()));
            esvo.setModDt(esvo.getModDt());
            esvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            esvo.setModIp(paraMap.get("ipaddr").toString());
            esvo.setUsedYn("Y");
            SpotEquip chkvo = spopEquipRepo.findByCustNoAndSpotNoAndEquipNoAndUsedYn(custNo,esvo.getSpotNo(),esvo.getEquipNo(), "Y");
            if (chkvo != null) {
                esvo.setSpotEquipNo(chkvo.getSpotEquipNo());
            } else {
                esvo.setSpotEquipNo(0L);
                esvo.setRegDt(esvo.getRegDt());
                esvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                esvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            spopEquipRepo.save(esvo);

        }

    }


    @Override
    @Transactional
    public void saveSpot(Map<String, Object> paraMap) {
        String tag = "SpotEquipService.saveSpot =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SpotInfo spotVo = new SpotInfo();
        try{
            spotVo.setSpotNo(Long.parseLong(paraMap.get("spotNo").toString()));
        }catch (NullPointerException e){
            spotVo.setSpotNo(0L);
        }
        spotVo.setSpotNm(paraMap.get("spotNm").toString());
        spotVo.setSvcTp(paraMap.get("svcTp").toString());
        try {
            spotVo.setScadaApiInv(paraMap.get("scadaApiInv").toString());
        }
        catch (NullPointerException ne) {

        }
        try {
            spotVo.setScadaApiIng(paraMap.get("scadaApiIng").toString());
        }
        catch (NullPointerException ne) {

        }
        try {
            spotVo.setScadaApiEnd(paraMap.get("scadaApiEnd").toString());
        }
        catch (NullPointerException ne) {

        }
        spotVo.setUsedYn("Y");
        spotVo.setModDt(DateUtils.getCurrentDate());
        spotVo.setModIp(paraMap.get("ipaddr").toString());
        spotVo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        SpotInfo  spotchkvo = spopInfoRepo.findByCustNoAndSpotNoAndUsedYn(custNo,spotVo.getSpotNo(), "Y");
        if (spotchkvo != null) {
            spotVo.setSpotNo(spotchkvo.getSpotNo());
            spotVo.setRegDt(spotchkvo.getRegDt());
            spotVo.setRegIp(spotchkvo.getRegIp());
            spotVo.setRegId(spotchkvo.getRegId());
        }
        else {
            spotVo.setSpotNo(0L);
        }
        spopInfoRepo.save(spotVo);

    }

    @Override
    @Transactional
    public void saveSpotEquip(Map<String, Object> paraMap) {
        String tag = "SpotEquipService.saveSpotEquip =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        SpotEquip spotEquipVo = new SpotEquip();
        try{
            spotEquipVo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        }catch (NullPointerException e){
            spotEquipVo.setSpotEquipNo(0L);
        }

        try {
            spotEquipVo.setSpotNo(Long.parseLong(paraMap.get("spotNo").toString()));
        }
        catch (NullPointerException ne) {
            spotEquipVo.setSpotNo(0L);
        }


        try {
            spotEquipVo.setEquipNo(Long.parseLong(paraMap.get("equipNo").toString()));
        }
        catch (NullPointerException ne) {
            spotEquipVo.setEquipNo(0L);
        }

        try {
            spotEquipVo.setMaxLmtVal(Float.parseFloat(paraMap.get("maxLmtVal").toString()));
        }
        catch (NullPointerException ne) {
        }
        try {
            spotEquipVo.setMinLmtVal(Float.parseFloat(paraMap.get("minLmtval").toString()));
        }
        catch (NullPointerException ne) {
        }
        try {
            spotEquipVo.setMaxNorVal(Float.parseFloat(paraMap.get("maxNorVal").toString()));
        }
        catch (NullPointerException ne) {
            spotEquipVo.setMaxNorVal(0f);
        }

        try {
            spotEquipVo.setMinNorVal(Float.parseFloat(paraMap.get("minNorVal").toString()));
        }
        catch (NullPointerException ne) {
            spotEquipVo.setMinNorVal(0f);
        }
        try {
            spotEquipVo.setMeasUnit(Long.parseLong(paraMap.get("measUnit").toString()));
        }
        catch (NullPointerException ne) {
            spotEquipVo.setMeasUnit(0L);
        }
        spotEquipVo.setUsedYn("Y");
        spotEquipVo.setModDt(DateUtils.getCurrentDate());
        spotEquipVo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        spotEquipVo.setModIp(paraMap.get("ipaddr").toString());

        SpotEquip chkvo = spopEquipRepo.findByCustNoAndSpotEquipNoAndUsedYn(custNo,spotEquipVo.getSpotEquipNo(), "Y");
        if (chkvo != null) {
            spotEquipVo.setSpotEquipNo(chkvo.getSpotEquipNo());
            spotEquipVo.setRegDt(chkvo.getRegDt());
        }
        else {
            spotEquipVo.setSpotEquipNo(0L);
            log.info(tag + "SpotEquip For Append = " + spotEquipVo.toString());
        }
        spopEquipRepo.save(spotEquipVo);
    }

    @Override
    @Transactional
    public void dropSpotEquip(Map<String, Object> paraMap) {
        String tag = "SpotEquipService.dropSpotEquip =>";
        log.info(tag + "params = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SpotEquip vo = new SpotEquip();
        vo = spopEquipRepo.findByCustNoAndSpotEquipNoAndUsedYn(custNo,Long.parseLong(paraMap.get("spotEquipNo").toString()),"Y");
        if (vo != null) {
            vo.setUsedYn("N");
            vo.setModDt(DateUtils.getCurrentDate());
            vo.setModIp(paraMap.get("ipaddr").toString());
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            spopEquipRepo.save(vo);
        }
    }

    @Override
    public List<Map<String, Object>> getComboSpotEquip(Map<String, Object> paraMap) {
        return mapper.getComboSpotEquip(paraMap);
    }

    @Override
    public Map<String, Object> getSpotEquipInfo(Map<String, Object> paraMap) {
        Long spotEquipNo = Long.parseLong(paraMap.get("spotEquipNo").toString());
        return mapper.getSpotEquipInfo(paraMap);
       // return StringUtil.voToMap(spopEquipRepo.findBySpotEquipNoAndUsedYn(spotEquipNo,"Y"));
    }
}
