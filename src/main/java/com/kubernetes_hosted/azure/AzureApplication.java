package com.kubernetes_hosted.azure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
@SpringBootApplication
public class AzureApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureApplication.class);

	@Autowired
	private StringRedisTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(AzureApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		String key = "testkeyaccess";
		if(!this.template.hasKey(key)){
			ops.set(key, "Hello World!!!!!!!!!!!!!!!!!!");
			LOGGER.info("Add a key is OK");
		}
		LOGGER.info("Please system, return the value from the cache, thanks! Where is? ... {}", ops.get(key));
	}
}
