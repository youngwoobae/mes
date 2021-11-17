package daedan.mes.common.service.util;


import daedan.mes.common.error_handle.CustomErrorException;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common</li>
 * <li>설 명 : CommonUtils.java / 공통 함수 모음</li>
 * <li>작성일 : 2020. 6. 12.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
public class CommonUtils {

	/**
	 * 
	 * getConvertMap
	 * 
	 * @param param
	 * @return Map<Integer,Object>
	 */
	public Map<Integer, Object> getConvertMap(Map<String, Object> param) {
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		int cnt = 0;
		for (Entry<String, Object> entrStr : param.entrySet()) {
			// String key = entrStr.getKey();
			String value = (String) entrStr.getValue();
			result.put(cnt, value);
			cnt++;
		}
		return result;
	}

	/**
	 * 
	 * getlocale
	 * 
	 * @param request
	 * @return String
	 */
	public static Locale getLocale(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Locale locale = null;
		if (session.getAttribute("locale") != null) {
			locale = (Locale) session.getAttribute("locale");
		}
		return locale;
	}

	/**
	 * 
	 * getlocale
	 * 
	 * @param session
	 * @return String
	 */
	public static String getLocale(HttpSession session) {
		Map<String, Object> reMap = getSessionLocaleInfo(session);
		String locale = null;
		if (reMap != null && reMap.get("locale") != null) {
			locale = (String) reMap.get("locale");
		}
		return locale;
	}

	/**
	 * 
	 * getSessionLocaleInfo
	 * 
	 * @param session
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> getSessionLocaleInfo(HttpSession session) {
		Map<String, Object> localeInfo = new HashMap<String, Object>();
		Locale locale = (Locale) session.getAttribute("locale");
		localeInfo.put("locale", locale.toString());
		return localeInfo;
	}

	/**
	 * getUserId
	 * 
	 * @param session
	 * @return String
	 */
	public static String getUserId(HttpSession session) {
		Map<String, Object> reMap = getSessionInfo(session);
		String userId = null;
		if (reMap != null && reMap.get("userId") != null) {
			userId = reMap.get("userId").toString();
		}
		return userId;
	}

