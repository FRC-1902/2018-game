package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.controllers.LogitechController;
import com.explodingbacon.bcnlib.framework.AbstractOI;

public class OI extends AbstractOI {

    public static LogitechController driver;

    public OI() {
        driver = new LogitechController(0);
    }
}
