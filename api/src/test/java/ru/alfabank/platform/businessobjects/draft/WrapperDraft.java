package ru.alfabank.platform.businessobjects.draft;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import ru.alfabank.platform.businessobjects.Device;
import ru.alfabank.platform.businessobjects.Entity;
import ru.alfabank.platform.businessobjects.Method;


@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WrapperDraft {
  private final List<Object> operations;
  private final String version;
  private final Device device;

  /**
   * Class constructor.
   * @param operations operations
   */
  @JsonCreator
  public WrapperDraft(List<Object> operations) {
    this.operations = operations;
    this.version = "01";
    this.device = Device.desktop;
  }

  /**
   * OperationDraft Class.
   */
  @JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
  @JsonInclude (JsonInclude.Include.NON_NULL)
  public static class OperationDraft {

    private final Object data;
    private final Entity entity;
    private final Method method;
    private final String uid;

    /**
     * Class constructor.
     * @param data data
     * @param entity entity
     * @param method method
     * @param uid uid
     */
    @JsonCreator
    public OperationDraft(Object data, Entity entity, Method method, String uid) {
      this.data = data;
      this.entity = entity;
      this.method = method;
      this.uid = uid;
    }
  }
}

