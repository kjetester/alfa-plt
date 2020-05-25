package ru.alfabank.platform.rolemodel;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.Geo.RU;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.COM;
import static ru.alfabank.platform.businessobjects.enums.ProductType.DC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.INV;
import static ru.alfabank.platform.businessobjects.enums.ProductType.MG;
import static ru.alfabank.platform.businessobjects.enums.ProductType.PIL;
import static ru.alfabank.platform.businessobjects.enums.ProductType.SME;
import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.CommonUser.getCommonUser;
import static ru.alfabank.platform.users.ContentManager.getContentManager;
import static ru.alfabank.platform.users.CreditCardUser.getCreditCardUser;
import static ru.alfabank.platform.users.DebitCardUser.getDebitCardUser;
import static ru.alfabank.platform.users.InvestUser.getInvestUser;
import static ru.alfabank.platform.users.MortgageUser.getMortgageUser;
import static ru.alfabank.platform.users.PilUser.getPilUser;
import static ru.alfabank.platform.users.SmeUser.getSmeUser;
import static ru.alfabank.platform.users.UnclaimedUser.getUnclaimedUser;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option.Builder;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.businessobjects.enums.Team;
import ru.alfabank.platform.option.OptionBaseTest;
import ru.alfabank.platform.users.AccessibleUser;
import ru.alfabank.platform.users.CommonUser;
import ru.alfabank.platform.users.CreditCardUser;
import ru.alfabank.platform.users.DebitCardUser;
import ru.alfabank.platform.users.InvestUser;
import ru.alfabank.platform.users.MortgageUser;
import ru.alfabank.platform.users.PilUser;
import ru.alfabank.platform.users.SmeUser;
import ru.alfabank.platform.users.UnclaimedUser;

public class NegativeRoleModelTest extends OptionBaseTest {

  private static final String[] ERRORS = {"403", "Forbidden", "Access is denied"};
  private static final CreditCardUser CREDIT_CARD_USER = getCreditCardUser();
  private static final DebitCardUser DEBIT_CARD_USER = getDebitCardUser();
  private static final InvestUser INVEST_USER = getInvestUser();
  private static final MortgageUser MORTGAGE_USER = getMortgageUser();
  private static final PilUser PIL_USER = getPilUser();
  private static final SmeUser SME_USER = getSmeUser();
  private static final CommonUser COMMON_USER = getCommonUser();
  private static final UnclaimedUser UNCLAIMED_USER = getUnclaimedUser();

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
        {CREDIT_CARD_USER, List.of(DEBIT_CARD), DC},
        {DEBIT_CARD_USER, List.of(INVEST), INV},
        {INVEST_USER, List.of(MORTGAGE), MG},
        {MORTGAGE_USER, List.of(Team.PIL), PIL},
        {PIL_USER, List.of(Team.SME), SME},
        {SME_USER, List.of(COMMON), COM},
        {COMMON_USER, List.of(CREDIT_CARD), CC},
        {UNCLAIMED_USER, List.of(CREDIT_CARD), CC}
    };
  }
}
