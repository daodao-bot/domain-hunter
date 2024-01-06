package run.ice.fun.domain.hunter.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import run.ice.lib.core.serialize.LongStringJsonDeserializer;
import run.ice.lib.core.serialize.LongStringJsonSerializer;
import run.ice.lib.core.serialize.Serializer;

import java.time.LocalDateTime;

/**
 * @author DaoDao
 */
@Schema(title = "域名数据", description = "域名数据")
@Data
public class DomainData implements Serializer {

    @Schema(title = "id", description = "主键 ID", example = "1")
    @NotNull
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long id;

    @Schema(title = "二级域名", description = "ice.run 中的 ice", example = "ice")
    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String sld;

    @Schema(title = "顶级域名", description = "ice.run 中的 run", example = "run")
    @NotBlank
    @Size(min = 1, max = 16)
    @Pattern(regexp = "^[a-z]+$")
    private String tld;

    @Schema(title = "是否可用", description = "是否可注册", example = "false")
    @NotNull
    private Boolean avail;

    @Schema(title = "价格", description = "单位：元", example = "100")
    @NotNull
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long price;

    @Schema(title = "创建时间", description = "ISO-8601", example = "2024-01-01T00:00:00")
    @NotNull
    private LocalDateTime createTime;

    @Schema(title = "更新时间", description = "ISO-8601", example = "2024-01-01T00:00:00")
    @NotNull
    private LocalDateTime updateTime;

    @Schema(title = "是否有效", description = "软删除标识", example = "true")
    @NotNull
    private Boolean valid;

}
