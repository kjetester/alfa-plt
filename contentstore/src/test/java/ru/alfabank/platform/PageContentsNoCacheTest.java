package ru.alfabank.platform;

import static io.restassured.RestAssured.given;
import static rp.org.apache.http.HttpStatus.SC_OK;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;

import io.restassured.http.ContentType;
import java.time.Duration;
import java.time.Instant;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PageContentsNoCacheTest extends BaseTest {

  private static final Logger LOGGER = LogManager.getLogger(PageContentsNoCacheTest.class);

  private static int totalDiff = 0;

  /**
   * Test data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] uris() {
    return new Object[][]{
        {"/everyday/debit-cards/aeroflotcard-premium-short/"},
        {"/sme/tariffs/goodchoice/"},
        {"/get-money/mortgage/ipoteka-inostrannym-grazhdanam/"},
        {"/get-money/mortgage/ipoteka-na-5000000-rublej/"},
        {"/get-money/mortgage/ipoteka-pod-zalog-nedvizhimosti/"},
        {"/get-money/mortgage/complete_house_ab/"},
        {"/about/"},
        {"/everyday/debit-cards/alfacard-benefit/"},
        {"/get-money/credit/credit-cash/"},
        {"/everyday/debit-cards/aeroflot/"},
        {"/get-money/credit-cards/100-days/"},
        {"/sme/agent/recomendation-more-new/"},
        {"/everyday/debit-cards/alfacard-premium-short/"},
        {"/get-money/credit/credit-cash/step1/"},
        {"/get-money/mortgage/complete_house/"},
        {"/everyday/debit-cards/alfacard-short/"},
        {"/get-money/credit/credit-cash-ab/"},
        {"/get-money/credit-cards/land/100-days-salary/"},
        {"/sme/start/"},
        {"/get-money/credit-cards/cc-short/step1/"},
        {"/everyday/debit-cards/alfacard-short2/"},
        {"/get-money/credit/bez-spravok-i-poruchitelej/"},
        {"/get-money/credit/bez-otkaza/"},
        {"/get-money/credit/bez-kreditnoj-istorii/"},
        {"/get-money/credit/vygodnyj/"},
        {"/get-money/credit/5-let/"},
        {"/get-money/credit-cards/100-days-salary/"},
        {"/sme/class-a/"},
        {"/get-money/credit-cards/100-days-ab/"},
        {"/get-money/mortgage/refin/"},
        {"/everyday/debit-cards/cash-back-card/"},
        {"/sme/businesscard/"},
        {"/get-money/credit-cards/land/100-days-590/"},
        {"/get-money/mortgage/complete_house_promo/"},
        {"/sme/agent/start/"},
        {"/sme/tariffs/"},
        {"/privacy/"},
        {"/everyday/debit-cards/s-nachisleniem-procentov/"},
        {"/everyday/debit-cards/s-dostavkoj/"},
        {"/everyday/debit-cards/s-besplatnym-obsluzhivaniem/"},
        {"/sme/tariffs/alltoneed/"},
        {"/sme/tariffs/ved_plus/"},
        {"/everyday/debit-cards/travel-card-short/"},
        {"/get-money/mortgage/dokumenty-dlya-ipoteki/"},
        {"/get-money/mortgage/ipoteka-na-10-mln-rublej/"},
        {"/get-money/mortgage/ipoteka-na-dom/"},
        {"/get-money/mortgage/ipoteka-s-materinskim-kapitalom/"},
        {"/get-money/mortgage/"},
        {"/sme/tariffs/onepercent/"},
        {"/sme/"},
        {"/sme-new/"},
        {"/get-money/credit/calculator/"},
        {"/sme/submit-click/"},
        {"/get-money/credit/credit-cash-adv/"},
        {"/sme/raschetnyj-schet/"},
        {"/everyday/debit-cards/momentalnye/"},
        {"/everyday/debit-cards/multivalyutnaya/"},
        {"/get-money/credit/credit-cash-adv/step1/"},
        {"/get-money/credit-cards/100-days-metrics/"},
        {"/get-money/credit-cards/bez-spravok-o-doxodax/"},
        {"/get-money/credit-cards/dlya-snyatiya-nalichnyx/"},
        {"/get-money/credit-cards/s-18-let/"},
        {"/get-money/credit-cards/s-lgotnym-periodom/"},
        {"/get-money/credit-cards/s-lgotnym-snyatiem-nalichnyx/"},
        {"/sme/raschetnyj-schet/ip/"},
        {"/sme/raschetnyj-schet/ooo/"},
        {"/sme/start-old/"},
        {"/get-money/mortgage/ipotechnyj-kalkulyator/"},
        {"/sme/tariffs-compare/"},
        {"/everyday/debit-cards/aeroflotcard-short/"},
        {"/sme/tariffs/beststart/"},
        {"/everyday/debit-cards/travel-card-premium-short/"},
        {"/get-money/mortgage/ipoteka-bez-straxovki/"},
        {"/get-money/mortgage/ipoteka-na-4000000-rublej/"},
        {"/get-money/mortgage/ipoteka-po-2-dokumentam/"},
        {"/get-money/mortgage/ipoteka-s-pervym-vznosom/"},
        {"/get-money/credit-cards-archive/"},
        {"/get-money/credit-cards/100-days-0rub/"},
        {"/sme/raschetnyj-schet/biznes/"},
        {"/everyday/debit-cards/alfacard-short-rko/"},
        {"/get-money/credit-cards/rassrochka/"},
        {"/sme/raschetnyj-schet/besplatno/"},
        {"/sme/abp/"},
        {"/get-money/mortgage/refin_short/"},
        {"/get-money/mortgage/complete_house_short/"},
        {"/everyday/debit-cards/dlya-pensionerov/"},
        {"/get-money/credit/credit-cash-adv/step1-ab/"},
        {"/sme/shortpage/"}
    };
  }

  @Test(dataProvider = "uris")
  public void pageContentsNoCacheTest(@NotNull final String uri) {
    final var devStarted = Instant.now();
    given()
        .baseUri("http://develop.ci.k8s.alfa.link")
        .basePath("api/v1/content-store/page-contents-nocache")
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .queryParam("device", desktop)
        .queryParam("uri", uri)
        .body("{\"groups\":[\"ru\"]}")
        .when().post()
        .then().statusCode(SC_OK);
    final var devFinished = Instant.now();
    final var devDuration = Math.toIntExact(Duration.between(devStarted, devFinished).toMillis());
    final var featureStarted = Instant.now();
    given()
        .baseUri("http://feature-alfabankru-22049.content-store.reviews.ci.k8s.alfa.link")
        .basePath("page-contents-nocache")
        .relaxedHTTPSValidation()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .queryParam("device", desktop)
        .queryParam("uri", uri)
        .body("{\"groups\":[\"ru\"]}")
        .when().post()
        .then().statusCode(SC_OK);
    final var featureFinished = Instant.now();
    final var featureDuration =
        Math.toIntExact(Duration.between(featureStarted, featureFinished).toMillis());
    LOGGER.info(String.format("DEVELOP: %d mills", devDuration));
    LOGGER.info(String.format("FEATURE: %d mills", featureDuration));
    final var diff = devDuration - featureDuration;
    LOGGER.info(String.format("DIFF: %d mills", diff));
    totalDiff += diff;
  }

  @AfterTest
  public void afterTest() {
    LOGGER.info(String.format("TOTAL DIFF: %d mills", totalDiff));
  }
}
