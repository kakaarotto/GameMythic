package com.gm.config;

/**
 * Application constants.
 */
public final class Constants {
    public static final String USERNAME_REGEX = "^[_'.@A-Za-z0-9-]*$";

    // User default avatar
    public final static String USER_DEFAULT_PHOTO = "https://qiniu.easyapi.com/user/default.jpg";

    // 1 followed，2 liked，3 commented，4 content
    public final static int MESSAGE_followed = 1;
    public final static int MESSAGE_liked = 2;
    public final static int MESSAGE_commented = 3;
    public final static int MESSAGE_content = 4;
}
