package ru.alfabank.platform.helpers;

import ru.alfabank.platform.businessobjects.geofacade.GeoGroup;

public class GeoGroupHelper {

  public static final String RU =
      new GeoGroup.Builder()
          .setCode("ru")
          .build()
          .getCode();
  public static final String MSK_MO =
      new GeoGroup.Builder()
          .setCode("msk_mo")
          .build()
          .getCode();
  public static final String BEZ_MSK_MO =
      new GeoGroup.Builder()
          .setCode("bez_msk_mo")
          .build()
          .getCode();
  public static final String VLADIMIR =
      new GeoGroup.Builder()
          .setCode("vladimir")
          .build()
          .getCode();
}
