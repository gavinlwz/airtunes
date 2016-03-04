package com.mycompany.airtunes;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mycompany.airtunes.R;


public class SearchGroupActivity extends Activity {

    SearchController sc;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        sc = new SearchController();
    }


    // Add group
    public void onCreateButtonClick(View view) {
        String groupName = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";
        sc.addGroup(groupName, "Wai Wu");
        System.out.println("Created group");
    }



    // Request code to create new Activity
    public static final int SearchButtonActivity_ID = 1;

    public void onSearchButtonClick(View view) {
        String search = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";
        Group group = sc.searchGroup(search);
        if (group == null) {
            System.out.println("Group not found!");
            Context context = getApplicationContext();
            CharSequence text = "Group not found!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        Log.d("Message: ", "Group found: " + group.groupName);


        // TODO: Akash
        /* TODO: Akash

        Intent goToRoom = new Intent(this, PlayButtonClickActivity.class);
        goToRoom.putExtra("Size", boardSize);
        startActivityForResult(startPlay, SearchButtonActivity_ID);

        //receive
            ClassName model = (ClassName) getIntent().getSerializableExtra("Editing");

            And

            Class ClassName implements Serializable {
            }
          */

    }


}