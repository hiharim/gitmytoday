package com.example.mytoday;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends ArrayAdapter<DiaryData> implements Filterable {

    Context myContext;
    LayoutInflater inflater;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList (원본데이터)
    private ArrayList<DiaryData> list = new ArrayList<DiaryData>();

    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트보유
    private ArrayList<DiaryData> calendarItemList=list;
    Filter listFilter;

    //itemPosition: adapter의 position값을 MainActivity에서도 사용할 수 있기 위해서 itemPosition을 전역변수로 빼두었다
    public int itemPosition;


    public CalendarAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DiaryData> objects) {
        super(context, resource, objects);
        myContext = context;
        list = objects;
        inflater = LayoutInflater.from(context);

    }

    // Container Class for item
    private class ViewHolder {
        ImageView iv_photo;
        TextView tv_content;
        TextView tv_date;
        TextView tv_time;
        TextView tv_feeling;
        TextView tv_location;
    }

    public void addItem(DiaryData item) {
        list.add(item);
    }

    public void setItem(DiaryData item) {
        list.set(itemPosition, item);
    }


    /**
     * getView() 메소드는 실제로 CustomListView의 아이템을 구성하는 메소드로, 사용하려는 레이아웃xml파일로부터
     * View객체를 생성하고 그 View를 바탕으로 필요한 정보를 설정한다
     * getView() 메소드에서 convertView가 null일경우에만 LayoutInflater를 통해 View객체를 생성하도록 하여
     * 효율적으로 레이아웃을 구성할 수 있도록 한다.
     * 그리고 레이아웃.xml파일로부터 생성된 View로부터 레이아웃 안의 다른 View를 불러와야하므로
     * findViewById를 통해 View를 가져올때, 위에서 생성한 view를 이용하여 view.findViewById();를해야한다는점을 주의해라!
     * 내가 아이템이 계속 중복되서 겹쳤던 이유 : getView를 잘못 쓰고 있었음.......
     */
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    public View getView(final int position, View convertView, ViewGroup parent) {
        //convertView : 각 아이템을 구성하는 view
        View view = convertView;
        itemPosition=position;

        if (view == null) {
            //아이템을 구성하는 convertView가 null일경우
            //layout xml파일로부터 view객체를 생성
            view = inflater.inflate(R.layout.item_diary, null);
        }

            /*
            생성한 view 객체로부터 설정해야하는 뷰를 획득 이 경우 위에서 생성한 view를 가져오는 것이므로
            view.findViewById(R.id.view_id) 를 통해 획득해야함
             */
        ImageView iv_photo = (ImageView) view.findViewById(R.id.item_diary_iv_photo);
        TextView tv_content = (TextView) view.findViewById(R.id.item_diary_tv_content);
        TextView tv_date = (TextView) view.findViewById(R.id.item_diary_tv_date);
        TextView tv_time = (TextView) view.findViewById(R.id.item_diary_tv_time);
        TextView tv_feeling = (TextView) view.findViewById(R.id.item_diary_tv_feeling);
        TextView tv_location = (TextView) view.findViewById(R.id.item_diary_tv_location);

        //listView포지션에 따른 데이터를 로드
        final DiaryData diaryData = calendarItemList.get(position);

        //listView포지션에 따른 데이터를 뷰에 세팅
        iv_photo.setImageURI(Uri.parse(diaryData.getPhoto()));
        tv_content.setText(diaryData.getContent());
        tv_date.setText(diaryData.getDate());
        tv_time.setText(diaryData.getTime());
        tv_feeling.setText(diaryData.getFeelings());
        tv_location.setText(diaryData.getLocation());

        return view;
    }

    @Override
    public void remove(DiaryData object) {
        list.remove(object);
        notifyDataSetChanged();
    }

    public List<DiaryData> getMyList() {
        return list;
    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override // 리스트 뷰가 어댑터에게 데이터 몇 개 가지고 있니? 물어보는 함수
    public int getCount() {
        return calendarItemList.size();
    }

    // 지정한 위치(postion)에 있는 데이터와 관계된 아이템의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }


    // 지정한 위치(position)에 있는 데이터 리턴
    public DiaryData getItem(int position) {
        return calendarItemList.get(position);
    }


    @Override
    public Filter getFilter() {

        if(listFilter ==null){
            listFilter=new CalendarAdapter.ListFilter();
        }
        return listFilter;
    }


    //Filter 클래스 추가 및 구현
    private class ListFilter extends Filter {

        //필터링을 수행하는 메서드
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint ==null || constraint.length() ==0){
                results.values=list;
                results.count=list.size();
            }else{
                ArrayList<DiaryData> itemList=new ArrayList<DiaryData>();
                for(DiaryData item : list) {
                    if (item.getDate().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        itemList.add(item) ;
                    }
                }
                results.values=itemList;
                results.count=itemList.size();
            }
            return results;
        }

        // performFiltering()메서드에서 필터링된 결과를 UI에 갱신하는 메서드
        // 어댑터를 통한 리스트뷰 갱신작업
        @Override
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            calendarItemList=(ArrayList<DiaryData>) results.values;

            if(results.count>0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    }



}
