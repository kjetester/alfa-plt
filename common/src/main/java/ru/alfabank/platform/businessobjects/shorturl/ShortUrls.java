package ru.alfabank.platform.businessobjects.shorturl;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class ShortUrls extends AbstractBusinessObject {

  @JsonProperty("redirect_url") private String redirectUrl;
  @JsonProperty("short") private String shortUrl;
  @JsonProperty("status") private String status;

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public String getStatus() {
    return status;
  }
}
