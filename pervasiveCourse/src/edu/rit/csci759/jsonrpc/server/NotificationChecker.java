package edu.rit.csci759.jsonrpc.server;



import edu.rit.csci759.rspi.Implementation;
//Seperate thread running a method to check for changes in temperature
public class NotificationChecker {
	public static void checker(){
		int temp1;
		int temp2;
		String response_txt;
		Implementation getter= new Implementation();
		while(true){
			temp1=(int)getter.read_temperature();
			int count=0;
			for(count=0;count<1000;count++){}
			temp2 =(int)getter.read_temperature();
			if(Math.abs(temp2-temp1)>=2){
				response_txt = Jsonclient.testJSONRequest("10.10.10.126:8081", "update");	
			}
		}
	}

}
