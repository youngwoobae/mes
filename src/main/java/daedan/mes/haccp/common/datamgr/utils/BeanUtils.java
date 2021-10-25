package daedan.mes.haccp.common.datamgr.utils;

import org.springframework.context.ApplicationContext;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common.datamgr.utils</li>
 * <li>설 명 : BeanUtils.java</li>
 * <li>작성일 : 2020. 9. 15.</li>
 * <li>작성자 : 태공</li>
 * </ul>
 */
public class BeanUtils {
	
	/**
	 * bean에 등록되어 있는 클래스 사용시 어노테이션을 사용할 수 없는경우 해당 메서드를 사용하여 처리
	 * 
//	 * @param String beanName
	 * @return void
	 */
    public static Object getBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }
    
}
