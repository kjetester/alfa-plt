package ru.alfabank.platform.tests.acms.widget;

import static io.restassured.RestAssured.given;

import com.epam.reportportal.message.ReportPortalMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.ITestContext;
import ru.alfabank.platform.buisenessobjects.Page;
import ru.alfabank.platform.tests.acms.page.BasePageTest;

/**
 * Class for common methods for the Tests about Widgets.
 */
public class BaseWidgetTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseWidgetTest.class);
  protected static final String sourcePage = "/about/";

  /**
   * Comparing a given page against a source page.
   * @param page page
   * @throws JSONException exception
   */
  protected void compareCreatedAndSourcePages(
      Page page, ITestContext context) throws JSONException {
    LOGGER.info(
        String.format("Сравнение страниц - исходной: '%s' и новой: '%s'",
            sourcePage, page.getUri()));
    context.setAttribute("case", context.getName());
    String expected = getExistedPageFromContentPageController();
    context.setAttribute("expected", expected);
    String actual = getCreatedPageFromContentPageController(page);
    context.setAttribute("actual", actual);
    LOGGER.debug(new ReportPortalMessage(String.format("Полученный JSON: '%s'", actual)));
    CustomComparator comparator;
    if (context.getName().contains("copy")) {
      comparator = new CustomComparator(
          JSONCompareMode.STRICT_ORDER,
          new Customization("title", (o1, o2) -> true),
          new Customization("description", (o1, o2) -> true),
          new Customization("**.uid", (o1, o2) -> true)
      );
    } else {
      comparator = new CustomComparator(
          JSONCompareMode.STRICT_ORDER,
          new Customization("title", (o1, o2) -> true),
          new Customization("description", (o1, o2) -> true)
      );
    }
    JSONAssert.assertEquals(expected, actual, comparator);
    LOGGER.info("Успех");
  }

  /**
   * Get expected result.
   * @return result as string
   */
  private String getExistedPageFromContentPageController() {
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", sourcePage));
    return given().spec(contentPageControllerSpec)
        .queryParam("uri", sourcePage)
        .when().get().then().extract().response().getBody().asString();
  }

  /**
   * Get actual result.
   * @param page page
   * @return result as string
   */
  private String getCreatedPageFromContentPageController(Page page) {
    LOGGER.info(String.format("Запрос страницы '%s' в '/contentPageController'", page.getUri()));
    return given().spec(contentPageControllerSpec)
        .queryParam("uri", page.getUri())
        .when().get().then().extract().response().getBody().asString();
  }
}
