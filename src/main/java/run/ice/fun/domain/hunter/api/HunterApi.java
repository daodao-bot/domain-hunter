package run.ice.fun.domain.hunter.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import run.ice.fun.domain.hunter.constant.ApiConstant;
import run.ice.fun.domain.hunter.model.DomainData;
import run.ice.fun.domain.hunter.model.DomainSniper;
import run.ice.fun.domain.hunter.model.DomainTarget;
import run.ice.lib.core.constant.AppConstant;
import run.ice.lib.core.model.Ok;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

/**
 * @author DaoDao
 */
@Tag(name = "HunterApi", description = "猎手接口")
@HttpExchange(url = AppConstant.API)
public interface HunterApi {

    @Operation(summary = "猎手搜寻", description = "@DaoDao 扫描符合条件的域名")
    @PostExchange(url = ApiConstant.DOMAIN_HUNTER)
    Response<Ok> domainHunter(@RequestBody @Valid Request<DomainTarget> request);

    @Operation(summary = "猎手狙击", description = "@DaoDao 狙击指定条件的域名")
    @PostExchange(url = ApiConstant.DOMAIN_SNIPER)
    Response<DomainData> domainSniper(@RequestBody @Valid Request<DomainSniper> request);

}
