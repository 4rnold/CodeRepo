package mayfly.sys.api.model.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.util.TreeUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @date 2022-03-20 21:22
 */
@Getter
@Setter
@ToString
public class ResourceDTO implements TreeUtils.TreeNode<Long, ResourceDTO> {

    private Long id;

    private Long pid;

    /**
     * @see mayfly.sys.api.enums.ResourceTypeEnum
     */
    private Integer type;

    private String name;

    private String code;

    private Integer status;

    private String meta;

    private List<ResourceDTO> children;

    @Override
    public Long id() {
        return this.getId();
    }

    @Override
    public Long parentId() {
        return this.pid;
    }

    @Override
    public boolean root() {
        return Objects.equals(this.pid, 0L);
    }

    @Override
    public void setChildren(List<ResourceDTO> children) {
        this.children = children;
    }
}
