package com.innowireless.xcal.mobile4g;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ManagerAdapter extends BaseAdapter {



    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<MyItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MyItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_user_setting, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username) ;
        TextView tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime) ;
        ImageView iv_logout = (ImageView) convertView.findViewById(R.id.iv_logout) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        MyItem myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
//        iv_img.setImageBitmap(myItem.getIcon());
//        Glide.with(convertView).load(myItem.getIcon()).into(iv_img);
        tv_username.setText(myItem.getUsername());
        tv_datetime.setText(myItem.getDatetime());

        iv_logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.d("ManagetAdapter", "iv_logout - click");
                SQLiteHelper mSQLiteHelper;
                mSQLiteHelper = new SQLiteHelper(context.getApplicationContext(), "MyInsta.db", null, 1);
                mSQLiteHelper.deletUsetInfoDatas(myItem.getUserid(), myItem.getUsername(), myItem.getDatetime());
            }
        });


        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String username, String datetime, String userid) {

        MyItem mItem = new MyItem();

        /* MyItem에 아이템을 setting한다. */
        mItem.setUsername(username);
        mItem.setDatetime(datetime);
        mItem.setUserid(userid);


        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }
}