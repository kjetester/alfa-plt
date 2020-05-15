package ru.alfabank.platform.rolemodel;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
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
import static ru.alfabank.platform.businessobjects.enums.User.COMMON_USER;
import static ru.alfabank.platform.businessobjects.enums.User.CONTENT_MANAGER;
import static ru.alfabank.platform.businessobjects.enums.User.CREDIT_CARD_USER;
import static ru.alfabank.platform.businessobjects.enums.User.DEBIT_CARD_USER;
import static ru.alfabank.platform.businessobjects.enums.User.INVEST_USER;
import static ru.alfabank.platform.businessobjects.enums.User.MORTGAGE_USER;
import static ru.alfabank.platform.businessobjects.enums.User.PIL_USER;
import static ru.alfabank.platform.businessobjects.enums.User.SME_USER;
import static ru.alfabank.platform.businessobjects.enums.User.UNCLAIMED_USER;
import static ru.alfabank.platform.helpers.KeycloakHelper.logout;

import com.epam.reportportal.annotations.ParameterKey;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.Experiment;
import ru.alfabank.platform.businessobjects.Option.Builder;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.businessobjects.enums.Team;
import ru.alfabank.platform.businessobjects.enums.User;
import ru.alfabank.platform.option.OptionBaseTest;

public class RoleModelTest extends OptionBaseTest {

  private static final String[] ERRORS = {"403", "Forbidden", "Access is denied"};

  @Test(description = "Позитивный тест",
        dataProvider = "positiveDataProvider")
  public void positiveTest(@ParameterKey("user") final User user,
                                   @ParameterKey("productType") final ProductType productType) {
    setUser(user);
    final var experimentEnd = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(user.getTeams());
    final var pageId = page.getId();
    final var experiment = createExperiment(desktop, pageId, productType, experimentEnd, .5D);
    getExperiment(experiment);
    final var option = createOption(true, List.of(), experiment.getUuid(), .5D);
    getOption(option);
    modifyOption(option, new Builder().setTrafficRate(.4D).build());
    deleteOption(option);
    modifyExperiment(experiment, new Experiment.Builder().setTrafficRate(.3D).build());
    deleteExperiment(experiment);
  }

