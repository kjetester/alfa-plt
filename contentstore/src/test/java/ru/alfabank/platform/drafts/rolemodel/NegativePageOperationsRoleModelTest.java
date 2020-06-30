package ru.alfabank.platform.drafts.rolemodel;

import static org.assertj.core.api.Assertions.assertThat;
import static rp.org.apache.http.HttpStatus.SC_FORBIDDEN;
import static ru.alfabank.platform.businessobjects.enums.CopyMethod.CURRENT;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.Team.COMMON;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL;
import static ru.alfabank.platform.businessobjects.enums.Team.SME;
import static ru.alfabank.platform.businessobjects.enums.Team.UNCLAIMED;
import static ru.alfabank.platform.steps.BaseSteps.CREATED_PAGES;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.contentstore.Page;
import ru.alfabank.platform.businessobjects.enums.Team;
import ru.alfabank.platform.steps.cs.PagesSteps;
import ru.alfabank.platform.users.AccessibleUser;

public class NegativePageOperationsRoleModelTest extends RoleModelBaseTest {

  private static final Integer CREDIT_CARD_PAGE_ID =
      PAGES_STEPS.createPage(List.of(CREDIT_CARD), CONTENT_MANAGER);
  private static final Integer DEBIT_CARD_PAGE_ID =
      PAGES_STEPS.createPage(List.of(DEBIT_CARD), CONTENT_MANAGER);
  private static final Integer CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID =
      PAGES_STEPS.createPage(List.of(CREDIT_CARD, DEBIT_CARD), CONTENT_MANAGER);
  private static final Integer INVEST_PAGE_ID =
      PAGES_STEPS.createPage(List.of(INVEST), CONTENT_MANAGER);
  private static final Integer MORTGAGE_PAGE_ID =
      PAGES_STEPS.createPage(List.of(MORTGAGE), CONTENT_MANAGER);
  private static final Integer INVEST_AND_MORTGAGE_PAGE_ID =
      PAGES_STEPS.createPage(List.of(INVEST, MORTGAGE), CONTENT_MANAGER);
  private static final Integer PIL_PAGE_ID = PAGES_STEPS.createPage(List.of(PIL), CONTENT_MANAGER);
  private static final Integer SME_PAGE_ID = PAGES_STEPS.createPage(List.of(SME), CONTENT_MANAGER);
  private static final Integer PIL_AND_SME_PAGE_ID =
      PAGES_STEPS.createPage(List.of(PIL, SME), CONTENT_MANAGER);
  private static final Integer COMMON_PAGE_ID =
      PAGES_STEPS.createPage(List.of(COMMON), CONTENT_MANAGER);
  private static final Integer CREDIT_CARD_AND_COMMON_PAGE_ID =
      PAGES_STEPS.createPage(List.of(CREDIT_CARD, COMMON), CONTENT_MANAGER);
  private static final Integer UNCLAIMED_PAGE_ID = PAGES_STEPS.createPage(null, CONTENT_MANAGER);

  @Test(description = "Негативный тест просмотра страниц CS",
      dataProvider = "negativeReadPageDataProvider")
  public void positiveReadPageRoleModelTest(final AccessibleUser user) {
    final var softly = new SoftAssertions();
    PAGES_STEPS.getPagesList(getContentManager()).stream()
        .filter(PagesSteps.distinctByKey(Page::getTeams)).forEach(page -> {
          softly.assertThat(PAGES_STEPS.getPage(page.getId(), desktop, user).getStatusCode())
              .isEqualTo(SC_FORBIDDEN);
          softly.assertThat(PAGES_STEPS.getPage(page.getId(), mobile, user).getStatusCode())
              .isEqualTo(SC_FORBIDDEN);
        });
    softly.assertAll();
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "negativeReadPageDataProvider")
  public static Object[][] negativeReadPageDataProvider() {
    return new Object[][]{
        {AUDIT_VIEW_USER},
        {AUDIT_ROLLBACK_USER}
    };
  }

