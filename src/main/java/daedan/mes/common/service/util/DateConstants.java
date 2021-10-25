package daedan.mes.common.service.util;

import java.util.TimeZone;

/**
 * 일자관련상수선언
 * @author <a href="mailto:wckim@neozex.co.kr">김운철</a>
 * @since 2013. 6. 14.
 * @see
 * -----------------------------<br>
 * Modified By : <br>
 * Reason : <br>
 * refer : <br>
 * -----------------------------<br>
 */
public interface DateConstants {
	/** 1일을 밀리초로 환산한 값(86400000 = 24*60*60*1000) */
    public static final long MILLISECOND_OF_DAY = 24 * 60 * 60 * 1000;
    /** 현재로케일에서 그리니치기준시와의 차이를 밀리세컨드로 환산한 값. */
    public static final long GMT_RAW_OFFSET = TimeZone.getDefault().getRawOffset();

    /** 기본형식(yyyyMMddHHmmss)*/
    public static final String DF_BASE = "yyyyMMddHHmmss";
    /** 기본일자형식(yyyyMMdd) */
    public static final String DF_DATE = "yyyyMMdd";
    /** 기본시간형식(HHmmss) */
    public static final String DF_TIME = "HHmmss";

    /** 기본년월형식(yyyyMM) */
    public static final String DF_YYYYMM = "yyyyMM";
    /** 기본월일형식(MMdd) */
    public static final String DF_MMDD = "MMdd";
    /** 기본일형식(dd) */
    public static final String DF_DD = "dd";
    /** 기본시분형식(HHmm) */
    public static final String DF_HHMM = "HHmm";

    /** 기본년월일시분형식 (yyyyMMddHHmm) */
    public static final String DF_YYYYMMDDHHMM = "yyyyMMddHHmm";
    /** 기본년월일시분초형식 (yyyyMMddHHmm) */
    public static final String DF_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /** 간략일자형식 (yyMMdd) */
    public static final String DF_YYMMDD = "yyMMdd";

    /** 간략년도형식 (yyyy) */
    public static final String DF_YYYY = "yyyy";
    /** 간략월형식 (MM) */
    public static final String DF_MM = "MM";

    /** 계산서일련번호발급시사용(yyMM) */
    public static final String DF_YYMM = "yyMM";

    /** 년월일 Display 형식 (yyyy-MM-dd) */
    public static final String DF_DATE_DP = "yyyy-MM-dd";
    /** 년월 Display 형식 (yyyy-MM) */
    public static final String DF_YYYYMM_DP = "yyyy-MM";
    /** 년월일시분 Display 형식 (yyyy-MM-dd HH:mm)  */
    public static final String DF_YYYYMMDDHHMM_DP = "yyyy-MM-dd HH:mm";
    /** 년월일시분초 Display 형식 (yyyy-MM-dd HH:mm:ss)  */
    public static final String DF_YYYYMMDDHHMMSS_DP = "yyyy-MM-dd HH:mm:ss";
    /** 년월일 Display 형식 (yyyy/MM/dd) */
    public static final String DF_DATE_SLASH = "yyyy/MM/dd";
    /** 년월일 Display 형식 (yyyy/MM/dd) */
    public static final String DF_DATE_HANGUL = "yyyy년 MM월 dd일";
    /** 시분 Display 형식 (HH:mm) */
    public static final String DF_TIME_SLASH = "HH:mm";

    /** 초기일자({@link #DF_DATE}) : "00010101" */
    public static final String MINIMUM_DATE = "00010101";
    /** 최종일자({@link #DF_DATE}) : "99991231" */
    public static final String MAXIMUM_DATE = "99991231";
    /** 년도초기일자({@link #DF_MMDD}) : "0101" */
    public static final String MINIMUN_MMDD = "0101";
    /** 년도최종일자({@link #DF_MMDD}) : "1231" */
    public static final String MAXIMUM_MMDD = "1231";
    /** 월최소일수({@link #DF_DD}) : "01" */
    public static final String MINIMUN_DAY = "01";
    /** 월최대일수({@link #DF_DD}) : "31" */
    public static final String MAXIMUM_DAY = "31";
    /** 일자초기시간({@link #DF_TIME}) : "000000" */
    public static final String MINIMUN_TIME = "000000";
    /** 일자최종시간({@link #DF_TIME}) : "235959" */
    public static final String MAXIMUM_TIME = "235959";

    /** 요일(한글명) */
    public static final String[] DAY_OF_WEEKS_KOREAN = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
    /** 요일(축약 한글명) */
    public static final String[] DAY_OF_WEEKS_SHORT_KOREAN = { "일", "월", "화", "수", "목", "금", "토" };
    /** 요일(영문명) */
    public static final String[] DAY_OF_WEEKS_ENGLISH = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    /** 월 (숫자)*/
    public static String[] MONTHS_OF_YEAR = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
}