	/**
	 * getSessionInfo
	 * 
	 * @param session
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> getSessionInfo(HttpSession session) {
		Map<String, Object> userInfo = new HashMap<String, Object>();
		String userId = (String) session.getAttribute("userId");
		userInfo.put("userId", userId);
		return userInfo;
	}

	/**
	 * isEmpty
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
			return true;
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		if (obj instanceof List) {
			return ((List<?>) obj).isEmpty();
		}
		if (obj instanceof Object[]) {
			// return (((Object[])obj).length == 0);
			if ((((Object[]) obj).length == 0)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * isEmptyContainsNullString
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isEmptyContainsNullString(Object obj) {
		if (null == obj || "".equals(obj) || "null".equalsIgnoreCase(String.valueOf(obj))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 입력한 값이 NULL OR Empty 인 지 검사한다.
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null || StringUtils.isEmpty(obj)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 입력한 값이 NULL OR Empty 일 경우 Empty 문자열을 돌려준다. 그렇지 않을 경우, 입력한 값을 돌려준다.
	 * 
	 * @param obj
	 * @return
	 */
	public static String isNullOrEmptyDefaultValue(Object obj) {
		if (obj == null || StringUtils.isEmpty(obj)) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * LPAD
	 * 
	 * @param str
	 * @param size
	 * @param fStr
	 * @return String
	 */
	public static String lPad(String str, int size, String fStr) {
		byte[] bte = str.getBytes();
		int len = bte.length;

		int tmp = size - len;

		for (int i = 0; i < tmp; i++) {
			str = fStr + str;
		}
		return str;
	}

	/**
	 * 
	 * RPAD
	 * 
	 * @param str
	 * @param size
	 * @param fStr
	 * @return String
	 */
	public static String rPad(String str, int size, String fStr) {

		byte[] bte = str.getBytes();
		int len = bte.length;

		int tmp = size - len;

		for (int i = 0; i < tmp; i++) {
			str += fStr;
		}
		return str;
	}

	/**
	 * 
	 * isNull
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * formatHTMLCode
	 * 
	 * @param text
	 * @return String
	 */
	public static String formatHTMLCode(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		// StringBuffer sb = new StringBuffer(text);
		String[] iPatt = { "<", ">", "\'", "\"", " ", "\r", "\n" };
		String[] oPatt = { "&lt;", "&gt;", "&#39;", "&quot;", "&nbsp;", "<BR>", "<BR>" };

		String s1 = text;
		for (int i = 0; i < iPatt.length; i++) {
			s1 = s1.replaceAll(iPatt[i], oPatt[i]);
		}
		/*
		 * for(int i = 0; i < sb.length(); i++) { char ch = sb.charAt(i); if(ch == '<')
		 * { sb.replace(i, i + 1, "&lt;"); i += 3; } else if(ch == '>') { sb.replace(i,
		 * i + 1, "&gt;"); i += 3; } else if(ch == '\'') { sb.replace(i, i + 1,
		 * "&#39;"); i += 4; } else if(ch == '"') { sb.replace(i, i + 1, "&quot;"); i +=
		 * 5; } else if(ch == ' ') { sb.replace(i, i + 1, "&nbsp;"); i += 5; } else
		 * if(ch == '\r' || ch == '\n') { sb.replace(i, i + 1, "<BR>"); i += 3; } }
		 */
		return s1;
	}

	/**
	 * 
	 * toENC
	 * 
	 * @param text
	 * @return String
	 */
	public static String toENC(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		// StringBuffer sb = new StringBuffer(text);
		String[] iPatt = { "<", ">", "\'", "\"", " ", "\r", "\n" };
		String[] oPatt = { "&lt;", "&gt;", "&#39;", "&quot;", "&nbsp;", "<BR>", "<BR>" };

		String s1 = text;
		for (int i = 0; i < iPatt.length; i++) {
			s1 = s1.replaceAll(iPatt[i], oPatt[i]);
		}
		return s1;
	}

	/**
	 * 
	 * toDEC
	 * 
	 * @param text
	 * @return String
	 */
	public static String toDEC(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		// StringBuffer sb = new StringBuffer(text);
		String[] iPatt = { "<", ">", "\'", "\"", " ", "\r", "\n" };
		String[] oPatt = { "&lt;", "&gt;", "&#39;", "&quot;", "&nbsp;", "<BR>", "<BR>" };

		String s1 = text;
		for (int i = 0; i < iPatt.length; i++) {
			s1 = s1.replaceAll(oPatt[i], iPatt[i]);
		}
		return s1;
	}

	/**
	 * 
	 * toDECtagBR
	 * 
	 * @param text
	 * @return String
	 */
	public static String toDECtagBR(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		// StringBuffer sb = new StringBuffer(text);
		String[] iPatt = { "\r", "\n" };
		String[] oPatt = { "<BR>", "<BR>" };

		String s1 = text;
		for (int i = 0; i < iPatt.length; i++) {
			s1 = s1.replaceAll(oPatt[i], iPatt[i]);
		}
		return s1;
	}

	/**
	 * 
	 * formatHTMLCode2
	 * 
	 * @param text
	 * @return String
	 */
	public static String formatHTMLCode2(String text) {
		if (text == null || text.equals("")) {
			return "";
		}
		StringBuffer sb = new StringBuffer(text);
		int idx = 0;
		for (idx = 0; idx < sb.length(); idx++) {
			char ch = sb.charAt(idx);
			/*
			 * if(ch == '<') { sb.replace(i, i + 1, "&lt;"); i += 3; } else if(ch == '>') {
			 * sb.replace(i, i + 1, "&gt;"); i += 3; } else
			 */
			if (ch == '\'') {
				sb.replace(idx, idx + 1, "&#39;");
				idx += 4;
			} else {
				if (ch == '"') {
					sb.replace(idx, idx + 1, "&quot;");
					idx += 5;
				} /*
					 * else if(ch == ' ') { sb.replace(i, i + 1, "&nbsp;"); i += 5; } else if(ch ==
					 * '\r' && sb.charAt(i + 1) == '\n') { sb.replace(i, i + 2, "<BR>"); i += 3; }
					 */
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * formatHTMLCodeNoBR
	 * 
	 * @param text
	 * @return String
	 */
	public static String formatHTMLCodeNoBR(String text) {
		if (text == null || "".equals(text)) {
			return "";
		}
		int idx = 0;
		StringBuffer sb = new StringBuffer(text);
		for (idx = 0; idx < sb.length(); idx++) {
			char ch = sb.charAt(idx);
			if (ch == '\'') {
				sb.replace(idx, idx + 1, "&#39;");
				idx += 4;
			} else {
				if (ch == '"') {
					sb.replace(idx, idx + 1, "&quot;");
					idx += 5;
				}
			}
		}

		return sb.toString();
	}

	/**
	 * genSaveFileName : 파일 이름을 날짜와 덧붙여서 변경
	 * 
	 * @param originFilename
	 * @return String
	 */
	public static String genSaveFileName(String originFilename) {
		String fileName = "";
		String beforeExt = originFilename.substring(0, originFilename.lastIndexOf(".")).replaceAll(" ", "_");
		String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());

		Calendar calendar = Calendar.getInstance();
		fileName += beforeExt + "-";
		fileName += calendar.get(Calendar.YEAR) + "_";
		fileName += calendar.get(Calendar.MONTH) + 1 + "_";
		fileName += calendar.get(Calendar.DATE) + "_";
		fileName += calendar.get(Calendar.HOUR) + "_";
		fileName += calendar.get(Calendar.MINUTE) + "_";
		fileName += calendar.get(Calendar.SECOND) + "_";
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += extName;

		return fileName;
	}

	/**
	 * delLastUriParam : URI의 마지막 파라미터 제거
	 * 
	 * @param orginalUri
	 * @return String
	 */
	public static String delLastUriParam(String orginalUri) {
		String[] array = orginalUri.split("/");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length - 1; i++) {
			if (i == array.length - 2) {
				sb.append(array[i]);
			} else {
				sb.append(array[i] + "/");
			}
		}
		return sb.toString();
	}

	/**
	 * getMapFromRequest : GET 메소드 전용. Request 파라미터 Map을 추출하여 맵으로 민들기
	 * 
	 * @param request
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getMapFromRequest(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String[]> paramMap = request.getParameterMap();
		Set<String> rs = paramMap.keySet();
		Iterator<String> it = rs.iterator();

		while (it.hasNext()) {
			String mapKey = it.next();
			if (paramMap.get(mapKey)[0].contains(",") || mapKey.toUpperCase().contains("ARR")) {
				String[] strArr = paramMap.get(mapKey)[0].split(",");
				resultMap.put(mapKey, strArr);
			} else {
				resultMap.put(mapKey, paramMap.get(mapKey)[0]);
			}
		}
		if (resultMap.containsKey("page") && resultMap.containsKey("perPage")) {
			int page = Integer.parseInt(resultMap.get("page").toString());
			int perPage = Integer.parseInt(resultMap.get("perPage").toString());
			int offset = (page - 1) * perPage;
			resultMap.put("page", page);
			resultMap.put("perPage", perPage);
			resultMap.put("offset", offset);
		}
		return resultMap;
	}

	/**
	 * getMapFromRequestWithDevextreme : GET 메소드 전용. Request 파라미터 Map을 추출하여 맵으로 민들기.
	 * Devextreme의 파라미터도 받아와 Java에서도 읽을 수 있게 함.
	 * 
	 * @param request
	 * @return Map<String, Object>
	 * @throws ParseException
	 */
	public static Map<String, Object> getMapFromRequestWithDevextreme(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String[]> paramMap = request.getParameterMap();
		Set<String> rs = paramMap.keySet();
		Iterator<String> it = rs.iterator();

		while (it.hasNext()) {
			String mapKey = it.next();
			if (paramMap.get(mapKey)[0].contains(",") || mapKey.toUpperCase().contains("ARR")) {
				String[] strArr = paramMap.get(mapKey)[0].split(",");
				resultMap.put(mapKey, strArr);
			} else {
				resultMap.put(mapKey, paramMap.get(mapKey)[0]);
			}
		}
		if (resultMap.containsKey("sort")) {
			String[] sort = (String[]) resultMap.get("sort");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < sort.length; i++) {
				if (i != sort.length - 1) {
					sb.append(sort[i] + ",");
				} else {
					sb.append(sort[i]);
				}
			}
			String sortJson = sb.toString();
			Map<String, Object> sortMap = JsonUtil.getMapFromJsonString2(sortJson);
			resultMap.putAll(sortMap);
			resultMap.remove("sort");
		}
		return resultMap;
	}

	/**
	 * convertToFileFromMultipart : MultipartFile을 File로 컨버트
	 * 
	 * @param  mf
	 * @param  directoryPath
	 * @return File
	 * @throws IOException
	 * @throws CustomErrorException
	 */
	// http://blog.naver.com/PostView.nhn?blogId=stork838&logNo=220142660973&parentCategoryNo=&categoryNo=37&viewDate=&isShowPopularPosts=true&from=search
	public static File convertToFileFromMultipart(MultipartFile mf, String directoryPath)
			throws IOException ,CustomErrorException {
		File file = null;
		FileOutputStream fos = null;
		String docFileName = null;
		String docFileNameBak = null;
		try {
			docFileNameBak = mf.getOriginalFilename().toLowerCase();
			String ext = StringUtil.getExt(docFileNameBak);
			int size = (int) mf.getSize();
			if (size >= 1000000000) {
				throw new CustomErrorException("업로드 하고자 하는 파일의 용량이 1000MB 이상이면 안됩니다.");
			}
			if (/** 문서 확장자들 */
			!ext.equals("zip") && !ext.equals("doc") && !ext.equals("docx") && !ext.equals("hwp") && !ext.equals("xls")
					&& !ext.equals("xlsx") && !ext.equals("pdf")
					/** 이미지 확장자들 */
					&& !ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png") && !ext.equals("gif")
					&& !ext.equals("tiff") && !ext.equals("raw")) {
				throw new CustomErrorException(ext + "는 올바르지 않은 파일 형식입니다.");
			}
			docFileName = mf.getOriginalFilename();
			new File(directoryPath).mkdirs();
			file = new File(directoryPath + docFileName);
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(mf.getBytes());
		} catch (IOException e) {
			throw e;

		} finally {
			if (fos != null) {
				fos.close();
			}

		}
		return file;
	}

	/**
	 * convertToFileFromMultipartWithCustomName : MultipartFile을 File로 컨버트. 그리고 지정한
	 * 이름으로 파일 생성
	 * 
	 * @param  mf
	 * @param  directoryPath
	 * @return File
	 */
	// http://blog.naver.com/PostView.nhn?blogId=stork838&logNo=220142660973&parentCategoryNo=&categoryNo=37&viewDate=&isShowPopularPosts=true&from=search
	public static File convertToFileFromMultipartWithCustomName(MultipartFile mf, String directoryPath,
			String customName) throws IOException {
		File file = null;
		FileOutputStream fos = null;
		try {
			new File(directoryPath).mkdirs();
			file = new File(directoryPath + customName);
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(mf.getBytes());
		} catch (IOException e) {
			throw e;

		} finally {
			if (fos != null) {
				fos.close();
			}

		}
		return file;
	}

	/**
	 * convertFileToBlob : File을 SQL로 컨버트
	 * 
	 * @return Blob
	 * @throws SQLException
	 * @throws IOException
	 */
	// https://m.blog.naver.com/PostView.nhn?blogId=bb_&logNo=221317350676&proxyReferer=https:%2F%2Fwww.google.com%2F
	public static byte[] convertFileToBlob(File file) throws IOException, SQLException {

		Blob blob = null;
		FileInputStream inputStream = null;
		byte[] blobAsBytes = null;
		try {
			byte[] byteArray = new byte[(int) file.length()];
			inputStream = new FileInputStream(file);
			inputStream.read(byteArray);

			blob = new javax.sql.rowset.serial.SerialBlob(byteArray);

			int bloblength = (int) blob.length();
			blobAsBytes = blob.getBytes(1, bloblength);

		} catch (SerialException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				inputStream = null;
			} finally {
				inputStream = null;
			}
		}
		return blobAsBytes;
	}

