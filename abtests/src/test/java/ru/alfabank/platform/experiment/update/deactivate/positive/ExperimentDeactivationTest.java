package ru.alfabank.platform.experiment.update.deactivate.positive;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.CANCELLED;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Widget;

public class ExperimentDeactivationTest extends BaseTest {

  @Test (description = "Позитивный тест деактивации эксперимента")
  public void experimentDeactivationTest() {
    final var experimentEndDate = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(
        page,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var widget1_1 = createWidget(
        page,
        widget1,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    page = createdPages.get(pageId);
    final var widget2 = createWidget(
        page,
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var widget2_1 = createWidget(
        page,
        widget2,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        null,
        null,
        getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), experimentEndDate, trafficRate, getContentManager());
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    actualExperiment = runExperimentAssumingSuccess(actualExperiment, getContentManager());
    // TEST //
    actualExperiment = stopExperimentAssumingSuccess(actualExperiment, getContentManager());
    getExperiment(actualExperiment, getContentManager()).equals(new Experiment.Builder()
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
            .using(widget1)
            .setChildren(List.of(widget1_1))
            .build(),
        new Widget.Builder()
            .using(widget2)
            .setExperimentOptionName(DEFAULT.toString())
            .setChildren(
                List.of(new Widget.Builder()
                    .using(widget2_1)
                    .setExperimentOptionName(DEFAULT.toString())
                    .build()))
            .build());
    final var actualWidgetsList = getWidgetsList(pageId, device, getContentManager());
    IntStream.range(0, expectedWidgetsList.size()).forEach(i ->
        assertThat(actualWidgetsList.get(i)).isEqualTo(expectedWidgetsList.get(i)));
  }
}
