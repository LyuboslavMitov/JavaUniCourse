package bg.sofia.uni.fmi.mjt.git;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Repository {
	//ovveride equals and hashcode for Branch
	//is map better here ? <String,Branch> 
	//maybe make it <String, Integer > where integer is the index of which the commits are available (removal of multiplication of identical commits? 
	private Map<String,Branch> branches=new HashMap<>();
	private Branch activeBranch;
	
	public Repository() {
		activeBranch=new Branch();
		branches.put(activeBranch.getName(),activeBranch);
	}
	
	public Result add(String... files) {
		return activeBranch.stageFiles(Arrays.asList(files));
	}
	
	public Result commit(String message){
		int changedFiles = activeBranch.getNumberOfChangedFiles();
		if(changedFiles==0) {
			return new Result(false,"nothing to commit, working tree clean");
		}
		StringBuilder resultMessage = new StringBuilder();
		resultMessage.append(changedFiles).append(" files changed");
		LocalDateTime commitDate= LocalDateTime.now();
		try {
		activeBranch.addCommit(new Commit(GitUtil.hexDigest(GitUtil.formatDate(commitDate)+message),message,commitDate));
		}
		catch (NoSuchAlgorithmException e) {
			return new Result(false, "There is no SHA-1 algorithm");
		}
		activeBranch.clearStagedFiles();
		return new Result(true, resultMessage.toString());
	}
	
	public Result remove(String... files) {
		return activeBranch.removeFiles(Arrays.asList(files));
	}
	
	public Commit getHead() {
		return activeBranch.getHead();
	}
	
	public Result log() {
		return activeBranch.log();
		
	}
	
	public String getBranch() {
		return activeBranch.getName();
	}
	
	public Result createBranch(String name) {
		
		StringBuilder resultMessage = new StringBuilder();
		if(branches.containsKey(name)) {
			resultMessage.append("branch ")
						 .append(name)
						 .append(" already exists");
			return new Result(false,resultMessage.toString());
		}
		Branch newBranch = new Branch(name,activeBranch.getCommits(),activeBranch.getBranchFiles());
		branches.put(name,newBranch);
		resultMessage.append("created branch ").append(name);
		return new Result(true,resultMessage.toString());
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
	
	public Result checkoutCommit(String hash) {
		StringBuilder message = new StringBuilder();
		if(activeBranch.checkoutCommit(hash)) {
			message.append("HEAD is now at ").append(hash);
			return new Result(true,message.toString());
		}
		message.append("commit ").append(hash).append(" does not exist");
		return new Result(false, message.toString());
	}
	
}
