package ru.alfabank.platform;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;

import java.time.Instant;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.shorturl.ShortUrl;

public class CreateShortUrlTest extends BaseTest {

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] createShortUrlPositiveDataProvider() {
    return new Object[][]{
        {
            new ShortUrl.Builder()
                .setRedirectUrl("https://alfabank.ru/get-money/credit-cards/100-days/")
                .setShortId(randomAlphanumeric(6))
                .setFrom(Instant.now().plusSeconds(2).toString())
                .setEndDate(Instant.now().plusSeconds(60).toString())
                .setRedirectCode(SC_MOVED_PERMANENTLY)
                .build()
        }
    };
  }

  @Test(dataProvider = "createShortUrlPositiveDataProvider")
  public void createSUrlPositiveTest(final ShortUrl body) {
    STEP.createShortUrlAssumingSuccess(body);
    STEP.checkSavedShortUrl(body);
  }
}
