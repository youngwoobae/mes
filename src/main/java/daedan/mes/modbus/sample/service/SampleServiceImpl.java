package daedan.mes.modbus.sample.service;

import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.domain.EquipMngrHstr;
import daedan.mes.equip.repository.EquipMngrHstrRepository;
import daedan.mes.modbus.sample.domain.SampleHumygData;
import daedan.mes.modbus.sample.domain.SampleHumypData;
import daedan.mes.modbus.sample.domain.SampleTempData;
import daedan.mes.modbus.sample.mapper.SampleDataMapper;
import daedan.mes.modbus.sample.repository.SampleHumygDataRepository;
import daedan.mes.modbus.sample.repository.SampleHumypDataRepository;
import daedan.mes.modbus.sample.repository.SampleTempDataRepository;
import daedan.mes.spot.service.SpotEquipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service("sampleService")
public class SampleServiceImpl implements SampleService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private SampleDataMapper mapper;

    @Autowired
    private SampleTempDataRepository sampleTempDataRepos;

    @Autowired
    private SampleHumygDataRepository sampleHumygDataRepo;

    @Autowired
    private SampleHumypDataRepository sampleHumypDataRepo;

    @Autowired
    private SpotEquipService spotEquipService;

    @Autowired
    private EquipMngrHstrRepository equipMangrHistRepo;

    @Override
    public Map<String, Object> getSampleTempData(Map<String, Object> paraMap) {
        String tag = "sampleService.getSampleTempData ==> ";
        log.info(tag + " paraMap = " + paraMap.toString());

        SampleTempData samplevo = new SampleTempData();
        long key = (long) (Math.random() * 100) / 2  ;    //fValue = 0.6564628951664216 , 0.291998526814751 , 0.5208022473820823

        samplevo.setKeyNo(key);
        samplevo = sampleTempDataRepos.findByKeyNo(samplevo.getKeyNo());

        EquipMngrHstr emhvo = new EquipMngrHstr();
        emhvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));


        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cnvTime = sdf.format(new Date(System.currentTimeMillis()));
        StringBuffer buf = new StringBuffer();
        buf.append(paraMap.get("procDt").toString()).append(cnvTime.substring(10));
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        try {
            Date procDt = sdf.parse(buf.toString());
            Long unixHms = procDt.getTime() / 1000;
            emhvo.setUnixHms(unixHms);

            Map<String, Object> semap = spotEquipService.getSpotEquipInfo(paraMap);
            if (semap == null) {
                emhvo.setMaxLmtVal(0f);
                emhvo.setMinLmtVal(0f);
            } else {
                emhvo.setMaxLmtVal(Float.parseFloat(semap.get("max_lmt_val").toString()));
                emhvo.setMinLmtVal(Float.parseFloat(semap.get("min_lmt_val").toString()));
            }
            emhvo.setMeasVal(samplevo.getSamVal());
            emhvo.setAlarmYn("N");
            emhvo.setFeYn("N");
            emhvo.setSusYn("N");
            emhvo.setUsedYn("Y");
            try {
                EquipMngrHstr chkvo = equipMangrHistRepo.findBySpotEquipNoAndUnixHms(emhvo.getSpotEquipNo(), emhvo.getUnixHms());
                if (chkvo != null) {
                    emhvo.setMngrHstrNo(chkvo.getMngrHstrNo());
                } else {
                    emhvo.setMngrHstrNo(0L);
                }
                emhvo = equipMangrHistRepo.save(emhvo);
            }
            catch (IncorrectResultSizeDataAccessException ie) {

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return StringUtil.voToMap(emhvo);
    }

    @Override
    public Map<String, Object> getSampleHumygData(Map<String, Object> paraMap) {
        String tag = "sampleService.getSampleHumygData ==> ";
        SampleHumygData samplevo = new SampleHumygData();
        long key = (long) (Math.random() * 100) / 2;    //fValue = 0.6564628951664216 , 0.291998526814751 , 0.5208022473820823
        samplevo.setKeyNo(key);
        samplevo = sampleHumygDataRepo.findByKeyNo(samplevo.getKeyNo());

        EquipMngrHstr emhvo = new EquipMngrHstr();
        //emhvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        emhvo.setSpotEquipNo(7822L);
        paraMap.put("spotEquipNo", 7822);

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cnvTime = sdf.format(new Date(System.currentTimeMillis()));
        StringBuffer buf = new StringBuffer();
        buf.append(paraMap.get("procDt").toString()).append(cnvTime.substring(10));
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        try {
            Date procDt = sdf.parse(buf.toString());
            Long unixHms = procDt.getTime() / 1000;
            emhvo.setUnixHms(unixHms);

            Map<String, Object> semap = spotEquipService.getSpotEquipInfo(paraMap);
            if (semap == null) {
                emhvo.setMaxLmtVal(0f);
                emhvo.setMinLmtVal(0f);
            } else {
                emhvo.setMaxLmtVal(Float.parseFloat(semap.get("max_lmt_val").toString()));
                emhvo.setMinLmtVal(Float.parseFloat(semap.get("min_lmt_val").toString()));
            }
            emhvo.setMeasVal(samplevo.getSamVal());
            emhvo.setUsedYn("Y");
            try {
                EquipMngrHstr chkvo = equipMangrHistRepo.findBySpotEquipNoAndUnixHms(emhvo.getSpotEquipNo(), emhvo.getUnixHms());
                if (chkvo != null) {
                    emhvo.setMngrHstrNo(chkvo.getMngrHstrNo());
                } else {
                    emhvo.setMngrHstrNo(0L);
                }
                emhvo = equipMangrHistRepo.save(emhvo);
            }
            catch (IncorrectResultSizeDataAccessException ie) {
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return StringUtil.voToMap(emhvo);
    }
    @Override
    public Map<String, Object> getSampleHumypData(Map<String, Object> paraMap) {
        String tag = "sampleService.getSampleHumypData ==> ";
        SampleHumypData samplevo = new SampleHumypData();
        long key = (long) (Math.random() * 100) / 2;    //fValue = 0.6564628951664216 , 0.291998526814751 , 0.5208022473820823
        samplevo.setKeyNo(key);
        samplevo = sampleHumypDataRepo.findByKeyNo(samplevo.getKeyNo());

        EquipMngrHstr emhvo = new EquipMngrHstr();
        //emhvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        emhvo.setSpotEquipNo(7823L);
        paraMap.put("spotEquipNo", 7823);

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cnvTime = sdf.format(new Date(System.currentTimeMillis()));
        StringBuffer buf = new StringBuffer();
        buf.append(paraMap.get("procDt").toString()).append(cnvTime.substring(10));
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        try {
            Date procDt = sdf.parse(buf.toString());
            Long unixHms = procDt.getTime() / 1000;
            emhvo.setUnixHms(unixHms);

            Map<String, Object> semap = spotEquipService.getSpotEquipInfo(paraMap);
            if (semap == null) {
                emhvo.setMaxLmtVal(0f);
                emhvo.setMinLmtVal(0f);
            } else {
                emhvo.setMaxLmtVal(Float.parseFloat(semap.get("max_lmt_val").toString()));
                emhvo.setMinLmtVal(Float.parseFloat(semap.get("min_lmt_val").toString()));
            }
            emhvo.setMeasVal(samplevo.getSamVal());
            emhvo.setUsedYn("Y");
            try {
                //EquipMngrHstr chkvo = equipMangrHistRepo.findBySpotEquipNoAndUnixHms(emhvo.getSpotEquipNo(), emhvo.getUnixHms());
                EquipMngrHstr chkvo = equipMangrHistRepo.findBySpotEquipNoAndUnixHms(7823L, emhvo.getUnixHms());
                if (chkvo != null) {
                    emhvo.setMngrHstrNo(chkvo.getMngrHstrNo());
                } else {
                    emhvo.setMngrHstrNo(0L);
                }
                emhvo = equipMangrHistRepo.save(emhvo);
            }
            catch (IncorrectResultSizeDataAccessException ie) {
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return StringUtil.voToMap(emhvo);
    }
}
