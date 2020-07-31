package display.interactive.mvpdemo.view;

import android.os.Handler;

import java.util.List;
import display.interactive.mvpdemo.Contact;

/**
 * @ClassName IGetContactView
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/27 15:06
 * @Version 1.0
 */
public interface IGetContactView {
    Handler getHandler();
    void showProgressDialog(boolean show);
    void showAddContractDialog();
    void refreshView(List<Contact> contactList);
}
