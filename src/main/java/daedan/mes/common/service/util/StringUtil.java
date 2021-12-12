/**

 * @Description : 문자열 데이터 처리 관련 유틸리티


 * @Modification Information
 * 
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.01.13     박정규          최초 생성
 *   2009.02.13     이삼섭          내용 추가
 *
 * @author 공통 서비스 개발팀 박정규
 * @since 2009. 01. 13
 * @version 1.0
 * @see 
 * 
 */
package daedan.mes.common.service.util;
/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the ";License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS"; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


public class StringUtil {
    private static final String PADDING[];
	private static int INDEX_SIZE = 10;

    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각자음 마다 가지는 글자수
    //자음
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    static    {
        PADDING = new String[65535];
        PADDING[32] = "                                                                ";
    }


    public static ByteBuffer str_to_bb(String msg, Charset charset){
        return ByteBuffer.wrap(msg.getBytes(charset));
    }

    public static String bb_to_str(ByteBuffer buffer, Charset charset){
        byte[] bytes;
        if(buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }

    /**
     * 빈 문자열 <code>""</code>.
     */
    public static final String EMPTY = "";
    
    /**
     * <p>Padding을 할 수 있는 최대 수치</p>
     */
    // private static final int PAD_LIMIT = 8192;
    
    /**
     * <p>An array of <code>String</code>s used for padding.</p>
     * <p>Used for efficient space padding. The length of each String expands as needed.</p>
     */
    /*
	private static final String[] PADDING = new String[Character.MAX_VALUE];

	static {
		// space padding is most common, start with 64 chars
		PADDING[32] = "                                                                ";
	}	
     */
    

	public static String getReturnURL(String str) {	
	  StringBuffer buf_url = new StringBuffer();
	  StringTokenizer st = new StringTokenizer(str,"^");
	  if (st.hasMoreTokens())  {
	    buf_url.append(st.nextToken());
	    if (st.hasMoreTokens())  {
	      String params= st.nextToken();
	      StringTokenizer param = new StringTokenizer(params,"|");
	      int idx = 0;
	      while(param.hasMoreTokens())  {  
	        String para = param.nextToken();
	        if (++idx == 1) {
	          buf_url.append("?").append(para);
	        }
	        else {
	        buf_url.append("&").append(para);
	        }
	      }
	    }
	  }
	  return buf_url.toString();
	}
	
    
	
    /**
     * 문자열이 지정한 길이를 초과했을때 지정한길이에다가 해당 문자열을 붙여주는 메서드.
     * @param source 원본 문자열 배열
     * @param output 더할문자열
     * @param slength 지정길이
     * @return 지정길이로 잘라서 더할분자열 합친 문자열
     */
    public static String cutString(String source, String output, int slength) {
        String returnVal = null;
        if (source != null) {
            if (source.length() > slength) {
                returnVal = source.substring(0, slength) + output;
            } else
                returnVal = source;
        }
        return returnVal;
    }

    /**
     * 문자열이 지정한 길이를 초과했을때 해당 문자열을 삭제하는 메서드
     * @param source 원본 문자열 배열
     * @param slength 지정길이
     * @return 지정길이로 잘라서 더할분자열 합친 문자열
     */
    public static String cutString(String source, int slength) {
        String result = null;
        if (source != null) {
            if (source.length() > slength) {
                result = source.substring(0, slength);
            } else
                result = source;
        }
        return result;
    }    
    
    /**
     * <p>
     * String이 비었거나("") 혹은 null 인지 검증한다.
     * </p>
     * 
     * <pre>
     *  StringUtil.isEmpty(null)      = true
     *  StringUtil.isEmpty("")        = true
     *  StringUtil.isEmpty(" ")       = false
     *  StringUtil.isEmpty("bob")     = false
     *  StringUtil.isEmpty("  bob  ") = false
     * </pre>
     * 
     * @param str - 체크 대상 스트링오브젝트이며 null을 허용함
     * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우 
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    
    /**
     * <p>기준 문자열에 포함된 모든 대상 문자(char)를 제거한다.</p>
     *
     * <pre>
     * StringUtil.remove(null, *)       = null
     * StringUtil.remove("", *)         = ""
     * StringUtil.remove("queued", 'u') = "qeed"
     * StringUtil.remove("queued", 'z') = "queued"
     * </pre>
     *
     * @param str  입력받는 기준 문자열
     * @param remove  입력받는 문자열에서 제거할 대상 문자열
     * @return 제거대상 문자열이 제거된 입력문자열. 입력문자열이 null인 경우 출력문자열은 null
     */
    public static String remove(String str, char remove) {
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }
    
    /**
     * <p>문자열 내부의 콤마 character(,)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.removeCommaChar(null)       = null
     * StringUtil.removeCommaChar("")         = ""
     * StringUtil.removeCommaChar("asdfg,qweqe") = "asdfgqweqe"
     * </pre>
     *
     * @param str 입력받는 기준 문자열
     * @return " , "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeCommaChar(String str) {
        return remove(str, ',');
    }
    
    /**
     * <p>문자열 내부의 마이너스 character(-)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.removeMinusChar(null)       = null
     * StringUtil.removeMinusChar("")         = ""
     * StringUtil.removeMinusChar("a-sdfg-qweqe") = "asdfgqweqe"
     * </pre>
     *
     * @param str  입력받는 기준 문자열
     * @return " - "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeMinusChar(String str) {
        return remove(str, '-');
    }
        
    
    /**
     * 원본 문자열의 포함된 특정 문자열을 새로운 문자열로 변환하는 메서드
     * @param source 원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object 변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열
     */
    public static String replace(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr  = source;
        
        while (srcStr.indexOf(subject) >= 0) {
            preStr = srcStr.substring(0, srcStr.indexOf(subject));
            nextStr = srcStr.substring(srcStr.indexOf(subject) + subject.length(), srcStr.length());
            srcStr = nextStr;
            rtnStr.append(preStr).append(object);
        }
        rtnStr.append(nextStr);
        return rtnStr.toString();
    }

    /**
     * 원본 문자열의 포함된 특정 문자열 첫번째 한개만 새로운 문자열로 변환하는 메서드
     * @param source 원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object 변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열 / source 특정문자열이 없는 경우 원본 문자열
     */
    public static String replaceOnce(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        if (source.indexOf(subject) >= 0) {
            preStr = source.substring(0, source.indexOf(subject));
            nextStr = source.substring(source.indexOf(subject) + subject.length(), source.length());
            rtnStr.append(preStr).append(object).append(nextStr);
            return rtnStr.toString();
        } else {
            return source;
        }
    }

    /**
     * <code>subject</code>에 포함된 각각의 문자를 object로 변환한다.
     * 
     * @param source 원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object 변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열
     */
    public static String replaceChar(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr  = source;
        
        char chA;

        for (int i = 0; i < subject.length(); i++) {
            chA = subject.charAt(i);

            if (srcStr.indexOf(chA) >= 0) {
                preStr = srcStr.substring(0, srcStr.indexOf(chA));
                nextStr = srcStr.substring(srcStr.indexOf(chA) + 1, srcStr.length());
                srcStr = rtnStr.append(preStr).append(object).append(nextStr).toString();
            }
        }
        
        return srcStr;
    }  
    
    /**
     * <p><code>str</code> 중 <code>searchStr</code>의 시작(index) 위치를 반환.</p>
     *
     * <p>입력값 중 <code>null</code>이 있을 경우 <code>-1</code>을 반환.</p>
     *
     * <pre>
     * StringUtil.indexOf(null, *)          = -1
     * StringUtil.indexOf(*, null)          = -1
     * StringUtil.indexOf("", "")           = 0
     * StringUtil.indexOf("aabaabaa", "a")  = 0
     * StringUtil.indexOf("aabaabaa", "b")  = 2
     * StringUtil.indexOf("aabaabaa", "ab") = 1
     * StringUtil.indexOf("aabaabaa", "")   = 0
     * </pre>
     *
     * @param str  검색 문자열
     * @param searchStr  검색 대상문자열
     * @return 검색 문자열 중 검색 대상문자열이 있는 시작 위치 검색대상 문자열이 없거나 null인 경우 -1
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }    
    
    
    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>defaultStr</code>을 반환한다.
     * </p>
     * 
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("하이", "하이", null, "bar") = null
     * StringUtil.decode("하이", "하이  ", "foo", null) = null
     * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
     * StringUtil.decode("하이", "하이  ", "foo", "bar") = "bar"
     * </pre>
     * 
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @param defaultStr sourceStr와 compareStr의 값이 다를 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 defaultStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) {
        if (sourceStr == null && compareStr == null) {
            return returnStr;
        }
        
        if (sourceStr == null && compareStr != null) {
            return defaultStr;
        }

        if (sourceStr.trim().equals(compareStr)) {
            return returnStr;
        }

        return defaultStr;
    }

    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>sourceStr</code>을 반환한다.
     * </p>
     * 
     * <pre>
     * StringUtil.decode(null, null, "foo") = "foo"
     * StringUtil.decode("", null, "foo") = ""
     * StringUtil.decode(null, "", "foo") = null
     * StringUtil.decode("하이", "하이", "foo") = "foo"
     * StringUtil.decode("하이", "하이 ", "foo") = "하이"
     * StringUtil.decode("하이", "바이", "foo") = "하이"
     * </pre>
     * 
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 sourceStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr) {
        return decode(sourceStr, compareStr, returnStr, sourceStr);
    }    
    
    /**
     * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
     * @param object 원본 객체
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object) {
        String string = "";
        
        if (object != null) {
            string = object.toString().trim();
        }
        
        return string;
    }
    
    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static String nullConvert(Object src) {
	//if (src != null && src.getClass().getName().equals("java.math.BigDecimal")) {
	if (src != null && src instanceof BigDecimal) {
	    return ((BigDecimal)src).toString();
	}

	if (src == null || src.equals("null")) {
	    return "";
	} else {
	    return ((String)src).trim();
	}
    }
    
    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static String nullConvert(String src) {

	if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
	    return "";
	} else {
	    return src.trim();
	}
    }	
	
    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;0&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;0&quot;로 바꾼 String 값.
     *</pre>
     */
    public static int zeroConvert(Object src) {

	if (src == null || src.equals("null")) {
	    return 0;
	} else {
	    return Integer.parseInt(((String)src).trim());
	}
    }

    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static int zeroConvert(String src) {

	if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
	    return 0;
	} else {
	    return Integer.parseInt(src.trim());
	}
    }
	
