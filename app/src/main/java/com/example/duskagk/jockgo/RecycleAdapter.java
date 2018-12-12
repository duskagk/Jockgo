package com.example.duskagk.jockgo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    private static final String TAG = "RecycleAdapter";

    private ArrayList<String> mNames= new ArrayList<>();
    private ArrayList<String> mImages= new ArrayList<>();
    private Context mContext;
    private ArrayList<Integer> bNo;

    private int[] tag;

    private ArrayList<ArrayList<Integer>> gTag;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;



    public RecycleAdapter(Context mContext, ArrayList<String> mNames, ArrayList<String> mImages, ArrayList mTag) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.bNo = mTag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext).asBitmap().load(mImages.get(position))
                .into(holder.image);


        holder.name.setTag(bNo.get(position));
        holder.name.setText(mNames.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked an image:" + mNames.get(position));

                NetworkTask networkTask = new NetworkTask("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/subject?b_no=" + bNo.get(position), null, "GET");
                try {
                    String result = networkTask.execute().get();

                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {
                        prepareListData(jsonArray);

                        AlertDialog.Builder mBulid = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inf = (LayoutInflater) v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                        final View mv = inf.inflate(R.layout.subject_dialog, null);
                        EditText q_num = (EditText) mv.findViewById(R.id.q_cnt);

                        expListView = (ExpandableListView) mv.findViewById(R.id.lvExp);
                        //expListView.setAddStatesFromChildren(true);
                        listAdapter = new ExListAdapter(v.getContext(), listDataHeader, listDataChild, gTag);
                        expListView.setAdapter(listAdapter);



                        expListView.setOnGroupClickListener(new OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                Toast.makeText(v.getContext(),
                                        ((ExListAdapter)parent.getExpandableListAdapter()).getGroup(groupPosition).toString() + " : " + ((ExListAdapter)parent.getExpandableListAdapter()).getGroupCheckBox(groupPosition).isChecked(),
                                        Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });


//                        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
//                            @Override
//                            public void onGroupExpand(int groupPosition) {
//                                Toast.makeText(mv.getContext(), listDataHeader.get(groupPosition)
//                                        + "Exapand", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });

//                        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(view.getContext(), id +" : OnItemClick" + position, Toast.LENGTH_LONG);
//                            }
//                        });
//
//                        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
//
//                            @Override
//                            public void onGroupCollapse(int groupPosition) {
//                                Toast.makeText(mv.getContext(),
//                                        listDataHeader.get(groupPosition) + " Collapsed",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
                        expListView.setOnChildClickListener(new OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                ((ExListAdapter)parent.getExpandableListAdapter()).setCheck(groupPosition, childPosition);
                                tag = ((ExListAdapter)parent.getExpandableListAdapter()).getTag();
                                return false;
                            }
                        });

                        TextView sub_na = (TextView) mv.findViewById(R.id.subName);
                        sub_na.setText(mNames.get(position));
                        Button stbtn = (Button) mv.findViewById(R.id.mock_start);
                        stbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (tag != null) {
                                    Intent intent = new Intent(v.getContext(), Mock_view.class);
                                    intent.putExtra("no", tag);
                                    intent.putExtra("b_no", bNo.get(position));
                                    v.getContext().startActivity(intent);
                                }
                            }
                        });
                        mBulid.setView(mv);
                        mBulid.create();
                        mBulid.show();
//                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "정상적으로 등록되지 않은 책입니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image_view);
            name=itemView.findViewById(R.id.name);
        }
    }

    private void prepareListData(JSONArray jsonArray) {
        String tmpHead = "";
        int index = -1;
        List<String> child = null;
        listDataHeader = new ArrayList<String>();
        listDataChild  = new HashMap<String, List<String>>();
        gTag = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i< jsonArray.length(); i++){
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (!(jsonObj.get("s_subject").toString().equals(tmpHead))){
                    if (index >= 0){
                        listDataChild.put(listDataHeader.get(index), child);
                    }
                    tmpHead = jsonObj.get("s_subject").toString();
                    listDataHeader.add(tmpHead);


                    gTag.add(new ArrayList<Integer>());
                    gTag.get(index+1).add(jsonObj.getInt("s_no"));
                    child = new ArrayList<String>();

                    child.add(jsonObj.get("s_denouement").toString());
                    index++;
                }else {
                    gTag.get(index).add(jsonObj.getInt("s_no"));
                    child.add(jsonObj.get("s_denouement").toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        listDataChild.put(listDataHeader.get(index), child);
//
//        for (int i = 0; i <= 10; i++) {
//            listDataHeader.add("Header" + i);
//
//            // Adding child data
//            List<String> child = new ArrayList<String>();
//            child.add("Child" + i);
//            child.add("Child" + i);
//
//            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
//
//        }
    }
}