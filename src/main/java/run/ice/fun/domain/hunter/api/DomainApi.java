package run.ice.fun.domain.hunter.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import run.ice.fun.domain.hunter.constant.ApiConstant;
import run.ice.fun.domain.hunter.model.DomainData;
import run.ice.fun.domain.hunter.model.DomainSearch;
import run.ice.fun.domain.hunter.model.DomainSelect;
import run.ice.fun.domain.hunter.model.DomainUpsert;
import run.ice.lib.core.constant.AppConstant;
import run.ice.lib.core.model.PageData;
import run.ice.lib.core.model.PageParam;
import run.ice.lib.core.model.Request;
import run.ice.lib.core.model.Response;

/**
 * @author DaoDao
 */
@Tag(name = "DomainApi", description = "域名接口")
@HttpExchange(url = AppConstant.API)
public interface DomainApi {

    @Operation(summary = "域名查询", description = "@DaoDao 域名查询")
    @PostExchange(url = ApiConstant.DOMAIN_SELECT)
    Response<DomainData> domainSelect(@RequestBody @Valid Request<DomainSelect> request);

    @Operation(summary = "域名写入", description = "@DaoDao 域名写入")
    @PostExchange(url = ApiConstant.DOMAIN_UPSERT)
    Response<DomainData> domainUpsert(@RequestBody @Valid Request<DomainUpsert> request);

    @Operation(summary = "域名搜索", description = "@DaoDao 域名搜索")
    @PostExchange(url = ApiConstant.DOMAIN_SEARCH)
    Response<PageData<DomainData>> domainSearch(@RequestBody @Valid Request<PageParam<DomainSearch>> request);

}
