package ru.alfabank.platform.geogroups.deletion;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.geofacade.GeoGroup;

public class GeoGroupDeletionTest extends BaseTest {

  @Test(dataProvider = "positiveDataProvider")
  private void geoGroupDeletionPlainPositiveTest(final GeoGroup geoGroup) {
    final var createdGeoGroup = GEO_STEPS.createGeoGroupAssumingSuccess(geoGroup);
    GEO_STEPS.deleteGeoGroupAssumingSuccess(createdGeoGroup);
    GEO_STEPS.getAbsentGeoGroup(createdGeoGroup);
  }

  @Test(dataProvider = "positiveDataProvider", priority = 1)
  private void unassignedFromWidgetGeoGroupDeletionPositiveTest(final GeoGroup geoGroup) {
    final var createdGeoGroup =
        GEO_STEPS.createGeoGroupAssumingSuccess(geoGroup);
    final var pageId =
        PAGES_STEPS.createEnabledPage();
    final var widget =
        DRAFT_STEPS.createDefaultDesktopRootEnabledUndatedWidgetByContentManager(
            pageId,
            List.of(createdGeoGroup.getCode()));
    DRAFT_STEPS.deleteWidget(pageId, widget);
    GEO_STEPS.deleteGeoGroupAssumingSuccess(createdGeoGroup);
    GEO_STEPS.getAbsentGeoGroup(createdGeoGroup);
  }

  @Test(dataProvider = "positiveDataProvider", priority = 1)
  private void unassignedFromValueGeoGroupDeletionPositiveTest(final GeoGroup geoGroup) {
    final var createdGeoGroup =
        GEO_STEPS.createGeoGroupAssumingSuccess(geoGroup);
    final var pageId =
        PAGES_STEPS.createEnabledPage();
    final var widget =
        DRAFT_STEPS.createDefaultDesktopRootEnabledUndatedWidgetByContentManager(
            pageId,
            List.of());
    DRAFT_STEPS.createPropertyWithValueByContentManager(
        pageId,
        widget,
        List.of(createdGeoGroup.getCode()));
    DRAFT_STEPS.deleteWidget(pageId, widget);
    GEO_STEPS.deleteGeoGroupAssumingSuccess(createdGeoGroup);
    GEO_STEPS.getAbsentGeoGroup(createdGeoGroup);
  }

  @Test(dataProvider = "positiveDataProvider", priority = 2)
  private void unassignedFromWidgetGeoGroupDeletionNegativeTest(final GeoGroup geoGroup) {
    final var createdGeoGroup =
        GEO_STEPS.createGeoGroupAssumingSuccess(geoGroup);
    final var pageId =
        PAGES_STEPS.createEnabledPage();
    final var widget =
        DRAFT_STEPS.createDefaultDesktopRootEnabledUndatedWidgetByContentManager(
            pageId,
            List.of(createdGeoGroup.getCode()));
    GEO_STEPS.deleteGeoGroupAssumingFailureDueToItsUsage(createdGeoGroup, pageId);
    GEO_STEPS.getExistingGeoGroup(createdGeoGroup);
  }

  @Test(dataProvider = "positiveDataProvider", priority = 2)
  private void unassignedFromValueGeoGroupDeletionNegativeTest(final GeoGroup geoGroup) {
    final var createdGeoGroup =
        GEO_STEPS.createGeoGroupAssumingSuccess(geoGroup);
    final var pageId =
        PAGES_STEPS.createEnabledPage();
    final var widget =
        DRAFT_STEPS.createDefaultDesktopRootEnabledUndatedWidgetByContentManager(
            pageId,
            List.of());
    DRAFT_STEPS.createPropertyWithValueByContentManager(
        pageId,
        widget,
        List.of(createdGeoGroup.getCode()));
    GEO_STEPS.deleteGeoGroupAssumingFailureDueToItsUsage(createdGeoGroup, pageId);
    GEO_STEPS.getExistingGeoGroup(createdGeoGroup);
  }

  /**
   * Test data provider.
   *
   * @return Test data
   */
  @DataProvider
  public static Object[][] positiveDataProvider() {
    return new Object[][] {
        {
            new GeoGroup.Builder()
                .setCode(randomAlphanumeric(5))
                .setName(randomAlphanumeric(5))
                .setDescription(randomAlphanumeric(5))
                .setCities(List.of("d76255c8-3173-4db5-a39b-badd3ebdf851"))
                .build()
        }
    };
  }
}
