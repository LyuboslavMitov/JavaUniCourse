package bg.sofia.uni.fmi.mjt.git;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class Branch {
	private String name;
	private List<Commit> commits = new ArrayList<>();
	private Set<String> stagedFiles= new HashSet<>();
	private Set<String> branchFiles = new HashSet<>();
	private int changedFiles=0;
	public Branch() {
		name="master";
	}
	public Branch(String branchName, List<Commit> commits,Set<String> branchFiles) {
		this.name=branchName;
		this.commits=new ArrayList<>(commits);
		this.branchFiles= new HashSet<>(branchFiles);
		
	}
	
	public Result stageFiles(List<String> listOfFiles) {
		
			StringBuilder message =new StringBuilder();
			String unsuccessfulFile = atomicAdd(listOfFiles);
			if(unsuccessfulFile==null) {
				message.append("added ");
				message.append(String.join(", ",listOfFiles));
				message.append(" to stage");
				changedFiles+=listOfFiles.size();
				stagedFiles.addAll(listOfFiles);
				return new Result(true,message.toString());
			}
			else {
					message.append("'")
							.append(unsuccessfulFile)
							.append("'")
							.append(" already exists");
					return new Result(false,message.toString());
					
			}
		
	}
	public int getNumberOfChangedFiles() {
		return changedFiles;
	}
	public void clearStagedFiles() {
		stagedFiles.clear();
		changedFiles=0;
	}
	public Result removeFiles(List<String> listOfFiles) {
		StringBuilder message =new StringBuilder();
		String unsuccessfulFile = atomicRemove(listOfFiles);
		if(unsuccessfulFile==null) {
			//with + size they the staged files are added to the changedFiles but that does not count so 
			//
			changedFiles=(changedFiles+listOfFiles.size())-howManyFilesNotCommited(listOfFiles)*2;
			
			stagedFiles.removeAll(listOfFiles);
			branchFiles.removeAll(listOfFiles);
			
			message.append("added ")
				   .append(String.join(", ",listOfFiles))
				   .append(" for removal");
			return new Result(true,message.toString());
		}
		else {
					message.append("'")
						   .append(unsuccessfulFile)
						   .append("'")
						   .append(" did not match any files");
					return new Result(false,message.toString());
				}

	}
	public Set<String> getBranchFiles() {
		return new HashSet<>(branchFiles);
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
		return new ArrayList<>(commits);
	}
	public void addCommit(Commit newCommit) {
		commits.add(newCommit);
		branchFiles.addAll(stagedFiles);
	}
	public boolean checkoutCommit(String hash) {
		int index = 0;
		for (Commit com: commits) {
			if(com.getHash().equals(hash)) {
				commits.subList(index+1,commits.size()).clear();;	
				return true;
			}
			index++;
		}
		return false;
		
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
	
	public Result log() {
		StringBuilder message = new StringBuilder();
		if(commits.isEmpty()) {
			message.append("branch ").append(name).append(" does not have any commits yet");
			return new Result(false,message.toString());
		}
		ListIterator<Commit> it = commits.listIterator(commits.size());
		Commit commit;
		while(it.hasPrevious()){
			commit = it.previous();
			message.append("commit ").append(commit.getHash()).append("\n")
			.append("Date: ").append(GitUtil.formatDate(commit.getDate())).append("\n\n\t")
			.append(commit.getMessage());
			if(it.hasPrevious())
				message.append("\n\n");
			
		}
		
		return new Result(true, message.toString());
	}
	
	private String atomicRemove(List<String> files) {
		for (String file : files) {
			if(!stagedFiles.contains(file) && !branchFiles.contains(file))
				return file;
		}
		return null;
	}
	private String atomicAdd(List<String> files) {
		for (String file : files) {
			if(stagedFiles.contains(file) || branchFiles.contains(file))
				return file;
		}
		return null;
	}
	private int howManyFilesNotCommited(List<String> files) {
		int result=0;
		for(String file: files) {
			if(stagedFiles.contains(file)) {
				result++;
			}
		}
		return result;
	}

}


