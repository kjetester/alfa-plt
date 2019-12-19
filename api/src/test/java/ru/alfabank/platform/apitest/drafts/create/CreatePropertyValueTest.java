package ru.alfabank.platform.apitest.drafts.create;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.helpers.TestDataHelper.DRAFT_CONTROLLER_URL;
import static ru.alfabank.platform.helpers.TestDataHelper.createdEntities;
import static ru.alfabank.platform.helpers.TestDataHelper.getNewUuid;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestProperty;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.apitest.BaseTest;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;


public class CreatePropertyValueTest extends BaseTest {

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика создания нового Value")
  public void makeDraft() {
    newEntityUid = getNewUuid();
    DataDraft newValueData = new DataDraft.DataDraftBuilder()
        .forProperty(getTestProperty().getUid())
        .value("")
        .cityGroups(getCityGroup("RU"))
        .enable(true)
        .build();
    operations.add(
        new WrapperDraft.OperationDraft(
            newValueData, Entity.propertyValue, Method.create, newEntityUid));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика создания нового Value")
  public void saveTest() {
    given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
        .when().put(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка получения сохраненного черновика создания нового Value",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    // TODO JSONAssert.assertEquals(
    //  response.getBody().asString(), objMapper.writeValueAsString(body), true);
  }

  @Test(
      description = "Проверка публикации черновика создания нового Value",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().post(DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    createdEntities.put(Entity.propertyValue, newEntityUid);
  }

  @Test(
      description = "Проверка отсутствия черновика создания нового Value после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }

  /**
   * Put the created Values's UID to the Created Entities list.
   * @param context test context
   */
  @AfterClass(
      description = "Добваление созданного Value в список созданных сущностей")
  public void setCreatedValue(final ITestContext context) {
    if (context.getFailedTests().size() == 0) {
      createdEntities.put(Entity.propertyValue, newEntityUid);
    }
  }
}
