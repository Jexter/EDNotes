package myapps.alex.se.ednotes.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.TypefaceSpan;


public class CommoditiesListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodities_list_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableString s = new SpannableString(getIntent().getStringExtra(AppConstants.STATION_NAME));
        TypefaceSpan ts = new TypefaceSpan(this, "eurostile.TTF");
        s.setSpan(ts, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

}
