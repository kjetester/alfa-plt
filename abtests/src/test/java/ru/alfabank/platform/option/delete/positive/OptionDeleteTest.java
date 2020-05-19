package ru.alfabank.platform.option.delete.positive;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionDeleteTest extends OptionBaseTest {

  private static final String NAME = randomAlphanumeric(16);
  private static final String DESCRIPTION = randomAlphanumeric(50);

  private Widget defaultWidget;
  private Widget abTestWidget;
  private Experiment experiment;

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    final var experimentEnd = getValidEndDatePlusWeek();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    defaultWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    abTestWidget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    experiment = EXPERIMENT_STEPS.createExperiment(
        defaultWidget.getDevice(),
        page_id,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
  }

  @Test(description = "Позитивный тест удаления вариантов")
  public void optionDeletePositiveTest() {
    final var createdDefaultOption = OPTION_STEPS.createOption(
        new Option.Builder()
            .setDefault(true)
            .setName(NAME + "1")
            .setDescription(DESCRIPTION + "1")
            .setWidgetUids(List.of(defaultWidget.getUid()))
            .setExperimentUuid(experiment.getUuid())
            .setTrafficRate(.5D)
            .build(), getContentManager());
    final var createdAbTestOption = OPTION_STEPS.createOption(
        new Option.Builder()
            .setDefault(false)
            .setName(NAME + "2")
            .setDescription(DESCRIPTION + "2")
            .setWidgetUids(List.of(abTestWidget.getUid()))
            .setExperimentUuid(experiment.getUuid())
            .setTrafficRate(.5D)
            .build(), getContentManager());
    OPTION_STEPS.deleteOption(createdDefaultOption, getContentManager());
    OPTION_STEPS.deleteOption(createdAbTestOption, getContentManager());
    assertThat(OPTION_STEPS.getAbsentOption(
        createdDefaultOption,
        getContentManager()).getStatusCode())
        .as("Проверка отсутствия варианта")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(OPTION_STEPS.getAbsentOption(
        createdAbTestOption,
        getContentManager()).getStatusCode())
        .as("Проверка отсутствия варианта")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
  }
}
