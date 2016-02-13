package com.doctorfinderapp.doctorfinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by fedebyes on 13/02/16.
 */

public class FacebookProfile {

    private static String name;
    private static String email;
    private static String lastname;

    private static com.parse.ParseException ret = null;

    // Function to get information from FBuser and put them in user
    public static com.parse.ParseException getUserDetailsRegisterFB(ParseUser Puser, View v) throws InterruptedException {
        final View vi = v;
        final ParseUser user = Puser;

        // Prelevo informazioni da facebook
        Bundle parameters = new Bundle();

        // parameters from facebook
        parameters.putString("fields", "email,first_name,last_name");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {

                // Prelevo il risultato
                try {
                    email = response.getJSONObject().getString("email");
                    lastname = response.getJSONObject().getString("last_name");
                    name = response.getJSONObject().getString("first_name");


                    Log.d("FacebookUtil", "Informazioni prelevate da Facebook");

                    // Inserisco le info nel ParseUser
                    user.setEmail(email);
                    user.put("name", name.trim());
                    //user.put("flagISA","Persona");
                    try {
                        //uso save e non savebackground perchè non deve essere asincrona
                        user.save();

                        ParseObject persona = new ParseObject("Persona");
                        persona.put("username",user.getUsername());
                        persona.put("lastname", lastname.trim());

                        //persona.put("city",citta.trim());
                       // persona.save();
                    } catch (com.parse.ParseException e) {
                        ret = e;
                        System.out.println("debug: ret = "+ret.getMessage().toString());

                    }
                } catch (JSONException e) {
                    System.out.println("debug: eccezione nell'ottenere info da facebook");

                }
            }
        }
        ).executeAndWait();
        return ret;
    }
}