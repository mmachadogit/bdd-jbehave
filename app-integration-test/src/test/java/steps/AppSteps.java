package steps;

import client.AppsAPI;
import model.AppModel;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

public class AppSteps extends Steps{


  @Given("Any test")
  public void test() {
    Assert.assertTrue(true);
  }

  @Then("A new application $appId can be created")
  public void createApp(String appId){

    AppModel appModel = new AppModel();
    appModel.setAppId(appId);
    appModel.setDescription("test");
    appModel.setName("test");

    AppModel response = new AppsAPI().createApp(appModel);
    Assert.assertNotNull("Response cannot be null", response);
    Assert.assertEquals("Match appid", appId, response.getAppId());

  }

}
