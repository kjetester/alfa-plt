package ru.alfabank.platform.importfile;

import static ru.alfabank.platform.steps.offices.OfficesSteps.expectedOffices;

import com.epam.reportportal.annotations.Step;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;

public class ImportFileTest extends BaseTest {

  @Test
  @Step
  public void importFileTest() {
    STEP.importFileAssumingSuccess();
  }

  @Test(dependsOnMethods = "importFileTest", priority = 1)
  @Step
  public void officeMappingTest() {
    STEP.checkOfficeMapping(expectedOffices);
  }

  @Test(dependsOnMethods = "importFileTest", priority = 2)
  @Step
  public void locationMappingTest() {
    STEP.checkLocationMapping(expectedOffices);
  }

  @Test(dependsOnMethods = "importFileTest", priority = 3, enabled = false)
  @Step
  public void kindsMappingTest() {
    STEP.checkKindsMapping(expectedOffices);
  }

  @Test(dependsOnMethods = "importFileTest", priority = 4, enabled = false)
  @Step
  public void servicesMappingTest() {
    STEP.checkServicesMapping(expectedOffices);
  }

  @Test(dependsOnMethods = "importFileTest", priority = 5)
  @Step
  public void listOfOperationsMappingTest() {
    STEP.checkListOfOperationsMapping(expectedOffices);
  }

  @Test(dependsOnMethods = "importFileTest", priority = 6)
  @Step
  public void changeDateTimeMappingTest() {
    STEP.checkChangeDateTimeMapping(expectedOffices);
  }
}
