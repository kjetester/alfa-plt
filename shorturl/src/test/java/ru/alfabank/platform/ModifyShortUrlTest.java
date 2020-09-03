package ru.alfabank.platform;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;

import java.time.Instant;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.shorturl.ShortUrl;

public class ModifyShortUrlTest extends BaseTest {

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] modifyShortUrlPositiveDataProvider() {
    final var shortId = randomAlphanumeric(6);
    return new Object[][]{
        {
            new ShortUrl.Builder()
                .setRedirectUrl("https://alfabank.ru/get-money/credit-cards/100-days/")
                .setShortId(shortId)
                .setFrom(Instant.now().plusSeconds(2).toString())
                .setEndDate(Instant.now().plusSeconds(60).toString())
                .setRedirectCode(SC_MOVED_PERMANENTLY)
                .build(),
            new ShortUrl.Builder()
                .setRedirectUrl("https://alfabank.ru/sme/")
                .setShortId(shortId)
                .setFrom(Instant.now().plusSeconds(2).toString())
                .setEndDate(Instant.now().plusSeconds(180).toString())
                .setRedirectCode(SC_MOVED_TEMPORARILY)
                .build()
        }
    };
  }

  @Test(dataProvider = "modifyShortUrlPositiveDataProvider")
  public void modifyShortUrlPositiveTest(final ShortUrl original,
                                         final ShortUrl modifying) {
    STEP.createShortUrlAssumingSuccess(original);
    STEP.modifyShortUrlAssumingSuccess(original, modifying);
    STEP.checkSavedShortUrl(modifying);
  }
}
