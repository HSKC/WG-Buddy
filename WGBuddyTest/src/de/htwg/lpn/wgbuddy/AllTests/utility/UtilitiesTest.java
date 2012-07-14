package de.htwg.lpn.wgbuddy.AllTests.utility;

import android.test.AndroidTestCase;
import de.htwg.lpn.wgbuddy.utility.Utilities;

public class UtilitiesTest extends AndroidTestCase
{
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testgetHighLowCharacter()
	{
		String testString = "hallo";

		assertEquals("Hallo", Utilities.getHighLowCharacter(testString));
	}

	public void testcheckOnlyAllowedCharacterTrue()
	{
		String testString = "abcdef";

		boolean erg = Utilities.checkOnlyAllowedCharacter(testString);

		assertEquals(true, erg);
	}

	public void testcheckOnlyAllowedCharacterFalse()
	{
		String testString = "_";

		boolean erg = Utilities.checkOnlyAllowedCharacter(testString);

		assertEquals(false, erg);
	}
}
