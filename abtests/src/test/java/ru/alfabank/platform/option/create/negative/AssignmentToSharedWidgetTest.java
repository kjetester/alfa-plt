package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.enums.User;
import ru.alfabank.platform.option.OptionBaseTest;

public class AssignmentToSharedWidgetTest extends OptionBaseTest {

  @Test (description = "Тест создания (не)дефолтного варианта с привязкой к шаренному виджету",
      dataProvider = "dataProvider")
  public void assignmentToSharedWidgetTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    setUser(User.CONTENT_MANAGER);
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    page = createdPages.get(pageId);
    Widget widget;
    if (isDefaultOption) {
      widget = createWidget(page, null, desktop, true, DEFAULT, true, null, null);
    } else {
      widget = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null);
    }
    final var page2 = createPage(null, null, true);
    shareWidgetToAnotherPage(widget, page2);
    final var experiment = createExperiment(
            widget.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D);
    Response response;
    if (isDefaultOption) {
      response = createOptionAssumingFail(
              true, List.of(widget.getUid()), experiment.getUuid(), .5D);
    } else {
      response = createOptionAssumingFail(
              false, List.of(widget.getUid()), experiment.getUuid(), .5D);
    }
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("В эксперименте не могут участвовать виджеты, общие для нескольких страниц."
            + " Отвяжите следующие виджеты:", widget.getUid());
  }

  @Test (description = "Тест создания (не)дефолтного варианта с привязкой к виджету,"
      + " имеющего шаренного предка", dataProvider = "dataProvider")
  public void assignmentToWidgetWhichHasSharedAncestorTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    page = createdPages.get(pageId);
    final var sharedAncestor = createWidget(
            page, null, desktop, true, DEFAULT, true, null, null);
    final var ancestor = createWidget(
            page, sharedAncestor, desktop, true, DEFAULT, true, null, null);
    Widget widget;
    if (isDefaultOption) {
      widget = createWidget(
              page, ancestor, desktop, true, DEFAULT, true, null, null);
    } else {
      widget = createWidget(
              page, ancestor, desktop, false, FOR_AB_TEST, false, null, null);
    }
    final var page2 = createPage(null, null, true);
    shareWidgetToAnotherPage(sharedAncestor, page2);
    final var experiment = createExperiment(
            widget.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D);
    Response response;
    if (isDefaultOption) {
      response = createOptionAssumingFail(
              true, List.of(widget.getUid()), experiment.getUuid(), .5D);
    } else {
      response = createOptionAssumingFail(
              false, List.of(widget.getUid()), experiment.getUuid(), .5D);
    }
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("В эксперименте не могут участвовать виджеты, общие для нескольких страниц."
            + " Отвяжите следующие виджеты:");
  }

  @Test (description = "Тест создания (не)дефолтного варианта с привязкой к виджету,"
      + " имеющего шаренного потомка", dataProvider = "dataProvider")
  public void assignmentToWidgetWhichHasSharedDescendantTest(
      @ParameterKey("Дефолтный вариант") final Boolean isDefaultOption) {
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    page = createdPages.get(pageId);
    Widget widget;
    Widget sharedDescendant;
    if (isDefaultOption) {
      widget = createWidget(
              page, null, desktop, true, DEFAULT, true, null, null);
      final var descendant = createWidget(
              page, widget, desktop, true, DEFAULT, true, null, null);
      sharedDescendant = createWidget(
              page, descendant, desktop, true, DEFAULT, true, null, null);
    } else {
      widget = createWidget(
              page, null, desktop, false, FOR_AB_TEST, false, null, null);
      final var descendant = createWidget(
              page, widget, desktop, false, FOR_AB_TEST, false, null, null);
      sharedDescendant = createWidget(
              page, descendant, desktop, false, FOR_AB_TEST, false, null, null);
    }
    final var page2 = createPage(null, null, true);
    shareWidgetToAnotherPage(sharedDescendant, page2);
    final var experiment = createExperiment(
            widget.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D);
    Response response;
    if (isDefaultOption) {
      response = createOptionAssumingFail(
              true, List.of(widget.getUid()), experiment.getUuid(), .5D);
    } else {
      response = createOptionAssumingFail(
              false, List.of(widget.getUid()), experiment.getUuid(), .5D);
    }
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("В эксперименте не могут участвовать виджеты, общие для нескольких страниц."
            + " Отвяжите следующие виджеты:", sharedDescendant.getUid());
  }
}
