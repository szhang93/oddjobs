package com.example.oddjobs2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

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

        skillSuggestionsList = new ArrayList<String>();
        //hardcoded for now
        skillSuggestionsList.add("apple");
        skillSuggestionsList.add("pear");
        skillSuggestionsList.add("banana");
        skillSetSet = new HashSet<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_dropdown_item_1line, skillSuggestionsList);

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
            public boolean onQueryTextChange(String newText){
                return onSkillSearchQuery(newText);
            }
            @Override
            public boolean onQueryTextSubmit(String query){
                return onSkillSearchSubmit(query);
            }
        });


        return view;
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


    public void updateSkillSet(String query){
        TextView skill_text = new TextView(mContext);
        Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
        skill_text.setText(query);
        if(skillSetSet.size()%2==0){
            //Alternate colors.
            skill_text.setBackgroundResource(R.color.tealButton);
        }
        else{
            skill_text.setBackgroundResource(R.color.yellowButton);
        }
        skill_text.setPadding(10,5,5,10);
        skillSet.addView(skill_text);
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}