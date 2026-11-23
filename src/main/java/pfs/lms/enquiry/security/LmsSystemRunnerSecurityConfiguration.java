package pfs.lms.enquiry.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Runs startup and scheduled work under {@link LmsSystemSecurity#INTERNAL_AUTHORITY} so secured repositories remain
 * callable without an HTTP principal.
 */
@Configuration
public class LmsSystemRunnerSecurityConfiguration implements SchedulingConfigurer {

    @Bean
    public static BeanPostProcessor lmsSystemCommandLineRunnerPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName)
                    throws BeansException {
                if (bean instanceof CommandLineRunner && !(bean instanceof LmsSystemCommandLineRunnerDecorator)) {
                    return new LmsSystemCommandLineRunnerDecorator((CommandLineRunner) bean);
                }
                if (bean instanceof ApplicationRunner && !(bean instanceof LmsSystemApplicationRunnerDecorator)) {
                    return new LmsSystemApplicationRunnerDecorator((ApplicationRunner) bean);
                }
                return bean;
            }
        };
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("lms-scheduled-");
        scheduler.initialize();
        taskRegistrar.setScheduler(new LmsSystemDelegatingTaskScheduler(scheduler));
    }

    private static final class LmsSystemCommandLineRunnerDecorator implements CommandLineRunner {

        private final CommandLineRunner delegate;

        private LmsSystemCommandLineRunnerDecorator(CommandLineRunner delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run(String... args) throws Exception {
            LmsSystemSecurity.runThrowing(() -> delegate.run(args));
        }
    }

    private static final class LmsSystemApplicationRunnerDecorator implements ApplicationRunner {

        private final ApplicationRunner delegate;

        private LmsSystemApplicationRunnerDecorator(ApplicationRunner delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
            LmsSystemSecurity.runThrowing(() -> delegate.run(args));
        }
    }
}