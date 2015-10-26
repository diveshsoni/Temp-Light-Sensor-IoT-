package edu.rit.csci759.jsonrpc.server;
import java.util.*;
import net.sourceforge.jFuzzyLogic.*;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.RuleExpression;
import net.sourceforge.jFuzzyLogic.rule.RuleTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodOrMax;

import edu.rit.csci759.rspi.Implementation;

public class RuleAdder {
	
	 
	 public static RuleBlock ruleBlock;
	 public static int count;
	 
	 
//Method for updating the blinds and sending back the blindstate to the android app
	public static String blindState(){
		 String filename="/home/pi/ritcoursematerials_student/PervasiveCourse_student/FuzzyLogic/tipper.fcl";
		 FIS fis = FIS.load(filename, true);
		 if(fis==null){
			 System.err.println("error");
		 }
		 Implementation imp=new Implementation();
		 fis.setVariable("temperature",imp.read_temperature());
		 fis.setVariable("ambient",imp.read_ambient_light_intensity());
	
		 fis.evaluate();
		 float temp = (int)fis.getVariable("blind").getValue();
		 if(0<=temp & temp<33.3){
			 String state="Blinds Open";
			 imp.led_when_high();
			 return state;
		 }
		 if(33.3<=temp & temp<66.6){
			 String state="Blinds Half Open";
			 imp.led_when_mid();
			 return state;
		 }
		 if(66.6<=temp & temp<=100){
			 String state="Blinds Close";
			 imp.led_when_low();
			 return state;
		 }
		return "null";	 
	 }
	// Method for adding fuzzy logic rules 
	public static String ruleAdder(){
		String filename="/home/pi/ritcoursematerials_student/PervasiveCourse_student/FuzzyLogic/tipper.fcl";
		FIS fis = FIS.load(filename, true);
		ruleBlock =fis.getFunctionBlock("blinds").getFuzzyRuleBlock("No1");
		
		for(int i=0;i<JsonHandler.list.size();i=i+4){
		Rule rule4=new Rule("Rule5", ruleBlock);
		System.out.println(JsonHandler.list.get(i));
		 RuleTerm term1= new RuleTerm(fis.getVariable("temperature"),JsonHandler.list.get(i).toString(),false);
		 RuleTerm term2= new RuleTerm(fis.getVariable("ambient"),JsonHandler.list.get(i+2).toString(),false);
		 if(JsonHandler.list.get(i+1).toString().equalsIgnoreCase("or")){
		 RuleExpression antecedentOr= new RuleExpression(term1,term2,RuleConnectionMethodOrMax.get());
		 rule4.setAntecedents(antecedentOr);
		 }
		 else{
			 RuleExpression antecedentAnd= new RuleExpression(term1,term2,RuleConnectionMethodAndMin.get());
			 rule4.setAntecedents(antecedentAnd);
		 }
		 rule4.addConsequent(fis.getVariable("blind"), JsonHandler.list.get(i+3).toString(), false);
		 ruleBlock.add(rule4);
		 
		}
		 System.out.print(ruleBlock);
		 fis.evaluate();
		 Implementation imp=new Implementation();
		 float temp = (int)fis.getVariable("blind").getValue();
		 if(0<=temp & temp<33.3){
			 String state="Blinds Open";
			 imp.led_when_high();
			 return state;
		 }
		 if(33.3<=temp & temp<66.6){
			 String state="Blinds Half Open";
			 imp.led_when_mid();
			 return state;
		 }
		 if(66.6<=temp & temp<=100){
			 String state="Blinds Close";
			 imp.led_when_low();
			 return state;
		 }
		 return null;
	}
	

}
