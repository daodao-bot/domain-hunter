package run.ice.fun.domain.hunter.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import run.ice.lib.core.serialize.LongStringJsonDeserializer;
import run.ice.lib.core.serialize.LongStringJsonSerializer;
import run.ice.lib.core.serialize.Serializer;

/**
 * @author DaoDao
 */
@Schema(title = "域名搜索", description = "域名搜索")
@Data
public class DomainSearch implements Serializer {

    @Schema(title = "二级域名", description = "ice.run 中的 ice", example = "ice")
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String sld;

    @Schema(title = "顶级域名", description = "ice.run 中的 run", example = "run")
    @Size(min = 1, max = 16)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String tld;

    @Schema(title = "是否可用", description = "是否可注册：默认查询可注册数据", example = "false")
    private Boolean avail;

    @Schema(title = "价格下限", description = "单位：元", example = "1")
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long priceLower;

    @Schema(title = "价格上限", description = "单位：元", example = "1000")
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long priceUpper;

    @Schema(title = "是否有效", description = "软删除标识：默认查询有效数据", example = "true")
    private Boolean valid;

}
