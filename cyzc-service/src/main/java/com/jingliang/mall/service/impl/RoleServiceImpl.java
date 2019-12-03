package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Role;
import com.jingliang.mall.repository.RoleRepository;
import com.jingliang.mall.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 角色表ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl (RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

    @Override
    public Page<Role> findAll(Specification<Role> roleSpecification, PageRequest pageRequest) {
        return roleRepository.findAll(roleSpecification,pageRequest);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}