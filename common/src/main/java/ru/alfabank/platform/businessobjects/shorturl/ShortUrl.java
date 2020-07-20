package ru.alfabank.platform.businessobjects.shorturl;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.within;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class ShortUrl extends AbstractBusinessObject {

  @JsonProperty("redirect_url")
  private final String redirectUrl;
  @JsonProperty("short_id")
  private final String shortId;
  @JsonProperty("from")
  private final String from;
  @JsonProperty("endDate")
  private final String endDate;
  @JsonProperty("created_at")
  private final String createdAt;
  @JsonProperty("redirect_code")
  private final Integer redirectCode;
  @JsonProperty("is_published")
  private final Boolean isPublished;
  @JsonProperty("is_moderated")
  private final Boolean isModerated;
  @JsonProperty("is_deleted")
  private final Boolean isDeleted;
  @JsonProperty("author")
  private final String author;

  /**
   * Class creator.
   *
   * @param builder short url
   */
  public ShortUrl(Builder builder) {
    this.redirectUrl = builder.redirectUrl;
    this.shortId = builder.shortId;
    this.from = builder.from;
    this.endDate = builder.endDate;
    this.createdAt = builder.createdAt;
    this.redirectCode = builder.redirectCode;
    this.isPublished = builder.isPublished;
    this.isModerated = builder.isModerated;
    this.isDeleted = builder.isDeleted;
    this.author = builder.author;
  }

  /**
   * Class creator.
   *
   * @param redirectUrl  redirectUrl
   * @param shortId      shortId
   * @param from         from
   * @param endDate      endDate
   * @param redirectCode redirectCode
   * @param isPublished  isPublished
   * @param isModerated  isModerated
   * @param isDeleted    isDeleted
   * @param author       author
   */
  @JsonCreator
  public ShortUrl(@JsonProperty("redirect_url") String redirectUrl,
                  @JsonProperty("short_id") String shortId,
                  @JsonProperty("from") String from,
                  @JsonProperty("endDate") String endDate,
                  @JsonProperty("created_at") String createdAt,
                  @JsonProperty("redirect_code") Integer redirectCode,
                  @JsonProperty("is_published") Boolean isPublished,
                  @JsonProperty("is_moderated") Boolean isModerated,
                  @JsonProperty("is_deleted") Boolean isDeleted,
                  @JsonProperty("author") String author) {
    this.redirectUrl = redirectUrl;
    this.shortId = shortId;
    this.from = from;
    this.endDate = endDate;
    this.createdAt = createdAt;
    this.redirectCode = redirectCode;
    this.isPublished = isPublished;
    this.isModerated = isModerated;
    this.isDeleted = isDeleted;
    this.author = author;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public String getShortId() {
    return shortId;
  }

  public String getFrom() {
    return from;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getAuthor() {
    return author;
  }

  public Boolean getPublished() {
    return isPublished;
  }

  public Boolean getModerated() {
    return isModerated;
  }

  public Boolean getDeleted() {
    return isDeleted;
  }

  public Integer getRedirectCode() {
    return redirectCode;
  }

  /**
   * Compare two short urls.
   *
   * @param expected expected short url
   */
  public void equalsTo(final ShortUrl expected) {
    final var softly = new SoftAssertions();
    softly.assertThat(this.getRedirectUrl()).isEqualTo(expected.getRedirectUrl());
    softly.assertThat(this.getShortId()).isEqualTo(expected.getShortId());
    if (this.getFrom() != null) {
      softly.assertThat(LocalDateTime.parse(this.getFrom(), ISO_OFFSET_DATE_TIME))
          .isCloseTo(LocalDateTime.parse(expected.getFrom(), ISO_OFFSET_DATE_TIME),
              within(5, SECONDS));
    }
    if (this.getEndDate() != null) {
      softly.assertThat(LocalDateTime.parse(this.getEndDate(), ISO_OFFSET_DATE_TIME))
          .isCloseTo(
              LocalDateTime.parse(expected.getEndDate(), ISO_OFFSET_DATE_TIME),
              within(5, SECONDS));
    }
    if (this.getCreatedAt() != null) {
      softly.assertThat(LocalDateTime.parse(this.getCreatedAt(), ISO_OFFSET_DATE_TIME))
          .isCloseTo(
              LocalDateTime.parse(expected.getCreatedAt(), ISO_OFFSET_DATE_TIME),
              within(5, SECONDS));
    }
    softly.assertThat(this.getRedirectCode()).isEqualTo(expected.getRedirectCode());
    softly.assertThat(this.getPublished()).isEqualTo(expected.getPublished());
    softly.assertThat(this.getModerated()).isEqualTo(expected.getModerated());
    softly.assertThat(this.getDeleted()).isEqualTo(expected.getDeleted());
    softly.assertThat(this.getAuthor()).isEqualTo(expected.getAuthor());
    softly.assertAll();
  }

  public static class Builder {

    private String redirectUrl;
    private String shortId;
    private String from;
    private String endDate;
    private String createdAt;
    private Integer redirectCode;
    private Boolean isPublished;
    private Boolean isModerated;
    private Boolean isDeleted;
    private String author;


    public Builder setRedirectUrl(String redirectUrl) {
      this.redirectUrl = redirectUrl;
      return this;
    }

    public Builder setShortId(String shortId) {
      this.shortId = shortId;
      return this;
    }

    public Builder setFrom(String from) {
      this.from = from;
      return this;
    }

    public Builder setEndDate(String endDate) {
      this.endDate = endDate;
      return this;
    }

    public Builder setCreatedAt(String createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder setRedirectCode(Integer redirectCode) {
      this.redirectCode = redirectCode;
      return this;
    }

    public Builder setPublished(Boolean published) {
      isPublished = published;
      return this;
    }

    public Builder setModerated(Boolean moderated) {
      isModerated = moderated;
      return this;
    }

    public Builder setDeleted(Boolean deleted) {
      isDeleted = deleted;
      return this;
    }

    public Builder setAuthor(String author) {
      this.author = author;
      return this;
    }

    public ShortUrl build() {
      return new ShortUrl(this);
    }

    /**
     * Reuse shortUrl.
     *
     * @param shortUrl shortUrl
     * @return builder
     */
    public Builder using(ShortUrl shortUrl) {
      this.redirectUrl = shortUrl.redirectUrl;
      this.shortId = shortUrl.shortId;
      this.from = shortUrl.from;
      this.endDate = shortUrl.endDate;
      this.createdAt = shortUrl.createdAt;
      this.redirectCode = shortUrl.redirectCode;
      this.isPublished = shortUrl.isPublished;
      this.isModerated = shortUrl.isModerated;
      this.isDeleted = shortUrl.isDeleted;
      this.author = shortUrl.author;
      return this;
    }
  }
}
