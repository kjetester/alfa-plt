package ru.alfabank.platform.steps.feedback;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.feedback.DataWrapper;
import ru.alfabank.platform.businessobjects.feedback.Feedback;
import ru.alfabank.platform.steps.BaseSteps;

public class FeedbackSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(FeedbackSteps.class);

  /**
   * Add feedback.
   *
   * @param feedbackData feedback data
   * @return response
   */
  public Response addFeedback(final DataWrapper feedbackData) {
    LOGGER.info("Выполняю запрос на добавление фидбека:\n"
        + describeBusinessObject(feedbackData));
    final var response = given()
        .spec(getFeedbackAdditionSpec())
        .body(feedbackData)
        .post();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Get feedback.
   *
   * @param feedback feedback
   * @return response
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Response getFeedback(final Feedback feedback) {
    LOGGER.info("Выполняю запрос на чтение фидбека:\n"
        + describeBusinessObject(feedback));
    Map queryParams = new HashMap();
    if (feedback.getUri() != null) {
      queryParams.put("uri.eq", feedback.getUri());
    }
    if (feedback.getFeedbackDate() != null) {
      queryParams.put("feedbackDate.eq", feedback.getFeedbackDate());
    }
    if (feedback.getRate() != null) {
      queryParams.put("rate.eq", feedback.getRate());
    }
    if (feedback.getQuestion() != null) {
      queryParams.put("question.eq", feedback.getQuestion());
    }
    if (feedback.getCityPath() != null) {
      queryParams.put("cityPath.eq", feedback.getCityPath());
    }
    final var response = given()
        .spec(getFeedbackAdditionSpec())
        .auth().oauth2(getContentManager().getJwt().getAccessToken())
        .queryParams(queryParams)
        .get();
    describeResponse(LOGGER, response);
    return response;
  }
}