  /**
   * Data provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][] {
        {CREDIT_CARD_USER, CC},
        {DEBIT_CARD_USER, DC},
        {INVEST_USER, INV},
        {MORTGAGE_USER, MG},
        {PIL_USER, PIL},
        {SME_USER, SME},
        {COMMON_USER, COM}
    };
  }

  @Test(description = "Негативный тест",
        dataProvider = "negativeDataProvider")
  public void negativeTest(@ParameterKey("user") final User user,
                           @ParameterKey("page team") final List<Team> teams,
                           @ParameterKey("productType") final ProductType productType) {
    setUser(CONTENT_MANAGER);
    final var softly = new SoftAssertions();
    final var experimentEnd = getCurrentDateTime().plusDays(5).toString();
    var page = createPage(teams);
    final var pageId = page.getId();
    logout(CONTENT_MANAGER);
    setUser(user);
    softly
        .assertThat(createExperimentAssumingFail(
            randomAlphanumeric(50),
            randomAlphanumeric(50),
            desktop,
            pageId,
            productType,
            experimentEnd,
            .5D).asString())
        .as("")
        .contains(ERRORS);
    logout(user);
    setUser(CONTENT_MANAGER);
    final var experiment = createExperiment(mobile, pageId, productType, experimentEnd, .5D);
    logout(CONTENT_MANAGER);
    setUser(user);
    getExperiment(experiment);
    softly
        .assertThat(createOptionAssumingFail(true, List.of(), experiment.getUuid(), .5D).asString())
        .as("")
        .contains(ERRORS);
    logout(user);
    setUser(CONTENT_MANAGER);
    final var option = createOption(false, List.of(), experiment.getUuid(), .5D);
    logout(CONTENT_MANAGER);
    setUser(user);
    getOption(option);
    softly
        .assertThat(
            modifyOptionAssumingFail(option, new Builder().setTrafficRate(.4D).build()).asString())
        .as("")
        .contains(ERRORS);
    softly
        .assertThat(deleteOptionAssumingFail(option).asString())
        .as("")
        .contains(ERRORS);
    softly
        .assertThat(
            modifyExperimentAssumingFail(
                experiment, new Experiment.Builder().setTrafficRate(.3D).build())
                .asString())
        .as("")
        .contains(ERRORS);
    softly
        .assertThat(deleteExperiment(experiment).asString())
        .as("")
        .contains(ERRORS);
    logout(user);
    softly.assertAll();
  }

  /**
   * Data provider.
   * @return test data
   */
  @DataProvider
  public static Object[][] negativeDataProvider() {
    return new Object[][] {
        {CREDIT_CARD_USER,  List.of(DEBIT_CARD),   DC},
        {CREDIT_CARD_USER,  List.of(INVEST),       INV},
        {CREDIT_CARD_USER,  List.of(MORTGAGE),     MG},
        {CREDIT_CARD_USER,  List.of(Team.PIL),     PIL},
        {CREDIT_CARD_USER,  List.of(Team.SME),     SME},
        {CREDIT_CARD_USER,  List.of(COMMON),       COM},
        {DEBIT_CARD_USER,   List.of(INVEST),       INV},
        {DEBIT_CARD_USER,   List.of(MORTGAGE),     MG},
        {DEBIT_CARD_USER,   List.of(Team.PIL),     PIL},
        {DEBIT_CARD_USER,   List.of(Team.SME),     SME},
        {DEBIT_CARD_USER,   List.of(COMMON),       COM},
        {DEBIT_CARD_USER,   List.of(CREDIT_CARD),  CC},
        {INVEST_USER,       List.of(MORTGAGE),     MG},
        {INVEST_USER,       List.of(Team.PIL),     PIL},
        {INVEST_USER,       List.of(Team.SME),     SME},
        {INVEST_USER,       List.of(COMMON),       COM},
        {INVEST_USER,       List.of(CREDIT_CARD),  CC},
        {INVEST_USER,       List.of(DEBIT_CARD),   DC},
        {MORTGAGE_USER,     List.of(Team.PIL),     PIL},
        {MORTGAGE_USER,     List.of(Team.SME),     SME},
        {MORTGAGE_USER,     List.of(COMMON),       COM},
        {MORTGAGE_USER,     List.of(CREDIT_CARD),  CC},
        {MORTGAGE_USER,     List.of(DEBIT_CARD),   DC},
        {MORTGAGE_USER,     List.of(INVEST),       INV},
        {PIL_USER,          List.of(Team.SME),     SME},
        {PIL_USER,          List.of(COMMON),       COM},
        {PIL_USER,          List.of(CREDIT_CARD),  CC},
        {PIL_USER,          List.of(DEBIT_CARD),   DC},
        {PIL_USER,          List.of(INVEST),       INV},
        {PIL_USER,          List.of(MORTGAGE),     MG},
        {SME_USER,          List.of(COMMON),       COM},
        {SME_USER,          List.of(CREDIT_CARD),  CC},
        {SME_USER,          List.of(DEBIT_CARD),   DC},
        {SME_USER,          List.of(INVEST),       INV},
        {SME_USER,          List.of(MORTGAGE),     MG},
        {SME_USER,          List.of(Team.PIL),     PIL},
        {COMMON_USER,       List.of(CREDIT_CARD),  CC},
        {COMMON_USER,       List.of(DEBIT_CARD),   DC},
        {COMMON_USER,       List.of(INVEST),       INV},
        {COMMON_USER,       List.of(MORTGAGE),     MG},
        {COMMON_USER,       List.of(Team.PIL),     PIL},
        {COMMON_USER,       List.of(Team.SME),     SME},
        {UNCLAIMED_USER,    List.of(CREDIT_CARD),  CC},
        {UNCLAIMED_USER,    List.of(DEBIT_CARD),   DC},
        {UNCLAIMED_USER,    List.of(INVEST),       INV},
        {UNCLAIMED_USER,    List.of(MORTGAGE),     MG},
        {UNCLAIMED_USER,    List.of(Team.PIL),     PIL},
        {UNCLAIMED_USER,    List.of(Team.SME),     SME},
        {UNCLAIMED_USER,    List.of(COMMON),       COM}
    };
  }
}
