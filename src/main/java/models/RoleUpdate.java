import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @author kun.han
 * Create at 2020-06-29 14:04
 */
@Schema(description = "null")
public class RoleUpdate {

    /**
     * 
     */
    @Schema(description = "null", required = true)
    @NotBlank(message = "null 必填")
    public String name;

    /**
     * 
     */
    @Schema(description = "null", required = true)
    @NotBlank(message = "null 必填")
    public String alias;
}