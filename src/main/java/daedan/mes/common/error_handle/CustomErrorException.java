package daedan.mes.common.error_handle;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common.error_handle</li>
 * <li>설 명 : CustomErrorException.java</li>
 * <li>작성일 : 2020. 11. 18.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
public class CustomErrorException extends Exception {

	private static final long serialVersionUID = 4620467011826436431L;

	private String message;

	public CustomErrorException(String message) {
		super(message);
		this.message = message;
	}

	public String getCustomErrorMsg() {
		return message;
	}
}