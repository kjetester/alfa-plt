package ru.alfabank.platform.audit.rolemodel;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.alfabank.platform.users.PilUser.getPilUser;
import static ru.alfabank.platform.users.SmeUser.getSmeUser;
import static ru.alfabank.platform.users.UnclaimedUser.getUnclaimedUser;

import org.testng.annotations.Test;
import ru.alfabank.platform.audit.AuditBaseTest;

public class NegativeAuditRoleModelTest extends AuditBaseTest {

  @Test(description = "Негативный тест просмотра списка транзакций")
  public void negativeReadTransactionsListTest() {
    final var response =
        AUDIT_STEPS.getTransactionList(getPilUser());
    assertThat(response.getStatusCode()).isEqualTo(SC_FORBIDDEN);
  }

  @Test(description = "Негативный тест просмотра транзакции", priority = 1)
  public void negativeReadTransactionTest() {
    final var response =
        AUDIT_STEPS.getTransaction(getSmeUser());
    assertThat(response.getStatusCode()).isEqualTo(SC_FORBIDDEN);
  }

  @Test(description = "Негативный тест отката транзакции", priority = 2)
  public void negativeRollbackTransactionTest() {
    final var response =
        AUDIT_STEPS.rollbackTransaction(getUnclaimedUser());
    assertThat(response.getStatusCode()).isEqualTo(SC_FORBIDDEN);
  }
}
