

package rule_base_component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import data_structures.*;

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

public class RuleLoader {

	private ListRuleSet RuleSet;
	public String RuleBaseFileName;

	public RuleLoader(ListRuleSet ruleSet) {
		RuleSet = ruleSet;
		ruleSetInitializer();
	}

	public void ruleSetInitializer() {

		/*
		 * We currently load all the rules. 
		 * This case the interactions between
		 * the rule base and the program increases and the time increases.
		 * Is it required to load all the rules?
		 * If not, which portion should be loaded. 
		 * We can insert the rules by adding columns about the next rule and the previous
		 * rule?
		 * 
		 */

		importRuleFromFile(Paths.get("").toAbsolutePath().toString() + "\\src\\rule_base_component\\Rule Base");

	}

	public void importRuleFromFile(String InputRulesFileName) {
		File InputFile = new File(InputRulesFileName);
		BufferedReader bf;

		try {
			bf = new BufferedReader(new FileReader(InputFile));

			String RuleString;
			while ((RuleString = bf.readLine()) != null) {
			
				if (!RuleString.trim().isEmpty() && !RuleString.trim().startsWith("###")) {
					Rule r = new Rule();
					r.ParseRule(RuleString);
					addRule(r);
				}
			}

			bf.close();
		}

		catch (IOException Ex) {
			Ex.printStackTrace();
		}

	}

	public void addRule(Rule newRule) {
		RuleSet.addRule(newRule);
	}

}