    /**
     * <p>문자열에서 {@link Character#isWhitespace(char)}에 정의된 
     * 모든 공백문자를 제거한다.</p>
     *
     * <pre>
     * StringUtil.removeWhitespace(null)         = null
     * StringUtil.removeWhitespace("")           = ""
     * StringUtil.removeWhitespace("abc")        = "abc"
     * StringUtil.removeWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str  공백문자가 제거도어야 할 문자열
     * @return the 공백문자가 제거된 문자열, null이 입력되면 <code>null</code>이 리턴
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        
        return new String(chs, 0, count);
    }
    	
    /**
     * Html 코드가 들어간 문서를 표시할때 태그에 손상없이 보이기 위한 메서드
     * 
     * @param strString
     * @return HTML 태그를 치환한 문자열
     */
    public static String checkHtmlView(String strString) {
	String strNew = "";

	try {
	    StringBuffer strTxt = new StringBuffer("");

	    char chrBuff;
	    int len = strString.length();

	    for (int i = 0; i < len; i++) {
		chrBuff = (char)strString.charAt(i);

		switch (chrBuff) {
		case '<':
		    strTxt.append("&lt;");
		    break;
		case '>':
		    strTxt.append("&gt;");
		    break;
		case '"':
		    strTxt.append("&quot;");
		    break;
		case 10:
		    strTxt.append("<br>");
		    break;
		case ' ':
		    strTxt.append("&nbsp;");
		    break;
		//case '&' :
		    //strTxt.append("&amp;");
		    //break;
		default:
		    strTxt.append(chrBuff);
		}
	    }

	    strNew = strTxt.toString();

	} catch (Exception ex) {
	    return null;
	}

	return strNew;
    }
	
	
    /**
     * 문자열을 지정한 분리자에 의해 배열로 리턴하는 메서드.
     * @param source 원본 문자열
     * @param separator 분리자
     * @return result 분리자로 나뉘어진 문자열 배열
     */
    public static String[] split(String source, String separator) throws NullPointerException {
        String[] returnVal = null;
        int cnt = 1;

        int index = source.indexOf(separator);
        int index0 = 0;
        while (index >= 0) {
            cnt++;
            index = source.indexOf(separator, index + 1);
        }
        returnVal = new String[cnt];
        cnt = 0;
        index = source.indexOf(separator);
        while (index >= 0) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);
        
