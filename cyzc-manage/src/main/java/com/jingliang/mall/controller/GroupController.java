package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.req.GroupReq;
import com.jingliang.mall.resp.GroupResp;
import com.jingliang.mall.service.GroupService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组Controller
 *
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:18:08
 */
@Api(tags = "组")
@RestController
@Slf4j
@RequestMapping(value = "/back/group")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @PostMapping("/groupAll")
    @ApiOperation(value = "查询所有可用的组")
    public List<Group> groupAll() {
        List<Group> list = groupService.findGroupAll();
        return list;
    }

    @GetMapping("/getFatherGroup")
    @ApiOperation(value = "查询出顶级父组")
    public Result<GroupResp> getAllGroup() {
        log.debug("请求参数：先进行查询操作。");
        //获取分组表中的父级数据
        Group group = groupService.getFatherGroup();
        //开始判断组类型
        if (Objects.equals(0, group.getIsAvailable())) {
            //顶级父级组处于不可用状态
            return Result.build(Msg.FAIL, Msg.TEXT_IS_AVAILABLE_FAIL);
        }
        //类型转换
        GroupResp groupResp = BeanMapper.map(group, GroupResp.class);
        //查询成功
        log.debug("返回结果：{}", groupResp);
        return Result.buildQueryOk(groupResp);
    }

    @GetMapping("/getGroupWithFather")
    @ApiOperation(value = "查询顶级父组的分组")
    public Result<List<GroupResp>> getGroupWithFatherGroup(GroupReq groupReq) {
        log.debug("请求参数：{}", groupReq);
        //查询该父组下的所有分组
        List<Group> groupWithFathers = groupService.getGroupWithFather(groupReq.getParentGroupId(), groupReq.getIsAvailable());
        //进行判断
        for (Group group : groupWithFathers) {
            //表示组不可用
            if (Objects.equals(0, group.getIsAvailable())) {
                return Result.build(Msg.FAIL, Msg.TEXT_IS_AVAILABLE_FAIL);
            }
        }
        //类型转换
        List<GroupResp> groupResps = BeanMapper.mapList(groupWithFathers, GroupResp.class);
        log.debug("返回结果：{}", groupResps);
        return Result.buildQueryOk(groupResps);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增分组")
    public Result<GroupResp> saveGroup(@RequestBody GroupReq groupReq) {
        log.debug("请求参数:{}", groupReq);
        //判断是否填写组名
        if (Objects.isNull(groupReq.getGroupName()) || groupReq.getParentGroupId() == null) {
            return Result.buildParamFail();
        }
        //寻找该组的父组并修改节点
        Group fartherGroup = groupService.findFartherGroup(groupReq.getParentGroupId());
        //判断父组节点是否为true,若不是，修改为false
        if (fartherGroup != null && !fartherGroup.getChild()) {
            fartherGroup.setChild(true);
            //更新father节点
            groupService.save(fartherGroup);
        }
        //判断同级分组是否有重复组名
        Group group = groupService.findByGroupNameAndParentId(groupReq.getGroupName(), groupReq.getParentGroupId());
        if (group != null && !group.getId().equals(groupReq.getId())) {
            return Result.build(Msg.FAIL, "组名重复");
        }
        //判断组编号否有重复
        group = groupService.findByGroupNo(groupReq.getGroupNo());
        if (group != null && !group.getId().equals(groupReq.getId())) {
            return Result.build(Msg.FAIL, "组编号重复");
        }
        //类型转换
        group = BeanMapper.map(groupReq, Group.class);
        assert group != null;
        if (group.getId() == null) {
            //修改该组节点
            group.setChild(true);

        }
        group.setIsAvailable(true);
        group = groupService.save(group);
        GroupResp groupResp = BeanMapper.map(group, GroupResp.class);
        log.debug("返回结果：{}", groupResp);
        return Result.buildSaveOk(groupResp);
    }

    /**
     * 根据组名或编号模糊查询
     */
    @ApiOperation(value = "根据组名或编号模糊查询")
    @GetMapping("/like/search")
    public Result<List<GroupResp>> likeSearch(String search) {
        return Result.buildQueryOk(BeanMapper.mapList(groupService.likeSearch(search), GroupResp.class));
    }


    /**
     * 合并分组(包括子组一同合并到指定的组)
     */
    @PostMapping("/merge")
    @ApiOperation(value = "合并分组")
    public Result<Boolean> mergeGroups(@RequestBody Map<String, Object> map) {
        String groupNo = (String) map.get("groupNo");
        List<String> mergeGroupNos = ((List<?>) map.get("mergeGroupNos")).stream().map(p -> String.valueOf(p.toString())).collect(Collectors.toList());
        groupService.mergeGroups(groupNo, mergeGroupNos);
        return Result.build(Msg.OK, "操作成功", true);
    }

}