package abk.com.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Map;

/**
 * Created by user on 4/06/2016.
 */
public class DbObjectUtils {
    //TODO: not sure if ObjectMapper is thread safe!!!!!!!!!!!!!!
    private static final ObjectMapper mapper = new ObjectMapper();

    public static final String DB_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final String DB_DATE_FORMAT_STRING_SHORT = "yyyy-MM-dd";

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

//        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

//        setDateFormat(new DateFormatExt(DB_DATE_FORMAT_STRING, DB_DATE_FORMAT_STRING_SHORT));

        /**
         * ALLOW COMMENTS IN JSON FILES
         * Two types of comments can be used  /* *\/ and //
         */
        mapper.getFactory().enable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    public static void setDateFormat(DateFormat format) {
        mapper.setDateFormat(format);
    }

    public static Map<String, Object> jsonToMap(byte[] json) {
        return jsonToObject(json, Map.class);
    }

    public static <T extends Object> T jsonToObject(byte[] json, Class<T> clazz) {
        T result = null;
        try {
            result = mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static byte[] objectToJson(Object object) {
        byte[] json = null;
        try {
            json = mapper.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static byte[] objectToJsonIgnoreNulls(Object object) {
        byte[] json = null;
        try {
            ByteArrayBuilder bb = new ByteArrayBuilder(mapper.getJsonFactory()._getBufferRecycler());
            JsonGenerator jsonGenerator = mapper.getJsonFactory().createJsonGenerator(bb, JsonEncoding.UTF8);

            SerializationConfig serializationConfig = mapper.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.writeValue(jsonGenerator, object);
//            json = mapper.writeValueAsBytes(object);
            json = bb.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static <T extends Object> Map<String, Object> objectToMap(T object) {
        byte[] json = objectToJson(object);
        Map<String, Object> map = jsonToMap(json);

        return map;
    }

    public static <T extends Object> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        byte[] json = objectToJson(map);
        T result = jsonToObject(json, clazz);

        return result;
    }

    public static <T> T readValue(JsonParser jp, Class<T> clazz) throws IOException {
        return mapper.readValue(jp, clazz);
    }
}
