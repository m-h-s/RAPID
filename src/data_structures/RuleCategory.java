package data_structures;

public enum RuleCategory {

	
		NF_REF, OP_FF_REF, OP_FI_REF, COR, OP_NF_REF, UNDET
		/*
		 * NF-REF : The relationship between two NFRs. OP-FF-REQ: The relationship
		 * between two FREQs ****** OP-FI-REQ: The relationship between an FREQ and a
		 * specific implementation of the freq; i.e. design alternative ;i.e. one FREQ
		 * refines the other FREQ. COR: The relationship between a design option (the
		 * refinement of an FREQ) and an NFR; i.e. the impact of the refinement of a
		 * FREQ on NFRS. OP-NF-REQ: The relationship between a design objective (an
		 * FREQ) and NFR ; i.e. Refinement of an NFR to FREQ. We differentiate between
		 * COR and OP and FREQ.
		 */
	

}
