package ru.alfabank.platform.drafts.rolemodel;

import static org.assertj.core.api.Assertions.assertThat;
import static rp.org.apache.http.HttpStatus.SC_OK;
import static ru.alfabank.platform.businessobjects.enums.CopyMethod.CURRENT;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.Device.mobile;
import static ru.alfabank.platform.businessobjects.enums.Team.COMMON_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.CREDIT_CARD_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.DEBIT_CARD_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.INVEST_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.MORTGAGE_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.PIL_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.SME_TEAM;
import static ru.alfabank.platform.businessobjects.enums.Team.UNCLAIMED_TEAM;
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

public class PositivePageOperationsRoleModelTest extends RoleModelBaseTest {

  @Test(description = "Позитивный тест просмотра страниц CS",
      dataProvider = "positiveReadPageDataProvider")
  public void positiveReadPageRoleModelTest(final AccessibleUser user) {
    final var softly = new SoftAssertions();
    PAGES_STEPS.getPagesList(getContentManager()).stream()
        .filter(PagesSteps.distinctByKey(Page::getTeams)).forEach(page -> {
          softly.assertThat(PAGES_STEPS.getPage(page.getId(), desktop, user).getStatusCode())
              .isEqualTo(SC_OK);
          softly.assertThat(PAGES_STEPS.getPage(page.getId(), mobile, user).getStatusCode())
              .isEqualTo(SC_OK);
        });
    softly.assertAll();
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "positiveReadPageDataProvider")
  public static Object[][] positiveReadPageDataProvider() {
    return new Object[][]{
        {CREDIT_CARD_USER},
        {DEBIT_CARD_USER},
        {INVEST_USER},
        {MORTGAGE_USER},
        {PIL_USER},
        {SME_USER},
        {COMMON_USER},
        {UNCLAIMED_USER}
    };
  }

  @Test(description = "Позитивный тест создания страницы в CS",
      dataProvider = "positiveCreatePageDataProvider")
  public void positiveCreatePageRoleModelTest(final List<Team> team,
                                              final AccessibleUser user) {
    assertThat(PAGES_STEPS.createPageAndGetResponse(team, user).getStatusCode()).isEqualTo(SC_OK);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "positiveCreatePageDataProvider")
  public static Object[][] positiveCreatePageDataProvider() {
    return new Object[][]{
        {
            List.of(
                CREDIT_CARD_TEAM,
                DEBIT_CARD_TEAM,
                INVEST_TEAM,
                MORTGAGE_TEAM,
                PIL_TEAM,
                SME_TEAM,
                COMMON_TEAM),
            CONTENT_MANAGER
        },
        {
            List.of(CREDIT_CARD_TEAM),
            CREDIT_CARD_USER
        },
        {
           List.of(DEBIT_CARD_TEAM),
            DEBIT_CARD_USER
        },
        {
           List.of(INVEST_TEAM),
            INVEST_USER
        },
        {
           List.of(MORTGAGE_TEAM),
            MORTGAGE_USER
        },
        {
           List.of(PIL_TEAM),
            PIL_USER
        },
        {
           List.of(SME_TEAM),
            SME_USER
        },
        {
            List.of(COMMON_TEAM),
            COMMON_USER
        },
        {
            UNCLAIMED_TEAM.getId(),
            UNCLAIMED_USER
        }
    };
  }

