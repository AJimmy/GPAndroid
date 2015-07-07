package com.alice.loadersmscontactstest.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.alice.loadersmscontactstest.R;
import com.alice.loadersmscontactstest.apdater.SmsSimpleCursorAdapter;

/**
 * Created by Administrator on 15-7-4.
 * Project: LoaderSmsContactsTest
 * User: Alice
 * Data: 15-7-4
 */
public class SmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SMS_URI = "content://sms/inbox";
    public static final int LOADER_ID = 101;
    private static final String TAG = "SmsFragment";
    private LoaderManager manager;
    private ListView listView;
    //TODO 自定义适配器
    private SmsSimpleCursorAdapter adapter;
//    private SimpleCursorAdapter adapter;

    public SmsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        listView = (ListView) view.findViewById(R.id.list_sms);
//        adapter = new SimpleCursorAdapter(getActivity(),
//                R.layout.item_sms, null,
//                new String[]{"address", "date", "body"},
//                new int[]{R.id.item_sms_phone, R.id.item_sms_time, R.id.item_sms_content},
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter = new SmsSimpleCursorAdapter(getActivity(), R.layout.item_sms, null,
                new String[]{"address", "date", "body"},
                new int[]{R.id.item_sms_phone, R.id.item_sms_time, R.id.item_sms_content},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);

        manager = getActivity().getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putString("uri", SMS_URI);
        manager.initLoader(LOADER_ID, args, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //可以使用自定义的Loader，但是数据加载不能自动刷新
/*
        DatabaseLoader loader = new DatabaseLoader(getActivity());
        if (args != null) {
            String uri = args.getString("uri");
            if (uri != null) {
                loader.setUriStr(uri);
            }
        }*/

        CursorLoader loader = new CursorLoader(getActivity(), Uri.parse(args.getString("uri")), null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}