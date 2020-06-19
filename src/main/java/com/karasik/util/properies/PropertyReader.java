package com.karasik.util.properies;

import java.util.ResourceBundle;

public class PropertyReader {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    public static String getProperty(String property) {
        return resourceBundle.getString(property);
    }
}
