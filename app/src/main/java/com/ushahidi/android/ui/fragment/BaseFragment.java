/*
 * Copyright (c) 2014 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.ui.fragment;

import com.andreabaccega.widget.FormAutoCompleteTextView;
import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.state.IDeploymentState;
import com.ushahidi.android.ui.activity.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Base {@link android.app.Fragment} class that every Fragment in this app will have to implement.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Layout resource mId
     */
    protected final int mLayout;

    /**
     * Menu resource mId
     */
    protected final int mMenu;

    /**
     * BaseFragment
     *
     * @param menu mMenu resource mId
     */
    public BaseFragment(int layout, int menu) {
        this.mLayout = layout;
        this.mMenu = menu;
    }

    /**
     * Initializes the {@link com.ushahidi.android.presenter.IPresenter} for this fragment in a MVP
     * pattern used to architect the application presentation layer.
     */
    abstract void initPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        injectDependencies();
        initPresenter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = null;
        if (mLayout != 0) {
            root = inflater.inflate(mLayout, container, false);
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.mMenu != 0) {
            inflater.inflate(this.mMenu, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    protected View fadeIn(final android.view.View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));

            } else {

                view.clearAnimation();
            }
        }

        return view;

    }

    protected View fadeOut(final android.view.View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_out));
            } else {
                view.clearAnimation();
            }
        }
        return view;

    }

    protected View setViewGone(final View view) {
        return setViewGone(view, true);
    }

    protected View setViewGone(final View view, final boolean gone) {
        if (view != null) {
            if (gone) {
                if (GONE != view.getVisibility()) {

                    fadeOut(view, true);

                    view.setVisibility(GONE);
                }
            } else {
                if (VISIBLE != view.getVisibility()) {
                    view.setVisibility(VISIBLE);

                    fadeIn(view, true);

                }
            }
        }
        return view;
    }

    /**
     * Replace every field annotated using @Inject annotation with the provided dependency specified
     * inside a Dagger module value.
     */
    private void injectDependencies() {
        ((BaseActivity) getActivity()).inject(this);
    }

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     *
     * @param view to extract each widget injected in the fragment.
     */
    private void injectViews(final View view) {
        ButterKnife.inject(this, view);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(int message) {
        Toast.makeText(getActivity(), getText(message), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message string
     */
    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
                .show();
    }

    private boolean validateForm(FormEditText item, boolean isFocus) {
        if (item.testValidity()) {
            return true;
        } else {
            if (isFocus) {
                item.requestFocus();
            }
            return false;
        }
    }

    private boolean validateForm(FormAutoCompleteTextView item, boolean isFocus) {
        if (item.testValidity()) {
            return true;
        } else {
            if (isFocus) {
                item.requestFocus();
            }
            return false;
        }
    }

    private boolean validateForms(FormAutoCompleteTextView[] list, boolean isFocus) {
        boolean result = true;
        for (FormAutoCompleteTextView item : list) {
            if (!validateForm(item, result && isFocus)) {
                result = false;
            }
        }
        return result;
    }

    protected boolean validateForms(FormEditText... list) {
        return validateForms(list, true);
    }

    protected boolean validateForms(FormAutoCompleteTextView... list) {
        return validateForms(list, true);
    }

    private boolean validateForms(FormEditText[] list, boolean isFocus) {
        boolean result = true;
        for (FormEditText item : list) {
            if (!validateForm(item, result && isFocus)) {
                result = false;
            }
        }
        return result;
    }
}
