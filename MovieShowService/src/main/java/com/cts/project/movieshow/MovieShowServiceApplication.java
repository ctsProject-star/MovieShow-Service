package com.cts.project.movieshow;

import java.util.concurrent.Executor; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableAsync
public class MovieShowServiceApplication {
	
//	@Bean     // Create a bean for restTemplate to call services   
//	@LoadBalanced		// Load balance between service instances running at different ports.
//	public RestTemplate restTemplate() 
//	{
//	    return new RestTemplate();
//	}
	
	@Bean
	public Executor taskExecutor() 
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("AysncServiceThreadLookup-");
		executor.initialize();
		return executor;
	}
	
	@Bean
	@LoadBalanced
    public WebClient.Builder getWebClientBuilder()
    {
        return  WebClient.builder();
    }

	public static void main(String[] args) {
		SpringApplication.run(MovieShowServiceApplication.class, args);
	}

}
