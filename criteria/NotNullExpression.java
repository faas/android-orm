package nl.spikey.orm.criteria;

class NotNullExpression implements Criterion
{
	private String property;

	NotNullExpression(String property)
	{
		this.property = property;
	}

	@Override
	public String getExpression()
	{
		return property + " IS NOT NULL";
	}

	@Override
	public String getArgument()
	{
		return null;
	}
}
