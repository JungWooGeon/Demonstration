package com.police.demonstration;

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
}
