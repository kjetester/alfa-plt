package ru.alfabank.platform.option.delete.positive;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.Device.desktop;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.ProductType.getRandomProductType;

import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.option.OptionBaseTest;

public class OptionDeleteTest extends OptionBaseTest {

  private Widget defaultWidget;
  private Widget abTestWidget;
  private Experiment experiment;
  private static final String NAME = randomAlphanumeric(16);
  private static final String DESCRIPTION = randomAlphanumeric(50);

  /**
   * Before Class.
   */
  @BeforeClass
  public void beforeClass() {
    final var experimentEnd = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    defaultWidget = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    abTestWidget = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, null, null);
    experiment = createExperiment(
        defaultWidget.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D);
  }

  @Test(description = "Позитивный тест удаления вариантов")
  public void optionCreateTest() {
    final var createdDefaultOption = createOption(
        new Option.Builder()
            .setDefault(true)
            .setName(NAME + "1")
            .setDescription(DESCRIPTION + "1")
            .setWidgetUids(List.of(defaultWidget.getUid()))
            .setExperimentUuid(experiment.getUuid())
            .setTrafficRate(.5D)
            .build());
    final var createdAbTestOption = createOption(
        new Option.Builder()
            .setDefault(false)
            .setName(NAME + "2")
            .setDescription(DESCRIPTION + "2")
            .setWidgetUids(List.of(abTestWidget.getUid()))
            .setExperimentUuid(experiment.getUuid())
            .setTrafficRate(.5D)
            .build());
    deleteOption(createdDefaultOption);
    deleteOption(createdAbTestOption);
    assertThat(getAbsentOption(createdDefaultOption).getStatusCode())
        .as("Проверка отсутствия варианта")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(getAbsentOption(createdAbTestOption).getStatusCode())
        .as("Проверка отсутствия варианта")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
  }
}
