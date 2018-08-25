package com.liviurau.bakers.ui.step;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.liviurau.bakers.R;
import com.liviurau.bakers.data.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liviurau.bakers.UiConstants.DEFAULT_POSITION;

public class StepActivity extends AppCompatActivity {

    public static final String CURRENT_RECIPE = "current_recipe";
    public static final String SELECTED_STEP = "selected_step";

    @BindView(R.id.stepToolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_step_tab_layout)
    TabLayout mTlRecipeStep;
    @BindView(R.id.recipe_step_viewpager)
    ViewPager mVpRecipeStep;

    private Recipe currentRecipe;
    private int selectedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_return_light);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setTitle("");

        currentRecipe = getIntent().getParcelableExtra(CURRENT_RECIPE);
        selectedStep = getIntent().getIntExtra(SELECTED_STEP, DEFAULT_POSITION);
        if (selectedStep == DEFAULT_POSITION) {
            closeOnError();
            return;
        } else {
            toolbar.setTitle(currentRecipe.getName());
        }

        StepFragmentPagerAdapter adapter = new StepFragmentPagerAdapter(currentRecipe.getSteps(), getSupportFragmentManager());
        mVpRecipeStep.setAdapter(adapter);
        mTlRecipeStep.setupWithViewPager(mVpRecipeStep);
        mVpRecipeStep.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(currentRecipe.getName());
                toolbar.setSubtitle(currentRecipe.getSteps().get(position).getShortDescription());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpRecipeStep.setCurrentItem(selectedStep);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_missing, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
