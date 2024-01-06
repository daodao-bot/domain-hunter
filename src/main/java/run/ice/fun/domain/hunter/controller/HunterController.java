package run.ice.fun.domain.hunter.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import run.ice.fun.domain.hunter.api.HunterApi;
import run.ice.fun.domain.hunter.model.DomainSniper;
import run.ice.fun.domain.hunter.model.DomainTarget;
import run.ice.fun.domain.hunter.service.HunterService;
import run.ice.lib.core.model.Ok;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

@RestController
public class HunterController implements HunterApi {

    @Resource
    private HunterService hunterService;

    @Override
    public Response<Ok> domainHunter(Request<DomainTarget> request) {
        hunterService.domainHunter(request.getParam());
        return Response.ok();
    }

    @Override
    public Response<Ok> domainSniper(Request<DomainSniper> request) {
        hunterService.domainSniper(request.getParam());
        return Response.ok();
    }

}
