package steps;

import client.ApiClient;
import client.AppsAPI;
import exception.ApiException;
import model.AppModel;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

import javax.ws.rs.core.Response;

public class AppSteps extends Steps{

  @Given("Any test")
  public void test() {
    Assert.assertTrue(true);
  }

  @Then("A new application $appId can be created")
  public void createApp(String appId){

    AppModel appModel = getAppModel(appId);

    AppModel response = new AppsAPI().createApp(appModel); //Define POST verb parameters
    Assert.assertNotNull("Response cannot be null", response);
    Assert.assertEquals("Match appid", appId, response.getAppId());

  }

  private AppModel getAppModel(String appId) {
    AppModel appModel = new AppModel();
    appModel.setAppId(appId);
    appModel.setDescription("test");
    appModel.setName("test");
    return appModel;
  }

  @Then("user can't create $appId with null description")
  public void cannotCreateAppWithNullDescription(String appId) {
    AppModel appModel = getAppModel(appId);
    appModel.setDescription(null);
    AppModel response = null;
    try {
      response = new AppsAPI().createApp(appModel);
    } catch (ApiException exp) {
      Assert.assertEquals(400, exp.getErrorCode());
      Assert.assertEquals("VALIDATION_ERROR_CREATE_APP", exp.getError().getCode());
      Assert.assertEquals("description is a required attribute", exp.getError().getMessage());
    }
    Assert.assertNull("APP shouldn't be created.",null);
  }

}
