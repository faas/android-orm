package nl.spikey.orm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration
{
	private Map<String, Class< ? extends IdObject>> classes =
		new HashMap<String, Class< ? extends IdObject>>();

	private boolean foreignKeyEnabled = false;

	/**
	 * The session is used to call de database methodes
	 */
	private Session session;

	public Configuration(Session session)
	{
		this.setSession(session);
	}

	/**
	 * By using this methode you register a class to the orm. All classes that have the @Entity
	 * annotation should be registered.
	 */
	public void addAnnotatedClass(Class< ? extends IdObject> clazz)
	{
		classes.put(clazz.getSimpleName(), clazz);
	}

	public Map<String, Class< ? extends IdObject>> getClassMap()
	{
		return classes;
	}

	public Set<String> getClassNames()
	{
		return classes.keySet();
	}

	public Collection<Class< ? extends IdObject>> getClasses()
	{
		return classes.values();
	}

	public boolean isForeignKeyEnabled()
	{
		return foreignKeyEnabled;
	}

	/**
	 * Using this setting foreignkey's can be enabled, this will decrease the performance
	 * but can guarantee some constraints will not be violated.
	 * 
	 * TODO: [ORM] currently this foreignkey's are not supported
	 */
	public void setForeignKeyEnabled(boolean foreignKeyEnabled)
	{
		this.foreignKeyEnabled = foreignKeyEnabled;
	}

	public Session getSession()
	{
		return session;
	}

	/**
	 * Because the ORM has no knowledge of what type of database you use, you need to
	 * register a session(manager) which will provide the ORM with the needed database
	 * functions.
	 * 
	 * This will register the session(manager) to the ORM
	 */
	public void setSession(Session session)
	{
		this.session = session;
	}
}