  @Test(description = "Негативный тест создания страницы в CS",
      dataProvider = "negativeCreatePageDataProvider")
  public void negativeCreatePageRoleModelTest(final List<Team> team,
                                              final AccessibleUser user) {
    assertThat(PAGES_STEPS.createPageAndGetResponse(team, user).getStatusCode())
        .isEqualTo(SC_FORBIDDEN);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "negativeCreatePageDataProvider")
  public static Object[][] negativeCreatePageDataProvider() {
    return new Object[][]{
        {List.of(DEBIT_CARD), CREDIT_CARD_USER},
        {List.of(INVEST), CREDIT_CARD_USER},
        {List.of(MORTGAGE), CREDIT_CARD_USER},
        {List.of(PIL), CREDIT_CARD_USER},
        {List.of(SME), CREDIT_CARD_USER},
        {List.of(COMMON), CREDIT_CARD_USER},
        {UNCLAIMED.getId(), CREDIT_CARD_USER},

        {List.of(CREDIT_CARD), DEBIT_CARD_USER},
        {List.of(INVEST), DEBIT_CARD_USER},
        {List.of(MORTGAGE), DEBIT_CARD_USER},
        {List.of(PIL), DEBIT_CARD_USER},
        {List.of(SME), DEBIT_CARD_USER},
        {List.of(COMMON), DEBIT_CARD_USER},
        {UNCLAIMED.getId(), DEBIT_CARD_USER},

        {List.of(CREDIT_CARD), INVEST_USER},
        {List.of(DEBIT_CARD), INVEST_USER},
        {List.of(MORTGAGE), INVEST_USER},
        {List.of(PIL), INVEST_USER},
        {List.of(SME), INVEST_USER},
        {List.of(COMMON), INVEST_USER},
        {UNCLAIMED.getId(), INVEST_USER},

        {List.of(CREDIT_CARD), MORTGAGE_USER},
        {List.of(DEBIT_CARD), MORTGAGE_USER},
        {List.of(INVEST), MORTGAGE_USER},
        {List.of(PIL), MORTGAGE_USER},
        {List.of(SME), MORTGAGE_USER},
        {List.of(COMMON), MORTGAGE_USER},
        {UNCLAIMED.getId(), MORTGAGE_USER},

        {List.of(CREDIT_CARD), PIL_USER},
        {List.of(DEBIT_CARD), PIL_USER},
        {List.of(INVEST), PIL_USER},
        {List.of(MORTGAGE), PIL_USER},
        {List.of(SME), PIL_USER},
        {List.of(COMMON), PIL_USER},
        {UNCLAIMED.getId(), PIL_USER},

        {List.of(CREDIT_CARD), SME_USER},
        {List.of(DEBIT_CARD), SME_USER},
        {List.of(INVEST), SME_USER},
        {List.of(MORTGAGE), SME_USER},
        {List.of(PIL), SME_USER},
        {List.of(COMMON), SME_USER},
        {UNCLAIMED.getId(), SME_USER},

        {List.of(CREDIT_CARD), COMMON_USER},
        {List.of(DEBIT_CARD), COMMON_USER},
        {List.of(INVEST), COMMON_USER},
        {List.of(MORTGAGE), COMMON_USER},
        {List.of(PIL), COMMON_USER},
        {List.of(SME), COMMON_USER},
        {UNCLAIMED.getId(), COMMON_USER},

        {List.of(CREDIT_CARD), UNCLAIMED_USER},
        {List.of(DEBIT_CARD), UNCLAIMED_USER},
        {List.of(INVEST), UNCLAIMED_USER},
        {List.of(MORTGAGE), UNCLAIMED_USER},
        {List.of(PIL), UNCLAIMED_USER},
        {List.of(SME), UNCLAIMED_USER},
        {List.of(COMMON), UNCLAIMED_USER},

        {List.of(CREDIT_CARD, DEBIT_CARD, INVEST, MORTGAGE, PIL, SME, COMMON), AUDIT_VIEW_USER},
        {UNCLAIMED.getId(), AUDIT_ROLLBACK_USER},
    };
  }

