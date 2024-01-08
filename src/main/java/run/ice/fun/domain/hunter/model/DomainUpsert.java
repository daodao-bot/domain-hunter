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
@Schema(title = "域名写入", description = "域名写入")
@Data
public class DomainUpsert implements Serializer {

    @Schema(title = "id", description = "主键 ID，有值覆写，无值新写", example = "1")
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long id;

    @Schema(title = "二级域名", description = "ice.run 中的 ice", example = "ice")
    @NotBlank
    @Size(min = 1, max = 16)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String sld;

    @Schema(title = "顶级域名", description = "ice.run 中的 run", example = "run")
    @NotBlank
    @Size(min = 1, max = 8)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String tld;

    @Schema(title = "是否可用", description = "是否可注册", example = "false")
    @NotNull
    private Boolean avail;

    @Schema(title = "价格", description = "单位：元", example = "100")
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long price;

    @Schema(title = "是否有效", description = "软删除标识", example = "true")
    private Boolean valid;

}
