package com.alice.securityguards.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.alice.securityguards.R;
import com.alice.securityguards.database.DBHelper;
import com.alice.securityguards.providers.BlackContentProvider;

/**
 * Created by Administrator on 15-7-10.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-10
 */
public class BlackListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "BlackListFragment";
    private View view;
    private DBHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private SQLiteDatabase database;
    private ListView blackListView;

    public BlackListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            dbHelper = new DBHelper(getActivity());
            view = inflater.inflate(R.layout.fragment_black_list, container, false);
            blackListView = (ListView) view.findViewById(R.id.list_black_list);
            adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_black_contacts,
                    null, new String[]{"display_name", "phone"},
                    new int[]{R.id.item_black_contact_name, R.id.item_black_contact_phone},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            blackListView.setAdapter(adapter);

            LoaderManager manager = getActivity().getSupportLoaderManager();
            manager.initLoader(ContactsFragment.BLACK_LOADER_ID, null, this);
            registerForContextMenu(blackListView);
        }
//        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
//        adapter.swapCursor(cursor);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_blackcontacts, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menu_grout_black) {
            ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
            AdapterView.AdapterContextMenuInfo info = null;
            int id = -1;
            if (menuInfo != null && menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
                info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                id = (int) info.id;
            }
            switch (item.getItemId()) {
                case R.id.menu_black_list_delete:
                    //查找Contacts数据库 data 表，根据人名找到电话
//                    database = dbHelper.getWritableDatabase();
//                    int ret = database.delete(DBHelper.TABLE_CONTACTS, "_id =?", new String[]{String.valueOf(id)});
                    ContentResolver resolver = getActivity().getContentResolver();
                    int ret = resolver.delete(Uri.parse(BlackContentProvider.CONTACTS_URI), "_id =?", new String[]{String.valueOf(id)});
                    if (ret != 0) {
                        Toast.makeText(getActivity(), "该联系人已经移出黑名单", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
        return false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(), Uri.parse(BlackContentProvider.CONTACTS_URI),
                null, null, null, "_id desc");
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