package myapps.alex.se.ednotes.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.activities.CommoditiesListActivity;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.model.System;


public class TradesListAdapter extends BaseAdapter {

	final private Activity activity;
    private final int STATION = 0;
    private final int NO_SYSTEMS = 1;
    private Station[] stations;
    private System system;

	public TradesListAdapter(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public int getViewTypeCount(){
		return 2;
	}

	public int getItemViewType(int position) {
		if(stations == null || stations.length == 0) {
			return NO_SYSTEMS;
		}
		else {
			return STATION;
		}
	}
	

	@Override
	public int getCount() {
		if(stations == null || stations.length == 0) {
			return 0;
		}
		
		return stations.length;
	}
	
	@Override
	public Object getItem(int position) {
		return stations[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		switch (getItemViewType(position)) {
			case NO_SYSTEMS:
/*
				LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.no_content_item, null);
				TextView no_content_textview = (TextView) convertView.findViewById(R.id.no_content_textview);
				no_content_textview.setText(GTApplication.getStringResource("no_issues_text"));
*/
            break;
			
			case STATION:
				if (convertView == null) {		
					LayoutInflater layoutInflater_issue = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater_issue.inflate(R.layout.station_list_item, null);
		
					viewHolder = new ViewHolder();
                    viewHolder.station_name_textview = (TextView) convertView.findViewById(R.id.station_name_textview);
                    viewHolder.blackmarket_textview = (TextView) convertView.findViewById(R.id.blackmarket_textview);
                    viewHolder.station_type_icon = (ImageView) convertView.findViewById(R.id.station_type_icon);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
                    viewHolder.station_name_textview.setTypeface(font);
                    viewHolder.blackmarket_textview.setTypeface(font);
                    //viewHolder.last_visited_textview = (TextView) convertView.findViewById(R.id.last_visited_textview);

					convertView.setTag(viewHolder);
				}
				else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
			
				final Station station = stations[position];
/*
				viewHolder.system_name_textview.setText(station.getName());
                viewHolder.last_visited_textview.setText(Utils.getDateAsTimePassed(station.getLastVisited()));

                String stationCountText = "No stations entered here yet";
                stationCountText = station.getStationCount() + " stations";

                viewHolder.station_count_textView.setText(stationCountText);
*/
                viewHolder.station_name_textview.setText(station.getName());


                Object hasMarket = station.getMisc().get(AppConstants.BLACK_MARKET);

                if(hasMarket != null) {
                    boolean hasBlackMarket = (boolean) station.getMisc().get(AppConstants.BLACK_MARKET);

                    if(hasBlackMarket) {
                        viewHolder.blackmarket_textview.setVisibility(View.VISIBLE);
                    }
                    else {
                        viewHolder.blackmarket_textview.setVisibility(View.GONE);
                    }
                }
                else {
                    viewHolder.blackmarket_textview.setVisibility(View.GONE);
                }

                String station_type_string = (String) station.getMisc().get(AppConstants.STATION_TYPE);

                if(station_type_string == null) {
                    viewHolder.station_type_icon.setVisibility(View.GONE);
                }
                else {
                    viewHolder.station_type_icon.setVisibility(View.VISIBLE);

                    if("station".equals(station_type_string)) {
                        viewHolder.station_type_icon.setImageResource(R.drawable.station);
                    }
                    else {
                        viewHolder.station_type_icon.setImageResource(R.drawable.outpost);
                    }
                }


                String lastVisitedString = Utils.getDateAsTimePassed(station.getLastVisited());
                //viewHolder.last_visited_textview.setText(lastVisitedString);

				convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
						Intent intent = new Intent(activity, CommoditiesListActivity.class);
                        intent.putExtra(AppConstants.STATION_NAME, station.getName());
                        intent.putExtra(AppConstants.SYSTEM_NAME, activity.getIntent().getStringExtra(AppConstants.SYSTEM_NAME));

						activity.startActivity(intent);
                    }
                });

                final TradesListAdapter thisAdapter = this;



			break;
		}
		
		return convertView;
	}

	private static class ViewHolder {
		TextView station_name_textview, blackmarket_textview;//, last_visited_textview;
        ImageView station_type_icon;
	}
	
	public void setSystem(System system) {
		this.system = system;

        Station[] stationArray = new Station[system.getStations().size()];
        this.stations = system.getStations().toArray(stationArray);

        notifyDataSetChanged();
	}

}






