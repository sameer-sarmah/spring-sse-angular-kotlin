package northwind.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
@ComponentScan(basePackages = arrayOf( "northwind" ))
open class AppConfig {

    @Bean
    public open fun elasticScheduler(): Scheduler {
        return Schedulers.elastic()
    }

    companion object {

        @Bean
        public open fun propertySourcesPlaceholderConfigurer(): PropertySourcesPlaceholderConfigurer {
            return PropertySourcesPlaceholderConfigurer()
        }
    }
}