	/**
	 * blobToByteArr : Bolb을 DB에 저장 할 byte[]로 컨버트
	 * 
	 * @param blob
	 * @return byte[]
	 */
	public static byte[] blobToByteArr(Blob blob) throws Exception {
		byte[] blobAsBytes = null;
		int bloblength = (int) blob.length();
		blobAsBytes = blob.getBytes(1, bloblength);

		return blobAsBytes;
	}

	/**
	 * sendHttpPostWithMultipart :HTTP Post 방식으로 URL 및 MultipartFile을 보낸다.
	 * 
	 * @param url
	 * @param imagePath
	 * @param paramStringMap
	 * @param paramMultipartMap
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> sendHttpPostWithMultipart(String url, String imagePath,
			Map<String, String> paramStringMap, Map<String, MultipartFile> paramMultipartMap) throws Exception {
		try {
			
			File tmpFile = null;
			MultipartFile tmpMf = null;
			String strResponse = null;
			Map<String, Object> requestResult = null;

			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			Set<String> paramStringKey = paramStringMap.keySet();
			Iterator<String> itForParamString = paramStringKey.iterator();
			while (itForParamString.hasNext()) {
				String mapKey = itForParamString.next();
				builder.addTextBody(mapKey, paramStringMap.get(mapKey), ContentType.APPLICATION_JSON);
			}

			Set<String> paramMultipartKey = paramMultipartMap.keySet();
			Iterator<String> itForParamMultipart = paramMultipartKey.iterator();
			while (itForParamMultipart.hasNext()) {
				String mapKey = itForParamMultipart.next();
				tmpMf = paramMultipartMap.get(mapKey);
				if (tmpMf.getSize() > 0) {
					tmpFile = CommonUtils.convertToFileFromMultipart(tmpMf, imagePath);
					FileInputStream fis = new FileInputStream(tmpFile);
					builder.addBinaryBody(mapKey, fis, ContentType.APPLICATION_OCTET_STREAM, tmpFile.getName())
							.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					fis.close();
				}
			}

			builder.setCharset(StandardCharsets.UTF_8);
			HttpEntity multipart = builder.build();
			httpPost.setEntity(multipart);
			HttpResponse response = null;
			
			response = httpClient.execute(httpPost);
			
			HttpEntity entity = response.getEntity();
			int code = 0;
			if (response.getStatusLine().getStatusCode() != -1) {
				code = response.getStatusLine().getStatusCode();
			}

			InputStreamReader isr = new InputStreamReader(entity.getContent());
			BufferedReader reader = new BufferedReader(isr);
			strResponse = reader.readLine();
			reader.close();
			isr.close();

			if (JsonUtil.getMapFromJsonString(strResponse) != null) {
				requestResult = JsonUtil.getMapFromJsonString(strResponse);
			}
			String errorCheck = (String) requestResult.get("error");
			if (code != 200) {
				if (errorCheck != null) {
					String errMsg = requestResult.get("error").toString();
					throw new CustomErrorException(errMsg);
				} else {
					throw new CustomErrorException("통신 에러가 발생하였습니다.");
				}
			}
			requestResult.put("code", code);
			return requestResult;
		} catch (CustomErrorException e) {
			throw new CustomErrorException(e.getMessage());
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException("클라이언트와 프로토콜 통신 에러가 발생하였습니다.");
		} catch (IOException e) {
			throw new IOException("통신 에러가 발생하였습니다.");
		}
	}

	/**
	 * sendHttpPostWithFile :HTTP Post 방식으로 URL 및 MultipartFile을 보낸다.
	 * 
	 * @param url
	 * @param paramStringMap
	 * @param paramFileMap
	 * @return Map<String, Object>
	 * @throws ParseException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws CustomErrorException
	 */
	public static Map<String, Object> sendHttpPostWithFile(String url, Map<String, String> paramStringMap,
			Map<String, File> paramFileMap)
			throws ParseException, ClientProtocolException, IOException, CustomErrorException {
		try {
			File tmpFile = null;
			String strResponse = null;
			Map<String, Object> requestResult = new HashMap<String, Object>();
			List<FileInputStream> fisList = new ArrayList<FileInputStream>();
			int fisListIdx = 0;

			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			Set<String> paramStringKey = paramStringMap.keySet();
			Iterator<String> itForParamString = paramStringKey.iterator();
			while (itForParamString.hasNext()) {
				String mapKey = itForParamString.next();
				builder.addTextBody(mapKey, paramStringMap.get(mapKey), ContentType.APPLICATION_JSON);
			}

			Set<String> paramFileKey = paramFileMap.keySet();
			Iterator<String> itForParamFile = paramFileKey.iterator();
			while (itForParamFile.hasNext()) {
				String mapKey = itForParamFile.next();
				tmpFile = paramFileMap.get(mapKey);
				if (tmpFile.getTotalSpace() > 0) {
					fisList.add(new FileInputStream(tmpFile));
					builder.addBinaryBody(mapKey, fisList.get(fisListIdx), ContentType.APPLICATION_OCTET_STREAM,
							tmpFile.getName()).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					fisListIdx += 1;
				}
			}

			builder.setCharset(StandardCharsets.UTF_8);
			HttpEntity multipart = builder.build();
			httpPost.setEntity(multipart);
			HttpResponse response = null;

			response = httpClient.execute(httpPost);

			// 전송 후에 FileInputStream 리스트를 불러와 차레대로 닫는다.
			for (FileInputStream fis : fisList) {
				fis.close();
			}

			HttpEntity entity = response.getEntity();
			int code = 0;
			if (response.getStatusLine().getStatusCode() != -1) {
				code = response.getStatusLine().getStatusCode();
			}

			InputStreamReader isr = new InputStreamReader(entity.getContent());
			BufferedReader reader = new BufferedReader(isr);
			strResponse = reader.readLine();
			reader.close();
			isr.close();

			if (JsonUtil.getMapFromJsonString(strResponse) != null) {
				requestResult = JsonUtil.getMapFromJsonString(strResponse);
			}
			String errorCheck = (String) requestResult.get("error");
			if (code != 200) {
				if (errorCheck != null) {
					String errMsg = requestResult.get("error").toString();
					throw new CustomErrorException(errMsg);
				} else {
					throw new CustomErrorException("통신 에러가 발생하였습니다.");
				}
			}
			requestResult.put("code", code);
			return requestResult;
		} catch (CustomErrorException e) {
			throw new CustomErrorException(e.getMessage());
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException("클라이언트와 프로토콜 통신 에러가 발생하였습니다.");
		} catch (IOException e) {
			throw new IOException("통신 에러가 발생하였습니다.");
		}
	}

