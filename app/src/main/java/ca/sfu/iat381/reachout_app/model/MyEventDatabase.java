package ca.sfu.iat381.reachout_app.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyEventDatabase {

    private SQLiteDatabase db;
    private Context context;
    private final EventDbHelper helper;

    public MyEventDatabase(Context c) {
        context = c;
        helper = new EventDbHelper(context);
    }

    public long insertData (String name, String location, String time,
                            String venue, String longitude, String latitude, String description)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.NAME, name);
        contentValues.put(DBConstants.LOCATION, location);
        contentValues.put(DBConstants.TIME, time);
        contentValues.put(DBConstants.VENUE, venue);
        contentValues.put(DBConstants.LONGITUDE, longitude);
        contentValues.put(DBConstants.LATITUDE, latitude);
        contentValues.put(DBConstants.DESCRIPTION, description);

        long id = db.insert(DBConstants.TABLE_NAME, null, contentValues);

        return id;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DBConstants.UID, DBConstants.NAME, DBConstants.LOCATION, DBConstants.TIME, DBConstants.VENUE,
        DBConstants.LONGITUDE, DBConstants.LATITUDE, DBConstants.DESCRIPTION};

        Cursor cursor = db.query(DBConstants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

}
