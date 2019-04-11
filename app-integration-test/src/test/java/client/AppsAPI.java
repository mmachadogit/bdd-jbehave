package client;

import model.AppModel;

import java.util.Map;

public class AppsAPI {

  private static final String APPS_PATH = "/apps";

  private ApiClient apiClient;

  public AppsAPI() {
    apiClient = new ApiClient();
  }


  public AppModel createApp(AppModel app) {
    String path = APPS_PATH;
    return apiClient.invoke(AppModel.class, null, "POST", path, null, app);
  }
}
