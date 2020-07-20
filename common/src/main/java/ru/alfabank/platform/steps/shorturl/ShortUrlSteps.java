package ru.alfabank.platform.steps.shorturl;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;

import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.shorturl.ShortUrl;
import ru.alfabank.platform.businessobjects.shorturl.ShortUrl.Builder;
import ru.alfabank.platform.steps.BaseSteps;

public class ShortUrlSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(ShortUrlSteps.class);
  private static final String ACCESS_TOKEN = "x5to8rjrAn0hwBKHoBaHHJCuFTx6zKam";


  /**
   * Create short URL.
   *
   * @param body body
   */
  public void createShortUrlAssumingSuccess(ShortUrl body) {
    LOGGER.info("Отправляю сообщение для создания короткой ссылки:\n"
        + describeBusinessObject(body));
    final var response = createNewShortUrl(body);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    LIST_OF_CREATED_ENTITIES.add(body.getShortId());
  }

  /**
   * Create array of short URLs.
   *
   * @param body body
   */
  public void createArrayOfShortUrlsAssumingSuccess(List<String> body) {
    LOGGER.info("Отправляю сообщение для создания короткой ссылки:\n"
        + describeBusinessObject(body));
    final var response = createArrayOfNewShortUrl(body);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    // body.forEach(e -> LIST_OF_CREATED_ENTITIES.add(e.getShortId()));
  }

  private Response createArrayOfNewShortUrl(List<String> body) {
    final var response = given()
        .spec(getShortUrlCreateArraySpec())
        .auth().oauth2(ACCESS_TOKEN)
        .body(body)
        .post();
    describeResponse(LOGGER, response);
    return response;
  }

  public void checkSavedShortUrl(List<ShortUrl> expected) {
    expected.forEach(this::checkSavedShortUrl);
  }

  /**
   * Check Saved Short Url.
   *
   * @param expected expected
   */
  public void checkSavedShortUrl(ShortUrl expected) {
    LOGGER.info("Отправляю сообщение для получения короткой ссылки:\n"
        + describeBusinessObject(expected));
    final var response = getShortUrl(expected);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    final var list = Arrays.asList(response.as(ShortUrl[].class));
    assertThat(list.size()).isEqualTo(1);
    expected = new Builder()
        .using(expected)
        .setCreatedAt(expected.getFrom())
        .setAuthor("u_m1413")
        .setPublished(true)
        .setModerated(true)
        .setDeleted(false)
        .build();
    list.get(0).equalsTo(expected);
  }

  private Response createNewShortUrl(ShortUrl body) {
    final var response = given()
        .spec(getShortUrlCreateSpec())
        .auth().oauth2(ACCESS_TOKEN)
        .body(body)
        .post();
    describeResponse(LOGGER, response);
    return response;
  }

  private Response getShortUrl(ShortUrl body) {
    final var response = given()
        .spec(getShortUrlReadSpec())
        .auth().oauth2(ACCESS_TOKEN)
        .queryParams(
            Map.of(
                "redirect_url.eq", body.getRedirectUrl(),
                "short.eq", body.getShortId()))
        .body(body)
        .get();
    describeResponse(LOGGER, response);
    return response;
  }

  public void modifyShortUrlAssumingSuccess(ShortUrl original, ShortUrl modifying) {
    final var response = modifyShortUrl(original, modifying);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
  }

  private Response modifyShortUrl(ShortUrl original, ShortUrl modifying) {
    LOGGER.info("Отправляю сообщение для создания короткой ссылки:\n"
        + describeBusinessObject(modifying));
    final var response = given()
        .spec(getShortUrlUpdateDeleteSpec())
        .auth().oauth2(ACCESS_TOKEN)
        .pathParams("id", original.getShortId())
        .body(modifying)
        .patch();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Delete All Created Entities.
   *
   * @param createdEntitiesIdsList createdEntitiesIdsList
   */
  public void deleteAllCreatedEntities(List<String> createdEntitiesIdsList) {
    createdEntitiesIdsList.forEach(id -> {
      final var response = given()
          .spec(getShortUrlUpdateDeleteSpec())
          .auth().oauth2(ACCESS_TOKEN)
          .pathParams("id", id)
          .delete();
      describeResponse(LOGGER, response);
      if (response.statusCode() == SC_OK) {
        LOGGER.info(String.format("Ссылка с ID '%s' удалена", id));
      } else {
        LOGGER.warn(String.format("Не удалось удалить ссылку с ID '%s'", id));
      }
    });

  }
}
