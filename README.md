# Rational API Designer (RAPID)

### Objectives
RAPID is a conversational assistant that aids software developers in addressing non-functional requirements in the design of web APIs.

### Main Features
RAPID provides four types of guidelines to the users: <Enter>
* Elaborating and clarifying a given requirement
* Introducing a set of design alternatives to address a given requirement
* Analyzing the impact of design alternatives on non-functional requirements
* Recommending design alternatives based on the requirements of a given case.

### Design and Architecture
RAPID has four main components:<br>
* *Rule Base Component (RB)*: RB is responsible for storing and managing the rules encoding design knowledge. 
   <br><br>
* *Rule Explorer (RE)*: RE is responsible for retrieving the rules that are related to a session of interaction with a user, <br> 
  and managing them until the interaction session ends.
  <br><br> 
* *User Interaction Component (UIM)*: UIM allows a user to interact with the system, to pose design queries over the rule base, to and receive a set of responses. 
<br><br>
* *Reasoner*: Reasoner is responsible for evaluating the rules related to an interaction session and selecting and recommending appropriate design solutions. 

### Copyright
License: Creative Commons <br>
* The use, modification, and distribution of the source code is allowed under the condition that the original work and the original developer is cited.
<br><br>
* Obtaining permission from the original developer is required.

### References
1. Sadi, M. H. (2020). "Assisting with API design through Reusing Design Knowledge". <br>
Doctoral Dissertation. Department of Computer Science. University of Toronto.
<br> <br>
2. Sadi, M.H. & Yu, E. (2020). "RAPID: Assisting with API Design through Reusing Design Knowledge". <br>
Submitted to the Requirements Engineering Journal.

### Contributers
* Mahsa H. Sadi, PH.D., Department of Computer Science University of Toronto
