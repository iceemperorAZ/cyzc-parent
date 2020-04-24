package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserGroup;
import com.jingliang.mall.req.GroupReq;
import com.jingliang.mall.req.UserGroupReq;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.GroupResp;
import com.jingliang.mall.resp.UserGroupResp;
import com.jingliang.mall.resp.UserResp;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.jingliang.mall.service.UserGroupService;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 员工与组关系映射表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/userGroup")
@Api(tags = "员工与组关系映射表")
public class UserGroupController {

	private final UserGroupService userGroupService;


	public UserGroupController(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	@PostMapping("/save")
	@ApiOperation(value = "更新/增加新组员")
    public Result<UserGroupResp> saveGroupUser(@RequestBody UserReq userReq){
	    log.debug("请求参数:{}",userReq);
	    //对前台参数进行校验
	    if (Objects.isNull(userReq)){
	        return Result.buildParamFail();
        }
        User user = BeanMapper.map(userReq, User.class);
        //对传来的参数进行保存操作
        //对用户表，用户分组表都进行保存
        UserGroup userGroup = userGroupService.saveU(user);
        //类型转换
        UserGroupResp userGroupResp = BeanMapper.map(userGroup, UserGroupResp.class);
        log.debug("返回结果：{}",userGroupResp);
        return Result.buildSaveOk(userGroupResp);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "逻辑删除组员")
    public Result<UserGroupResp> deleteGroupUser(@RequestBody UserGroupReq userGroupReq){
	    log.debug("请求参数：{}",userGroupReq);
	    if (Objects.isNull(userGroupReq)){
	        return Result.buildParamFail();
        }
        UserGroup userGroup = BeanMapper.map(userGroupReq, UserGroup.class);
	    //这里-2代表所有未分组成员，因为-1是顶级父组
	    userGroup.setGroupId(-2L);
        UserGroup userGroup1 = userGroupService.delete(userGroup);
        UserGroupResp userGroupResp = BeanMapper.map(userGroup1, UserGroupResp.class);
        log.debug("返回数据：{}",userGroupResp);
        return Result.buildDeleteOk(userGroupResp);
    }

	@GetMapping("/getPageAll")
	@ApiOperation(value = "分页查询分组的成员")
	//此处需要前台传来该组的数据
	public Result<MallPage<UserResp>> pageAllUserByGroup(GroupReq groupReq){
		log.debug("请求参数:{}",groupReq);
		if (StringUtils.isBlank(groupReq.getGroupNo())){
			return Result.buildParamFail();
		}

        Group group = BeanMapper.map(groupReq, Group.class);
		//首先要通过编号查询组，查询到组之后要通过编号对该组成员进行分页查询
        /*Group inGroup = userGroupService.findGroup(groupReq.getGroupNo());*/
        return null;
	}

}