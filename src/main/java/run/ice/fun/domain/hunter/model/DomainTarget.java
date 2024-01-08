package run.ice.fun.domain.hunter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import run.ice.lib.core.serialize.Serializer;

/**
 * @author DaoDao
 */
@Schema(title = "域名目标", description = "域名目标")
@Data
public class DomainTarget implements Serializer {

    @Schema(title = "顶级域名", description = "ice.run 中的 run", example = "run")
    @NotBlank
    @Size(min = 1, max = 8)
    @Pattern(regexp = "^[a-z]+$")
    private String tld;

    @Schema(title = "位数", description = "二级域名位数", example = "1")
    @NotNull
    @Positive
    @Min(value = 1)
    @Max(value = 4)
    private Integer bit;

}
