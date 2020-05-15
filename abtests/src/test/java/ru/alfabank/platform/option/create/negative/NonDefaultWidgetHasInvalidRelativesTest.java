package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.enums.User;
import ru.alfabank.platform.option.OptionBaseTest;

public class NonDefaultWidgetHasInvalidRelativesTest extends OptionBaseTest {

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
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget0 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = createWidget(
        page, widget0, desktop, true, DEFAULT, true, null, null);
    final var widget1_1 = createWidget(
        page, widget1, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    final var result = createOptionAssumingFail(
        false, List.of(widget1_1.getUid()), actualExperiment.getUuid(), .05D);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget1_1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
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
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget0 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1 = createWidget(
        page, widget0, desktop, false, FOR_AB_TEST, false, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    final var result = createOptionAssumingFail(
        false, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Виджет '" + widget1.getUid()
            + "' не должен иметь верхнеуровневых родителей с флагом 'forABtest'");
  }

  @Test (description = "Тест создания недефлотного варианта с привязкой к виджету:"
      + "\n\t* enable=false"
      + "\n\t* experimentOptionName=forABtest"
      + "\n\t* defaultWidget=false"
      + "\n\t* с негативным условием:"
      + "\n\t\t1. Ребенок:"
      + "\n\t\t\t* enable=true/false"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true")
  public void nonDefaultWidgetHasInvalidChildrenTest() {
    setUser(User.CONTENT_MANAGER);
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var widget1_1 = createWidget(
        page, widget1, desktop, false, DEFAULT, true, null, null);
    final var widget1_2 = createWidget(
        page, widget1, desktop, true, DEFAULT, true, null, null);
    final var device = widget1.getDevice();
    final var actualExperiment = createExperiment(
        device, pageId, getRandomProductType(), experimentEnd, .5D);
    final var result = createOptionAssumingFail(
        false, List.of(widget1.getUid()), actualExperiment.getUuid(), .5D);
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для варианта '", widget1_1.getUid(), widget1_2.getUid(),
            "' должны быть помечены как 'forABtest', быть выключенными и не должны быть "
                + "виджетами по умолчанию");
  }
}