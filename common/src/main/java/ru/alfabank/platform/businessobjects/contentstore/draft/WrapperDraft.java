package ru.alfabank.platform.businessobjects.contentstore.draft;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.enums.Device;

public class WrapperDraft extends AbstractBusinessObject {

  private final List<Object> operations;
  private final String version;
  private final Device device;

  /**
   * Class constructor.
   *
   * @param operations operations
   */
  @JsonCreator
  public WrapperDraft(final List<Object> operations, final Device device) {
    this.operations = operations;
    this.version = "1.0.0";
    this.device = device;
  }

  /**
   * OperationDraft Class.
   */
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class OperationDraft {

    private final Object data;
    private final String entity;
    private final String method;
    private final Object uid;

    /**
     * Class constructor.
     *
     * @param data   data
     * @param entity entity
     * @param method method
     * @param uid    uid
     */
    @JsonCreator
    public OperationDraft(Object data, String entity, String method, Object uid) {
      this.data = data;
      this.entity = entity;
      this.method = method;
      this.uid = uid;
    }
  }
}
