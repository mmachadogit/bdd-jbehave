package client;

import exception.ApiException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import utils.BddContext;
import utils.JSONUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class ApiClient {

  private String environment;
  List<String> contentTypes = Arrays.asList("application/json");
  private List<String> accepts = Arrays.asList("application/json");

  private BddContext bddContext;

  public ApiClient() {
    bddContext = BddContext.createOrGetInstance();
    this.environment = bddContext.getEnvironment();
  }

  public <T> T invoke(Class<T> returnType, Map<String, String> headerParams, String method, String path, Map<String, String> queryParams, Object body) {
    WebTarget target = buildWebTarget(path, queryParams);
    Response response = buildResponse(target, method, body, headerParams);

    if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
      return returnType.cast(response);
    }

    if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
      return returnType == null ? null : JSONUtil.deserialize(response, returnType);
    }

    String message = "error";
    if (response.hasEntity()) {
      try {
        message = String.valueOf(response.readEntity(String.class));
      } catch (RuntimeException var21) {
        ;
      }
    }
    throw new ApiException(response.getStatus(), message);
  }

  /**
   * build webtarget, it mean the url which gonna be called join the base path to the uri and adding the query string
   */
  private WebTarget buildWebTarget(String path, Map<String, String> queryParams) {
    StringBuilder basePath = new StringBuilder(this.environment);

    ClientConfig clientConfig = new ClientConfig();
    Client client = ClientBuilder.newClient(clientConfig);
    WebTarget target = client.target(basePath.toString()).path(path);

    if (queryParams == null) {
      return target;
    }

    for (Map.Entry<String, String> queryParam: queryParams.entrySet()) {
      if (queryParam.getValue() == null) {
        continue;
      }
      target = target.queryParam(queryParam.getKey(), new Object[] {queryParam.getValue()});
    }

    return target;
  }

  public Invocation.Builder build(WebTarget target, Map<String, String> headerParams) {
    Invocation.Builder builder =
        target.request().accept(accepts.toArray(new String[accepts.size()]));

    if (headerParams == null) {
      return builder;
    }
    for (Map.Entry<String, String> param: headerParams.entrySet()) {
      if(param.getValue() != null) {
        builder = builder.header(param.getKey(), param.getValue());
      }
    }
    return builder;
  }

  private Response buildResponse(WebTarget target, String method, Object body,
      Map<String, String> headerParams) {
    Entity<?> entity;
    Invocation.Builder builder = build(target, headerParams);

    switch (method) {
      case "GET":
        return builder.get();
      case "DELETE":
        return builder.delete();
      case "POST":
        entity = buildEntity(body);
        return builder.post(entity);
      case "PUT":
        entity = buildEntity(body);
        return builder.put(entity);
      case "PATCH":
        entity = buildEntity(body);
        return target.request().headers(getHeaderForPATCH(headerParams)).build("PATCH", entity).property(
            HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();
      default:
        throw new ApiException(500, "unknown method type " + method);
    }
  }

  public MultivaluedMap<String, Object> getHeaderForPATCH(Map<String, String> headerParams) {
    if (headerParams == null) {
      return null;
    }
    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    for (Map.Entry<String, String> param: headerParams.entrySet()) {
      if(param.getValue() != null) {
        headers.add(param.getKey(), param.getValue());
      }
    }
    return headers;
  }

  private Entity<?> buildEntity(Object body) {
    if (body == null) {
      return null;
    }
    if (contentTypes.contains("application/json")) {
      return Entity.json(JSONUtil.serialize(body));
    }
    throw new ApiException(400, "can not serialize object into Content-Type: " + contentTypes);
  }

}
