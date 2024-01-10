package run.ice.fun.domain.hunter.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Tag(name = "IndexApi", description = "Index")
@HttpExchange(url = "")
public interface IndexApi {

    @Operation(summary = "index", description = "index")
    @GetExchange(url = "")
    String index();

}
