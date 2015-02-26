package myapps.alex.se.ednotes.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.activities.CommoditiesListActivity;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.model.CommodityTradeRoute;


public class TradesListAdapter extends BaseAdapter {

	final private Activity activity;
    private final int TRADE = 0;
    private final int NO_TRADES = 1;
    CommodityTradeRoute[] trades;

	public TradesListAdapter(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public int getViewTypeCount(){
		return 2;
	}

	public int getItemViewType(int position) {
		if(trades == null || trades.length == 0) {
			return NO_TRADES;
		}
		else {
			return TRADE;
		}
	}
	

	@Override
	public int getCount() {
		if(trades == null || trades.length == 0) {
			return 0;
		}
		
		return trades.length;
	}
	
	@Override
	public Object getItem(int position) {
		return trades[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		switch (getItemViewType(position)) {
			case NO_TRADES:
/*
				LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.no_content_item, null);
				TextView no_content_textview = (TextView) convertView.findViewById(R.id.no_content_textview);
				no_content_textview.setText(GTApplication.getStringResource("no_issues_text"));
*/
            break;
			
			case TRADE:
				if (convertView == null) {		
					LayoutInflater layoutInflater_issue = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater_issue.inflate(R.layout.trade_list_single_item, null);
		
					viewHolder = new ViewHolder();
                    viewHolder.fromstation_name_textview = (TextView) convertView.findViewById(R.id.fromstation_name_textview);
                    viewHolder.tostation_name_textview = (TextView) convertView.findViewById(R.id.tostation_name_textview);
                    viewHolder.commodity_textview = (TextView) convertView.findViewById(R.id.commodity_textview);
                    viewHolder.profit_textview = (TextView) convertView.findViewById(R.id.profit_textview);

                    viewHolder.fromstation_type_icon = (ImageView) convertView.findViewById(R.id.fromstation_type_icon);
                    viewHolder.tostation_type_icon = (ImageView) convertView.findViewById(R.id.tostation_type_icon);

                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
                    viewHolder.fromstation_name_textview.setTypeface(font);
                    viewHolder.tostation_name_textview.setTypeface(font);
                    viewHolder.commodity_textview.setTypeface(font);
                    viewHolder.profit_textview.setTypeface(font);
                    //viewHolder.last_visited_textview = (TextView) convertView.findViewById(R.id.last_visited_textview);

					convertView.setTag(viewHolder);
				}
				else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
			
				final CommodityTradeRoute tradeRoute = trades[position];

                viewHolder.fromstation_name_textview.setText(tradeRoute.getFromSystem().getName() + " [" + tradeRoute.getFromStation().getName() + "]");
                viewHolder.tostation_name_textview.setText(tradeRoute.getToSystem().getName() + " [" + tradeRoute.getToStation().getName() + "]");
                viewHolder.commodity_textview.setText(tradeRoute.getFromStationCommodity().getName());
                viewHolder.profit_textview.setText(" (" + tradeRoute.getProfit() + " profit)");

                String fromstation_type_string = (String) tradeRoute.getFromStation().getMisc().get(AppConstants.STATION_TYPE);
                String tostation_type_string = (String) tradeRoute.getToStation().getMisc().get(AppConstants.STATION_TYPE);

                if("station".equals(fromstation_type_string)) {
                    viewHolder.fromstation_type_icon.setImageResource(R.drawable.station);
                }
                else {
                    viewHolder.fromstation_type_icon.setImageResource(R.drawable.outpost);
                }

                if("station".equals(tostation_type_string)) {
                    viewHolder.tostation_type_icon.setImageResource(R.drawable.station);
                }
                else {
                    viewHolder.tostation_type_icon.setImageResource(R.drawable.outpost);
                }

                viewHolder.fromstation_name_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, CommoditiesListActivity.class);
                        intent.putExtra(AppConstants.STATION_NAME, tradeRoute.getFromStation().getName());
                        intent.putExtra(AppConstants.SYSTEM_NAME, tradeRoute.getFromSystem().getName());

                        activity.startActivity(intent);
                    }
                });

                viewHolder.tostation_name_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, CommoditiesListActivity.class);
                        intent.putExtra(AppConstants.STATION_NAME, tradeRoute.getToStation().getName());
                        intent.putExtra(AppConstants.SYSTEM_NAME, tradeRoute.getToSystem().getName());

                        activity.startActivity(intent);
                    }
                });

                //viewHolder.last_visited_textview.setText(lastVisitedString);

//				convertView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//						Intent intent = new Intent(activity, CommoditiesListActivity.class);
//                        intent.putExtra(AppConstants.STATION_NAME, station.getName());
//                        intent.putExtra(AppConstants.SYSTEM_NAME, activity.getIntent().getStringExtra(AppConstants.SYSTEM_NAME));
//
//						activity.startActivity(intent);
//                    }
//                });





			break;
		}
		
		return convertView;
	}

	private static class ViewHolder {
		TextView fromstation_name_textview, tostation_name_textview, commodity_textview, profit_textview;
        ImageView fromstation_type_icon, tostation_type_icon;
	}
	
	public void setTrades(ArrayList<CommodityTradeRoute> trades) {

        CommodityTradeRoute[] tempTrades = new CommodityTradeRoute[trades.size()];
        this.trades = trades.toArray(tempTrades);

        notifyDataSetChanged();
	}

}






