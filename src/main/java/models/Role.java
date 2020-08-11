import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.*;

/**
 * 
 * @author kun.han
 * Create at 2020-06-29 13:44
 */
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Role {

    /**
     * 
     */
    @Id
    @Setter(AccessLevel.PROTECTED)
    @NonNull
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 
     */
    @Column(name = "alias", nullable = false)
    private String alias;
}