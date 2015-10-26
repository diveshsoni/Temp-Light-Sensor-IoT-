package com.example.divesh.smartblinds;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;
import android.widget.TextView;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.NotificationHandler;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

public class JSONHandler {

	private final static String serverURL = "10.10.10.102:8080";
	static SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmm");

	public static String testJSONRequest(String server_URL_text, String method){
		// Creating a new session to a JSON-RPC 2.0 web service at a specified URL

		Log.d("Debug serverURL", server_URL_text);
		
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
		Log.d("debug serv", "bef call");
		JSONRPC2Request request = new JSONRPC2Request(method, requestID);
		Log.d("debug serv", "bef aft");

		// Send request
		JSONRPC2Response response = null;

		try {
			response = mySession.send(request);

		} catch (JSONRPC2SessionException e) {

		Log.e("error", e.getMessage().toString());
		// handle exception...
		}

		if (response != null) {
			// Print response result / error
			Log.d("runtime12", "res not null");
			if (response.indicatesSuccess())
				Log.d("debug", response.getResult().toString());
			else
				Log.e("error", response.getError().getMessage().toString());

			return response.getResult().toString();
		}
		else {
			Log.d("main in bg", "in jsonrpc error");
			return " Error ";
		}

	}
	public static String testJSONRequest1(String server_URL_text,List list, String method){
		// Creating a new session to a JSON-RPC 2.0 web service at a specified URL

		Log.d("Debug serverURL1", server_URL_text);

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
		Log.d("Debug serve call", "before");
		JSONRPC2Request request = new JSONRPC2Request(method,list,requestID);
		Log.d("Debug serve call", "after");

		// Send request
		JSONRPC2Response response = null;

		try {
			response = mySession.send(request);

		} catch (JSONRPC2SessionException e) {

			Log.e("error", e.getMessage().toString());
			// handle exception...
		}
		if (response != null) {
			// Print response result / error
			Log.d("runtime12", "res not null");
			if (response.indicatesSuccess())
				Log.d("debug", response.getResult().toString());
			else
				Log.e("error", response.getError().getMessage().toString());

			return response.getResult().toString();
		}
		else {
			Log.d("main in bg", "in jsonrpc error");
			return " Error ";
		}

	}

	// Implements a handler for an "echo" JSON-RPC method
	public static class UpdateHandler implements RequestHandler {

		String tempUpdate = "", ambUpdate = "", blindUpdate = "";

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"update"};
		}
		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("update")) {

//				update valuse on textviews in the main_activity
				tempUpdate = JSONHandler.testJSONRequest(serverURL, "read_temperature") + (char) 0x00B0 + "C";
				MainActivity.temp_tv.setText("Demo");
				MainActivity.temp_tv.setText(tempUpdate);
				ambUpdate = JSONHandler.testJSONRequest(serverURL, "read_ambient_light_intensity");
				MainActivity.ambient_tv.setText(ambUpdate);
				blindUpdate = JSONHandler.testJSONRequest(serverURL, "blindState");
				MainActivity.blindState_tv.setText(blindUpdate);

				String currentDateandTime = sdf.format(new Date());

				MainActivity.sensorHistory.add(1, "   "+ currentDateandTime+"   "+tempUpdate+"   "+ambUpdate+"   "+blindUpdate);

				return new JSONRPC2Response("ok", req.getID());
			} else {
				// Method name not supported
				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}
}
