package ru.alfabank.platform.option.create.negative;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.enums.Device.desktop;
import static ru.alfabank.platform.businessobjects.enums.ExperimentOptionName.FOR_AB_TEST;
import static ru.alfabank.platform.businessobjects.enums.ProductType.getRandomProductType;

import java.util.List;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.enums.User;
import ru.alfabank.platform.option.OptionBaseTest;

public class AssignmentToAlreadyAssignedWidgetTest extends OptionBaseTest {

  @Test (description = "Тест создания варианта с ассоциацией с виджетом, который уже ассоциирован "
          + "с другим вариантом")
  public void assignmentToAlreadyAssignedWidgetTest() {
    setUser(User.CONTENT_MANAGER);
    final var experimentEnd = getCurrentDateTime().plusDays(1).plusMinutes(5).toString();
    var page = createPage(null, null, true);
    final var pageId = page.getId();
    page = createdPages.get(pageId);
    final var widget = createWidget(
            page, null, desktop, false, FOR_AB_TEST, false, null, null);
    final var experiment = createExperiment(
            widget.getDevice(), pageId, getRandomProductType(), experimentEnd, .5D);
    createOption(
            false, List.of(widget.getUid()), experiment.getUuid(), .5D);
    final var response = createOptionAssumingFail(
            false, List.of(widget.getUid()), experiment.getUuid(), .5D);
    assertThat(response.getStatusCode())
        .as("Проверка статус-кода")
        .isGreaterThanOrEqualTo(SC_BAD_REQUEST);
    assertThat(response.getBody().asString())
        .as("Проверка сообщения")
        .contains("Обнаружены виджеты, использущиеся в нескольких вариантах");
  }
}
