package data_structures;
import java.util.*;
import reasoner_component.ConstraintSet;

/**
 * @author Mahsa Sadi
 * 
 * @since 2018 - 11 - 01
 * 
 * License: Creative Commons
 * 
 * Copyright by Mahsa Sadi
 * 
 */

public interface RuleSet <T> {

	void addRule(Rule r);

	void deleteRule(Rule r);

	int findRule(Rule r);

	T toStringRuleGraph();

	boolean containsRule(Rule r);

	int getSize();

	T getExpansionHistoryinString ();

	Rule getRule(int RulePostion);

	T findInRulesLHS(String TermString);

	ConstraintSet matchConstraints(ConstraintSet q);
	
	T deepCopyRuleGraph ();
	
	List <Rule> getSourceRules ();
	
	void pruneDanglingRules ();
	
	T getRules (RuleType RT);
	
	void pruneDanglingTerms (Rule r);
	

}