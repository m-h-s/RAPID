package rule_base_component;

import java.util.*;

import data_structures.*;
import user_interaction_component.UserInteractionManager;

import java.io.*;
import java.nio.file.Paths;

public class RuleBaseManager {

	public ArrayListRuleGraph RuleSet;
	private UserInteractionManager Cont;
	private RuleLoader RuleLoader;

	public RuleBaseManager(UserInteractionManager Cont) {

		this.Cont = Cont;

		RuleSet = new ArrayListRuleGraph();
		RuleLoader = new RuleLoader(RuleSet);
		// print rule base as loaded from the file
		Cont.receiveOutputFromModel("other", RuleSet.RuleBaseToString());
	}

	public ArrayList<Rule> matchLeftHandSide(ArrayList LHS) {

		return RuleSet.findLeftHandSide(LHS);

	}

	public ArrayList<Rule> matchRightHandSide(Term RHS) {

		return RuleSet.findRightHandSide(RHS);

	}

}
