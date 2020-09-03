package ru.alfabank.platform;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;

import java.time.Instant;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.shorturl.ShortUrl;

public class CreateArrayOfShortUrlsTest extends BaseTest {

  /**
   * Data provider.
   *
   * @return test data
   */
  @DataProvider
  public static Object[][] createShortUrlPositiveDataProvider() {
    return new Object[][]{
        {
            List.of(
                new ShortUrl.Builder()
                    .setRedirectUrl("https://alfabank.ru/sme/")
                    .setShortId(randomAlphanumeric(6))
                    .setFrom(Instant.now().plusSeconds(2).toString())
                    .setEndDate(Instant.now().plusSeconds(60).toString())
                    .setRedirectCode(SC_MOVED_TEMPORARILY)
                    .build(),
                new ShortUrl.Builder()
                    .setRedirectUrl("https://alfabank.ru/about/")
                    .setShortId(randomAlphanumeric(6))
                    .setFrom(Instant.now().plusSeconds(2).toString())
                    .setEndDate(Instant.now().plusSeconds(60).toString())
                    .setRedirectCode(SC_MOVED_PERMANENTLY)
                    .build())
        }
    };
  }

  @Test(dataProvider = "createShortUrlPositiveDataProvider")
  public void createSUrlPositiveTest(final List<String> body) {
    STEP.createArrayOfShortUrlsAssumingSuccess(body);
//    STEP.checkSavedShortUrl(LIST_OF_CREATED_ENTITIES);
  }
}
