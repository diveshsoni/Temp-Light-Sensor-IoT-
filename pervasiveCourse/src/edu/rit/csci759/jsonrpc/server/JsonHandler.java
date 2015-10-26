package edu.rit.csci759.jsonrpc.server;

/**
* Demonstration of the JSON-RPC 2.0 Server framework usage. The request
* handlers are implemented as static nested classes for convenience, but in 
* real life applications may be defined as regular classes within their old 
* source files.
*
* @author Vladimir Dzhuvinov
* @version 2011-03-05
*/ 

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

import edu.rit.csci759.rspi.Implementation;

public class JsonHandler {
	public static List list;
	
	// Implements a handler for an "echo" JSON-RPC method
	 public static class EchoHandler implements RequestHandler {
		

	     // Reports the method names of the handled requests
	     public String[] handledRequests() {
			
	         return new String[]{"echo"};
	     }
			
			
	      // Processes the requests
	      public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {
				
	          if (req.getMethod().equals("echo")) {
					
	              // Echo first parameter
					
	              List params = (List)req.getParams();
		 
		         Object input = params.get(0);
		 
		         return new JSONRPC2Response(input, req.getID());
	         } else {
		
	             // Method name not supported
					
	             return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
		    }
	     }
	 }
		
		
	 
	 
	 // Implements a handler for all of android application requests JSON-RPC methods
	 // that return the current date and time
	 public static class AppHandler implements RequestHandler {
		
		
	     // Reports the method names of the handled requests
		public String[] handledRequests() {
		
		    return new String[]{"getDate", "getTime","read_temperature","read_ambient_light_intensity","blindState","ruleAdder"};
		}
		
		
		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {
		
			String hostname="unknown";
			try {
				hostname=InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		    if (req.getMethod().equals("getDate")) {
		    
		        DateFormat df = DateFormat.getDateInstance();
			
			String date = df.format(new Date());
			
			return new JSONRPC2Response(hostname+" "+date, req.getID());

	         }
	         else if (req.getMethod().equals("getTime")) {
	        	
		        DateFormat df = DateFormat.getTimeInstance();
			
			String time = df.format(new Date());
			
			return new JSONRPC2Response(hostname+" "+time, req.getID());
	         }
	         else if (req.getMethod().equals("read_temperature")) {
		        	
			        
				Implementation getter= new Implementation();
				return new JSONRPC2Response(" "+getter.read_temperature(), req.getID());
		         }
	         
	         else if (req.getMethod().equals("read_ambient_light_intensity")) {
		        	
			        
					Implementation getter= new Implementation();
					if(getter.read_ambient_light_intensity()<=35){
						return new JSONRPC2Response("DARK", req.getID());
					}
					if(getter.read_ambient_light_intensity()<=75){
						return new JSONRPC2Response("DIM", req.getID());
					}
					if(getter.read_ambient_light_intensity()>=75){
						return new JSONRPC2Response("BRIGHT", req.getID());
					}
					return new JSONRPC2Response(" ", req.getID());
	         }
	         else if (req.getMethod().equals("blindState")) {
		        	
			        
					RuleAdder getter=new RuleAdder();
					
					return new JSONRPC2Response(getter.blindState(), req.getID());
	         }
	         else if (req.getMethod().equals("ruleAdder")) {
		        	
	        	 	list=req.getPositionalParams();
					return new JSONRPC2Response(RuleAdder.ruleAdder(), req.getID());
	         }
		     else {
		    
		        // Method name not supported
			
			return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
	         }
	     }
	 }
}
