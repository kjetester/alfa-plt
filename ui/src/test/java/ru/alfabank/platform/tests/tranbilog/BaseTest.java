package ru.alfabank.platform.tests.tranbilog;

import org.assertj.core.api.SoftAssertions;

public class BaseTest {

  protected static final String USER_AGENT = "Mozilla/5.0 (Win64; x64) AppleWebKit";
  protected static final String RECIPIENT_URL = "https://anketa.alfabank.ru/alfaform-common-land/";
  protected static final String REFERER_URL = "https://alfabank.ru/get-money/credit/credit-cash/";
  protected static final String EXPECTED_ERROR_MESSAGE = "Должно быть не пустое значение";

  protected SoftAssertions soft = new SoftAssertions();
}
