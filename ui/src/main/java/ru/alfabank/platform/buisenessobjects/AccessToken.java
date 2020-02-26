package ru.alfabank.platform.buisenessobjects;

import com.fasterxml.jackson.annotation.*;

import java.time.*;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AccessToken {

  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private String notBeforePolicy;
  private String sessionState;
  private String scope;
  private int expiresIn;
  private int refreshExpiresIn;
  @JsonIgnore private LocalDateTime expireTime;

  /**
   * Class constructor.
   * @param accessToken accessToken
   * @param refreshToken refreshToken
   * @param tokenType tokenType
   * @param notBeforePolicy noBeforPolicy
   * @param sessionState sessionState
   * @param scope scope
   * @param expiresIn expiresIn
   * @param refreshExpiresIn refreshExpiresIn
   */
  @JsonCreator
  public AccessToken(
      @JsonProperty("access_token") String accessToken,
      @JsonProperty("refresh_token") String refreshToken,
      @JsonProperty("token_type") String tokenType,
      @JsonProperty("not-before-policy") String notBeforePolicy,
      @JsonProperty("session_state") String sessionState,
      @JsonProperty("scope") String scope,
      @JsonProperty("expires_in") int expiresIn,
      @JsonProperty("refresh_expires_in") int refreshExpiresIn) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = tokenType;
    this.notBeforePolicy = notBeforePolicy;
    this.sessionState = sessionState;
    this.scope = scope;
    this.expiresIn = expiresIn;
    this.refreshExpiresIn = refreshExpiresIn;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  @JsonIgnore
  public void setExpireTime(LocalDateTime expireTime) {
    this.expireTime = expireTime;
  }

  @JsonIgnore
  public LocalDateTime getExpireTime() {
    return expireTime;
  }
}
