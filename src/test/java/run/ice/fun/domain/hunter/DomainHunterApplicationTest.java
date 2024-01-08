package run.ice.fun.domain.hunter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import run.ice.lib.core.constant.AppConstant;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

import java.nio.charset.Charset;

/**
 * @author DaoDao
 */
@Slf4j
@Rollback
@Transactional
@ActiveProfiles("local")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomainHunterApplicationTest {

    private String uri;

    private HttpHeaders httpHeaders;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        uri = "/" + AppConstant.API + "/";
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown() {
        Assertions.assertTrue(true);
    }

    public <P, D> Response<D> mockMvc(String api, Request<P> request, TypeReference<Response<D>> typeReference) {
        HttpHeaders headers = new HttpHeaders();
        return mockMvc(api, headers, request, typeReference);
    }

    public <P, D> Response<D> mockMvc(String api, HttpHeaders headers, Request<P> request, TypeReference<Response<D>> typeReference) {

        log.debug("{}", request.toJson());

        String url = uri + api;

        httpHeaders.addAll(headers);

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String responseBody;
        try {
            responseBody = mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .headers(httpHeaders)
                            .content(requestBody)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(Charset.defaultCharset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Response<D> response;
        try {
            response = objectMapper.readValue(responseBody, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.debug("{}", response.toJson());

        return response;
    }

}
