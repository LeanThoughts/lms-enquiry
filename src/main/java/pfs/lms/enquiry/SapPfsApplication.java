package pfs.lms.enquiry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableRetry
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class SapPfsApplication {

	@Primary
	@Bean
	public TaskExecutor primaryTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// add necessary properties to the executor
		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(SapPfsApplication.class, args);
	}

	/*@Bean(name = {"applicationEventMulticaster"})
	public ApplicationEventMulticaster threadPoolAndSecurityAwareEventMulticaster() {
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Executor delegatedExecutor = Executors.newWorkStealingPool();
		Executor delegatingExecutor = new DelegatingSecurityContextExecutor(delegatedExecutor, securityContext);
		eventMulticaster.setTaskExecutor(delegatingExecutor);
		return eventMulticaster;
	}*/
}
