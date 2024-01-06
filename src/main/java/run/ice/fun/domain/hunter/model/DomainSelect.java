package run.ice.fun.domain.hunter.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import run.ice.lib.core.serialize.LongStringJsonDeserializer;
import run.ice.lib.core.serialize.LongStringJsonSerializer;
import run.ice.lib.core.serialize.Serializer;

/**
 * @author DaoDao
 */
@Schema(title = "域名查询", description = "域名查询")
@Data
public class DomainSelect implements Serializer {

    @Schema(title = "id", description = "主键 ID", example = "1")
    @NotNull
    @Positive
    @Min(value = 1)
    @Max(value = Long.MAX_VALUE)
    @JsonSerialize(using = LongStringJsonSerializer.class)
    @JsonDeserialize(using = LongStringJsonDeserializer.class)
    private Long id;

}
