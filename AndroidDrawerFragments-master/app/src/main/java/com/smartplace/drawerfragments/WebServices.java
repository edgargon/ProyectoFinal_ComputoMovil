package com.smartplace.drawerfragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


import java.io.IOException;

/**
 * Created by Cesar on 28/11/2014.
 */
public class WebServices {

    //private static final String SERVER_URL = "http://ec2-54-148-58-107.us-west-2.compute.amazonaws.com/";
    private static final String SERVER_URL = "http://ec2-54-68-167-172.us-west-2.compute.amazonaws.com/";

    //private static final String PATH_GET_NAMES= "welcome/getFullNameAndroid";
    //private static final String PATH_GET_NAMES= "welcome/getFullNameChespirito";
    private static final String PATH_GET_NAMES= "welcome/getFullCleanHouse";

    private static final int CONNECTION_TIMEOUT = 10000;

    private static final String TAG = "MyActivity";

    public static void getNames(final Handler handler)
    {

        //Run new thread to perform users Info query
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                //Set post information to perform query
                HttpClient client = new DefaultHttpClient(httpParameters);
                //set post's URL
                HttpPost post = new HttpPost(SERVER_URL + PATH_GET_NAMES);


                //set post's header
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                //Create new NameValue pair list
                // List<NameValuePair> params = new ArrayList<NameValuePair>();
                //set parameters
                //params.add(new BasicNameValuePair("userID", userID));
                //try {
                //set post value
                //    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                //} catch (UnsupportedEncodingException e) {
                //    e.printStackTrace();
                //}
                try {

                    //Execute post
                    HttpResponse httpResponse = client.execute(post);
                    //get post's entity
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    //Set message to send to the handler
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        //start new thread
        thread.start();


    }
}
