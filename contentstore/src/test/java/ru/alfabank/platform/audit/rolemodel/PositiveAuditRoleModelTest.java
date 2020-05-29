package ru.alfabank.platform.audit.rolemodel;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.alfabank.platform.users.AuditRollbackUser.getAuditRollbackUser;
import static ru.alfabank.platform.users.AuditViewUser.getAuditViewUser;

import org.testng.annotations.Test;
import ru.alfabank.platform.audit.AuditBaseTest;

public class PositiveAuditRoleModelTest extends AuditBaseTest {

  @Test(description = "Позитивный тест просмотра списка транзакций")
  public void positiveReadTransactionsListTest() {
    final var response =
        AUDIT_STEPS.getTransactionList(getAuditViewUser());
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
  }

  @Test(description = "Позитивный тест просмотра транзакции", priority = 1)
  public void positiveReadTransactionTest() {
    final var response =
        AUDIT_STEPS.getTransaction(getAuditViewUser());
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
  }

  @Test(description = "Позитивный тест отката транзакции", priority = 2)
  public void positiveRollbackTransactionTest() {
    final var response =
        AUDIT_STEPS.rollbackTransaction(getAuditRollbackUser());
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
  }

}
