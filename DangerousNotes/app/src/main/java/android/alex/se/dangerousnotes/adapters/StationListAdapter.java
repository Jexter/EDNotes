package android.alex.se.dangerousnotes.adapters;


import android.alex.se.dangerousnotes.R;
import android.alex.se.dangerousnotes.activities.CommoditiesListActivity;
import android.alex.se.dangerousnotes.common.AppConstants;
import android.alex.se.dangerousnotes.common.Utils;
import android.alex.se.dangerousnotes.model.*;
import android.alex.se.dangerousnotes.model.System;
import android.alex.se.dangerousnotes.persistence.Storage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class StationListAdapter extends BaseAdapter {

	final private Activity activity;
    private final int STATION = 0;
    private final int NO_SYSTEMS = 1;
    private Station[] stations;
    private System system;

	public StationListAdapter(Activity activity) {
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
					viewHolder.last_visited_textview = (TextView) convertView.findViewById(R.id.last_visited_textview);

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
                String lastVisitedString = Utils.getDateAsTimePassed(station.getLastVisited());
                viewHolder.last_visited_textview.setText(lastVisitedString);

				convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
						Intent intent = new Intent(activity, CommoditiesListActivity.class);
                        intent.putExtra(AppConstants.STATION_NAME, station.getName());
                        intent.putExtra(AppConstants.SYSTEM_NAME, activity.getIntent().getStringExtra(AppConstants.SYSTEM_NAME));

						activity.startActivity(intent);
                    }
                });

                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                        // set prompts.xml to alertdialog builder
//                        View promptsView = li.inflate(R.layout.commodity_info_prompt, null);
//                        alertDialogBuilder.setView(promptsView);

                        alertDialogBuilder
                                .setTitle("Delete station: " + station.getName())
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                // get user input and set it to result
                                                // edit text
                                                Storage.updateAndSaveStationInSystem(system, station);
                                                setSystem(Storage.loadSystem(system.getName()));
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                        return false;
                    }
                });

			break;
		}
		
		return convertView;
	}

	private static class ViewHolder {
		TextView station_name_textview, last_visited_textview;
	}
	
	public void setSystem(System system) {
		this.system = system;

        Station[] stationArray = new Station[system.getStations().size()];
        this.stations = system.getStations().toArray(stationArray);

        notifyDataSetChanged();
	}

}






