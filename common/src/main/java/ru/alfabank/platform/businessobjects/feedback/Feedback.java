package ru.alfabank.platform.businessobjects.feedback;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.within;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;

public class Feedback extends AbstractBusinessObject {

  private final String uri;
  private final String platform;
  private final String feedbackDate;
  private final Integer rate;
  private final String question;
  private final String selectedComment;
  private final String comment;
  private final String codeTeam;
  private final String cityPath;

  /**
   * Class constructor.
   *
   * @param uri             uri
   * @param platform        platform
   * @param feedbackDate    feedbackDate
   * @param rate            rate
   * @param question        question
   * @param selectedComment selectedComment
   * @param comment         comment
   * @param codeTeam        codeTeam
   * @param cityPath        cityPath
   */
  @JsonCreator
  public Feedback(@JsonProperty("uri") final String uri,
                  @JsonProperty("platform") final String platform,
                  @JsonProperty("feedbackDate") final String feedbackDate,
                  @JsonProperty("rate") final Integer rate,
                  @JsonProperty("question") final String question,
                  @JsonProperty("selectedComment") final String selectedComment,
                  @JsonProperty("comment") final String comment,
                  @JsonProperty("codeTeam") final String codeTeam,
                  @JsonProperty("cityPath") final String cityPath) {
    this.uri = uri;
    this.platform = platform;
    this.feedbackDate = feedbackDate;
    this.rate = rate;
    this.question = question;
    this.selectedComment = selectedComment;
    this.comment = comment;
    this.codeTeam = codeTeam;
    this.cityPath = cityPath;
  }

  /**
   * Class constructor.
   *
   * @param builder builder
   */
  public Feedback(Builder builder) {
    this.uri = builder.uri;
    this.platform = builder.platform;
    this.feedbackDate = builder.feedbackDate;
    this.rate = builder.rate;
    this.question = builder.question;
    this.selectedComment = builder.selectedComment;
    this.comment = builder.comment;
    this.codeTeam = builder.codeTeam;
    this.cityPath = builder.cityPath;
  }

  public String getUri() {
    return uri;
  }

  public String getPlatform() {
    return platform;
  }

  public String getFeedbackDate() {
    return feedbackDate;
  }

  public Integer getRate() {
    return rate;
  }

  public String getQuestion() {
    return question;
  }

  public String getSelectedComment() {
    return selectedComment;
  }

  public String getComment() {
    return comment;
  }

  public String getCodeTeam() {
    return codeTeam;
  }

  public String getCityPath() {
    return cityPath;
  }

  /**
   * Check feedback.
   *
   * @param expected expected feedback
   */
  public void equals(final Feedback expected) {
    final var softly = new SoftAssertions();
    softly.assertThat(this.uri).isEqualTo(expected.uri);
    softly.assertThat(this.platform).isEqualTo(expected.platform);
    softly.assertThat(Instant.parse(this.feedbackDate))
        .isCloseTo(Instant.parse(expected.feedbackDate), within(1, SECONDS));
    softly.assertThat(this.rate).isEqualTo(expected.rate);
    softly.assertThat(this.question).isEqualTo(expected.question, 0, 1000);
    softly.assertThat(this.selectedComment)
        .isEqualTo(StringUtils.substring(expected.selectedComment, 0, 5000));
    softly.assertThat(this.comment).isEqualTo(StringUtils.substring(expected.comment, 0, 5000));
    softly.assertThat(this.codeTeam).isEqualTo(expected.codeTeam);
    softly.assertThat(this.cityPath).isEqualTo(expected.cityPath);
    softly.assertAll();
  }

  public static class Builder {
    private String uri;
    private String platform;
    private String feedbackDate;
    private Integer rate;
    private String question;
    private String selectedComment;
    private String comment;
    private String codeTeam;
    private String cityPath;

    public Builder setUri(String uri) {
      this.uri = uri;
      return this;
    }

    public Builder setPlatform(String platform) {
      this.platform = platform;
      return this;
    }

    public Builder setFeedbackDate(String feedbackDate) {
      this.feedbackDate = feedbackDate;
      return this;
    }

    public Builder setRate(Integer rate) {
      this.rate = rate;
      return this;
    }

    public Builder setQuestion(String question) {
      this.question = question;
      return this;
    }

    public Builder setSelectedComment(String selectedComment) {
      this.selectedComment = selectedComment;
      return this;
    }

    public Builder setComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Builder setCodeTeam(String codeTeam) {
      this.codeTeam = codeTeam;
      return this;
    }

    public Builder setCityPath(String cityPath) {
      this.cityPath = cityPath;
      return this;
    }

    public Feedback build() {
      return new Feedback(this);
    }
  }
}
