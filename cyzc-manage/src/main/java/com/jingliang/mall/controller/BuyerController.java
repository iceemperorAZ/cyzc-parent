package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerSaleService;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.GoldLogService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
@Api(tags = "会员")
@RequestMapping("/back/buyer")
@RestController("backBuyerController")
@Slf4j
public class BuyerController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    @Value("${token.buyer.redis.prefix}")
    private String tokenBuyerPrefix;

    private final BuyerService buyerService;
    private final RedisService redisService;
    private final UserService userService;
    private final BuyerSaleService buyerSaleService;
    private final GoldLogService goldLogService;

    public BuyerController(BuyerService buyerService, RedisService redisService, UserService userService, BuyerSaleService buyerSaleService, GoldLogService goldLogService) {
        this.buyerService = buyerService;
        this.redisService = redisService;
        this.userService = userService;
        this.buyerSaleService = buyerSaleService;
        this.goldLogService = goldLogService;
    }

    /**
     * 修改会员信息
     */
    @ApiOperation(value = "修改会员信息")
    @PostMapping("/save")
    public MallResult<BuyerResp> save(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        if (Objects.isNull(buyerReq.getId())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        if (buyerReq.getSaleUserId() != null) {
            User user = userService.findById(buyerReq.getSaleUserId());
            if (Objects.isNull(user)) {
                return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_BUYER_FAIL);
            }
        }
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (Objects.isNull(buyer)) {
            return MallResult.build(MallConstant.DATA_FAIL, MallConstant.TEXT_BUYER_DATA_FAIL);
        }
        //修改会员信息后清空redis中的会员token
        redisService.remove(tokenBuyerPrefix + buyer.getId());
        BuyerResp buyerResp = MallBeanMapper.map(buyerService.save(MallBeanMapper.map(buyerReq, Buyer.class)), BuyerResp.class);
        log.debug("返回结果：{}", buyerResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_UPDATE_OK, buyerResp);
    }

    /**
     * 重新绑定销售
     */
    @ApiOperation(value = "重新绑定销售")
    @PostMapping("/update/saleUser")
    public MallResult<BuyerResp> updateSaleUser(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        if (Objects.isNull(buyerReq.getId())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (Objects.isNull(buyer)) {
            return MallResult.build(MallConstant.DATA_FAIL, MallConstant.TEXT_BUYER_DATA_FAIL);
        }
        BuyerSale buyerSale = buyerSaleService.findBySaleIdAndBuyerId(buyer.getSaleUserId(), buyer.getId());
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        if (buyerSale != null) {
            buyerSale.setUntyingTime(date);
            buyerSale.setUpdateTime(date);
            buyerSale.setUpdateUser(user.getUserName());
            buyerSale.setUpdateUserId(user.getId());
            buyerSaleService.save(buyerSale);
        }
        //更换销售后修改最后一次下单时间
        buyerReq.setLastOrderTime(date);
        //修改会员信息后清空redis中的会员token
        redisService.remove(tokenBuyerPrefix + buyer.getId());
        buyerReq.setLastOrderTime(date);
        BuyerResp buyerResp = MallBeanMapper.map(buyerService.save(MallBeanMapper.map(buyerReq, Buyer.class)), BuyerResp.class);
        buyerSale = new BuyerSale();
        buyerSale.setBuyerId(buyer.getId());
        buyerSale.setSaleId(buyerReq.getSaleUserId());
        buyerSale.setIsAvailable(true);
        buyerSale.setCreateTime(date);
        buyerSale.setUpdateTime(date);
        buyerSale.setUpdateTime(date);
        buyerSale.setUpdateTime(date);
        buyerSale = buyerSaleService.save(buyerSale);
        log.debug("返回结果：{}", buyerResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_UPDATE_OK, buyerResp);
    }

    /**
     * 分页查询全部会员
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部会员")
    public MallResult<MallPage<BuyerResp>> pageAllProduct(BuyerReq buyerReq) {
        log.debug("请求参数：{}", buyerReq);
        PageRequest pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        if (StringUtils.isNotBlank(buyerReq.getClause())) {
            pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize(), Sort.by(BaseMallUtils.separateOrder(buyerReq.getClause())));
        }
        Specification<Buyer> buyerSpecification = (Specification<Buyer>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (buyerReq.getId() != null) {
                predicateList.add(cb.equal(root.get("id"), buyerReq.getId()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<Buyer> buyerPage = buyerService.findAll(buyerSpecification, pageRequest);
        MallPage<BuyerResp> buyerRespMallPage = BaseMallUtils.toMallPage(buyerPage, BuyerResp.class);
        log.debug("返回结果：{}", buyerRespMallPage);
        return MallResult.buildQueryOk(buyerRespMallPage);
    }

    /**
     * 导出excel
     */
    @ApiOperation(value = "导出商户信息excel")
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> download(BuyerReq buyerReq) throws IOException {
        Specification<Buyer> buyerSpecification = (Specification<Buyer>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (buyerReq.getCreateTimeStart() != null && buyerReq.getCreateTimeEnd() != null) {
                predicateList.add(cb.between(root.get("createTime"), buyerReq.getCreateTimeStart(), buyerReq.getCreateTimeEnd()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.isNull(root.get("saleUserId")));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("createTime")));
            return query.getRestriction();
        };
        List<Buyer> buyers = buyerService.findAll(buyerSpecification);
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("商户信息", MallConstant.buyerExcelTitle);
        XSSFSheet sheet = orderWorkbook.getSheet("商户信息");
        XSSFCellStyle cellStyle = orderWorkbook.createCellStyle();
        CreationHelper createHelper = orderWorkbook.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
        int rowNum = 1;
        for (Buyer buyer : buyers) {
            XSSFRow row = sheet.createRow(rowNum);
            //"主键ID", "手机号", "注册时间"
            //主键ID
            int celNum = 0;
            Long id = buyer.getId();
            row.createCell(celNum).setCellValue(id);
            //手机号
            String phone = buyer.getPhone();
            row.createCell(++celNum).setCellValue(phone);
            //注册时间
            Date createTime = buyer.getCreateTime();
            XSSFCell cell = row.createCell(++celNum);
            cell.setCellValue(createTime);
            cell.setCellStyle(cellStyle);
            rowNum++;
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        orderWorkbook.write(arrayOutputStream);
        String newName = URLEncoder.encode("商户信息-" + new SimpleDateFormat("yyyy-MM-dd").format(buyerReq.getCreateTimeStart()) + " ~ " + new SimpleDateFormat("yyyy-MM-dd").format(buyerReq.getCreateTimeEnd()) + ".xlsx", "utf-8")
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


    /**
     * 修改剩余返利次数
     */
    @PostMapping("/order/specific/num")
    @ApiOperation(value = "修改剩余返利次数")
    public MallResult<Boolean> orderSpecificNum(Map<String, String> map) {
        Long buyerId = Long.parseLong(map.get("buyerId"));
        Integer num = Integer.parseInt(map.get("num"));
        Buyer buyer = buyerService.findById(buyerId);
        buyer.setUpdateTime(new Date());
        buyer.setOrderSpecificNum(num);
        buyerService.save(buyer);
        return MallResult.buildSaveOk(true);
    }


}