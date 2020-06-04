package com.jingliang.mall.controller;

import com.jingliang.mall.common.ExcelUtils;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.BuyerManageService;
import com.jingliang.mall.service.GroupService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lmd
 * @date 2020/5/11
 * @company 晶粮
 */
@Api(tags = "商户管理")
@RestController
@RequestMapping("/buyerManage")
@Slf4j
public class BuyerManageController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final BuyerManageService buyerManageService;
    private final UserService userService;
    private final GroupService groupService;

    public BuyerManageController(BuyerManageService buyerManageService, UserService userService, GroupService groupService) {
        this.buyerManageService = buyerManageService;
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * 根据父分组分组统计商户增加量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/date/parentGroupId/achievement")
    @ApiOperation(value = "根据父分组分组统计商户增加量")
    public Result<List<Map<String, Object>>> dateAndGroupNoAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                       Long parentGroupId,
                                                                       HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //如果未传父分组编号，则赋值-1L
        if (parentGroupId == null) {
            parentGroupId = -1L;
            List<Map<String, Object>> counts = buyerManageService.dateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
            return Result.buildQueryOk(counts);
        }
        //通过父组id查询
        List<Map<String, Object>> counts = buyerManageService.dateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
        return Result.buildQueryOk(counts);
    }

    /**
     * 查询组内销售名下的商户数
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @GetMapping("/user/groupNo/buyerCounts")
    @ApiOperation(value = "查询组内销售名下的商户数")
    public Result<List<Map<String, Object>>> userByGroupNoAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                      String groupNo,
                                                                      HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        List<Map<String, Object>> counts = buyerManageService.userByGroupNoAchievement(startTime, endTime, groupNo);
        return Result.buildQueryOk(counts);
    }

    /**
     * 根据父id查询子区在每年的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @GetMapping("/year/parentGroupId/buyerCounts")
    @ApiOperation(value = "根据父id查询子区在每年的用户量")
    public Result<?> yearByDateAndParentGroupIdAchievement(Long parentGroupId,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                           HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> counts = buyerManageService.yearByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
            log.debug("返回参数:{}", counts);
            Set<Object> date = counts.stream().map(stringObjectMap -> stringObjectMap.get("days")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            counts.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 根据父id查询子区在每月的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @GetMapping("/month/parentGroupId/buyerCounts")
    @ApiOperation(value = "根据父id查询子区在每月的用户量")
    public Result<?> monthByDateAndParentGroupIdAchievement(Long parentGroupId,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                            HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> counts = buyerManageService.monthByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
            log.debug("返回参数:{}", counts);
            Set<Object> date = counts.stream().map(stringObjectMap -> stringObjectMap.get("days")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            counts.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 根据父id查询子区在每天的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @GetMapping("/day/parentGroupId/buyerCounts")
    @ApiOperation(value = "根据父id查询子区在每天的用户量")
    public Result<?> daysByDateAndParentGroupIdAchievement(Long parentGroupId,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                           HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> counts = buyerManageService.daysByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
            log.debug("返回参数:{}", counts);
            Set<Object> date = counts.stream().map(stringObjectMap -> stringObjectMap.get("days")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            counts.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 根据组编号查询该组在每年的用户量
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @GetMapping("/year/groupNo/buyerCounts")
    @ApiOperation(value = "根据组编号查询该组在每年的用户量")
    public Result<?> yearByDateAndParentGroupIdAchievement(String groupNo,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                           HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        if (!Objects.isNull(groupNo)) {
            List<Map<String, Object>> counts = buyerManageService.yearByDateAndGroupNoAchievement(startTime, endTime, groupNo);
            log.debug("返回参数:{}", counts);
            Set<Object> date = counts.stream().map(stringObjectMap -> stringObjectMap.get("days")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            counts.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 根据组编号查询该组在每月的用户量
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @GetMapping("/month/groupNo/buyerCounts")
    @ApiOperation(value = "根据组编号查询该组在每月的用户量")
    public Result<?> monthByDateAndGroupNoAchievement(String groupNo,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                      HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        if (!Objects.isNull(groupNo)) {
            List<Map<String, Object>> counts = buyerManageService.monthByDateAndGroupNoAchievement(startTime, endTime, groupNo);
            log.debug("返回参数:{}", counts);
            Set<Object> date = counts.stream().map(stringObjectMap -> stringObjectMap.get("days")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            counts.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 根据组编号查询该组在每天的用户量
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @GetMapping("/day/groupNo/buyerCounts")
    @ApiOperation(value = "根据组编号查询该组在每天的用户量")
    public Result<?> daysByDateAndGroupNoAchievement(String GroupNo,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                     HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        if (!Objects.isNull(GroupNo)) {
            List<Map<String, Object>> counts = buyerManageService.daysByDateAndGroupNoAchievement(startTime, endTime, GroupNo);
            log.debug("返回参数:{}", counts);
            Set<Object> date = counts.stream().map(stringObjectMap -> stringObjectMap.get("days")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            counts.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 当前年销售下单量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    @ApiOperation(value = "当前年销售下单量top")
    @GetMapping("/topOrderCounts")
    public Result<List<Map<String, Object>>> topOfOrderCountsByUser(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                    @RequestParam(defaultValue = "5") Integer topNum,
                                                                    HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        List<Map<String, Object>> mapList = buyerManageService.topOfOrderCountsByUser(startTime, endTime, topNum);
        return Result.buildQueryOk(mapList);
    }

    /**
     * 当前年商品销售量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    @ApiOperation(value = "当前年商品销售量top")
    @GetMapping("/year/topProductCounts")
    public Result<List<Map<String, Object>>> yeartopOfProductCountsByOrder(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                           @RequestParam(defaultValue = "5") Integer topNum,
                                                                           HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        List<Map<String, Object>> mapList = buyerManageService.yeartopOfProductCountsByOrder(startTime, endTime, topNum);
        return Result.buildQueryOk(mapList);
    }

    /**
     * 当前月商品销售量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    @ApiOperation(value = "当前月商品销售量top")
    @GetMapping("/month/topProductCounts")
    public Result<List<Map<String, Object>>> monthtopOfProductCountsByOrder(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                            @RequestParam(defaultValue = "5") Integer topNum,
                                                                            HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        List<Map<String, Object>> mapList = buyerManageService.monthtopOfProductCountsByOrder(startTime, endTime, topNum);
        return Result.buildQueryOk(mapList);
    }

    /**
     * 销售新增商户统计
     *
     * @return
     */
    @GetMapping("/countsByUserId")
    public Result<List<Map<String, String>>> countsByUserId() {
        List<Map<String, String>> maps = buyerManageService.countsByUserId();
        log.debug("返回参数:{}", maps);
        return Result.buildOk(maps);
    }

    /**
     * 查询全部可用的销售
     *
     * @return
     */
    @GetMapping("/searchAllBuyer")
    @ApiOperation(value = "查询全部可用的销售")
    public Result<List<Map<String, Object>>> searchAllBuyer() {
        List<Map<String, Object>> maps = buyerManageService.searchAllBuyer();
        log.debug("返回参数:{}", maps);
        return Result.buildOk(maps);
    }

    /**
     * 导出销售新增商户统计的excel
     *
     * @return
     */
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> download() throws IOException {
        List<Map<String, String>> maps = buyerManageService.countsByUserId();
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("销售新增商户统计", Msg.buyerCountsToUserExcelTitle);
        XSSFSheet sheet = orderWorkbook.getSheet("销售新增商户统计");
        XSSFCellStyle cellStyle = orderWorkbook.createCellStyle();
        int rowNum = 1;
        for (Map<String, String> map : maps) {
            XSSFRow row = sheet.createRow(rowNum);
            /*
             * "时间","新增数量","销售员ID","销售员","区域经理","区域"
             * */
            int celNum = 0;
            //时间
            row.createCell(celNum).setCellValue(map.get("createTime"));
            //新增数量
            row.createCell(++celNum).setCellValue(map.get("counts"));
            //销售员ID
            row.createCell(++celNum).setCellValue(map.get("userId"));
            //销售员
            row.createCell(++celNum).setCellValue(map.get("userName"));
            //区域经理
            row.createCell(++celNum).setCellValue(map.get("manageName"));
            //区域
            row.createCell(++celNum).setCellValue(map.get("groupName"));
            //创建一行
            row = sheet.createRow(++rowNum);
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        orderWorkbook.write(arrayOutputStream);
        String newName = URLEncoder.encode("销售新增商户统计-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".xlsx", "utf-8")
                .replaceAll("\\+", "%20").replaceAll("%28", "\\(")
                .replaceAll("%29", "\\)").replaceAll("%3B", ";")
                .replaceAll("%40", "@").replaceAll("%23", "\\#")
                .replaceAll("%26", "\\&").replaceAll("%2C", "\\,");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", newName));
        headers.add("Expires", "0");
        headers.add("Pragma", "no-cache");
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(arrayOutputStream.size())
                .body(arrayOutputStream.toByteArray());
    }

    /*
     *
     *  查询未绑定销售的商户数
     * * */
    @GetMapping("/searchBuyerDontHaveSale")
    @ApiOperation(value = "查询未绑定销售的商户")
    public Result<?> searchBuyerDontHaveSale() {
        List<Map<String, Object>> buyers = buyerManageService.searchBuyerDontHaveSale();
        log.debug("返回参数:{}", buyers);
        return Result.buildQueryOk(buyers);
    }

    /*
     *
     *  查询绑定销售的商户数
     * * */
    @GetMapping("/searchBuyerHaveSale")
    @ApiOperation(value = "查询未绑定销售的商户")
    public Result<?> searchBuyerHaveSale() {
        List<Map<String, Object>> buyers = buyerManageService.searchBuyerHaveSale();
        log.debug("返回参数:{}", buyers);
        return Result.buildQueryOk(buyers);
    }
}
