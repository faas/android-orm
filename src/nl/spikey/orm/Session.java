package nl.spikey.orm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This interface contains the used sqlite database calls, most common use will be to
 * implement this interface in a wrapper class, which passes the calls through to the
 * sqlite implementation class.
 * 
 * @author faas
 */
public interface Session
{
	/**
	 * Calls a query at the database and returns a Cursor to the database.
	 * 
	 * NOTE: this is the only method that should not be closed by the implementing class.
	 * The ORM will close the database when the Returning Cursor isn't beeing used
	 * anymore.
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit);

	public Cursor rawQuery(String sql, String[] selectionArgs);

	public void execSQL(String sql);

	public void close();

	public int delete(String table, String whereClause, String[] whereArgs);

	public long insert(String table, String nullColumnHack, ContentValues values);

	public int update(String table, ContentValues values, String whereClause, String[] whereArgs);

}
