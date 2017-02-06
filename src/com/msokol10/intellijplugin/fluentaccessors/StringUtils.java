package com.msokol10.intellijplugin.fluentaccessors;

public class StringUtils {

    public static String capitalizeFirstLetter (String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
