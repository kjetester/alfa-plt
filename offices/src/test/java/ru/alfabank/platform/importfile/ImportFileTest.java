package ru.alfabank.platform.importfile;

import com.epam.reportportal.annotations.ParameterKey;
import com.epam.reportportal.annotations.Step;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class ImportFileTest extends ImportBaseTest {

  @Test
  @Step
  public void importFileTest() {
    STEP.importFileAssumingSuccess();
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 1)
  @Step
  public void officeMappingTest(@ParameterKey("MS ID") final Integer masterSystemId,
                                @ParameterKey("PID") final String pid,
                                @ParameterKey("Mnemonic") final String mnemonic,
                                final Office expectedOffice) {
    STEP.checkOfficeMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 2)
  @Step
  public void locationMappingTest(@ParameterKey("MS ID") final Integer masterSystemId,
                                  @ParameterKey("PID") final String pid,
                                  @ParameterKey("Mnemonic") final String mnemonic,
                                  final Office expectedOffice) {
    STEP.checkOfficeLocationMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 3,
      enabled = false)
  @Step
  public void kindsMappingTest(@ParameterKey("MS ID") final Integer masterSystemId,
                               @ParameterKey("PID") final String pid,
                               @ParameterKey("Mnemonic") final String mnemonic,
                               final Office expectedOffice) {
    STEP.checkOfficeKindsMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 4,
      enabled = false)
  @Step
  public void servicesMappingTest(@ParameterKey("MS ID") final Integer masterSystemId,
                                  @ParameterKey("PID") final String pid,
                                  @ParameterKey("Mnemonic") final String mnemonic,
                                  final Office expectedOffice) {
    STEP.checkOfficeServicesMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 5)
  @Step
  public void listOfOperationsMappingTest(@ParameterKey("MS ID") final Integer masterSystemId,
                                          @ParameterKey("PID") final String pid,
                                          @ParameterKey("Mnemonic") final String mnemonic,
                                          final Office expectedOffice) {
    STEP.checkOfficeListOfOperationsMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 6)
  @Step
  public void changeDateTimeMappingTest(@ParameterKey("MS ID") final Integer masterSystemId,
                                        @ParameterKey("PID") final String pid,
                                        @ParameterKey("Mnemonic") final String mnemonic,
                                        final Office expectedOffice) {
    STEP.checkOfficeChangeDateTimeMapping(expectedOffice);
  }
}
