package ru.alfabank.platform.rolemodel;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.COMMON_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CREDIT_CARD_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.DEBIT_CARD_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.INVEST_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.MORTGAGE_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.PIL_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.ProductType.SME_PRODUCT_TYPE;
import static ru.alfabank.platform.businessobjects.enums.Team.COMMON_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE_TEAM;
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
import ru.alfabank.platform.businessobjects.enums.Team;
import ru.alfabank.platform.option.OptionBaseTest;
import ru.alfabank.platform.users.AccessibleUser;

public class NegativeRoleModelTest extends OptionBaseTest {

  private static final String[] ERRORS = {"403", "Forbidden", "Access is denied"};

  private final String experimentEndDate = getValidExperimentEndDatePlusWeek();

  @Test(description = "Негативный тест создания экспермента",
      dataProvider = "negativeDataProvider")
  public void negativeCreateExperimentTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
    final var response = EXPERIMENT_STEPS.createExperimentAssumingFail(
        randomAlphanumeric(50),
        randomAlphanumeric(50),
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        user);
    assertThat(response.asString()).contains(ERRORS);
  }

  @Test(description = "Негативный тест чтения экспермента",
      dataProvider = "negativeDataProvider")
  public void negativeReadExperimentTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    EXPERIMENT_STEPS.getExistingExperiment(experiment, user);
  }

  @Test(description = "Негативный тест изменения экспермента",
      dataProvider = "negativeDataProvider")
  public void negativeModifyExperimentTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    final var response = EXPERIMENT_STEPS.modifyExperimentAssumingFail(
        experiment,
        new Experiment.Builder().setTrafficRate(.3D).build(), user);
    assertThat(response.asString()).contains(ERRORS);
  }

  @Test(description = "Негативный тест удаления экспермента",
      dataProvider = "negativeDataProvider")
  public void negativeDeleteExperimentTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    final var response = EXPERIMENT_STEPS.deleteExperiment(experiment, user);
    assertThat(response.asString()).contains(ERRORS);
  }

  @Test(description = "Негативный тест запуска экспермента",
      dataProvider = "negativeDataProvider")
  public void negativeRunExperimentTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        teams,
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
    assertThat(EXPERIMENT_STEPS.runExperimentAssumingFail(experiment, user).asString())
        .contains(ERRORS);
  }

  @Test(description = "Негативный тест остановки экспермента",
      dataProvider = "negativeDataProvider")
  public void negativeStopExperimentTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        teams,
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
    EXPERIMENT_STEPS.runExperimentAssumingSuccess(experiment, getContentManager());
    assertThat(
        EXPERIMENT_STEPS.stopExperimentAssumingFail(experiment, user).asString())
        .contains(ERRORS);
  }

  @Test(description = "Негативный тест создания вариата",
      dataProvider = "negativeDataProvider")
  public void negativeCreateOptionTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
    final var experiment = EXPERIMENT_STEPS.createExperiment(
        desktop,
        page_id,
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    assertThat(OPTION_STEPS.createOptionAssumingFail(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        user).asString())
        .contains(ERRORS);
  }

  @Test(description = "Негативный тест чтения вариата",
      dataProvider = "negativeDataProvider")
  public void negativeReadOptionTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
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
    OPTION_STEPS.getOption(option, user);
  }

  @Test(description = "Негативный тест изменения вариата",
      dataProvider = "negativeDataProvider")
  public void negativeModifyOptionTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(
        teams,
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
    final var response = OPTION_STEPS.modifyOptionAssumingFail(
        option,
        new Builder().setTrafficRate(.4D).build(), user);
    assertThat(response.asString()).contains(ERRORS);
  }

  @Test(description = "Негативный тест удаления вариата",
      dataProvider = "negativeDataProvider")
  public void negativeDeleteOptionTest(
      @ParameterKey("user") AccessibleUser user,
      @ParameterKey("page team") final List<Team> teams,
      @ParameterKey("productType") final ProductType productType) {
    final var page_id = PAGES_STEPS.createPage(teams, getContentManager());
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
    assertThat(OPTION_STEPS.deleteOptionAssumingFail(option, user).asString())
        .contains(ERRORS);
  }

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] negativeDataProvider() {
    return new Object[][]{
        {CREDIT_CARD_USER, List.of(DEBIT_CARD_TEAM), DEBIT_CARD_PRODUCT_TYPE},
        {DEBIT_CARD_USER, List.of(INVEST_TEAM), INVEST_PRODUCT_TYPE},
        {INVEST_USER, List.of(MORTGAGE_TEAM), MORTGAGE_PRODUCT_TYPE},
        {MORTGAGE_USER, List.of(Team.PIL_TEAM), PIL_PRODUCT_TYPE},
        {PIL_USER, List.of(Team.SME_TEAM), SME_PRODUCT_TYPE},
        {SME_USER, List.of(COMMON_TEAM), COMMON_PRODUCT_TYPE},
        {COMMON_USER, List.of(CREDIT_CARD_TEAM), CREDIT_CARD_PRODUCT_TYPE},
        {UNCLAIMED_USER, List.of(CREDIT_CARD_TEAM), CREDIT_CARD_PRODUCT_TYPE}
    };
  }
}
