package com.cts.project.movieshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class MovieShowServiceApplication {
	
//	@Bean     // Create a bean for restTemplate to call services   
//	@LoadBalanced		// Load balance between service instances running at different ports.
//	public RestTemplate restTemplate() 
//	{
//	    return new RestTemplate();
//	}
	
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

//@Configuration
//class RestTemplateConfig {
//	
//	// Create a bean for restTemplate to call services
//	@Bean
//	@LoadBalanced		// Load balance between service instances running at different ports.
//	public RestTemplate restTemplate() {
//	    return new RestTemplate();
//	}
//}
