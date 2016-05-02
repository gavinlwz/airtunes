package com.mycompany.airtunes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

// Class redirects user to screen to sign up for premium account
public class PremiumRedirectActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_redirect);
    }
}
