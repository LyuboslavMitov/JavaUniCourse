package bg.sofia.uni.fmi.jira.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import bg.sofia.uni.fmi.jira.Component;
import bg.sofia.uni.fmi.jira.Jira;
import bg.sofia.uni.fmi.jira.User;
import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.interfaces.IssueTracker;
import bg.sofia.uni.fmi.jira.issues.Bug;
import bg.sofia.uni.fmi.jira.issues.Issue;
import bg.sofia.uni.fmi.jira.issues.Task;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidReporterException;

public class SampleIssueTrackerTest {

	private static Issue[] issues;

	private static IssueTracker issueTracker;

	private static User ivan;

	private static User pesho;

	private static Component peshosComponent;

	@BeforeClass
	public static void setUp() throws InvalidReporterException {
		ivan = new User("Ivan");
		pesho = new User("Pesho");
		peshosComponent = new Component("Pesho", "pc", pesho);
		issues = new Issue[3];
		issues[0] = new Bug(IssuePriority.MAJOR, peshosComponent, ivan, "Big Bug");
		issues[1] = new Bug(IssuePriority.CRITICAL, peshosComponent, ivan, "Bigger Bug");
		issues[2]=new Task(IssuePriority.MINOR, peshosComponent, ivan, "idgaf", LocalDateTime.now().plusMonths(1));
		issueTracker = new Jira(issues);
	}

	@Test
	public void testFindAllByStatus() {
		Issue[] result = issueTracker.findAll(peshosComponent, IssueStatus.OPEN);

		assertEquals(3, getIssues(result));
	}

	@Test
	public void testFindAllByResolution() {
		Issue[] result = issueTracker.findAll(peshosComponent, IssueResolution.UNRESOLVED);

		assertEquals(3, getIssues(result));
	}
	private int getIssues(Issue[] res) {
		int c=0;
		for(int i=0;i<res.length;i++) {
			if(res[i]!=null)
				c++;
		}
		return c;
	}
	@Test
	public void testFindAllByType() {
		Issue[] result = issueTracker.findAll(peshosComponent, IssueType.TASK);
		
		assertEquals(1, getIssues(result));
	}
	@Test
	public void testFindAllByTypeZero() {
		Issue[] result = issueTracker.findAll(peshosComponent, IssueType.NEW_FEATURE);
		
		assertEquals(0, getIssues(result));
	}
	@Test
	public void testId() {
		String actual = issues[0].getId();
		String expected = peshosComponent.getShortName()+"-"+1;
		assertEquals(expected,actual);
		actual = issues[1].getId();
		expected=peshosComponent.getShortName()+"-"+2;
		assertEquals(expected, actual);
	}
}
