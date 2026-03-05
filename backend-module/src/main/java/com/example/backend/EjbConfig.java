package com.example.backend;

import com.example.ejb.service.IBeneficioEjbService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class EjbConfig {
    @Value("${ejb.url}")
    String ejbUrl;
    @Value("${ejb.context}")
    String context;

    @Bean
    public IBeneficioEjbService beneficioEjb() throws NamingException {

        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, context);

        props.put(Context.PROVIDER_URL, ejbUrl);

        Context ctx = new InitialContext(props);

        return (IBeneficioEjbService) ctx.lookup(
                "ejb:/ejb-module-1.0.0/BeneficioEjbService!com.example.ejb.service.IBeneficioEjbService"
        );
    }
}
