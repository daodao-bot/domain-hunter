package run.ice.fun.domain.hunter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import run.ice.fun.domain.hunter.DomainHunterApplicationTest;
import run.ice.fun.domain.hunter.constant.ApiConstant;
import run.ice.fun.domain.hunter.model.DomainData;
import run.ice.fun.domain.hunter.model.DomainSearch;
import run.ice.fun.domain.hunter.model.DomainSelect;
import run.ice.fun.domain.hunter.model.DomainUpsert;
import run.ice.lib.core.error.AppError;
import run.ice.lib.core.model.PageData;
import run.ice.lib.core.model.PageParam;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

import java.util.List;

@Slf4j
class DomainControllerTest extends DomainHunterApplicationTest {

    @Test
    void domainSelect() {

        String api = ApiConstant.DOMAIN_SELECT;

        DomainSelect param = new DomainSelect();
        param.setId(1L);

        Request<DomainSelect> request = new Request<>(param);

        Response<DomainData> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(AppError.OK.code, response.getCode());

        DomainData data = response.getData();

        Assertions.assertNotNull(data);
        Assertions.assertEquals(1L, data.getId());

    }

    @Test
    void domainUpsert() {

        String api = ApiConstant.DOMAIN_UPSERT;

        DomainUpsert param = new DomainUpsert();
        param.setId(1L);
        param.setSld("ice");
        param.setTld("run");
        param.setAvail(Boolean.FALSE);

        Request<DomainUpsert> request = new Request<>(param);

        Response<DomainData> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(AppError.OK.code, response.getCode());

        DomainData data = response.getData();

        Assertions.assertNotNull(data);
        Assertions.assertEquals(1L, data.getId());

    }

    @Test
    void domainSearch() {

        String api = ApiConstant.DOMAIN_SEARCH;

        DomainSearch param = new DomainSearch();
        param.setSld("ice");
        param.setTld("run");

        PageParam<DomainSearch> pageParam = new PageParam<>(param);

        Request<PageParam<DomainSearch>> request = new Request<>(pageParam);

        Response<PageData<DomainData>> response = mockMvc(api, request, new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(AppError.OK.code, response.getCode());

        PageData<DomainData> pageData = response.getData();

        Assertions.assertNotNull(pageData);
        List<DomainData> list = pageData.getList();
        Assertions.assertFalse(list.isEmpty());

    }

}