        return returnVal;
    }
    
    /**
     * <p>{@link String#toLowerCase()}를 이용하여 소문자로 변환한다.</p>
     *
     * <pre>
     * StringUtil.lowerCase(null)  = null
     * StringUtil.lowerCase("")    = ""
     * StringUtil.lowerCase("aBc") = "abc"
     * </pre>
     *
     * @param str 소문자로 변환되어야 할 문자열
     * @return 소문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        
        return str.toLowerCase();
    }
    
    /**
     * <p>{@link String#toUpperCase()}를 이용하여 대문자로 변환한다.</p>
     *
     * <pre>
     * StringUtil.upperCase(null)  = null
     * StringUtil.upperCase("")    = ""
     * StringUtil.upperCase("aBc") = "ABC"
     * </pre>
     *
     * @param str 대문자로 변환되어야 할 문자열
     * @return 대문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        
        return str.toUpperCase();
    }
    
    /**
     * <p>입력된 String의 앞쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.stripStart(null, *)          = null
     * StringUtil.stripStart("", *)            = ""
     * StringUtil.stripStart("abc", "")        = "abc"
     * StringUtil.stripStart("abc", null)      = "abc"
     * StringUtil.stripStart("  abc", null)    = "abc"
     * StringUtil.stripStart("abc  ", null)    = "abc  "
     * StringUtil.stripStart(" abc ", null)    = "abc "
     * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }
        
        return str.substring(start);
    }


    /**
     * <p>입력된 String의 뒤쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.stripEnd(null, *)          = null
     * StringUtil.stripEnd("", *)            = ""
     * StringUtil.stripEnd("abc", "")        = "abc"
     * StringUtil.stripEnd("abc", null)      = "abc"
     * StringUtil.stripEnd("  abc", null)    = "  abc"
     * StringUtil.stripEnd("abc  ", null)    = "abc"
     * StringUtil.stripEnd(" abc ", null)    = " abc"
     * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }
        
        return str.substring(0, end);
    }

    /**
     * <p>입력된 String의 앞, 뒤에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     * 
     * <pre>
     * StringUtil.strip(null, *)          = null
     * StringUtil.strip("", *)            = ""
     * StringUtil.strip("abc", null)      = "abc"
     * StringUtil.strip("  abc", null)    = "abc"
     * StringUtil.strip("abc  ", null)    = "abc"
     * StringUtil.strip(" abc ", null)    = "abc"
     * StringUtil.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String strip(String str, String stripChars) {
	if (isEmpty(str)) {
	    return str;
	}

	String srcStr = str;
	srcStr = stripStart(srcStr, stripChars);
	
	return stripEnd(srcStr, stripChars);
    }

    /**
     * 문자열을 지정한 분리자에 의해 지정된 길이의 배열로 리턴하는 메서드.
     * @param source 원본 문자열
     * @param separator 분리자
     * @param arraylength 배열 길이
     * @return 분리자로 나뉘어진 문자열 배열
     */
    public static String[] split(String source, String separator, int arraylength) throws NullPointerException {
        String[] returnVal = new String[arraylength];
        int cnt = 0;
        int index0 = 0;
        int index = source.indexOf(separator);
        while (index >= 0 && cnt < (arraylength - 1)) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);
        if (cnt < (arraylength - 1)) {
            for (int i = cnt + 1; i < arraylength; i++) {
                returnVal[i] = "";
            }
        }
        
        return returnVal;
    }

    /**
     * 문자열 A에서 Z사이의 랜덤 문자열을 구하는 기능을 제공 시작문자열과 종료문자열 사이의 랜덤 문자열을 구하는 기능
     * 
     * @param startChr
     *            - 첫 문자
     * @param endChr
     *            - 마지막문자
     * @return 랜덤문자
     * @see
     */
    public static String getRandomStr(char startChr, char endChr) {

	int randomInt;
	String randomStr = null;

	// 시작문자 및 종료문자를 아스키숫자로 변환한다.
	int startInt = Integer.valueOf(startChr);
	int endInt = Integer.valueOf(endChr);

	// 시작문자열이 종료문자열보가 클경우
	if (startInt > endInt) {
	    throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
	}

	try {
	    // 랜덤 객체 생성
	    SecureRandom rnd = new SecureRandom();

	    do {
		// 시작문자 및 종료문자 중에서 랜덤 숫자를 발생시킨다.
		randomInt = rnd.nextInt(endInt + 1);
	    } while (randomInt < startInt); // 입력받은 문자 'A'(65)보다 작으면 다시 랜덤 숫자 발생.

	    // 랜덤 숫자를 문자로 변환 후 스트링으로 다시 변환
	    randomStr = (char)randomInt + "";
	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new RuntimeException(e);
	}

	// 랜덤문자열를 리턴
	return randomStr;
    }

    /**
     * 문자열을 다양한 문자셋(EUC-KR[KSC5601],UTF-8..)을 사용하여 인코딩하는 기능 역으로 디코딩하여 원래의 문자열을
     * 복원하는 기능을 제공함 String temp = new String(문자열.getBytes("바꾸기전 인코딩"),"바꿀 인코딩");
     * String temp = new String(문자열.getBytes("8859_1"),"KSC5601"); => UTF-8 에서
     * EUC-KR
     * 
     * @param srcString
     *            - 문자열
     * @param srcCharsetNm
     *            - 원래 CharsetNm
     * @param cnvrCharsetNm
     *            - CharsetNm
     * @return 인(디)코딩 문자열
     * @see
     */
    public static String getEncdDcd(String srcString, String srcCharsetNm, String cnvrCharsetNm) {

	String rtnStr = null;

	if (srcString == null)
	    return null;

	try {
	    rtnStr = new String(srcString.getBytes(srcCharsetNm), cnvrCharsetNm);
	} catch (UnsupportedEncodingException e) {
	    rtnStr = null;
	}

	return rtnStr;
    }