  @Test(description = "Негативный тест изменения страницы в CS",
      dataProvider = "negativeModifyPageDataProvider")
  public void negativeModifyPageRoleModelTest(final Integer pageId,
                                              final Page pageModification,
                                              final AccessibleUser user) {
    assertThat(PAGES_STEPS.modifyPage(pageId, pageModification, user).getStatusCode())
        .isEqualTo(SC_FORBIDDEN);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "negativeModifyPageDataProvider")
  public static Object[][] negativeModifyPageDataProvider() {
    final Integer unPageId = PAGES_STEPS.createPage(null, CONTENT_MANAGER);
    return new Object[][]{
        // CREDIT_CARD_USER
        {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
        CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID,
        new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID))
            .setTeamsList(List.of(CREDIT_CARD)).build(),
        CREDIT_CARD_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            CREDIT_CARD_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            CREDIT_CARD_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            CREDIT_CARD_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            CREDIT_CARD_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            CREDIT_CARD_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            CREDIT_CARD_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            CREDIT_CARD_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            CREDIT_CARD_USER
        },
        // DEBIT_CARD_USER
        {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
        CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID,
        new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID))
            .setTeamsList(List.of(DEBIT_CARD, INVEST)).build(),
        DEBIT_CARD_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            DEBIT_CARD_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            DEBIT_CARD_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            DEBIT_CARD_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            DEBIT_CARD_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            DEBIT_CARD_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            DEBIT_CARD_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            DEBIT_CARD_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            DEBIT_CARD_USER
        },
        // INVEST_USER
        {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            INVEST_USER
        }, {
            INVEST_AND_MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_AND_MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD)).build(),
            INVEST_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            INVEST_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            INVEST_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            INVEST_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            INVEST_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            INVEST_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            INVEST_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(INVEST)).build(),
            INVEST_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            INVEST_USER
        },
        // MORTGAGE_USER
        {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            MORTGAGE_USER
        }, {
            INVEST_AND_MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_AND_MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE, SME)).build(),
            MORTGAGE_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            MORTGAGE_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            MORTGAGE_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            MORTGAGE_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            MORTGAGE_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            MORTGAGE_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            MORTGAGE_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(MORTGAGE)).build(),
            MORTGAGE_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            MORTGAGE_USER
        },
        // PIL_USER
        {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            PIL_USER
        }, {
            PIL_AND_SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_AND_SME_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            PIL_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            PIL_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            PIL_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            PIL_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            PIL_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            PIL_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(PIL)).build(),
            PIL_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            PIL_USER
        },
        // SME_USER
        {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            SME_USER
        }, {
            PIL_AND_SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_AND_SME_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD, SME)).build(),
            SME_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            SME_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            SME_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            SME_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            SME_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            SME_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            SME_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(SME)).build(),
            SME_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            SME_USER
        },
        // COMMON_USER
        {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            COMMON_USER
        }, {
            CREDIT_CARD_AND_COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_AND_COMMON_PAGE_ID))
                .setTeamsList(List.of(DEBIT_CARD, COMMON)).build(),
            COMMON_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            COMMON_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            COMMON_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            COMMON_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            COMMON_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            COMMON_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            COMMON_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(COMMON)).build(),
            COMMON_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            COMMON_USER
        },
        // UNCLAIMED_USER
        {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(CREDIT_CARD)).build(),
            UNCLAIMED_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            CREDIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(CREDIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            DEBIT_CARD_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(DEBIT_CARD_PAGE_ID))
                .setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            INVEST_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(INVEST_PAGE_ID)).setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            MORTGAGE_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(MORTGAGE_PAGE_ID)).setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            PIL_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(PIL_PAGE_ID)).setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            SME_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(SME_PAGE_ID)).setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID))
                .setTeamsList(List.of(UNCLAIMED)).build(),
            UNCLAIMED_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            UNCLAIMED_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            AUDIT_VIEW_USER
        }, {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId))
                .setTeamsList(List.of(COMMON)).build(),
            AUDIT_VIEW_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setEnable(false).build(),
            AUDIT_ROLLBACK_USER
        }, {
            COMMON_PAGE_ID,
            new Page.Builder().using(CREATED_PAGES.get(COMMON_PAGE_ID)).setTeamsList(null).build(),
            AUDIT_ROLLBACK_USER
        }
    };
  }

  @Test(description = "Негативный тест копирования страницы в CS",
      dataProvider = "negativeCopyPageDataProvider")
  public void negativeCopyPageRoleModelTest(final Integer pageId,
                                            final AccessibleUser user) {
    assertThat(PAGES_STEPS.copyPage(pageId, CURRENT, user).getStatusCode()).isEqualTo(SC_FORBIDDEN);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "negativeCopyPageDataProvider")
  public static Object[][] negativeCopyPageDataProvider() {
    return new Object[][]{
        // CREDIT_CARD_USER
        {CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID, CREDIT_CARD_USER},
        {DEBIT_CARD_PAGE_ID, CREDIT_CARD_USER},
        {DEBIT_CARD_PAGE_ID, CREDIT_CARD_USER},
        {INVEST_PAGE_ID, CREDIT_CARD_USER},
        {MORTGAGE_PAGE_ID, CREDIT_CARD_USER},
        {PIL_PAGE_ID, CREDIT_CARD_USER},
        {SME_PAGE_ID, CREDIT_CARD_USER},
        {COMMON_PAGE_ID, CREDIT_CARD_USER},
        {UNCLAIMED_PAGE_ID, CREDIT_CARD_USER},
        // DEBIT_CARD_USER
        {CREDIT_CARD_AND_DEBIT_CARD_PAGE_ID, DEBIT_CARD_USER},
        {CREDIT_CARD_PAGE_ID, DEBIT_CARD_USER},
        {INVEST_PAGE_ID, DEBIT_CARD_USER},
        {MORTGAGE_PAGE_ID, DEBIT_CARD_USER},
        {PIL_PAGE_ID, DEBIT_CARD_USER},
        {SME_PAGE_ID, DEBIT_CARD_USER},
        {COMMON_PAGE_ID, DEBIT_CARD_USER},
        {UNCLAIMED_PAGE_ID, DEBIT_CARD_USER},
        // INVEST_USER
        {INVEST_AND_MORTGAGE_PAGE_ID, INVEST_USER},
        {CREDIT_CARD_PAGE_ID, INVEST_USER},
        {DEBIT_CARD_PAGE_ID, INVEST_USER},
        {MORTGAGE_PAGE_ID, INVEST_USER},
        {PIL_PAGE_ID, INVEST_USER},
        {SME_PAGE_ID, INVEST_USER},
        {COMMON_PAGE_ID, INVEST_USER},
        {UNCLAIMED_PAGE_ID, INVEST_USER},
        // MORTGAGE_USER
        {INVEST_AND_MORTGAGE_PAGE_ID, MORTGAGE_USER},
        {CREDIT_CARD_PAGE_ID, MORTGAGE_USER},
        {DEBIT_CARD_PAGE_ID, MORTGAGE_USER},
        {INVEST_PAGE_ID, MORTGAGE_USER},
        {PIL_PAGE_ID, MORTGAGE_USER},
        {SME_PAGE_ID, MORTGAGE_USER},
        {COMMON_PAGE_ID, MORTGAGE_USER},
        {UNCLAIMED_PAGE_ID, MORTGAGE_USER},
        // PIL_USER
        {PIL_AND_SME_PAGE_ID, PIL_USER},
        {CREDIT_CARD_PAGE_ID, PIL_USER},
        {DEBIT_CARD_PAGE_ID, PIL_USER},
        {INVEST_PAGE_ID, PIL_USER},
        {MORTGAGE_PAGE_ID, PIL_USER},
        {SME_PAGE_ID, PIL_USER},
        {COMMON_PAGE_ID, PIL_USER},
        {UNCLAIMED_PAGE_ID, PIL_USER},
        // SME_USER
        {PIL_AND_SME_PAGE_ID, SME_USER},
        {CREDIT_CARD_PAGE_ID, SME_USER},
        {DEBIT_CARD_PAGE_ID, SME_USER},
        {INVEST_PAGE_ID, SME_USER},
        {MORTGAGE_PAGE_ID, SME_USER},
        {PIL_PAGE_ID, SME_USER},
        {COMMON_PAGE_ID, SME_USER},
        {UNCLAIMED_PAGE_ID, SME_USER},
        // COMMON_USER
        {CREDIT_CARD_AND_COMMON_PAGE_ID, COMMON_USER},
        {CREDIT_CARD_PAGE_ID, COMMON_USER},
        {DEBIT_CARD_PAGE_ID, COMMON_USER},
        {INVEST_PAGE_ID, COMMON_USER},
        {MORTGAGE_PAGE_ID, COMMON_USER},
        {PIL_PAGE_ID, COMMON_USER},
        {SME_PAGE_ID, COMMON_USER},
        {UNCLAIMED_PAGE_ID, COMMON_USER},
        // UNCLAIMED_USER
        {CREDIT_CARD_PAGE_ID, UNCLAIMED_USER},
        {DEBIT_CARD_PAGE_ID, UNCLAIMED_USER},
        {INVEST_PAGE_ID, UNCLAIMED_USER},
        {MORTGAGE_PAGE_ID, UNCLAIMED_USER},
        {PIL_PAGE_ID, UNCLAIMED_USER},
        {SME_PAGE_ID, UNCLAIMED_USER},
        {COMMON_PAGE_ID, UNCLAIMED_USER}
    };
  }

  @Test(description = "Негативный тест удаления страницы в CS",
      dataProvider = "negativeDeletePageDataProvider")
  public void negativeDeletePageRoleModelTest(final Integer pageId,
                                              final AccessibleUser user) {
    assertThat(PAGES_STEPS.deletePage(pageId, user).getStatusCode()).isEqualTo(SC_FORBIDDEN);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "negativeDeletePageDataProvider")
  public static Object[][] negativeDeletePageDataProvider() {
    return new Object[][]{
        // CREDIT_CARD_USER
        {DEBIT_CARD_PAGE_ID, CREDIT_CARD_USER},
        {DEBIT_CARD_PAGE_ID, CREDIT_CARD_USER},
        {INVEST_PAGE_ID, CREDIT_CARD_USER},
        {MORTGAGE_PAGE_ID, CREDIT_CARD_USER},
        {PIL_PAGE_ID, CREDIT_CARD_USER},
        {SME_PAGE_ID, CREDIT_CARD_USER},
        {COMMON_PAGE_ID, CREDIT_CARD_USER},
        {UNCLAIMED_PAGE_ID, CREDIT_CARD_USER},
        // DEBIT_CARD_USER
        {CREDIT_CARD_PAGE_ID, DEBIT_CARD_USER},
        {INVEST_PAGE_ID, DEBIT_CARD_USER},
        {MORTGAGE_PAGE_ID, DEBIT_CARD_USER},
        {PIL_PAGE_ID, DEBIT_CARD_USER},
        {SME_PAGE_ID, DEBIT_CARD_USER},
        {COMMON_PAGE_ID, DEBIT_CARD_USER},
        {UNCLAIMED_PAGE_ID, DEBIT_CARD_USER},
        // INVEST_USER
        {CREDIT_CARD_PAGE_ID, INVEST_USER},
        {DEBIT_CARD_PAGE_ID, INVEST_USER},
        {MORTGAGE_PAGE_ID, INVEST_USER},
        {PIL_PAGE_ID, INVEST_USER},
        {SME_PAGE_ID, INVEST_USER},
        {COMMON_PAGE_ID, INVEST_USER},
        {UNCLAIMED_PAGE_ID, INVEST_USER},
        // MORTGAGE_USER
        {CREDIT_CARD_PAGE_ID, MORTGAGE_USER},
        {DEBIT_CARD_PAGE_ID, MORTGAGE_USER},
        {INVEST_PAGE_ID, MORTGAGE_USER},
        {PIL_PAGE_ID, MORTGAGE_USER},
        {SME_PAGE_ID, MORTGAGE_USER},
        {COMMON_PAGE_ID, MORTGAGE_USER},
        {UNCLAIMED_PAGE_ID, MORTGAGE_USER},
        // PIL_USER
        {CREDIT_CARD_PAGE_ID, PIL_USER},
        {DEBIT_CARD_PAGE_ID, PIL_USER},
        {INVEST_PAGE_ID, PIL_USER},
        {MORTGAGE_PAGE_ID, PIL_USER},
        {SME_PAGE_ID, PIL_USER},
        {COMMON_PAGE_ID, PIL_USER},
        {UNCLAIMED_PAGE_ID, PIL_USER},
        // SME_USER
        {CREDIT_CARD_PAGE_ID, SME_USER},
        {DEBIT_CARD_PAGE_ID, SME_USER},
        {INVEST_PAGE_ID, SME_USER},
        {MORTGAGE_PAGE_ID, SME_USER},
        {PIL_PAGE_ID, SME_USER},
        {COMMON_PAGE_ID, SME_USER},
        {UNCLAIMED_PAGE_ID, SME_USER},
        // COMMON_USER
        {CREDIT_CARD_PAGE_ID, COMMON_USER},
        {DEBIT_CARD_PAGE_ID, COMMON_USER},
        {INVEST_PAGE_ID, COMMON_USER},
        {MORTGAGE_PAGE_ID, COMMON_USER},
        {PIL_PAGE_ID, COMMON_USER},
        {SME_PAGE_ID, COMMON_USER},
        {UNCLAIMED_PAGE_ID, COMMON_USER},
        // UNCLAIMED_USER
        {CREDIT_CARD_PAGE_ID, UNCLAIMED_USER},
        {DEBIT_CARD_PAGE_ID, UNCLAIMED_USER},
        {INVEST_PAGE_ID, UNCLAIMED_USER},
        {MORTGAGE_PAGE_ID, UNCLAIMED_USER},
        {PIL_PAGE_ID, UNCLAIMED_USER},
        {SME_PAGE_ID, UNCLAIMED_USER},
        {COMMON_PAGE_ID, UNCLAIMED_USER}
    };
  }
}
