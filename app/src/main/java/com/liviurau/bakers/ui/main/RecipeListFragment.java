package com.liviurau.bakers.ui.main;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.liviurau.bakers.R;
import com.liviurau.bakers.RecipeService;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.utils.RecipeTaskLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.liviurau.bakers.ui.main.MainActivity.RECIPE_LIST;

public class RecipeListFragment extends Fragment {

    private static final int TASK_ID = 43;

    @BindView(R.id.recipeRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recipeProgressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;

    private RecipeListAdapter recipeListAdapter;
    private LoaderManager mLoaderManager;

    private Context mContext;

    List<Recipe> recipeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(RECIPE_LIST)) {
            recipeList = getArguments().getParcelable(RECIPE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipies, container, false);

        mContext = getContext();

        unbinder = ButterKnife.bind(this, rootView);

        mLoaderManager = getLoaderManager();

        recipeListAdapter = new RecipeListAdapter(mContext);

        StaggeredGridLayoutManager mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setAdapter(recipeListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        showLoading();

        if (recipeList != null){
            recipeListAdapter.swapList(recipeList);
            showDataView();
        } else {
            loadTask(TASK_ID);
        }
        return rootView;
    }

    private void loadTask(int taskID) {
        LoaderManager.LoaderCallbacks<List<Recipe>> callback = new LoaderManager.LoaderCallbacks<List<Recipe>>() {

            @Override
            public Loader<List<Recipe>> onCreateLoader(int i, Bundle bundle) {
                return new RecipeTaskLoader(mContext);
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
                RecipeService recipeService = new RecipeService(mContext);
                recipeService.setList(recipes);
                recipeListAdapter.swapList(recipeService.getRecipes());
                showDataView();
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {

            }
        };
        mLoaderManager.initLoader(taskID, null, callback);
    }

    private void showDataView() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
