package bg.sofia.uni.fmi.jira;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.interfaces.IssueTracker;
import bg.sofia.uni.fmi.jira.issues.AbstractTask;
import bg.sofia.uni.fmi.jira.issues.Bug;
import bg.sofia.uni.fmi.jira.issues.Issue;

public class Jira implements IssueTracker {
	private Issue[] issues;
	
	public Jira(Issue[] issues) {
		this.issues=issues;
	}
	
	@Override
	public Issue[] findAll(Component component, IssueStatus status) {
		Issue[] result = new Issue[issues.length];
		int counter = 0;
		for(int i=0;i<issues.length;i++) {
			if(issues[i]!= null && issues[i].getComponentName().equals(component.getName()) && issues[i].getStatus()==status) {
				result[counter++]=issues[i];
			}
		}
		return result;
	}

	@Override
	public Issue[] findAll(Component component, IssuePriority priority) {
		Issue[] result = new Issue[issues.length];
		int counter = 0;
		for(int i=0;i<issues.length;i++) {
			if(issues[i]!= null && issues[i].getComponentName().equals(component.getName()) && issues[i].getPriority()==priority) {
				result[counter++]=issues[i];
			}
		}
		return result;
	}

	@Override
	public Issue[] findAll(Component component, IssueType type) {
		Issue[] result = new Issue[issues.length];
		int counter = 0;
		for(int i=0;i<issues.length;i++) {
			if(issues[i]!= null && issues[i].getComponentName().equals(component.getName()) && issues[i].getType()==type) {
				result[counter++]=issues[i];
			}
		}
		return result;
	}

	@Override
	public Issue[] findAll(Component component, IssueResolution resolution) {
		Issue[] result = new Issue[issues.length];
		int counter = 0;
		for(int i=0;i<issues.length;i++) {
			if(issues[i]!= null && issues[i].getComponentName().equals(component.getName()) && issues[i].getResolution()==resolution) {
				result[counter++]=issues[i];
			}
		}
		return result;
	}

	@Override
	public Issue[] findAllIssuesCreatedBetween(LocalDateTime startTime, LocalDateTime endTime) {
		Issue[] result = new Issue[issues.length];
		int counter = 0;
		for(int i=0;i<issues.length;i++) {
			if(issues[i]!= null && issues[i].getCreatedAt().isAfter(startTime) && issues[i].getCreatedAt().isBefore(endTime)) {
				result[counter++]=issues[i];
			}
		}	
		return result;
	}

	@Override
	public Issue[] findAllBefore(LocalDateTime dueTime) {
		Issue[] result = new Issue[issues.length];
		int counter = 0;
		for(int i=0;i<issues.length;i++) {
			if(issues[i]!= null && (issues[i].getType()==IssueType.TASK || issues[i].getType()==IssueType.NEW_FEATURE )&& LocalDateTime.now().isBefore(dueTime)) {
				AbstractTask c = (AbstractTask)issues[i];
				if(c.getDueTime().isBefore(dueTime))
					result[counter++]=issues[i];
			}
		}
		return result;
	}

}
