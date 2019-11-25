package com.jingliang.mall.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 返回对象
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "MallResult", description = "返回结果")
public class MallResult<T> implements Serializable {
    private static final long serialVersionUID = -2492072809889519824L;

    /**
     * 定义jackson对象
     */
    @ApiModelProperty(hidden = true)
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 响应状态
     */
    @ApiModelProperty(value = "响应状态")
    private Integer code;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息")
    private String msg;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据", notes = "响应数据")
    private T data;

    public static <T> MallResult<T> build(Integer code, String msg, T data) {
        return new MallResult<T>(code, msg, data);
    }

    public static <T> MallResult<T> buildOk(T data) {
        return new MallResult<T>(data);
    }

    public static <T> MallResult<T> buildOk() {
        return new MallResult<T>(null);
    }

    public MallResult() {

    }

    public static <T> MallResult<T> build(Integer code, String msg) {
        return new MallResult<T>(code, msg, null);
    }

    /**
     * build参数不全返回结果
     */
    public static <T> MallResult<T> buildParamFail() {
        return new MallResult<T>(MallConstant.PARAM_FAIL, MallConstant.TEXT_PARAM_FAIL, null);
    }

    /**
     * build保存成功返回结果
     */
    public static <T> MallResult<T> buildSaveOk(T data) {
        return new MallResult<T>(MallConstant.OK, MallConstant.TEXT_SAVE_OK, data);
    }

    /**
     * build修改成功返回结果
     */
    public static <T> MallResult<T> buildUpdateOk(T data) {
        return new MallResult<T>(MallConstant.OK, MallConstant.TEXT_UPDATE_OK, data);
    }

    /**
     * build删除成功返回结果
     */
    public static <T> MallResult<T> buildDeleteOk(T data) {
        return new MallResult<T>(MallConstant.OK, MallConstant.TEXT_DELETE_OK, data);
    }

    /**
     * build保存失败返回结果
     */
    public static <T> MallResult<T> buildSaveFail() {
        return new MallResult<T>(MallConstant.SAVE_FAIL, MallConstant.TEXT_SAVE_FAIL, null);
    }

    /**
     * build查询成功返回结果
     */
    public static <T> MallResult<T> buildQueryOk(T data) {
        return new MallResult<T>(MallConstant.OK, MallConstant.TEXT_QUERY_OK, data);
    }

    public MallResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public MallResult(T data) {
        this.code = MallConstant.OK;
        this.msg = MallConstant.TEXT_OK;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为E3Result对象
     *
     * @param jsonData json数据
     * @param clazz    E3Result中的object类型
     * @return
     */
    public static <T> MallResult formatToPojo(String jsonData, Class<T> clazz) {
        try {
            if (clazz == null) {
                return mapper.readValue(jsonData, MallResult.class);
            }
            JsonNode jsonNode = mapper.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            T obj = null;
            if (data.isObject()) {
                obj = mapper.readValue(data.traverse(), clazz);
            } else if (data.isTextual()) {
                obj = mapper.readValue(data.asText(), clazz);
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static MallResult format(String json) {
        try {
            return mapper.readValue(json, MallResult.class);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz    集合中的类型
     * @return
     */
    public static <T> MallResult formatToList(String jsonData, Class<T> clazz) {
        try {
            JsonNode jsonNode = mapper.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            T obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = mapper.readValue(data.traverse(),
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText());
        } catch (Exception e) {
            return null;
        }
    }
}
