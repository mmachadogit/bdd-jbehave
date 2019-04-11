package utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import exception.ApiException;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.Response;

public class JSONUtil {

  public static ObjectMapper getMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    mapper.registerModule(new JodaModule());

    return mapper;
  }

  public static String serialize(Object obj) {
    try {
      return obj != null ? getMapper().writeValueAsString(obj) : null;
    } catch (Exception var3) {
      throw new ApiException(400, var3.getMessage());
    }
  }

  public static <T> T deserialize(Response response, Class<T> returnType)  {
    String contentType = null;
    List<Object> contentTypes = (List)response.getHeaders().get("Content-Type");
    if (contentTypes != null && !contentTypes.isEmpty()) {
      contentType = String.valueOf(contentTypes.get(0));
    }

    if (contentType == null) {
      throw new ApiException(500, "missing Content-Type in response");
    }

    String body;
    if (response.hasEntity()) {
      body = (String)response.readEntity(String.class);
    } else {
      body = "";
    }

    if (contentType.startsWith("application/json")) {
      return deserialize(body, returnType);
    }

    if (returnType.equals(String.class)) {
      return (T) body;
    }

    throw new ApiException(500, "Content type \"" + contentType + "\" is not supported for type: " + returnType);

  }

  public static <T> T deserialize(String value, Class<T> returnType) {
    try {
      return getMapper().readValue(value, returnType);
    } catch (IOException exception) {
      if (returnType.equals(String.class)) {
        return (T) value;
      }
      throw new ApiException(500, exception.getMessage());

    }
  }

}
