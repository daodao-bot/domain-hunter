package run.ice.fun.domain.hunter.message;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import run.ice.fun.domain.hunter.constant.DomainConstant;
import run.ice.fun.domain.hunter.service.HunterService;

import java.util.List;

@Slf4j
@Component
public class MessageConsumer {

    @Resource
    private HunterService hunterService;

    @RabbitListener(queues = "domain-hunter")
    public void domainHunter(String domain) {
        log.info("MessageConsumer.domainHunter : {}", domain);
        if (null == domain || domain.isEmpty()) {
            log.error("MessageConsumer.domainHunter : domain is null or empty");
            return;
        }
        if (!domain.matches("^([a-z]+)\\.([a-z]+)$")) {
            log.error("MessageConsumer.domainHunter : domain is not match : {}", domain);
            return;
        }
        String[] split = domain.split("\\.");
        String sld = split[0];
        String tld = split[1];
        String[] tlds = DomainConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            log.error("MessageConsumer.domainHunter : tld is not match : {}", tld);
            return;
        }
        hunterService.doCheck(sld, tld);
    }

}
