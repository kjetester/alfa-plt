package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class AccessToken extends AbstractBusinessObject {

  private static final int DRIFT = 15;

  private final String accessToken;
  private final String refreshToken;
  private final String tokenType;
  private final String notBeforePolicy;
  private final String sessionState;
  private final String scope;
  private final int expiresIn;
  private final int refreshExpiresIn;
  @JsonIgnore
  private Instant expireAccessTokenTime;
  @JsonIgnore
  private Instant expireRefreshTokenTime;

  /**
   * Class constructor.
   *
   * @param accessToken      accessToken
   * @param refreshToken     refreshToken
   * @param tokenType        tokenType
   * @param notBeforePolicy  noBeforePolicy
   * @param sessionState     sessionState
   * @param scope            scope
   * @param expiresIn        expiresIn
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
    setExpireAccessTokenTime(Instant.now().plusSeconds(expiresIn - DRIFT));
    this.refreshExpiresIn = refreshExpiresIn;
    setExpireRefreshTokenTime(Instant.now().plusSeconds(refreshExpiresIn - DRIFT));
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @JsonIgnore
  private void setExpireAccessTokenTime(Instant expireAccessTokenTime) {
    this.expireAccessTokenTime = expireAccessTokenTime;
  }

  @JsonIgnore
  public Instant getExpireAccessTokenTime() {
    return expireAccessTokenTime;
  }

  @JsonIgnore
  public Instant getExpireRefreshTokenTime() {
    return expireRefreshTokenTime;
  }

  @JsonIgnore
  private void setExpireRefreshTokenTime(Instant expireRefreshTokenTime) {
    this.expireRefreshTokenTime = expireRefreshTokenTime;
  }
}
