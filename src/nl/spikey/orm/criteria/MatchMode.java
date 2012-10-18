package nl.spikey.orm.criteria;

/**
 * Represents an strategy for matching strings using "like".
 * 
 * @author Hylke Faas
 */
public enum MatchMode
{

	/**
	 * Match the entire string to the pattern
	 */
	EXACT
	{
		@Override
		public String stringToMatch(String pattern)
		{
			return pattern;
		}
	},

	/**
	 * Match the start of the string to the pattern
	 */
	START
	{
		@Override
		public String stringToMatch(String pattern)
		{
			return pattern + '%';
		}
	},

	/**
	 * Match the end of the string to the pattern
	 */
	END
	{
		@Override
		public String stringToMatch(String pattern)
		{
			return '%' + pattern;
		}
	},

	/**
	 * Match the pattern anywhere in the string
	 */
	ANYWHERE
	{
		@Override
		public String stringToMatch(String pattern)
		{
			return '%' + pattern + '%';
		}
	};

	/**
	 * convert the pattern, by appending/prepending "%"
	 */
	public abstract String stringToMatch(String pattern);

}
