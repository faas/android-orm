package nl.spikey.orm;


public interface IdObject
{
	/**
	 * This will have to return the Object Id annotated with @Id annotiation.
	 */
	public Long getId();

	/**
	 * The function to set the database id.
	 */
	public void setId(Long id);

	/**
	 * This will have to return state of the object if true the object has only an ID and
	 * still need to be loaded from the database if false the object is fully loaded from
	 * the database.
	 */
	public boolean needUpdate();

	/**
	 * The function to set the updateState of the object.
	 */
	public void setNeedUpdate(boolean updateState);
}
