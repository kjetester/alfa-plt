package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.option.OptionBaseTest;

public class DefaultOptionLimitViolationTest extends OptionBaseTest {

  @Test(description = "Тест создания более одного дефолтного варианта одного эксперимента")
  public void defaultOptionLimitViolationNegativeTest() {
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var widget1 = DRAFT_STEPS.createWidget(
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
    final var device = widget1.getDevice();
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        getValidExperimentEndDate(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget1.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    final var result = OPTION_STEPS.createOptionAssumingFail(
        true,
        List.of(widget2.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для эксперимента '" + experiment.getUuid()
            + "' уже существует дефолтный вариант");
  }
}
