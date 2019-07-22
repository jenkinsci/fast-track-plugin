package nl.arnom.jenkins.fasttrack.rules;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildStartedByUserSortRuleTest {

	@Test
	public void equals() {
		BuildStartedByUserSortRule ruleA = new BuildStartedByUserSortRule();
		BuildStartedByUserSortRule ruleB = new BuildStartedByUserSortRule();

		Assert.assertTrue(ruleA.equals(ruleB));
	}
}