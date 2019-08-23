package reasoner_component;

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

public class Constraint {

	private Term Attribute;
	private TermPriority TermPriority;

	public Constraint(Term attribute, TermPriority attributePriority, TermSatisfactionValues attributeValue) {
		this.setAttribute(attribute);
		this.setAttributePriority(attributePriority);
		this.setAttributeValue(attributeValue);

	}

	/**
	 * @return the requirement
	 */
	public Term getAttribute() {
		return Attribute;
	}

	/**
	 * @param requirement the requirement to set
	 */
	public void setAttribute(Term attribute) {
		this.Attribute = attribute;
	}

	/**
	 * @return the expectedSatLevel
	 */
	public TermSatisfactionValues getAttributeValue() {
		return this.Attribute.getFinalSatValue();
	}

	/**
	 * @param expectedSatLevel the expectedSatLevel to set
	 */
	public void setAttributeValue(TermSatisfactionValues attributeValue) {
		this.Attribute.setFinalSatValue(attributeValue);
	}

	/**
	 * @return the priority
	 */
	public TermPriority getAttributePriority() {
		return TermPriority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setAttributePriority(TermPriority priority) {
		this.TermPriority = priority;
	}

	public String printQueriedTerm() {
		String QueryString = "< ";
		QueryString = QueryString + this.Attribute.printTerm() + ":=";
		QueryString = QueryString + this.getAttributeValue().toString().toLowerCase() + ", ";
		QueryString = QueryString + "priority:= " + this.TermPriority.name().toLowerCase() + " >";
		return QueryString;

	}
}
