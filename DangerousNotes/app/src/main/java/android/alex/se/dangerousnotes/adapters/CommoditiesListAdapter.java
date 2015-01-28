package android.alex.se.dangerousnotes.adapters;


import android.alex.se.dangerousnotes.R;
import android.alex.se.dangerousnotes.common.Utils;
import android.alex.se.dangerousnotes.model.Availability;
import android.alex.se.dangerousnotes.model.Commodity;
import android.alex.se.dangerousnotes.model.CommodityCategory;
import android.alex.se.dangerousnotes.model.Station;
import android.alex.se.dangerousnotes.model.System;
import android.alex.se.dangerousnotes.persistence.Storage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class CommoditiesListAdapter extends BaseAdapter {

	final private Activity activity;
    private final int CATEGORY = 0;
    private final int COMMODITY = 1;
    private final int NO_COMMODITIES = 2;
    private Station station;
    private android.alex.se.dangerousnotes.model.System system;
    private int totalRowCount;
    private int[] positionsForHeaders;
    private int currentHeaderPositionIndex;

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

                    String supplyString = "";

                    if(commodity.getAvailability() != null) {
                        supplyString = String.valueOf(commodity.getAvailability()) + "";
                    }

                    viewHolder.price_textview.setText(priceString);
                    viewHolder.supply_textview.setText(supplyString);
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
                                Switch supply_button = (Switch) promptsView.findViewById(R.id.demand_button);
                                final Switch demand_button = (Switch) promptsView.findViewById(R.id.demand_button);
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
                                demand_button.setChecked(commodity.getAvailability()==Availability.DEMAND?false:true);
                                userInput.setText(commodity.getPrice()>0?String.valueOf(commodity.getPrice()):"");

                            // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
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
                                                                commodity.setAvailability(demand_button.isChecked() ? Availability.SUPPLY : Availability.DEMAND);
                                                            }
                                                            Storage.saveSystem(system);
                                                            notifyDataSetChanged();
                                                        }


                                                    }
                                                })
                                        .setNegativeButton("CANCEL",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();



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

    private void updatePriceOnCommodity(String price) {
        Log.d("New price on commodity:", price);
    }

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






