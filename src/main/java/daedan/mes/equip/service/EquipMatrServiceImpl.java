package daedan.mes.equip.service;

import daedan.mes.common.domain.Result;
import daedan.mes.equip.domain.EquipMatr;
import daedan.mes.equip.domain.EquipMngrHstr;
import daedan.mes.equip.mapper.EquipMatrMapper;
import daedan.mes.equip.repository.EquipMatrRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*설비원부자재관리*/
@Service("equipMatrService")
public class EquipMatrServiceImpl implements EquipMatrService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private EquipMatrRepository emRepository;

    @Autowired
    private EquipMatrMapper emMapper;

    @Override
    public List<Map<String, Object>> getEquipMatrList(Map<String, Object> paraMap) {
        return emMapper.getEquipMatrList(paraMap);
    }

    @Override
    public int getEquipMatrListCount(Map<String, Object> paraMap) {
        return emMapper.getEquipMatrListCount(paraMap);
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void saveEquipMatr(Map<String, Object> paraMap) {
        String ipaddr = (String) paraMap.get("ipaddr");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long madein = 0L;
        Properties properties = new Properties();
        EquipMatr em = new EquipMatr();
        try {
            String resource = "config/mes.properties";
            Reader reader = Resources.getResourceAsReader(resource);
            properties.load(reader);
            try {
               em.setMatrNo(Long.parseLong(paraMap.get("matr_no").toString()));
            } catch (NullPointerException ne) {
                em.setMatrNo(0L);
            }
            try {
                em.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
            } catch (NullPointerException ne) {
                em.setFileNo(0L);
            }
            try {
                em.setPursUnitPrc(Long.parseLong(paraMap.get("purs_unit_prc").toString()));
            } catch (NullPointerException ne) {
                em.setPursUnitPrc(0L);
            }
            try {
                em.setPursUnit(Long.parseLong(paraMap.get("purs_unit").toString()));
            } catch (NullPointerException ne) {
                em.setPursUnit(0L);
            }
            try {
                madein = (Long.parseLong(paraMap.get("madein").toString()));
            } catch (NullPointerException ne) {
                madein = Long.parseLong(properties.getProperty("madein_default_cd"));
            }
            em.setMadein(madein);
            em.setItemCd((String)paraMap.get("itemCd"));
            em.setMatrNm(paraMap.get("matr_nm").toString());
            em.setSafeStkQty(Long.parseLong(paraMap.get("safe_stk_qty").toString()));
            em.setMakeCmpy((paraMap.get("make_cmpy").toString()));
            em.setModlNm(paraMap.get("modl_nm").toString());
            em.setSz(paraMap.get("sz").toString());
            em.setPartNo(Long.parseLong(paraMap.get("part_no").toString()));
            em.setUsedYn("Y");
            em.setRegId(userId);
            em.setRegIp(ipaddr);
            em.setRegDt(em.getRegDt());
            em.setModId(userId);
            em.setModIp(ipaddr);
            em.setModDt(em.getModDt());
            em.setCustNo(custNo);
            emRepository.save(em);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(properties.getProperty("property_file_not_found"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(properties.getProperty("error_msg"));
        }
    }

    @Transactional
    @Override
    public void dropEquipMatr(Map<String, Object> paraMap) {
        String tag = "EquipMatrService.dropEquipMatr => ";
        log.info (tag + "paraMap = " + paraMap);
        emMapper.dropEquipMatr(paraMap);
    }


    @Override
    public EquipMatr getEquipMatr(Long matrNo) {
        return emRepository.findByMatrNo(matrNo);
    }

    @Override
    public List<Map<String, Object>> getEquipBomList(Map<String, Object> paraMap) {
        return emMapper.equipBomList(paraMap);
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public Map<String, Object> equipMatrInfo(Map<String, Object> map) {
        Map<String, Object> result = emMapper.equipMatrInfo(map);

        return result;
    }
}
