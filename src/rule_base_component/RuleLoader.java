package rule_base_component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import data_structures.*;

public class RuleLoader {

	private ArrayListRuleGraph RuleSet;
	public String RuleBaseFileName;

	public RuleLoader(ArrayListRuleGraph ruleSet) {
		RuleSet = ruleSet;
		ruleSetInitializer();
	}

	public void ruleSetInitializer() {

		/*
		 * ##### We currently load all the rules. Is it required to load all the rules?
		 * If not, which portion should be loaded. In this case the interactions between
		 * the rule base and the program increases and the time increases. #####Should
		 * we insert the rules by adding columns about the next rule and the previous
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
				// System.out.println ("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				// System.out.println("Rule String"+ RuleString);
				if (!RuleString.trim().isEmpty() && !RuleString.trim().startsWith("###")) {
					Rule r = new Rule();
					r.ParseRule(RuleString);
					// System.out.println ("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
					// System.out.println(r.printRule());
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
