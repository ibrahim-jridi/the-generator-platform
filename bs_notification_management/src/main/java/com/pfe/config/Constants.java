package com.pfe.config;

import java.util.UUID;

/**
 * Application constants.
 */
public final class Constants {

  public static final String SYSTEM = "system";
  public static final String USER_ID = "user_id";
  public static UUID ROOT_ID;
  public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";

  private Constants() {
  }

  public static UUID getRootId() {
    return ROOT_ID;
  }

  public static void setRootId(UUID rootId) {
    ROOT_ID = rootId;
  }
}
