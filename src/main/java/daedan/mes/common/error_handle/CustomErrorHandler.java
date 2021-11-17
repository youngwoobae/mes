package daedan.mes.common.error_handle;

import net.minidev.json.parser.ParseException;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.parsing.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common.error_handle</li>
 * <li>설 명 : CustomErrorHandler.java</li>
 * <li>작성일 : 2020. 11. 17.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
public class CustomErrorHandler {
	
	/**
     * AOP 로깅 처리
     */
    private static Logger logger = LoggerFactory.getLogger(CustomErrorHandler.class);
    
    /**
	 * 예외 처리 에러 확인 및 사용자 보는 메세지 반환
	 *
	 * @param  String className 클래스명
	 * @param  Exception e 예외 항목
	 * @return String 조회 데이터 맵을 반환 한다.
	 */
	public static String handle(String className, Exception e) {
		logger.error("error occured className: " + className);
		logger.error("exception className: " + e.getClass().getSimpleName());
		logger.error("exception detail className: " + e.getClass().getName());
		logger.error("error: ", e);
		String result = "";
		if (e instanceof BadSqlGrammarException) {
			result = "SQL 문법에 문제 있습니다.";
		} else if(e instanceof SQLException) {
			result = "데이터베이스 처리 문제 있습니다..";
		} else if(e instanceof TooManyResultsException) {
			result = "결과 값이 하나 나와야 하는데 여러개가 나옵니다.";
		} else if(e instanceof DataSourceException) {
			result = "데이터베이스 연결 에러입니다.";
		} else if(e instanceof ParsingException) {
			result = "JSON 파싱 문제가 발생하였습니다.";
		} else if(e instanceof ParseException) {
			result = "데이터를 파싱할 때 에러납니다. 올바르게 작성해주세요.";
		} else if(e instanceof NullPointerException) {
			result = "입력 값이 없습니다.";
		} else if(e instanceof ClassCastException) {
			result = "변환할 수 없는 값으로 변하게 하는 시도하여 에러 발생하였습니다.";
		} else if(e instanceof IllegalArgumentException) {
			result = "객체의 상태가 메소드 호출에 부적절합니다.";
		} else if(e instanceof NumberFormatException) {
			result = "문자열이 나타내는 숫자와 일치하지 않은 타입의 숫자로 변환 에러입니다.";
		} else if(e instanceof ArrayIndexOutOfBoundsException) {
			result = "배열의 범위를 벗어난 접근 에러가 발생하였습니다.";
		} else if(e instanceof ArithmeticException) {
			result = "정수를 0으로 나눌 때 에러가 발생하였습니다.";
		} else if(e instanceof ConcurrentModificationException) {
			result = "금지된 곳에서 객체를 동시에 수정하는 것이 감지되어 에러 발생하였습니다.";
		} else if(e instanceof UnsupportedOperationException) {
			result = "객체가 메소드를 지원하지 않아 에러가 발생하였습니다.";
		} else if(e instanceof InterruptedException) {
			result = "데이터 처리 중에 문제 발생하였습니다.";
		} else if(e instanceof IOException) {
			result = "서버에서 처리 문제가 발생하였습니다.";
		} else if(e instanceof CustomErrorException) {
			CustomErrorException ce = (CustomErrorException) e;
			result = ce.getCustomErrorMsg();
		} else {
			result = "서버에 에러가 발생하였습니다.";
		}
		logger.error("error message: " + result);
		return result;
	}
}
