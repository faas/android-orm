package nl.spikey.orm.criteria;

class NotExpression implements Criterion
{

	private Criterion criterion;

	NotExpression(Criterion criterion)
	{
		this.criterion = criterion;
	}

	@Override
	public String getExpression()
	{
		return "NOT " + criterion.getExpression();
	}

	@Override
	public String getArgument()
	{
		return criterion.getArgument();
	}
}
