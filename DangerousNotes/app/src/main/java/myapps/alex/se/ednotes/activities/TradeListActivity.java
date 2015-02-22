package myapps.alex.se.ednotes.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.TypefaceSpan;
import myapps.alex.se.ednotes.fragments.TradesListFragment;
import myapps.alex.se.ednotes.model.CommodityTradeRoute;


public class TradeListActivity extends ActionBarActivity {

    private ArrayList<CommodityTradeRoute> ctr;
    private TradesListFragment tradeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trades_list_activity);
        ctr = ((ArrayList<CommodityTradeRoute>)(getIntent().getExtras().getSerializable(AppConstants.TRADES)));
        int nrOfTrades = ctr.size();
        SpannableString s = new SpannableString(nrOfTrades + " POSSIBLE TRADE" + (nrOfTrades==0?"S":(nrOfTrades>1?"S":"")));
        TypefaceSpan ts = new TypefaceSpan(this, "eurostile.TTF");
        s.setSpan(ts, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        tradeFragment.setTrades(ctr);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);

        tradeFragment = (TradesListFragment) fragment;
    }
}
