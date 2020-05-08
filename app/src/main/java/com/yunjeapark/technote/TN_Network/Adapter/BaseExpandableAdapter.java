package com.yunjeapark.technote.TN_Network.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunjeapark.technote.R;

import java.util.ArrayList;

/**
 * Created by ium on 14. 2. 26.
 */
public class BaseExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> groupList = null;
    private ArrayList<ArrayList<String>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    public BaseExpandableAdapter(Context c, ArrayList<String> groupList, ArrayList<ArrayList<String>> childList){

        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public String getGroup(int groupPosition){
        return groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount(){
        return groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_network_board_subject_list_row, parent, false);
            viewHolder.tv_groupName = (TextView)v.findViewById(R.id.tv_group);
            viewHolder.iv_image = (ImageView)v.findViewById(R.id.iv_image);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        if(isExpanded){ // 확장했을 때 동작하는 코드
        }else{ //확장하지 않았을 때 동작하는 코드
        }

        viewHolder.tv_groupName.setText(getGroup(groupPosition));

        return v;
    }

    @Override
    public String getChild(int groupPosition, int childPosition){
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return childList.get(groupPosition).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_network_board_subject_list_row, null);
            viewHolder.tv_childName = (TextView) v.findViewById(R.id.tv_child);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));

        return v;
    }

    @Override
    public boolean hasStableIds(){return true;}

    @Override
    public boolean isChildSelectable(int groupPostion, int childPosition){return true;}



    class ViewHolder{
        public ImageView iv_image;
        public TextView tv_groupName;
        public TextView tv_childName;
    }
}