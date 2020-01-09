package ru.alfabank.platform.apitest.drafts.modify;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.helpers.TestDataHelper.DRAFT_CONTROLLER_URL;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestProperty;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPropertyValue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.apitest.drafts.BaseTest;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;

public class ModifyPropertyValueTest extends BaseTest {

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика изменения Value")
  public void makeDraft() {
    DataDraft valueData = new DataDraft.DataDraftBuilder()
        .forProperty(getTestProperty().getUid())
        .value("value")
        .cityGroups(getCityGroup("geo-5"))
        .build();
    operations.add(
        new WrapperDraft.OperationDraft(
            valueData, Entity.propertyValue, Method.change, getTestPropertyValue().getUid()));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика изменения Value")
  public void saveTest() {
    given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
        .when().put(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(description = "Проверка получения черновика изменения Value",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    //TODO: need to replace with rest-assured methods
    // JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);
  }

  @Test(description = "Проверка публикации черновика изменения Value",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().post(DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(description = "Проверка отсутствия черновика изменения Value после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }
}
