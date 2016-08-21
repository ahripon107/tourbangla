package com.androidfragmant.tourxyz.banglatourism.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.androidfragmant.tourxyz.banglatourism.R;

/**
 * Created by amin on 8/21/16.
 */
public class MenuHelper {
    public static final int NONE = -1;

    public static final int MENU_ITEM_ID_SEARCH = 10;


    public static void addSearchMenuItem(Menu menu, final FilterableRecyclerAdapter filterableRecyclerAdapter,
                                         AppCompatActivity listActivity) {

        addMenuItem(menu, Menu.NONE, MENU_ITEM_ID_SEARCH, Menu.FIRST, NONE, android.R.drawable.ic_menu_search,
                MenuItem.SHOW_AS_ACTION_ALWAYS, null)
                .setActionView(getSearchView(filterableRecyclerAdapter, listActivity));
    }

    /*
     * c.f.: http://stackoverflow.com/questions/13746283/use-searchview-in-actionbar-with-actionbarsherlock
     */

    private static SearchView getSearchView(final FilterableRecyclerAdapter filterableRecyclerAdapter,
                                            AppCompatActivity listActivity) {

        final SearchView searchView = new SearchView(listActivity.getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*
                 When filtering is being performed and the app goes into background (e.g. due to incoming call) and
                 gets killed and comes to foreground again, the searchview remains closed but the list remains
                 filtered. This will confuse the user. So, the following code fragment opens the searchview text
                 input (in case of non-empty filtering) so that the user can see what filter text is currently
                 applied on the list.
                 */
                if (newText == null || newText.trim().length() == 0) {
                    searchView.setIconified(false);
                }

                filterableRecyclerAdapter.getFilter().filter(newText);

                return true;
            }
        });

        return searchView;
    }


    private static MenuItem addMenuItem(Menu menu, int groupId, int itemId, int order, int titleRes, int iconRes,
                                        int actionEnum, MenuItem.OnMenuItemClickListener onMenuItemClickListener) {

        MenuItem menuItem = titleRes != NONE
                ? menu.add(groupId, itemId, order, titleRes)
                : menu.add(groupId, itemId, order, "");

        if (iconRes != NONE) {
            menuItem.setIcon(iconRes);
        }

        if (actionEnum != NONE) {
            menuItem.setShowAsAction(actionEnum);
        }

        if (onMenuItemClickListener != null) {
            menuItem.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        return menuItem;
    }

}
