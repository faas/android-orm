package nl.spikey.orm.engine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nl.spikey.orm.Configuration;
import nl.spikey.orm.IdObject;
import nl.spikey.orm.Session;
import nl.spikey.orm.annotations.Column;
import nl.spikey.orm.annotations.Entity;
import nl.spikey.orm.annotations.Id;
import nl.spikey.orm.annotations.MappedSuperclass;
import nl.spikey.orm.criteria.Criteria;
import nl.spikey.orm.criteria.Criterion;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Orm
{
	private static final String DTYPE = "dtype";

	private static final String NULL = "null";

	private Configuration configuration;

	public Orm(Configuration configuration)
	{
		this.configuration = configuration;
	}

	private Session getSession()
	{
		if (configuration != null)
			return configuration.getSession();
		return null;
	}

	private static boolean isNull(String enumConst)
	{
		if (enumConst == null || enumConst.toLowerCase().equals(NULL.toLowerCase()))
			return true;
		return false;
	}

	private List<Field> getTableColumnFields(Class< ? > clazz)
	{
		List<Field> properties = new ArrayList<Field>();
		while (clazz.isAnnotationPresent(MappedSuperclass.class)
			|| clazz.isAnnotationPresent(Entity.class))
		{
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields)
			{
				if (field.isAnnotationPresent(Column.class))
				{
					properties.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return properties;
	}

	private <T extends IdObject> ContentValues getContentValuesForObject(T object)
	{
		ContentValues values = new ContentValues();
		values.put(DTYPE, object.getClass().getSimpleName());
		for (Field field : getTableColumnFields(object.getClass()))
		{
			if (field.isAnnotationPresent(Id.class))
				continue;

			boolean flag = field.isAccessible();
			field.setAccessible(true);

			try
			{
				Class< ? > typeClass = field.getType();
				if (typeClass.isEnum())
				{
					if (field.get(object) == null)
						values.putNull(getColumnName(field));
					else
						values.put(getColumnName(field), field.get(object).toString());
				}
				else if (typeClass.equals(Long.class) || typeClass.equals(long.class))
				{
					if (typeClass.equals(Long.class) && field.get(object) == null)
						values.putNull(getColumnName(field));
					else
						values.put(getColumnName(field), field.getLong(object));
				}
				else if (typeClass.equals(Integer.class) || typeClass.equals(int.class))
				{
					if (typeClass.equals(Integer.class) && field.get(object) == null)
						values.putNull(getColumnName(field));
					else
						values.put(getColumnName(field), field.getInt(object));
				}
				else if (typeClass.equals(Double.class) || typeClass.equals(double.class))
				{
					if (typeClass.equals(Double.class) && field.get(object) == null)
						values.putNull(getColumnName(field));
					else
						values.put(getColumnName(field), field.getDouble(object));
				}
				else if (typeClass.equals(String.class))
				{
					if (field.get(object) == null)
						values.putNull(getColumnName(field));
					else
						values.put(getColumnName(field), field.get(object).toString());
				}
				else if (typeClass.equals(Boolean.class) || typeClass.equals(boolean.class))
				{
					if (typeClass.equals(Boolean.class) && field.get(object) == null)
						values.putNull(getColumnName(field));
					else
						values.put(getColumnName(field), field.getBoolean(object));
				}
				else if (typeClass.equals(byte.class))
				{
					values.put(getColumnName(field), field.getByte(object));
				}
				else if (field.getDeclaringClass().isAssignableFrom(IdObject.class))
				{
					if (field.get(object) == null)
						values.putNull(getColumnName(field));
					else
					{
						IdObject entity = (IdObject) field.get(object);
						if (entity.getId() == null)
							throw new ObjectNotSavedException("object reference not saved");
						values.put(getColumnName(field), entity.getId());
					}
				}
			}
			catch (IllegalAccessException accEx)
			{
				Log.d(Orm.class.getSimpleName() + "(" + accEx.getStackTrace()[3].getLineNumber()
					+ ")", "illegal access: " + accEx.getMessage());
			}
			catch (IllegalArgumentException argEx)
			{
				Log.d(Orm.class.getSimpleName() + "(" + argEx.getStackTrace()[3].getLineNumber()
					+ ")", "illegal arguments: " + argEx.getMessage());
			}

			field.setAccessible(flag);
		}
		return values;
	}

	public Cursor selectQuery(Criteria criteria)
	{
		Class< ? extends IdObject> clazz = criteria.getClzz();
		if (!clazz.isAnnotationPresent(Entity.class))
			return null;
		String table = getTableName(clazz);

		String[] columns = null; // is all columns
		String selection = "";
		List<String> selectionArgs = new ArrayList<String>();
		// check if clazz is a subclass of the table class
		if (!table.equals(clazz.getSimpleName()))
		{
			// clazz and table are not the same, so clazz is a subclass of table, if clazz
			// has more subclasses than add these classes to DTYPE query.
			for (Class< ? > dtype : getSubClasses(clazz))
			{
				// als clazz nog subclasses heeft deze toevoegen aan dtype selection
				selection += DTYPE + "=? OR ";
				selectionArgs.add(dtype.getSimpleName());
			}
			selection = selection.substring(0, selection.length() - 4);
			selection += " AND ";
		}

		for (Criterion criterion : criteria.getCriterion())
		{
			selection += criterion.getExpression() + " AND ";
			if (criterion.getArguments() != null && !criterion.getArguments().isEmpty())
				selectionArgs.addAll(criterion.getArguments());
		}
		if (selection.length() > 4)
			selection = selection.substring(0, selection.length() - 5);

		String groupBy = null;
		String having = null;
		String orderBy = criteria.getOrderBy();
		String limit = criteria.getLimitString();

		return getSession().query(table, columns, selection, selectionArgs.toArray(new String[0]),
			groupBy, having, orderBy, limit);
	}

	private List<Class< ? >> getSubClasses(Class< ? > clazz)
	{
		List<Class< ? >> result = new ArrayList<Class< ? >>();
		result.add(clazz);

		for (Class< ? > clzz : configuration.getClasses())
		{
			if (clazz.equals(clzz.getSuperclass()))
			{
				result.addAll(getSubClasses(clzz));
			}
		}

		return result;
	}

	private String getColumnName(Field field)
	{
		if (!field.getAnnotation(Column.class).name().isEmpty())
			return field.getName();
		return field.getName();
	}

	private String getTableName(Class< ? > clazz)
	{
		if (clazz.getSuperclass().isAnnotationPresent(Entity.class))
			return getTableName(clazz.getSuperclass());
		return clazz.getSimpleName();
	}

	public int count(Criteria criteria)
	{
		Cursor cursor = selectQuery(criteria);
		int result = 0;
		if (cursor != null)
			result = cursor.getCount();
		getSession().close();
		return result;
	}

	public <T extends IdObject> List<T> list(Criteria criteria)
	{

		Cursor cursor = selectQuery(criteria);

		List<T> resultList = new ArrayList<T>();
		if (cursor != null && cursor.moveToFirst())
		{
			do
			{
				T object = fillObject(cursor);
				resultList.add(object);
			}
			while (cursor.moveToNext());

			cursor.close();
		}
		getSession().close();

		return resultList;
	}

	private <T extends IdObject> T fillObject(Cursor cursor)
	{
		T object = getNewInstanceOf(cursor.getString(cursor.getColumnIndex(DTYPE)));

		Class< ? > clzz = object.getClass();
		while (clzz.isAnnotationPresent(MappedSuperclass.class)
			|| clzz.isAnnotationPresent(nl.spikey.orm.annotations.Entity.class))
		{
			Field[] fields = clzz.getDeclaredFields();

			for (Field field : fields)
			{
				if (field.isAnnotationPresent(Column.class))
				{
					boolean flag = field.isAccessible();
					field.setAccessible(true);

					Class< ? > typeClass = field.getType();

					try
					{
						if (typeClass.isEnum())
						{
							String enumConst =
								cursor.getString(cursor.getColumnIndex(getColumnName(field)));
							if (enumConst != null && !isNull(enumConst))
							{
								@SuppressWarnings({"unchecked", "rawtypes"})
								Enum< ? > enumValue =
									Enum.valueOf((Class< ? extends Enum>) typeClass, enumConst);
								field.set(object, enumValue);
							}
						}
						else if (typeClass.equals(Long.class) || typeClass.equals(long.class))
						{
							Long longVar = null;
							if (typeClass.equals(Long.class))
							{
								String stringValue =
									cursor.getString(cursor.getColumnIndex(getColumnName(field)));
								if (!isNull(stringValue))
									longVar = Long.getLong(stringValue);
							}
							else
							{
								longVar =
									cursor.getLong(cursor.getColumnIndex(getColumnName(field)));
							}
							if (longVar != null)
								field.set(object, longVar);
						}
						else if (typeClass.equals(Integer.class) || typeClass.equals(int.class))
						{
							Integer intVar = null;
							if (typeClass.equals(Integer.class))
							{
								String stringValue =
									cursor.getString(cursor.getColumnIndex(getColumnName(field)));
								if (!isNull(stringValue))
									intVar = Integer.getInteger(stringValue);
							}
							else
							{
								intVar = cursor.getInt(cursor.getColumnIndex(getColumnName(field)));
							}
							if (intVar != null)
								field.setInt(object, intVar.intValue());
						}
						else if (typeClass.equals(Double.class) || typeClass.equals(double.class))
						{

							Double doubleVar = null;
							if (typeClass.equals(Double.class))
							{
								String stringValue =
									cursor.getString(cursor.getColumnIndex(getColumnName(field)));
								if (!isNull(stringValue))
									doubleVar = Double.valueOf(stringValue);
							}
							else
							{
								doubleVar =
									cursor.getDouble(cursor.getColumnIndex(getColumnName(field)));
							}
							if (doubleVar != null)
								field.setDouble(object, doubleVar.doubleValue());
						}
						else if (typeClass.equals(Float.class) || typeClass.equals(float.class))
						{

							Float floatVar = null;
							if (typeClass.equals(Float.class))
							{
								String stringValue =
									cursor.getString(cursor.getColumnIndex(getColumnName(field)));
								if (!isNull(stringValue))
									floatVar = Float.valueOf(stringValue);
							}
							else
							{
								floatVar =
									cursor.getFloat(cursor.getColumnIndex(getColumnName(field)));
							}
							if (floatVar != null)
								field.setFloat(object, floatVar.floatValue());
						}

						else if (typeClass.equals(String.class))
						{
							field.set(object,
								cursor.getString(cursor.getColumnIndex(getColumnName(field))));
						}
						else if (typeClass.equals(Boolean.class) || typeClass.equals(boolean.class))
						{
							Short boolVar = cursor.getShort(cursor.getColumnIndex(field.getName()));
							if (boolVar != null)
								field.setBoolean(object, boolVar == 0 ? false : true);
						}
						else if (typeClass.equals(byte.class))
						{
							byte[] byteVar = cursor.getBlob(cursor.getColumnIndex(field.getName()));
							if (byteVar != null && byteVar.length > 0)
								field.set(object, byteVar);
						}
						else if (field.getDeclaringClass().isAssignableFrom(IdObject.class))
						{
							IdObject entity = (IdObject) field.getDeclaringClass().newInstance();
							entity
								.setId(cursor.getLong(cursor.getColumnIndex(getColumnName(field))));
							entity.setNeedUpdate(true);
							field.set(object, entity);
						}
					}
					catch (IllegalAccessException accEx)
					{
						Log.d(
							Orm.class.getSimpleName() + "("
								+ accEx.getStackTrace()[3].getLineNumber() + ")",
							"illegal access: " + accEx.getMessage());
					}
					catch (IllegalArgumentException argEx)
					{
						Log.d(
							Orm.class.getSimpleName() + "("
								+ argEx.getStackTrace()[3].getLineNumber() + ")",
							"illegal arguments: " + argEx.getMessage());
					}
					catch (InstantiationException instEx)
					{
						Log.d(Orm.class.getSimpleName(),
							"can't creat new instance: " + instEx.getMessage());
					}
					field.setAccessible(flag);
				}
			}

			clzz = clzz.getSuperclass();
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	private <T extends IdObject> T getNewInstanceOf(String className)
	{
		Class< ? > clazz = configuration.getClassMap().get(className);
		if (clazz != null)
		{
			try
			{
				return (T) clazz.newInstance();
			}
			catch (IllegalAccessException accEx)
			{
				Log.d(Orm.class.getSimpleName() + "(" + accEx.getStackTrace()[3].getLineNumber()
					+ ")", "illegal access: " + accEx.getMessage());
			}
			catch (InstantiationException instEx)
			{
				Log.d(Orm.class.getSimpleName(), "can't creat new instance: " + instEx.getMessage());
			}
		}
		else
		{
			Log.d(Orm.class.getSimpleName(), "Class \"" + className + "\" is not registred.");
		}
		return null;
	}

	/**
	 * based on {@link: http://www.sqlite.org/datatype3.html#affinity} and {@link:
	 * http://www.sqlite.org/lang_createtable.html}
	 */
	private String generateColumnNameAndConstraint(Field field)
	{
		String query = getColumnName(field);
		Class< ? > typeClass = field.getType();
		if (typeClass.equals(Long.class) || typeClass.equals(long.class))
			query += " INTEGER";
		else if (typeClass.equals(Integer.class) || typeClass.equals(int.class))
			query += " INTEGER";
		else if (typeClass.equals(Double.class) || typeClass.equals(double.class))
			query += " REAL";
		else if (typeClass.equals(String.class))
			query += " TEXT";
		else if (typeClass.equals(Boolean.class) || typeClass.equals(boolean.class))
			query += " INTEGER";
		else if (typeClass.equals(byte.class))
			query += " BLOB";
		else if (typeClass.isEnum())
			query += " TEXT";
		else if (field.getDeclaringClass().isAssignableFrom(IdObject.class))
		{
			query += " INTEGER";
			// TODO ff uitzoeken hoe we die isForeignLKeyEnabled het beste aan kunnen
			// pakken.
			if (configuration.isForeignKeyEnabled())
				query += " REFERENCES " + getTableName(field.getDeclaringClass());
		}
		if (!field.getAnnotation(Column.class).nullable())
			query += " NOT NULL";
		if (field.getAnnotation(Column.class).unique())
			query += " UNIQUE";
		return query;
	}

	public void dropTable(String tableName)
	{
		String query = "DROP TABLE IF EXISTS " + tableName;
		getSession().execSQL(query);
	}

	/**
	 * Creates tables with as table name the first subclass annotated with @Entity of an @MappedSuperClass
	 * annotated class. Or the first subclass of IdObject if no @MappedSupperClass
	 * annotation is present.
	 * 
	 * It will only include all the columns of the entityClasses and there super classes
	 * all columns of the subclasses of entityClasses will have to be added manually using
	 * the addCollumnFor method.
	 */
	public void createTable(Class< ? extends IdObject> clazz)
	{
		String query = "CREATE TABLE " + getTableName(clazz) + "(" + DTYPE + " TEXT";
		for (Field field : getTableColumnFields(clazz))
		{
			query += ',';
			query += generateColumnNameAndConstraint(field);
			if (field.isAnnotationPresent(Id.class))
				query += " PRIMARY KEY";
		}
		query += ")";
		getSession().execSQL(query);
	}

	public void dropAndRecreateTable(Class< ? extends IdObject> clazz)
	{
		dropTable(clazz.getSimpleName());
		createTable(clazz);
	}

	public void addColumnFor(Class< ? extends IdObject> clazz, String propertyName)
	{
		String query = "ALTER TABLE " + getTableName(clazz) + " ADD COLUMN";

		for (Field field : getTableColumnFields(clazz))
		{
			if (field.getName().equals(propertyName))
			{
				query += generateColumnNameAndConstraint(field);
			}
		}
		getSession().execSQL(query);
	}

	// TODO misschien nog een rename column?

	/**
	 * This function deletes the object from the database. Keep in mind that objects
	 * referencing to this object will have to be deleted first, although this will not be
	 * checked unless you have enabled foreign keys (default disabled)
	 */
	public <T extends IdObject> boolean deleteObject(T object)
	{
		if (object == null || getSession() == null)
			return false;
		String whereClause = "";
		for (Field field : getTableColumnFields(object.getClass()))
		{
			if (field.isAnnotationPresent(Id.class))
				whereClause = getColumnName(field) + "=?";
		}
		int result =
			getSession().delete(getTableName(object.getClass()), whereClause,
				new String[] {object.getId().toString()});

		return (result == 1);
	}

	/**
	 * This function will save the object in the database which will then generate a id
	 * for the object.
	 * 
	 * Keep in mind that only this object will be saved if the object have references to
	 * other objects which are not jet saved you will have to save these objects first
	 * otherwise other wise it will result in a ObjectNotSavedException because the
	 * referenced object is not saved (has no id).
	 */
	public <T extends IdObject> boolean saveObject(T object)
	{
		if (object == null || getSession() == null)
			return false;
		Long result =
			getSession().insert(getTableName(object.getClass()), null,
				getContentValuesForObject(object));
		if (result != null)
			object.setId(result);

		return (result != null);
	}

	/**
	 * This function will update all the properties of this object in the database
	 * 
	 * Keep in mind that only this object will be updated if the object have references to
	 * other objects which are not jet saved you will have to save these objects first
	 * otherwise other wise it will result in a ObjectNotSavedException because the
	 * referenced object is not saved (has no id).
	 */
	public <T extends IdObject> boolean updateObject(T object)
	{
		if (object == null || getSession() == null)
			return false;
		String whereClause = "";
		for (Field field : getTableColumnFields(object.getClass()))
		{
			if (field.isAnnotationPresent(Id.class))
				whereClause = getColumnName(field) + "=?";
		}
		int result =
			getSession().update(getTableName(object.getClass()), getContentValuesForObject(object),
				whereClause, new String[] {object.getId().toString()});

		return (result == 1);
	}

	/**
	 * If the object has no id the object will be saved as a new object in the database,
	 * however when the object does have an id the object will be updated
	 * 
	 * Keep in mind that only this object will be updated/saved if the object have
	 * references to other objects which are not jet saved you will have to save these
	 * objects first otherwise other wise it will result in a ObjectNotSavedException
	 * because the referenced object is not saved (has no id).
	 */
	public <T extends IdObject> boolean saveOrUpdateObject(T object)
	{
		if (object == null || getSession() == null)
			return false;
		if (object.getId() != null && object.getId().longValue() > 0)
			return updateObject(object);
		else
			return saveObject(object);
	}
}
