import java.util.*;

public class Query {
	
	
	private List <QueriedTerm>  QuerySet;
	
	
	public Query ()
	{
		QuerySet = new ArrayList<QueriedTerm> ();
		
	}
	
	
	public void addQueriedTerm (QueriedTerm queryElement)
	
	{
		QuerySet.add(queryElement);
	}
	
	public void addQueriedTerm (Term Attribute, TermPriority attributePriority, TermSatisfactionValues attributeValue )
	{
		QueriedTerm ReqSpec = new QueriedTerm (Attribute, attributePriority, attributeValue );
		QuerySet.add(ReqSpec);
	}
	
	public void printQuery ()
	{
		System.out.print("{");
		for (QueriedTerm r : QuerySet)
		
			System.out.print(r.printQueriedTerm());
		System.out.print("}");
		
	}
	
	public List <QueriedTerm> getQuerySet ()
	{
		return QuerySet;
	}
}
