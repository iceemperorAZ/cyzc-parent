package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.req.GroupReq;
import com.jingliang.mall.resp.GroupResp;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.jingliang.mall.service.GroupService;
import springfox.documentation.annotations.ApiIgnore;

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
	@PostMapping("/save")
    @ApiOperation(value = "新增分组")
    public Result<GroupResp> saveGroup(@RequestBody GroupReq groupReq){
        log.debug("请求参数:{}",groupReq);
        //判断是否填写组名
        if (Objects.isNull(groupReq.getGroupName())){
            return Result.buildParamFail();
        }
        //寻找该组的父组并修改节点
		Group fartherGroup = groupService.findFartherGroup(groupReq.getParentGroupId());
		System.out.println(fartherGroup);
        //判断父组节点是否为true,若不是，修改为false
		if(!fartherGroup.getChild()){
			fartherGroup.setChild(true);
			//更新father节点
			groupService.save(fartherGroup);
		}
		//类型转换
		Group group = BeanMapper.map(groupReq, Group.class);
		//修改该组节点
		group.setChild(false);
		//判断是否有重复组名
		List<Group> groups = groupService.findAll();
		for (Group group1 : groups){
			//组名重复，无法保存
			if (Objects.equals(group1.getGroupName(),group.getGroupName())){
				return Result.buildParamFail();
			}
		}
		group = groupService.save(group);
		GroupResp groupResp = BeanMapper.map(group, GroupResp.class);
		log.debug("返回结果：{}",groupResp);
		return Result.buildSaveOk(groupResp);
    }
}