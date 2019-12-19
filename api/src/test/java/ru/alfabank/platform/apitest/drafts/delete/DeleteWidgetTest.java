package ru.alfabank.platform.apitest.drafts.delete;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.helpers.TestDataHelper.DRAFT_CONTROLLER_URL;
import static ru.alfabank.platform.helpers.TestDataHelper.createdEntities;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.apitest.BaseTest;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;

public class DeleteWidgetTest extends BaseTest {

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика удаления Widget'а")
  public void makeDraft() {
    operations.add(
        new WrapperDraft.OperationDraft(
            null, Entity.widget, Method.delete, createdEntities.get(Entity.widget)));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика удаления Widget'а")
  public void saveTest() {
    given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
        .when().put(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка получения черновика удаления Widget'а",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    //TODO: need to replace with rest-assured methods
    // JSONAssert.assertEquals(getDraftResponse.getBody().asString(), body, true);
  }

  @Test(
      description = "Проверка публикации черновика удаления Widget'а",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().post(DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка отсутствия черновика удаления Widget'а после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }

  /**
   * Remove the deleted Widgets's UID to the Created Entities list.
   * @param context test context
   */
  @AfterClass(
      description = "Изъятие удаленного Widget'а из списка созданных сущностей")
  public void setCreatedValue(ITestContext context) {
    if (context.getFailedTests().size() == 0) {
      createdEntities.remove(Entity.widget);
    }
  }
}
