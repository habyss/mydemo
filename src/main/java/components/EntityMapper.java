import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EntityMapper {
    void assign(@MappingTarget Role role, RoleNew data);

    void assign(@MappingTarget Role role, RoleUpdate data);

    void assign(@MappingTarget Role role, RoleNew data);

    void assign(@MappingTarget Role role, RoleUpdate data);

    void assign(@MappingTarget Role role, RoleNew data);

    void assign(@MappingTarget Role role, RoleUpdate data);
}