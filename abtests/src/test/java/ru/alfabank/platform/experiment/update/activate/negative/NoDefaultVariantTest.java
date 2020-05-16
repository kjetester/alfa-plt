package ru.alfabank.platform.experiment.update.activate.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class NoDefaultVariantTest extends BaseTest {

  // FIXME: 09.05.2020 http://ci-mob-slave9/report/ui/#alfasite-platform/launches/all%7Cpage.page=1&page.size=50&page.sort=start_time,number%2CDESC/5eb5a9199835a80001806aa2%7Cpage.page=1&page.size=50&filter.eq.has_childs=false&filter.in.issue$issue_type=TI001&page.sort=start_time%2CASC?page.page=1&page.size=50&filter.eq.has_childs=false&filter.in.issue$issue_type=TI001&page.sort=start_time%2CASC&log.item=5eb5acad9835a8000180a192
  @Test (description = "Тест активации эксперимента с негативным условием:"
      + "\n\tНет дефолтного варианта")
  public void noDefaultVariantTest() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true, getContentManager());
    final var pageId = page.getId();
    final var widget1 = createWidget(
        createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var widget2 = createWidget(
        createdPages.get(pageId), null, desktop, false, FOR_AB_TEST, false, null, null, getContentManager());
    final var device = widget1.getDevice();
    final var trafficRate = .5D;
    final var actualExperiment =
        createExperiment(device, pageId, getRandomProductType(), end, trafficRate, getContentManager());
    createOption(false, List.of(widget1.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(), trafficRate, getContentManager());
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(false)
        .setCreatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(DISABLED)
        .setCreationDate(start)
        .build();
    final var result = runExperimentAssumingFail(actualExperiment, getContentManager());
    assertThat(result.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);;
    assertThat(result.asString())
        .as("Проверка сообщения об ошибке")
        .contains("Для эксперимента '" + actualExperiment.getUuid()
            + "' должен существовать ровно один вариант по умолчанию");
    getExperiment(actualExperiment, getContentManager()).checkUpdatedExperiment(expectedExperiment);
  }
}
