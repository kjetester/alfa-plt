package ru.alfabank.platform.experiment.update.deactivate.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.CANCELLED;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
import ru.alfabank.platform.businessobjects.contentstore.Widget;

public class ExperimentDeactivationTest extends BaseTest {

  @Test(description = "Позитивный тест деактивации эксперимента")
  public void experimentDeactivationPositiveTest() {
    final var experimentEndDate = getValidExperimentEndDatePlusWeek();
    final var page_id = PAGES_STEPS.createEnabledPage(getContentManager());
    final var default_widget_1 = DRAFT_STEPS.createWidget(
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
    final var default_widget_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        default_widget_1,
        desktop,
        true,
        DEFAULT,
        true,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var abTest_widget_1 = DRAFT_STEPS.createWidget(
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
    final var abTest_widget_1_1 = DRAFT_STEPS.createWidget(
        CREATED_PAGES.get(page_id),
        abTest_widget_1,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        List.of(RU),
        null,
        null,
        getContentManager());
    final var device = default_widget_1.getDevice();
    final var trafficRate = .5D;
    var actualExperiment = EXPERIMENT_STEPS.createExperiment(
        device,
        page_id,
        getRandomProductType(),
        experimentEndDate,
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(default_widget_1.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(abTest_widget_1.getUid()),
        actualExperiment.getUuid(),
        trafficRate,
        getContentManager());
    actualExperiment = EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    // TEST //
    actualExperiment = EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        actualExperiment,
        getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(actualExperiment, getContentManager()).equals(
        new Experiment.Builder()
            .setUuid(actualExperiment.getUuid())
            .setCookieValue(actualExperiment.getCookieValue())
            .setDescription(actualExperiment.getDescription())
            .setPageId(actualExperiment.getPageId())
            .setProductTypeKey(actualExperiment.getProductTypeKey())
            .setEndDate(actualExperiment.getEndDate())
            .setTrafficRate(actualExperiment.getTrafficRate())
            .setDevice(actualExperiment.getDevice())
            .setEnabled(false)
            .setCreationDate(actualExperiment.getCreationDate())
            .setCreatedBy(actualExperiment.getCreatedBy())
            .setActivationDate(actualExperiment.getActivationDate())
            .setActivatedBy(actualExperiment.getActivatedBy())
            .setDeactivationDate(getCurrentDateTime().toString())
            .setDeactivatedBy(getContentManager().getLogin())
            .setStatus(CANCELLED)
            .build());
    final var expectedWidgetsList = List.of(
        new Widget.Builder()
            .using(default_widget_1)
            .setChildren(List.of(default_widget_1_1))
            .build(),
        new Widget.Builder()
            .using(abTest_widget_1)
            .setExperimentOptionName(DEFAULT.toString())
            .setChildren(
                List.of(new Widget.Builder()
                    .using(abTest_widget_1_1)
                    .setExperimentOptionName(DEFAULT.toString())
                    .build()))
            .build());
    final var actualWidgetsList = PAGES_STEPS.getWidgetsList(page_id, device, getContentManager());
    IntStream.range(0, expectedWidgetsList.size()).forEach(i ->
        actualWidgetsList.get(i).equals(expectedWidgetsList.get(i)));
  }
}
