package com.example.backend;

import com.example.ejb.service.IBeneficioEjbService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

@Configuration
public class EjbConfig {
    @Bean
    public IBeneficioEjbService beneficioEjb() throws Exception {

        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.wildfly.naming.client.WildFlyInitialContextFactory");

        props.put(Context.PROVIDER_URL,
                "http-remoting://localhost:8080");

        Context context = new InitialContext(props);

        return (IBeneficioEjbService) context.lookup(
                "ejb:/ejb-module-1.0.0/BeneficioEjbService!com.example.ejb.service.IBeneficioEjbService"
        );
    }
}
