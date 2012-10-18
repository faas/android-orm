package nl.spikey.orm.engine;

public class ObjectNotSavedException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public ObjectNotSavedException(String detailMessage)
	{
		super(detailMessage);
	}

}
