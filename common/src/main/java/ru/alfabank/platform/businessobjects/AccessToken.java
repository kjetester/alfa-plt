package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonAutoDetect (fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AccessToken {

  private static final int DRIFT = 15;

  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private String notBeforePolicy;
  private String sessionState;
  private String scope;
  private int expiresIn;
  private int refreshExpiresIn;
  @JsonIgnore private LocalDateTime expireAccessTokenTime;
  @JsonIgnore private LocalDateTime expireRefreshTokenTime;

  /**
   * Class constructor.
   * @param accessToken accessToken
   * @param refreshToken refreshToken
   * @param tokenType tokenType
   * @param notBeforePolicy noBeforePolicy
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
    setExpireAccessTokenTime(LocalDateTime.now().plusSeconds(expiresIn - DRIFT));
    this.refreshExpiresIn = refreshExpiresIn;
    setExpireRefreshTokenTime(LocalDateTime.now().plusSeconds(refreshExpiresIn - DRIFT));
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @JsonIgnore
  private void setExpireAccessTokenTime(LocalDateTime expireAccessTokenTime) {
    this.expireAccessTokenTime = expireAccessTokenTime;
  }

  @JsonIgnore
  public LocalDateTime getExpireAccessTokenTime() {
    return expireAccessTokenTime;
  }

  @JsonIgnore
  public LocalDateTime getExpireRefreshTokenTime() {
    return expireRefreshTokenTime;
  }

  @JsonIgnore
  private AccessToken setExpireRefreshTokenTime(LocalDateTime expireRefreshTokenTime) {
    this.expireRefreshTokenTime = expireRefreshTokenTime;
    return this;
  }
}