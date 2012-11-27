package nl.spikey.orm.criteria;

import java.util.ArrayList;
import java.util.List;

public class LogicalExpression implements Criterion
{

	private final Criterion criterion1;

	private final Criterion criterion2;

	private final String operator;

	protected LogicalExpression(Criterion criterion1, Criterion criterion2, String operator)
	{
		this.criterion1 = criterion1;
		this.criterion2 = criterion2;
		this.operator = operator;
	}

	@Override
	public String getExpression()
	{
		return "(" + criterion1.getExpression() + " " + operator + " " + criterion2.getExpression()
			+ ")";
	}

	@Override
	public List<String> getArguments()
	{
		List<String> arguments = new ArrayList<String>();
		if (criterion1.getArguments() != null)
			arguments.addAll(criterion1.getArguments());
		if (criterion2.getArguments() != null)
			arguments.addAll(criterion2.getArguments());

		return arguments;
	}
}
