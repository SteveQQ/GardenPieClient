package com.steveq.gardenpieclient.database;

import android.provider.BaseColumns;

/**
 * Created by Adam on 2017-08-05.
 */

public class SectionsContract {
    //explicitly make constructor private to prevent class to be instantiated
    private SectionsContract(){
        //intentionally empty}
    }

    public static class SectionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "sections_table";
        public static final String COLUMN_SECTION_NUM = "section_number";
        public static final String COLUMN_ACTIVE = "active";
    }

    public static class SectionsDaysEntry implements BaseColumns {
        public static final String TABLE_NAME = "sections_days_table";
        public static final String COLUMN_SECTION_ID = "section_id";
        public static final String COLUMN_DAY = "day";
    }

    public static class SectionsTimesEntry implements BaseColumns {
        public static final String TABLE_NAME = "sections_times_table";
        public static final String COLUMN_SECTION_ID = "section_id";
        public static final String COLUMN_TIME = "time";
    }
}
