package com.example.oddjobs2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AddSkillsFragment extends Fragment {

    private SearchView skillsSearch;
    private ListView skillsList;
    private FlexboxLayout skillSet;

    private Context mContext;
    private Set<String> skillSetSet;
    private List<String> skillSuggestionsList;
    private List<String> skillSuggestionsListFiltered;
    ArrayList<String> skills = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public AddSkillsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_add_skills, container, false);

        skillsSearch = view.findViewById(R.id.skills_search_frag);
        skillsList = view.findViewById(R.id.skills_list_frag);
        skillSet = view.findViewById(R.id.skill_set_frag);

        /*
        skillSuggestionsList = new ArrayList<String>();
        //hardcoded for now
        skillSuggestionsList.add("apple");
        skillSuggestionsList.add("pear");
        skillSuggestionsList.add("banana");
        */
        DH dh = new DH();
        skillSuggestionsList = new ArrayList<String>();
        skillSuggestionsListFiltered = new ArrayList<String>();
        getSkills();


        skillSetSet = new HashSet<String>();

        adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_dropdown_item_1line, skillSuggestionsListFiltered);

        //https://stackoverflow.com/questions/21295328/android-listview-with-onclick-items
        skillsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String item = (String)parent.getItemAtPosition(position);
                onSkillSearchSubmit(item);
            }

        });

        skillsList.setAdapter(adapter);

        skillsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            //https://stackoverflow.com/questions/5713653/how-to-get-the-events-of-searchview-in-android
            @Override
            public boolean onQueryTextChange(String newText)
            {
                skillSuggestionsListFiltered.clear();
                for(String skill: skillSuggestionsList){

                    if(skill.contains(newText)){
                        skillSuggestionsListFiltered.add(skill);
                    }
                }
                adapter.notifyDataSetChanged();
                return onSkillSearchQuery(newText);
            }
            @Override
            public boolean onQueryTextSubmit(String query){
                return onSkillSearchSubmit(query);
            }
        });


        return view;
    }

    public void getSkills(){
        // TODO: pull from database job skills that match query
        // TODO: make skills order by most searched

        //TODO: right now, just gets popular job skills, not users
        DH dh = new DH();
        Query popularSkills = dh.mSkillMapJobs.orderByChild("count");
        popularSkills.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    skillSuggestionsList.add(0, data.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }


    public boolean onSkillSearchQuery(String newText){
        //Input nextText on generateSkillsList
        return true; //always true for now
    }

    public void generateSkillsList(){
        //Query database to get top most searched skills.
        //modifies skillSuggestionList
    }

    public boolean onSkillSearchSubmit(String query){
        //add skill to skillSet
        if(skillSetSet.add(query)){
            updateSkillSet(query);
        }
        return true; //always true for now
    }

    @SuppressLint("ResourceAsColor")
    public void updateSkillSet(String query){
        final TextView skill_text = new TextView(mContext);
       // Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
        skill_text.setText(query);
        skill_text.setTextColor(Color.WHITE);
        if(skillSetSet.size()%2==0){
            //Alternate colors.
            skill_text.setBackgroundResource(R.drawable.teal_button_bg);
        }
        else{
            skill_text.setBackgroundResource(R.drawable.yellow_button_bg);
        }
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 5, 10, 5);
        skill_text.setLayoutParams(params);
        skill_text.setPadding(30,30,30,30);

        skill_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mContext instanceof ProfileActivity){
                    ProfileActivity activity = (ProfileActivity) mContext;
                    activity.mySkills.remove(skill_text.getText().toString());
                    skillSetSet.remove(skill_text.getText().toString());
                    skill_text.setVisibility(View.GONE);
                } else if(mContext instanceof SwipeActivity) {
                    SwipeActivity activity = (SwipeActivity) mContext;
                    activity.mySkills.remove(skill_text.getText().toString());
                    skillSetSet.remove(skill_text.getText().toString());
                    skill_text.setVisibility(View.GONE);
                }
            }
        });


        skillSet.addView(skill_text);
        if(mContext instanceof ProfileActivity){
            ProfileActivity activity = (ProfileActivity) mContext;
            activity.mySkills.add(skill_text.getText().toString());
        } else if(mContext instanceof SwipeActivity) {
            SwipeActivity activity = (SwipeActivity) mContext;
            activity.mySkills.add(skill_text.getText().toString());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
