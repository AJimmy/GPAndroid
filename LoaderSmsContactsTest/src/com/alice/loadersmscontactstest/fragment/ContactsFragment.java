package com.alice.loadersmscontactstest.fragment;

import android.content.Intent;
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
import android.widget.*;
import com.alice.loadersmscontactstest.ContactDetailActivity;
import com.alice.loadersmscontactstest.R;

/**
 * Created by Administrator on 15-7-4.
 * Project: LoaderSmsContactsTest
 * User: Alice
 * Data: 15-7-4
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final String TAG = "ContactsFragment";
    private SimpleCursorAdapter adapter;
    private LoaderManager manager;

    public ContactsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_contacts);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_contacts, null,
                new String[]{ContactsContract.RawContacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{R.id.item_contacts_id, R.id.item_contacts_name},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        manager = getActivity().getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putString("uri", ContactsContract.RawContacts.CONTENT_URI.toString());
        manager.initLoader(102, args, this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (args != null) {
            loader = new CursorLoader(getActivity(), Uri.parse(args.getString("uri")),
                    new String[]{ContactsContract.RawContacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                    ContactsContract.RawContacts.DELETED +"!= ?",
                    new String[]{"1"}, null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        TextView txtView = (TextView) view.findViewById(R.id.item_contacts_id);
        int rawId = Integer.parseInt(txtView.getText().toString());
        Log.e(TAG, "raw   "+rawId);
        intent.putExtra("id", rawId);
        startActivity(intent);
    }
}