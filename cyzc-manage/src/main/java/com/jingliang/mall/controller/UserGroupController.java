package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserGroup;
import com.jingliang.mall.req.GroupReq;
import com.jingliang.mall.req.UserGroupReq;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.UserGroupResp;
import com.jingliang.mall.resp.UserResp;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.Api;
import com.jingliang.mall.service.UserGroupService;

import java.util.Objects;

/**
 * 员工与组关系映射表Controller
 * 
 * @author XiaoBing Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/userGroup")
@Api(description = "员工与组关系映射表")
public class UserGroupController {

	private final UserGroupService userGroupService;


	public UserGroupController(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	@PostMapping("/save")
	@ApiOperation(description = "更新/增加新组员")
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
    @ApiOperation(description = "逻辑删除组员")
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
	@ApiOperation(description = "分页查询分组的成员")
	//此处需要前台传来该组的数据
	public Result<MallPage<UserResp>> pageAllUserByGroup(GroupReq groupReq){
		log.debug("请求参数:{}",groupReq);
		if (StringUtils.isBlank(groupReq.getGroupNo())){
			return Result.buildParamFail();
		}
        Group group = BeanMapper.map(groupReq, Group.class);
		//通过组编号分页查询用户数据
		Page<User> userByGroupNo = userGroupService.findUserByGroupNo(group.getGroupNo());
		//进行转换
		MallPage<UserResp> userResp = MallUtils.toMallPage(userByGroupNo, UserResp.class);
		log.debug("返回数据：{}",userResp);
		return Result.buildQueryOk(userResp);
	}

}