package ru.alfabank.platform.businessobjects.offices;

import java.util.List;
import ru.alfabank.platform.businessobjects.AbstractBusinessObject;
import ru.alfabank.platform.businessobjects.offices.Offices.Office;

public class FileImportResponse extends AbstractBusinessObject {

  Integer successCount;
  Integer errorCount;
  List<Office> errorDetails;

  public List<Office> getErrorDetails() {
    return errorDetails;
  }
}
