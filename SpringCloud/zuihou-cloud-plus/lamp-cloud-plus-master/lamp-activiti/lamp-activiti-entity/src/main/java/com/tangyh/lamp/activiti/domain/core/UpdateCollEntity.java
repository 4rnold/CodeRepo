package com.tangyh.lamp.activiti.domain.core;

import lombok.Data;

import java.util.List;

/**
 * 主要用于批量修改
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
public class UpdateCollEntity<T> {
    /**
     * id集合
     */
    private List<T> ids;
}
