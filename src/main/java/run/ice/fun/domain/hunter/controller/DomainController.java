package run.ice.fun.domain.hunter.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import run.ice.fun.domain.hunter.api.DomainApi;
import run.ice.fun.domain.hunter.model.DomainData;
import run.ice.fun.domain.hunter.model.DomainSearch;
import run.ice.fun.domain.hunter.model.DomainSelect;
import run.ice.fun.domain.hunter.model.DomainUpsert;
import run.ice.fun.domain.hunter.service.DomainService;
import run.ice.lib.core.model.PageData;
import run.ice.lib.core.model.PageParam;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

@RestController
public class DomainController implements DomainApi {

    @Resource
    private DomainService domainService;

    @Override
    public Response<DomainData> domainSelect(Request<DomainSelect> request) {
        DomainData data = domainService.domainSelect(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<DomainData> domainUpsert(Request<DomainUpsert> request) {
        DomainData data = domainService.domainUpsert(request.getParam());
        return new Response<>(data);
    }

    @Override
    public Response<PageData<DomainData>> domainSearch(Request<PageParam<DomainSearch>> request) {
        PageData<DomainData> data = domainService.domainSearch(request.getParam());
        return new Response<>(data);
    }

}
