package ru.alfabank.platform.experiment.update.activate.positive;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Widget;

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
  public void experimentActivationPositiveOneTest() {
    final var start = getCurrentDateTime().toString();
    final var end = getValidEndDatePlus10Minutes();
    final var page_id = PAGES_STEPS.createPage(start, end, true, getContentManager());
    // Создание экспериментов для DESKTOP и MOBILE версии страницы
    final var desktopExperiment = EXPERIMENT_STEPS.createExperiment(desktop, page_id,
        getRandomProductType(), getValidEndDate(), .5D, getContentManager());
    final var mobileExperiment = EXPERIMENT_STEPS.createExperiment(mobile, page_id,
        getRandomProductType(), getValidEndDate(), .5D, getContentManager());
    // Создание дерева виджетов по варианту №1 для DESKTOP версии страницы
    final var var_1_desktop_abTest_widget_1 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        null, desktop, false, FOR_AB_TEST, false, start, end, getContentManager());
    final var var_1_desktop_default_widget_2 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_1_desktop_abTest_widget_1, desktop, true, DEFAULT, true, start, end,
        getContentManager());
    final var var_1_desktop_default_widget_3 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_1_desktop_default_widget_2, desktop, true, DEFAULT, true, start, end,
        getContentManager());
    // И варианта по-умолчанию
    final var desktop_default_option = OPTION_STEPS.createOption(true,
        List.of(var_1_desktop_default_widget_2.getUid()), desktopExperiment.getUuid(), .5D,
        getContentManager());
    // Создание дерева виджетов по варианту №2 для DESKTOP версии страницы
    final var var_2_desktop_default_widget_1 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        null, desktop, true, DEFAULT, true, null, null, getContentManager());
    final var var_2_desktop_abTest_widget_2 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_2_desktop_default_widget_1, desktop, false, FOR_AB_TEST, false, null, null,
        getContentManager());
    final var var_2_desktop_abTest_widget_3 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_2_desktop_abTest_widget_2, desktop, false, FOR_AB_TEST, false, null, null,
        getContentManager());
    // И тестового варианта
    final var desktop_abTest_option = OPTION_STEPS.createOption(false,
        List.of(var_2_desktop_abTest_widget_2.getUid()), desktopExperiment.getUuid(), .5D,
        getContentManager());
    // Создание дерева виджетов по варианту №1 для MOBILE версии страницы
    final var var_1_mobile_abTest_widget_1 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        null, mobile, false, FOR_AB_TEST, false, start, end, getContentManager());
    final var var_1_mobile_default_widget_2 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_1_mobile_abTest_widget_1, mobile, true, DEFAULT, true, start, end,
        getContentManager());
    final var var_1_mobile_default_widget_3 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_1_mobile_default_widget_2, mobile, true, DEFAULT, true, start, end,
        getContentManager());
    // И варианта по-умолчанию
    final var mobile_default_option = OPTION_STEPS.createOption(true,
        List.of(var_1_mobile_default_widget_2.getUid()), mobileExperiment.getUuid(), .5D,
        getContentManager());
    // Создание дерева виджетов по варианту №2 для MOBILE версии страницы
    final var var_2_mobile_default_widget_1 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        null, mobile, true, DEFAULT, true, start, end, getContentManager());
    final var var_2_mobile_abTest_widget_2 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_2_mobile_default_widget_1, mobile, false, FOR_AB_TEST, false, null, null,
        getContentManager());
    final var var_2_mobile_abTest_widget_3 = DRAFT_STEPS.createWidget(CREATED_PAGES.get(page_id),
        var_2_mobile_abTest_widget_2, mobile, false, FOR_AB_TEST, false, null, null,
        getContentManager());
    // И тестового варианта
    final var mobile_abTest_option = OPTION_STEPS.createOption(false,
        List.of(var_2_mobile_abTest_widget_2.getUid()), mobileExperiment.getUuid(), .5D,
        getContentManager());
    // Проверка активации DESKTOP экспермиента
    final var expectedDesktopExperiment = new Experiment.Builder()
        //TODO: .using(desktopExperiment)
        .setUuid(desktopExperiment.getUuid())
        .setCookieValue(desktopExperiment.getCookieValue())
        .setDescription(desktopExperiment.getDescription())
        .setPageId(desktopExperiment.getPageId())
        .setProductTypeKey(desktopExperiment.getProductTypeKey())
        .setEndDate(desktopExperiment.getEndDate())
        .setTrafficRate(desktopExperiment.getTrafficRate())
        .setDevice(desktopExperiment.getDevice())
        .setEnabled(true)
        .setCreatedBy(getContentManager().getLogin())
        .setActivatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(RUNNING)
        .setCreationDate(start)
        .build();
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(desktopExperiment, getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(desktopExperiment, getContentManager())
        .equals(expectedDesktopExperiment);
    // Проверка активации MOBILE экспермиента
    final var expectedMobileExperiment = new Experiment.Builder()
        //TODO: .using(mobileExperiment)
        .setUuid(mobileExperiment.getUuid())
        .setCookieValue(mobileExperiment.getCookieValue())
        .setDescription(mobileExperiment.getDescription())
        .setPageId(mobileExperiment.getPageId())
        .setProductTypeKey(mobileExperiment.getProductTypeKey())
        .setEndDate(mobileExperiment.getEndDate())
        .setTrafficRate(mobileExperiment.getTrafficRate())
        .setDevice(mobileExperiment.getDevice())
        .setEnabled(true)
        .setCreatedBy(getContentManager().getLogin())
        .setActivatedBy(getContentManager().getLogin())
        .setActivationDate(start)
        .setStatus(RUNNING)
        .setCreationDate(start)
        .build();
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(mobileExperiment, getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(mobileExperiment, getContentManager())
        .equals(expectedMobileExperiment);
    // Проверка переопределения названий DESKTOP виджетов
    final var expectedDesktopWidgetsList = List.of(
        new Widget.Builder()
            .using(var_1_desktop_abTest_widget_1)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_1_desktop_default_widget_2)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_1_desktop_default_widget_3)
                            .setExperimentOptionName(
                                desktop_default_option.getName())
                            .build())).build())).build(),
        new Widget.Builder()
            .using(var_2_desktop_default_widget_1)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_2_desktop_abTest_widget_2)
                    .setExperimentOptionName(desktop_abTest_option.getName())
                    .isEnabled(true)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_2_desktop_abTest_widget_3)
                            .setExperimentOptionName(desktop_abTest_option.getName())
                            .isEnabled(true)
                            .build())).build())).build());
    final var actual_desktop_widgetsList = PAGES_STEPS.getWidgetsList(
        page_id,
        desktop,
        getContentManager());
    IntStream.range(0, expectedDesktopWidgetsList.size()).forEach(i ->
        assertThat(actual_desktop_widgetsList.get(i)).isEqualTo(expectedDesktopWidgetsList.get(i)));

    // Проверка переопределения названий MOBILE виджетов
    final var expected_mobile_widgets_list = List.of(
        new Widget.Builder()
            .using(var_1_mobile_abTest_widget_1)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_1_mobile_default_widget_2)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_1_mobile_default_widget_3)
                            .setExperimentOptionName(
                                mobile_default_option.getName())
                            .build())).build())).build(),
        new Widget.Builder()
            .using(var_2_mobile_default_widget_1)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_2_mobile_abTest_widget_2)
                    .setExperimentOptionName(mobile_abTest_option.getName())
                    .isEnabled(true)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_2_mobile_abTest_widget_3)
                            .setExperimentOptionName(mobile_abTest_option.getName())
                            .isEnabled(true)
                            .build())).build())).build());
    final var actual_mobile_widgets_list = PAGES_STEPS.getWidgetsList(
        page_id,
        mobile,
        getContentManager());
    IntStream.range(0, expected_mobile_widgets_list.size()).forEach(i ->
        assertThat(actual_mobile_widgets_list.get(i))
            .isEqualTo(expected_mobile_widgets_list.get(i)));
  }
}
