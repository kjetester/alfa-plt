package ru.alfabank.platform.experiment.update.activate.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;

public class ExperimentActivationTwoTest extends BaseTest {

  @Test (description = "Тест запуска эксперимента с условиями:"
      + "\n\t1. На странице нет запущенных экспериментов"
      + "\n\t2. Даты активности страницы НЕ установлены"
      + "\n\t3. Даты активности виджетов НЕ установлены"
      + "\n\t4. Статус эксперимента 'DISABLED'"
      + "\n\t5. Дата окончания эксперимента более текущей даты + 1 день"
      + "\n\t6. У эксперимента есть 3 варианта:"
      + "\n\t\t1. Дефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=true"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true"
      + "\n\t\t2. Недефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false"
      + "\n\t\t3. Недефолтный вариант не привязан виджету")
  public void positiveExperimentActivationTwoTest() {
    final var start = getCurrentDateTime().plusSeconds(10).toString();
    final var end = getCurrentDateTime().plusHours(27).plusMinutes(10).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(start, end, true);
    final var widget1 = createWidget(createdPages.get(page.getId()),
        null,
        desktop,
        true,
        DEFAULT,
        true,
        start,
        end);
    final var widget2 = createWidget(createdPages.get(page.getId()),
        null,
        desktop,
        false,
        FOR_AB_TEST,
        false,
        start,
        end);
    page = createdPages.get(page.getId());
    final var actualExperiment =
        createExperiment(createdPages.get(page.getId()).getWidgetList().get(0).getDevice(),
            page.getId(), getRandomProductType(), experimentEnd, .5D);
    createOption(true, List.of(widget1.getUid()), actualExperiment.getUuid(), .33D);
    createOption(false, List.of(widget2.getUid()), actualExperiment.getUuid(),.33D);
    createOption(false, null, actualExperiment.getUuid(),.34D);
    final var expectedExperiment = new Experiment.Builder()
        .setUuid(actualExperiment.getUuid())
        .setCookieValue(actualExperiment.getCookieValue())
        .setDescription(actualExperiment.getDescription())
        .setPageId(actualExperiment.getPageId())
        .setProductTypeKey(actualExperiment.getProductTypeKey())
        .setEndDate(actualExperiment.getEndDate())
        .setTrafficRate(actualExperiment.getTrafficRate())
        .setDevice(actualExperiment.getDevice())
        .setEnabled(true)
        .setCreatedBy(USER.getLogin())
        .setActivatedBy(USER.getLogin())
        .setActivationDate(start)
        .setStatus(RUNNING)
        .setCreationDate(start)
        .build();

    // TEST //
    runExperimentAssumingSuccess(actualExperiment).checkActivatedExperiment(expectedExperiment);
  }
}