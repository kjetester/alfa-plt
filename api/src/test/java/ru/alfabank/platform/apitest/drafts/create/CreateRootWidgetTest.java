package ru.alfabank.platform.apitest.drafts.create;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static ru.alfabank.platform.businessobjects.CityGroup.getCityGroup;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.helpers.TestDataHelper.DRAFT_CONTROLLER_URL;
import static ru.alfabank.platform.helpers.TestDataHelper.createdEntities;
import static ru.alfabank.platform.helpers.TestDataHelper.getNewUuid;
import static ru.alfabank.platform.helpers.TestDataHelper.getRequestSpecification;
import static ru.alfabank.platform.helpers.TestDataHelper.getTestPage;
import static ru.alfabank.platform.helpers.TestDataHelper.putNewRootWidgetOnTheTop;
import static ru.alfabank.platform.helpers.TestDataHelper.setTestWidget;

import java.util.Arrays;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.apitest.drafts.BaseTest;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;

public class CreateRootWidgetTest extends BaseTest {

  private Widget newWidget;

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика создания нового корневого Widget'а")
  public void makeDraft() {
    newEntityUid = getNewUuid();
    newWidget = new Widget(
        newEntityUid,
        "Widget_" + newEntityUid,
        1,
        dateFrom,
        dateTo,
        desktop,
        true,
        "RU",
        Arrays.asList(getCityGroup()),
        null,
        null,
        false);
    DataDraft newWidgetData = new DataDraft.DataDraftBuilder()
        .name(newWidget.getName())
        .dateFrom(newWidget.getDateFrom().toString())
        .dateTo(newWidget.getDateTo().toString())
        .device(newWidget.getDevice())
        .enable(newWidget.isEnabled())
        .localization(newWidget.getLocalization())
        .cityGroups(newWidget.getWidgetGeo().toArray())
        .build();
    DataDraft placementData = new DataDraft.DataDraftBuilder()
        .childUids(putNewRootWidgetOnTheTop(newWidget.getUid())).build();
    operations.addAll(Arrays.asList(
        new WrapperDraft.OperationDraft(
            newWidgetData, Entity.widget, Method.create, newWidget.getUid()),
        new WrapperDraft.OperationDraft(
            placementData, Entity.page, Method.changeLinks, getTestPage().getId())));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика создания нового корневого Widget'а")
  public void saveTest() {
    given().spec(getRequestSpecification()).body(body).pathParam("pageId", getTestPage().getId())
        .when().put(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка получения сохраненного черновика создания нового корневого Widget'а",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    // TODO JSONAssert.assertEquals(
    //  response.getBody().asString(), objMapper.writeValueAsString(body), true);
  }

  @Test(
      description = "Проверка публикации черновика создания нового корневого Widget'а",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().post(DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка отсутствия черновика создания нового корневого Widget'а"
          + " после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(getRequestSpecification()).pathParam("pageId", getTestPage().getId())
        .when().get(DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }

  /**
   * Put the created Widgets's UID to the Created Entities list.
   * @param context test context
   */
  @AfterClass(
      description = "Добваление созданного Widget'а в список созданных сущностей")
  public void setCreatedValue(final ITestContext context) {
    if (context.getFailedTests().size() == 0) {
      createdEntities.put(Entity.widget, newWidget.getUid());
      setTestWidget(newWidget);
    }
  }
}
