package com.alice.securityguards.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.alice.securityguards.R;
import com.alice.securityguards.adapter.SmsSimpleCursorAdapter;
import com.alice.securityguards.providers.BlackContentProvider;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 15-7-10.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-10
 */
public class BlackSmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private View view;
    private LoaderManager manager;
    private SmsSimpleCursorAdapter adapter;

    public BlackSmsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_black_sms, container, false);
            ListView smsListView = (ListView) view.findViewById(R.id.list_black_sms);
            adapter = new SmsSimpleCursorAdapter(getActivity(), R.layout.item_black_sms,
                    null, new String[]{"display_name", "time", "body"},
                    new int[]{R.id.item_black_sms_telephone, R.id.item_black_sms_time, R.id.item_black_sms_body}
                    , CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            smsListView.setAdapter(adapter);

            manager = getActivity().getSupportLoaderManager();
            manager.initLoader(ContactsFragment.SMS_LOADER_ID, null, this);

        }

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO 加载短信数据
        CursorLoader loader = new CursorLoader(getActivity(), Uri.parse(BlackContentProvider.SMS_URI),
                null, null, null, "time desc");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}