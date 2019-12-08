package orj.worf.scheduler.report.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jersey.repackaged.com.google.common.collect.Lists;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(sdf);
    }

    public static String objectToJsonStr(Object o) throws JsonGenerationException, JsonMappingException, IOException {
        return objectMapper.writeValueAsString(o);
    }

    public static <T> T jsonStrToObject(String json, Class<T> cls) throws JsonParseException, JsonMappingException,
            IOException {
        return objectMapper.readValue(json, cls);
    }

    public static <T> List<T> jsonStrToList(String jsonStr, Class<?> clazz) throws JsonParseException,
            JsonMappingException, IOException {
        List<T> list = Lists.newArrayList();
        // 指定容器结构和类型（这里是ArrayList和clazz）
        TypeFactory t = TypeFactory.defaultInstance();
        list = objectMapper.readValue(jsonStr, t.constructCollectionType(ArrayList.class, clazz));
        return list;
    }

    public static Map jsonStrToMap(String attributes) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(attributes, HashMap.class);
    }

    public static String jsonStrByKey(String attributes, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(attributes);
        return jsonObject.get(key).toString();
    }

    public static String jsonStringByKey(String attributes, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(attributes);
        return jsonObject.getString(key);
    }
}
