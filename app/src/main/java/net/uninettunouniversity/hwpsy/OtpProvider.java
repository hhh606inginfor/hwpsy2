package net.uninettunouniversity.hwpsy;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class OtpProvider extends ContentProvider {

    static final String PROVIDER_NAME = "net.uninettunouniversity.hwpsy.OtpProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/otpbucket";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    public static final String OTP = "otp";
    public static final String TIMESTAMP = "timestamp";

    private static HashMap<String, String> OTPBUCKET_PROJECTION_MAP;

    static final int OTPBUCKET = 1;
    static final int OTPBUCKET_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "otpbucket", OTPBUCKET);
        uriMatcher.addURI(PROVIDER_NAME, "otpbucket/#", OTPBUCKET_ID);
    }

    private SQLiteDatabase db;

    static final String DATABASE_NAME = "MyOtpBucket";
    static final String OTPBUCKET_TABLE_NAME = "otpbucket";
    static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE =
            "CREATE TABLE " + OTPBUCKET_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " otp TEXT NOT NULL," +
                    " timestamp TEXT NOT NULL);";


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d("-->","onCreate");

            db.execSQL(CREATE_DB_TABLE);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("-->","onUpgrade");
            db.execSQL("DROP TABLE IF EXISTS " + OTPBUCKET_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Log.d("-->","onCreate2");
        Context context = getContext();
        try (DatabaseHelper dbHelper = new DatabaseHelper(context)) {

            db = dbHelper.getWritableDatabase();
        }

        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        open();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(OTPBUCKET_TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case OTPBUCKET:
                qb.setProjectionMap(OTPBUCKET_PROJECTION_MAP);
                break;
            case OTPBUCKET_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if (sortOrder == null || sortOrder == "") {
            sortOrder = TIMESTAMP;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        close();
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case OTPBUCKET:
                return "vnd.android.cursor.dir/vnd.uninettuno.otpbucket";
            case OTPBUCKET_ID:
                return "vnd.android.cursor.item/vnd.otpbucket";
            default:
                throw new IllegalArgumentException("Unsupported uri " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        open();
        Long rowID = db.insert(OTPBUCKET_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
close();
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case OTPBUCKET:
                count = db.delete(OTPBUCKET_TABLE_NAME, selection, selectionArgs);
                break;
            case OTPBUCKET_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(OTPBUCKET_TABLE_NAME, _ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case OTPBUCKET:
                count = db.update(OTPBUCKET_TABLE_NAME, values, selection, selectionArgs);
                break;
            case OTPBUCKET_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(OTPBUCKET_TABLE_NAME, values, _ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /** For OPEN database **/
    public synchronized DatabaseHelper open() throws SQLiteException {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

            db = dbHelper.getWritableDatabase();

        return dbHelper;
    }

    /** For CLOSE database **/
    public void close() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.close();
    }
}
