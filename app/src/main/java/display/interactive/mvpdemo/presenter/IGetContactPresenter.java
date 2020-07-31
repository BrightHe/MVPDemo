package display.interactive.mvpdemo.presenter;

import android.content.Context;

import java.util.logging.Handler;

import display.interactive.mvpdemo.Contact;

/**
 * @ClassName IGetContactPresenter
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/27 15:11
 * @Version 1.0
 */
public interface IGetContactPresenter {
    void getContact(Context context);
    void getContactSuccess();
    void addContact();
    void addContactToList(Contact contact);
    void operateFailed();
}
