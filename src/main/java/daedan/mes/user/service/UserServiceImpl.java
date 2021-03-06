package daedan.mes.user.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.dept.domain.DeptInfo;
import daedan.mes.dept.repository.DeptRepository;
import daedan.mes.sysmenu.domain.SysMenu;
import daedan.mes.sysmenu.service.SysMenuService;
import daedan.mes.user.mapper.UserMapper;
import daedan.mes.user.domain.*;
import daedan.mes.user.repository.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import kidpia.academy.common.storage.StorageService;

/**
 * Created by vivie on 2017-06-08.
 */
@Service("userService")
public  class UserServiceImpl implements UserService {
	private Log log = LogFactory.getLog(this.getClass());

	private static final String DEFAULT_NICKNAME = "번째러버";
	private static final String PROFILE_DEFAULT_PATH = "/profile/0/profile_default.jpg";
	private static final String SIGNIN_EXCEPTION_MSG = "로그인정보가 일치하지 않습니다.";
	private static final String MailAddr_EXIST_EXCEPTION_MSG = "이미 계정이 존재합니다.";
	private static final String NICKNAME_EXIST_EXCEPTION_MSG = "이미 닉네임이 존재합니다.";
	private static final String CHANGE_USER_PASS = "현재 비밀번호가 일치하지 않습니다..";


	@Autowired
	private Environment env;

	@Autowired
	private UserRepository ur;

	@Autowired
	private DeptRepository dr;

	@Autowired
	private CodeRepository cr;

	@Autowired
	private UserMapper mapper;

	@Autowired
	private UserHstrRepository uhr;

	@Autowired
	private AccHstrEvntRepository aheRepo;

	@Autowired
	private CmmnService cmmnService;

	@Autowired
	private AccHstrRepository accHstrRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private SysMenuService sysmenuService;
	@Autowired
	CustInfoRepository custInfoRepo;

	@Autowired
	private AuthUserRepository authUserRepo;

	@Autowired
	private AuthUserMenuRepository authUserMenuRepo;

	@Autowired
	private UserWorkRepository userWorkRepo;

	@Override
	public List<Map<String, Object>> getUserList(Map<String, Object> paraMap) {
		String tag = "userService.getUserList => ";
		log.info(tag + "paraMap = " + paraMap.toString());
		return mapper.getUserList(paraMap);
	}

	@Override
	public int getUserListCount(Map<String, Object> paraMap) {
		return mapper.getUserListCount(paraMap);
	}

	@Override
	public UserInfo signin(String mailAddr, String password) {
		UserInfo userInfo = ur.findByMailAddrAndUsedYn(mailAddr,"Y");
		if (userInfo != null) {
			userInfo.setToken(null);
			userRepo.save(userInfo);
		}
		Objects.requireNonNull(userInfo, SIGNIN_EXCEPTION_MSG);
		if (!this.isAccordPassword(userInfo, password)) {
			throw new IllegalStateException(SIGNIN_EXCEPTION_MSG);
		}

		/*
		Map<String,Object> umap = new HashMap<String,Object>();
		umap.put("emplId", userInfo.getUserId());
		umap = mapper.getUserInfo(umap)
		*/
		return userInfo;
	}

	//회원가입 & 정보수정
	@Override
	public UserInfo signup(UserInfo UserMast) {
		String MailAddr = UserMast.getMailAddr();
		this.validate(MailAddr);
		this.setupForSave(UserMast);
		UserInfo createdMember = ur.save(UserMast);
		return createdMember;
	}


	@Override
	public boolean isExist(String mailAddr) {
		boolean isExist = false;
		UserInfo member = ur.findByMailAddrAndUsedYn(mailAddr,"Y");
		if (member != null) {
			isExist = true;
		}
		return isExist;
	}

	@Override
	public void validate(String MailAddr) {

	}


	private boolean isAccordPassword(UserInfo userMast, String password) {
		String encodedPassword = userMast.getSecrtNo();
		return BCrypt.checkpw(password, encodedPassword);
	}