  @Test(description = "Позитивный тест изменения страницы в CS",
      dataProvider = "positiveModifyPageDataProvider")
  public void positiveModifyPageRoleModelTest(final Integer pageId,
                                              final Page pageModification,
                                              final AccessibleUser user) {
    assertThat(PAGES_STEPS.modifyPage(pageId, pageModification, user).getStatusCode())
        .isEqualTo(SC_OK);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "positiveModifyPageDataProvider")
  public static Object[][] positiveModifyPageDataProvider() {
    final Integer ccPageId = PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM), CONTENT_MANAGER);
    final Integer dcPageId = PAGES_STEPS.createPage(List.of(DEBIT_CARD_TEAM), CONTENT_MANAGER);
    final Integer ccDcPageId =
        PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, DEBIT_CARD_TEAM), CONTENT_MANAGER);
    final Integer invPageId = PAGES_STEPS.createPage(List.of(INVEST_TEAM), CONTENT_MANAGER);
    final Integer mrtPageId = PAGES_STEPS.createPage(List.of(MORTGAGE_TEAM), CONTENT_MANAGER);
    final Integer invMrtPageId =
        PAGES_STEPS.createPage(List.of(INVEST_TEAM, MORTGAGE_TEAM), CONTENT_MANAGER);
    final Integer pilPageId = PAGES_STEPS.createPage(List.of(PIL_TEAM), CONTENT_MANAGER);
    final Integer smePageId = PAGES_STEPS.createPage(List.of(SME_TEAM), CONTENT_MANAGER);
    final Integer pilSmePageId =
        PAGES_STEPS.createPage(List.of(PIL_TEAM, SME_TEAM), CONTENT_MANAGER);
    final Integer comPageId = PAGES_STEPS.createPage(List.of(COMMON_TEAM), CONTENT_MANAGER);
    final Integer ccComPageId =
        PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, COMMON_TEAM), CONTENT_MANAGER);
    final Integer unPageId = PAGES_STEPS.createPage(null, CONTENT_MANAGER);
    return new Object[][]{
        // CREDIT_CARD_USER
        {
            ccPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccPageId)).setEnable(false).build(),
            CREDIT_CARD_USER
        },
        {
            ccDcPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccDcPageId)).setEnable(false).build(),
            CREDIT_CARD_USER
        },
        // DEBIT_CARD_USER
        {
            dcPageId,
            new Page.Builder().using(CREATED_PAGES.get(dcPageId)).setEnable(false).build(),
            DEBIT_CARD_USER
        },
            {
            ccDcPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccDcPageId)).setEnable(false).build(),
            DEBIT_CARD_USER
        },
        // INVEST_USER
        {
            invPageId,
            new Page.Builder().using(CREATED_PAGES.get(invPageId)).setEnable(false).build(),
            INVEST_USER
        },
        {
            invMrtPageId,
            new Page.Builder().using(CREATED_PAGES.get(invMrtPageId)).setEnable(false).build(),
            INVEST_USER
        },
        // MORTGAGE_USER
        {
            mrtPageId,
            new Page.Builder().using(CREATED_PAGES.get(mrtPageId)).setEnable(false).build(),
            MORTGAGE_USER
        },
            {
            invMrtPageId,
            new Page.Builder().using(CREATED_PAGES.get(invMrtPageId)).setEnable(false).build(),
            MORTGAGE_USER
        },
        // PIL_USER
        {
            pilPageId,
            new Page.Builder().using(CREATED_PAGES.get(pilPageId)).setEnable(false).build(),
            PIL_USER
        },
        {
            pilSmePageId,
            new Page.Builder().using(CREATED_PAGES.get(pilSmePageId)).setEnable(false).build(),
            PIL_USER
        },
        // SME_USER
        {
            smePageId,
            new Page.Builder().using(CREATED_PAGES.get(smePageId)).setEnable(false).build(),
            SME_USER
        },
        {
            pilSmePageId,
            new Page.Builder().using(CREATED_PAGES.get(pilSmePageId)).setEnable(false).build(),
            SME_USER
        },
        // COMMON_USER
        {
            comPageId,
            new Page.Builder().using(CREATED_PAGES.get(comPageId)).setEnable(false).build(),
            COMMON_USER
        },
        {
            ccComPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccComPageId)).setEnable(false).build(),
            COMMON_USER
        },
        // UNCLAIMED_USER
        {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false).build(),
            UNCLAIMED_USER
        },
        // CREDIT_CARD_USER
        {
            ccPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            dcPageId,
            new Page.Builder().using(CREATED_PAGES.get(dcPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            ccDcPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccDcPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            invPageId,
            new Page.Builder().using(CREATED_PAGES.get(invPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            mrtPageId,
            new Page.Builder().using(CREATED_PAGES.get(mrtPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            invMrtPageId,
            new Page.Builder().using(CREATED_PAGES.get(invMrtPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            pilPageId,
            new Page.Builder().using(CREATED_PAGES.get(pilPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            smePageId,
            new Page.Builder().using(CREATED_PAGES.get(smePageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            pilSmePageId,
            new Page.Builder().using(CREATED_PAGES.get(pilSmePageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            comPageId,
            new Page.Builder().using(CREATED_PAGES.get(comPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            ccComPageId,
            new Page.Builder().using(CREATED_PAGES.get(ccComPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
        {
            unPageId,
            new Page.Builder().using(CREATED_PAGES.get(unPageId)).setEnable(false)
                .setTeamsList(List.of(DEBIT_CARD_TEAM)).build(),
            CONTENT_MANAGER
        },
    };
  }

  @Test(description = "Позитивный тест копирования страницы в CS",
      dataProvider = "positiveCopyPageDataProvider")
  public void positiveCopyPageRoleModelTest(final Integer pageId,
                                            final AccessibleUser user) {
    assertThat(PAGES_STEPS.copyPage(pageId, CURRENT, user).getStatusCode()).isEqualTo(SC_OK);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "positiveCopyPageDataProvider")
  public static Object[][] positiveCopyPageDataProvider() {
    return new Object[][]{
        // CREDIT_CARD_USER
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM), CONTENT_MANAGER),
            CREDIT_CARD_USER
        },
        // DEBIT_CARD_USER
        {
            PAGES_STEPS.createPage(List.of(DEBIT_CARD_TEAM), CONTENT_MANAGER),
            DEBIT_CARD_USER
        },
        // INVEST_USER
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM), CONTENT_MANAGER),
            INVEST_USER
        },
        // MORTGAGE_USER
        {
            PAGES_STEPS.createPage(List.of(MORTGAGE_TEAM), CONTENT_MANAGER),
            MORTGAGE_USER
        },
        // PIL_USER
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM), CONTENT_MANAGER),
            PIL_USER
        },
        // SME_USER
        {
            PAGES_STEPS.createPage(List.of(SME_TEAM), CONTENT_MANAGER),
            SME_USER
        },
        // COMMON_USER
        {
            PAGES_STEPS.createPage(List.of(COMMON_TEAM), CONTENT_MANAGER),
            COMMON_USER
        },
        // UNCLAIMED_USER
        {
            PAGES_STEPS.createPage(null, CONTENT_MANAGER),
            UNCLAIMED_USER
        },
        // CONTENT_MANAGER
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(DEBIT_CARD_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, DEBIT_CARD_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(MORTGAGE_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM, MORTGAGE_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(SME_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM, SME_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(COMMON_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, COMMON_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
            },
        {
            PAGES_STEPS.createPage(null, CONTENT_MANAGER),
            CONTENT_MANAGER
        }
    };
  }

  @Test(description = "Позитивный тест удаления страницы в CS",
      dataProvider = "positiveDeletePageDataProvider")
  public void positiveDeletePageRoleModelTest(final Integer pageId,
                                              final AccessibleUser user) {
    assertThat(PAGES_STEPS.deletePage(pageId, user).getStatusCode()).isEqualTo(SC_OK);
  }

  /**
   * Data provider.
   *
   * @return user
   */
  @DataProvider(name = "positiveDeletePageDataProvider")
  public static Object[][] positiveDeletePageDataProvider() {
    return new Object[][]{
        // CREDIT_CARD_USER
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM), CONTENT_MANAGER),
            CREDIT_CARD_USER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, DEBIT_CARD_TEAM), CONTENT_MANAGER),
            CREDIT_CARD_USER
        },
        // DEBIT_CARD_USER
        {
            PAGES_STEPS.createPage(List.of(DEBIT_CARD_TEAM), CONTENT_MANAGER),
            DEBIT_CARD_USER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, DEBIT_CARD_TEAM), CONTENT_MANAGER),
            DEBIT_CARD_USER
        },
        // INVEST_USER
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM), CONTENT_MANAGER),
            INVEST_USER
        },
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM, MORTGAGE_TEAM), CONTENT_MANAGER),
            INVEST_USER
        },
        // MORTGAGE_USER
        {
            PAGES_STEPS.createPage(List.of(MORTGAGE_TEAM), CONTENT_MANAGER),
            MORTGAGE_USER
        },
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM, MORTGAGE_TEAM), CONTENT_MANAGER),
            MORTGAGE_USER
        },
        // PIL_USER
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM), CONTENT_MANAGER),
            PIL_USER
        },
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM, SME_TEAM), CONTENT_MANAGER),
            PIL_USER
        },
        // SME_USER
        {
            PAGES_STEPS.createPage(List.of(SME_TEAM), CONTENT_MANAGER),
            SME_USER
        },
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM, SME_TEAM), CONTENT_MANAGER),
            SME_USER
        },
        // COMMON_USER
        {
            PAGES_STEPS.createPage(List.of(COMMON_TEAM), CONTENT_MANAGER),
            COMMON_USER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, COMMON_TEAM), CONTENT_MANAGER),
            COMMON_USER
        },
        // UNCLAIMED_USER
        {
            PAGES_STEPS.createPage(null, CONTENT_MANAGER),
            UNCLAIMED_USER
        },
        // CONTENT_MANAGER
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(DEBIT_CARD_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, DEBIT_CARD_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(MORTGAGE_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(INVEST_TEAM, MORTGAGE_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(SME_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(PIL_TEAM, SME_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(COMMON_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(List.of(CREDIT_CARD_TEAM, COMMON_TEAM), CONTENT_MANAGER),
            CONTENT_MANAGER
        },
        {
            PAGES_STEPS.createPage(null, CONTENT_MANAGER),
            CONTENT_MANAGER
        }
    };
  }
}
