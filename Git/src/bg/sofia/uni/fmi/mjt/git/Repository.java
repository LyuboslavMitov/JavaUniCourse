package bg.sofia.uni.fmi.mjt.git;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Repository {
	//ovveride equals and hashcode for Branch
	//is map better here ? <String,Branch> 
	//maybe make it <String, Integer > where integer is the index of which the commits are available (removal of multiplication of identical commits? 
	private Map<String,Branch> branches=new HashMap<>();
	private Branch activeBranch;
	private Set<String> repoFiles;
	private int changedFiles=0;
	public Repository() {
		activeBranch=new Branch();
		branches.put(activeBranch.getName(),activeBranch);
		repoFiles = new HashSet<>();
	}
	public Result add(String... files) {
		StringBuilder message =new StringBuilder();
		List<String> listOfFiles=Arrays.asList(files);
		if(repoFiles.addAll(listOfFiles)) {
			message.append("added ");
			message.append(String.join(", ",listOfFiles));
			message.append(" to stage");
			changedFiles+=listOfFiles.size();
			//maybe store staged Files somewhere ?
			return new Result(true,message.toString());
		}
		else {
				HashSet<String> rollback = new HashSet<>(repoFiles);
				for (String file : listOfFiles) {
					if(!repoFiles.add(file)) {
						message.append("'")
							   .append(file)
							   .append("'")
							   .append(" already exists");
						repoFiles=rollback;
						return new Result(false,message.toString());
					}
				}
		return null;
		}
		
		
	}
	public Result commit(String message) {
		if(changedFiles==0) {
			return new Result(false,"nothing to commit, working tree clean");
		}
		StringBuilder resultMessage = new StringBuilder();
		resultMessage.append(changedFiles).append(" files changed");
		changedFiles=0;
		activeBranch.addCommit(new Commit("hash",message));
		return new Result(true, resultMessage.toString());
	}
	public Result remove(String... files) {
		StringBuilder message =new StringBuilder();
		List<String> listOfFiles=Arrays.asList(files);
		if(repoFiles.removeAll(listOfFiles)) {
			message.append("added ");
			message.append(String.join(", ",listOfFiles));
			message.append(" for removal");
			changedFiles-=listOfFiles.size();
			//maybe store staged Files somewhere ?
			return new Result(true,message.toString());
		}
		else {
			HashSet<String> rollback = new HashSet<>(repoFiles);
			for (String file : listOfFiles) {
				if(!repoFiles.remove(file)) {
					message.append("'")
						   .append(file)
						   .append("'")
						   .append(" did not match any files");
					repoFiles=rollback;
					return new Result(false,message.toString());
				}
			}
	return null;
	}
	}
	public Commit getHead() {
		return activeBranch.getHead();
	}
	/*public Result log() {
		
	}*/
	public String getBranch() {
		return activeBranch.getName();
	}
	public Result createBranch(String name) {
		Branch newBranch = new Branch(name,activeBranch.getCommits());
		StringBuilder resultMessage = new StringBuilder();
		if(branches.containsKey(name)) {
			resultMessage.append("branch ")
						 .append(name)
						 .append(" already exists");
			return new Result(false,resultMessage.toString());
		}
		branches.put(name,newBranch);
		return new Result(true," branch created");
	}
	public Result checkoutBranch(String name) {
		StringBuilder resultMessage = new StringBuilder();
		if(!branches.containsKey(name)) {
			resultMessage.append("branch ")
			 			 .append(name)
			 			 .append(" does not exist");
			return new Result(false,resultMessage.toString());
		}
		activeBranch=branches.get(name);
		return new Result(true,"switched to branch "+name);
		
	}
	/*public Result checkoutCommit(String hash) {
		
	}
	*/
}
