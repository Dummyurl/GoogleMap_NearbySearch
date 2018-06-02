package com.example.waterflower124.googlemap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    ArrayList<ListViewItem> list;
    Context c;
    Fragment_GoogleMap fragment_googleMap;
    View row;

    CustomListAdapter(Context context) {
        c = context;
        list = new ArrayList<ListViewItem>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = layoutInflater.inflate(R.layout.list_item, parent, false);
        TextView m_ListItemPlaceName = (TextView)row.findViewById(R.id.placename);
        TextView m_ListItemVicinty = (TextView)row.findViewById(R.id.vicinty);
        ImageView m_ListItemImage = (ImageView)row.findViewById(R.id.imageView);
        TextView m_ListItemRating = (TextView)row.findViewById(R.id.rating);
        RatingBar m_ListItemRatingBar = (RatingBar)row.findViewById(R.id.rating_bar);
        TextView m_ListItemReview = (TextView)row.findViewById(R.id.review);
        TextView m_ListItemDistance = (TextView)row.findViewById(R.id.distance);
        TextView m_ListItemOpenStatus = (TextView)row.findViewById(R.id.opening_now);
        ImageView m_ListItemCallImage = (ImageView)row.findViewById(R.id.call_image);
        TextView m_ListItemCallStatus = (TextView)row.findViewById(R.id.call_status);
        //invisible
        TextView m_ListItemPlaceID = (TextView) row.findViewById(R.id.place_id);
        TextView m_ListItemLat = (TextView)row.findViewById(R.id.lat);
        TextView m_ListItemLng = (TextView)row.findViewById(R.id.lng);
        TextView m_ListItemCallNumber = (TextView)row.findViewById(R.id.call_number);

        LinearLayout mTextLayout = (LinearLayout)row.findViewById(R.id.list_itemtext);
        LinearLayout mCallLayout = (LinearLayout)row.findViewById(R.id.telephone_call);

        mTextLayout.setTag(position);


        ListViewItem tmp = list.get(position);

        m_ListItemPlaceName.setText(tmp.placeName);
        m_ListItemVicinty.setText(tmp.vicinty);
        if(!tmp.rating.isEmpty()) {
            m_ListItemRating.setText(tmp.rating);
            m_ListItemRatingBar.setRating(Float.parseFloat(tmp.rating));
        } else {
            m_ListItemRating.setText("N/A");
            m_ListItemRatingBar.setRating(0f);
        }
        m_ListItemReview.setText("(" + tmp.review + ")");
        m_ListItemDistance.setText(tmp.distance + "km");
        m_ListItemOpenStatus.setText(tmp.open_now);

        //invisible
        m_ListItemPlaceID.setText(tmp.place_id);
        m_ListItemLat.setText(tmp.latitude);
        m_ListItemLng.setText(tmp.longitude);
        if(tmp.phonenumber.isEmpty()) {
            m_ListItemCallImage.setImageResource(R.drawable.phone_call_disable);
            m_ListItemCallStatus.setText("N/A");
            m_ListItemCallNumber.setText("");
        } else
            m_ListItemCallNumber.setText(tmp.phonenumber);
//        m_ListItemImage.setImageURI((android.net.Uri)tmp.pictureuri);

        mTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posi = (Integer) v.getTag();

                Intent intent = new Intent("Detail Map");
                intent.putExtra("place_id", (((TextView) v.findViewById(R.id.place_id)).getText().toString()).trim());
                intent.putExtra("lat", (((TextView) v.findViewById(R.id.lat)).getText().toString()).trim());
                intent.putExtra("lng", (((TextView) v.findViewById(R.id.lng)).getText().toString()).trim());
                intent.putExtra("placename", (((TextView) v.findViewById(R.id.placename)).getText().toString()).trim());
                intent.putExtra("vicinty", (((TextView) v.findViewById(R.id.vicinty)).getText().toString()).trim());
//                intent.putExtra("phone_number", (((TextView) v.findViewById(R.id.call_number)).getText().toString()).trim());
                c.sendBroadcast(intent);

            }
        });

        mCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = (((TextView) v.findViewById(R.id.call_number)).getText().toString()).trim();
                if(!phone_number.isEmpty()) {
                    Toast.makeText(parent.getContext(), phone_number, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(parent.getContext(), "NO CALL NUMBER", Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }
}
