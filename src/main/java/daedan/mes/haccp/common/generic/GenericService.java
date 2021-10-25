package daedan.mes.haccp.common.generic;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common.service</li>
 * <li>설 명 : GenericService.java</li>
 * <li>작성일 : 2020. 6. 12.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
@Service
public abstract class GenericService {

    /**
     * AOP 로깅 처리
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Properties 설정
     */
    @Autowired
    protected Environment properties = null;

    /**
     * messageSource 변수
     */
    @Autowired
    protected MessageSource messageSource = null;
    
    /**
     * 리소스번들에서 메시지 값 조회
     * 
     * @param messageCode       리소스번들의 코드값
     * @param messageParameters array of arguments that will be filled in for params
     *                          within the message (params look like "{0},
     *                          }{1,date}", "{2,time}" within a message)
     * @param defaultMessage    리소스번들 조회 실패시 기본 제공 메시지
     * @return 코드값에 해당하는 리소스 값
     */
    public String getMessage(String messageCode, Object[] messageParameters, String defaultMessage) {
        Locale locale = Locale.getDefault();
        String message = this.messageSource.getMessage(messageCode, messageParameters, defaultMessage, locale);
        return message;
    }

    /**
     * 리소스번들에서 메시지 값 조회
     * 
     * @param messageCode       리소스번들의 코드값
     * @param messageParameters array of arguments that will be filled in for params
     *                          within the message (params look like "{0},
     *                          }{1,date}", "{2,time}" within a message)
     * @param defaultMessage    리소스번들 조회 실패시 기본 제공 메시지
     * @param locale            로케일
     * @return String
     */
    public String getMessage(String messageCode, Object[] messageParameters, String defaultMessage, Locale locale) {
        String message = this.messageSource.getMessage(messageCode, messageParameters, defaultMessage, locale);
        return message;
    }
}
