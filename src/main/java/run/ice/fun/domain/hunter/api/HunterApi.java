package run.ice.fun.domain.hunter.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import run.ice.fun.domain.hunter.constant.AppConstant;
import run.ice.fun.domain.hunter.model.DomainSniper;
import run.ice.fun.domain.hunter.model.DomainTarget;
import run.ice.lib.core.constant.CoreConstant;
import run.ice.lib.core.model.Ok;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

/**
 * @author DaoDao
 */
@Tag(name = "HunterApi", description = "猎手接口")
@HttpExchange(url = CoreConstant.API)
public interface HunterApi {

    @Operation(summary = "猎手搜寻", description = "@DaoDao 扫描符合条件的域名")
    @PostExchange(url = AppConstant.DOMAIN_HUNTER)
    Response<Ok> domainHunter(@RequestBody @Valid Request<DomainTarget> request);

    @Operation(summary = "猎手狙击", description = "@DaoDao 狙击指定条件的域名")
    @PostExchange(url = AppConstant.DOMAIN_SNIPER)
    Response<Ok> domainSniper(@RequestBody @Valid Request<DomainSniper> request);

}