/**
     * 특수문자를 웹 브라우저에서 정상적으로 보이기 위해 특수문자를 처리('<' -> & lT)하는 기능이다
     * @param 	srcString 		- '<' 
     * @return 	변환문자열('<' -> "&lt"
     * @see
     */
    public static String getSpclStrCnvr(String srcString) {

	String rtnStr = null;

	try {
	    StringBuffer strTxt = new StringBuffer("");

	    char chrBuff;
	    int len = srcString.length();

	    for (int i = 0; i < len; i++) {
		chrBuff = (char)srcString.charAt(i);

		switch (chrBuff) {
		case '<':
		    strTxt.append("&lt;");
		    break;
		case '>':
		    strTxt.append("&gt;");
		    break;
		case '&':
		    strTxt.append("&amp;");
		    break;
		default:
		    strTxt.append(chrBuff);
		}
	    }

	    rtnStr = strTxt.toString();

	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new RuntimeException(e);
	}

	return rtnStr;
    }

    /**
     * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
     * 
     * @param
     * @return Timestamp 값

     * @see
     */
    public static String getTimeStamp() {

	String rtnStr = null;

	// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
	String pattern = "yyyyMMddhhmmssSSS";

	try {
	    SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
	    Timestamp ts = new Timestamp(System.currentTimeMillis());

	    rtnStr = sdfCurrent.format(ts.getTime());
	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new RuntimeException(e);
	}

	return rtnStr;
    }
    
    /**
     * html의 특수문자를 표현하기 위해
     * 
     * @param srcString
     * @return String
     * @exception Exception
     * @see
     */    
    public static String getHtmlStrCnvr(String srcString) {
    	
    	String tmpString = srcString;

		try {
		    tmpString = tmpString.replaceAll("&lt;", "<");
		    tmpString = tmpString.replaceAll("&gt;", ">");
		    tmpString = tmpString.replaceAll("&amp;", "&");
		    tmpString = tmpString.replaceAll("&nbsp;", " ");
		    tmpString = tmpString.replaceAll("&apos;", "\'");
		    tmpString = tmpString.replaceAll("&quot;", "\"");
		} catch (Exception ex) {
		    //ex.printStackTrace();
		    throw new RuntimeException(ex);
		}

		return tmpString;

    }  
    
    /**
     * html의 특수문자를 표현하기 위해
     * 
     * @param srcString
     * @return String
     * @exception Exception
     * @see
     */    
    public static String getHtmlStr(String srcString) {
    	try {
    		srcString = srcString.replaceAll("<", "&lt;" );
    		srcString = srcString.replaceAll(">", "&gt;" );
    		srcString = srcString.replaceAll("&", "&amp;" );
    		srcString = srcString.replaceAll(" ", "&nbsp;" );
    		srcString = srcString.replaceAll("\'", "&apos;" );
		    srcString = srcString.replaceAll("\"", "&quot;" );
    	} catch (Exception ex) {
    		//ex.printStackTrace();
    		throw new RuntimeException(ex);
    	}

    	return srcString;

    }    
    
    
    /**
     * <p>날짜 형식의 문자열 내부에 마이너스 character(-)를 추가한다.</p>
     *
     * <pre>
     *   StringUtil.addMinusChar("20100901") = "2010-09-01"
     * </pre>
     *
     * @param date  입력받는 문자열
     * @return " - "가 추가된 입력문자열
     */
    public static String addMinusChar(String date) {
	if (date.length() == 8)
	    return date.substring(0, 4).concat("-").concat(date.substring(4, 6)).concat("-").concat(date.substring(6, 8));
	else
	    return "";
    }
    
    /**
     * 문자열이 null 이거나 공백이면 공백을 리턴한다.
     * @param str
     * @return
     */
    public static String defaultIfEmpty(String str)  throws Exception   {
         str = (str != null && str != "") ? str : "";
         str = str.replace("null", "");
         return str;
    }

    /**
     * 문자열이 null 이거나 공백이면 지정된 문자를 리턴한다.
     * @param str
     * @param defaultStr
     * @return
     */
    public static String defaultIfEmpty(String str, String defaultStr) throws Exception   {
        return (str != null && str != "") ? str : defaultStr;
    }

    /**
     * 문자열을 int로 변환, 숫자가 아니면 0을 반환
     * @param str
     * @return 
     */
    public static int defaultInt(String str)  throws Exception   {
    	return defaultInt(str, 0);
    }
    

    /**
     * 문자열을 int로 변환, 숫자가 아니면 0을 반환
     * @param str
     * @return 
     */
    public static int defaultInt(int str)   throws Exception   {
    	return  defaultInt(Integer.toString(str)) ;
    }
      
    
    /**
     * 문자열을 int로 변환, 숫자가 아니면 지정한 값을 반환
     * @param str
     * @param defaultVal
     * @return
     */
    public static int defaultInt(String str, int defaultVal)    {
    	try
    	{
    		str = (str == null) ? "0" : str;
    		return Integer.parseInt(str);
    	}
    	catch (NumberFormatException e)
    	{
    		return defaultVal;
    	}
    }
    
    /**
     * 문자열을 double로 변환, 숫자가 아니면 0.0을 반환
     * @param str
     * @return
     */
    public static double defaultDouble(String str) throws Exception   {
    	return defaultDouble(str, 0.0);
    }
    
    /**
     * 문자열을 double로 변환, 숫자가 아니면 지정된 값을 반환
     * @param str
     * @param defaultVal
     * @return
     */
    public static double defaultDouble(String str, double defaultVal) throws Exception   {
    	try    	{
    		return Double.parseDouble(str);
    	}
    	catch (NumberFormatException e)    	{
    		return defaultVal;
    	}
    }
    
    /**
     * 문자열을 boolean으로 변환, 
     * @return "false","0" : false
     *         "true","1" : true
     *         유효한 값이 아니면 false 반환  
     */
    public static boolean defaultBoolean(String str)  throws Exception   {
    	return Boolean.getBoolean(str);
    }
    
    
    
    /**
     *@author : Kang Moo Jin At 08.09.17
     *@version aistalk 1.0
     *@param map :

     *@return  String : 
     *----------------------------------------------------------------------------
     * Modified By     :  
     * Reason          : 
     *-----------------------------------------------------------------------------  
     * Remark          :
     *-----------------------------------------------------------------------------  
	*/
	public static String genPageIndex(Map<String,Object> map) {
		StringBuffer pageIndexString = new StringBuffer();

		int totalCount  = Integer.parseInt(map.get("dataCnt").toString());
		int pageSize    = Integer.parseInt(map.get("dataCnt").toString());
		int currentPage = Integer.parseInt(map.get("pageNo").toString());
		
		int pageCount       = (totalCount%pageSize)==0 ? totalCount/pageSize : totalCount/pageSize+1; // 전체 인덱스페이지 갯수
		int pageIndexPageNo = (currentPage%pageSize)==0 ? currentPage/pageSize : currentPage/pageSize+1; // 현재 페이지에 해당하는 인덱스페이지 NO
		int pageIndexFrom   = (currentPage-1)-((currentPage-1)%INDEX_SIZE)+1;
		//int pageIndexTo   = pageCount > INDEX_SIZE * pageIndexPageNo ? INDEX_SIZE * pageIndexPageNo : pageCount; 	 //Remarked By KMJ AT 08.09.17 
		int pageIndexTo     = ( pageCount > INDEX_SIZE * pageIndexPageNo) ?  +  pageIndexFrom + INDEX_SIZE -1 : pageCount;; //Added By KMJ AT 08.09.17
		pageIndexTo = (pageIndexTo > pageCount) ? pageCount : pageIndexTo; //Added By KMJ AT 08.09.17
		int prev_ix = pageIndexFrom-INDEX_SIZE;
		
		System.out.println("PageControl-----------------------" );
		System.out.println("INDEX_SIZE    : " + INDEX_SIZE      );
		System.out.println("totalCount    : " + totalCount      );
		System.out.println("pageSize      : " + pageSize        );
		System.out.println("currentPage   : " + currentPage     );
		System.out.println("pageCount     : " + pageCount       );
		System.out.println("pageIndexNo   : " + pageIndexPageNo );
		System.out.println("pageIndexFrom : " + pageIndexFrom   );
		System.out.println("pageIndexTo   : " + pageIndexTo     );

		pageIndexString.append("<div class=\"paginate\"> ");
		if ( (totalCount  * pageIndexTo ) > 0 ) {
			
			
			pageIndexString.append("<a href=\"JavaScript:goPage(1)\" class=\"prev_page\"><span>맨처음</span></a> ");
				
			if (currentPage > (pageIndexTo-INDEX_SIZE)) {
				pageIndexString.append("<a href=\"JavaScript:goPage(")
				               .append(currentPage - INDEX_SIZE)
				               .append(")\" class=\"prev\"><span>이전</span></a> ");
			}
			else {
				pageIndexString.append("<a href=\"JavaScript:goPage(")
	               .append(1)
	               .append(")\" class=\"prev\"><span>이전</span></a> ");
			}

			for(int pageIndex=pageIndexFrom;pageIndex<=pageIndexTo;pageIndex++)	{
				if (pageIndex==currentPage) {
					if(pageIndex==pageIndexFrom) {
						pageIndexString.append(" <strong class=\"first\">&nbsp;")
										.append(pageIndex)
										.append("&nbsp;&nbsp;</strong>");
					}
					else {
						pageIndexString.append("<strong>&nbsp;")
										.append(pageIndex)
										.append("&nbsp;&nbsp;</strong>");
					}
				}
				else
				{
					pageIndexString.append("<a ");
					if(pageIndex==pageIndexFrom) {
						pageIndexString.append("class=\"first\" ");
					}
					
					pageIndexString.append("href=\"JavaScript:goPage(")
							.append(pageIndex)
							.append(")\">&nbsp;")
							.append(pageIndex)
							.append("</a> ");
					
				}
			
			}
			
			if (pageCount < (pageIndexFrom+INDEX_SIZE))	{
				pageIndexString.append("<a href=\"JavaScript:goPage(")
							   .append(pageCount)
    	                       .append(")\" class=\"next\"><span>다음</span></a> ");
			}
			else {
				pageIndexString.append("<a href=\"JavaScript:goPage(")
	               	           .append(pageIndexFrom+INDEX_SIZE)
	                           .append(")\" class=\"next\"><span>다음</span></a> ");
			}
				pageIndexString.append("<a href=\"JavaScript:goPage(")
							   .append(pageCount)
							   .append(")\" class=\"next_page\"><span>마지막</span></a> ");
		}
		pageIndexString.append("</div>\n");
		return pageIndexString.toString();

		
	}
	
	public static String clobToString(Clob clb)     {
		String szResult = "";
		
		try {
			if (clb == null) return  "";
			Reader reader = clb.getCharacterStream();
			StringBuffer out = new StringBuffer();
			char[] buff = new char[1024];
			int nchars = 0;

			while ((nchars = reader.read(buff)) > 0) {
				out.append(buff, 0, nchars);
			}
			szResult = out.toString();
		}
		catch (Exception e) {
			System.out.println("CLOB -> STRING 변환오류 발생.msg = " + e.getCause());
			e.printStackTrace();
		}
		return szResult;
	}
	
	public static Map<String,Object> voToMap(Object obj){
        try {
            //Field[] fields = obj.getClass().getFields(); //private field는 나오지 않음.
            Field[] fields = obj.getClass().getDeclaredFields();
            
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
            for(int idx = 0; idx <= fields.length-1;idx++){
                fields[idx].setAccessible(true);
                //System.out.println("필드명 = " + fields[idx].getName() + "/필드값 = " + fields[idx].get(obj));//KILL
                resultMap.put(fields[idx].getName(), fields[idx].get(obj));
            }

            fields = obj.getClass().getSuperclass().getDeclaredFields();
            for(int idx = 0; idx <= fields.length-1;idx++){
                fields[idx].setAccessible(true);
                //System.out.println("필드명 = " + fields[idx].getName() + "/필드값 = " + fields[idx].get(obj));//KILL
                resultMap.put(fields[idx].getName(), fields[idx].get(obj));
            }
            
            return resultMap;
        } 
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        } 
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Map을 Vo로 변환
     * @param map
     * @param obj
     * @return
     */
    public static Object mapToVo(Map<String,Object> map, Object obj) {
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while (itr.hasNext()) {
            keyAttribute = (String) itr.next();
            methodString = setMethodString + keyAttribute.substring(0, 1).toUpperCase() + keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methodString.equals(methods[i].getName())) {
                    try {
                        methods[i].invoke(obj, map.get(keyAttribute));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

    public static int convertPageNo(Map<String,Object> map) {
        int pageNo = 0;
        int pageSz = 0;
        String resource = "config/mes.properties";
        Properties properties = null;
        try {
            List<Map<String,Object>> list = null;
            int count = 0;
            properties = new Properties();

            pageNo = (int) map.get("pageNo");
            pageSz = (int) map.get("pageSz");
        }
        catch (NullPointerException ne) {
            try {
                Reader reader = Resources.getResourceAsReader(resource);
                properties.load(reader);
                pageSz = Integer.parseInt(properties.getProperty("max_page_sz"));
                pageNo = 1;
            }
            catch (IOException ie) {
                pageSz = 1000;
                pageNo = 1;
            }
        }
        pageNo = (pageNo-1) * pageSz;
        return pageNo;
    }

    public int[] bytearrayTointarray(byte[] barray)
    {
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray)
            iarray[i++] = b & 0xff;
        return iarray;
    }

    public static String byteArrayToHexString(byte[] bytes){

        StringBuilder sb = new StringBuilder();

        for(byte b : bytes){
            sb.append(String.format("%02X", b&0xff));
        }
        return sb.toString();
    }

    public static String fixLenString(String s, int size) {
        String result = "";
        StringBuffer buf = new StringBuffer();
        if (size > 0) {
            buf.setLength(0);
            while (s.length() <= --size) {
                buf.append("0");
            }
            buf.append(s);
        }
        result = buf.toString();
        return result.toUpperCase();
    }
    public static String stringToHex(String s, int size) {
        String result = "";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            result += String.format("%02X", (int) s.charAt(i));
        }
        return result.toUpperCase();
    }

    // 헥사 접두사 "0x" 붙이는 버전
    public static String stringToHex0x(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result += String.format("0x%02X", (int) s.charAt(i));
        }

        return result;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static int toDecimal(char hexChar) {
        int result = 0;
        switch(hexChar) {
            case 'A' : result = 10; break;
            case 'B' : result = 11; break;
            case 'C' : result = 12; break;
            case 'D' : result = 13; break;
            case 'E' : result = 14; break;
            case 'F' : result = 15; break;
            default : result = Integer.parseInt(Character.toString(hexChar));
        }
        return result;
    }

    public static int hexToDecimal(String hexStr) {
        char hexArr[] = hexStr.toCharArray();

        int result =  StringUtil.toDecimal(hexArr[0]) * 16 + StringUtil.toDecimal(hexArr[1]);
        return result;
    }

    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }
        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }




    public static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException("Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    public static String getCrc(String chkStr) {
        //이하 CRC 처리 시작
        byte[] bytes = new BigInteger(chkStr, 16).toByteArray();
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        String crc = Integer.toHexString(CRC);
        if (crc.length() == 2) {
            crc = "00" + crc;
        } else if (crc.length() == 3) {
            crc = "0" + crc;
        }
        if (crc.length() == 1) {
            crc = "000" + crc;
        }
        crc = crc.substring(2, 4) + crc.substring(0, 2);
        return crc;
    }

    public static String getUnixTimeToDate(Long timestamp ){
        Date date = new Date(timestamp*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    /**
     * 해당 문자가 INITIAL_SOUND인지 검사.
     * @param searchar
     * @return
     */
    private static boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }

    /**
     * 해당 문자의 자음을 얻는다.
     *
     * @param c 검사할 문자
     * @return
     */
    private static char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    /**
     * 해당 문자가 한글인지 검사
     * @param c 문자 하나
     * @return
     */
    private static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }

    /** * 검색을 한다. 초성 검색 완벽 지원함.
     * @param value : 검색 대상 ex> 초성검색합니다
     * @param search : 검색어 ex> ㅅ검ㅅ합ㄴ
     * @return 매칭 되는거 찾으면 true 못찾으면 false. */
    public static boolean matchString(String value, String search){
        int t = 0;
        int seof = value.length() - search.length();
        int slen = search.length();
        if(seof < 0)
            return false; //검색어가 더 길면 false를 리턴한다.
        for(int i = 0;i <= seof;i++){
            t = 0;
            while(t < slen){
                if(isInitialSound(search.charAt(t))==true && isHangul(value.charAt(i+t))){
                    //만약 현재 char이 초성이고 value가 한글이면
                    if(getInitialSound(value.charAt(i+t))==search.charAt(t))
                        //각각의 초성끼리 같은지 비교한다
                        t++;
                    else
                        break;
                } else {
                    //char이 초성이 아니라면
                    if(value.charAt(i+t)==search.charAt(t))
                        //그냥 같은지 비교한다.
                        t++;
                    else
                        break;
                }
            }
            if(t == slen)
                return true; //모두 일치한 결과를 찾으면 true를 리턴한다.
        }
        return false; //일치하는 것을 찾지 못했으면 false를 리턴한다.
    }
    public static JSONObject mapToJson(Map<String, Object> map) {

        JSONObject json = new JSONObject();

        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                String key = entry.getKey();

                Object value = entry.getValue();

                // json.addProperty(key, value);

                json.put(key, value);

            }
        } catch (JSONException e) {
            System.out.println(e.toString());
        }

        return json;
    }

    public static String getExt(String FileName) {
        int idx = FileName.lastIndexOf(".");
        String Ext = FileName.substring(idx + 1).toLowerCase();
        return Ext;
    }


    public static Object maptovo(Map<String,Object> map,Object obj) {
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        Iterator itr = map.keySet().iterator();

        while (itr.hasNext()) {
            keyAttribute = (String) itr.next();
            methodString = setMethodString + keyAttribute.substring(0, 1).toUpperCase() + keyAttribute.substring(1);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methodString.equals(methods[i].getName())) {
                    try {
                        methods[i].invoke(obj, map.get(keyAttribute));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

    public static boolean chkSaupNo(String saupNo) {
        final int[] LOGIC_NUM = {1, 3, 7, 1, 3, 7, 1, 3, 5, 1};

        if (!isNumeric(saupNo) || saupNo.length() != 10) return false;
        int sum = 0;
        int j = -1;
        for (int i = 0; i < 9; i++) {
            j = Character.getNumericValue(saupNo.charAt(i));
            sum += j * LOGIC_NUM[i];
        }
        sum += (int) (Character.getNumericValue(saupNo.charAt(8)) * 5 / 10);
        int checkNum = (10 - sum % 10) % 10;
        return (checkNum == Character.getNumericValue(saupNo.charAt(9)));
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