	/**
	 * sendHttpPost :HTTP Post 방식으로 URL 및 맵을 보낸다
	 * 
	 * @param url
	 * @param paramStringMap
	 * @return Map<String, Object>
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 * @throws CustomErrorException
	 */
	public static Map<String, Object> sendHttpPost(String url, Map<String, String> paramStringMap)
			throws ClientProtocolException, IOException, ParseException, CustomErrorException {
		try {
			String strResponse = null;
			Map<String, Object> requestResult = new HashMap<String, Object>();

			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			Set<String> paramStringKey = paramStringMap.keySet();
			Iterator<String> itForParamString = paramStringKey.iterator();
			while (itForParamString.hasNext()) {
				String mapKey = itForParamString.next();
				builder.addTextBody(mapKey, paramStringMap.get(mapKey), ContentType.APPLICATION_JSON);
			}

			builder.setCharset(StandardCharsets.UTF_8);
			HttpEntity multipart = builder.build();
			httpPost.setEntity(multipart);
			HttpResponse response = null;

			response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			int code = 0;
			if (response.getStatusLine().getStatusCode() != -1) {
				code = response.getStatusLine().getStatusCode();
			}

			InputStreamReader isr = new InputStreamReader(entity.getContent());
			BufferedReader reader = new BufferedReader(isr);
			strResponse = reader.readLine();
			reader.close();
			isr.close();

			if (JsonUtil.getMapFromJsonString(strResponse) != null) {
				requestResult = JsonUtil.getMapFromJsonString(strResponse);
			}
			String errorCheck = (String) requestResult.get("error");
			if (code != 200) {
				if (errorCheck != null) {
					String errMsg = requestResult.get("error").toString();
					throw new CustomErrorException(errMsg);
				} else {
					throw new CustomErrorException("통신 에러가 발생하였습니다.");
				}
			}
			requestResult.put("code", code);
			return requestResult;
		} catch (CustomErrorException e) {
			throw new CustomErrorException(e.getMessage());
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException("클라이언트와 프로토콜 통신 에러가 발생하였습니다.");
		} catch (IOException e) {
			throw new IOException("통신 에러가 발생하였습니다.");
		}
	}

	/**
	 * toProperCase : 카멜케이스 toCamelCase 함수를 보조하는 역할로 camecase 만드는데 쓰인다.
	 * 
	 * @param s
	 * @param isCapital
	 * @return String
	 */
	public static String toProperCase(String s, boolean isCapital) {
		String rtnValue = "";

		if (isCapital) {
			rtnValue = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
		} else {
			rtnValue = s.toLowerCase();
		}

		return rtnValue;
	}

	/**
	 * toCamelCase : 문자열을 카멜 케이스로 만든다.
	 * 
	 * @param  s
	 * @return String
	 */
	public static String toCamelCase(String s) {
		String[] parts = s.split("_");
		StringBuilder camelCaseString = new StringBuilder();

		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			camelCaseString.append(toProperCase(part, (i != 0 ? true : false)));
		}

		return camelCaseString.toString();
	}
}
