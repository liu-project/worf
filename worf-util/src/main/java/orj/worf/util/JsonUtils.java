package orj.worf.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    private static final Gson gson = new GsonBuilder().create();
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        VisibilityChecker<?> visibilityChecker = mapper.getSerializationConfig().getDefaultVisibilityChecker();
        mapper.setVisibilityChecker(visibilityChecker.withFieldVisibility(Visibility.ANY)
                .withCreatorVisibility(Visibility.NONE).withGetterVisibility(Visibility.NONE)
                .withSetterVisibility(Visibility.NONE).withIsGetterVisibility(Visibility.NONE));
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
        mapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
//        mapper.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
    }

    public static <T> String toJSON(final T object) {
        try {
        	if (object == null) {
        		return null;
        	}
			if (object instanceof JsonNode) {
				JsonNode node = (JsonNode) object;
				if (node.isValueNode()) {
					return node.asText();
				}
			}
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            return gson.toJson(object);
        }
    }

    public static <T> T parseJSON(final String json, final Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            return gson.fromJson(json, clazz);
        }
    }
    
    public static <T> T parseJSON(final String json, TypeReference<T> type) {
        try {
        	return mapper.readValue(json, type);
        } catch (IOException e) {
            return gson.fromJson(json, type.getType());
        }
    }

    public static <T> T parseJSON(final String json, final Class<?> parametrized, final Class<?>... parameterClass) {
        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameterClass);
            return mapper.readValue(json, type);
        } catch (IOException e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public static JsonNode readTree(final String json) {
    	try {
			JsonNode jsonNode = mapper.readTree(json);
			return jsonNode;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    @SuppressWarnings("rawtypes")
	public static Map objectToMap(Object obj) {
    	return parseJSON(toJSON(obj), Map.class);
    }
  
}

