package ru.alfabank.platform.businessobjects.abtests;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.within;
import static ru.alfabank.platform.businessobjects.enums.Status.DISABLED;
import static ru.alfabank.platform.businessobjects.enums.Status.RUNNING;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.reactivex.annotations.NonNull;
import java.time.LocalDateTime;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ProductType;
import ru.alfabank.platform.businessobjects.enums.Status;

public class Experiment extends AbstractBusinessObject {

  @JsonIgnore
  private static final Logger LOGGER = LogManager.getLogger(Experiment.class);

  private final String uuid;
  private final String cookieValue;
  private final String description;
  private final Integer pageId;
  private final ProductType productTypeKey;
  private final String endDate;
  private final Double trafficRate;
  private final Device device;
  private final Boolean enabled;
  private final String createdBy;
  private final String activatedBy;
  private final String activationDate;
  private final String deactivatedBy;
  private final String deactivationDate;
  private final Status status;
  private final String creationDate;

  @JsonCreator
  private Experiment(Builder builder) {
    this.uuid = builder.uuid;
    this.cookieValue = builder.cookieValue;
    this.description = builder.description;
    this.pageId = builder.pageId;
    this.productTypeKey = builder.productTypeKey;
    this.endDate = builder.endDate;
    this.trafficRate = builder.trafficRate;
    this.device = builder.device;
    this.enabled = builder.enabled;
    this.createdBy = builder.createdBy;
    this.activatedBy = builder.activatedBy;
    this.activationDate = builder.activationDate;
    this.deactivatedBy = builder.deactivatedBy;
    this.deactivationDate = builder.deactivationDate;
    this.status = builder.status;
    this.creationDate = builder.creationDate;
  }

  public String getUuid() {
    return uuid;
  }

  public String getCookieValue() {
    return cookieValue;
  }

  public String getDescription() {
    return description;
  }

  public Integer getPageId() {
    return pageId;
  }

  public ProductType getProductTypeKey() {
    return productTypeKey;
  }

  public String getEndDate() {
    return endDate;
  }

  public Double getTrafficRate() {
    return trafficRate;
  }

