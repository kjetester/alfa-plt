package ru.alfabank.platform.steps.cs;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.businessobjects.AbstractBusinessObject.describeBusinessObject;
import static ru.alfabank.platform.businessobjects.enums.Entity.PAGE;
import static ru.alfabank.platform.businessobjects.enums.Entity.WIDGET;
import static ru.alfabank.platform.businessobjects.enums.Localization.RU;
import static ru.alfabank.platform.businessobjects.enums.Method.CHANGE;
import static ru.alfabank.platform.businessobjects.enums.Method.CHANGE_LINKS;
import static ru.alfabank.platform.businessobjects.enums.Method.CREATE;
import static ru.alfabank.platform.businessobjects.enums.Version.V_1_0_0;
import static ru.alfabank.platform.helpers.UuidHelper.getNewUuid;

import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.Widget;
import ru.alfabank.platform.businessobjects.draft.DataDraft;
import ru.alfabank.platform.businessobjects.draft.WrapperDraft;
import ru.alfabank.platform.businessobjects.enums.Device;
import ru.alfabank.platform.businessobjects.enums.ExperimentOptionName;
import ru.alfabank.platform.businessobjects.enums.Geo;
import ru.alfabank.platform.steps.BaseSteps;
import ru.alfabank.platform.users.AccessibleUser;

public class DraftSteps extends BaseSteps {

  private static final Logger LOGGER = LogManager.getLogger(DraftSteps.class);

  /**
   * Create new widget at page level.
   *
   * @param page                 page
   * @param parentWidget         parent widget
   * @param device               device
   * @param enabled              is enabled
   * @param experimentOptionName experiment option name
   * @param defaultWidget        is default widget
   * @param geos                 geos
   * @param start                start date
   * @param end                  end date
   * @param user                 user
   * @return widget
   */
  public Widget createWidget(@NotNull final Page page,
                             final Widget parentWidget,
                             @NotNull final Device device,
                             @NotNull final Boolean enabled,
                             @NotNull final ExperimentOptionName experimentOptionName,
                             @NotNull final Boolean defaultWidget,
                             @NotNull final List<Geo> geos,
                             final String start,
                             final String end,
                             @NotNull final AccessibleUser user) {
    final var widget = new Widget.Builder()
        .setUid(getNewUuid())
        .setName(randomAlphanumeric(10))
        .setDateFrom(start)
        .setDateTo(end)
        .setDevice(device)
        .setLocalization(RU.getLocalization())
        .isEnabled(enabled)
        .setChildren(new ArrayList<>())
        .setChildUids(new ArrayList<>())
        .setProperties(new ArrayList<>())
        .setGeo(geos)
        .setVersion(V_1_0_0.getVersion())
        .setExperimentOptionName(experimentOptionName.toString())
        .isDefaultWidget(defaultWidget)
        .build();
    final var widgetCreationOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(widget.getName())
            .setDevice(widget.getDevice())
            .setLocalization(widget.getLocalization())
            .isEnabled(widget.isEnabled())
            .setVersion(widget.getVersion())
            .setExperimentOptionName(widget.getExperimentOptionName())
            .isDefaultWidget(widget.isDefaultWidget())
            .setCityGroups(List.of("ru"))
            .setDateFrom(widget.getDateFrom())
            .setDateTo(widget.getDateTo())
            .build(),
        WIDGET.toString(),
        CREATE.toString(),
        widget.getUid());
    WrapperDraft.OperationDraft widgetPlacementOperation = null;
    if (parentWidget == null) {
      final var widgetsList = page.getWidgetList()
          .stream()
          .filter(w -> w.getDevice() == device)
          .map(Widget::getUid)
          .collect(Collectors.toList());
      widgetsList.add(widget.getUid());
      widgetPlacementOperation = new WrapperDraft.OperationDraft(
          new DataDraft.Builder()
              .setChildUids(widgetsList)
              .build(),
          PAGE.toString(),
          CHANGE_LINKS.toString(),
          page.getId());
    } else {
      final var widgetsList = parentWidget.getChildUids();
      widgetsList.add(widget.getUid());
      widgetPlacementOperation = new WrapperDraft.OperationDraft(
          new DataDraft.Builder()
              .setChildUids(widgetsList)
              .build(),
          WIDGET.toString(),
          CHANGE_LINKS.toString(),
          parentWidget.getUid());
    }
    final var draft = new WrapperDraft(
        List.of(widgetCreationOperation, widgetPlacementOperation),
        widget.getDevice());
    LOGGER.info("Выполняю запрос сохранения черновика\n" + describeBusinessObject(draft));
    final var response =
        given()
            .spec(getDraftSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("pageId", page.getId())
            .body(draft)
            .put();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    publishDraft(page.getId(), device, user);
    if (parentWidget == null) {
      CREATED_PAGES.get(page.getId()).getWidgetList().add(widget);
    }
    return widget;
  }

