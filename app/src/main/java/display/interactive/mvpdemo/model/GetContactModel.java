package display.interactive.mvpdemo.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import display.interactive.mvpdemo.Contact;
import display.interactive.mvpdemo.presenter.IGetContactPresenter;

/**
 * @ClassName GetContactModel
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/27 15:15
 * @Version 1.0
 */
public class GetContactModel implements IGetContactModel {
    private IGetContactPresenter mIGetContactPresenter;
    private TaskListener mListener;

    public GetContactModel(IGetContactPresenter IGetContactPresenter, TaskListener listener) {
        mIGetContactPresenter = IGetContactPresenter;
        mListener = listener;
    }

    @Override
    public void getContract(Context context) {
        List<Contact> contactList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                    int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("TAG", "getContactData: name is " + name + ", phone is " + phone);
                    Contact contact = new Contact(id, name, phone, lookupKey);
                    contactList.add(contact);
                }
            }
            mListener.success(contactList);
        } catch (Exception e) {
            e.printStackTrace();
            mListener.failed();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
