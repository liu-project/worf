package orj.worf.rtx.persistence;

import java.util.Set;

import orj.worf.rtx.model.Transaction;


public interface Persistence {

	public void addTrans(Transaction transaction);
	
	public void deleteTrans(Transaction transaction);
	
	public void updateTrans(Transaction transaction);
	
	public Transaction queryTrans(String id);

	public Set<String> findAllUnCommit(long time);
}
