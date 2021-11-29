package com.tweek.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.tweek.properties.FileUploadProperties;

@SpringBootApplication(scanBasePackages = {"com.tweek"})
@EnableConfigurationProperties({
    FileUploadProperties.class
})
public class CsvuploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvuploaderApplication.class, args);
	}

}
