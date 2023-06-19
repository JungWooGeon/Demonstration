package com.police.demonstration;

import java.util.Map;

/**
 * 상수 관리 class
 */
public class Constants {
    public static final int TIME_ZONE_DAY = 0;
    public static final int TIME_ZONE_NIGHT = 1;
    public static final int TIME_ZONE_LATE_NIGHT = 2;

    public static final int PLACE_ZONE_HOME = 0;
    public static final int PLACE_ZONE_PUBLIC = 1;
    public static final int PLACE_ZONE_ETC = 2;

    public static final String INTENT_NAME_NAME_DETAIL = "name";
    public static final String INTENT_NAME_PHONE_NUMBER_DETAIL = "number";
    public static final String INTENT_NAME_POSITION_DETAIL = "position";

    public final static int DATE_DETAIL_START_DATE_IDX = 0;
    public final static int DATE_DETAIL_END_DATE_IDX = 1;

    public final static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd-hh-mm";
    public final static String YEAR_DATE_FORMAT = "yyyy";

    public final static int STATUS_ING = 0;
    public final static int STATUS_PRE = 1;
    public final static int STATUS_POST = 2;

    public static final String INTENT_NAME_IS_ADD_BACKGROUND_NOISE = "isAddBackgroundNoise";
    public static final String INTENT_NAME_PARCELABLE_DEMONSTRATION = "parcelableDemonstration";

    public static final int DEFAULT_EQUIVALENT_NOISE_DAY_HOME = 65;
    public static final int DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME = 60;
    public static final int DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME = 55;
    public static final int DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC = 65;
    public static final int DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC = 60;
    public static final int DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC = 60;
    public static final int DEFAULT_EQUIVALENT_NOISE_DAY_ETC = 75;
    public static final int DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC = 65;
    public static final int DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC = 65;

    public static final int DEFAULT_HIGHEST_NOISE_DAY_HOME = 85;
    public static final int DEFAULT_HIGHEST_NOISE_NIGHT_HOME = 80;
    public static final int DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME = 75;
    public static final int DEFAULT_HIGHEST_NOISE_DAY_PUBLIC = 85;
    public static final int DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC = 80;
    public static final int DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC = 80;
    public static final int DEFAULT_HIGHEST_NOISE_ETC = 95;

    public static final String INTENT_NAME_EQUIVALENT_NOISE = "equivalentNoise";
    public static final String INTENT_NAME_HIGHEST_NOISE = "highestNoise";

    public static final String INTENT_NAME_PARCELABLE_MEASUREMENT = "parcelableMeasurement";

    // 소음도 보정치 기준표
    public static Map<Double, Double> STANDARD_CORRECTION_NOISE = Map.<Double, Double>ofEntries(
            Map.entry(3.0, -3.0), Map.entry(3.1, -2.9),
            Map.entry(3.2, -2.8), Map.entry(3.3, -2.7),
            Map.entry(3.4, -2.7), Map.entry(3.5, -2.6),
            Map.entry(3.6, -2.5), Map.entry(3.7, -2.4),
            Map.entry(3.8, -2.3), Map.entry(3.9, -2.3),
            Map.entry(4.0, -2.2), Map.entry(4.1, -2.1),
            Map.entry(4.2, -2.1), Map.entry(4.3, -2.0),
            Map.entry(4.4, -2.0), Map.entry(4.5, -1.9),
            Map.entry(4.6, -1.8), Map.entry(4.7, -1.8),
            Map.entry(4.8, -1.7), Map.entry(4.9, -1.7),
            Map.entry(5.0, -1.7), Map.entry(5.1, -1.6),
            Map.entry(5.2, -1.6), Map.entry(5.3, -1.5),
            Map.entry(5.4, -1.5), Map.entry(5.5, -1.4),
            Map.entry(5.6, -1.4), Map.entry(5.7, -1.4),
            Map.entry(5.8, -1.3), Map.entry(5.9, -1.3),
            Map.entry(6.0, -1.3), Map.entry(6.1, -1.2),
            Map.entry(6.2, -1.2), Map.entry(6.3, -1.2),
            Map.entry(6.4, -1.1), Map.entry(6.5, -1.1),
            Map.entry(6.6, -1.1), Map.entry(6.7, -1.0),
            Map.entry(6.8, -1.0), Map.entry(6.9, -1.0),
            Map.entry(7.0, -1.0), Map.entry(7.1, -0.9),
            Map.entry(7.2, -0.9), Map.entry(7.3, -0.9),
            Map.entry(7.4, -0.9), Map.entry(7.5, -0.9),
            Map.entry(7.6, -0.8), Map.entry(7.7, -0.8),
            Map.entry(7.8, -0.8), Map.entry(7.9, -0.8),
            Map.entry(8.0, -0.7), Map.entry(8.1, -0.7),
            Map.entry(8.2, -0.7), Map.entry(8.3, -0.7),
            Map.entry(8.4, -0.7), Map.entry(8.5, -0.7),
            Map.entry(8.6, -0.6), Map.entry(8.7, -0.6),
            Map.entry(8.8, -0.6), Map.entry(8.9, -0.6),
            Map.entry(9.0, -0.6), Map.entry(9.1, -0.6),
            Map.entry(9.2, -0.6), Map.entry(9.3, -0.5),
            Map.entry(9.4, -0.5), Map.entry(9.5, -0.5),
            Map.entry(9.6, -0.5), Map.entry(9.7, -0.5),
            Map.entry(9.8, -0.5), Map.entry(9.9, -0.5)
    );
}
