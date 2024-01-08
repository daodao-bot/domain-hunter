package run.ice.fun.domain.hunter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import run.ice.fun.domain.hunter.DomainHunterApplicationTest;
import run.ice.fun.domain.hunter.constant.ApiConstant;
import run.ice.fun.domain.hunter.model.DomainSniper;
import run.ice.fun.domain.hunter.model.DomainTarget;
import run.ice.lib.core.error.AppError;
import run.ice.lib.core.model.Ok;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

@Slf4j
class HunterControllerTest extends DomainHunterApplicationTest {

    @Test
    void domainHunter() {

        String api = ApiConstant.DOMAIN_HUNTER;

        DomainTarget param = new DomainTarget();
        param.setBit(1);
        param.setTld("com");

        Request<DomainTarget> request = new Request<>(param);

        Response<Ok> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(AppError.OK.code, response.getCode());

    }

    @Test
    void domainSniper() {

        String api = ApiConstant.DOMAIN_SNIPER;

        DomainSniper param = new DomainSniper();
        param.setSld("ice");
        param.setTld("run");

        Request<DomainSniper> request = new Request<>(param);

        Response<Ok> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(AppError.OK.code, response.getCode());

    }

}