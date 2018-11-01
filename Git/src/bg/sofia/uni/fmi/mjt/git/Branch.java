package bg.sofia.uni.fmi.mjt.git;

import java.util.LinkedList;
import java.util.List;

public class Branch {
	private String name;
	private List<Commit> commits = new LinkedList<>();
	public Branch() {
		name="master";
	}
	public Branch(String branchName, List<Commit> commits) {
		this.name=branchName;
	}
	

	public String getName() {
		return name;
	}
	public Commit getHead() {
		if(commits.size()>0)
			return commits.get(commits.size()-1);
		return null;
	}
	public List<Commit> getCommits(){
		return commits;
	}
	public void addCommit(Commit newCommit) {
		commits.add(newCommit);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commits == null) ? 0 : commits.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Branch other = (Branch) obj;
		if (commits == null) {
			if (other.commits != null)
				return false;
		} else if (!commits.equals(other.commits))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
