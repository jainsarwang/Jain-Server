package com.Server.utils;

//import com.Server.configuration.Configuration;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JSON {
   /* private static String string;

    public JSON(String str) {
        string = str;
    }

    public Object parse() {
        return JSONValue.parse(string);
    }

    public static <T> T loadToClass(Object o, Class c) {
        T obj;

        // creating object of a class
        try {
            obj = (T) c.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // formatting to json object
        JSONObject json = (JSONObject) o;

        // iterating through all the fields of a class
        for(Field f : c.getDeclaredFields()) {
            String key = f.getName();
            Object value = json.get(key);
            Class<?> type = f.getType();

            // makng private method accessible
            f.setAccessible(true);

            try {
                if(Long.class.equals(type) || Long.TYPE.equals(type)) f.set(obj, (Long) value);
                else if(String.class.equals(type)) f.set(obj, (String) value);
                else f.set(obj, value);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return obj;
    }*/

    private static ObjectMapper myObjectMapper = defaultObjectMapper();
    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper obj = new ObjectMapper();
        obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // prevent from crashing during parsing when on property is missing
        return obj;
    }

    public static JsonNode parse(String jsonSrc) throws IOException {
        return myObjectMapper.readTree(jsonSrc);
    }

    public static <A> A fromJson(JsonNode node , Class<A> clazz) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node, clazz);
    }

    public static JsonNode toJSON(Object obj ) {
        return myObjectMapper.valueToTree(obj);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJSON(node, false);
    }

    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJSON(node, true);
    }

    private static String generateJSON(Object o, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();

        if(pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(o);
    }

    public void deepCopy()
    {
        System.out.println(this);
//        ObjectNode ret = new ObjectNode();
//        for (Map.Entry<String, JsonNode> entry: _children.entrySet())
//            ret._children.put(entry.getKey(), entry.getValue().deepCopy());
//        return ret;
    }
}
