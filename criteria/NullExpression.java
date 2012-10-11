package nl.spikey.orm.criteria;

class NullExpression implements Criterion
{
	private String property;

	NullExpression(String property)
	{
		this.property = property;
	}

	@Override
	public String getExpression()
	{
		return property + " IS NULL";
	}

	@Override
	public String getArgument()
	{
		return null;
	}
}
