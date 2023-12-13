package org.owasp.dsomm.metricca.analyzer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan({"com.baeldung.freemarker"})
public class SpringWebConfig implements  WebMvcConfigurer {

}