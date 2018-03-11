package com.javarank.syncpractice;

/**
 * Created by Anik on 3/10/2018.
 */

public class DBContract  {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;

     public static final String DATABASE_NAME = "sync_db";
     public static final String TABLE_NAME = "persons";
     public static final String ID = "id";
     public static final String FIRST_NAME = "first_name";
     public static final String LAST_NAME = "last_name";
     public static final String SYNC_STATUS = "sync_status";

     public static final String BASE_URL = "http://10.0.2.2/sync/";
     public static final String CREATE_PERSON_URL = BASE_URL+"insert.php";
     public static final String UI_UPDATE_BROADCAST = "my_broadcast";

}
