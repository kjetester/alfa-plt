package ru.alfabank.platform.buisenessobjects.transitions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RabbitBody {

  @JsonProperty("properties")
  private Properties properties;
  @JsonProperty("headers")
  private Headers headers;
  @JsonProperty("routing_key")
  private String routingKey;
  @JsonProperty("payload")
  private String payload;
  @JsonProperty("payload_encoding")
  private String payloadEncoding;

  /**
   * Class constructor.
   * @param body body
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public RabbitBody(Body body) {
    this.properties = new Properties();
    this.headers = new Headers();
    this.routingKey = "business-log.business-log-service";
    this.payload = new Gson().toJson(body);
    this.payloadEncoding = "string";
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public void setHeaders(Headers headers) {
    this.headers = headers;
  }

  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public void setPayloadEncoding(String payloadEncoding) {
    this.payloadEncoding = new Gson().toJson(payloadEncoding);
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
