import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/**
 * 
 * @author kun.han
 * Create at 2020-06-29 14:04
 */
@RestController
@RequestMapping("role")
@Tag(name = "null")
public class RoleController {

    private final RoleRepository roleRepository;

    private final EntityMapper entityMapper;

    private final DataMapper dataMapper;

    public RoleController(RoleRepository roleRepository, EntityMapper entityMapper, DataMapper dataMapper) {
        this.roleRepository = roleRepository;
        this.entityMapper = entityMapper;
        this.dataMapper = dataMapper;
    }
    /**
     * 查询列表
     * @param pageable 分页参数
     * @param criteria 查询条件
     * @return 列表数据
     */
    @GetMapping
    @Operation(summary = "查询列表")
    public Page<RoleRecord> get(@SortDefault(direction = Sort.Direction.DESC) Pageable pageable, RoleCriteria criteria) {
        var predicate = Predicates.build(QRole.class, (u, b) -> {
                b.add(p -> u.name.like('%' + p + '%'), criteria.name);
                b.add(p -> u.alias.like('%' + p + '%'), criteria.alias);
        });
        return roleRepository.findAll(predicate, pageable).map(dataMapper::of);
    }

    /**
     * 查询单条
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @Operation(summary = "查询单条")
    public RoleRecord get(@PathVariable String id) {
        return roleRepository.findById(id).map(dataMapper::of).orElseThrow(NotFoundException::new);
    }

    /**
     * 新增
     * @param data 新增数据
     * @return 主键
     */
    @PostMapping
    @Transactional
    @Operation(summary = "新增")
    public RoleRecord create(@RequestBody RoleNew data) {
        Role role = new Role(UUID.randomUUID().toString());
        entityMapper.assign(role, data);
        roleRepository.save(role);
        return dataMapper.of(role);
    }

    /**
     * 修改
     * @param id 主键
     * @param data 修改数据
     */
    @PutMapping("{id}")
    @Transactional
    @Operation(summary = "修改")
    public void update(@PathVariable String id, @RequestBody RoleUpdate data) {
        Role role = roleRepository.findById(id).orElseThrow(NotFoundException::new);
        entityMapper.assign(role, data);
        roleRepository.save(role);
    }

    /**
     * 删除
     * @param id 主键
     */
    @DeleteMapping("{id}")
    @Transactional
    @Operation(summary = "删除")
    public void delete(@PathVariable String id) {
        Role role = roleRepository.findById(id).orElseThrow(NotFoundException::new);
        roleRepository.delete(role);
    }

}