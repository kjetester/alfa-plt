package ru.alfabank.platform.importfile;

import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class ImportFileTest extends BaseTest {

  @Test
  public void importFileTest() {
    STEP.importFileAssumingSuccess();
    STEP.checkOfficesFromFileMapping();
    // STEP.checkKindsMapping();
    STEP.checkListOfOperationsFromFileMapping();
    STEP.checkLocationFromFileMapping();
    STEP.checkServicesFromFileMapping();
    STEP.checkChangeDateFromFileTimeMapping();
  }
}
