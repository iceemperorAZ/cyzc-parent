package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.GroupRegion;
import com.jingliang.mall.entity.Region;
import com.jingliang.mall.req.GroupRegionReq;
import com.jingliang.mall.service.GroupRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 组与区域映射关系表Controller
 * 
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:39:41
 */
@RestController
@RequestMapping(value = "/back/groupRegion")
@Slf4j
@Api(tags = "组与区域映射关系表")
public class GroupRegionController {

	private final GroupRegionService groupRegionService;

	public GroupRegionController(GroupRegionService groupRegionService) {
		this.groupRegionService = groupRegionService;
	}

	@PostMapping("/getGroupRegion")
	@ApiOperation(value = "根据组id查询映射表,获取区域id，查询对应区域")
	public Result<Region> getGroupRegion(@RequestParam("groupId") Long groupId){
		return Result.buildQueryOk(groupRegionService.findRegionByGroupId(groupId));
	}

	@PostMapping("/saveGroupRegion")
	@ApiOperation(value = "保存组与区域映射关系表")
	public Result<GroupRegion> save(@RequestBody GroupRegionReq groupRegionReq){
		//获取传递对象，并保存
		GroupRegion groupRegion = new GroupRegion();
		groupRegion.setGroupId(groupRegionReq.getGroupId());
		groupRegion.setRegionId(groupRegionReq.getRegionId());
		groupRegion.setCreateTime(new Date());
		//默认为真
		groupRegion.setIsAvailable(true);
		return Result.buildSaveOk(groupRegionService.saveGroupRegion(groupRegion));
	}

	@PostMapping("/updateGroupRegion")
	@ApiOperation(value = "修改组与区域映射关系表")
	public Result<GroupRegion> update(@RequestBody GroupRegionReq groupRegionReq){
		//获取传递对象，并保存
		GroupRegion groupRegion = new GroupRegion();
		groupRegion.setGroupId(groupRegionReq.getGroupId());
		groupRegion.setRegionId(groupRegionReq.getRegionId());
		groupRegion.setCreateTime(new Date());
		//获取数据库中的可用性
		groupRegion.setIsAvailable(groupRegionReq.getIsAvailable());
		return Result.buildUpdateOk(groupRegionService.saveGroupRegion(groupRegion));
	}

	@PutMapping("/updateIs")
	@ApiOperation(value = "删除组和区域的绑定关系（修改可用性）")
	public Result<GroupRegion> updateIs(@RequestBody GroupRegion groupRegion){
		return Result.buildDeleteOk(groupRegionService.updateIsAvailable(groupRegion));
	}


}