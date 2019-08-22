
enum TermPriority
{
	HIGH,
	MEDIUM,
	LOW
}

public class QueriedTerm {
	
	private Term Attribute;
	private TermPriority TermPriority;
	
	
	
	public QueriedTerm (Term attribute, TermPriority attributePriority, TermSatisfactionValues attributeValue)
	{
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
	
    
	
	public String printQueriedTerm ()
	{
		String QueryString ="< ";
		QueryString = QueryString + this.Attribute.printTerm() + ":=";
		QueryString = QueryString + this.getAttributeValue().toString().toLowerCase() + ", ";
		QueryString = QueryString + "priority:= " + this.TermPriority.name().toLowerCase() + " >";
		return QueryString;
		
	}
}
