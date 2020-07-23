package ru.alfabank.platform.importfile;

import static ru.alfabank.platform.steps.offices.OfficesSteps.expectedOffices;

import org.testng.annotations.DataProvider;
import ru.alfabank.platform.BaseTest;

public class ImportBaseTest extends BaseTest {

  /**
   * Test data provider.
   * @return Test data
   */
  @DataProvider
  public Object[][] importedOffice() {
    final var offices = expectedOffices.getOffices();
    final var size = offices.size();
    final var res = new Object[size][4];
    for (int i = 0; i < size; i++) {
      final var office = offices.get(i);
      res[i][0] = office.getIdMasterSystem();
      res[i][1] = office.getPid();
      res[i][2] = office.getMnemonic();
      res[i][3] = office;
    }
    return res;
  }
}
