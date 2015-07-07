package com.alice.loadersmscontactstest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import com.alice.loadersmscontactstest.fragment.ContactsFragment;
import com.alice.loadersmscontactstest.fragment.SmsFragment;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private FragmentManager manager;
    private SmsFragment smsFragment;
    private ContactsFragment contactsFragment;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        //初始显示smsFragment
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (smsFragment == null) {
            smsFragment = new SmsFragment();
        }
        transaction.replace(R.id.fragment_container, smsFragment);
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_sms){
            FragmentTransaction transaction = manager.beginTransaction();
            if (smsFragment == null) {
                smsFragment = new SmsFragment();
            }
            transaction.replace(R.id.fragment_container, smsFragment);
            transaction.commit();
        }
        else if (checkedId == R.id.radio_contacts){
            FragmentTransaction transaction = manager.beginTransaction();
            if (contactsFragment == null) {
                contactsFragment = new ContactsFragment();
            }
            transaction.replace(R.id.fragment_container, contactsFragment);
            transaction.commit();
        }
    }
}
