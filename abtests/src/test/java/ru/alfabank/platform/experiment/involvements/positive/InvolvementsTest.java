package ru.alfabank.platform.experiment.involvements.positive;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.experiment.involvements.InvolvementsBaseTest;

public class InvolvementsTest extends InvolvementsBaseTest {

  @Test (description = "Тест получения признака участия в эксперименте\n"
      + "\t Статус эксперимента 'RUNNING'",
      dataProvider = "dataProvider")
  public void involvementsRunningExperimentTest(
      @ParameterKey("Устройство пользователя") final Device clientDevice,
      @ParameterKey("Гео-метка пользователя") final List<String> geos) {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusHours(27).plusMinutes(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(page, null, desktop, true, DEFAULT, true, start, end, getContentManager());
    page = createdPages.get(pageId);
    final var widget2 = createWidget(page, null, desktop, false, FOR_AB_TEST, false, start, end, getContentManager());
    final var experimentDevice = widget1.getDevice();
    final var experiment = createExperiment(
        experimentDevice, pageId, getRandomProductType(), experimentEnd, .5D, getContentManager());
    final var option1 = createOption(true, List.of(widget1.getUid()), experiment.getUuid(), .33D, getContentManager());
    option2 = createOption(false, null, experiment.getUuid(), .33D, getContentManager());
    option3 = createOption(false, List.of(widget2.getUid()), experiment.getUuid(), .34D, getContentManager());
    final var runningExperiment = runExperimentAssumingSuccess(experiment, getContentManager());
    // TEST //
    Response response = getInvolvements(pageId, clientDevice, geos, getContentManager());
    final var softly = new SoftAssertions();
    if (clientDevice.equals(experimentDevice)) {
      assertThat(response.jsonPath().getBoolean("involved"))
          .as("Приверка наличия признака вовлеченность")
          .isNotNull();
      if (response.jsonPath().getBoolean("involved")) {
        final var optionName = response.jsonPath().getString("optionName");
        softly.assertThat(optionName)
            .as("Проверка наименования варианта АБ-теста")
            .isIn(option2.getName(), option3.getName());
        if (optionName.equals(option2.getName())) {
          option2counter++;
        } else {
          option3counter++;
        }
      } else {
        softly.assertThat(response.jsonPath().getString("optionName"))
            .as("Проверка наименования дефолтного варианта")
            .isEqualTo(option1.getName());
      }
      softly.assertThat(response.jsonPath().getString("uuid"))
          .as("Проверка UUID эксперимента")
          .isEqualTo(runningExperiment.getUuid());
      softly.assertThat(response.jsonPath().getString("cookieValue"))
          .as("Проверка cookieValue эксперимента")
          .isEqualTo(runningExperiment.getCookieValue());
      softly.assertThat(response.jsonPath().getString("endDate"))
          .as("Проверка даты завершения эксперимента")
          .isEqualTo(runningExperiment.getEndDate());
    } else {
      assertThat(response.getBody().asString())
          .as("Проверка отсутствия переданных данных какого-либо эксперимента")
          .isNullOrEmpty();
    }
    softly.assertAll();
  }
}
