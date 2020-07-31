package display.interactive.mvpdemo.model;

import android.content.Context;

import java.util.List;

import display.interactive.mvpdemo.Contact;

/**
 * @ClassName IGetContractModel
 * @Description TODO
 * @Author hezhihui6
 * @Date 2020/7/27 15:03
 * @Version 1.0
 */
public interface IGetContactModel {
    /**
     * 获取系统通讯录联系人
     *
     * @param context 上下文
     */
    void getContract(Context context);

}
