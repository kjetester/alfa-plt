package ru.alfabank.platform.option.create.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionCreateTest extends OptionBaseTest {

  @Test(description = "Позитивный тест создания вариантов", dataProvider = "dataProvider")
  public void optionCreatePositiveTest(final Boolean isDefaultNotAssignedWidget) {
    final var experimentEnd = getValidExperimentEndDatePlusWeek();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    Widget widget;
    if (isDefaultNotAssignedWidget) {
      widget = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          null,
          desktop,
          false,
          FOR_AB_TEST,
          false,
          List.of(RU),
          null,
          null,
          getContentManager());
    } else {
      DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          null,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
      final var widget1_1 = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          null,
          desktop,
          false,
          FOR_AB_TEST,
          false,
          List.of(RU),
          null,
          null,
          getContentManager());
      final var widget1_1_1 = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          widget1_1,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
      widget = DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          widget1_1_1,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
      DRAFT_STEPS.createWidget(
          CREATED_PAGES.get(page_id),
          widget,
          desktop,
          true,
          DEFAULT,
          true,
          List.of(RU),
          null,
          null,
          getContentManager());
    }
    final var widget2 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget2_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget2_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var widget2_1_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        widget2_1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var device = widget2.getDevice();
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    Option defaultOption;
    Option nonDefaultOption2;
    final var nonDefaultOption1 = OPTION_STEPS.createOption(
        false,
        List.of(widget2_1_1.getUid()),
        experiment.getUuid(),
        .33D,
        getContentManager());
    if (isDefaultNotAssignedWidget) {
      defaultOption = OPTION_STEPS.createOption(
          true,
          null,
          experiment.getUuid(),
          .33D,
          getContentManager());
      nonDefaultOption2 = OPTION_STEPS.createOption(
          false,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .34D,
          getContentManager());
    } else {
      defaultOption = OPTION_STEPS.createOption(
          true,
          List.of(widget.getUid()),
          experiment.getUuid(),
          .33D,
          getContentManager());
      nonDefaultOption2 = OPTION_STEPS.createOption(
          false,
          List.of(),
          experiment.getUuid(),
          .34D,
          getContentManager());
    }
    final var softly = new SoftAssertions();
    softly.assertThat(OPTION_STEPS.getOption(defaultOption, getContentManager()))
        .as("Проверка дефолтного варианта")
        .isEqualTo(defaultOption);
    softly.assertThat(OPTION_STEPS.getOption(nonDefaultOption1, getContentManager()))
        .as("Проверка перого недефолтного варианта")
        .isEqualTo(nonDefaultOption1);
    softly.assertThat(OPTION_STEPS.getOption(nonDefaultOption2, getContentManager()))
        .as("Проверка второго недефолтного варианта")
        .isEqualTo(nonDefaultOption2);
  }
}
