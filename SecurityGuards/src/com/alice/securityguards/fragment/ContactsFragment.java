package com.alice.securityguards.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.alice.securityguards.R;
import com.alice.securityguards.database.DBHelper;
import com.alice.securityguards.providers.BlackContentProvider;
import org.apache.http.client.utils.URIUtils;

/**
 * Created by Administrator on 15-7-10.
 * Project: SecurityGuards
 * User: Alice
 * Data: 15-7-10
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int CONTACTS_LOADER_ID = 115;
    public static final int SMS_LOADER_ID = 116;
    public static final int BLACK_LOADER_ID = 117;
    private static final String TAG = "ContactsFragment";
    private View view;
    private SimpleCursorAdapter adapter;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private LoaderManager manager;
    private ListView contactsListView;

    public ContactsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            dbHelper = new DBHelper(getActivity());
            database = dbHelper.getWritableDatabase();
            view = inflater.inflate(R.layout.fragment_contacts, container, false);
            adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_contact, null,
                    new String[]{ContactsContract.RawContacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                    new int[]{R.id.item_contact_id, R.id.item_contact_phone}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            contactsListView = (ListView) view.findViewById(R.id.list_contacts);
            contactsListView.setAdapter(adapter);

            manager = getActivity().getSupportLoaderManager();
            manager.initLoader(CONTACTS_LOADER_ID, null, this);
            //ListView添加弹出式菜单，加入黑名单
            registerForContextMenu(contactsListView);
        }

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_contacts, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menu_group_contacts) {
            ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
            AdapterView.AdapterContextMenuInfo info = null;
            int id = -1;
            if (menuInfo != null && menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
                info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                id = (int) info.id;
            }

            switch (item.getItemId()) {
                case R.id.menu_contacts_add_black:
                    //查找Contacts数据库 data 表，根据人名找到电话
                    ContentValues values = queryContacts(id);
                    if (values != null) {
//                        long insert = database.insert(DBHelper.TABLE_CONTACTS, null, values);
                        ContentResolver resolver = getActivity().getContentResolver();
                        Uri uri = resolver.insert(Uri.parse(BlackContentProvider.CONTACTS_URI), values);
                        long parseId = ContentUris.parseId(uri);
                        if (parseId > 0){
                            Toast.makeText(getActivity(), "已将"+ values.get("display_name")+"加入黑名单", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        CursorLoader loader = new CursorLoader(getActivity(), uri,
                new String[]{ContactsContract.RawContacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.RawContacts.DELETED + "!=?", new String[]{"1"}, ContactsContract.RawContacts._ID + " desc");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ContentValues queryContacts(int id) {
        ContentValues values = null;
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1},
                ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{String.valueOf(id)},
                null);

        if (cursor != null) {
            values = new ContentValues();
            values.put("raw_contacts_id", String.valueOf(id));
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
                if (index != -1) {
                    String mimeType = cursor.getString(index);
                    if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {//电话
                        index = cursor.getColumnIndex(ContactsContract.Data.DATA1);
                        if (index != -1) {
                            String data1 = cursor.getString(index);
                            if (data1.startsWith("+86")) {
                                data1 = data1.substring(3);
                            }
                            //TODO 先判断有无区号，一般把区号去掉
                            //把 - “ ” （）都去掉，只留数字和加号，可以前面保留国家码86等
                            data1 = data1.replace(" ", "");
                            data1 = data1.replace("-", "");
                            data1 = data1.replace("(", "");
                            data1 = data1.replace(")", "");
                            values.put("phone", data1);
                            Log.e(TAG, "电话：" + data1);
                        }
                    } else if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                        index = cursor.getColumnIndex(ContactsContract.Data.DATA1);
                        if (index != -1) {
                            String data1 = cursor.getString(index);
                            values.put("display_name", data1);
                            Log.e(TAG, "姓名：" + data1);
                        }
                    }
                }
            }
        }
        return values;
    }
}