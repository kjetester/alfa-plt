package ru.alfabank.platform.experiment.update.activate.positive;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Widget;

public class ExperimentActivationOneTest extends BaseTest {

  private Integer pageId;
  private Experiment actualDesktopExperiment;
  private Experiment expectedDesktopExperiment;
  private Experiment actualMobileExperiment;
  private Experiment expectedMobileExperiment;
  private List<Widget> expectedDesktopWidgetsList;
  private List<Widget> expectedMobileWidgetsList;
  private List<Widget> actualDesktopWidgetsList;
  private List<Widget> actualMobileWidgetsList;

  /**
   * Before method.
   */
  @BeforeMethod(description = "Выполнение предусловий:"
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
  public void beforeMethod() {
    final String date_from = getValidWidgetDateFrom();
    final String date_to = getValidWidgetDateTo();
    pageId = PAGES_STEPS.createPage(date_from, date_to, true, getContentManager());
    // Создание дерева виджетов по варианту №1 для DESKTOP версии страницы
    final var var_1_desktop_abTest_widget_1 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), null, desktop, false, FOR_AB_TEST,
            false, List.of(RU), date_from, date_to, getContentManager());
    final var var_1_desktop_default_widget_2 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_1_desktop_abTest_widget_1, desktop,
            true, DEFAULT, true, List.of(RU), date_from, date_to, getContentManager());
    final var var_1_desktop_default_widget_3 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_1_desktop_default_widget_2,
            desktop, true, DEFAULT, true, List.of(RU), date_from, date_to,
            getContentManager());
    final var var_1_desktop_default_widget_4 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_1_desktop_default_widget_3,
            desktop, true, DEFAULT, true, List.of(RU), date_from, date_to,
            getContentManager());
    // Создание дерева виджетов по варианту №2 для DESKTOP версии страницы
    final var var_2_desktop_default_widget_1 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), null, desktop, true, DEFAULT,
            true, List.of(RU), null, null, getContentManager());
    final var var_2_desktop_abTest_widget_2 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_2_desktop_default_widget_1,
            desktop, false, FOR_AB_TEST, false, List.of(RU), null, null,
            getContentManager());
    final var var_2_desktop_abTest_widget_3 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_2_desktop_abTest_widget_2,
            desktop, false, FOR_AB_TEST, false, List.of(RU), null, null,
            getContentManager());
    // Создание дерева виджетов по варианту №1 для MOBILE версии страницы
    final var var_1_mobile_abTest_widget_1 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), null, mobile, false, FOR_AB_TEST,
            false, List.of(RU), date_from, date_to, getContentManager());
    final var var_1_mobile_default_widget_2 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_1_mobile_abTest_widget_1, mobile,
            true, DEFAULT, true, List.of(RU), date_from, date_to, getContentManager());
    final var var_1_mobile_default_widget_3 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_1_mobile_default_widget_2,
            mobile, true, DEFAULT, true, List.of(RU), date_from, date_to, getContentManager());
    // Создание дерева виджетов по варианту №2 для MOBILE версии страницы
    final var var_2_mobile_default_widget_1 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), null, mobile, true, DEFAULT,
            true, List.of(RU), date_from, date_to, getContentManager());
    final var var_2_mobile_abTest_widget_2 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_2_mobile_default_widget_1,
            mobile, false, FOR_AB_TEST, false, List.of(RU), null, null, getContentManager());
    final var var_2_mobile_abTest_widget_3 =
        DRAFT_STEPS.createWidget(CREATED_PAGES.get(pageId), var_2_mobile_abTest_widget_2, mobile,
            false, FOR_AB_TEST, false, List.of(RU), null, null, getContentManager());
    // Создание экспериментов и вариантов
    actualDesktopExperiment =
        EXPERIMENT_STEPS.createExperiment(desktop, pageId, getRandomProductType(),
            getValidExperimentEndDate(), .5D, getContentManager());
    actualMobileExperiment =
        EXPERIMENT_STEPS.createExperiment(mobile, pageId, getRandomProductType(),
            getValidExperimentEndDate(), .5D, getContentManager());
    final var desktop_default_option =
        OPTION_STEPS.createOption(true, List.of(var_1_desktop_default_widget_3.getUid()),
            actualDesktopExperiment.getUuid(), .5D, getContentManager());
    final var desktop_abTest_option =
        OPTION_STEPS.createOption(false, List.of(var_2_desktop_abTest_widget_2.getUid()),
            actualDesktopExperiment.getUuid(), .5D, getContentManager());
    final var mobile_default_option =
        OPTION_STEPS.createOption(true, List.of(var_1_mobile_default_widget_2.getUid()),
            actualMobileExperiment.getUuid(), .5D, getContentManager());
    final var mobile_abTest_option =
        OPTION_STEPS.createOption(false, List.of(var_2_mobile_abTest_widget_2.getUid()),
            actualMobileExperiment.getUuid(), .5D, getContentManager());
    expectedDesktopExperiment = new Experiment.Builder()
        .using(actualDesktopExperiment)
        .setCreationDate(getCurrentDateTime().toString())
        .setCreatedBy(getContentManager().getLogin())
        .setStatus(RUNNING)
        .setEnabled(true)
        .setActivationDate(getCurrentDateTime().toString())
        .setActivatedBy(getContentManager().getLogin())
        .build();
    expectedMobileExperiment = new Experiment.Builder()
        .using(actualMobileExperiment)
        .setCreationDate(getCurrentDateTime().toString())
        .setCreatedBy(getContentManager().getLogin())
        .setStatus(RUNNING)
        .setEnabled(true)
        .setActivationDate(getCurrentDateTime().toString())
        .setActivatedBy(getContentManager().getLogin())
        .build();
    expectedDesktopWidgetsList = List.of(
        new Widget.Builder()
            .using(var_1_desktop_abTest_widget_1)
            .setReused(false)
            .setOrderNumber(1)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_1_desktop_default_widget_2)
                    .setReused(false)
                    .setOrderNumber(1)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_1_desktop_default_widget_3)
                            .setReused(false)
                            .setOrderNumber(1)
                            .setExperimentOptionName(desktop_default_option.getName())
                            .setChildren(List.of(
                                new Widget.Builder()
                                    .using(var_1_desktop_default_widget_4)
                                    .setReused(false)
                                    .setOrderNumber(1)
                                    .setExperimentOptionName(desktop_default_option.getName())
                                    .build()))
                            .build()))
                    .build()))
            .build(),
        new Widget.Builder()
            .using(var_2_desktop_default_widget_1)
            .setReused(false)
            .setOrderNumber(2)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_2_desktop_abTest_widget_2)
                    .setReused(false)
                    .setOrderNumber(1)
                    .setExperimentOptionName(desktop_abTest_option.getName())
                    .isEnabled(true)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_2_desktop_abTest_widget_3)
                            .setReused(false)
                            .setOrderNumber(1)
                            .setExperimentOptionName(desktop_abTest_option.getName())
                            .isEnabled(true)
                            .build()))
                    .build()))
            .build());
    expectedMobileWidgetsList = List.of(
        new Widget.Builder()
            .using(var_1_mobile_abTest_widget_1)
            .setReused(false)
            .setOrderNumber(1)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_1_mobile_default_widget_2)
                    .setReused(false)
                    .setOrderNumber(1)
                    .setExperimentOptionName(mobile_default_option.getName())
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_1_mobile_default_widget_3)
                            .setReused(false)
                            .setOrderNumber(1)
                            .setExperimentOptionName(mobile_default_option.getName())
                            .build()))
                    .build()))
            .build(),
        new Widget.Builder()
            .using(var_2_mobile_default_widget_1)
            .setReused(false)
            .setOrderNumber(2)
            .setChildren(List.of(
                new Widget.Builder()
                    .using(var_2_mobile_abTest_widget_2)
                    .setReused(false)
                    .setOrderNumber(1)
                    .setExperimentOptionName(mobile_abTest_option.getName())
                    .isEnabled(true)
                    .setChildren(List.of(
                        new Widget.Builder()
                            .using(var_2_mobile_abTest_widget_3)
                            .setReused(false)
                            .setOrderNumber(1)
                            .setExperimentOptionName(mobile_abTest_option.getName())
                            .isEnabled(true)
                            .build()))
                    .build()))
            .build());
  }

  @Test()
  public void experimentActivationPositiveOneTest() {
    // Проверка активации DESKTOP экспермиента
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(actualDesktopExperiment, getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(actualDesktopExperiment, getContentManager())
        .equals(expectedDesktopExperiment);
    // Проверка активации MOBILE экспермиента
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(actualMobileExperiment, getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(actualMobileExperiment, getContentManager())
        .equals(expectedMobileExperiment);
    // Проверка переопределения названий DESKTOP виджетов
    actualDesktopWidgetsList = PAGES_STEPS.getWidgetsList(pageId, desktop, getContentManager());
    IntStream.range(0, expectedDesktopWidgetsList.size()).forEach(i ->
        actualDesktopWidgetsList.get(i).equals(expectedDesktopWidgetsList.get(i)));
    // Проверка переопределения названий MOBILE виджетов
    actualMobileWidgetsList = PAGES_STEPS.getWidgetsList(pageId, mobile, getContentManager());
    IntStream.range(0, expectedMobileWidgetsList.size()).forEach(i ->
        actualMobileWidgetsList.get(i).equals(expectedMobileWidgetsList.get(i)));
  }
}
