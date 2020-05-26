package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ru.alfabank.platform.businessobjects.enums.Entity;
import ru.alfabank.platform.businessobjects.enums.OperationType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AuditTransactionList extends AbstractBusinessObject {

  private List<AuditTransaction> transactions;

  public List<AuditTransaction> getTransactions() {
    return transactions;
  }

  public static class AuditTransaction {
    private String transactionId;
    private String transactionTime;
    private String performer;
    private List<Page> pages;
    private List<Operation> operations;

    public String getTransactionId() {
      return transactionId;
    }

    public String getTransactionTime() {
      return transactionTime;
    }

    public String getPerformer() {
      return performer;
    }

    public List<Page> getPages() {
      return pages;
    }

    public List<Operation> getOperations() {
      return operations;
    }

    private static class Operation {

      @JsonProperty("new")
      private String newValues;
      private Integer operationNum;
      private OperationType operationType;
      private Integer pageId;
      private Entity entity;
    }
  }
}