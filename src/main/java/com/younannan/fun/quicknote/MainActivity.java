package com.younannan.fun.quicknote;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.younannan.data.DataCenter;
import com.younannan.data.InfoData;
import com.younannan.tool.PermisionUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private ListView recordList;
    private ArrayList<InfoData> infoDataList;
    private DataCenter dataCenter;

    private static final int DELETE_RECORD = 1;
    private static final int DIAL_RECORD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PermisionUtils.verifyAllPermissions(MainActivity.this);

        dataCenter = new DataCenter(getFilesDir().getAbsolutePath());
        recordList = (ListView) findViewById(R.id.record_list);
        this.registerForContextMenu(recordList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        //每次进入时重新加载
        prepareContentList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        //设置Menu显示内容
        menu.setHeaderTitle("操作");
        menu.add(1,DELETE_RECORD,1,"删除该记录");
        menu.add(1,DIAL_RECORD,1,"拨打号码");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case DELETE_RECORD:
                infoDataList.remove(menuInfo.position);
                dataCenter.saveInfoDataList(infoDataList);
                ((BaseAdapter)recordList.getAdapter()).notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "已删除！", Toast.LENGTH_SHORT).show();
                break;
            case DIAL_RECORD:
                InfoData infoData = infoDataList.get(menuInfo.position);
                if(infoData.phoneNumber != null && infoData.phoneNumber.length() > 2){
                    PermisionUtils.verifyCallPermissions(MainActivity.this);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + infoData.phoneNumber);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "该人员并没有登记电话号码！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void prepareContentList(){
        infoDataList = dataCenter.loadInfoDataList();
        if(infoDataList.isEmpty()){
            return;
        }
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return infoDataList.size();//数目
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View view;
                if (convertView == null) {
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    view = inflater.inflate(R.layout.main_content_item_layout, null);
                }else{
                    view = convertView;
                }
                setItemFromData(view, infoDataList.get(position));
                return view;
            }
            @Override
            public long getItemId(int position) {//取在列表中与指定索引对应的行id
                return 0;
            }
            @Override
            public Object getItem(int position) {//获取数据集中与指定索引对应的数据项
                return null;
            }
        };
        recordList.setAdapter(adapter);
        //获取当前ListView点击的行数，并且得到该数据
        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("recordPosition", position) ;
                startActivity(intent);

            }
        });
    }

    private void deleteRecord(int position){

    }

    private void setItemFromData(View itemView, InfoData infoData){
        TextView nameSexAge = (TextView) itemView.findViewById(R.id.main_content_item_name_sex_age);
        TextView phone = (TextView) itemView.findViewById(R.id.main_content_item_phone);
        TextView time = (TextView) itemView.findViewById(R.id.main_content_item_time);
        TextView idNum = (TextView) itemView.findViewById(R.id.main_content_item_idNum);
        nameSexAge.setText(infoData.name + "  " + infoData.sex + " " + infoData.age + "岁");
        phone.setText(infoData.phoneNumber);
        time.setText(infoData.endTime);
        idNum.setText(infoData.idNumber);
    }

}
