package com.liviurau.bakers.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.liviurau.bakers.R;
import com.liviurau.bakers.RecipeService;
import com.liviurau.bakers.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String RECIPE_LIST = "recipe_list";
    @BindView(R.id.recipeFrameLayout)
    FrameLayout recipeFrameLayout;

    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        List<Recipe> recipeList = new RecipeService(this).getRecipes();

        if (!isInternetConnection()) {
            if (recipeList.size() > 0) {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList(RECIPE_LIST, (ArrayList<? extends Parcelable>) recipeList);

                RecipeListFragment fragment = new RecipeListFragment();
                fragment.setArguments(arguments);

                pushFragment(fragment);
            } else {
                Snackbar snackbar = Snackbar.make(recipeFrameLayout, R.string.no_internet_message, Snackbar.LENGTH_LONG);

                View sbView = snackbar.getView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sbView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                }
                snackbar.show();
            }
        } else {
            pushFragment(new RecipeListFragment());
        }
    }

    private void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.recipeFrameLayout, fragment);
                ft.commit();
            }
        }
    }

    private boolean isInternetConnection() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            connected = networkInfo != null &&
                    networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            return connected;

        } catch (Exception e) {
            Log.v(getString(R.string.log_network_connection), e.toString());
        }
        return connected;
    }

}
