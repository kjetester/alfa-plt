package ru.alfabank.platform.businessobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.annotations.NonNull;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentPageControllerResponse extends AbstractBusinessObject {

  private static final Logger LOGGER =
      LogManager.getLogger(ContentPageControllerResponse.class);

  private final String title;
  private final String description;
  private final List<Widget> widgets;

  @JsonCreator
  private ContentPageControllerResponse(
      @JsonProperty("title") String title,
      @JsonProperty("description") String description,
      @JsonProperty("widgets") List<Widget> widgets) {
    this.title = title;
    this.description = description;
    this.widgets = widgets;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public List<Widget> getWidgets() {
    return widgets;
  }

  public static class Widget {

    private final String uid;
    private final String name;
    private final String version;
    @JsonRawValue
    private final Map<String, JsonNode> properties;
    private final List<Widget> childrenWidgetsList;

    @JsonCreator
    private Widget(
        @JsonProperty("uid") String uid,
        @JsonProperty("name") String name,
        @JsonProperty("version") String version,
        @JsonProperty("properties") Map<String, JsonNode> propertiesMap,
        @JsonProperty("children") List<Widget> childrenWidgetsList) {
      this.uid = uid;
      this.name = name;
      this.version = version;
      this.properties = propertiesMap;
      this.childrenWidgetsList = childrenWidgetsList;
    }

    private String getUid() {
      return uid;
    }

    private String getName() {
      return name;
    }

    private String getVersion() {
      return version;
    }

    private Map<String, JsonNode> getProperties() {
      return properties;
    }

    private List<Widget> getChildrenWidgetsList() {
      return childrenWidgetsList;
    }

    /**
     * Compare widgets.
     *
     * @param widget comparing widget
     * @param method copying method
     */
    @JsonIgnore
    public void equals(@NonNull Widget widget, CopyMethod method) {
      final SoftAssertions softly = new SoftAssertions();
      LOGGER.debug(String.format(
          "Сравнение виджетов:\n1.\t%s\n2.\t%s",
          this.toString(),
          widget.toString()));
      switch (method) {
        case SHARE:
          softly
              .assertThat(this.getUid())
              .as("Корректность UID при методе копирования " + method.toString())
              .isEqualTo(widget.getUid());
          break;
        case COPY:
          softly
              .assertThat(this.getUid())
              .as("Корректность UID при методе копирования " + method.toString())
              .isNotEqualTo(widget.getUid());
          break;
        default:
          LOGGER.warn("Метод копирования 'CURRENT' - корректность UID'ов не проверяется");
          break;
      }
      softly.assertThat(this.getName()).isEqualTo(widget.getName());
      softly.assertThat(this.getVersion()).isEqualTo(widget.getVersion());
      softly.assertThat(this.getProperties()).isEqualTo(widget.getProperties());
      IntStream.range(0, this.getChildrenWidgetsList().size()).forEach(i ->
          this.getChildrenWidgetsList().get(i)
              .equals(widget.getChildrenWidgetsList().get(i), method));
      softly.assertAll();
    }
  }
}
