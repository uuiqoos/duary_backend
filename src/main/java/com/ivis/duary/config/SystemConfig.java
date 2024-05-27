package com.ivis.duary.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SystemConfig {

    @Autowired @Setter
    private Environment env;

    @Getter
    private boolean isLocal;
    @Getter
    private boolean isDev;
    @Getter
    private boolean isProd;
    @Getter
    boolean isTest;

    @PostConstruct
    public void init() {
        List<String> actives = Arrays.asList(env.getActiveProfiles());
        log.info("Active prifiles : {}", actives);

        this.isLocal = actives.stream().anyMatch(p->p.startsWith("local"));
        this.isDev = actives.stream().anyMatch(p->p.startsWith("dev"));
        this.isProd = actives.stream().anyMatch(p -> p.startsWith("prod"));
        this.isTest = actives.stream().anyMatch(p -> p.startsWith("test"));
    }

    public String getCurrentProfile() {
        return env.getActiveProfiles()[0];
    }
}
