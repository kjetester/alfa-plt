package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.enums.User;

public class WidgetRelativesViolationTest extends BaseTest {

  @Test (description = "Тест создания дефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=true"
      + "\n\t* experimentOptionName=default"
      + "\n\t* defaultWidget=true"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Прямой потомок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true"
      + "\n\t\t2. Прямой потомок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void defaultWidgetHasInvalidRelativesTest() {
    setUser(User.CONTENT_MANAGER);
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    final var widget1_1 = createWidget(
        page, widget1, desktop, true, DEFAULT, true, null, null);
    final var widget1_2 = createWidget(
        page, widget1, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(page.getId());
    final var widget2 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), .5D);
    changeWidgetABtestProps(widget1_1, pageId, false, DEFAULT, true);
    changeWidgetABtestProps(widget1_2, pageId, false, FOR_AB_TEST, false);
    // TEST //
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта по умолчанию ", widget1_1.getUid(), widget1_2.getUid(),
            "' должны быть с дефолтным названием варианта, быть включенными и быть виджетами по "
                + "умолчанию");
    getExperiment(actualExperiment).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setCreatedBy(getUser().getLogin())
        .setStatus(DISABLED)
        .setEnabled(false)
        .setCreationDate(start)
        .build());
  }

  @Test (description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Предок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidAncestorTest() {
    setUser(User.CONTENT_MANAGER);
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(page.getId());
    final var widget2 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    final var widget2_1 = createWidget(
        page, widget2, desktop, true, DEFAULT, true, null, null);
    final var widget2_1_1 = createWidget(
        page, widget2_1, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    createOption(false, List.of(widget2_1_1.getUid()), actualExperiment.getUuid(), .5D);
    changeWidgetABtestProps(widget2, pageId, false, FOR_AB_TEST, false);
    // TEST //
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget2_1_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
    getExperiment(actualExperiment).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setCreatedBy(getUser().getLogin())
        .setStatus(DISABLED)
        .setEnabled(false)
        .setCreationDate(start)
        .build());
  }

  @Test (description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Прямой предок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidFatherTest() {
    setUser(User.CONTENT_MANAGER);
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(page.getId());
    final var widget2 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    final var widget2_1 = createWidget(
        page, widget2, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    createOption(false, List.of(widget2_1.getUid()), actualExperiment.getUuid(), .5D);
    changeWidgetABtestProps(widget2, pageId, false, FOR_AB_TEST, false);
    // TEST //
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget2_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
    getExperiment(actualExperiment).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setCreatedBy(getUser().getLogin())
        .setStatus(DISABLED)
        .setEnabled(false)
        .setCreationDate(start)
        .build());
  }

  @Test (description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Ребенок:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false")
  public void nonDefaultWidgetHasInvalidChildrenTest() {
    setUser(User.CONTENT_MANAGER);
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    page = createdPages.get(page.getId());
    final var widget2 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget2_1 = createWidget(
        page, widget2, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget2_2 = createWidget(
        page, widget2, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), .5D);
    changeWidgetABtestProps(widget2_1, pageId, true, DEFAULT, true);
    changeWidgetABtestProps(widget2_2, pageId, false, DEFAULT, true);
    // TEST //
    final var result = runExperimentAssumingFail(actualExperiment);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта '", widget2_1.getUid(), widget2_2.getUid(),
            "' должны быть помечены как 'forABtest',"
                + " быть выключенными и не должны быть виджетами по умолчанию");
    getExperiment(actualExperiment).equals(new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setCreatedBy(getUser().getLogin())
        .setStatus(DISABLED)
        .setEnabled(false)
        .setCreationDate(start)
        .build());
  }
}
