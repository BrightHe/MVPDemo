package display.interactive.mvpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import display.interactive.mvpdemo.presenter.GetContactPresenter;
import display.interactive.mvpdemo.view.IGetContactView;

public class MainActivity extends AppCompatActivity implements IGetContactView {


    private final String TAG = MainActivity.class.getName();

    private final int REFRESH_DATA = 1;

    private Context mContext;

    private RecyclerView recyclerView;

    private FloatingActionButton fabAdd;

    private ContactAdapter mContactAdapter;

    private ProgressDialog progressDialog;

    private GetContactPresenter mGetContactPresenter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case REFRESH_DATA:
                    refresh();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mGetContactPresenter = new GetContactPresenter(MainActivity.this);
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //渲染UI
                    init();
                } else {
                    Toast.makeText(this, this.getText(R.string.string_toast_info), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, Constant.PERMISSION_CODE);
        } else {
            //渲染UI
            init();
        }
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(true);

        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContactPresenter.addContact();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<Contact> contactList = new ArrayList<>();
        mContactAdapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(mContactAdapter);

        //拖拽移动和左滑删除
        ContactItemTouchCallBack contactItemTouchCallBack = new ContactItemTouchCallBack(mContactAdapter);
        //设置支持侧滑删除子项
        contactItemTouchCallBack.setSwipeEnable(true);
        ItemTouchHelper helper = new ItemTouchHelper(contactItemTouchCallBack);
        helper.attachToRecyclerView(recyclerView);

        mHandler.sendEmptyMessageDelayed(REFRESH_DATA, 500);
    }

    private void refresh(){
        //todo 建议放在子线程处理
        // 获取系统联系人列表
        mGetContactPresenter.getContact(mContext);
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void showProgressDialog(boolean show) {
        if (show) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showAddContractDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.add_contact_dialog, null);
        final EditText editContactName = view.findViewById(R.id.name_input);
        final EditText editContactPhone = view.findViewById(R.id.phone_input);
        AlertDialog.Builder addContactDialog = new AlertDialog.Builder(this);
        addContactDialog.setTitle(getText(R.string.string_add_contact));
        addContactDialog.setIcon(android.R.drawable.ic_dialog_info);
        addContactDialog.setView(view);
        addContactDialog.setPositiveButton(getText(R.string.string_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

                String editName = editContactName.getText().toString();

                String editPhone = editContactPhone.getText().toString();

                if (!TextUtils.isEmpty(editName) && !TextUtils.isEmpty(editPhone)) {
                    Contact contact = new Contact(editName, editPhone);
                    Log.i("TAG", "addContactDialog: onClick: add_name is " + contact.getName() + " add_phone is " + contact.getPhone());
//                    mContactList.add(contact);
//                    mContactAdapter.notifyDataSetChanged();
                    mGetContactPresenter.addContactToList(contact);
                } else {
                    Toast.makeText(MainActivity.this, getText(R.string.string_toast_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
        addContactDialog.setNegativeButton(getText(R.string.string_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(MainActivity.this, getText(R.string.string_add_contact_cancel), Toast.LENGTH_SHORT).show();
            }
        });
        addContactDialog.show();
    }

    @Override
    public void refreshView(List<Contact> contactList) {
        if (contactList != null && !contactList.isEmpty()) {
            mContactAdapter.setData(contactList);
            mContactAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, getText(R.string.string_empty_contact), Toast.LENGTH_SHORT).show();
        }
    }
}