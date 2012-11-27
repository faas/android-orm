package nl.spikey.orm.criteria;

import java.util.List;

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
	public List<String> getArguments()
	{
		return criterion.getArguments();
	}
}