  /**
   * Change widget active dates.
   *
   * @param widget widget
   * @param pageId page ID
   * @param start  start date
   * @param end    end date
   * @param user   user
   */
  public void changeWidgetActiveDates(final Widget widget,
                                      final Integer pageId,
                                      final String start,
                                      final String end,
                                      final AccessibleUser user) {
    var changedWidget = new Widget.Builder()
        .using(widget)
        .setDateFrom(start)
        .setDateTo(end)
        .build();
    var widgetChangingOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(changedWidget.getName())
            .setDevice(changedWidget.getDevice())
            .setLocalization(changedWidget.getLocalization())
            .isEnabled(changedWidget.isEnabled())
            .setVersion(changedWidget.getVersion())
            .setExperimentOptionName(changedWidget.getExperimentOptionName())
            .isDefaultWidget(changedWidget.isDefaultWidget())
            .setCityGroups(Collections.singletonList("ru"))
            .setDateFrom(changedWidget.getDateFrom())
            .setDateTo(changedWidget.getDateTo())
            .build(),
        WIDGET.toString(),
        CHANGE.toString(),
        changedWidget.getUid());
    var draft = new WrapperDraft(List.of(widgetChangingOperation), widget.getDevice());
    LOGGER.info("Выполняю запрос сохранения черновика\n" + describeBusinessObject(draft));
    var response =
        given()
            .spec(getDraftSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("pageId", pageId)
            .body(draft)
            .when().put()
            .then().extract().response();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    publishDraft(pageId, widget.getDevice(), user);
  }

  /**
   * Change Widget's AB-test Properties.
   *
   * @param widget               widget
   * @param pageId               pageId
   * @param isEnabled            isEnabled
   * @param experimentOptionName experimentOptionName
   * @param isDefaultWidget      isDefaultWidget
   * @param user                 user
   */
  public void changeWidgetABtestProps(final Widget widget,
                                      final Integer pageId,
                                      final Boolean isEnabled,
                                      final ExperimentOptionName experimentOptionName,
                                      final Boolean isDefaultWidget,
                                      final AccessibleUser user) {
    var changedWidget = new Widget.Builder()
        .using(widget)
        .isEnabled(isEnabled)
        .setExperimentOptionName(experimentOptionName.toString())
        .isDefaultWidget(isDefaultWidget)
        .build();
    var widgetChangingOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setName(changedWidget.getName())
            .setDevice(changedWidget.getDevice())
            .setLocalization(changedWidget.getLocalization())
            .isEnabled(changedWidget.isEnabled())
            .setVersion(changedWidget.getVersion())
            .setExperimentOptionName(changedWidget.getExperimentOptionName())
            .isDefaultWidget(changedWidget.isDefaultWidget())
            .setCityGroups(Collections.singletonList("ru"))
            .setDateFrom(changedWidget.getDateFrom())
            .setDateTo(changedWidget.getDateTo())
            .build(),
        WIDGET.toString(),
        CHANGE.toString(),
        changedWidget.getUid());
    var draft = new WrapperDraft(
        List.of(widgetChangingOperation),
        widget.getDevice());
    LOGGER.info("Выполняю запрос сохранения черновика\n" + describeBusinessObject(draft));
    var response =
        given()
            .spec(getDraftSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("pageId", pageId)
            .body(draft)
            .when().put()
            .then().extract().response();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    publishDraft(pageId, widget.getDevice(), user);
  }

  /**
   * Publish draft.
   *
   * @param pageId page
   * @param user   user
   */
  private void publishDraft(final Integer pageId,
                            final Device device,
                            final AccessibleUser user) {
    Response response;
    LOGGER.info("Выполняю запрос публикации черновика страницы с ID: " + pageId);
    response =
        given()
            .spec(getDraftExecSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("pageId", pageId)
            .queryParams("device", device)
            .when().post()
            .then().extract().response();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
  }

  /**
   * Share a Widget to another Page.
   *
   * @param widget widget
   * @param page   page
   * @param user   user
   */
  public void shareWidgetToAnotherPage(final Widget widget,
                                       final Page page,
                                       final AccessibleUser user) {
    Response response;
    var widgetsList = page.getWidgetList()
        .stream()
        .map(Widget::getUid)
        .collect(Collectors.toList());
    widgetsList.add(widget.getUid());
    var widgetPlacementOperation = new WrapperDraft.OperationDraft(
        new DataDraft.Builder()
            .setChildUids(widgetsList)
            .build(),
        PAGE.toString(),
        CHANGE_LINKS.toString(),
        page.getId());
    var draft = new WrapperDraft(
        List.of(widgetPlacementOperation),
        widget.getDevice());
    LOGGER.info("Выполняю запрос сохранения черновика шаринга\n" + describeBusinessObject(draft));
    response =
        given()
            .spec(getDraftSpec())
            .auth().oauth2(user.getJwt().getAccessToken())
            .pathParam("pageId", page.getId())
            .body(draft)
            .when().put()
            .then().extract().response();
    describeResponse(LOGGER, response);
    assertThat(response.getStatusCode()).isEqualTo(SC_OK);
    publishDraft(page.getId(), widget.getDevice(), user);
    CREATED_PAGES.get(page.getId()).getWidgetList().add(widget);
  }
}
