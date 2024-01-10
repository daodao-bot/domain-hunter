package run.ice.fun.domain.hunter.message;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageProducer {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void domainHunter(String domain) {
        amqpTemplate.convertAndSend("domain-hunter", domain);
        log.info("MessageProducer.domainHunter : {}", domain);
    }

}
