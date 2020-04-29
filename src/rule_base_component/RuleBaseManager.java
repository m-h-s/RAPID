package rule_base_component;
import java.util.*;
import data_structures.*;
import user_interaction_component.UserInteractionManager;
import java.io.*;
import java.nio.file.Paths;

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

public class RuleBaseManager {

	public ListRuleSet ruleSet;
	private UserInteractionManager controller;
	private RuleLoader ruleLoader;

	public RuleBaseManager(UserInteractionManager Cont) {

		this.controller = Cont;

		ruleSet = new ListRuleSet();
		ruleLoader = new RuleLoader(ruleSet);

		// print rule base as loaded from the file
		Cont.receiveOutputFromModel("other", ruleSet.RuleBaseToString());
	}

	public ArrayList<Rule> matchPrecedent(ArrayList LHS) {

		return ruleSet.findPrecedent(LHS);

	}

	public ArrayList<Rule> matchConsequent(Term RHS) {

		return ruleSet.findConsequent(RHS);

	}

}
