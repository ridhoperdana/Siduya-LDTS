package net.ridhoperdana.siduya;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RIDHO on 4/23/2016.
 */
public class PlaceAdapter extends ArrayAdapter<String> implements Filterable{

    private ArrayList<String> resultList;
//    private Filter filter;

    Context mContext;
    int mResource;

    PlaceAPI mPlaceAPI = new PlaceAPI();

//    PlaceAPI mPlaceAPI = new PlaceAPI();

    public PlaceAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
//                resultList = new ArrayList<String>()
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());
//                    Log.d("result list->", resultList.toString());

                    filterResults.values = resultList;
//                    Log.d("filter list->", filterResults.values.toString());
                    filterResults.count = resultList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}
