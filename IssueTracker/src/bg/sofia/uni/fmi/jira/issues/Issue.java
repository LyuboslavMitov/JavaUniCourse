package bg.sofia.uni.fmi.jira.issues;

import java.time.LocalDateTime;


import bg.sofia.uni.fmi.jira.Component;
import bg.sofia.uni.fmi.jira.User;
import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.interfaces.IIssue;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidReporterException;

public abstract class Issue implements IIssue {
	private IssuePriority priority;
	private Component component;
	private User reporter;
	private String description;
	private IssueResolution resolution;
	private IssueStatus status;
	private IssueType type;
	private LocalDateTime created;
	private LocalDateTime modified;
	private static int Id=1;
	
	public Issue(IssuePriority priority, Component component, User reporter, String description) throws InvalidReporterException  {
		if(reporter==null) {
			throw new InvalidReporterException();
		}
		if(priority==null || component ==null || description==null) {
			throw new IllegalArgumentException("One of the constructor arguments is null");
		}
		this.priority=priority;
		this.component=component;
		this.reporter=reporter;
		this.description=description;
		resolution=IssueResolution.UNRESOLVED;
		status = IssueStatus.OPEN;
		created = LocalDateTime.now();
		modified= LocalDateTime.now();
		
	}
	public IssueStatus getStatus() {
		return status;
	}
	
	public IssuePriority getPriority() {
		return priority;
	}
	public IssueResolution getResolution() {
		return resolution;
	}
	public LocalDateTime getCreatedAt() {
		return created;
	}
	public LocalDateTime getLastModifiedAt() {
		return modified;
	}
	public String getComponentName() {
		return component.getName();
	}
	@Override
	public String getId() {
		
		return component.getShortName() + "-" + Id++;
	}

	@Override
	public void resolve(IssueResolution resolution) {
		this.resolution=resolution;
		modified=LocalDateTime.now();
	}

	@Override
	public void setStatus(IssueStatus status) {
		this.status=status;
		modified=LocalDateTime.now();
	}
	public abstract IssueType getType() ;
	
	
}
