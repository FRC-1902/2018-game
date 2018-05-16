package com.explodingbacon.powerup.core.positioning;

public class Constants {

    //Wheel Stuff
    public static double wheelDiameterInches = 6;

    public static double kTrackLengthInches = 27;
    public static double kTrackWidthInches = 33;
    public static double kTrackEffectiveDiameter = (kTrackWidthInches * kTrackWidthInches + kTrackLengthInches * kTrackLengthInches) / kTrackWidthInches;
    public static double kTrackScrubFactor = 1; //0.5

    public static double pathFollowLookahead = 24.0; // inches
    public static double pathFollowMaxVel = 120.0; // inches/sec
    public static double pathFollowMaxAccel = 80.0; // inches/sec^2

    public static double kLooperDt = 0.01;

}
