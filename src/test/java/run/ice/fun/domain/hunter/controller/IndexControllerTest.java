package run.ice.fun.domain.hunter.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import run.ice.fun.domain.hunter.DomainHunterApplicationTest;
import run.ice.fun.domain.hunter.config.AppConfig;

import java.nio.charset.Charset;

@Slf4j
public class IndexControllerTest extends DomainHunterApplicationTest {

    @Resource
    private AppConfig appConfig;

    @Resource
    private MockMvc mockMvc;

    @Test
    void index() {

        String api = "/";

        String responseBody;

        try {
            responseBody = mockMvc.perform(MockMvcRequestBuilders.get(api))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(Charset.defaultCharset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(appConfig.getApplication(), responseBody);
    }

}
