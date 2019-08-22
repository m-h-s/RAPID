package data_structures;
import java.util.*;

import reasoner_component.Query;

public interface RuleGraph <T> {

	void addRule(Rule r);

	void deleteRule(Rule r);

	int findRule(Rule r);

	T toStringRuleGraph();

	boolean containsRule(Rule r);

	int getSize();

	T getExpansionHistoryinString ();

	Rule getRule(int RulePostion);

	T findInRulesLHS(String TermString);

	Query matchQuery(Query q);
	
	T deepCopyRuleGraph ();
	
	List <Rule> getSourceRules ();
	
	void pruneDanglingRules ();
	
	T getRules (RuleType RT);
	
	void pruneDanglingTerms (Rule r);
	

}