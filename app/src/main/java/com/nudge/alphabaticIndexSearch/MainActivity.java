package com.nudge.alphabaticIndexSearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.nudge.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fast_scroller_recycler)
    IndexFastScrollRecyclerView mRecyclerView;

    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialiseData();
        initialiseUI();
    }

    protected void initialiseData() {
        //Recycler view data
        mDataArray = DataHelper.getAlphabetData();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(mDataArray));

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#1D1E1C");
        mRecyclerView.setIndexBarCornerRadius(0);
        mRecyclerView.setIndexBarTransparentValue((float) 0.4);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(35);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#ffffff");

        mRecyclerView.setIndexBarVisibility(true);
        mRecyclerView.setIndexbarHighLateTextColor("#1D1E1C");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);
    }
}