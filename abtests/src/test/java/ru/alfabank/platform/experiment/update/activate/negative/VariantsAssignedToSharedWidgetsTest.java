package ru.alfabank.platform.experiment.update.activate.negative;

import static java.util.Collections.emptyList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class VariantsAssignedToSharedWidgetsTest extends BaseTest {

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\tВарианты привязаны к шаренным виджетам")
  public void bothVariantsAssignedToSharedWidgetTest() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page1 = createPage(null, null, true, getContentManager());
    final var page_1_id = page1.getId();
    createWidget(
        createdPages.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    createWidget(
        createdPages.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget1 = page1.getWidgetList().get(0);
    final var widget2 = page1.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, page_1_id, getRandomProductType(), end, trafficRate, getContentManager());
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    var page2 = createPage(null, null, true, getContentManager());
    final var page_2_id = page2.getId();
    shareWidgetToAnotherPage(widget1, createdPages.get(page_2_id), getContentManager());
    shareWidgetToAnotherPage(widget2, createdPages.get(page_2_id), getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains(widget2.getUid(), widget1.getUid());
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\tДефолтный вариант привязан к шаренному виджету")
  public void defaultVariantAssignedToSharedWidgetTest() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page1 = createPage(null, null, true, getContentManager());
    final var page_1_id = page1.getId();
    createWidget(
        createdPages.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    createWidget(
        createdPages.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget1 = page1.getWidgetList().get(0);
    final var widget2 = page1.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, page_1_id, getRandomProductType(), end, trafficRate, getContentManager());
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, emptyList(), actualExperiment.getUuid(), trafficRate, getContentManager());
    var page2 = createPage(start, end, true, getContentManager());
    final var page_2_id = page2.getId();
    shareWidgetToAnotherPage(widget1, createdPages.get(page_2_id), getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains("Отвяжите следующие виджеты: [" + widget1.getUid() + "]");
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }

  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\tНедефолтный вариант привязан к шаренному виджету")
  public void nonDefaultVariantAssignedToSharedWidgetTest() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page1 = createPage(null, null, true, getContentManager());
    final var page_1_id = page1.getId();
    createWidget(
        createdPages.get(page_1_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    createWidget(
        createdPages.get(page_1_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget1 = page1.getWidgetList().get(0);
    final var widget2 = page1.getWidgetList().get(1);
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, page_1_id, getRandomProductType(), end, trafficRate, getContentManager());
    createOption(true, emptyList(), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    var page2 = createPage(start, end, true, getContentManager());
    final var page_2_id = page2.getId();
    shareWidgetToAnotherPage(widget2, createdPages.get(page_2_id), getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains(
            "В эксперименте не могут участвовать виджеты, общие для нескольких страниц.")
        .contains("Отвяжите следующие виджеты: [" + widget2.getUid() + "]");
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }

  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего шаренного предка
  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего прямого шаренного предка
  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего шаренного потомка
  //TODO:
  // Тест создания (не)дефолтного варианта с привязкой к виджету, имеющего прямого шаренного потомка
}
