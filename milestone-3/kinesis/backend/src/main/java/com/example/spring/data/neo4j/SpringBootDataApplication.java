package com.example.spring.data.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@SpringBootApplication
public class SpringBootDataApplication extends SpringBootServletInitializer {

  @Autowired 
  Runnable MessageListener;
  
  @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(SpringBootDataApplication.class);
   }

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataApplication.class, args);
	}

  // set primary task sheduler
  @Bean
  @Primary
  public TaskExecutor taskExecutor() {
    return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
  }

  @Bean
  public CommandLineRunner shedulingRunner(TaskExecutor executor) {
    return new CommandLineRunner() {
      public void run(String... args) throws Exception {
        executor.execute(MessageListener);
      }
    };
  }
	
}
