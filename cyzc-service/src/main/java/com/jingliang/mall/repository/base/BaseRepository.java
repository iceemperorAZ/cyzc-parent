package com.jingliang.mall.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 基础Repository
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-22 15:53
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 分页查询可用数据列表
     *
     * @param pageable    分页条件
     * @param isAvailable 是否可用
     * @return 返回查询到的列表
     */
    Page<T> findAllByIsAvailable(Pageable pageable, Boolean isAvailable);

    /**
     * 查询可用数据列表
     *
     * @param isAvailable 是否可用
     * @return 返回查询到的列表
     */
    List<T> findAllByIsAvailable(Boolean isAvailable);

    /**
     * 根据主键和可用查询数据
     *
     * @param id          主键Id
     * @param isAvailable 是否可用
     * @return 返回查询到的数据
     */
    T findAllByIdAndIsAvailable(Long id, Boolean isAvailable);
}
