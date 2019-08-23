package reasoner_component;
import java.util.*;

import data_structures.Term;
import data_structures.TermPriority;
import data_structures.TermSatisfactionValues;


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

public class ConstraintSet {
	
	
	private List <Constraint>  constraints;
	
	
	public ConstraintSet ()
	{
		constraints = new ArrayList<Constraint> ();
		
	}
	
	
	public void addConstraint (Constraint queryElement)
	
	{
		constraints.add(queryElement);
	}
	
	public void addConstraint (Term Attribute, TermPriority attributePriority, TermSatisfactionValues attributeValue )
	{
		Constraint ReqSpec = new Constraint (Attribute, attributePriority, attributeValue );
		constraints.add(ReqSpec);
	}
	
	public void print ()
	{
		System.out.print("{");
		for (Constraint r : constraints)
		
			System.out.print(r.printQueriedTerm());
		System.out.print("}");
		
	}
	
	public List <Constraint> getConstraints ()
	{
		return constraints;
	}
}