	@Override
	public void dropUser(Map<String, Object> paraMap) {
		UserInfo uvo = new UserInfo();
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		uvo.setUserId(Long.parseLong(paraMap.get("emplId").toString()));
		UserInfo chkvo = ur.findByUserIdAndUsedYn(uvo.getUserId(),"Y");;
		if (chkvo != null) {
			chkvo.setUsedYn("N");
			chkvo.setModDt(chkvo.getRegDt());
			chkvo.setModIp(chkvo.getRegIp());
			chkvo.setModId(chkvo.getRegId());
			chkvo.setCustInfo(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));
			ur.save(chkvo);
		}
	}

	@Override
	public Map<String, Object> getEntryInfo() {
		Long custNo = Long.parseLong(env.getProperty("cust_no"));
		return StringUtil.voToMap(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));
	}


	@Override
	public Map<String, Object> getCustInfo(Map<String, Object> paraMap) {
		return mapper.getCustInfo(paraMap);
	}


	@Override
	public void saveUser(Map<String, Object> paraMap) {
		String tag = "UserService.saveUser => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		UserInfo uvo = new UserInfo();
		try {
			uvo.setUserId(Long.parseLong(paraMap.get("emplId").toString()));
		} catch (NullPointerException ne) {
			uvo.setUserId(0L);
		}
		try {
			uvo.setErpUserNo(paraMap.get("erpUserNo").toString());
		} catch (NullPointerException ne) {

		}

		uvo.setUserNm(paraMap.get("userNm").toString());
		uvo.setMatrInspYn(paraMap.get("matrInspYn").toString()); //원료입고 검수자 여부
		uvo.setProdInspYn(paraMap.get("prodInspYn").toString()); //제품춢고 검수자 여부
		try {
			uvo.setUserPosn(Long.parseLong(paraMap.get("userPosn").toString()));
		}
		catch (NullPointerException ne) {

		}
		uvo.setCustInfo(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));
		uvo.setUsedYn("Y");
		uvo.setOcpnKind(Long.parseLong(paraMap.get("ocpnKind").toString())); //직종구분 (사무직,생산직)

		/*채용구분 : 내국인,외국인,용역*/
		try {
			Long emplKind = Long.parseLong(paraMap.get("emplKind").toString());
			uvo.setEmplKind ( (emplKind == 0) ? 231 : emplKind) ;
		}
		catch(NullPointerException ne) {
			uvo.setEmplKind (231L);
		}
		/*사용자상태 : 근무,정직,휴직,퇴사*/
		uvo.setUserStat(Long.parseLong(paraMap.get("userStat").toString()));

		/*이용자권한 관리자,사용자,방문객*/
		try {
			uvo.setUserTp(UserType.valueOf(paraMap.get("userTp").toString()));
		}
		catch (NullPointerException ne) {
			uvo.setUserTp(UserType.USER);
		}
		try {
			uvo.setDeptNo(Long.parseLong(paraMap.get("deptNo").toString()));
		} catch (NullPointerException ne) {
			uvo.setDeptNo(0L);
		}
		/*사진*/
		try {
			uvo.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
		} catch (NullPointerException ne) {
			uvo.setFileNo(0L);
		}
		/*이동전화 식별번호*/
		try {
			uvo.setCellId(paraMap.get("cellId").toString());
		} catch (NullPointerException ne) {

		}
		try {
			uvo.setCellNo(cmmnService.encryptStr(custNo,paraMap.get("cellNo").toString())); //이동전화(암호화사용)
		} catch (NullPointerException ne) {
			uvo.setCellNo(cmmnService.encryptStr(custNo, "010-000-0000"));
		}
		/*메일주소*/
		try {
			uvo.setMailAddr(paraMap.get("mailAddr").toString());
		} catch (NullPointerException ne) {

		}
		/*입사일자*/
		try {
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			uvo.setEntrDt(transFormat.parse((String) paraMap.get("entrDt").toString().substring(0,10)));
		} catch (NullPointerException | ParseException ne) {

		}
		uvo.setIndsTp(IndsType.valueOf(env.getProperty("industry_type")));
		uvo.setModDt(uvo.getModDt());
		uvo.setModIp(paraMap.get("ipaddr").toString());
		uvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
		UserInfo chkvo = ur.findByUserIdAndUsedYn(uvo.getUserId(),"Y");
		if (chkvo != null) {
			uvo.setUserId(chkvo.getUserId());
			uvo.setSecrtNo(chkvo.getSecrtNo());
			uvo.setRegDt(chkvo.getRegDt());
			uvo.setRegIp(chkvo.getRegIp());
			uvo.setRegId(chkvo.getRegId());
		} else {
			uvo.setRegDt(DateUtils.getCurrentDate());
			uvo.setRegIp(paraMap.get("ipaddr").toString());
			uvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
			uvo.setSecrtNo(BCrypt.hashpw("adm", BCrypt.gensalt()));
		}
		uvo.setCustInfo(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));

		chkvo = userRepo.findByUserIdAndUsedYn(uvo.getUserId(),"Y");
		if (chkvo != null) {
			uvo.setRegDt(DateUtils.getCurrentDate());
			uvo.setRegIp(paraMap.get("ipaddr").toString());
			uvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));

		}
		ur.save(uvo);
	}

	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> paraMap) {
		String tag = "userService.getUserInfo => ";
		String strCellNo = "";
		log.info(tag + "paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		Map<String,Object> rmap = mapper.getUserInfo(paraMap);
		UserInfo uvo = userRepo.findByUserIdAndUsedYn(Long.parseLong(paraMap.get("emplId").toString()),"Y");
		if (uvo != null) {
			try {
				strCellNo = cmmnService.decryptStr(custNo,uvo.getCellNo());
				rmap = StringUtil.voToMap(uvo);
				rmap.put("cellNo",strCellNo);
				log.info(tag + "rmap = " + rmap);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return rmap;
	}

	/*
	@Override
	public String uploadProfileImg(String encodeImg, int memberId) {z
		String fileName = this.getRandomImageName();
		String fileDir ="profile/" + memberId + "/";
		awsService.uploadFile(encodeImg, fileDir, fileName);

		UserMast member = userRepository.findByIdAndUsedYn(memberId,"Y").get();
		member.setProfileImg("/"+fileDir +fileName);
		userRepository.save(member);
		return member.getProfileImg();
	}
	*/
	private String getRandomImageName() {
		return UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
	}

	private void setupForSave(UserInfo userMast) {
		String password = userMast.getSecrtNo();
		String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userMast.setSecrtNo(encodedPassword);

		userMast.setUserTp(UserType.USER);
	}

	private void updateSecrtNo(UserInfo userMast, String password) {
		if (!password.equals("")) {
			String encodePassword = BCrypt.hashpw(password, BCrypt.gensalt());
			userMast.setSecrtNo(encodePassword);
		}
	}

	@Override
	@Transactional
	public void loadRawMatByExcel(Map<String, Object> paraMap) throws Exception {
		String tag = "MatrService.loadRawMatByExcel => ";
		StringBuffer buf = new StringBuffer();
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		//String fileRoot = env.getProperty("file.root.path");
		String fileRoot= File.separator;
		buf.setLength(0);
		buf.append(fileRoot);
		String absFilePath = buf.toString();
		log.info(tag + " absFilePath = " + absFilePath);
		FileInputStream file = new FileInputStream(buf.toString());
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		int rowindex = 0;
		int basePubsUnitCd = 0;
		XSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		log.info(tag + "excel처리행수 = " + rows);
		String cmpyNm = "";


		for (rowindex = 0; rowindex < rows - 1; rowindex++) {
			if (rowindex <= 1) continue; //헤더정보 skip
			UserInfo userInfo = new UserInfo();


			XSSFRow row = sheet.getRow(rowindex);
			if (row == null) continue;

			int cells = row.getPhysicalNumberOfCells();
//			for(columnindex=0; columnindex<=cells; columnindex++) {
			userInfo.setUserId(0L);
			log.info(tag + " rowindex = " + rowindex);//kill
//			XSSFCell cell=row.getCell(0);

			log.info(" 머지0 = " + row.getCell(0).getStringCellValue());
			log.info(" 머지1 = " + row.getCell(1).getStringCellValue());
			log.info(" 머지3 = " + row.getCell(3).getStringCellValue());
			log.info(" 머지4 = " + row.getCell(4).getStringCellValue());
			log.info(" 머지5 = " + row.getCell(5).getStringCellValue());
			log.info(" 머지7 = " + row.getCell(7).getStringCellValue());

			DeptInfo deptInfo = new DeptInfo();
			CodeInfo codeInfo = new CodeInfo();

			String deptNm = row.getCell(3).getStringCellValue();
			deptInfo = dr.findByCustNoAndDeptNmAndUsedYn(custNo,deptNm,"Y");

			String codeNm = row.getCell(4).getStringCellValue();
			codeInfo = cr.findByCodeNmAndUsedYn(codeNm,"Y");
			String beforeDate = row.getCell(5).getStringCellValue();

			DateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date tempDate = sdFormat.parse(beforeDate);


			try {
				deptInfo.getDeptNo();
				codeInfo.getCodeNo();
				userInfo.setOcpnKind(codeInfo.getCodeNo());
				userInfo.setDeptNo(deptInfo.getDeptNo());

			}
			catch (NullPointerException e) {

				codeInfo = new CodeInfo();
				deptInfo = new DeptInfo();
				deptInfo.setDeptNo(0L); // 게시
				codeInfo.setCodeNo(0L);
				userInfo.setOcpnKind(codeInfo.getCodeNo());
				userInfo.setDeptNo(deptInfo.getDeptNo());
			}

//			userInfo.setUserTp("GUEST");

//				String encodedPassword = userInfo.getSecrtNo();
////			log.info("머냐"+to);
				userInfo.setEntrDt(tempDate);
				userInfo.setErpUserNo(row.getCell(0).getStringCellValue());
				userInfo.setUserNm(row.getCell(1).getStringCellValue());
				userInfo.setMailAddr(row.getCell(7).getStringCellValue());
				userInfo.setUsedYn("Y");

			    String password = "1234";
				userInfo.setUserTp(UserType.GUEST);
				userInfo.setSecrtNo(BCrypt.hashpw(password, BCrypt.gensalt()));
			log.info(tag + " rowindex = " + rowindex);
			log.info(tag + " rowindex = " + rowindex);

//
//			UserInfo chkvo = ur.findByErpUserNoAndUsedYn(userInfo.getErpUserNo(),"Y");
//
//			log.info("머냐?"+chkvo);
//
//			if (chkvo == null) {
				userInfo.setCustInfo(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));
				ur.save(userInfo);
//			}
//					dr.save(deptInfo);
//					cr.save(codeInfo);
//					userInfo = ur.findByMailAddrAndUsedYn(cmpyNm,"Y");
				}
			}

	@Override
	public UserInfo getUserData(String mailAddr, String password, String newPassword) {
		UserInfo userInfo = ur.findByMailAddrAndUsedYn(mailAddr,"Y");
		Objects.requireNonNull(userInfo, CHANGE_USER_PASS);
		if (!this.isAccordPassword(userInfo, password)) {
			throw new IllegalStateException(CHANGE_USER_PASS);
		}
		UserInfo uvo = userInfo;
		uvo.setSecrtNo(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

		ur.save(uvo);
		return uvo;
	}

	@Override
	public List<Map<String, Object>> getComboDeptList(Map<String, Object> paraMap) {
		return mapper.getComboDeptList(paraMap);
	}
	@Override
	public int getComboDeptListCount(Map<String, Object> paraMap) {
		return mapper.getComboDeptListCount(paraMap);
	}

	@Override
	public List<Map<String, Object>> getTabletWorkerList(Map<String, Object> paraMap){
		return mapper.getTabletWorkerList(paraMap);
	}

	@Override
	public int getTabletWorkerListCount(Map<String, Object> paraMap){
		return mapper.getTabletWorkerListCount(paraMap);
	}

	@Override
	public List<Map<String, Object>> getHstrSummaryList(Map<String, Object> paraMap){
		return mapper.getHstrSummaryList(paraMap);
	}
	@Override
	public int getHstrSummaryListCount(Map<String, Object> paraMap){
		return mapper.getHstrSummaryListCount(paraMap);
	}

	@Override
	public void makeAccHstr(Map<String, Object> paraMap) {
		String tag = "vsvc.makeAccHstr => ";
		log.info(tag + "paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		AccHstr vo = new AccHstr();
		vo.setUserId(Long.parseLong(paraMap.get("emplId").toString()));
		try {
			vo.setSysMenuNo(Long.parseLong(paraMap.get("sysMenuNo").toString()));
			AccHstr chkvo = accHstrRepo.findByCustNoAndAccNo(custNo,vo.getAccNo());
			if (chkvo == null) {
				accHstrRepo.save(vo);

				SysMenu mvo = sysmenuService.getSysMenuToVo(paraMap);
				if (mvo != null) {
					paraMap.put("sysMenuNm",mvo.getSysMenuNm());
					UserInfo uvo = ur.findByUserIdAndUsedYn(Long.parseLong(paraMap.get("userId").toString()), "Y");
					if(uvo != null){
						uvo.setAccPath(paraMap.get("sysMenuNm").toString());
						ur.save(uvo);
					}
				}
			}
		}
		catch (NullPointerException ne) {
		}
	}

	@Override
	public List<Map<String, Object>> getAuthUserList(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.getAuthUserList => ";
		log.info(tag + "paraMap = " + paraMap.toString());
		return mapper.getAuthUserList(paraMap);
	}

	@Override
	public int getAuthUserListCount(Map<String, Object> paraMap) {
		return mapper.getAuthUserListCount(paraMap);
	}

	@Override
	public void renewalAuthUser(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.renewalAuthUser => ";
		log.info(tag + "paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		List<Integer> ds = (List<Integer>) paraMap.get("users");
		Map<String,Object> smap = null;
		Long authCd = Long.parseLong(paraMap.get("authCd").toString());

		for(Integer el : ds){
			AuthUser authvo = new AuthUser();
			authvo.setUserId((long) el);
			authvo.setAuthCd(authCd);
			authvo.setUsedYn("Y");
			AuthUser chkvo = authUserRepo.findByCustNoAndUserIdAndAuthCd(custNo,authvo.getUserId(), authvo.getAuthCd());
			if (chkvo != null) {
				authvo.setAuthUserNo(chkvo.getAuthUserNo());
			}
			else {
				authvo.setAuthUserNo(0L);
			}
			authUserRepo.save(authvo);
		}
	}

	@Override
	public List<Map<String, Object>> getAuthUserMenuList(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.getAuthUserMenuList => ";
		log.info(tag + "paramMap = " + paraMap.toString());
		return mapper.getAuthUserMenuList(paraMap);
	}

	@Override
	public int getAuthUserMenuListCount(Map<String, Object> paraMap) {
		return mapper.getAuthUserMenuListCount(paraMap);
	}

	@Override
	public void saveAuthUserMenu(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.saveAuthUserMenu => ";
		log.info(tag + "paramMap = " + paraMap.toString());
		String paramStr = "";
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		AuthUserMenu vo = new AuthUserMenu();
		vo.setSysMenuNo(Long.parseLong(paraMap.get("sysMenuNo").toString()));
		vo.setAuthUserNo(Long.parseLong(paraMap.get("authUserNo").toString()));
		AuthUserMenu chkvo = authUserMenuRepo.findByCustNoAndAuthUserNoAndSysMenuNo(custNo,vo.getAuthUserNo(), vo.getSysMenuNo());
		vo.setAuthUserMenuNo( chkvo == null ? 0L :  chkvo.getAuthUserMenuNo());
		try {
			paramStr = paraMap.get("apndYn").toString();
			vo.setApndYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {

		}
		try {
			paramStr = paraMap.get("saveYn").toString();
			vo.setSaveYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {

		}
		try {
			paramStr = paraMap.get("dropYn").toString();
			vo.setDropYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {

		}
		try {
			paramStr = paraMap.get("dnloadYn").toString();
			vo.setDnloadYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {

		}
		try {
			paramStr = paraMap.get("uploadYn").toString();
			vo.setUploadYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {

		}

		try {
			paramStr = paraMap.get("listPrtYn").toString();
			vo.setListPrtYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {
		}
		try {
			paramStr = paraMap.get("infoPrtYn").toString();
			vo.setInfoPrtYn(paramStr.equals("-") ? "N": paramStr);
		}
		catch (NullPointerException ne) {
		}
		try {
			String perInfoLvl = paraMap.get("perInfoLvl").toString();
			if (perInfoLvl.equals("-")) {
				vo.setPerInfoLvl(0);
			}
			else {
				vo.setPerInfoLvl(Integer.parseInt(paraMap.get("perInfoLvl").toString()));
			}
		}
		catch (NullPointerException ne) {
			vo.setPerInfoLvl(0);
		}
		authUserMenuRepo.save(vo);
	}

	@Override
	public void initToken(Map<String, Object> paraMap) {
		String tag = "vsvc.UserService.initToken";
		UserInfo vo = null;
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		log.info(tag + "paraMap = " + paraMap.toString());
		vo = userRepo.findByUserIdAndUsedYn(Long.parseLong(paraMap.get("userId").toString()),"Y");
		if (vo != null) {
			vo.setToken(null);
			userRepo.save(vo);
		}
	}

	@Override
	public UserInfo saveToken(Map<String, Object> paraMap) {
		String tag = "vsvc.UserService.saveToken";
		UserInfo vo = null;
		log.info(tag + "paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		vo = userRepo.findByUserIdAndUsedYn(Long.parseLong(paraMap.get("userId").toString()),"Y");
		if (vo != null) {
			vo.setToken(paraMap.get("token").toString());
			vo = userRepo.save(vo);
		}
		return vo;
	}

	@Override
	public void renewalUserData(Map<String, Object> paraMap){
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		UserInfo chkvo = ur.findByUserIdAndUsedYn(Long.parseLong(paraMap.get("userId").toString()), "Y");
		if(chkvo != null){
			chkvo.setAuthCd(Long.parseLong(paraMap.get("authCd").toString()));
			ur.save(chkvo);
		}
	}

	@Override
	public void setLastAccPath(Map<String, Object> paraMap) {
		String tag = "UserService.setLastAccPath => ";
		log.info(tag + "paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		UserInfo chkvo = ur.findByUserIdAndUsedYn(Long.parseLong(paraMap.get("userId").toString()), "Y");
		if(chkvo != null){
			chkvo.setAccPath(paraMap.get("sysMenuNm").toString());
			ur.save(chkvo);
		}
	}

	@Override
	public UserInfo getUserInfByToken(HashMap<String, Object> paraMap) {
		String tag = "UserService.getUserInfByToken => ";
		log.info(tag + "paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		UserInfo userInfo = ur.findByToken(paraMap.get("token").toString());
		if (userInfo != null) {
			userRepo.save(userInfo);
		}
		Objects.requireNonNull(userInfo, SIGNIN_EXCEPTION_MSG);
		return userInfo;

	}

	@Override
	public UserInfo getUserInfoById(Long userId) {
		UserInfo uvo = userRepo.findByUserId(userId );
		//CustInfo civo = custInfoRepo.findByCustNoAndUsedYn(uvo.,"Y");
		//uvo.setCustInfo(civo);
		return uvo;
	}

	@Override
	public CustInfo getCustInfoByCustNo(HashMap<String, Object> paraMap) {
		String tag = "userService.getCustInfoByCustNo";
		CustInfo vo = new CustInfo();
		log.info(tag + "paraMap = " + paraMap.toString());
		CustInfo chkvo =  custInfoRepo.findByCustNoAndUsedYn(Long.parseLong(paraMap.get("custNo").toString()),"Y");
		if  (chkvo != null) vo = chkvo;
		return vo;
	}


	@Override
	public List<Map<String, Object>> getWorkerList(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.getWorkerList => ";
		log.info(tag + "paramMap = " + paraMap.toString());
		return mapper.getWorkerList(paraMap);
	}

	@Override
	public int getWorkerListCount(Map<String, Object> paraMap) {
		return mapper.getWorkerListCount(paraMap);
	}


	@Override
	public List<Map<String, Object>> getWorkList(Map<String, Object> paraMap) {
		String tag = "userService.getWorkList => ";
		log.info(tag + "paramMap = " + paraMap.toString());
		return mapper.getWorkList(paraMap);
	}

	@Override
	public int getWorkListCount(Map<String, Object> paraMap) {
		return mapper.getWorkListCount(paraMap);
	}

	@Override
	public List<Map<String, Object>> getUserAccLogList(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.getUserAccLogList => ";
		log.info(tag + "paramMap = " + paraMap.toString());
		return mapper.getUserAccLogList(paraMap);
	}
	@Override
	public int getUserAccLogListCount(Map<String, Object> paraMap) {
		String tag = "vsvc.userService.getUserAccLogListCount => ";
		log.info(tag + "paramMap = " + paraMap.toString());
		return mapper.getUserAccLogListCount(paraMap);
	}

	@Transactional
	@Override
	public void saveWorkInfo(Map<String, Object> paraMap) {
		String tag = "UserService.saveWorkInfo => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		this.saveWork(paraMap);
	}
	@Transactional
	@Override
	public void saveWorkList(Map<String, Object> paraMap) {
		String tag = "UserService.saveWorkList => ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		log.info(tag + " paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		Long userId = Long.parseLong(paraMap.get("userId").toString());
		Long worker = 0L;
		Long workNo = 0L;
		String workDt = paraMap.get("workDt").toString().substring(0,10);
		String ipaddr = paraMap.get("ipaddr").toString();
		List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("workerList");
		for (Map<String, Object> el : ds) {
			UserWork uwvo = new UserWork();
			workNo = Long.parseLong(el.get("workNo").toString());
			uwvo.setCustNo(custNo);
			try {
				uwvo.setWorkDt(sdf.parse(workDt));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			uwvo.setUserId(Long.parseLong(el.get("userId").toString()));
			//el.put("userId", userId);
			uwvo.setWorkFrTm(paraMap.get("workFrTm").toString());
			uwvo.setWorkToTm(paraMap.get("workToTm").toString());
			uwvo.setModId(userId);
			uwvo.setModIp(ipaddr);
			uwvo.setModDt(DateUtils.getCurrentDateTime());
			UserWork chkvo = userWorkRepo.findByCustNoAndWorkNoAndUsedYn(custNo,workNo,"Y");
			if (chkvo != null) {
				uwvo.setWorkNo(chkvo.getWorkNo());
				uwvo.setRegDt(chkvo.getRegDt());
				uwvo.setRegIp(chkvo.getRegIp());
				uwvo.setRegDt(chkvo.getRegDt());
			}
			else {
				uwvo.setWorkNo(0L);
				uwvo.setRegId(userId);
				uwvo.setRegIp(ipaddr);
				uwvo.setRegDt(DateUtils.getCurrentDateTime());
			}
			uwvo.setUsedYn("Y");
			uwvo.setCustNo(custNo);
			userWorkRepo.save(uwvo);
		}
	}

	private void saveWork(Map<String, Object> paraMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Long workNo = 0L;

		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		Long userId = Long.parseLong(paraMap.get("userId").toString());
		String ipaddr = paraMap.get("ipaddr").toString();

		Long worker = 0L;
		try {
			worker = Long.parseLong(paraMap.get("workerId").toString());
		}
		catch (NullPointerException ne) {

		}
		try {
			Date workDt = sdf.parse(paraMap.get("workDt").toString());
			UserWork uwvo = new UserWork();
			uwvo.setModDt(DateUtils.getCurrentBaseDateTime());
			uwvo.setModIp(paraMap.get("ipaddr").toString());
			uwvo.setModId(userId);

			uwvo.setWorkFrTm(paraMap.get("workFrTm").toString());
			uwvo.setWorkToTm(paraMap.get("workToTm").toString());

			uwvo.setUserId(worker);
			uwvo.setWorkDt(workDt);
			uwvo.setUsedYn("Y");
			try {
				workNo = Long.parseLong(paraMap.get("workNo").toString());
			}
			catch (NullPointerException ne) {
				workNo = 0L;
			}
			uwvo.setModDt(DateUtils.getCurrentBaseDateTime());
			uwvo.setModId(userId);
			uwvo.setModIp(ipaddr);

			UserWork uwchk = userWorkRepo.findByCustNoAndWorkNoAndUsedYn(custNo , workNo  , "Y");
			if (uwchk != null){
				uwvo.setWorkNo(uwchk.getWorkNo());
				uwvo.setRegDt(uwchk.getRegDt());
				uwvo.setRegId(uwchk.getRegId());
				uwvo.setRegIp(uwchk.getRegIp());
				uwvo.setUserId(uwchk.getUserId());
			}
			else{
				uwvo.setWorkNo(0L);
				uwvo.setRegDt(DateUtils.getCurrentBaseDateTime());
				uwvo.setRegId(userId);
				uwvo.setRegIp(ipaddr);
			}
			uwvo.setUsedYn("Y");
			uwvo.setCustNo(custNo);
			userWorkRepo.save(uwvo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public AccHstr saveAccHstr(Map<String, Object> paraMap) {
		String tag = "UserService.saveAccHstr => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		AccHstr accHstr = new AccHstr();
		accHstr.setAccNo(0L);
		accHstr.setCustNo(custNo);
		accHstr.setSysMenuNo(Long.parseLong(paraMap.get("sysMenuNo").toString()));
		accHstr.setUserId(Long.parseLong(paraMap.get("userId").toString()));
		accHstr.setAccDt(DateUtils.getCurrentBaseDateTime());
		return accHstrRepo.save(accHstr);
	}

	@Transactional
	@Override
	public void saveAccLogEvnt(Long custNo, Long accNo, EvntType evntTp, int transCnt) {
		String tag = "UserService.hstrEnvtSave => ";
		Map<String, Object> paraMap = new HashMap<String,Object>();
		log.info(tag + " paraMap = " + paraMap.toString());

		AccHstrEvnt vo = new AccHstrEvnt();
		vo.setAccEvntNo(0L);
		vo.setCustNo(custNo);
		vo.setAccNo(accNo);
		vo.setEvntTp(evntTp);
		vo.setTransCnt(transCnt);
		aheRepo.save(vo);
	}

	@Transactional
	@Override
	public void deleteAuthUser(Map<String, Object> paraMap) {
		String tag = "UserService.deleteAuthUser => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
//		Map<String,Object> dmap = (Map<String, Object>) paraMap.get("userList");
		List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("userList");
		//userList=[{userNm=한  환, authCd=2501, userPosn=250, posnNm=과장, deptNm=E-biz팀, userId=284115, deptNo=1160, vgt_id=0, originalIndex=0, vgtSelected=true}, {userNm=김재현, authCd=2501, userPosn=251, posnNm=대리, deptNm=E-biz팀, userId=284116, deptNo=1160, vgt_id=0, originalIndex=1, vgtSelected=true}, {userNm=이상경, authCd=2501, userPosn=248, posnNm=차장, deptNm=GMP동(2F), userId=284068, deptNo=550, vgt_id=0, originalIndex=2, vgtSelected=true}], custNo=6, pageNo=0}

		for(Map<String, Object> el : ds) {
			Long userId = Long.parseLong(el.get("userId").toString());
			Long authCd = Long.parseLong(el.get("authCd").toString());
			try {
				AuthUser auvo = authUserRepo.findByCustNoAndUserIdAndAuthCd(custNo, userId, authCd);
				log.info("###"+ auvo.toString());
				auvo.setUsedYn("N");
				log.info("@@@"+ auvo.toString());
				auvo.setModDt(DateUtils.getCurrentBaseDateTime());
//				auvo.setModIp(el.get("modIp").toString());
				authUserRepo.save(auvo);

			}
			catch(NullPointerException ne) {
				continue;
			}
		}
	}

	@Override
	public void dropWorkerList(Map<String, Object> paraMap) {
		String tag = "UserService.dropWorkerList =>";
		log.info(tag + " paraMap = " + paraMap.toString());
		Long custNo = Long.parseLong(paraMap.get("custNo").toString());
		Long userId = Long.parseLong(paraMap.get("userId").toString());
		String ipaddr = paraMap.get("ipaddr").toString();

		List<Map<String, Object>> dsMap = (List<Map<String, Object>>) paraMap.get("WorkerList");
		for (Map<String, Object> el : dsMap) {
			String workDt = paraMap.get("workDt").toString().substring(0,10);
			Long workerId= Long.parseLong(el.get("value").toString());
			UserWork chkpmvo = userWorkRepo.findByCustNoAndWorkDtAndUserIdAndUsedYn(custNo, workDt, workerId ,"Y");
			if (chkpmvo != null) {
				chkpmvo.setUsedYn("N");
				chkpmvo.setModDt(DateUtils.getCurrentBaseDateTime());
				chkpmvo.setModId(userId);
				chkpmvo.setModIp(ipaddr);
				userWorkRepo.save(chkpmvo);
			}
		}
	}


	@Override
	public List<Map<String, Object>> getHstrList(Map<String, Object> paraMap) {
		String tag = "UserService.getHstrList => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		String actEvtLogYn = paraMap.get("actEvtLogYn").toString();
		return  (actEvtLogYn.equals("Y")) ?  mapper.getHstrEvtLogList(paraMap) : mapper.getHstrList(paraMap);
	}

	@Override
	public int getHstrListCount(Map<String, Object> paraMap) {
		String actEvtLogYn = paraMap.get("actEvtLogYn").toString();
		return  (actEvtLogYn.equals("Y")) ?  mapper.getHstrEvtLogListCount(paraMap) : mapper.getHstrListCount(paraMap);
	}

	@Transactional
	@Override
	public void dropWorkInfo(Map<String, Object> paraMap) {
		String tag = "UserService.getHstrListCount => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		Long custNo  = Long.parseLong(paraMap.get("custNo").toString());
		Long workNo  = Long.parseLong(paraMap.get("workNo").toString());
		UserWork uwvo  = userWorkRepo.findByCustNoAndWorkNoAndUsedYn(custNo,workNo,"Y");
		if  (uwvo != null) {
			uwvo.setUsedYn("N");
			uwvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
			uwvo.setModDt(DateUtils.getCurrentDateTime());
			uwvo.setModIp(paraMap.get("ipaddr").toString());
			userWorkRepo.save(uwvo);
		}
	}

	public List<Map<String, Object>> getUserGroup(Map<String, Object> paraMap) {
		String tag = "UserService.getUserGroup => ";
		log.info(tag + " paraMap = " + paraMap.toString());
		return mapper.getUserGroup(paraMap);
	}
}
