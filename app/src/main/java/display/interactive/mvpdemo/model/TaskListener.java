package display.interactive.mvpdemo.model;

import java.util.List;

import display.interactive.mvpdemo.Contact;

/**
 * @ClassName TaskListener
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/28 14:47
 * @Version 1.0
 */
public interface TaskListener {
    void success(List<Contact> contactList);
    void failed();
}
