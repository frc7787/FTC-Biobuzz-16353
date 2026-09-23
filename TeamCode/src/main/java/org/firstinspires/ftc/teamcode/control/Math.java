package org.firstinspires.ftc.teamcode.control;

public class Math {
    public static Double MaxOf(Double minimum, Double input) {
        if (minimum > input) return minimum;
        else return input;
    }

    public static Double CoerceIn(Double minimum, Double input, Double maximum) {
        if (minimum >= input) return minimum;
        else if (maximum <= input) return maximum;
        else return input;
    }

}