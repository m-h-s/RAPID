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

	public ListRuleSet RuleSet;
	private UserInteractionManager Cont;
	private RuleLoader RuleLoader;

	public RuleBaseManager(UserInteractionManager Cont) {

		this.Cont = Cont;

		RuleSet = new ListRuleSet();
		RuleLoader = new RuleLoader(RuleSet);

		// print rule base as loaded from the file
		Cont.receiveOutputFromModel("other", RuleSet.RuleBaseToString());
	}

	public ArrayList<Rule> matchPrecedent(ArrayList LHS) {

		return RuleSet.findPrecedent(LHS);

	}

	public ArrayList<Rule> matchConsequent(Term RHS) {

		return RuleSet.findConsequent(RHS);

	}

}
