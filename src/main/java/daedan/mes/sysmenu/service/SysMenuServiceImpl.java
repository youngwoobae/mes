package daedan.mes.sysmenu.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.sysmenu.domain.AuthSysMenu;
import daedan.mes.sysmenu.domain.SysMenu;
import daedan.mes.sysmenu.domain.SysMenuHot;
import daedan.mes.sysmenu.mapper.SysMenuMapper;
import daedan.mes.sysmenu.repository.SysMenuAuthRepository;
import daedan.mes.sysmenu.repository.SysMenuHotRepository;
import daedan.mes.sysmenu.repository.SysMenuRepository;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.repository.AccHstrRepository;
import daedan.mes.user.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service("sysmenuService")
public class SysMenuServiceImpl implements SysMenuService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private SysMenuRepository sysmenuRepo;

    @Autowired
    private AccHstrRepository accHstrRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SysMenuHotRepository sysMenuHotRepository;

    @Autowired
    private SysMenuAuthRepository sysMenuAuthRepo;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private SysMenuMapper sysmenuMapper;

    private List<Map<String, Object>> elist = null;
    LinkedHashMap<String, Object> root = new LinkedHashMap<String, Object>();


    @Override
    public List<Map<String, Object>> getSysMenuList(Map<String, Object> paraMap) {
        String tag = "sysMenuService.getSysMenuList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return sysmenuMapper.getSysMenuList(paraMap);
    }

    @Override
    public int getSysMenuListCount(Map<String, Object> paraMap) {
        return sysmenuMapper.getSysMenuListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getActMenuList(Map<String, Object> map) {
        return sysmenuMapper.getActMeuList(map);
    }

    @Override
    public SysMenu getSysMenuToVo(Map<String, Object> paraMap) {
        String tag = "sysMenuService.getSysMenuToVo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long sysMenuNo = Long.parseLong(paraMap.get("sysMenuNo").toString());
        return sysmenuRepo.findByCustNoAndSysMenuNoAndUsedYn(custNo, sysMenuNo, "Y");
    }

    @Override
    public List<Map<String, Object>> saveHotMenu(Map<String, Object> paraMap) {
        String tag = "SysMenuService.saveHotMenu => ";
        int hotMenuCounter = sysmenuMapper.getHotMenuCounter(paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SysMenuHot vo = new SysMenuHot();
        vo.setSysMenuNo(Long.parseLong(paraMap.get("sysMenuNo").toString()));
        vo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
        vo.setDispSeq(hotMenuCounter + 1);
        vo.setRegIp((String) paraMap.get("ipaddr"));
        vo.setRegDt(vo.getRegDt());
        vo.setCustNo(custNo);
        sysMenuHotRepository.save(vo);

        int ableHotMenuCount = 0;
        //String resource = "config/mes.properties";
        //Properties properties = new Properties();

        String atmc = env.getProperty("able_tab_menu_counter");
        ableHotMenuCount = Integer.parseInt(atmc);
        paraMap.put("ableHotMenuCount", ableHotMenuCount);
        List<Map<String, Object>> hotMenus = sysmenuMapper.getHotMenuList(paraMap);
        List<Map<String, Object>> retMenus = new ArrayList<Map<String, Object>>();
        return hotMenus;
    }

    @Transactional
    @Override
    public void saveSysMenuInfo(Map<String, Object> passMap) {
        SysMenu sysvo = new SysMenu();
        Long custNo = Long.parseLong(passMap.get("custNo").toString());
        Map<String, Object> paraMap = (Map<String, Object>) passMap.get("sysMenuInfo");
        sysvo.setSysMenuNo(Long.parseLong(paraMap.get("sys_menu_no").toString()));
        try {
            sysvo.setSysMenuNo(Long.parseLong(paraMap.get("sys_menu_no").toString()));
        } catch (NullPointerException ne) {
            sysvo.setSysMenuNo(0L); // 게시
            // 생성일자 등록
            sysvo.setRegDt(sysvo.getModDt());
            sysvo.setRegId(Long.parseLong(passMap.get("userId").toString()));
            sysvo.setRegIp(passMap.get("ipaddr").toString());
            sysvo.setDispYn((String) paraMap.get("Y"));
            sysvo.setUsedYn((String) paraMap.get("Y"));
        }
        sysvo.setParSysMenuNo(Long.parseLong(paraMap.get("par_sys_menu_no").toString()));
        sysvo.setSysMenuNm((String) paraMap.get("sys_menu_nm"));
        sysvo.setPrtSeq(Long.parseLong(paraMap.get("prt_seq").toString()));
        sysvo.setDispYn((String) paraMap.get("disp_yn"));
        sysvo.setUsedYn((String) paraMap.get("used_yn"));

        // 수정일자 등록
        sysvo.setModDt(sysvo.getModDt());
        sysvo.setModId(Long.parseLong(passMap.get("userId").toString()));
        sysvo.setModIp(passMap.get("ipaddr").toString());
        sysvo.setCustNo(custNo);
        sysmenuRepo.save(sysvo);
    }


    @Override
    public int getMenuListCount(Map<String, Object> paraMap) {
        return sysmenuMapper.getMenuListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMenuList(Map<String, Object> paraMap) {
        return sysmenuMapper.getMenuList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getParSysMenus(Map<String, Object> map) {
        return sysmenuMapper.getParSysMenus(map);
    }


    @Transactional
    @Override
    public void initAuthMenu(Map<String, Object> paraMap) {
        String tag = "vsvc.SysMenuService.initAuthMenu => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long authCd = Long.parseLong(paraMap.get("authCd").toString());
        sysMenuAuthRepo.deleteByCustNoAndAuthCd(custNo, authCd);
    }

    @Transactional
    @Override
    public void renewalAuthMenu(Map<String, Object> paraMap) {
        String tag = "vsvc.SysMenuService.renewalAuthMenu => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<String> ds = (List<String>) paraMap.get("menuNos");
        Map<String, Object> smap = null;
        Long authCd = Long.parseLong(paraMap.get("authCd").toString());
        //sysMenuAuthRepo.deleteByAuthCd(authCd);
        Long chkMenuNo = 0L;
        for (String el : ds) {
            SysMenu vo = sysmenuRepo.findByCustNoAndSysMenuNoAndUsedYn(custNo, Long.parseLong(el), "Y");
            if (vo == null) continue;
            while (true) {
                AuthSysMenu authvo = new AuthSysMenu();
                authvo.setSysMenuNo(vo.getSysMenuNo());
                authvo.setAuthCd(authCd);
                AuthSysMenu chkvo = sysMenuAuthRepo.findByCustNoAndSysMenuNoAndAuthCd(custNo, authvo.getSysMenuNo(), authvo.getAuthCd());
                if (chkvo != null) {
                    authvo.setSysMenuAuthNo(chkvo.getSysMenuAuthNo());
                } else {
                    authvo.setSysMenuAuthNo(0L);
                }
                sysMenuAuthRepo.save(authvo);

                vo = sysmenuRepo.findByCustNoAndSysMenuNoAndUsedYn(custNo, vo.getParSysMenuNo(), "Y");
                log.info(tag + "chkMenuNo = " + vo.getParSysMenuNo());
                if (vo.getParSysMenuNo() == 0) {
                    authvo = new AuthSysMenu();
                    authvo.setSysMenuNo(vo.getSysMenuNo());
                    authvo.setAuthCd(authCd);
                    chkvo = sysMenuAuthRepo.findByCustNoAndSysMenuNoAndAuthCd(custNo, authvo.getSysMenuNo(), authvo.getAuthCd());
                    if (chkvo != null) {
                        authvo.setSysMenuAuthNo(chkvo.getSysMenuAuthNo());
                    } else {
                        authvo.setSysMenuAuthNo(0L);
                    }
                    sysMenuAuthRepo.save(authvo);
                    break;
                }

            }
        }
    }

    @Override
    public List<Object> getSysMenuTree(Map<String, Object> paraMap) {
        //String tag = "mgr.am.aadm.selectPageGradeMappingList==>";
        //Map < String, Object > state = new HashMap<String,Object>();

        elist = sysmenuMapper.getSysMenuTree(paraMap);
        List<Object> children = getChildren(0); //eaMenuList(node) 생성
        List<Object> treeData = new ArrayList<Object>();
        root.put("id", "0");
        root.put("text", "MES 시스템");

        LinkedHashMap<String, Object> state = new LinkedHashMap<String, Object>();

        state.put("expanded", true);
        root.put("state", state);
        root.put("children", children);
        treeData.add(root);
        return treeData;
    }

    private List<Object> getChildren(int idx) {
        String pid = "";
        String menuNo = "";
        String menuNm = "";
        Map<String, Object> emap = null;
        int nReadDepth = 0;

        List<Object> menuList0 = new ArrayList<Object>();
        List<Object> menuList1 = new ArrayList<Object>();
        List<Object> menuList2 = new ArrayList<Object>();
        List<Object> menuList3 = new ArrayList<Object>();
        List<Object> menuList4 = new ArrayList<Object>();

        int nSaveDepth = 4; //메뉴최대깊이 : 임시로 상수처리하고 있음.
        int nJdx = 0;
        Map<String, Object> state = new HashMap<String, Object>();
        while (idx < elist.size()) {

            emap = elist.get(idx);
            menuNo = String.valueOf(emap.get("sys_menu_no"));
            menuNm = String.valueOf(emap.get("sys_menu_nm"));
            pid = String.valueOf(emap.get("par_sys_menu_no"));
            nReadDepth = (int) emap.get("level");
            LinkedHashMap<String, Object> node = new LinkedHashMap<String, Object>();
            List<Object> etree = null;
            //
            if (nReadDepth != nSaveDepth) {

                if (nReadDepth == 0) {
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList1.size()) {
//
                        chkmap = (Map<String, Object>) menuList0.get(menuList0.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList1.get(nJdx);
                        Long chkParentId = Long.parseLong(chkmap.get("id").toString());
                        Long parent = Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList1.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList0.add(node);
                } else if (nReadDepth == 1) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    if (menuNm.equals("게시관리")) {

                    }
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList2.size()) {
                        chkmap = (Map<String, Object>) menuList1.get(menuList1.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList2.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
//
                            etree.add(menuList2.get(nJdx));
                        }

                    }

                    if (etree.size() > 0) {
                        chkmap.put("children", etree);

                    }
                    menuList1.add(node);

                } else if (nReadDepth == 2) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList3.size()) {
                        chkmap = (Map<String, Object>) menuList2.get(menuList2.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList3.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList3.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList2.add(node);
                } else if (nReadDepth == 3) {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    nJdx = -1;
                    etree = new ArrayList<Object>();
                    Map<String, Object> chkmap = null;
                    while (++nJdx < menuList4.size()) {
                        chkmap = (Map<String, Object>) menuList3.get(menuList3.size() - 1);
                        Map<String, Object> mapInfo = (Map<String, Object>) menuList4.get(nJdx);
                        int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                        int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                        if (chkParentId == parent) {
                            etree.add(menuList4.get(nJdx));
                        }
                    }
                    if (etree.size() > 0) {
                        chkmap.put("children", etree);
                    }
                    menuList3.add(node);
                } else {
                    node.put("pid", pid);
                    node.put("id", menuNo);
                    node.put("text", menuNm);
                    menuList4.add(node);
                }
                ++idx;
            }
        }
//        List<Object> testList = getChildrenLast(idx - 1);
//
        emap = elist.get(idx - 1);
        menuNo = String.valueOf(emap.get("sys_menu_no"));
        menuNm = String.valueOf(emap.get("sys_menu_nm"));
        pid = String.valueOf(emap.get("par_sys_menu_no"));
        nReadDepth = (int) emap.get("level");

        if (nReadDepth > 0) {
            --nReadDepth;
        }

        LinkedHashMap<String, Object> node = new LinkedHashMap<String, Object>();
        List<Object> etree = null;
        if (nReadDepth == 0) {
            node.put("id", menuNo);
            node.put("text", menuNm);
            nJdx = -1;
            etree = new ArrayList<Object>();
            Map<String, Object> chkmap = null;
            while (++nJdx < menuList1.size()) {
//
                chkmap = (Map<String, Object>) menuList0.get(menuList0.size() - 1);
                Map<String, Object> mapInfo = (Map<String, Object>) menuList1.get(nJdx);
                Long chkParentId = Long.parseLong(chkmap.get("id").toString());
                Long parent = Long.parseLong(mapInfo.get("pid").toString());
                if (chkParentId == parent) {
                    etree.add(menuList1.get(nJdx));
                }
            }
            if (etree.size() > 0) {
                chkmap.put("children", etree);
            }
//            menuList0.add(node);
        } else if (nReadDepth == 1) {
            node.put("pid", pid);
            node.put("id", menuNo);
            node.put("text", menuNm);
            if (menuNm.equals("게시관리")) {

            }
            nJdx = -1;
            etree = new ArrayList<Object>();
            Map<String, Object> chkmap = null;
            while (++nJdx < menuList2.size()) {
                chkmap = (Map<String, Object>) menuList1.get(menuList1.size() - 1);
                Map<String, Object> mapInfo = (Map<String, Object>) menuList2.get(nJdx);
                int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                if (chkParentId == parent) {
//
                    etree.add(menuList2.get(nJdx));
                }
            }
            if (etree.size() > 0) {
                chkmap.put("children", etree);
            }
//            menuList1.add(node);

        } else if (nReadDepth == 2) {

            node.put("pid", pid);
            node.put("id", menuNo);
            node.put("text", menuNm);
            nJdx = -1;
            etree = new ArrayList<Object>();
            Map<String, Object> chkmap = null;
            while (++nJdx < menuList3.size()) {
                chkmap = (Map<String, Object>) menuList2.get(menuList2.size() - 1);
                Map<String, Object> mapInfo = (Map<String, Object>) menuList3.get(nJdx);
                int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                if (chkParentId == parent) {
                    etree.add(menuList3.get(nJdx));
                }
            }
            if (etree.size() > 0) {
                chkmap.put("children", etree);

            }

//            menuList2.add(node);
        } else if (nReadDepth == 3) {
            node.put("pid", pid);
            node.put("id", menuNo);
            node.put("text", menuNm);
            nJdx = -1;
            etree = new ArrayList<Object>();
            Map<String, Object> chkmap = null;
            while (++nJdx < menuList4.size()) {
                chkmap = (Map<String, Object>) menuList3.get(menuList3.size() - 1);
                Map<String, Object> mapInfo = (Map<String, Object>) menuList4.get(nJdx);
                int chkParentId = (int) Long.parseLong(chkmap.get("id").toString());
                int parent = (int) Long.parseLong(mapInfo.get("pid").toString());
                if (chkParentId == parent) {
                    etree.add(menuList4.get(nJdx));
                }
            }
            if (etree.size() > 0) {
                chkmap.put("children", etree);
            }
            menuList3.add(node);
        } else {
            node.put("pid", pid);
            node.put("id", menuNo);
            node.put("text", menuNm);
//            menuList4.add(node);
        }


        return menuList1;
    }

    @Override
    public List<Map<String, Object>> getAuthMenuList(Map<String, Object> paraMap) {
        return sysmenuMapper.getAuthMenuList(paraMap);
    }

    @Override
    public String getMenuPosList(Map<String, Object> paraMap) {
        return sysmenuMapper.getMenuPosList(paraMap);
    }

    @Override
    public List<Map<String, Object>> routerList(Map<String, Object> paraMap) {
        return sysmenuMapper.routerList(paraMap);
    }

    @Override
    /*
      UnixTimeStamp;
       - 2021.08.01 09:00 = 1627776000
       - 2021.08.31 18:00 = 1630400400

     Double형 난수 발생 임의값 = 0.8888650101288209
     */
    public AccHstr makeAccHstr(Map<String, Object> paraMap) {
        String tag = "sysMenuService.makeAccHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());

        /*1. 처리일자 추출 : 강제 생성시에만  사용할 것.
        Map<String,Object> rmap =  cmmnService.getMakeHstrPreVal(paraMap);
        int makeTerm = Integer.parseInt(rmap.get("unixTime").toString()); //날짜생성 기간처리용 unixTime(ex: 2021.08.22 18:00 - 2021.08.01 09:00 의 unixTime)
        int startUnixTime = Integer.parseInt(rmap.get("startUnixTime").toString()); //날짜생성의 최초 시작일 (ex: 2021.08.01 09:00)
        int  menuCount = Integer.parseInt(rmap.get("menuCnt").toString()); //생성메뉴처리용 최대 범위를 설정하기 위한 max(sys_menu.make_seq)
        int  userCount = Integer.parseInt(rmap.get("userCnt").toString()); //생성사용자처리용 최대 범위를 설정하기 위한 max(user_info.make_seq)

        int procUxixTime = 0;
        while(true) {
            procUxixTime = startUnixTime + (int) (Math.random() * makeTerm); //처리월에대한 년월일시분초를  unixTime 형태로 추출
            Date date = new java.util.Date(procUxixTime*1000L);
            DateFormat dfWeek = new SimpleDateFormat("u");
            int weekIdx = Integer.parseInt(dfWeek.format(date));
            if (weekIdx >= 6) continue;
            DateFormat dfTime = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateTime = dfTime.format(date);
            int hour = Integer.parseInt(dateTime.substring(8,10));
            if (hour >= 9 && hour <= 18) break;
        }
        */

        /*2. 처리메뉴*/
        Long procUxixTime = System.currentTimeMillis() / 1000; //메뉴접근시간
        AccHstr ahvo = new AccHstr();
        ahvo.setAccNo(0L);
        ahvo.setAccUnixTime(procUxixTime);

        /*강제생성시에만 사용할 것.
        Double  randomVal = Math.random();
        int menuNo = (int) (randomVal * menuCount );
        menuNo = (menuNo <= 0) ? 1 : menuNo;
        SysMenu smvo = sysmenuRepo.findByCustNoAndMakeSeq(custNo, menuNo );
        ahvo.setSysMenuNo( smvo.getSysMenuNo());

        randomVal = Math.random();
        int userNo = (int) (randomVal * userCount );
        userNo = (userNo <= 0) ? 1 : userNo;
        UserInfo uvo = userRepo.findByMakeSeq(userNo );

        ahvo.setUserId( uvo.getUserId() )  ;
         */
        ahvo.setSysMenuNo(Long.parseLong(paraMap.get("sysMenuNo").toString()));

        ahvo.setAccUnixTime(procUxixTime);
        ahvo.setCustNo(custNo);
        ahvo.setUserId(userId);
        ahvo.setAccNo(0L);
        log.info(tag + "메뉴접근시간 = " + ahvo.getAccUnixTime());//kill
        //ahvo.setAccDt(new java.util.Date(procUxixTime*1000L));

        //AccHstr chkvo = accHstrRepo.findByCustNoAndAccNo(custNo,ahvo.getAccNo());
        //if (chkvo == null) {
        //ahvo.setAccNo(0L);
        ahvo = accHstrRepo.save(ahvo);
        // }
        return ahvo;
    }

    @Override
    public List<Map<String, Object>> getCircleMenuList(Map<String, Object> paraMap) {
        String tag = "SysMenuService.getCircleMenuList => ";
        log.info(tag + " paraMap = " + paraMap.toString());
//        List<Map<String,Object>> ds= sysmenuMapper.getCircleMenuList(paraMap);
//        int circleMentCnt = sysmenuMapper.getCircleMenuListCount(paraMap);
//        if (ds.size() >= 5 && circleMentCnt > 5) {
//            Map<String,Object> moreMap = new HashMap<String,Object>();
//            moreMap.put("id",9999);
//            moreMap.put("routhPath","more");
//            moreMap.put("icon","more");
//            moreMap.put("menuNm","more");
//            moreMap.put("title","more");
//            ds.add(moreMap);
//        }
//        return ds;
        return null;
    }

    @Override
    public int getCircleMenuListCount(Map<String, Object> paraMap) {
        return sysmenuMapper.getCircleMenuListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCustMenuList(Map<String, Object> paraMap) {
        String tag = "SysMenuService.getCustMenuList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return sysmenuMapper.getCustMenuList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCustMenu(Map<String, Object> paraMap) {
        return sysmenuMapper.getCustMenu();
    }

    @Override
    public void deleteCustMenu(Map<String, Object> paraMap) {
        String tag = "BordService.deleteBord => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long sysMenuNo = Long.parseLong(paraMap.get("sysMenuNo").toString());
        SysMenu sysvo = sysmenuRepo.findByCustNoAndSysMenuNoAndUsedYn(custNo,sysMenuNo,"Y");
        if (sysvo != null) {
            sysvo.setUsedYn("N");
            sysmenuRepo.save(sysvo);
        }
    }
}
