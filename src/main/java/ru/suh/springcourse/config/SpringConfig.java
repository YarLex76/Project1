package ru.suh.springcourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;;import java.util.Objects;

// данный класс заменит файл applicationContextMVC.xml

@Configuration // это класс конфигурации
@ComponentScan ("ru.suh.springcourse")  // == <context:component-scan base-package="ru.suh.springcourse"/>
@EnableWebMvc // поддержка web приложения. == <mvc:annotation-driven/>
@PropertySource("classpath:database.properties") // аннотация указывает путь с файлом с настойками к БД
public class SpringConfig implements WebMvcConfigurer { // WebMvcConfigurer - дает возможность вмето стандартного
                                                        // шаблонизатора использовать другой шаблонизатор
                                                        // например thymeleaf
    private final ApplicationContext applicationContext;

    private final Environment environment; // с помощью этог бина мы получаем доступ к свойствам @PropertySource

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");  // то, где будут лежать представления
        templateResolver.setSuffix(".html");           // расширение представлений
        templateResolver.setCharacterEncoding("UTF-8"); // поддержка русского языка
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) { // задает шаблонизатор thymeleaf
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");  // поддержка русского языка

        registry.viewResolver(resolver);

    }

    @Bean
    public DataSource dataSource () {  // Создание бина dataSource - доступ к БД

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // все получим из файла database.properties
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("driver"))); // драйвер к БД
        dataSource.setUrl(environment.getProperty("url")); // URL БД
        dataSource.setUsername(environment.getProperty("username1")); // имя пользователя
        dataSource.setPassword(environment.getProperty("password")); // пароль

        return  dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){ // бин JdbcTemplate будет работать с БД посредством dataSource
        return new JdbcTemplate(dataSource());
    }


}
