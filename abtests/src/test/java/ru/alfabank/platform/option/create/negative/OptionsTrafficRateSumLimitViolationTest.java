package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionsTrafficRateSumLimitViolationTest extends OptionBaseTest {

  @Test(description = "Тест создания более одного непривязанного к виджету варианта")
  public void optionsTrafficRateSumLimitViolationNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        getRandomProductType(),
        getValidEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .99D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .01D,
        getContentManager());
    final var result = OPTION_STEPS.createOptionAssumingFail(
        false,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .01D,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Сумма доли траффика вариантов для эксперимента '" + experiment.getUuid()
            + "' не может превышать единицу");
  }
}
