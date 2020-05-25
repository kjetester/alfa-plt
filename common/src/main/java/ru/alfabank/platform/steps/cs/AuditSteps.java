package ru.alfabank.platform.steps.cs;

import static io.restassured.RestAssured.given;
import static ru.alfabank.platform.users.ContentManager.getContentManager;

import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.alfabank.platform.businessobjects.AuditTransactionList;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.users.AccessibleUser;

public class AuditSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(PagesSteps.class);

  /**
   * Get the list of unfiltered transactions.
   *
   * @param user user
   * @return response
   */
  public Response getTransactionList(AccessibleUser user) {
    Response response = given()
        .spec(getAuditTransactionsSpec())
        .auth().oauth2(user.getJwt().getAccessToken())
        .get();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Rollback transaction.
   *
   * @param user user
   * @return response
   */
  public Response rollbackTransaction(AccessibleUser user) {
    Response response = given()
        .spec(getAuditRollbackTransactionsSpec())
        .pathParam("transactionUid", getFirstTransactionId())
        .auth().oauth2(user.getJwt().getAccessToken())
        .post();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Get transaction.
   *
   * @param user user
   * @return response
   */
  public Response getTransaction(AccessibleUser user) {
    Response response = given()
        .spec(getAuditTransactionSpec())
        .pathParam("uid", getFirstTransactionId())
        .auth().oauth2(user.getJwt().getAccessToken())
        .get();
    describeResponse(LOGGER, response);
    return response;
  }

  /**
   * Get ID of the first transaction in the list.
   *
   * @return id
   */
  private String getFirstTransactionId() {
    final var response = getTransactionList(getContentManager());
    final var transactionsList = response.as(AuditTransactionList.class);
    return transactionsList.getTransactions().get(0).getTransactionId();
  }
}
