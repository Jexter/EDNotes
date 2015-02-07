package myapps.alex.se.ednotes.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.activities.StationListActivity;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.DNApplication;
import myapps.alex.se.ednotes.model.MiniSystem;
import myapps.alex.se.ednotes.persistence.Storage;


public class SystemListAdapter extends BaseAdapter {

	final private Activity activity;
    private final int SYSTEM = 0;
    private final int NO_SYSTEMS = 1;
    private MiniSystem[] miniSystems;

	public SystemListAdapter(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public int getViewTypeCount(){
		return 2;
	}

	public int getItemViewType(int position) {
		if(miniSystems == null || miniSystems.length == 0) {
			return NO_SYSTEMS;
		}
		else {
			return SYSTEM;
		}
	}
	

	@Override
	public int getCount() {
		if(miniSystems == null || miniSystems.length == 0) {
			return 0;
		}
		
		return miniSystems.length;
	}
	
	@Override
	public Object getItem(int position) {
		return miniSystems[position];
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
			
			case SYSTEM:
				if (convertView == null) {		
					LayoutInflater layoutInflater_issue = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater_issue.inflate(R.layout.system_list_item, null);
		
					viewHolder = new ViewHolder();
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
                    viewHolder.system_name_textview = (TextView) convertView.findViewById(R.id.system_name_textview);
                    viewHolder.system_allegiance_textview = (TextView) convertView.findViewById(R.id.system_allegiance_textview);
                    viewHolder.system_name_textview.setTypeface(font);
                    viewHolder.system_allegiance_textview.setTypeface(font);

					convertView.setTag(viewHolder);
				}
				else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
			
				final MiniSystem miniSystem = miniSystems[position];
				
				viewHolder.system_name_textview.setText(miniSystem.getName());

                Object allegianceObject = miniSystem.getMisc().get(AppConstants.ALLEGIANCE_MISC_KEY);

                if(allegianceObject != null) {
                    String allegianceString = (String) allegianceObject;

                    if(!"NONE".equals(allegianceString)) {
                        viewHolder.system_allegiance_textview.setText(allegianceString);
                        if("EMPIRE".equals(allegianceString)) {
                            viewHolder.system_allegiance_textview.setTextColor(DNApplication.getContext().getResources().getColor(R.color.color_red));
                        }
                        if("INDEPENDANT".equals(allegianceString)) {
                            viewHolder.system_allegiance_textview.setTextColor(DNApplication.getContext().getResources().getColor(R.color.color_green));
                        }
                        if("FEDERATION".equals(allegianceString)) {
                            viewHolder.system_allegiance_textview.setTextColor(DNApplication.getContext().getResources().getColor(R.color.color_blue));
                        }
                        if("ALLIANCE".equals(allegianceString)) {
                            viewHolder.system_allegiance_textview.setTextColor(DNApplication.getContext().getResources().getColor(R.color.color_orange));
                        }
                        viewHolder.system_allegiance_textview.setVisibility(View.VISIBLE);
                    }
                    else {
                        viewHolder.system_allegiance_textview.setVisibility(View.GONE);
                    }
                }
                else {
                    viewHolder.system_allegiance_textview.setVisibility(View.GONE);
                }



				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity, StationListActivity.class);
						intent.putExtra(AppConstants.SYSTEM_NAME, miniSystem.getName());
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
                                .setTitle("Delete system: " + miniSystem.getName())
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                ArrayList<MiniSystem> minis = Storage.deleteSystem(miniSystem);
                                                miniSystems = new MiniSystem[minis.size()];
                                                miniSystems = minis.toArray(miniSystems);

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
		TextView system_name_textview, system_allegiance_textview;//, station_count_textView;//, last_visited_textview;
	}
	
	public void setSystems(MiniSystem[] miniSystems) {
		this.miniSystems = miniSystems;
        notifyDataSetChanged();
	}

}






