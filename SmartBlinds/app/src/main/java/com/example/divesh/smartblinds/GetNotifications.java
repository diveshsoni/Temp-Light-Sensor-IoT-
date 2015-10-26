package com.example.divesh.smartblinds;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Divesh on 10/11/2015.
 */
public class GetNotifications extends IntentService {

    /**
     * The port that the server listens on.
     */
    private static final int PORT = 8081;

    ArrayList<String> temp_changes;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GetNotifications(String name) {
        super(name);
    }

    public GetNotifications( ) {
        super("GetNotifications");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        temp_changes = new ArrayList<String>();

        System.out.println("The server is running.");
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent bintent = new Intent("pl");
//        i.putextra
//        localbroadcastmanager.send
        bintent.setAction("getNotify");
        bintent.addCategory(Intent.CATEGORY_DEFAULT);
        bintent.putExtra ("temp_change_list", temp_changes );
        sendBroadcast(bintent, "getNotify");
//
    }

    private static class Handler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private Dispatcher dispatcher;
        //private int local_count;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {
            this.socket = socket;

            // Create a new JSON-RPC 2.0 request dispatcher
            this.dispatcher =  new Dispatcher();

            // Register the "echo", "getDate" and "getTime" handlers with it
            dispatcher.register(new JSONHandler.UpdateHandler());
        }

        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // read request
                String line;
                line = in.readLine();
                //System.out.println(line);
                StringBuilder raw = new StringBuilder();
                raw.append("" + line);
                boolean isPost = line.startsWith("POST");
                int contentLength = 0;
                while (!(line = in.readLine()).equals("")) {
                    //System.out.println(line);
                    raw.append('\n' + line);
                    if (isPost) {
                        final String contentHeader = "Content-Length: ";
                        if (line.startsWith(contentHeader)) {
                            contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                        }
                    }
                }
                StringBuilder body = new StringBuilder();
                if (isPost) {
                    int c = 0;
                    for (int i = 0; i < contentLength; i++) {
                        c = in.read();
                        body.append((char) c);
                    }
                }
                Log.d("runtime1", "b4");
                System.out.println(body.toString());
                JSONRPC2Request request = JSONRPC2Request.parse(body.toString());

                JSONRPC2Response resp = dispatcher.process(request, null);
                Log.d("runtime1", "aft");


                // send response
                out.write("HTTP/1.1 200 OK\r\n");
                out.write("Content-Type: application/json\r\n");
                out.write("\r\n");
                out.write(resp.toJSONString());
                // do not in.close();
                out.flush();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            } catch (JSONRPC2ParseException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }


    }




}
