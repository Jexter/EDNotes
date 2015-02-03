package myapps.alex.se.ednotes.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.DNApplication;
import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.model.Availability;
import myapps.alex.se.ednotes.model.Commodity;
import myapps.alex.se.ednotes.model.CommodityCategory;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.model.System;
import myapps.alex.se.ednotes.persistence.Storage;


public class CommoditiesListAdapter extends BaseAdapter {

	final private Activity activity;
    private final int CATEGORY = 0;
    private final int COMMODITY = 1;
    private final int NO_COMMODITIES = 2;
    private Station station;
    private System system;
    private int totalRowCount;
    private int[] positionsForHeaders;
    private int currentHeaderPositionIndex;
    Button supply_button = null;
    Button demand_button = null;

	public CommoditiesListAdapter(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public int getViewTypeCount(){
		return 3;
	}

	public int getItemViewType(int position) {
		if(station == null || station.getCategories() ==  null || station.getCategories().size() == 0) {
			return NO_COMMODITIES;
		}
		else if((currentHeaderPositionIndex = Utils.calculateWhichHeaderThisIs(positionsForHeaders, position)) != -1) {

			return CATEGORY;
		}
        else {
            currentHeaderPositionIndex = Utils.calculateHighestHeaderIndexForThisPosition(positionsForHeaders, position);
            return COMMODITY;
        }
	}
	

	@Override
	public int getCount() {
		if(station == null || station.getCategories() ==  null || station.getCategories().size() == 0) {
			return 0;
		}

       	return totalRowCount;
	}
	
	@Override
	public Object getItem(int position) {
		return null;//station[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		switch (getItemViewType(position)) {
			case NO_COMMODITIES:
/*
				LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.no_content_item, null);
				TextView no_content_textview = (TextView) convertView.findViewById(R.id.no_content_textview);
				no_content_textview.setText(GTApplication.getStringResource("no_issues_text"));
*/
            break;
			
			case CATEGORY:
				if (convertView == null) {		
					LayoutInflater layoutInflater_issue = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater_issue.inflate(R.layout.category_list_item, null);
		
					viewHolder = new ViewHolder();
                    viewHolder.category_name_textview = (TextView) convertView.findViewById(R.id.category_name_textview);

                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
                    viewHolder.category_name_textview.setTypeface(font);

					convertView.setTag(viewHolder);
				}
				else {
					viewHolder = (ViewHolder) convertView.getTag();
				}

				final CommodityCategory category = station.getCategories().get(currentHeaderPositionIndex);
				
				viewHolder.category_name_textview.setText(category.getName());


			break;

            case COMMODITY:
                if (convertView == null) {
                    LayoutInflater layoutInflater_issue = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater_issue.inflate(R.layout.commodity_list_item, null);

                    viewHolder = new ViewHolder();
                    viewHolder.commodity_name_textview = (TextView) convertView.findViewById(R.id.commodity_name_textview);
                    viewHolder.price_textview = (TextView) convertView.findViewById(R.id.price_textview);
                    viewHolder.supply_textview = (TextView) convertView.findViewById(R.id.supply_textview);

                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
                    viewHolder.commodity_name_textview.setTypeface(font);
                    viewHolder.price_textview.setTypeface(font);
                    viewHolder.supply_textview.setTypeface(font);

                    //viewHolder.commodity_supply_level_textview = (TextView) convertView.findViewById(R.id.commodity_supply_level_textview);

                    convertView.setTag(viewHolder);
                }
                else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                try {
                    //currentHeaderPositionIndex = Utils.contains(positionsForHeaders, position);

                    final Commodity commodity = station.getCategories().get(currentHeaderPositionIndex).getCommodities().get(position - positionsForHeaders[currentHeaderPositionIndex] - 1);
                    String priceString = "";

                    if(commodity.getPrice() != 0) {
                        priceString = String.valueOf(commodity.getPrice()) + " CR";
                    }

                    Availability availability = commodity.getAvailability();

                    String supplyString = "";

                    if(availability != null) {
                        supplyString = String.valueOf(commodity.getAvailability());

                        if(availability == Availability.DEMAND) {
                            viewHolder.supply_textview.setTextColor(DNApplication.getContext().getResources().getColor(R.color.color_six));
                        }
                        else {
                            viewHolder.supply_textview.setTextColor(DNApplication.getContext().getResources().getColor(R.color.color_seven));
                        }
                    }

                    viewHolder.supply_textview.setText(supplyString);
                    viewHolder.price_textview.setText(priceString);






                    viewHolder.commodity_name_textview.setText(commodity.getName());


                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // get prompts.xml view
                                LayoutInflater li = LayoutInflater.from(activity);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                                // set prompts.xml to alertdialog builder
                                View promptsView = li.inflate(R.layout.commodity_info_prompt, null);
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText) promptsView.findViewById(R.id.price_edittext);

                                supply_button = (Button) promptsView.findViewById(R.id.supply_button);
                                demand_button = (Button) promptsView.findViewById(R.id.demand_button);

                                TextView price_title_textview = (TextView) promptsView.findViewById(R.id.price_title_textview);
                                TextView popup_commodity_name_textview = (TextView) promptsView.findViewById(R.id.popup_commodity_name_textview);
                                TextView demand_title_textview = (TextView) promptsView.findViewById(R.id.demand_title_textview);


                                Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
                                supply_button.setTypeface(font);
                                demand_button.setTypeface(font);
                                userInput.setTypeface(font);
                                userInput.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                price_title_textview.setTypeface(font);
                                demand_title_textview.setTypeface(font);
                                popup_commodity_name_textview.setTypeface(font);

                                popup_commodity_name_textview.setText(commodity.getName());
                                demand_button.setPressed(commodity.getAvailability() == Availability.DEMAND ? true : false);
                                supply_button.setPressed(commodity.getAvailability() == Availability.DEMAND ? false : true);
                                userInput.setText(commodity.getPrice()>0?String.valueOf(commodity.getPrice()):"");

                            demand_button.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                                        demand_button.setPressed(true);
                                        supply_button.setPressed(false);
                                    }
/*
                                    if(event.getAction() == MotionEvent.ACTION_UP) {
                                        demand_button.setPressed(true);
                                        supply_button.setPressed(false);
                                    }
*/
                                    return true;
                                }
                            });

                            supply_button.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
  /*
                                    if(event.getAction() == MotionEvent.ACTION_UP) {
                                        demand_button.setPressed(false);
                                        supply_button.setPressed(true);
                                    }
*/
                                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                                        demand_button.setPressed(false);
                                        supply_button.setPressed(true);
                                    }

                                    return true;
                                }
                            });




                            // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK", null)
                                        .setNegativeButton("CANCEL", null);

                                // create alert dialog
                                final AlertDialog alertDialog = alertDialogBuilder.create();

                                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                    @Override
                                    public void onShow(DialogInterface dialog) {

                                        Button pos = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                        pos.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                String comPrice = userInput.getText().toString();
                                                boolean isValid = Utils.validateCommodityPrice(comPrice);

                                                if(isValid || "".equals(comPrice)) {
                                                    if("".equals(comPrice)) {
                                                        comPrice = "0";
                                                    }

                                                    commodity.setPrice(Integer.valueOf(comPrice));
                                                    if(commodity.getPrice()==0) {
                                                        commodity.setAvailability(null);
                                                    }
                                                    else {
                                                        commodity.setAvailability(supply_button.isPressed() ? Availability.SUPPLY : Availability.DEMAND);
                                                    }


                                                    system.getMisc().put(AppConstants.STATION_TYPE, stationType);
                                                    Storage.saveSystem(system);
                                                    notifyDataSetChanged();

                                                    alertDialog.dismiss();
                                                }
                                                else {
                                                    alertDialog.findViewById(R.id.price_popup_error_message).setVisibility(View.VISIBLE);
                                                }


                                            }
                                        });

                                        // Cencel button
                                        Button neg = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                        neg.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertDialog.dismiss();
                                            }
                                        });
                                    }
                                });

                                // show it
                                alertDialog.show();
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(font);
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(font);

                            }

                    });

                }
                catch(Exception e) {
                    Log.d("Aj", "");
                }

                break;

        }
		
		return convertView;
	}

/*
    public void onSupplyToggleClicked(View v) {

        if(supply_button.isChecked()) {
            demand_button.setChecked(false);
        }
    }

    public void onDemandToggleClicked(View v) {
        if(demand_button.isChecked()) {
            supply_button.setChecked(false);
        }
    }
*/

	private static class ViewHolder {
		TextView commodity_name_textview, price_textview, supply_textview, category_name_textview;
	}
	
	public void setStationAndSystem(Station station, System system) {
		this.station = station;
        this.system = system;
        totalRowCount = Utils.getTotalRowCountForCommoditiesListInStation(station);
        positionsForHeaders = Utils.getPositionsForHeaders(station);

        notifyDataSetChanged();
	}

}






