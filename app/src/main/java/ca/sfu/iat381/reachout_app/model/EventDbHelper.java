package ca.sfu.iat381.reachout_app.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.widget.Toast;

/**
 * Created by pmadeya on 2017-04-03.
 */

public class EventDbHelper extends SQLiteOpenHelper {

    private Context context;


    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    DBConstants.TABLE_NAME + " (" +
                    DBConstants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBConstants.NAME + " TEXT, " +
                    DBConstants.LOCATION + " TEXT, " +
                    DBConstants.TIME + " TEXT, " +
                    DBConstants.VENUE + " TEXT, " +
                    DBConstants.LONGITUDE + " TEXT, " +
                    DBConstants.LATITUDE + " TEXT, " +
                    DBConstants.DESCRIPTION + " TEXT);" ;


    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME;

    public EventDbHelper(Context context){
        super (context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            //Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
