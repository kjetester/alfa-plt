package ru.alfabank.platform.experiment.involvements.negative;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.apache.log4j.LogManager;
import org.testng.SkipException;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.experiment.involvements.InvolvementsBaseTest;

public class InvolvementsTest extends InvolvementsBaseTest {

  @Test (description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'DISABLED'",
      dataProvider = "dataProvider")
  public void involvementsDisabledExperimentTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка пользователя") final List<String> geos) {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusHours(27).plusMinutes(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, start, end);
    final var experimentDevice = widget1.getDevice();
    final var experiment = createExperiment(
        experimentDevice, pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), experiment.getUuid(), .5D);
    createOption(false, null, experiment.getUuid(),.5D);
    // TEST //
    Response response = getInvolvements(pageId, clientDevice, geos);
    assertThat(response.getBody().asString())
        .as("Проверка отсутствия переданных данных какого-либо эксперимента")
        .isNullOrEmpty();
  }

  @Test (description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'CANCELLED'",
      dataProvider = "dataProvider")
  public void involvementsCancelledExperimentTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка пользователя") final List<String> geos) {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusHours(27).plusMinutes(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, true);
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, start, end);
    final var experimentDevice = widget1.getDevice();
    final var experiment = createExperiment(
        experimentDevice, pageId, getRandomProductType(), experimentEnd, .5D);
    final var option1 = createOption(true, List.of(widget1.getUid()), experiment.getUuid(), .5D);
    final var option2 = createOption(false, null, experiment.getUuid(),.5D);
    final var runningExperiment = runExperimentAssumingSuccess(experiment);
    stopExperimentAssumingSuccess(runningExperiment);
    // TEST //
    Response response = getInvolvements(pageId, clientDevice, geos);
    assertThat(response.getBody().asString())
        .as("Проверка отсутствия переданных данных какого-либо эксперимента")
        .isNullOrEmpty();
  }

  @Test (description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'EXPIRED'")
  public void involvementsExpiredExperimentTest() {
    LogManager.getLogger(InvolvementsTest.class).warn("Manual Testing Needed");
    throw new SkipException("Manual Testing Needed");
  }
}
