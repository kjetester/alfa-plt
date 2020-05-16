package ru.alfabank.platform.option.create.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionCreateTest extends OptionBaseTest {

  @Test (description = "Позитивный тест создания вариантов", dataProvider = "dataProvider")
  public void optionCreateTest(final Boolean isDefaultNotAssignedWidget) {
    final var experimentEnd = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    Widget widget;
    if (isDefaultNotAssignedWidget) {
      widget = createWidget(page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
      page = createdPages.get(pageId);
    } else {
      createWidget(
              page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
      final var widget1_1 = createWidget(
              page, null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
      final var widget1_1_1 = createWidget(
              page, widget1_1, desktop, true, DEFAULT, true, null, null, getContentManager());
      widget = createWidget(
              page, widget1_1_1, desktop, true, DEFAULT, true, null, null, getContentManager());
      createWidget(
              page, widget, desktop, true, DEFAULT, true, null, null, getContentManager());
    }
    final var widget2 = createWidget(
            page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    final var widget2_1 = createWidget(
            page, null, desktop, true, DEFAULT, true, null, null, getContentManager());
    final var widget2_1_1 = createWidget(
            page, widget2, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var widget2_1_1_1 = createWidget(
            page, widget2_1, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = widget2.getDevice();
    final var experiment = createExperiment(
            device, pageId, getRandomProductType(), experimentEnd, .5D, getContentManager());
    Option defaultOption;
    Option nonDefaultOption2;
    final var nonDefaultOption1 = createOption(
            false, List.of(widget2_1_1.getUid()), experiment.getUuid(), .33D, getContentManager());
    if (isDefaultNotAssignedWidget) {
      defaultOption = createOption(true, null, experiment.getUuid(), .33D, getContentManager());
      nonDefaultOption2 = createOption(false, List.of(widget.getUid()), experiment.getUuid(), .34D, getContentManager());
    } else {
      defaultOption = createOption(true, List.of(widget.getUid()), experiment.getUuid(), .33D, getContentManager());
      nonDefaultOption2 = createOption(false, List.of(), experiment.getUuid(), .34D, getContentManager());
    }
    final var softly = new SoftAssertions();
    softly.assertThat(getOption(defaultOption, getContentManager()))
        .as("Проверка дефолтного варианта")
        .isEqualTo(defaultOption);
    softly.assertThat(getOption(nonDefaultOption1, getContentManager()))
        .as("Проверка перого недефолтного варианта")
        .isEqualTo(nonDefaultOption1);
    softly.assertThat(getOption(nonDefaultOption2, getContentManager()))
        .as("Проверка второго недефолтного варианта")
        .isEqualTo(nonDefaultOption2);
  }
}
