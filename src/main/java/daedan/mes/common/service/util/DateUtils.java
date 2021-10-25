package daedan.mes.common.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 일자와 시간에 관련된 유틸리티
 * <p>
 * 날짜(순우리말)와 일자(日字)는 같은 말이며 여기에서는 일자로 통일한다. 그러나 날짜로 하여도 무방하다.
 * </p><p>
 * <b>용도:</b>
 * <ul>
 * <li>현재일자, 현재일자문자열
 * <li>일자비교
 * <li>일자형식변환
 * </ul>
 * </p>
 * @author <a href="mailto:wckim@neozex.co.kr">김운철</a>
 * @since 2013. 6. 14.
 * @see
 * -----------------------------<br>
 * Modified By : <br>
 * Reason : <br>
 * refer : <br>
 * -----------------------------<br>
 */
public abstract class DateUtils implements DateConstants {

	// -------------------------------------------------------------------------
    // 현재 날짜 및 시간
    // -------------------------------------------------------------------------

    /**
     * 기본일자형식({@link DateConstants#DF_DATE})으로 된 현재날짜를 반환한다.
     * @return 현재날짜
     */
    public static Date getCurrentDate() {
        return getCurrentDateTime(DF_DATE); // 시간값은 무시됨
    }

    /**
     * 입력형식으로 된  현재날짜를 반환한다.
     * @param dateFormat 날짜형식
     * @return 현재날짜
     */
    public static Date getCurrentDate(String dateFormat) {
        return getCurrentDateTime(dateFormat);
    }

    /**
     * 현재시각을 반환한다.
     * @return 현재일시
     */
    public static Date getCurrentDateTime() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 기본일자형식({@link DateConstants#DF_BASE})으로 된 현재일시를 반환한다.
     * @return 현재일시
     */
    public static Date getCurrentBaseDateTime() {
        return getCurrentDateTime(DF_BASE); // 밀리세컨드는 무시됨.
    }

    /**
     * 입력형식으로 된  현재날짜 또는 일시를 반환한다.
     * @param dateFormat 날짜형식
     * @return 현재날짜
     */
    public static Date getCurrentDateTime(String dateFormat) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(sdf.format(currentDate));
        }
        catch (Exception e) {
            return currentDate;
        }
    }

