package edu.rit.csci759.jsonrpc.server;

import java.net.MalformedURLException;
import java.net.URL;



import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

public class Jsonclient {
	public static String testJSONRequest(String server_URL_text, String method){
		// Creating a new session to a JSON-RPC 2.0 web service at a specified URL

		
		
		// The JSON-RPC 2.0 server URL
		URL serverURL = null;

		try {
			serverURL = new URL("http://"+server_URL_text);

		} catch (MalformedURLException e) {
		// handle exception...
		}

		// Create new JSON-RPC 2.0 client session
		JSONRPC2Session mySession = new JSONRPC2Session(serverURL);


		// Once the client session object is created, you can use to send a series
		// of JSON-RPC 2.0 requests and notifications to it.

		// Sending an example "getTime" request:
		// Construct new request

		int requestID = 0;
		
		JSONRPC2Request request = new JSONRPC2Request(method, requestID);

		// Send request
		JSONRPC2Response response = null;

		try {
			response = mySession.send(request);

		} catch (JSONRPC2SessionException e) {

		
		// handle exception...
		}

		// Print response result / error
		if (response.indicatesSuccess())
			System.out.println("Success");
		else
			System.out.println("Error");
		
	
		return response.getResult().toString();
	}
	

}
