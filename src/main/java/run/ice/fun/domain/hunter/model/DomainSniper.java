package run.ice.fun.domain.hunter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.ice.lib.core.serialize.Serializer;

/**
 * @author DaoDao
 */
@Schema(title = "域名狙击", description = "域名狙击")
@Data
public class DomainSniper implements Serializer {

    @Schema(title = "二级域名", description = "ice.run 中的 ice", example = "ice")
    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[a-z]+$")
    private String sld;

    @Schema(title = "顶级域名", description = "ice.run 中的 run", example = "run")
    @NotBlank
    @Size(min = 1, max = 16)
    @Pattern(regexp = "^[a-z]+$")
    private String tld;

}