    /**
     * 입력받은 <code>java.util.Date</code>의 해당월 날짜를 1일로 설정하여 반환
     * @param date 설정할 날짜
     * @return 입력된 날짜의 해당월 첫번째 날짜
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * 입력받은 <code>java.util.Date</code>의 해당월 마지막 날짜를 설정하여 반환
     * @param date 설정할 날짜
     * @return 입력된 날짜의 해당월 마지막 날짜
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    /**
     * 입력받은 <code>java.util.Date</code>를 1월 1일로 설정하여 반환
     * @param date 설정할 날짜
     * @return 입력일자의 1월 1일 날짜
     */
    public static Date getFirstDateOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        return calendar.getTime();
    }

    /**
     * 입력받은 <code>java.util.Date</code>를 12월 31일로 설정하여 반환
     * @param date 설정할 날짜
     * @return 입력일자의 12월 31일 날짜
     */
    public static Date getLastDateOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);

        return calendar.getTime();
    }

    // -------------------------------------------------------------------------
    // 문자열 --> 날짜 변경
    // -------------------------------------------------------------------------

    /**
     * 기본일자형식({@link DateConstants#DF_DATE})의 날짜문자열을 날짜로 변환하여 반환한다.
     * <p>
     * 내부에서 {@link #toDate(String, String) toDate(dateString, DF_DATE)} 를 호출한다.
     * @param dateString 날짜문자열
     * @return 날짜
     */
    public static Date toDate(String dateString) {
        return toDate(dateString, DF_DATE);
    }

    /**
     * format형태인 String데이타를 java.util.Date로 변환
     * <p>
     * 다음과 같이 지정형식보다 많은 데이터값이 들어오면 오류가 발생한다.
     * 단, 지정 형식과 다른 데이터가 입력되면 널을 반환한다.
     * <pre><code>
     * DateUtils.toDate("20091223124040", "yyyyMMddHHmm"); // [Sat Dec 26 07:20:00 KST 2009]
     * DateUtils.toDate("20091223124040", "yyyyMMdd"); // [Wed May 21 00:00:00 KST 65321]
     * DateUtils.toDate("2009", "yyyy"); // [Fri Jan 01 00:00:00 KST 2009]
     * DateUtils.toDate("200912", "yyyy"); // [Fri Jan 01 00:00:00 KST 200912]
     * DateUtils.toDate("20091223", "yyyy-MM-dd"); // [null]
     * </code></pre>
     * <b><strong><font color="red">
     * ※ 위의 결과에서 볼수 있듯이 지정형식과 같으면서 데이터가 많을 경우
     * 원하는 결과값과 다른 결과를 반환하므로 사용시 주의해서 사용하도록 한다.
     * </font></strong></b>
     *
     * @param dateString 변경대상이 되는 일자문자열
     * @param dateFormat 날짜형식
     * @return java.util.Date
     */
    public static Date toDate(String dateString, String dateFormat) {
        if (dateString == null || dateString.length() == 0) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.parse(dateString);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 변경전날짜형식의 날짜문자열을 변경후날짜형식의 날짜로 변환하여 반환한다.
     * <p>
     * 변경전날짜형식과 변경후날짜형식이 널이면 기본날짜형식({@link DateConstants#DF_DATE})을 적용한다.
     *
     * @param dateString 날짜문자열
     * @param beforeDateFormat 변경전날짜형식
     * @param afterDateFormat 변경후날짜형식
     * @return 변경후날짜형식으로 변환된 날짜. 만약 입력된 날짜문자열이 널이거나 형식이 유효하지 않을 경우 널을 반환한다.
     */
    public static Date toDate(String dateString, String beforeDateFormat, String afterDateFormat) {
        if (dateString == null || dateString.length() == 0) return null;
        // 변경전날짜형식과 변경후날짜형식이 널이면 기본날짜형식을 적용하고 형식이 같을 경우 날짜를 변경하지 않고 반환한다.
        if (beforeDateFormat == null) beforeDateFormat = DF_DATE;
        if (afterDateFormat == null) afterDateFormat = DF_DATE;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(beforeDateFormat);
            Date date = sdf.parse(dateString);
            if (beforeDateFormat == afterDateFormat || beforeDateFormat.equals(afterDateFormat)) {
                return date;
            }

            // 패턴변경후 날짜변환
            sdf.applyPattern(afterDateFormat); // 패턴변경
            return sdf.parse(sdf.format(date));
        }
        catch (Exception e) {
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // 날짜 --> 문자열 변경
    // -------------------------------------------------------------------------

    /**
     * 기본일자형식({@link DateConstants#DF_DATE})으로 된 현재날짜를 문자열로 반환한다.
     * @return 현재날짜(yyyyMMdd)
     */
    public static String getCurrentDateString() {
        Date currentDate = getCurrentDateTime();
        return toDateString(currentDate, DF_DATE);
    }

    /**
     * 기본형식({@link DateConstants#DF_BASE})으로 된 현재날짜를 문자열로 반환한다.
     * @return 현재날짜(yyyyMMddHHmmss)
     */
    public static String getCurrentBaseDateString() {
        Date currentDate = getCurrentDateTime();
        return toDateString(currentDate, DF_BASE);
    }

    /**
     * 기본일자형식({@link DateConstants#DF_DATE})으로 된 초기일자(00010101)를 문자열로 반환한다.
     * @return 초기일자(yyyyMMdd)
     */
    public static String getInitialDateString() {
        return "00010101";
    }

    /**
     * 기본일자형식({@link DateConstants#DF_DATE})으로 된 초기일시(00010101000000)를 문자열로 반환한다.
     * @return 초기일시(yyyyMMddHHmmss)
     */
    public static String getInitialBaseDateString() {
        return "00010101000000";
    }

    /**
     * <code>java.util.Date</code>인 데이타를 날짜형식에 맞게 <code>java.lang.String</code>으로 변환
     * @param date 변경대상이 되는 일자
     * @param dateFormat 날짜형식
     * @return 입력된 날짜형식으로 변환된 날짜문자열
     */
    public static String toDateString(Date date, String dateFormat) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 입력받은 <code>java.util.Date</code>의 해당월 날짜를 1일로 설정하여
     * 데이타를 날짜형식에 맞게 <code>java.lang.String</code>으로 변환
     * @param date 설정할 날짜
     * @param dateFormat 날짜형식
     * @return 입력된 날짜형식으로 변환된 해당월 첫번째 날짜
     */
    public static String getFirstDateStringOfMonth(Date date, String dateFormat) {
        return toDateString(getFirstDateOfMonth(date), dateFormat);
    }

    /**
     * 입력받은 <code>java.util.Date</code>의 해당월 마지막 날짜를 설정하여
     * 데이터를 날짜 형식에 맞게 <code>java.lang.String</code>으로 변환
     * @param date 설정할 날짜
     * @param dateFormat 날짜형식
     * @return 입력된 날짜형식으로 변환된 해당월 마지막 날짜
     */
    public static String getLastDateStringOfMonth(Date date, String dateFormat) {
        return toDateString(getLastDateOfMonth(date), dateFormat);
    }

    /**
     * 입력받은 <code>java.util.Date</code>를 1월 1일로 설정하여
     * 데이터를 날짜 형식에 맞게 <code>java.lang.String</code>으로 변환
     * @param date 설정할 날짜
     * @param dateFormat 날짜형식
     * @return 입력된 날짜형식으로 변환된 입력일자의 1월 1일 날짜
     */
    public static String getFirstDateStringOfYear(Date date, String dateFormat) {
        return toDateString(getFirstDateOfYear(date), dateFormat);
    }

    /**
     * 입력받은 <code>java.util.Date</code>를 12월 31일로 설정하여
     * 데이터를 날짜 형식에 맞게 <code>java.lang.String</code>으로 변환
     * @param date 설정할 날짜
     * @param dateFormat 날짜형식
     * @return 입력된 날짜형식으로 변환된 입력일자의 12월 31일 날짜
     */
    public static String getLastDateStringOfYear(Date date, String dateFormat) {
        return toDateString(getLastDateOfYear(date), dateFormat);
    }

    // -------------------------------------------------------------------------
    // 문자열 --> 문자열 변경
    // -------------------------------------------------------------------------

    /**
     * 날짜형식변경
     * <p>
     * 변경전날짜형식과 변경후날짜형식이 널이면 기본날짜형식({@link DateConstants#DF_DATE})을
     * 적용하고 형식이 같을 경우 날짜를 변경하지 않고 반환한다.
     *
     * @param dateString 변경대상이 되는 일자
     * @param beforeDateFormat 변경전날짜형식. 널이 입력되면 {@link DateConstants#DF_DATE}을 적용한다.
     * @param afterDateFormat 변경후날짜형식. 널이 입력되면 {@link DateConstants#DF_DATE}을 적용한다.
     * @return 변경후날짜형식으로 변환된 날짜문자열. 만약 입력된 날짜문자열이 널이거나 형식이 유효하지 않을 경우 널을 반환한다.
     */
    public static String convertDateFormat(String dateString, String beforeDateFormat, String afterDateFormat) {
        if (dateString == null) return null;
        // 변경전날짜형식과 변경후날짜형식이 널이면 기본날짜형식을 적용하고 형식이 같을 경우 날짜를 변경하지 않고 반환한다.
        if (beforeDateFormat == null) beforeDateFormat = DF_DATE;
        if (afterDateFormat == null) afterDateFormat = DF_DATE;
        if (beforeDateFormat == afterDateFormat || beforeDateFormat.equals(afterDateFormat)) return dateString;

        SimpleDateFormat sdf = new SimpleDateFormat(beforeDateFormat);
        try {
            // 문자열날짜가 말일자(31)이면 해당월의 말일자로 변환
            if (DF_DATE.equals(beforeDateFormat)) {
                String yyyyMM = dateString.substring(0, 6);
                String dd = dateString.substring(6, 8);
                Date firstDateOfMonth = DateUtils.toDate(yyyyMM, DF_YYYYMM);
                if (dd.equals(MAXIMUM_DAY)) {
                    if (!DateUtils.getLastDateStringOfMonth(firstDateOfMonth, DF_DATE).equals(dateString)) {
                        dateString = DateUtils.getLastDateStringOfMonth(firstDateOfMonth, DF_DATE);
                    }
                }
            }
            Date date = sdf.parse(dateString);
            sdf.applyPattern(afterDateFormat); // 패턴변경
            return sdf.format(date);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 기준일자가 해당월의 말일자인지 여부를 반환한다.
     * @param basisDate 기준일자
     */
    public static boolean isLastDay(Date basisDate) {
        Calendar basisCal = Calendar.getInstance();
        basisCal.setTime(basisDate);
        return isLastDay(basisCal);
    }

    /**
     * 기준일자가 해당월의 말일자인지 여부를 반환한다.
     * @param basisCal 기준달력
     */
    public static boolean isLastDay(Calendar basisCal) {
        return (basisCal.get(Calendar.DATE) == basisCal.getActualMaximum(Calendar.DATE));
    }

    /**
     * 해당일의 말일자 구하기
     * <pre>
     * setLastDay("20100119", "yyyyMMdd") => "20100131"
     * setLastDay("20100205", "yyyy-MM-dd") => "2010-02-28"
     * @param dateString 기준일자 (yyyyMMdd)s
     * @param dateFormat 반환될 날짜형식
     * @return
     */
    public static String setLastDay(String dateString, String dateFormat) {
        Date date = toDate(dateString, DF_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return toDateString(new Date(calendar.getTimeInMillis()), dateFormat);
    }

    /**
     * 해당일의 초일자 구하기
     * <pre>
     * setFirstDay("20100119", "yyyyMMdd") => "20100101"
     * setFirstDay("20100205", "yyyy-MM-dd") => "2010-02-01"
     * @param dateString 기준일자 (yyyyMMdd)
     * @param dateFormat 반환될 날짜형식
     * @return
     */
    public static String setFirstDay(String dateString, String dateFormat) {
        Date date = toDate(dateString, DF_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);

        return toDateString(new Date(calendar.getTimeInMillis()), dateFormat);
    }

    /**
     * 일자의 요일을 반환
     * <pre>
     * "20100117(일요일) ==> 1 "20080123(토요일) ==> 7
     * @param date 일자
     * @return
     */
    public static int getDayOfWeek(Date date) {
        int result = 0;
        Calendar calendar = Calendar.getInstance(Locale.KOREAN);
        calendar.setTime(date);
        result = calendar.get(Calendar.DAY_OF_WEEK);
        return result;
    }

    /**
     * 입력받은 날짜에서 +/- N일의 날짜를 구한다.
     * @param date 기준 일자
     * @param day  +/- N일
     * @return 결과 일자
     */
    public static String changeDateWithDayLevel(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + day);
        return DateUtils.toDateString(cal.getTime(), DF_DATE);
    }

    /**
     * 입력한 일자가 오늘일자 기준 이전일자인지 여부 (오늘일자 포함 안함)
     *
     * @param date 일자
     * @return 여부
     */
    public static boolean isBeforeDate(Date date) {
        String currentDate = getCurrentDateString();
        String compareDate = toDateString(date, DF_DATE);
        int compare = currentDate.compareTo(compareDate);
        if (compare > 0) return true;
        return false;
    }

    /**
     * 입력한 일자가 오늘일자 기준 이전일자인지 여부 (오늘일자 포함 안함)
     *
     * @param compareDate 일자
     * @return 여부
     */
    public static boolean isBeforeDate(String compareDate) {
        String currentDate = getCurrentDateString();
        int compare = currentDate.compareTo(compareDate);
        if (compare > 0) return true;
        return false;
    }

    /**
     * 첫번째 입력한 일자가 두번째 입력한 비교일자 기준 이전 일자인지 여부
     * @param date 일자
     * @param compareDate 비교일자
     * @return 여부
     */
    public static boolean isBeforeDate(String date, String compareDate) {
        int compare = compareDate.compareTo(date);
        if (compare >= 0) return true;
        return false;
    }

    /**
     * 입력한 일자가 오늘일자 기준 이후 일자인지 여부 (오늘일자 포함 안함)
     *
     * @param date 일자
     * @return 여부
     */
    public static boolean isAfterDate(Date date) {
        String currentDate = getCurrentDateString();
        String compareDate = toDateString(date, DF_DATE);
        int compare = currentDate.compareTo(compareDate);
        if (compare < 0) return true;
        return false;
    }

    /**
     * 입력한 일자가 오늘일자 기준 이후 일자인지 여부 (오늘일자 포함 안함)
     *
     * @param date 일자
     * @return 여부
     */
    public static boolean isAfterDate(String date) {
        String currentDate = getCurrentDateString();
        int compare = currentDate.compareTo(date);
        if (compare < 0) return true;
        return false;
    }
    
    /**
     * 시작일자에서 종료일자까지의 경과년수를 반환한다.
     * <p>
     * 내부에서 {@link #getProgressMonths(Date, Date, boolean) getProgressMonths(startDate, endDate, true)}를
     * 호출하여 총경과월수를 구한뒤 12로 나눈몫을 경과년수로 설정하여 반환한다.
     * @param startDate 시작일자
     * @param endDate 종료일자
     * @return 경과년수
     * @see #getProgressMonths(Date, Date, boolean)
     * @throws NullPointerException 입력된 일자가 널일 경우 발생
     */
    public static int getProgressYears(Date startDate, Date endDate) {
        int progressMonths = getProgressMonths(startDate, endDate, true);
        int progressYears = progressMonths / 12;
        return progressYears;
    }

    /**
     * 시작일자에서 종료일자까지의 경과월수를 반환한다.
     * <p>
     * 경과월수 계산식
     * <pre>
     * startYear, startMonth, startDay // 시작년,월,일
     * endYear, endMonth, endDay //종료년,월,일
     *
     * 경과월수 = (endYear - startYear) * 12 + (endMonth - startMonth)
     * if((startDay > endDay ) && (종료일자 != 말일자)) {
     *     경과월수 = 경과월수 - 1;
     * }
     *
     * if((만한달 경과 체크여부 != true) && (시작일자 <= 종료일자)) {
     *     경과월수 = 경과월수 + 1;
     * }
     * </pre>
     * 예를 들어 시작일자가 "1999-10-31"이고 종료일자가 "2000-03-30"이면
     * <pre>
     * startYear = 1999, startMonth = 10, startDay  = 31
     * endYear = 2000, endMonth = 3, endDay = 30
     *
     * 경과월수 = (2000 - 1999) * 12 + (3 - 10) = 1*12 - 7 = 5
     * </pre>
     * 이고 종료일이 시작일보다 작고(30 < 31) 종료일자가 말일자가 아니므로(2000-03-30 != 2000-03-31)
     * 경과월수는 만한달경과 체크여부가 true인경우는 4개월, 아닌경우는 5개월이 된다.
     * <p>
     * 만한달경과 체크여부 = false인경우
     * <pre>
     *        시작일자      종료일자      경과월수  비고
     *   =================================================
     *   1.   2005-01-01   2005-07-02  7       일반
     *   2.   2005-01-31   2005-02-28  2       말일자기준
     *   3.   2000-01-31   2000-02-28  1       윤달말일자기준
     *   4.   2000-01-31   2000-02-29  2       윤달말일자기준
     *   5.   2000-02-29   2000-03-28  1       윤달말일자기준
     *   6.   2000-02-29   2000-03-29  2       윤달말일자기준
     * </pre>
     *
     * 만한달경과 체크여부 = true인경우
     * <pre>
     *        시작일자      종료일자      경과월수  비고
     *   =================================================
     *   1.   2005-01-01   2005-07-02  6       일반
     *   2.   2005-01-31   2005-02-28  1       말일자기준
     *   3.   2000-01-31   2000-02-28  0       윤달말일자기준
     *   4.   2000-01-31   2000-02-29  1       윤달말일자기준
     *   5.   2000-02-29   2000-03-28  0       윤달말일자기준
     *   6.   2000-02-29   2000-03-29  1       윤달말일자기준
     * </pre>
     *
     * @param startDate 시작일자
     * @param endDate 종료일자
     * @param isFull 만한달 경과 체크여부
     * @return 경과월수
     * @throws NullPointerException 입력된 일자가 널일 경우 발생
     */
    public static int getProgressMonths(Date startDate, Date endDate, boolean isFull) {
        int progressMonths = 0;

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        if (startTime < endTime) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            int startDay = calendar.get(Calendar.DATE);
            boolean isLastDayOfStartDate = (startDay == calendar.getActualMaximum(Calendar.DATE));

            calendar.setTime(endDate);
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            int endDay = calendar.get(Calendar.DATE);
            boolean isLastDayOfEndDate = (endDay == calendar.getActualMaximum(Calendar.DATE));

            progressMonths = (endYear - startYear) * 12 + (endMonth - startMonth);

            // 2003-02-28 ~ 2004-02-28 일자의 계산오류로 변경. Modified by 임병인 : 2006.09.07
            // 시작일이 종료일보다 크고 종료일자가 말일자가 아니면서 시작일자 또는 종료일자가 말일자가 아니면 1개월을 뺀다.
            if (((startDay > endDay) && !isLastDayOfEndDate) && (!isLastDayOfStartDate || !isLastDayOfEndDate)) {
                progressMonths--;
            }
        }

        // 만경과월수를 구하는게 아니고 시작일자가 종료일자보다 작거나 같을 경우 한달을 추가한다.
        if (!isFull && (startTime <= endTime)) {
            progressMonths++;
        }

        return progressMonths;
    }

    /**
     * <p>시작기준일에서 종료기준일까지 경과일수를 반환한다.</p>
     * <p><pre>
     *        시작일자      종료일자            경과일수
     *   ===================================
     *   1.   2005-01-01   2005-01-01  0
     *   2.   2005-01-02   2005-01-12  10
     *   3.   2000-01-12   2000-01-02  -10
     *   4.   2000-01-01   2000-01-02  1
     *   5.   2000-01-02   2000-01-01  -1
     * </pre></p>
     * @param fromDate 시작기준일(yyyyMMdd)
     * @param thruDate 종료기준일(yyyyMMdd)
     * @return 경과일수
     */
    public static int getProgressDays(String fromDate, String thruDate) {
        Date startDate = DateUtils.toDate(fromDate);
        Date endDate = DateUtils.toDate(thruDate);
        return getProgressDays(startDate, endDate);
    }

    /**
     * <p>시작기준일에서 종료기준일까지 경과일수를 반환한다.</p>
     * <p><pre>
     *        시작일자      종료일자            경과일수
     *   ===================================
     *   1.   2005-01-01   2005-01-01  0
     *   2.   2005-01-02   2005-01-12  10
     *   3.   2000-01-12   2000-01-02  -10
     *   4.   2000-01-01   2000-01-02  1
     *   5.   2000-01-02   2000-01-01  -1
     * </pre></p>
     * @param fromDate 시작기준일
     * @param thruDate 종료기준일
     * @return 경과일수
     */
    public static int getProgressDays(Date fromDate, Date thruDate) {
        long startTime = fromDate.getTime();
        long endTime = thruDate.getTime();

        int progressDays = (int) (endTime / MILLISECOND_OF_DAY) - (int) (startTime / MILLISECOND_OF_DAY);
        return progressDays;
    }

    /**
     * 시작일자에서 종료일자까지의 경과월수를 반환한다.
     * <p>
     * 내부에서 입력된 일자문자열을 해당 형식을 사용하여 일자로 변환한뒤
     * {@link #getProgressMonths(String, String, boolean, String)
     * getProgressMonths(startDateString, endDateString, true, DF_DATE)}를 호출한다.
     *
     * @param startDateString 시작일자문자열
     * @param endDateString 종료일자문자열
     * @return 경과월수
       */
    public static int getProgressMonths(String startDateString, String endDateString) {
        return getProgressMonths(startDateString, endDateString, true, DF_DATE);
    }

    /**
     * 시작일자에서 종료일자까지의 경과월수를 반환한다.
     * <p>
     * 내부에서 입력된 일자문자열을 해당 형식을 사용하여 일자로 변환한뒤
     * {@link #getProgressMonths(String, String, boolean, String)
     * getProgressMonths(startDateString, endDateString, true, DF_DATE)}를 호출한다.
     *
     * @param startDateString 시작일자문자열
     * @param endDateString 종료일자문자열
     * @param isFull 만한달 경과 체크여부
     * @return 경과월수
     */
    public static int getProgressMonths(String startDateString, String endDateString, boolean isFull) {
        return getProgressMonths(startDateString, endDateString, isFull, DF_DATE);
    }

    /**
     * 시작일자에서 종료일자까지의 경과월수를 반환한다.
     * <p>
     * 내부에서 입력된 일자문자열을 해당 형식을 사용하여 일자로 변환한뒤
     * {@link #getProgressMonths(Date, Date, boolean) getProgressMonths(startDate, endDate, isFull)}를 호출한다.
     * 단, 일자형식에 시간값이 들어 있는 경우에 계산상의 착오가 발생할 수 있으니 일자형식에 시간값은 가급적 넣지 않도록 한다.
     *
     * @param startDateString 시작일자문자열
     * @param endDateString 종료일자문자열
     * @param isFull 만한달 경과 체크여부
     * @param dateFormat 날짜형식
     * @return 경과월수
     * @see #getProgressMonths(Date, Date, boolean)
     * @see DateUtils#toDate(String, String, String)
     * @throws NullPointerException 입력된 일자가 널이거나 일자문자열을 변환하는 도중에 예외가 발생한 경우 발생
     */
    public static int getProgressMonths(String startDateString, String endDateString, boolean isFull, String dateFormat) {
        Date startDate = DateUtils.toDate(startDateString, dateFormat, DF_DATE); // 시간값을 클리어함.
        Date endDate = DateUtils.toDate(endDateString, dateFormat, DF_DATE); // 시간값을 클리어함.
        return getProgressMonths(startDate, endDate, isFull);
    }

    /**
     * 기준일에서 일자를 증감한다.
     * @param date 기준일자
     * @param nDay 증감(+ or -) 일수
     * @return
     */
    public static Date addDate(Date date, int nDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, nDay);
        return cal.getTime();
    }

    /**
     * 기준일에서 일자를 증감한다.
     * @param dateString 기준일자(yyyyMMdd)
     * @param nDay 증감(+ or -) 일수
     * @return
     */
    public static String addDate(String dateString, int nDay) {
        Date date = addDate(DateUtils.toDate(dateString), nDay);
        return DateUtils.toDateString(date, DF_DATE);
    }

    /**
     * 기준일에서 월수를 증감한다.
     * @param date 기준일자
     * @param nMonths 증감(+ or -) 월수
     */
    public static Date addMonth(Date date, int nMonths) {
        return addMonth(date, nMonths, false);
    }

    /**
     * 기준일에서 월수를 증감한다. 입력일자가 말일자이면 증감월일자도 말일자로 설정한다.
     * @param currentDate 기준일자
     * @param dateFormat 일자형식
     * @param nMonths 증감(+ or -) 월수
     */
    public static String addMonth(String currentDate, String dateFormat, int nMonths) {
        Date date = DateUtils.toDate(currentDate, dateFormat);
        Date toDate = addMonth(date, nMonths, true);
        return DateUtils.toDateString(toDate, dateFormat);
    }

    /**
     * 기준일에서 월수를 증감한다. 입력일자가 말일자이면 증감월일자도 말일자로 설정한다.
     * @param currentDate 기준일자
     * @param nMonths 증감(+ or -) 월수
     */
    public static String addMonth(String currentDate, int nMonths) {
        return addMonth(currentDate, DF_DATE, nMonths);
    }

    /**
     * 기준일에서 월수를 증감한다. 말일자검사여부가 true이고 입력일자가 말일자이면 증감월일자도 말일자로 설정한다.
     * @param date 기준일자
     * @param nMonths 증감(+ or -) 월수
     * @param lastDayCheckupYn 말일자검사여부
     */
    public static Date addMonth(Date date, int nMonths, boolean lastDayCheckupYn) {
        // 당월이면 입력된 기준일자를 반환한다.
        if (0 == nMonths) return (Date) date.clone();

        // 말일자검사여부가 true이고 입력일자가 말일자이면 증감월일자도 말일자로 설정한다.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (lastDayCheckupYn && DateUtils.isLastDay(calendar)) {
            calendar.add(Calendar.MONTH, nMonths);
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        }
        else {
            calendar.add(Calendar.MONTH, nMonths);
        }

        // 서머타임제 적용을 이유로 시간을 0으로 설정
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 월,날일 추가 및 말일자 여부가 true 이면 추가개월을 말일자를 적용
     * @param date : 바꿀일자
     * @param nMonth : 추가개월
     * @param nDate : 추가일자
     * @param isLastDay : 말일자 여부
     */
    public static Date addMonth(Date date, int nMonth, int nDate, boolean isLastDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isLastDay && DateUtils.isLastDay(calendar)) {
            calendar.add(Calendar.MONTH, nMonth);
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        else {
            calendar.add(Calendar.MONTH, nMonth);
            calendar.add(Calendar.DATE, nDate);
        }

        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 월,날일 추가 및 말일자 여부가 true 이면 추가개월을 말일자를 적용
     * @param date : 바꿀일자
     * @param basisDate : 기존일자
     * @param nMonth : 추가개월
     * @param nDate : 추가일자
     */
    public static Date addMonth(Date date, Date basisDate, int nMonth, int nDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (DateUtils.isLastDay(basisDate)) {
            calendar.add(Calendar.MONTH, nMonth);
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        else {
            calendar.add(Calendar.MONTH, nMonth);
            calendar.add(Calendar.DATE, nDate);
        }

        return new Date(calendar.getTimeInMillis());
    }
    public static List<Date> getBetweenDates(Date dateFr, Date dateTo) throws ParseException {
        String tag = "CommonService.getBetweenDates =>";

        List<Date>  dsDate = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        cal.setTime(dateFr);
        cal.add(Calendar.DATE ,  -1);
        Date date = null;
        String strProcDate = "";

        while(true) {
            cal.add(Calendar.DATE , + 1);
            if (cal.getTime().equals(dateTo)) break;
            dsDate.add(cal.getTime());
        }
        dsDate.add(cal.getTime());
        return dsDate;

    }
}
