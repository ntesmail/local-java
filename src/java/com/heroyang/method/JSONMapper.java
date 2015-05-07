package com.heroyang.method;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONMapper {
	private static final JSONMapper DEFAULT_INSTANCE = new JSONMapper();

    private static ObjectMapper mapper;
    
    public JSONMapper() {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
            false);
    }

    public static JSONMapper get() {
        return DEFAULT_INSTANCE;
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
     * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: List<MyBean> beanList =
     * mapper.getMapper().readValue(listString, new
     * TypeReference<List<MyBean>>() {});
     */
    public <T> T fromJSON(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T fromJSON(String jsonString, TypeReference<T> tf) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, tf);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
     */
    public static String toJSON(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 设置转换日期类型的format pattern,如果不设置默认打印Timestamp毫秒数.
     */
    public void setDateFormat(String pattern) {
        if (StringUtils.isNotBlank(pattern)) {
            DateFormat df = new SimpleDateFormat(pattern);
            mapper.setDateFormat(df);
        }
    }

    public void setDateFormat(DateFormat df) {
        if (null != df) {
            mapper.setDateFormat(df);
        }
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }


}
