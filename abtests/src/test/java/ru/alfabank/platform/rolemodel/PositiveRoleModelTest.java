package ru.alfabank.platform.rolemodel;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.COMMON_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CREDIT_CARD_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.DEBIT_CARD_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.INVEST_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.MORTGAGE_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.PIL_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.SME_PRODUCT_TYPE;
import static ru.alfabank.platform.helpers.GeoGroupHelper.RU;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.abtests.Experiment;
import ru.alfabank.platform.businessobjects.abtests.Option.Builder;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.option.OptionBaseTest;
import ru.alfabank.platform.users.AccessibleUser;

public class PositiveRoleModelTest extends OptionBaseTest {

  private final String experimentEndDate = getValidExperimentEndDatePlusWeek();

  @Test(description = "Позитивный тест создания экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveCreateExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        user);
  }

  @Test(
      description = "Позитивный тест чтения экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveReadExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(
        experiment,
        user);
  }

  @Test(
      description = "Позитивный тест изменения экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveModifyExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.modifyExperiment(
        experiment,
        new Experiment.Builder().setTrafficRate(.3D).build(),
        user);
  }

  @Test(
      description = "Позитивный тест удаления экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveDeleteExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.deleteExperiment(
        experiment,
        user);
  }

  @Test(
      description = "Позитивный тест запуска экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveRunExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var widget = DRAFT_STEPS.createWidget(
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
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        user);
  }

  @Test(
      description = "Позитивный тест остановки экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveStopExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var widget = DRAFT_STEPS.createWidget(
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
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        false,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(
        experiment,
        getContentManager());
    EXPERIMENT_STEPS.stopExperimentAssumingSuccess(
        experiment,
        user);
  }

  @Test(
      description = "Позитивный тест создания вариата",
      dataProvider = "positiveDataProvider")
  public void positiveCreateOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    OPTION_STEPS.createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        user);
  }

  @Test(
      description = "Позитивный тест чтения вариата",
      dataProvider = "positiveDataProvider")
  public void positiveReadOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    final var option = OPTION_STEPS.createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.getOption(
        option,
        user);
  }

  @Test(
      description = "Позитивный тест изменения вариата",
      dataProvider = "positiveDataProvider")
  public void positiveModifyOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(
        experiment,
        user);
    final var option = OPTION_STEPS.createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.modifyOption(
        option,
        new Builder().setTrafficRate(.4D).build(),
        user);
  }

  @Test(
      description = "Позитивный тест удаления вариата",
      dataProvider = "positiveDataProvider")
  public void positiveDeleteOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        user.getTeams(),
        getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(experiment, user);
    final var option = OPTION_STEPS.createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    OPTION_STEPS.deleteOption(option, user);
  }

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][]{
        {CREDIT_CARD_USER, CREDIT_CARD_PRODUCT_TYPE},
        {DEBIT_CARD_USER, DEBIT_CARD_PRODUCT_TYPE},
        {INVEST_USER, INVEST_PRODUCT_TYPE},
        {MORTGAGE_USER, MORTGAGE_PRODUCT_TYPE},
        {PIL_USER, PIL_PRODUCT_TYPE},
        {SME_USER, SME_PRODUCT_TYPE},
        {COMMON_USER, COMMON_PRODUCT_TYPE},
        {UNCLAIMED_USER, null}
    };
  }
}
