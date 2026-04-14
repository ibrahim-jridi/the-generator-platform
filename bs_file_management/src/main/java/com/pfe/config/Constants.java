package com.pfe.config;

import java.util.UUID;

/**
 * Application constants.
 */
public final class Constants {

  public static final String SYSTEM = "system";
  public static final String USER_ID = "user_id";

  public static final String BS_ROOT = "BS_ROOT";
  public static UUID ROOT_ID;
  public static final String BUCKET_NAME = "bs-bucket";
  public static final String BUCKET_CONFIG = "config";
  public static final String FOLDER_TEMPLATES = "templates";

  private Constants() {
  }

  public static UUID getRootId() {
    return ROOT_ID;
  }

  public static void setRootId(UUID rootId) {
    ROOT_ID = rootId;
  }
}
