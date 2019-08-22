package com.nudge.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nudge.App;
import com.nudge.R;
import com.nudge.model.CategoryDetail;
import com.nudge.model.SharedPreferencesConstants;
import com.nudge.pojo.PersonnaDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ADVANTAL on 6/22/2017.
 */

public class PersonaAdapter extends BaseAdapter {
    String pid;
    ArrayList<String> pid_list = new ArrayList<String>();
    public static String persona_id_str;
    List<CategoryDetail> categoryArrayList = new ArrayList<>();
    List<PersonnaDetail> selectedCategoryArrayList = new ArrayList<>();
    List<CategoryDetail> newCategoryArrayList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
   // Context con;
    Activity activity;
    private HashMap<String, Boolean> hasClicked = new HashMap<String, Boolean>();

    public PersonaAdapter(Activity con, List<CategoryDetail> categoryArrayList, HashMap<String, Boolean> hasClicked, List<CategoryDetail> newCategoryArrayList) {

        this.categoryArrayList = categoryArrayList;
        this.activity = con;
        this.hasClicked = hasClicked;
        this.newCategoryArrayList = newCategoryArrayList;
    }

    @Override
    public int getCount() {
        if(categoryArrayList!= null) {
            return categoryArrayList.size();
        }
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater lif = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lif.inflate(R.layout.add_profile_interest_grid_row, null);
        final ImageView im = (ImageView) v.findViewById(R.id.imageView);

        TextView personaText = (TextView)v.findViewById(R.id.personaText);
        if(categoryArrayList != null) {
            personaText.setText(categoryArrayList.get(position).getCategory());
        }

        final int[] myImageList = new int[]{R.drawable.arty, R.drawable.creative,R.drawable.entertainer,R.drawable.foodie,R.drawable.fun,R.drawable.game,R.drawable.music,R.drawable.practical,R.drawable.professional,R.drawable.soulful,R.drawable.sports,R.drawable.stylish,R.drawable.techie,R.drawable.thinker,R.drawable.traveller};
        final int[] myImageListSelected = new int[]{R.drawable.arty_selection, R.drawable.creative_selection,R.drawable.entertainer_selection,R.drawable.foodie_selection,R.drawable.fun_selection,R.drawable.game_selection,R.drawable.music_selection,R.drawable.practical_selection,R.drawable.professional_selection,R.drawable.soulful_selection,R.drawable.sports_selection,R.drawable.stylish_selection,R.drawable.techie_selection,R.drawable.thinker_selection,R.drawable.traveller_selection};

        im.setBackgroundResource(myImageList[position]);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryArrayList != null) {
                    if (hasClicked.get(categoryArrayList.get(position).getCategory() + "")) {
                        im.setBackgroundResource(myImageList[position]);
                        hasClicked.put(categoryArrayList.get(position).getCategory() + "", false);
                        App app = (App) activity.getApplication();
                        CategoryDetail categoryDetail = new CategoryDetail();
                        categoryDetail.setCategory(categoryArrayList.get(position).getCategory());
                        categoryDetail.setPid(categoryArrayList.get(position).getPid());
                        int posValue = -1;
                        for (CategoryDetail category : newCategoryArrayList)
                        {
                            pid= category.getPid();

                        }
                        pid_list.remove(pid);
                        System.out.println("pid list ==="+pid_list);
                        if (newCategoryArrayList != null) {
                            for (int i = 0; i < newCategoryArrayList.size(); i++) {
                                if (newCategoryArrayList.get(i).getPid().equalsIgnoreCase(categoryArrayList.get(position).getPid())) {
                                    posValue = i;
                                 }
                            }
                        }
                        if (posValue != -1) {
                            newCategoryArrayList.remove(newCategoryArrayList.get(posValue));
                            app.setNewCategoryArrayList(newCategoryArrayList);
                            if (newCategoryArrayList != null) {
                                                             if (newCategoryArrayList.size() == 0) {
                                    sharedpreferences = App.getInstance().getDefaultAppSharedPreferences();
                                    editor = sharedpreferences.edit();
                                    editor.putString(SharedPreferencesConstants.shouldSave, "shouldnotSave");
                                    editor.commit();
                                }
                            }
                        }
                    } else {
                        im.setBackgroundResource(myImageListSelected[position]);
                        App app = (App) activity.getApplication();
                        CategoryDetail categoryDetail = new CategoryDetail();
                        categoryDetail.setCategory(categoryArrayList.get(position).getCategory());
                        categoryDetail.setPid(categoryArrayList.get(position).getPid());
                        newCategoryArrayList.add(categoryDetail);
                        app.setNewCategoryArrayList(newCategoryArrayList);
                        System.out.println("selected category==="+newCategoryArrayList);
                      for (CategoryDetail category : newCategoryArrayList)
                        {
                        pid= category.getPid();
                        }
                        pid_list.add(pid);
                        System.out.println("pid list ==="+pid_list.toString());
                        persona_id_str = pid_list.toString().replaceAll("\\[", "").replaceAll("\\]","");    //to replace open and close square bracket
                        hasClicked.put(categoryArrayList.get(position).getCategory() + "", true);
                    }
                }
            }
          //  9926407728

        });
        if(categoryArrayList != null) {
            if (hasClicked.containsKey(categoryArrayList.get(position).getCategory() + "")) {
                boolean bool = hasClicked.get(categoryArrayList.get(position).getCategory() + "");
                if (bool) {
                    im.setBackgroundResource(myImageListSelected[position]);
                } else {
                    im.setBackgroundResource(myImageList[position]);
                }
            } else {
                im.setBackgroundResource(myImageList[position]);
                hasClicked.put(categoryArrayList.get(position).getCategory() + "", false);
            }
        }
        return v;
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
