package com.example.selfcheckout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NavigationUtils {

    /**
     * Navigate to a given activity.
     *
     * @param context The current context (usually the current activity).
     * @param targetActivityClass The class of the target activity.
     */
    public static void navigateTo(Context context, Class<? extends Activity> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        context.startActivity(intent);
    }

    /**
     * Set an OnClickListener on a view to navigate to a specified activity.
     *
     * @param view The view on which to set the OnClickListener.
     * @param context The current context (usually the current activity).
     * @param targetActivityClass The class of the target activity.
     */
    public static void setNavigationClickListener(View view, final Context context, final Class<? extends Activity> targetActivityClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(context, targetActivityClass);
            }
        });
    }

    public static void setFragmentNavigationClickListener(View view, final FragmentManager fragmentManager, final Fragment fragment, final int containerId, final boolean addToBackStack) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(fragmentManager, fragment, containerId, addToBackStack);
            }
        });
    }

    /**
     * Set an OnClickListener on a view to navigate to a specified activity.
     *
     * @param view The view on which to set the OnClickListener.
     * @param context The current context (usually the current activity).
     * @param targetActivityClass The class of the target activity.
     */
    public static void setActivityNavigationClickListener(View view, final Context context, final Class<? extends Activity> targetActivityClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(context, targetActivityClass);
            }
        });
    }

    public static void navigateToFragment(FragmentManager fragmentManager, Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    /**
     * Navigate to a given activity.
     *
     * @param context The current context (usually the current activity).
     * @param targetActivityClass The class of the target activity.
     */
    public static void navigateToActivity(Context context, Class<? extends Activity> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        context.startActivity(intent);
    }

    public static void setFragmentToActivityNavigationClickListener(View view, final Fragment fragment, final Class<? extends Activity> targetActivityClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(fragment.getActivity(), targetActivityClass);
            }
        });
    }

    /**
     * Simulate the behavior of pressing the back button.
     *
     * @param activity The current activity.
     */
    public static void navigateBack(Activity activity) {
        activity.onBackPressed();
    }

    /**
     * Set an OnClickListener on a view to simulate the behavior of pressing the back button.
     *
     * @param view The view on which to set the OnClickListener (e.g., button, text view, card view).
     * @param activity The current activity.
     */
    public static void setBackNavigationClickListener(View view, final Activity activity) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack(activity);
            }
        });
    }

    /**
     * Set an OnClickListener on a view to navigate to a specified fragment.
     *
     * @param view The view on which to set the OnClickListener.
     * @param currentFragment The current fragment from which the navigation is initiated.
     * @param targetFragmentClass The class of the target fragment.
     * @param containerId The container ID where the fragment should be placed.
     * @param addToBackStack Whether to add the transaction to the back stack.
     */
    public static void setFragmentNavigationClickListener(View view, final Fragment currentFragment,
                                                          final Class<? extends Fragment> targetFragmentClass,
                                                          final int containerId, final boolean addToBackStack) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment targetFragment = targetFragmentClass.newInstance();
                    FragmentTransaction transaction = currentFragment.getFragmentManager().beginTransaction();
                    transaction.replace(containerId, targetFragment);
                    if (addToBackStack) {
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


