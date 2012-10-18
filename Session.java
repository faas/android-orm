package nl.spikey.orm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This interface should be implemented by an class extending from SQLiteDatabase.
 * 
 * @author faas
 */
public interface Session
{
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit);

	public void close();

	public void execSQL(String sql);

	public int delete(String table, String whereClause, String[] whereArgs);

	public long insert(String table, String nullColumnHack, ContentValues values);

	public int update(String table, ContentValues values, String whereClause, String[] whereArgs);

}
