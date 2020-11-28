package com.example.mytoday;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import noman.googleplaces.Place;

//지역검색 어댑터
public class PlaceAdapter extends BaseAdapter {


    //데이터 그릇들의 집합을 정의
    private ArrayList<PlaceData> mItems=new ArrayList<>();

    LayoutInflater inflater;
    Context myContext;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        // 커스텀 리스트뷰의 xml을 inflate
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_place, parent, false);
        }


        /* 커스텀 리스트뷰 xml에 있는 속성값들을 정의 */

        TextView title=(TextView)convertView.findViewById(R.id.item_place_title);
        TextView address=(TextView)convertView.findViewById(R.id.item_place_address);
        TextView roadAddress=(TextView)convertView.findViewById(R.id.item_place_roadAddress);


        /* 데이터를 담는 그릇 정의 */
        final PlaceData naverPlace = getItem(position);

        /* 해당 그릇에 담긴 정보들을 커스텀 리스트뷰 xml의 각 TextView에 뿌려줌 */
        title.setText(naverPlace.getTitle());
        address.setText(naverPlace.getAddress());
        roadAddress.setText(naverPlace.getRoadAddress());


        return convertView;
    }

    /* 네이버 지역 검색 중 건물명,지번주소, 도로명주소를 데이터그릇에 담음 */
    public void addItem(String title, String address, String roadAddress) {

        PlaceData mItem = new PlaceData();

        mItem.setTitle(title);
        mItem.setAddress(address);
        mItem.setRoadAddress(roadAddress);

        /* 데이터그릇 mItem에 담음 */
        mItems.add(mItem);

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public PlaceData getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
