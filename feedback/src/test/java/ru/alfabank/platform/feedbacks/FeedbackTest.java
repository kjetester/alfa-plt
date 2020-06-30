package ru.alfabank.platform.feedbacks;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.feedback.DataWrapper;
import ru.alfabank.platform.businessobjects.feedback.Feedback;
import ru.alfabank.platform.businessobjects.feedback.Feedback.Builder;
import ru.alfabank.platform.businessobjects.feedback.FeedbackResponseWrapper;

public class FeedbackTest extends BaseTest {

  /**
   * Data provider.
   *
   * @return positive test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][]{
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1000),
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            100,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1000),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            100,
            randomAlphanumeric(1),
            null,
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            null,
            randomAlphanumeric(1000),
            randomAlphanumeric(5001),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(50),
            Instant.now().toString(),
            1,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)},
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            100,
            randomAlphanumeric(1000),
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1000),
            null,
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().toString(),
            100,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1),
            null,
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            100,
            randomAlphanumeric(1000),
            randomAlphanumeric(5001),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().toString(), null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1000),
            null,
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            100,
            randomAlphanumeric(1),
            randomAlphanumeric(10000),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            100,
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1000),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            1,
            randomAlphanumeric(1),
            null,
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            100,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            null,
            randomAlphanumeric(1000),
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(10000),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(50),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            1,
            randomAlphanumeric(1000),
            randomAlphanumeric(1),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(50),
            Instant.now().toString(),
            100,
            randomAlphanumeric(1000),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(50),
            Instant.now().toString(),
            1,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1000),
            null, randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(50),
            Instant.now().toString(),
            100,
            randomAlphanumeric(1000),
            null,
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        },
        {
            randomAlphanumeric(1024),
            randomAlphanumeric(1),
            Instant.now().minus(1, DAYS).toString(),
            1,
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            randomAlphanumeric(5001),
            randomAlphanumeric(1),
            randomAlphanumeric(50)
        }
    };
  }

  @Test(dataProvider = "positiveDataProvider")
  private void feedbackAdditionPositiveTest(final String uri,
                                            final String platform,
                                            final String feedbackDate,
                                            final Integer rate,
                                            final String question,
                                            final String selectedComment,
                                            final String comment,
                                            final String codeTeam,
                                            final String cityPath) {
    Feedback feedback = new Builder()
        .setUri(uri)
        .setPlatform(platform)
        .setFeedbackDate(feedbackDate)
        .setRate(rate)
        .setQuestion(question)
        .setSelectedComment(selectedComment)
        .setComment(comment)
        .setCodeTeam(codeTeam)
        .setCityPath(cityPath)
        .build();
    final var feedbackData = new DataWrapper(feedback);
    final var postResponse =
        FEEDBACK_STEPS.addFeedback(feedbackData);
    assertThat(postResponse.getStatusCode()).isEqualTo(SC_OK);
    final var getResponse =
        FEEDBACK_STEPS.getFeedback(feedback).as(FeedbackResponseWrapper.class).getData();
    assertThat(getResponse.size()).isEqualTo(1);
    getResponse.get(0).equals(feedbackData.getData());
  }

  /**
   * Data provider.
   *
   * @return negative test data
   */
  @DataProvider
  public static Object[][] negativeDataProvider() {
    return new Object[][]{
        {
            randomAlphanumeric(0),
            randomAlphanumeric(0),
            null,
            null,
            null,
            null,
            null,
            null,
            null
        },
        {
            randomAlphanumeric(0),
            null,
            Instant.now().plus(1, DAYS).toString(),
            0,
            randomAlphanumeric(0),
            randomAlphanumeric(0),
            randomAlphanumeric(0),
            randomAlphanumeric(0),
            randomAlphanumeric(0)
        },
        {
            null,
            null,
            null,
            0,
            null,
            randomAlphanumeric(0),
            null,
            randomAlphanumeric(0),
            null},
        {
            null,
            randomAlphanumeric(0),
            Instant.now().plus(1, DAYS).toString(),
            null,
            randomAlphanumeric(0),
            null,
            randomAlphanumeric(0),
            null,
            randomAlphanumeric(0)
        },
        {
            randomAlphanumeric(1),
            randomAlphanumeric(1),
            Instant.now().toString(),
            null,
            randomAlphanumeric(1),
            null,
            null,
            randomAlphanumeric(1),
            randomAlphanumeric(1)
        }
    };
  }

  @Test(dataProvider = "negativeDataProvider")
  private void feedbackAdditionNegativeTest(final String uri,
                                            final String platform,
                                            final String feedbackDate,
                                            final Integer rate,
                                            final String question,
                                            final String selectedComment,
                                            final String comment,
                                            final String codeTeam,
                                            final String cityPath) {
    final var feedbackData = new DataWrapper(new Feedback.Builder()
        .setUri(uri)
        .setPlatform(platform)
        .setFeedbackDate(feedbackDate)
        .setRate(rate)
        .setQuestion(question)
        .setSelectedComment(selectedComment)
        .setComment(comment)
        .setCodeTeam(codeTeam)
        .setCityPath(cityPath)
        .build());
    final var response = FEEDBACK_STEPS.addFeedback(feedbackData);
    assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST);
  }
}
