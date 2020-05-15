package ru.alfabank.platform.experiment.update.activate.positive;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.enums.User;

public class ExperimentActivationOneTest extends BaseTest {

  @Test(description = "Тест запуска эксперимента с условиями:"
      + "\n\t1. На странице нет запущенных экспериментов"
      + "\n\t2. Даты активности страницы установлены"
      + "\n\t3. Даты активности виджетов установлены"
      + "\n\t4. Статус эксперимента 'DISABLED'"
      + "\n\t5. Даты активности страницы не в диапазоне дат проведения эксперимента"
      + "\n\t6. Даты активности виджетов не в диапазоне дат проведения эксперимента"
      + "\n\t7. Дата окончания эксперимента более текущей даты + 1 день"
      + "\n\t8. У эксперимента есть 2 варианта:"
      + "\n\t\t1. Дефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=true"
      + "\n\t\t\t* experimentOptionName=default"
      + "\n\t\t\t* defaultWidget=true"
      + "\n\t\t\t* предок experimentOptionName=forABtest"
      + "\n\t\t\t* родитель experimentOptionName=default"
      + "\n\t\t\t* ребенок experimentOptionName=default"
      + "\n\t\t2. Недефолтный вариант привязан к нешаренному виджету, у которого:"
      + "\n\t\t\t* enable=false"
      + "\n\t\t\t* experimentOptionName=forABtest"
      + "\n\t\t\t* defaultWidget=false"
      + "\n\t\t\t* родитель experimentOptionName=default"
      + "\n\t\t\t* ребенок experimentOptionName=forABtest")
  public void positiveExperimentActivationOneTest() {
    setUser(User.CONTENT_MANAGER);
    final var start = getCurrentDateTime().toString();
    final var end = getCurrentDateTime().plusHours(27).plusMinutes(20).toString();
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(15).toString();
    var page = createPage(start, end, true);
    final var pageId = page.getId();
    // Создание эксперимента для DESKTOP версии страницы
    final var desktopWidget0 = createWidget(
        page, null, desktop, false, FOR_AB_TEST, false, start, end);
    final var desktopWidget1 = createWidget(
        page, desktopWidget0, desktop, true, DEFAULT, true, start, end);
    final var desktopWidget1_1 = createWidget(
        page, desktopWidget1, desktop, true, DEFAULT, true, start, end);
    final var desktopWidget1_1_1 = createWidget(
        page, desktopWidget1_1, desktop, true, DEFAULT, true, start, end);
    page = createdPages.get(pageId);
    final var desktopWidget2 = createWidget(
        page, null, desktop, true, DEFAULT, true, null, null);
    final var desktopWidget2_1 = createWidget(
        page, desktopWidget2, desktop, false, FOR_AB_TEST, false, null, null);
    final var desktopWidget2_1_1 = createWidget(
        page, desktopWidget2_1, desktop, false, FOR_AB_TEST, false, null, null);
    page = createdPages.get(pageId);
    final var desktopExperiment = createExperiment(
        desktop, pageId, getRandomProductType(), experimentEnd, .5D);
    final var desktopExperimentDefaultOption = createOption(
        true, List.of(desktopWidget1_1.getUid()), desktopExperiment.getUuid(), .5D);
    final var desktopExperimentAbTestOption = createOption(
        false, List.of(desktopWidget2_1.getUid()), desktopExperiment.getUuid(), .5D);
    // Создание эксперимента для MOBILE версии страницы
    final var mobileWidget0 = createWidget(
        page, null, mobile, false, FOR_AB_TEST, false, start, end);
    final var mobileWidget1 = createWidget(
        page, mobileWidget0, mobile, true, DEFAULT, true, start, end);
    final var mobileWidget1_1 = createWidget(
        page, mobileWidget1, mobile, true, DEFAULT, true, start, end);
    final var mobileWidget1_1_1 = createWidget(
        page, mobileWidget1_1, mobile, true, DEFAULT, true, start, end);
    page = createdPages.get(pageId);
    final var mobileWidget2 = createWidget(
        page, null, mobile, true, DEFAULT, true, null, null);
    final var mobileWidget2_1 = createWidget(
        page, mobileWidget2, mobile, false, FOR_AB_TEST, false, null, null);
    final var mobileWidget2_1_1 = createWidget(
        page, mobileWidget2_1, mobile, false, FOR_AB_TEST, false, null, null);
    final var mobileExperiment = createExperiment(
        mobile, pageId, getRandomProductType(), experimentEnd, .5D);
    final var mobileExperimentDefaultOption = createOption(
        true, List.of(mobileWidget1_1.getUid()), mobileExperiment.getUuid(), .5D);
    final var mobileExperimentAbTestOption = createOption(
        false, List.of(mobileWidget2_1.getUid()), mobileExperiment.getUuid(), .5D);

    // Проверка запуска экспермиента для DESKTOP версии страницы
    final var expectedDesktopExperiment = new Experiment.Builder()
        .setUuid(desktopExperiment.getUuid())
        .setCookieValue(desktopExperiment.getCookieValue())
        .setDescription(desktopExperiment.getDescription())
        .setPageId(desktopExperiment.getPageId())
        .setProductTypeKey(desktopExperiment.getProductTypeKey())
        .setEndDate(desktopExperiment.getEndDate())
        .setTrafficRate(desktopExperiment.getTrafficRate())
        .setDevice(desktopExperiment.getDevice())
        .setEnabled(true)
        .setCreatedBy(getUser().getLogin())
        .setActivatedBy(getUser().getLogin())
        .setActivationDate(start)
        .setStatus(RUNNING)
        .setCreationDate(start)
        .build();
    runExperimentAssumingSuccess(desktopExperiment);
    getExperiment(desktopExperiment).equals(expectedDesktopExperiment);

    // Проверка запуска экспермиента для MOBILE версии страницы
    final var expectedMobileExperiment = new Experiment.Builder()
        .setUuid(mobileExperiment.getUuid())
        .setCookieValue(mobileExperiment.getCookieValue())
        .setDescription(mobileExperiment.getDescription())
        .setPageId(mobileExperiment.getPageId())
        .setProductTypeKey(mobileExperiment.getProductTypeKey())
        .setEndDate(mobileExperiment.getEndDate())
        .setTrafficRate(mobileExperiment.getTrafficRate())
        .setDevice(mobileExperiment.getDevice())
        .setEnabled(true)
        .setCreatedBy(getUser().getLogin())
        .setActivatedBy(getUser().getLogin())
        .setActivationDate(start)
        .setStatus(RUNNING)
        .setCreationDate(start)
        .build();
    runExperimentAssumingSuccess(mobileExperiment);
    getExperiment(mobileExperiment).equals(expectedMobileExperiment);

    // Проверка переопределения названий DESKTOP виджетов
    final var expectedDesktopWidgetsList = List.of(
        new Widget.Builder()
            .using(desktopWidget0)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(desktopWidget1)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(desktopWidget1_1)
                            .setExperimentOptionName(
                                desktopExperimentDefaultOption.getName())
                            .setChildren(List.of(
                                new Widget.Builder()
                                    .using(desktopWidget1_1_1)
                                    .setExperimentOptionName(
                                        desktopExperimentDefaultOption.getName())
                                    .build())).build())).build())).build(),
        new Widget.Builder()
            .using(desktopWidget2)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(desktopWidget2_1)
                    .setExperimentOptionName(desktopExperimentAbTestOption.getName())
                    .isEnabled(true)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(desktopWidget2_1_1)
                            .setExperimentOptionName(desktopExperimentAbTestOption.getName())
                            .isEnabled(true)
                            .build())).build())).build());
    final var actualDesktopWidgetsList = getWidgetsList(pageId, desktop);
    IntStream.range(0, expectedDesktopWidgetsList.size()).forEach(i ->
        assertThat(actualDesktopWidgetsList.get(i)).isEqualTo(expectedDesktopWidgetsList.get(i)));

    // Проверка переопределения названий MOBILE виджетов
    final var expectedMobileWidgetsList = List.of(
        new Widget.Builder()
            .using(mobileWidget0)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(mobileWidget1)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(mobileWidget1_1)
                            .setExperimentOptionName(
                                mobileExperimentDefaultOption.getName())
                            .setChildren(List.of(
                                new Widget.Builder()
                                    .using(mobileWidget1_1_1)
                                    .setExperimentOptionName(
                                        mobileExperimentDefaultOption.getName())
                                    .build())).build())).build())).build(),
        new Widget.Builder()
            .using(mobileWidget2)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(mobileWidget2_1)
                    .setExperimentOptionName(mobileExperimentAbTestOption.getName())
                    .isEnabled(true)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(mobileWidget2_1_1)
                            .setExperimentOptionName(mobileExperimentAbTestOption.getName())
                            .isEnabled(true)
                            .build())).build())).build());
    final var actualMobileWidgetsList = getWidgetsList(pageId, mobile);
    IntStream.range(0, expectedMobileWidgetsList.size()).forEach(i ->
        assertThat(actualMobileWidgetsList.get(i)).isEqualTo(expectedMobileWidgetsList.get(i)));
  }
}
