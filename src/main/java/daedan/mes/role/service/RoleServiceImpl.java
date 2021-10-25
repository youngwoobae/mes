package daedan.mes.role.service;

import daedan.mes.role.domain.RoleInfo;
import daedan.mes.role.mapper.RoleMapper;
import daedan.mes.role.repository.RoleInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private RoleMapper mapper;

    @Autowired
    private RoleInfoRepository rir;

    @Override
    public List<Map<String, Object>> getRoleMenuList(Map<String, Object> map) {
        return mapper.getRoleMenuList(map);
    }

    @Override
    public int getRoleMenuListCount(HashMap<String, Object> paraMap) {
        return mapper.getRoleMenuListCount(paraMap);
    }

    @Override
    public Object getRoleMenuInfo(Long sysMenuNo) {
        return mapper.getRoleMenuInfo(sysMenuNo);
    }

    @Transactional
    @Override
    public void saveRoleInfo(Map<String, Object> paraMap) {
        String tag = "roleService.saveRoleInfo ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Map<String, Object> map = (Map<String, Object>) paraMap.get("roleInfo");
        log.info(tag + "map = " + map.toString());

        RoleInfo ri = new RoleInfo();
        log.info("(확인장소)RoleNo = " + map.get("role_no"));

        //        baseRoleCd : 210,  //기본역할코드
        //        roleCdAdmin: 211, //시스템관리역할
        //        roleCdSale : 212,  //영업관리역할
        //        roleCdMake : 213, //생산관리역할
        //        roleCdSupt : 214, //업무지원역할


        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long roleCdAdmin = Long.valueOf(env.getProperty("roleCdAdmin"));
        paraMap.put("roleCdAdmin", roleCdAdmin);
        Long roleCdSale = Long.valueOf(env.getProperty("roleCdSale"));
        paraMap.put("roleCdSale", roleCdSale);
        Long roleCdMake = Long.valueOf(env.getProperty("roleCdMake"));
        paraMap.put("roleCdMake", roleCdMake);
        Long roleCdSupt = Long.valueOf(env.getProperty("roleCdSupt"));
        paraMap.put("roleCdSupt", roleCdSupt);
        /*
        roleCdAdmin = (Long) paraMap.get("roleCdAdmin"); // 211
        roleCdSale = (Long) paraMap.get("roleCdSale");   // 212
        roleCdMake = (Long) paraMap.get("roleCdMake");   // 213
        roleCdSupt = (Long) paraMap.get("roleCdSupt");   // 214
        */

        Long adminYn = Long.parseLong(map.get("admin_yn").toString());
        Long saleYn = Long.parseLong(map.get("sale_yn").toString());
        Long makeYn = Long.parseLong(map.get("make_yn").toString());
        Long suptYn = Long.parseLong(map.get("supt_yn").toString());

        try {
            ri.setRoleNo(Long.parseLong(map.get("role_no").toString()));
        } catch (NullPointerException ne) {
            ri.setRoleNo(0L);
            ri.setRegDt(ri.getRegDt());
            ri.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            ri.setRegIp(paraMap.get("ipaddr").toString());
        }

        ri.setSysMenuNo(Long.parseLong(map.get("sys_menu_no").toString()));
        ri.setModDt(ri.getModDt());
        ri.setModId(Long.parseLong(paraMap.get("userId").toString()));
        ri.setModIp(paraMap.get("ipaddr").toString());
        ri.setUsedYn("Y");

        // 클라에서 admin_yn 보낸것을 role_code 컬럼에 저장 -- DB에서 해당 role_code와 sys_menu_no 일치여부 확인
        // insert into role_info select role_code from role_info where sys_menu_no = #{sysMenuNo} and role_code = #{roleCode}
        ri.setRoleCode(Long.parseLong(map.get("admin_yn").toString()));
        if (ri.getRoleCode() == roleCdAdmin) {
            RoleInfo chkvo = rir.findByCustNoAndSysMenuNoAndRoleCodeAndUsedYn(custNo,ri.getSysMenuNo(), ri.getRoleCode(),"Y");
            if (chkvo != null) {
                ri.setRoleNo(chkvo.getRoleNo());
            }
            rir.save(ri);
        }

        ri.setRoleCode(Long.parseLong(map.get("sale_yn").toString()));
        if (ri.getRoleCode() == roleCdSale) {
            RoleInfo chkvo = rir.findByCustNoAndSysMenuNoAndRoleCodeAndUsedYn(custNo,ri.getSysMenuNo(), ri.getRoleCode(),"Y");
            if (chkvo != null) {
                ri.setRoleNo(chkvo.getRoleNo());
            }
            rir.save(ri);
        }

        ri.setRoleCode(Long.parseLong(map.get("make_yn").toString()));
        if (ri.getRoleCode() == roleCdMake) {
            RoleInfo chkvo = rir.findByCustNoAndSysMenuNoAndRoleCodeAndUsedYn(custNo,ri.getSysMenuNo(), ri.getRoleCode(),"Y");
            if (chkvo != null) {
                ri.setRoleNo(chkvo.getRoleNo());
            }
            rir.save(ri);
        }

        ri.setRoleCode(Long.parseLong(map.get("supt_yn").toString()));
        if (ri.getRoleCode() == roleCdSupt) {
            RoleInfo chkvo = rir.findByCustNoAndSysMenuNoAndRoleCodeAndUsedYn(custNo,ri.getSysMenuNo(), ri.getRoleCode(),"Y");
            if (chkvo != null) {
                ri.setRoleNo(chkvo.getRoleNo());
            }
            rir.save(ri);
        }
        log.info("RoleInfo ri ==== " + ri);
        rir.save(ri);
    }
}



