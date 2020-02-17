package ru.alfabank.platform.buisenessobjects.transitions;

import com.fasterxml.jackson.annotation.*;
import com.google.gson.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RabbitBody {

  @JsonProperty("properties")
  private Properties properties;
  @JsonProperty("headers")
  private Headers headers;
  @JsonProperty("routing_key")
  private String routing_key;
  @JsonProperty("payload")
  private String payload;
  @JsonProperty("payload_encoding")
  private String payload_encoding;

  /**
   * Class constructor.
   * @param body body
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public RabbitBody(Body body) {
    this.properties = new Properties();
    this.headers = new Headers();
    this.routing_key = "business-log.business-log-service";
    this.payload = new Gson().toJson(body);
    this.payload_encoding = "string";
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public void setHeaders(Headers headers) {
    this.headers = headers;
  }

  public void setRouting_key(String routing_key) {
    this.routing_key = routing_key;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public void setPayload_encoding(String payload_encoding) {
    this.payload_encoding = new Gson().toJson(payload_encoding);
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  static class Properties {

    @JsonCreator
    public Properties() {
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  static class Headers {

    @JsonCreator
    public Headers() {
    }
  }
}
