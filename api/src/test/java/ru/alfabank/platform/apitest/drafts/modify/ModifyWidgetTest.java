package ru.alfabank.platform.apitest.drafts.modify;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.helpers.TestDataHelper.DRAFT_CONTROLLER_URL;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestWidget;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.apitest.BaseTest;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;

public class ModifyWidgetTest extends BaseTest {

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика изменения Widget'а")
  public void makeDraft() {
    DataDraft widgetData = new DataDraft.DataDraftBuilder()
        .dateFrom("2019-01-01T00:00:00.000Z")
        .dateTo("2020-01-01T00:00:00")
        .device(getTestWidget().getDevice())
        .enable(getTestWidget().isEnabled())
        .localization(getTestWidget().getLocalization())
        .state("")
        .name(getTestWidget().getName())
        .cityGroups(getCityGroup("123"))
        .build();
    operations.add(
        new WrapperDraft.OperationDraft(
            widgetData, Entity.widget, Method.change, getTestWidget().getUid()));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика изменения Widget'а")
  public void saveTest() {
    given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
        .when().put(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(description = "Проверка получения черновика изменения Widget'а",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    //TODO: need to replace with rest-assured methods
  }

  @Test(description = "Проверка публикации черновика изменения Widget'а",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().post(DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(description = "Проверка отсутствия черновика изменения Widget'а после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }
}
