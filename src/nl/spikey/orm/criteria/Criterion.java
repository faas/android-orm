package nl.spikey.orm.criteria;

public interface Criterion
{
	/**
	 * Returns the full expression including property and a '?' at the location of an
	 * argument (ex. "price>=?").
	 */
	public String getExpression();

	/**
	 * Returns the arguments needed in the expression. Return null when there are no
	 * arguments (no '?' is present in the expression).
	 */
	public String getArgument();

}
