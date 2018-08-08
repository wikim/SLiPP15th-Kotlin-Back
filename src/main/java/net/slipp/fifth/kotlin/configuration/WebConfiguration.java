package net.slipp.fifth.kotlin.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.h2.server.web.WebServlet;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.slipp.fifth.kotlin.common.AutowireHelper;
import net.slipp.fifth.kotlin.common.mapper.HibernateAwareObjectMapper;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFileService;
import net.slipp.fifth.kotlin.web.support.handler.AttacheFileMethodReturnValueHandler;
import net.slipp.fifth.kotlin.web.support.handler.FileDownloadMethodReturnValueHandler;
import net.slipp.fifth.kotlin.web.support.interceptor.PageStatusAutoPersistenceInterceptor;
import net.slipp.fifth.kotlin.web.support.resolver.MemberHandlerMethodArgumentResolver;
import net.slipp.fifth.kotlin.web.support.resolver.PageStatusHandlerMethodArgumentResolver;
import net.slipp.fifth.kotlin.web.support.resolver.PageableHandlerMethodArgumentResolver;
import net.slipp.fifth.kotlin.web.support.view.AttacheFileResultView;
import net.slipp.fifth.kotlin.web.support.view.FileDownloadView;

/**
 * 웹애플리케이션 설정
 *
 * @author jiheon
 *
 */
@Configuration
@EnableWebMvc
@EnableCaching
@EnableConfigurationProperties
public class WebConfiguration extends WebMvcConfigurerAdapter {
    private static final String PARAM_NAME = "lang";

    private static final Integer SOCKET_TIMEOUT = 20 * 1000;
    private static final Integer MAX_CONN_POOL = 200;
    private static final Integer MAX_PER_ROUTE = 50;
    
    @Resource
    private ApplicationProperties ujblibProperties;

    @Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
    
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(10000);
        super.configureAsyncSupport(configurer);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/default-error").setViewName("error/default-error");
        registry.addViewController("/error/400").setViewName("error/400");
        registry.addViewController("/error/404").setViewName("error/404");
        registry.addViewController("/error/403").setViewName("error/403");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(objectMapper());
        converters.add(jacksonConverter); // JSON
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(0);
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(0);
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/").setCachePeriod(0);
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/").setCachePeriod(0);
        registry.addResourceHandler("/elite/**").addResourceLocations("classpath:/static/elite/").setCachePeriod(0);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(pageStatusAutoPersistenceInterceptor());
        registry.addInterceptor(webContentInterceptor());
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public PageStatusAutoPersistenceInterceptor pageStatusAutoPersistenceInterceptor() {
        return new PageStatusAutoPersistenceInterceptor();
    }

    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        return webContentInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(pageStatusHandlerMethodArgumentResolver());
        argumentResolvers.add(pageableHandlerMethodArgumentResolver());
        argumentResolvers.add(sortHandlerMethodArgumentResolver());
        argumentResolvers.add(memberHandlerMethodArgumentResolver());
    }

    @Bean
    public SortHandlerMethodArgumentResolver sortHandlerMethodArgumentResolver() {
        return new SortHandlerMethodArgumentResolver();
    }

    @Bean
    public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
        return new PageableHandlerMethodArgumentResolver();
    }

    @Bean
    public PageStatusHandlerMethodArgumentResolver pageStatusHandlerMethodArgumentResolver() {
        return new PageStatusHandlerMethodArgumentResolver();
    }

    @Bean
    public MemberHandlerMethodArgumentResolver memberHandlerMethodArgumentResolver() {
        return new MemberHandlerMethodArgumentResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(ujblibProperties.getMaxUploadFileSize());
        factory.setMaxRequestSize(ujblibProperties.getMaxUploadFileSize());
        return factory.createMultipartConfig();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //registry.addConverter(longIdTypeConverter());
        //registry.addConverter(codeableEnumConverter());
    }
    
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new AttacheFileMethodReturnValueHandler(attacheFileService(), fileDownloadView()));
        returnValueHandlers.add(new FileDownloadMethodReturnValueHandler(fileDownloadView()));
    }

    @Bean
    public AttacheFileService attacheFileService() {
        return new AttacheFileService();
    }

    @Bean
    //@ConditionalOnMissingBean(FileDownloadView.class)
    public FileDownloadView fileDownloadView() {
        return new FileDownloadView();
    }

    @Bean
    //@ConditionalOnMissingBean(AttacheFileResultView.class)
    public AttacheFileResultView attacheFileResultView() {
        return new AttacheFileResultView();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new HibernateAwareObjectMapper();
    }

    @Bean
    public AutowireHelper autowireHelper() {
        return new AutowireHelper();
    }

    @Bean
    public Filter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/view.message", "classpath:messages/exception.message",
                "classpath:messages/code.message", "classpath:messages/mail.message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    public HandlerInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(PARAM_NAME);
        return localeChangeInterceptor;
    }

    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new UjblibContainerCustomizer();
    }

    private static class UjblibContainerCustomizer implements EmbeddedServletContainerCustomizer {
    	
        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
        }
    }

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/h2console/*");
        registration.addInitParameter("webAllowOthers", "true");
        return registration;
    }

    /**
     * 스프링 JPA 예외변환기 적용 <code>@Repository</code> 사용한 곳에 예외변환 AOP를 적용
     * 
     * @return
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(MAX_CONN_POOL);
    	connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
    	
    	RequestConfig config = RequestConfig.custom().setConnectTimeout(SOCKET_TIMEOUT).setConnectionRequestTimeout(SOCKET_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
    	return HttpClientBuilder.create().setDefaultRequestConfig(config).setConnectionManager(connManager).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    /**
     * 로컬 개발환경 프로파일
     * 
     * @author jiheon
     *
     */
    @Configuration
    @Profile("local")
    public static class LocalWebConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(0);
            registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(0);
            registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
                    .setCachePeriod(0);
            registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/").setCachePeriod(0);
            registry.addResourceHandler("/elite/**").addResourceLocations("classpath:/static/elite/").setCachePeriod(0);
        }
    }

    /**
     * 출시된 제품에 대한 캐시처리
     * 
     * @author jiheon
     *
     */
    @Configuration
    @Profile({"production"})
    public static class ProductionWebConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            int cachePeriod3Days = 259200;
            registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
                    .setCachePeriod(cachePeriod3Days);
            registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
                    .setCachePeriod(cachePeriod3Days);
            registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
                    .setCachePeriod(cachePeriod3Days);
            registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/")
                    .setCachePeriod(cachePeriod3Days);
            registry.addResourceHandler("/elite/**").addResourceLocations("classpath:/static/elite/")
            		.setCachePeriod(cachePeriod3Days);
        }
    }
    
    
    public void jvm8security() {
        List<String> f = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API"); 
        f.forEach(n -> System.out.println(n));
    }
    
    
}
