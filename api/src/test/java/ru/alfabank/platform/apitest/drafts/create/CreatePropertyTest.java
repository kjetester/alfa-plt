package ru.alfabank.platform.apitest.drafts.create;

import org.testng.*;
import org.testng.annotations.*;
import ru.alfabank.platform.apitest.drafts.*;
import ru.alfabank.platform.businessobjects.*;
import ru.alfabank.platform.businessobjects.draft.*;
import ru.alfabank.platform.helpers.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static ru.alfabank.platform.businessobjects.CityGroup.*;
import static ru.alfabank.platform.businessobjects.Device.*;
import static ru.alfabank.platform.helpers.TestDataHelper.*;

public class CreatePropertyTest extends BaseTest {

  private Property newProperty;

  /**
   * Draft generation.
   */
  @BeforeClass(
      description = "Генерация черновика создания нового Property")
  public void makeDraft() {
    newEntityUid = getNewUuid();
    Value newValue = new Value(
        newEntityUid,
        "Value" + newEntityUid,
        getCityGroup("RU"));
    newEntityUid = getNewUuid();
    newProperty = new Property(
        newEntityUid,
        "Property_" + newEntityUid,
        desktop,
        Collections.singletonList(newValue));
    DataDraft newPropertyData = new DataDraft.DataDraftBuilder()
        .forWidget(getTestWidget().getUid())
        .name(newProperty.getName())
        .device(newProperty.getDevice())
        .build();
    DataDraft newValueData = new DataDraft.DataDraftBuilder()
        .forProperty(newProperty.getUid())
        .value(newValue.getValue())
        .cityGroups(newValue.getGeo())
        .build();
    operations.addAll(Arrays.asList(
        new WrapperDraft.OperationDraft(
            newPropertyData, Entity.property, Method.create, newProperty.getUid()),
        new WrapperDraft.OperationDraft(
            newValueData, Entity.propertyValue, Method.create, newValue.getUid())));
    body = new WrapperDraft(operations);
  }

  @Test(
      description = "Проверка сохранения черновика создания нового Property")
  public void saveTest() {
    given().spec(TestDataHelper.getRequestSpecification())
        .body(body).pathParam("pageId", TestDataHelper.getTestPage().getId())
        .when().put(TestDataHelper.DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка получения сохраненного черновика создания нового Property",
      dependsOnMethods = "saveTest")
  public void getTest() {
    given().spec(TestDataHelper.getRequestSpecification())
        .pathParam("pageId", TestDataHelper.getTestPage().getId())
        .when().get(TestDataHelper.DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
    // TODO JSONAssert.assertEquals(
    //  response.getBody().asString(), objMapper.writeValueAsString(body), true);
  }

  @Test(description = "Проверка публикации черновика создания нового Property",
      dependsOnMethods = {"saveTest", "getTest"})
  public void publishTest() {
    given().spec(TestDataHelper.getRequestSpecification())
        .pathParam("pageId", TestDataHelper.getTestPage().getId())
        .when().post(TestDataHelper.DRAFT_CONTROLLER_URL + "/execute")
        .then().log().ifStatusCodeMatches(not(200)).statusCode(200);
  }

  @Test(
      description = "Проверка отсутствия черновика создания нового Property после его публикации",
      dependsOnMethods = "publishTest")
  public void absentPublishedDraftTest() {
    given().spec(TestDataHelper.getRequestSpecification())
        .pathParam("pageId", TestDataHelper.getTestPage().getId())
        .when().get(TestDataHelper.DRAFT_CONTROLLER_URL)
        .then().log().ifStatusCodeMatches(not(404)).statusCode(404);
  }

  /**
   * Put the created Property's UID to the Created Entities list.
   * @param context test context
   */
  @AfterClass(
      description = "Добваление созданного Property в список созданных сущностей")
  public void setCreatedValue(final ITestContext context) {
    if (context.getFailedTests().size() == 0) {
      TestDataHelper.createdEntities.put(Entity.property, newProperty.getUid());
      TestDataHelper.setTestProperty(newProperty);
    }
  }
}
