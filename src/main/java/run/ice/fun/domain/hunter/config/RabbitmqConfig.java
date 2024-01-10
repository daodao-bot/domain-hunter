package run.ice.fun.domain.hunter.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author DaoDao
 */
@Slf4j
@Configuration
public class RabbitmqConfig {

    @Resource
    private AmqpAdmin amqpAdmin;

    @Bean
    public Queue domainHunter() {
        Queue queue = new Queue("domain-hunter");
        amqpAdmin.declareQueue(queue);
        return queue;
    }

}
