package com.yaroslavgorbachh.counter.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CreateEditCounterViewModel;
import com.yaroslavgorbachh.counter.ViewModels.Factories.CreateEditCounterViewModelFactory;


public class CreateEditCounterFragment extends Fragment {
    private String mTitle;
    private long mValue;
    private long mStep;
    private String mGroup;
    private long mMaxValue;
    private long mMinValue;
    private long mCounterId;

    private TextInputEditText mTitle_et;
    private TextInputEditText mValue_et;
    private TextInputEditText mStep_et;
    private TextInputEditText mMaxValue_et;
    private TextInputEditText mMinValue_et;
    private AutoCompleteTextView mGroups_et;
    private CreateEditCounterViewModel mViewModel;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_edit_counter, container, false);

        /*initialize fields*/
        mTitle_et = view.findViewById(R.id.counterTitle_addCounter_detailed);
        mValue_et = view.findViewById(R.id.counterValue_addCounter_detailed);
        mStep_et = view.findViewById(R.id.counterStep_addCounter_detailed);
        mMaxValue_et = view.findViewById(R.id.counterMaxValue_addCounter_detailed);
        mMinValue_et = view.findViewById(R.id.counterMinValue_addCounter_detailed);
        mToolbar = view.findViewById(R.id.toolbar_counterCreateActivity);
        mGroups_et = view.findViewById(R.id.filled_exposed_dropdown);
        mCounterId = CreateEditCounterFragmentArgs.fromBundle(getArguments()).getCounterId();
        mViewModel = new ViewModelProvider(this, new CreateEditCounterViewModelFactory(requireActivity().getApplication(),
                mCounterId)).get(CreateEditCounterViewModel.class);

        /*set navigationIcon, inflate menu, and set listeners*/
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setNavigationOnClickListener(i -> {
            Navigation.findNavController(view).popBackStack();
        });

        mToolbar.inflateMenu(R.menu.menu_counter_create_activity);

        mViewModel.mCounter.observe(getViewLifecycleOwner(), counter -> {
            /*if counter == null that means that counter will be created*/
            if (counter == null) {
                mTitle = "";
                mValue = 0;
                mStep = 1;
                mGroup = "";
            /*if counter != null that means counter will
             be updated and we need to get old counter values*/
            } else {
                mTitle = counter.title;
                mValue = counter.value;
                mStep = counter.step;
                mGroup = counter.grope;
                mMaxValue = counter.maxValue;
                mMinValue = counter.minValue;
                mTitle_et.setText(counter.title);
                mValue_et.setText(String.valueOf(counter.value));
                mStep_et.setText(String.valueOf(counter.step));
                mGroups_et.setText(counter.grope);
                if (counter.maxValue != Long.parseLong("9999999999999999")) {
                    mMaxValue_et.setText(String.valueOf(counter.maxValue));
                }
                if (counter.minValue != Long.parseLong("-9999999999999999")) {
                    mMinValue_et.setText(String.valueOf(counter.minValue));
                }
            }

            /*create new counter*/
            mToolbar.setOnMenuItemClickListener(i -> {
                createCounter();
                return true;
            });
        });

        /*each new group sets into dropdown_menu*/
        mViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.dropdown_menu_popup_item,
                    Utility.deleteTheSameGroups(groups));
            mGroups_et.setAdapter(adapter);
        });
        return view;
    }


    private void createCounter() {
        /*if title is empty show error*/
        if (mTitle_et.getText().toString().trim().isEmpty()) {
            mTitle_et.setError("This field cannot be empty");
        } else {
            mTitle = mTitle_et.getText().toString();
        }

        /*if value is empty show error*/
        if (String.valueOf(mValue_et.getText()).trim().isEmpty()) {
            mValue_et.setError("This field cannot be empty");
        } else {
            mValue = Long.parseLong(String.valueOf(mValue_et.getText()));
        }

        /*if step is empty show error*/
        if (String.valueOf(mStep_et.getText()).trim().isEmpty()) {
            mStep_et.setError("This field cannot be empty");
        } else {
            mStep = Long.parseLong(String.valueOf(mStep_et.getText()));
        }

        /*if maxValue is empty set default value if is not set value from editText*/
        if (String.valueOf(mMaxValue_et.getText()).trim().isEmpty()) {
            mMaxValue = Long.parseLong("9999999999999999");
        } else {
            mMaxValue = Long.parseLong(String.valueOf(mMaxValue_et.getText()));
        }

        /*if minValue is empty set default value if is not set value from editText*/
        if (String.valueOf(mMinValue_et.getText()).trim().isEmpty()) {
            mMinValue = Long.parseLong("-9999999999999999");
        } else {
            mMinValue = Long.parseLong(String.valueOf(mMinValue_et.getText()));
        }

        /*if group is empty show error*/
        if (mGroups_et.getText().toString().trim().isEmpty()) {
            mGroup = null;
        } else {
            mGroup = mGroups_et.getText().toString();
        }

        /*if all fields are filled create counter*/
        if (!(String.valueOf(mValue_et.getText()).trim().isEmpty())
                && !(String.valueOf(mTitle_et.getText()).trim().isEmpty())
                && !(String.valueOf(mStep_et.getText()).trim().isEmpty())) {
            mViewModel.updateCreateCounter(mTitle, mValue, mMaxValue, mMinValue, mStep, mGroup);
            Navigation.findNavController(getView()).popBackStack();
        }
    }
}