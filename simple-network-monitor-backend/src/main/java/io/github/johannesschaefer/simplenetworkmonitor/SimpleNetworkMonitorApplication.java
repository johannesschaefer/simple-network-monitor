package io.github.johannesschaefer.simplenetworkmonitor;

import io.github.johannesschaefer.simplenetworkmonitor.repos.HostEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
@Configuration
@EnableSpringConfigured
@EnableJpaAuditing
public class SimpleNetworkMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleNetworkMonitorApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("CommandScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public Logger produceLogger(InjectionPoint injectionPoint) {
        Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
        return LoggerFactory.getLogger(classOnWired);
    }

    @Bean
    public HostEventHandler produceHostEventHandler() {
        return new HostEventHandler();
    }
}
