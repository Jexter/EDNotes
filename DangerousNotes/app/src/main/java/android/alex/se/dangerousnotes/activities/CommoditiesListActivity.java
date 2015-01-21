package android.alex.se.dangerousnotes.activities;

import android.alex.se.dangerousnotes.R;
import android.alex.se.dangerousnotes.common.AppConstants;
import android.alex.se.dangerousnotes.common.TypefaceSpan;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;

import android.view.Menu;
import android.view.MenuItem;


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
//        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

}
