package com.example.duskagk.jockgo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ExListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    //private ArrayList<CheckBox> groupCheckBox = new ArrayList<CheckBox>();
    //private ArrayList<ArrayList<CheckBox>> childCheckBox = new ArrayList<ArrayList<CheckBox>>();
    private CheckBox[][] childCheckBox;
    private CheckBox[] groupCheckBox;
    private ArrayList<ArrayList<Integer>> tag;

    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExListAdapter(Context context, List<String> listDataHeader,
                         HashMap<String, List<String>> listChildData, ArrayList gTag) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        groupCheckBox = new CheckBox[listDataHeader.size()];
        this.tag = gTag;

        int size = 0;
        for (int i = 0; listChildData.size() > i; i++){
            if (listChildData.get(listDataHeader.get(i)).size() > size)
                size = listChildData.get(listDataHeader.get(i)).size();
        }
        childCheckBox = new CheckBox[listDataHeader.size()][size];
    }


    public void setCheck(int groupPosition, int childPosititon){
        childCheckBox[groupPosition][childPosititon].setChecked(!( childCheckBox[groupPosition][childPosititon].isChecked() ));
    }

    public int[] getTag(){
        ArrayList<Integer> arrayTag = new ArrayList<Integer>();
        for (int i = 0; i < tag.size(); i++){
            for (int j = 0; j < tag.get(i).size(); j++){
                if(childCheckBox[i][j] != null && childCheckBox[i][j].isChecked())
                    arrayTag.add(tag.get(i).get(j));
            }
        }
        int[] selectTag = new int[arrayTag.size()];
        for (int i = 0; i < arrayTag.size(); i++)
            selectTag[i] = arrayTag.get(i);

        return selectTag;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.unit_item, null);

        }
        childCheckBox[groupPosition][childPosition] = (CheckBox) convertView.findViewById(R.id.lbitemcheck);


        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    public CheckBox getGroupCheckBox(int grupPosition){
        return groupCheckBox[grupPosition];
    }

    public CheckBox getChildCheckBox(int groupPosition, int childPosititon){
        return childCheckBox[groupPosition][childPosititon];
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.unit_group, null);
//            groupCheckBox.add((CheckBox)convertView.findViewById(R.id.lblListHeaderCheckbox));

        }

        groupCheckBox[groupPosition] = (CheckBox)convertView.findViewById(R.id.lblListHeaderCheckbox);

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}