  public Device getDevice() {
    return device;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public String getActivatedBy() {
    return activatedBy;
  }

  public String getActivationDate() {
    return activationDate;
  }

  public String getDeactivatedBy() {
    return deactivatedBy;
  }

  public String getDeactivationDate() {
    return deactivationDate;
  }

  public Status getStatus() {
    return status;
  }

  public String getCreationDate() {
    return creationDate;
  }

  /**
   * Check experiments properties.
   *
   * @param expected expected Experiment
   */
  public void equals(@NonNull final Experiment expected) {
    logComparingObjects(LOGGER, this, expected);
    final var softly = new SoftAssertions();
    softly.assertThat(this.getUuid())
        .as("Проверка соответствия UUID")
        .isEqualTo(expected.getUuid());
    softly.assertThat(this.getDescription())
        .as("Проверка соответствия Описания")
        .isEqualTo(expected.getDescription());
    softly.assertThat(this.getCookieValue())
        .as("Проверка соответствия Куки")
        .isEqualTo(expected.getCookieValue());
    softly.assertThat(this.getPageId())
        .as("Проверка соответствия ID страницы")
        .isEqualTo(expected.getPageId());
    softly.assertThat(this.getDevice())
        .as("Проверка соответствия Устройства")
        .isEqualTo(expected.getDevice());
    softly.assertThat(this.getProductTypeKey())
        .as("Проверка соответствия Ключа типа продукта")
        .isEqualTo(expected.getProductTypeKey());
    softly.assertThat(LocalDateTime.parse(this.getEndDate(), ISO_OFFSET_DATE_TIME))
        .as("Проверка соответствия Даты окончания")
        .isEqualTo(LocalDateTime.parse(expected.getEndDate(), ISO_OFFSET_DATE_TIME));
    softly.assertThat(this.getTrafficRate())
        .as("Проверка соответствия Уровня трафика")
        .isEqualTo(expected.getTrafficRate());
    softly.assertThat(this.getEnabled())
        .as("Проверка соответствия Признака активности")
        .isEqualTo(expected.getEnabled());
    softly.assertThat(this.getCreatedBy())
        .as("Проверка соответствия Автора создания")
        .isEqualTo(expected.getCreatedBy());
    softly.assertThat(this.getActivatedBy())
        .as("Проверка соответствия Автора активации")
        .isEqualTo(expected.getActivatedBy());
    if (expected.getStatus() != DISABLED) {
      softly.assertThat(LocalDateTime.parse(this.getActivationDate(), ISO_OFFSET_DATE_TIME))
          .as("Проверка соответствия Даты активации")
          .isCloseTo(
              LocalDateTime.parse(expected.getActivationDate(), ISO_OFFSET_DATE_TIME),
              within(1, MINUTES));
    } else {
      softly.assertThat(this.getActivationDate())
          .as("Проверка соответствия Даты активации")
          .isEqualTo(expected.getActivationDate());
    }
    softly.assertThat(this.getDeactivatedBy())
        .as("Проверка соответствия Автора деактивации")
        .isEqualTo(expected.getDeactivatedBy());
    if (expected.getStatus() != DISABLED && expected.getStatus() != RUNNING) {
      softly.assertThat(LocalDateTime.parse(this.getDeactivationDate(), ISO_OFFSET_DATE_TIME))
          .as("Проверка соответствия Даты деактивации")
          .isCloseTo(
              LocalDateTime.parse(expected.getDeactivationDate(), ISO_OFFSET_DATE_TIME),
              within(1, MINUTES));
    } else {
      softly.assertThat(this.getDeactivationDate())
          .as("Проверка соответствия Даты деактивации")
          .isEqualTo(expected.getDeactivationDate());
    }
    softly.assertThat(this.getStatus())
        .as("Проверка соответствия Статуса")
        .isEqualTo(expected.getStatus());
    softly.assertThat(LocalDateTime.parse(this.getCreationDate(), ISO_OFFSET_DATE_TIME))
        .as("Проверка соответствия Даты создания")
        .isCloseTo(
            LocalDateTime.parse(expected.getCreationDate(), ISO_OFFSET_DATE_TIME),
            within(1, MINUTES));
    softly.assertAll();
    logComparingResult(LOGGER, this.getUuid());
  }

  @JsonIgnoreType
  public static class Builder {

    private String uuid;
    private String cookieValue;
    private String description;
    private Integer pageId;
    private ProductType productTypeKey;
    private String endDate;
    private Double trafficRate;
    private Device device;
    private Boolean enabled;
    private String createdBy;
    private String activatedBy;
    private String activationDate;
    private String deactivatedBy;
    private String deactivationDate;
    private Status status;
    private String creationDate;

    public Builder setUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder setCookieValue(String cookieValue) {
      this.cookieValue = cookieValue;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setPageId(Integer pageId) {
      this.pageId = pageId;
      return this;
    }

    public Builder setProductTypeKey(ProductType productTypeKey) {
      this.productTypeKey = productTypeKey;
      return this;
    }

    public Builder setEndDate(String endDate) {
      this.endDate = endDate;
      return this;
    }

    public Builder setTrafficRate(Double trafficRate) {
      this.trafficRate = trafficRate;
      return this;
    }

    public Builder setDevice(Device device) {
      this.device = device;
      return this;
    }

    public Builder setEnabled(Boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public Builder setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder setActivatedBy(String activatedBy) {
      this.activatedBy = activatedBy;
      return this;
    }

    public Builder setActivationDate(String activationDate) {
      this.activationDate = activationDate;
      return this;
    }

    public Builder setDeactivatedBy(String deactivatedBy) {
      this.deactivatedBy = deactivatedBy;
      return this;
    }

    public Builder setDeactivationDate(String deactivationDate) {
      this.deactivationDate = deactivationDate;
      return this;
    }

    public Builder setStatus(Status status) {
      this.status = status;
      return this;
    }

    public Builder setCreationDate(String creationDate) {
      this.creationDate = creationDate;
      return this;
    }

    /**
     * Reuse experiment.
     *
     * @param experiment experiment
     * @return this
     */
    public Builder using(Experiment experiment) {
      this.uuid = experiment.uuid;
      this.cookieValue = experiment.cookieValue;
      this.description = experiment.description;
      this.pageId = experiment.pageId;
      this.productTypeKey = experiment.productTypeKey;
      this.endDate = experiment.endDate;
      this.trafficRate = experiment.trafficRate;
      this.device = experiment.device;
      this.enabled = experiment.enabled;
      this.createdBy = experiment.createdBy;
      this.activatedBy = experiment.activatedBy;
      this.activationDate = experiment.activationDate;
      this.deactivatedBy = experiment.deactivatedBy;
      this.deactivationDate = experiment.deactivationDate;
      this.status = experiment.status;
      this.creationDate = experiment.creationDate;
      return this;
    }

    public Experiment build() {
      return new Experiment(this);
    }
  }
}
