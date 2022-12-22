package ru.suh.springcourse.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;


// данный класс заменяет web.xml
public class MySpringMvcDispatcherSerlvetIntitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {
         // для астрактного класса добавить зависимость Java Servlet API
    @Override
    protected Class<?>[] getRootConfigClasses() { // пока использовать не будем
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() { // == <param-value>/WEB-INF/applicationContextMVC.xml</param-value>
        return new Class[] {SpringConfig.class};

        /* metod ==
        <servlet>
            <servlet-name>dispatcher</servlet-name>
                <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
                    <init-param>
                        <param-name>contextConfigLocation</param-name>
                        <param-value>/WEB-INF/applicationContextMVC.xml</param-value>
                </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>
        */
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};  // все запросы пользователя посылаем на диспетчерСервлет
        /* metod ==
        <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
        </servlet-mapping>
        */
    }

    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerCharacterEncodingFilter(aServletContext);  // поддержка русского языка
        registerHiddenFieldFilter(aServletContext);
    }

    private void registerHiddenFieldFilter(ServletContext aContext) {  // фильтр обработает запрос  th:method="PATCH"
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }


    private void registerCharacterEncodingFilter(ServletContext aContext) {  // поддержка русского языка
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = aContext.addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
    }
}
