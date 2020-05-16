package ru.alfabank.platform.rolemodel;

import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.DEFAULT;
import static ru.alfabank.platform.businessobjects.enums.ProductType.CC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.COM;
import static ru.alfabank.platform.businessobjects.enums.ProductType.DC;
import static ru.alfabank.platform.businessobjects.enums.ProductType.INV;
import static ru.alfabank.platform.businessobjects.enums.ProductType.MG;
import static ru.alfabank.platform.businessobjects.enums.ProductType.PIL;
import static ru.alfabank.platform.businessobjects.enums.ProductType.SME;
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

public class PositiveRoleModelTest extends OptionBaseTest {

  private static final CreditCardUser CREDIT_CARD_USER = getCreditCardUser();
  private static final DebitCardUser DEBIT_CARD_USER = getDebitCardUser();
  private static final InvestUser INVEST_USER = getInvestUser();
  private static final MortgageUser MORTGAGE_USER = getMortgageUser();
  private static final PilUser PIL_USER = getPilUser();
  private static final SmeUser SME_USER = getSmeUser();
  private static final CommonUser COMMON_USER = getCommonUser();
  private static final UnclaimedUser UNCLAIMED_USER = getUnclaimedUser();

  private final String experimentEndDate = getCurrentDateTime().plusDays(5).toString();

  @Test(description = "Позитивный тест создания экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveCreateExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    createExperiment(desktop, page.getId(), productType, experimentEndDate, .5D, user);
  }

  @Test(
      description = "Позитивный тест чтения экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveReadExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    getExperiment(experiment, user);
  }

  @Test(
      description = "Позитивный тест изменения экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveModifyExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    modifyExperiment(experiment, new Experiment.Builder().setTrafficRate(.3D).build(), user);
  }

  @Test(
      description = "Позитивный тест удаления экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveDeleteExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    deleteExperiment(experiment, user);
  }

  @Test(
      description = "Позитивный тест запуска экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveRunExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var widget = createWidget(
        page,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    createOption(
        true,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    createOption(
        false,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    runExperimentAssumingSuccess(experiment, user);
  }

  @Test(
      description = "Позитивный тест остановки экспермента",
      dataProvider = "positiveDataProvider")
  public void positiveStopExperimentTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var widget = createWidget(
        page,
        null,
        desktop,
        true,
        DEFAULT,
        true,
        null,
        null,
        getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    createOption(
        true,
        List.of(widget.getUid()),
        experiment.getUuid(),
        .5D,
        getContentManager());
    createOption(
        false,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    runExperimentAssumingSuccess(experiment, getContentManager());
    stopExperimentAssumingSuccess(experiment, user);
  }
  
  @Test(
      description = "Позитивный тест создания вариата",
      dataProvider = "positiveDataProvider")
  public void positiveCreateOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    createOption(
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
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    final var option = createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    getOption(option, user);
  }

  @Test(
      description = "Позитивный тест изменения вариата",
      dataProvider = "positiveDataProvider")
  public void positiveModifyOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    getExperiment(experiment, user);
    final var option = createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    modifyOption(option, new Builder().setTrafficRate(.4D).build(), user);
  }

  @Test(
      description = "Позитивный тест удаления вариата",
      dataProvider = "positiveDataProvider")
  public void positiveDeleteOptionTest(
      @ParameterKey("user") final AccessibleUser user,
      @ParameterKey("productType") final ProductType productType) {
    var page = createPage(user.getTeams(), getContentManager());
    final var experiment = createExperiment(
        desktop,
        page.getId(),
        productType,
        experimentEndDate,
        .5D,
        getContentManager());
    getExperiment(experiment, user);
    final var option = createOption(
        true,
        List.of(),
        experiment.getUuid(),
        .5D,
        getContentManager());
    deleteOption(option, user);
  }
  
  /**
   * Data provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][]{
        {CREDIT_CARD_USER, DC},
        {DEBIT_CARD_USER, CC},
        {INVEST_USER, INV},
        {MORTGAGE_USER, MG},
        {PIL_USER, PIL},
        {SME_USER, SME},
        {COMMON_USER, COM},
        {UNCLAIMED_USER, DC}
    };
  }
}
