package run.ice.fun.domain.hunter.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import run.ice.fun.domain.hunter.DomainHunterApplicationTest;

import java.util.LinkedHashMap;

@Slf4j
class HunterServiceTest extends DomainHunterApplicationTest {

    @Resource
    private HunterService hunterService;

    @Test
    void doCheck() {
        String sld = "ice";
        String tld = "run";
        hunterService.doCheck(sld, tld);
    }

    @Test
    void check() {
        LinkedHashMap<String, ?> check = hunterService.check("ice", "run");
        log.info("check: {}", check);
        Assertions.assertTrue(null != check && !check.isEmpty());
    }

}