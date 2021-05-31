package com.innowireless.xcal.mobile4g;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyFeedAdapter extends BaseAdapter {

    private LayoutInflater inflate;
    private ListViewAdapter.ViewHolder viewHolder;
    private List<HashMap<String, String>> list;
//    ArrayList<HashMap<String, String>>

    public MyFeedAdapter(Context context, List<HashMap<String, String>> list){
        // MainActivity 에서 데이터 리스트를 넘겨 받는다.
        this.list = list;
        this.inflate = LayoutInflater.from(context);
    }

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
            convertView = inflater.inflate(R.layout.listview_custom, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img) ;
        ImageView iv_ic = (ImageView) convertView.findViewById(R.id.iv_ic) ;
        ImageView iv_video = (ImageView) convertView.findViewById(R.id.iv_video) ;
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name) ;
        TextView tv_caption = (TextView) convertView.findViewById(R.id.tv_caption) ;
        TextView tv_contents = (TextView) convertView.findViewById(R.id.tv_contents) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        MyItem myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
//        iv_img.setImageBitmap(myItem.getIcon());
        Glide.with(convertView).load(myItem.getIcon()).into(iv_img);
        tv_caption.setText(myItem.getCaption());
        tv_name.setText(myItem.getName());
        tv_contents.setText(myItem.getContents());

        iv_video.setVisibility(View.GONE);
        iv_ic.setVisibility(View.GONE);

        if(myItem.getContents().equals("VIDEO")){
            iv_video.setVisibility(View.VISIBLE);
        }
        else if (myItem.getContents().equals("IMAGE")){

        }
        else{
            iv_ic.setVisibility(View.VISIBLE);
        }

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String img, String caption, String name, String contents) {

        MyItem mItem = new MyItem();

        /* MyItem에 아이템을 setting한다. */
        mItem.setIcon(img);
        mItem.setCaption(caption);
        mItem.setName(name);
        mItem.setContents(contents);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }
}