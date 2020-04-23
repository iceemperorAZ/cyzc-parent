package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.req.GroupReq;
import com.jingliang.mall.resp.GroupResp;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.jingliang.mall.service.GroupService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * 组Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@Api(tags = "组")
@RestController
@Slf4j
@RequestMapping(value = "/back/group")
public class GroupController {

	private final GroupService groupService;

	public GroupController (GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping("/getFatherGroup")
	@ApiOperation(value = "查询出顶级父组")
	public Result<GroupResp> getAllGroup(){
		log.debug("请求参数：先进行查询操作。");
		//获取分组表中的父级数据
		Group group = groupService.getFatherGroup();
		//开始判断组类型
		if (Objects.equals(0,group.getIsAvailable())){
			//顶级父级组处于不可用状态
			return Result.build(Msg.FAIL,Msg.TEXT_IS_AVAILABLE_FAIL);
		}
		//类型转换
		GroupResp groupResp = BeanMapper.map(group, GroupResp.class);
		//查询成功
		log.debug("返回结果：{}",groupResp);
		return Result.buildQueryOk(groupResp);
	}
	@GetMapping("/getGroupWithFather")
	@ApiOperation(value = "查询顶级父组的分组")
	public Result<List<GroupResp>> getGroupWithFatherGroup(GroupReq groupReq){
		log.debug("请求参数：{}",groupReq);
		//查询该父组下的所有分组
		List<Group> groupWithFathers = groupService.getGroupWithFather(groupReq.getParentGroupId(),groupReq.getIsAvailable());
		//进行判断
		for (Group group : groupWithFathers){
			//表示组不可用
			if (Objects.equals(0,group.getIsAvailable())){
				return Result.build(Msg.FAIL,Msg.TEXT_IS_AVAILABLE_FAIL);
			}
		}
		//类型转换
		List<GroupResp> groupResps = BeanMapper.mapList(groupWithFathers, GroupResp.class);
		log.debug("返回结果：{}",groupResps);
		return Result.buildQueryOk(groupResps);
	}
}