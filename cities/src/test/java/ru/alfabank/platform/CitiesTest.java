package ru.alfabank.platform;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.testng.annotations.Test;
import ru.alfabank.platform.businessobjects.cities.Cities;

public class CitiesTest extends BaseTest {

  @Test(dataProvider = "positiveDataProvider")
  public void getCitiesPositiveTest(final Map<String, ?> queryParamsList,
                                    final Cities expected) {
    final var result = CITIES_STEPS.getCitiesList(queryParamsList);
    assertThat(result.getStatusCode()).isEqualTo(SC_OK);
    result.as(Cities.class).equals(expected);
  }


  @Test(dataProvider = "negativeDataProvider")
  public void getCitiesNegativeTest(final Map<String, ?> queryParamsList) {
    final var result = CITIES_STEPS.getCitiesList(queryParamsList);
    assertThat(result.getStatusCode()).isGreaterThan(SC_BAD_REQUEST);
    assertThat(result.as(Cities.class).getCities()).isNullOrEmpty();
  }
}
