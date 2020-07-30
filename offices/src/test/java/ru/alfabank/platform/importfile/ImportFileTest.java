package ru.alfabank.platform.importfile;

import com.epam.reportportal.annotations.ParameterKey;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class ImportFileTest extends ImportBaseTest {

  @Test
  public void importFileTest() {
    STEP.importFileAssumingSuccess();
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 1)
  public void officeMappingTest(@ParameterKey("MSID") final Integer masterSystemId,
                                @ParameterKey("PID") final String pid,
                                @ParameterKey("Mnemonic") final String mnemonic,
                                final Office expectedOffice) {
    STEP.checkOfficeMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 2)
  public void locationMappingTest(@ParameterKey("MSID") final Integer masterSystemId,
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
  public void kindsMappingTest(@ParameterKey("MSID") final Integer masterSystemId,
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
  public void servicesMappingTest(@ParameterKey("MSID") final Integer masterSystemId,
                                  @ParameterKey("PID") final String pid,
                                  @ParameterKey("Mnemonic") final String mnemonic,
                                  final Office expectedOffice) {
    STEP.checkOfficeServicesMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 5)
  public void listOfOperationsMappingTest(@ParameterKey("MSID") final Integer masterSystemId,
                                          @ParameterKey("PID") final String pid,
                                          @ParameterKey("Mnemonic") final String mnemonic,
                                          final Office expectedOffice) {
    STEP.checkOfficeListOfOperationsMapping(expectedOffice);
  }

  @Test(
      dependsOnMethods = "importFileTest",
      dataProvider = "importedOffice",
      priority = 6)
  public void changeDateTimeMappingTest(@ParameterKey("MSID") final Integer masterSystemId,
                                        @ParameterKey("PID") final String pid,
                                        @ParameterKey("Mnemonic") final String mnemonic,
                                        final Office expectedOffice) {
    STEP.checkOfficeChangeDateTimeMapping(expectedOffice);
  }
}
