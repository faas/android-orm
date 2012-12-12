package nl.spikey.orm.engine;

public class UnKnownColumnTypeException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public UnKnownColumnTypeException(String detailMessage)
	{
		super(detailMessage);
	}
}
