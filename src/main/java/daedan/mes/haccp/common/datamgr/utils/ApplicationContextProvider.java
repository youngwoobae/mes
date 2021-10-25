package daedan.mes.haccp.common.datamgr.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common.datamgr.utils</li>
 * <li>설 명 : ApplicationContextProvider.java</li>
 * <li>작성일 : 2021. 9. 25.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware{
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    /**
     * Application에 등록되어 있는 bean의 정보를 조회
     *
     * @return void
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
