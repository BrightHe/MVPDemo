package display.interactive.mvpdemo.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;
import display.interactive.mvpdemo.Contact;
import display.interactive.mvpdemo.model.GetContactModel;
import display.interactive.mvpdemo.model.IGetContactModel;
import display.interactive.mvpdemo.model.TaskListener;
import display.interactive.mvpdemo.view.IGetContactView;

/**
 * @ClassName GetContactPresenter
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/27 15:21
 * @Version 1.0
 */
public class GetContactPresenter implements IGetContactPresenter {

    private IGetContactModel mIGetContactModel;

    private IGetContactView mIGetContactView;

    private List<Contact> mContactList;

    private TaskListener listener = new TaskListener() {
        @Override
        public void success(List<Contact> contactList) {
            mContactList = contactList;
            getContactSuccess();
        }

        @Override
        public void failed() {
            operateFailed();
        }
    };

    public GetContactPresenter(IGetContactView IGetContactView) {
        mIGetContactView = IGetContactView;
        mIGetContactModel = new GetContactModel(this, listener);
    }


    @Override
    public void getContact(Context context) {
        mIGetContactView.showProgressDialog(true);
        mIGetContactModel.getContract(context);
    }

    @Override
    public void getContactSuccess() {
        if (mContactList != null && !mContactList.isEmpty()) {
            mIGetContactView.showProgressDialog(false);
            Log.i("TAG", "getContact: contactList[1] name is " + mContactList.get(1).getName());
            mIGetContactView.refreshView(mContactList);
        }
    }

    @Override
    public void addContact() {
        mIGetContactView.showAddContractDialog();
    }

    @Override
    public void addContactToList(Contact contact) {
        mContactList.add(contact);
        mIGetContactView.refreshView(mContactList);
    }

    @Override
    public void operateFailed() {
        mIGetContactView.showProgressDialog(false);
    }
}
