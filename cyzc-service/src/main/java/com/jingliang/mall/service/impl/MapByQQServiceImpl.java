package com.jingliang.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.entity.AddressUserHistory;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.repository.AddressUserHistoryRepository;
import com.jingliang.mall.repository.BuyerAddressRepository;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.RegionRepository;
import com.jingliang.mall.service.MapService;
import com.jingliang.mall.utils.ReadExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jingliang.mall.utils.MultipartFileToFile.delteTempFile;
import static com.jingliang.mall.utils.MultipartFileToFile.multipartFileToFile;
import static com.jingliang.mall.utils.StaticField.*;


/**
 * @author lmd
 * @date 2020/4/22
 * @company
 */
@Service
@Slf4j
public class MapByQQServiceImpl implements MapService {

    private final BuyerAddressRepository buyerAddressRepository;
    private final AddressUserHistoryRepository addressUserHistoryRepository;
    private final RegionRepository regionRepository;
    private final BuyerRepository buyerRepository;

    public MapByQQServiceImpl(BuyerAddressRepository buyerAddressRepository, AddressUserHistoryRepository addressUserHistoryRepository, RegionRepository regionRepository, BuyerRepository buyerRepository) {
        this.buyerAddressRepository = buyerAddressRepository;
        this.addressUserHistoryRepository = addressUserHistoryRepository;
        this.regionRepository = regionRepository;
        this.buyerRepository = buyerRepository;
    }

    /**
     * 通过地址获取经纬度
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getlngAndLat(MultipartFile multipartFile) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        //MultipartFile转换为File
        File file = multipartFileToFile(multipartFile);
        //调用读取excel表格方法
        List<String> listExcel = ReadExcelUtil.readExcel(file);
        assert listExcel != null;
        //遍历获取单个地址
        for (String address : listExcel) {
            Map<String, Object> map = new HashMap<>();
            String json = loadJSON(address);
            //判断json是否为null或者是空字符串
            if (StringUtils.isNotBlank(json)) {
                //转成json
                JSONObject jsonObject = JSON.parseObject(json);
                String status = jsonObject.getString(STATUS);
                if ("0".equals(status)) {
                    Double lng = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LNG); // 经度
                    Double lat = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LAT); // 纬度
                    log.debug("返回结果：{}", "地址：" + address + "----纬度：" + lat + "-----经度：" + lng);
                    map.put(ADDRESS, address);
                    map.put(LONGITUDE, lng);
                    map.put(LATITUDE, lat);
                    listMap.add(map);
                }
            }
            delteTempFile(file);
        }
        return listMap;
    }


    /**
     * 获取所有以绑定销售员的商户的默认地址
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findAlldefaultBuyerAddress(Specification<Buyer> buyerSpecification) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<BuyerAddress> buyerAddressList = buyerAddressRepository.findAll();
        for (BuyerAddress buyerAddress : buyerAddressList) {
            if (buyerAddress.getIsDefault()) {
                //获取省份
                String province = regionRepository.findRegionByCodeAndIsAvailable(buyerAddress.getProvinceCode(), true).getName();
                //获取市
                String city = regionRepository.findRegionByCodeAndIsAvailable(buyerAddress.getCityCode(), true).getName();
                //获取区
                String area = regionRepository.findRegionByCodeAndIsAvailable(buyerAddress.getAreaCode(), true).getName();
                //获取具体地址
                String detailAddress = buyerAddress.getDetailedAddress();
                Map<String, Object> map = new HashMap<>();
                //拼接获得完整地址
                String address = province + city + area + detailAddress;
                //判断商户地址是否为空
                if (StringUtils.isNotBlank(address)) {
                    String json = loadJSON(address);
                    //判断json是否为null或者是空字符串
                    if (StringUtils.isNotBlank(json)) {
                        //转成json
                        JSONObject jsonObject = JSON.parseObject(json);
                        String status = jsonObject.getString(STATUS);
                        if ("0".equals(status)) {
                            Double lng = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LNG); // 经度
                            Double lat = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LAT); // 纬度
                            map.put(ADDRESS, address);
                            map.put(LONGITUDE, lng);
                            map.put(LATITUDE, lat);
                            mapList.add(map);
                        }
                    }
                }
            }
        }
        return mapList;
    }

    /**
     * 保存商户经纬度记录
     *
     * @return
     */
    @Override
    public AddressUserHistory saveMap(AddressUserHistory addressUserHistory) {
        return addressUserHistoryRepository.save(addressUserHistory);
    }

    /**
     * 通过商户id获取经纬度记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<AddressUserHistory> readMap(Long userId) {
        return addressUserHistoryRepository.findAllByUserIdAndIsAvailableOrderByCreateTimeAsc(userId, true);
    }

    @Transactional
    @Override
    public Boolean getAllMapToSave() {
        //获取所有商户地址
        List<BuyerAddress> buyerAddressList = buyerAddressRepository.findAll();
        for (BuyerAddress buyerAddress : buyerAddressList) {
            if (buyerAddress.getIsDefault()) {
                if (buyerAddress.getLongitude() == null || buyerAddress.getLatitude() == null) {
                    //获取省份
                    String province = regionRepository.findRegionByCodeAndIsAvailable(buyerAddress.getProvinceCode(), true).getName();
                    //获取市
                    String city = regionRepository.findRegionByCodeAndIsAvailable(buyerAddress.getCityCode(), true).getName();
                    //获取区
                    String area = regionRepository.findRegionByCodeAndIsAvailable(buyerAddress.getAreaCode(), true).getName();
                    //获取具体地址
                    String detailAddress = buyerAddress.getDetailedAddress();
                    //拼接获得完整地址
                    String address = province + city + area + detailAddress;
                    //判断商户地址是否为空
                    if (StringUtils.isNotBlank(address)) {
                        String json = loadJSON(address);
                        //判断json是否为null或者是空字符串
                        if (StringUtils.isNotBlank(json)) {
                            //转成json
                            JSONObject jsonObject = JSON.parseObject(json);
                            String status = jsonObject.getString(STATUS);
                            if ("0".equals(status)) {
                                Double lng = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LNG); // 经度
                                Double lat = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LAT); // 纬度
                                buyerAddressRepository.updateLngAndLatById(buyerAddress.getId(), lng, lat);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 根据组编号查询商户默认地址
     *
     * @param groupNo
     * @return
     */
    @Override
    public List<Map<String, Object>> findAddressByGroupNo(String groupNo) {
        //使用正则表达式将组编码后面的0移除 0100000000  0101000000  0101010000,如果不传值，则查询全部
        groupNo = groupNo.replaceAll("0*$", "") + "%";
        return buyerAddressRepository.findAddressByGroupNo(groupNo);
    }


    /**
     * 解析url地址
     *
     * @param address
     * @return
     */
    public String loadJSON(String address) {
        //腾讯地图API
//        String key = "DXCBZ-ICIWF-JL5JP-J64EA-CBMEJ-JTFKP"; //个人key
        String key = "5BOBZ-SOWRW-DO7RT-OLMIO-ZCZNV-TIBEQ";
        String url = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key=" + key;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //TODO 因为腾讯地图api调用的key是个人的，并发上限5，所以在此让线程睡眠来保证可以获取到数据
            // 这个睡眠值是根据excel中地址数测试得到的，不具有通用性
//            Thread.sleep(1000);
            URL url1 = new URL(url);
            URLConnection yc = url1.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }


}
