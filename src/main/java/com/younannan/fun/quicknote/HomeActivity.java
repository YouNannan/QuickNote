package com.younannan.fun.quicknote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.younannan.data.DataCenter;
import com.younannan.data.InfoData;
import com.younannan.data.TemplateData;
import com.younannan.tool.EmailProcessor;
import com.younannan.tool.MyDateTimeUtil;
import com.younannan.tool.PdfProcessor;
import com.younannan.tool.PermisionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //private TextView mTextMessage;
    private View frameInfo;
    private View frameQa;
    private View framePrint;
    private View frameTemplate;

    private InfoPage infoPage;
    private QaPage qaPage;
    private PrintPage printPage;
    private TemplatePage templatePage;

    private int recordPosition;
    private DataCenter dataCenter;



    private static final String pdfFileName = "myPdf.pdf";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setAllInvisible();
            switch (item.getItemId()){
                case R.id.navigation_info:
                    frameInfo.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_qa:
                    frameQa.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_print:
                    framePrint.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_template:
                    frameTemplate.setVisibility(View.VISIBLE);
                    //getFilesDir()	= /data/data/com.my.app/files
                    //Toast.makeText(getApplicationContext(), HomeActivity.this.getFilesDir().getAbsolutePath() , Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dataCenter = new DataCenter(getFilesDir().getAbsolutePath());

        //初始化“信息”，“问答”，“打印”，“模板”四个页面
        frameInfo = findViewById(R.id.frame_info_page);
        infoPage = new InfoPage();
        infoPage.prepareInfoPage();
        frameQa = findViewById(R.id.frame_qa_page);
        qaPage = new QaPage();
        qaPage.prepareQa();
        framePrint = findViewById(R.id.frame_print_page);
        printPage = new PrintPage();
        printPage.preparePrint();
        frameTemplate = findViewById(R.id.frame_template_page);
        templatePage = new TemplatePage();
        templatePage.prepareTemplate();

        //将“信息”页面置为可见
        setAllInvisible();
        frameInfo.setVisibility(View.VISIBLE);
        navigation.setSelectedItemId(R.id.navigation_info);

        //从数据文件中恢复数据填入表格
        recordPosition = getIntent().getIntExtra("recordPosition", -1);
        if(recordPosition != -1){
            InfoData infoData = dataCenter.loadInfoDataList().get(recordPosition);
            ArrayList<TemplateData> templateData = dataCenter.loadTemplateList();
            setPageFromData(infoData, templateData);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveInfo();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveInfo();
    }



    private void saveInfo(){
        //如果之前有
        InfoData infoData = new InfoData();
        setDataFromPage(infoData);
        ArrayList<InfoData> infoDataList = dataCenter.loadInfoDataList();
        if(recordPosition != -1){
            infoDataList.remove(recordPosition);
        }
        recordPosition = 0;
        infoDataList.add(infoData);
        Collections.sort(infoDataList);
        dataCenter.saveInfoDataList(infoDataList);
    }

    private void setAllInvisible(){
        frameInfo.setVisibility(View.INVISIBLE);
        frameQa.setVisibility(View.INVISIBLE);
        framePrint.setVisibility(View.INVISIBLE);
        frameTemplate.setVisibility(View.INVISIBLE);
    }

    private void createPDF(Context context, String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getPath();
        PdfProcessor pdfProcessor = null;
        try {
            pdfProcessor = new PdfProcessor(dirPath + "/" + fileName);
            pdfProcessor.fillInfo(infoPage.times.getText().toString(),
                    infoPage.beginTime.getYear(), infoPage.beginTime.getMonth(), infoPage.beginTime.getDay(), infoPage.beginTime.getHour(), infoPage.beginTime.getMinute(),
                    infoPage.endTime.getYear(), infoPage.endTime.getMonth(), infoPage.endTime.getDay(), infoPage.endTime.getHour(), infoPage.endTime.getMinute(),
                    infoPage.conversationLocation.getText().toString(),
                    "", "", printPage.department.getText().toString(),
                    "", printPage.department.getText().toString(),
                    infoPage.name.getText().toString(), infoPage.sex.getText().toString(), infoPage.age.getText().toString(),
                    infoPage.birthDate.getYear(), infoPage.birthDate.getMonth(), infoPage.birthDate.getDay(),
                    infoPage.idNumber.getText().toString(),
                    "是".equals(infoPage.rendadaibiao.getText().toString()),
                    "刑事-嫌疑人".equals(infoPage.type.getSelectedItem()),
                    "违法-嫌疑人".equals(infoPage.type.getSelectedItem()),
                    infoPage.nowAddress.getText().toString(),
                    infoPage.phoneNumber.getText().toString(),
                    infoPage.residenceAddress.getText().toString(),
                    qaPage.getQaList());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (pdfProcessor != null) {
                pdfProcessor.close();
            }
        }
    }









    public void setPageFromData(InfoData infoData, ArrayList<TemplateData> templateList){
        if(infoData != null){
            infoPage.setPageFromData(infoData);
            qaPage.setPageFromData(infoData);
            printPage.setPageFromData(infoData);
        }
        if(templateList != null){
            templatePage.setPageFromData(templateList);
        }
    }

    public void setDataFromPage(InfoData infoData){
        infoPage.setDataFromPage(infoData);
        qaPage.setDataFromPage(infoData);
        printPage.setDataFromPage(infoData);
        //templatePage.setDataFromPage(templateList);

    }

    //用于存放"信息"页面的控件
    private class InfoPage{
        public MyDateTimeUtil beginTime;
        public MyDateTimeUtil endTime;
        public EditText conversationLocation;
        public Spinner type;
        public EditText times;
        public EditText name;
        public EditText idNumber;
        public TextView sex;
        public Switch sexSwitch;
        public MyDateTimeUtil birthDate;
        public EditText age;
        public TextView rendadaibiao;
        public Switch rendadaibiaoSwitch;
        public EditText nowAddress;
        public EditText residenceAddress;
        public EditText phoneNumber;


        public void prepareInfoPage(){
            beginTime = new MyDateTimeUtil(HomeActivity.this, R.id.date01, R.id.time01, 0);
            endTime = new MyDateTimeUtil(HomeActivity.this, R.id.date02, R.id.time02, R.id.now02);
            setClearTimeButton(R.id.clear01);
            conversationLocation = (EditText) findViewById(R.id.editText03);
            type = (Spinner) findViewById(R.id.spinner04);
            times = (EditText) findViewById(R.id.editText05);
            name = (EditText) findViewById(R.id.editText06);
            idNumber = (EditText) findViewById(R.id.editText07);
            setSexSwitch();
            setBirthDateButton();
            setRendadaibiaoSwitch();
            nowAddress = (EditText) findViewById(R.id.editText12);
            residenceAddress = (EditText) findViewById(R.id.editText13);
            phoneNumber = (EditText) findViewById(R.id.editText14);
        }

        public void setPageFromData(InfoData infoData){
            beginTime.setDateTime(infoData.beginTime);
            endTime.setDateTime(infoData.endTime);
            conversationLocation.setText(infoData.conversationLocation);
            type.setSelection(infoData.typePosition);
            times.setText(infoData.times);
            name.setText(infoData.name);
            idNumber.setText(infoData.idNumber);
            sex.setText(infoData.sex);
            sexSwitch.setChecked(!"男".equals(infoData.sex));
            birthDate.setDateTime(infoData.birthDate);
            age.setText(infoData.age);
            rendadaibiao.setText(infoData.rendadaibiao);
            rendadaibiaoSwitch.setChecked("是".equals(infoData.rendadaibiao));
            nowAddress.setText(infoData.nowAddress);
            residenceAddress.setText(infoData.residenceAddress);
            phoneNumber.setText(infoData.phoneNumber);
        }
        public void setDataFromPage(InfoData infoData){
            infoData.beginTime = beginTime.getDateTime();
            infoData.endTime = endTime.getDateTime();
            infoData.conversationLocation = conversationLocation.getText().toString();
            infoData.typePosition = type.getSelectedItemPosition();
            infoData.times = times.getText().toString();
            infoData.name = name.getText().toString();
            infoData.idNumber = idNumber.getText().toString();
            infoData.sex = sex.getText().toString();
            infoData.birthDate = birthDate.getDateTime();
            infoData.age = age.getText().toString();
            infoData.rendadaibiao = rendadaibiao.getText().toString();
            infoData.nowAddress = nowAddress.getText().toString();
            infoData.residenceAddress = residenceAddress.getText().toString();
            infoData.phoneNumber = phoneNumber.getText().toString();
        }


        //设置"清除"按钮的点击事件
        private void setClearTimeButton(int buttonId){
            Button clearButton = (Button) findViewById(buttonId);
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beginTime.clearTime();
                    endTime.clearTime();
                }
            });
        }

        //设置"性别"开关的点击事件
        private void setSexSwitch(){
            sexSwitch = (Switch) findViewById(R.id.switch08);
            sex = (TextView) findViewById(R.id.text08);
            sexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        sex.setText("女");
                    }else {
                        sex.setText("男");
                    }
                }
            });
        }

        //设置出生日期后，年龄需要联动发生变化
        private void setBirthDateButton(){
            birthDate = new MyDateTimeUtil(HomeActivity.this, R.id.date09, 0, 0);
            age = (EditText) findViewById(R.id.editText10);
            birthDate.setDateListener(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Button birthDateButton = (Button) findViewById(R.id.date09);
                            birthDateButton.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            age.setText(String.valueOf(calendar.get(Calendar.YEAR) - year));
                        }
                    });
        }

        //设置"人大代表"开关的点击事件
        private void setRendadaibiaoSwitch(){
            rendadaibiaoSwitch = (Switch) findViewById(R.id.switch11);
            rendadaibiao = (TextView) findViewById(R.id.text11);
            rendadaibiaoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        rendadaibiao.setText("是");
                    }else {
                        rendadaibiao.setText("否");
                    }
                }
            });
        }
    }


    //用于存放"问答"页面的控件
    private class QaPage{
        public ScrollView qaScrollView;
        public LinearLayout qaLayout;
        public ArrayList<EditText> qaEditTextList;
        public Button question;
        public Button answer;
        public Spinner template;
        private ArrayAdapter<String> adapter;

        public void prepareQa(){
            qaScrollView = (ScrollView)findViewById(R.id.qa_scroll_view);
            qaLayout = (LinearLayout) findViewById(R.id.question_answer);
            qaEditTextList = new ArrayList<>();
            template = (Spinner) findViewById(R.id.template);
            reloadTemplate();
            question = (Button) findViewById(R.id.question);
            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int templateIndex = template.getSelectedItemPosition();
                    ArrayList<String> list;
                    if(templateIndex == 0) {
                        list =  TemplateData.getSystemInstance().questionList;//使用系统模板
                    } else {
                        list = dataCenter.loadTemplateList().get(templateIndex - 1).questionList;//使用自定义模板
                    }
                    final String[] sentenceShown = list.toArray(new String[list.size()]);

                    AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("提问")
                            .setItems(sentenceShown, new DialogInterface.OnClickListener() {//添加列表
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText qaItem = setQuestionItem("问：" + replaceKeyword(sentenceShown[i]));
                                    qaScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    qaItem.requestFocus();
                                }
                            })
                            .create();
                    alertDialog.show();


                }
            });
            answer = (Button) findViewById(R.id.answer);
            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int templateIndex = template.getSelectedItemPosition();
                    ArrayList<String> list;
                    if(templateIndex == 0) {
                        list =  TemplateData.getSystemInstance().answerList;//使用系统模板
                    } else {
                        list = dataCenter.loadTemplateList().get(templateIndex - 1).answerList;//使用自定义模板
                    }
                    final String[] sentenceShown = list.toArray(new String[list.size()]);

                    AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("回答")
                            .setItems(sentenceShown, new DialogInterface.OnClickListener() {//添加列表
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText qaItem = setAnswerItem("答：" + replaceKeyword(sentenceShown[i]));
                                    qaScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    qaItem.requestFocus();
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            });
        }

        public void setPageFromData(InfoData infoData){
            //先清空视图
            for(int i = 0; i < qaEditTextList.size(); ++i){
                qaLayout.removeView(qaEditTextList.get(i));
            }
            qaEditTextList.clear();

            //根据qaList填充视图
            for(int i = 0; i < infoData.qaList.size(); ++i){
                String qaStr = infoData.qaList.get(i);
                if(qaStr.startsWith("问")){
                    setQuestionItem(qaStr);
                } else {
                    setAnswerItem(qaStr);
                }
            }
        }

        public void setDataFromPage(InfoData infoData){
            infoData.qaList.clear();
            for(int i = 0; i < qaEditTextList.size(); ++i){
                infoData.qaList.add(qaEditTextList.get(i).getText().toString());
            }
        }


        public ArrayList<String> getQaList(){
            ArrayList<String> list = new ArrayList<>();
            for(int i = 0; i < qaEditTextList.size(); ++i){
                list.add(qaEditTextList.get(i).getText().toString());
            }
            return list;
        }

        public void reloadTemplate(){
            ArrayList<String> list = dataCenter.getTemplateNameList();
            list.add(0, TemplateData.getSystemInstance().name);
            adapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.template_spinner_item_layout,R.id.template_item_name,list);
            template.setAdapter(adapter);
        }

        private EditText setQuestionItem(String questionStr){
            final LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.question_item_layout, null).findViewById(
                    R.id.question_item_layout);
            setQaItem(layout, R.id.delete_question);
            EditText qaItem = (EditText)layout.findViewById(R.id.question_item);
            qaItem.setText(questionStr);
            qaEditTextList.add(qaItem);
            return qaItem;
        }
        private EditText setAnswerItem(String answerStr){
            final LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.answer_item_layout, null).findViewById(
                    R.id.answer_item_layout);
            setQaItem(layout, R.id.delete_answer);
            EditText qaItem = (EditText)layout.findViewById(R.id.answer_item);
            qaItem.setText(answerStr);
            qaEditTextList.add(qaItem);
            return qaItem;
        }
        private void setQaItem(LinearLayout layout, int deleteId){
            Button deleteQa = layout.findViewById(deleteId);
            deleteQa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View vp = (View)v.getParent();
                    while(vp.getId() != R.id.question_item_layout && vp.getId() != R.id.answer_item_layout
                            && vp != null){
                        vp = (View)vp.getParent();
                    }
                    if(vp != null && (vp.getId() == R.id.question_item_layout || vp.getId() == R.id.answer_item_layout)){
                        LinearLayout layoutDeleted = (LinearLayout)vp;
                        EditText qaItem;
                        if(vp.getId() == R.id.question_item_layout) {
                            qaItem = (EditText) layoutDeleted.findViewById(R.id.question_item);
                        } else {
                            qaItem = (EditText) layoutDeleted.findViewById(R.id.answer_item);
                        }
                        qaEditTextList.remove(qaItem);
                        qaLayout.removeView(vp);

                    }

                }
            });
            qaLayout.addView(layout);
        }

        private String replaceKeyword(String qaString){
            String result = qaString.replaceAll("\\{空白\\}", "");
            String departmentName = printPage.department.getText().toString();
            if(!departmentName.isEmpty()){
                result =  result.replaceAll("\\{工作单位\\}", departmentName);
            }
            return result;
        }
    }

    //用于存放"打印"页面的控件
    private class PrintPage{
        public EditText department;
        public EditText departmentEmail;
        private ProgressDialog progressDialog;
        private Handler handler;
        private final int CREATE_PDF_DONE_EVENT = 0x111;
        private final int EMAIL_PDF_DONE_EVENT = 0x112;
        private final int EMAIL_PDF_SUCCEED = 1;
        private final int EMAIL_PDF_FAIL = 0;

        public void preparePrint(){
            department = (EditText) findViewById(R.id.department_name);
            departmentEmail = (EditText) findViewById(R.id.department_email);
            Button toPdfButton = (Button) findViewById(R.id.to_pdf_button);
            toPdfButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击"生成pdf"按钮后，先校验文件读写权限，然后开新线程生成pdf
                    PermisionUtils.verifyStoragePermissions(HomeActivity.this);
                    //声明进度条对话框
                    progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("第一次生成pdf文件需要10～20秒，第二次开始就会很快，请耐心等候。");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == CREATE_PDF_DONE_EVENT) {
                                if(progressDialog != null) {
                                    progressDialog.hide();
                                }
                            }
                        }

                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                createPDF(HomeActivity.this, pdfFileName);
                                TextView toPdfTime = (TextView) findViewById(R.id.to_pdf_time);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new Date());
                                String dateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
                                        , calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                                toPdfTime.setText("最后生成时间：" + dateTime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Message msg = new Message();
                                msg.what = CREATE_PDF_DONE_EVENT;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();

                }
            });

            Button openPdfButton = (Button) findViewById(R.id.open_pdf_button);
            openPdfButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dirPath = Environment.getExternalStorageDirectory().getPath();
                    //DataCenter.MailDataSaver.createFile(dirPath + "/mailfrom");//这个代码记得注释掉
                    startActivity(getPdfFileIntent(dirPath + "/" + pdfFileName));
                }
            });

            Button emailPdfButton = (Button) findViewById(R.id.email_pdf_button);
            emailPdfButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermisionUtils.verifyStoragePermissions(HomeActivity.this);
                    PermisionUtils.verifyInternetPermissions(HomeActivity.this);
                    String emailAddress = departmentEmail.getText().toString();
                    if(!isEmailLegal(emailAddress)){
                        Toast.makeText(getApplicationContext(), "邮箱格式不正确！" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //声明进度条对话框
                    progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("邮件发送中，请耐心等候。");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == EMAIL_PDF_DONE_EVENT) {
                                if(progressDialog != null) {
                                    progressDialog.hide();

                                    String emailAddress = departmentEmail.getText().toString();
                                    if(msg.arg1 == EMAIL_PDF_SUCCEED) {
                                        Toast.makeText(getApplicationContext(), "邮件发送到" + emailAddress + "成功！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "邮件发送到" + emailAddress + "失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            try {
                                String dirPath = Environment.getExternalStorageDirectory().getPath();
                                EmailProcessor ep = EmailProcessor.getDefaultInstance(HomeActivity.this);
                                String emailAddress = departmentEmail.getText().toString();
                                boolean result = ep.mail(emailAddress,
                                        "从笔录软件发来的pdf附件",
                                        "笔录文档见附件",
                                        dirPath + "/" + pdfFileName);
                                msg.arg1 = result ? EMAIL_PDF_SUCCEED : EMAIL_PDF_FAIL;
                            } catch (Exception e) {
                                msg.arg1 = EMAIL_PDF_FAIL;
                            } finally {
                                msg.what = EMAIL_PDF_DONE_EVENT;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();

                }
            });

        }

        public void setPageFromData(InfoData infoData){
            department.setText(infoData.department);
            departmentEmail.setText(infoData.departmentEmail);
        }

        public void setDataFromPage(InfoData infoData){
            infoData.department = department.getText().toString();
            infoData.departmentEmail = departmentEmail.getText().toString();
        }

        //android获取一个用于打开PDF文件的intent
        private Intent getPdfFileIntent(String Path)
        {
            File file = new File(Path);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            return intent;
        }

        //校验Email格式是否正确
        private boolean isEmailLegal(String email) {
            if (email != null && email.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
                return true;
            }
            return false;
        }
    }

    //用于存放"模板"页面的控件
    private class TemplatePage{
        public Spinner templateSelector;
        private ArrayAdapter<String> adapter;
        public ArrayList<String> templateNameList;
        public EditText templateName;
        public Switch qaSwitch;
        public TextView qaSwitchText;
        public Button saveTemplate;
        public Button importSystemTemplate;
        public Button deleteTemplate;
        public EditText templateSentences;
        public TemplateData templateData;

        public void prepareTemplate(){
            templateSelector = (Spinner) findViewById(R.id.template_selector);
            templateNameList = new ArrayList<String>();
            templateData = new TemplateData();
            templateNameList.add("新增模板...");
            adapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.template_spinner_item_layout,R.id.template_item_name,templateNameList);
            templateSelector.setAdapter(adapter);
            templateSelector.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if(position == templateNameList.size() - 1){
                        templateName.setText("");
                        qaSwitch.setChecked(false);
                        templateData = new TemplateData();
                        templateSentences.setText("");
                    } else if(position < templateNameList.size() - 1){
                        ArrayList<TemplateData> list = dataCenter.loadTemplateList();
                        templateName.setText(list.get(position).name);
                        qaSwitch.setChecked(false);
                        templateData = list.get(position);
                        templateSentences.setText(templateData.getQuestionListString());
                    }

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }});
            templateName = (EditText) findViewById(R.id.template_name);
            qaSwitch = (Switch) findViewById(R.id.qa_switch);
            qaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        qaSwitchText.setText("答");
                        templateData.setQuestionList(templateSentences.getText().toString());
                        templateSentences.setText(templateData.getAnswerListString());
                    } else {
                        qaSwitchText.setText("问");
                        templateData.setAnswerList(templateSentences.getText().toString());
                        templateSentences.setText(templateData.getQuestionListString());
                    }
                }
            });
            qaSwitchText = (TextView) findViewById(R.id.qa_switch_text);
            saveTemplate = (Button) findViewById(R.id.save_template);
            saveTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDataFromPage();
                    templateSelector.setSelection(0);
                    adapter.notifyDataSetChanged();
                    qaPage.reloadTemplate();
                    qaPage.template.setSelection(0);
                    qaPage.adapter.notifyDataSetChanged();
                    ArrayList<TemplateData> list = dataCenter.loadTemplateList();
                    setPageFromData(list);
                    Toast.makeText(getApplicationContext(), "模板保存成功！", Toast.LENGTH_SHORT).show();
                }
            });
            importSystemTemplate = (Button) findViewById(R.id.import_system_template);
            importSystemTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TemplateData systemTemplate = TemplateData.getSystemInstance();
                    templateData.questionList.addAll((ArrayList<String>)systemTemplate.questionList);
                    templateData.answerList.addAll((ArrayList<String>)systemTemplate.answerList);
                    templateSentences.setText(qaSwitch.isChecked() ? templateData.getAnswerListString() : templateData.getQuestionListString());
                }
            });
            deleteTemplate = (Button) findViewById(R.id.delete_template);
            deleteTemplate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = templateSelector.getSelectedItemPosition();
                    if(position < templateNameList.size() - 1) {
                        ArrayList<TemplateData> list = dataCenter.loadTemplateList();
                        list.remove(position);
                        dataCenter.saveTemplateList(list);
                        templateSelector.setSelection(0);
                        adapter.notifyDataSetChanged();
                        qaPage.reloadTemplate();
                        qaPage.template.setSelection(0);
                        qaPage.adapter.notifyDataSetChanged();
                        setPageFromData(list);
                    }
                }
            });
            templateSentences = (EditText) findViewById(R.id.template_sentences);
        }
        public void setPageFromData(ArrayList<TemplateData> templateList){
            templateNameList.clear();
            for(int i = 0; i < templateList.size(); ++i){
                templateNameList.add(templateList.get(i).name);
            }
            templateNameList.add("新增模板...");
            int position = templateSelector.getSelectedItemPosition();
            if(position < templateList.size()) {
                switchToTemplate(templateList.get(position));
            }

        }

        private void saveDataFromPage(){
            ArrayList<TemplateData> templateList = dataCenter.loadTemplateList();
            templateData.name = templateName.getText().toString();
            if(qaSwitch.isChecked()){
                templateData.setAnswerList(templateSentences.getText().toString());
            } else {
                templateData.setQuestionList(templateSentences.getText().toString());
            }
            if(templateSelector.getSelectedItemPosition() < templateNameList.size() - 1){
                templateList.remove(templateSelector.getSelectedItemPosition());
            }
            templateList.add(templateData);
            Collections.sort(templateList);
            dataCenter.saveTemplateList(templateList);
        }

        private void switchToTemplate(TemplateData theTemplateData){
            templateData = theTemplateData;
            templateName.setText(templateData.name);
            qaSwitch.setChecked(false);
            templateSentences.setText(templateData.getQuestionListString());
        }
    }

}
