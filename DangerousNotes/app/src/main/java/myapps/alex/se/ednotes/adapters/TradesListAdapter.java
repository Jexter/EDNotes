package myapps.alex.se.ednotes.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
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
					convertView = layoutInflater_issue.inflate(R.layout.station_list_item, null);
		
//					viewHolder = new ViewHolder();
//                    viewHolder.station_name_textview = (TextView) convertView.findViewById(R.id.station_name_textview);
//                    viewHolder.blackmarket_textview = (TextView) convertView.findViewById(R.id.blackmarket_textview);
//                    viewHolder.station_type_icon = (ImageView) convertView.findViewById(R.id.station_type_icon);
//                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
//                    viewHolder.station_name_textview.setTypeface(font);
//                    viewHolder.blackmarket_textview.setTypeface(font);
//                    //viewHolder.last_visited_textview = (TextView) convertView.findViewById(R.id.last_visited_textview);

					convertView.setTag(viewHolder);
				}
				else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
			
				final CommodityTradeRoute station = trades[position];
/*
				viewHolder.system_name_textview.setText(station.getName());
                viewHolder.last_visited_textview.setText(Utils.getDateAsTimePassed(station.getLastVisited()));

                String stationCountText = "No stations entered here yet";
                stationCountText = station.getStationCount() + " stations";

                viewHolder.station_count_textView.setText(stationCountText);
*/



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

                final TradesListAdapter thisAdapter = this;



			break;
		}
		
		return convertView;
	}

	private static class ViewHolder {
		TextView station_name_textview, blackmarket_textview;//, last_visited_textview;
        ImageView station_type_icon;
	}
	
	public void setTrades(ArrayList<CommodityTradeRoute> trades) {
        CommodityTradeRoute[] tempTrades = new CommodityTradeRoute[trades.size()];
        this.trades = trades.toArray(tempTrades);

        notifyDataSetChanged();
	}

}






