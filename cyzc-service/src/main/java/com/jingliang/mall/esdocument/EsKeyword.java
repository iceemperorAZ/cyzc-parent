//package com.jingliang.mall.esdocument;
//
//import lombok.Data;
//import org.springframework.data.elasticsearch.annotations.*;
//
//import javax.persistence.Id;
//
///**
// * elasticsearch中的搜索词
// *
// * @author Zhenfeng Li
// * @version 1.0
// * @date 2019-10-05 13:56
// */
//@Document(indexName = "tob_keywords", type = "tob_keywords")
//@Setting(settingPath = "/json/keyword-setting.json")
//@Mapping(mappingPath = "/json/keyword-mapping.json")
//@Data
//public class EsKeyword {
//    /**
//     * 主键Id
//     */
//    @Id
//    @Field(type = FieldType.Long)
//    private Long id;
//
//    /**
//     * 搜索词
//     */
//    @Field(type = FieldType.Text)
//    private String keyword;
//}
