package ru.alfabank.platform.buisenessobjects;

import org.apache.log4j.*;

public class Resources {

  private static final Logger LOGGER = LogManager.getLogger(Resources.class);

  private String url;

  public String getName() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
