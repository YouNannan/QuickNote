package com.younannan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TemplateData implements Serializable, Comparable<TemplateData>{
    private static final long serialVersionUID = 4568097527509829565L;

    public String name;
    public ArrayList<String> questionList;
    public ArrayList<String> answerList;

    public long createTimeStamp;//创建时间
    public static final String[] systemQuestionArray= {
            "我们是{工作单位}民警（出示工作证），现在你被依法传唤，对你进行讯问，你要如实回答我们的问题，对本案无关的内容，你有拒绝回答的权利，你听清楚了吗？",
            "你的基本情况？",
            "你知道受害人和犯罪嫌疑人是什么关系吗？",
            "事发当天你在场吗？",
            "请你描述一下当天的情况。",
            "你把事情经过描述一下。",
            "能说的具体点吗？比如时间、地点。",
            "还有什么要补充的吗？",
            "你说的情况是否属实？",
            "请你把以上笔录看一下，是否与你所说的相符？",
            "{空白}",
    };
    public static final String[] systemAnswerArray= {
            "我清楚了。",
            "在。",
            "是。",
            "没有了。",
            "属实。",
            "以上笔录我已看过，与我所说的相符。",
            "{空白}",
    };

    public TemplateData() {
        super();
        name = "自定义模板";
        questionList = new ArrayList<String>();
        answerList = new ArrayList<String>();
        createTimeStamp = new Date().getTime();
    }

    public static TemplateData getSystemInstance(){
        TemplateData systemTemplate = new TemplateData();
        systemTemplate.createTimeStamp = Long.MAX_VALUE;//让系统模板有最高排序优先级
        systemTemplate.name = "系统模板";
        for(int i = 0; i < systemQuestionArray.length; ++i){
            systemTemplate.questionList.add(systemQuestionArray[i]);
        }
        for(int i = 0; i < systemAnswerArray.length; ++i){
            systemTemplate.answerList.add(systemAnswerArray[i]);
        }
        return systemTemplate;
    }

    public void setQuestionList(String str){
        questionList = stringToList(str);
    }
    public void setAnswerList(String str){
        answerList = stringToList(str);
    }
    public String getQuestionListString(){
        return listToString(questionList);
    }
    public String getAnswerListString(){
        return listToString(answerList);
    }
    private static String listToString(ArrayList<String> list){
        StringBuffer sb =  new StringBuffer();
        for(int i = 0; i < list.size(); ++i){
            sb.append(list.get(i) + "\n");
        }
        return sb.toString();
    }
    private static ArrayList<String> stringToList(String str){
        String[] array = str.split("\n");
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < array.length; ++i){
            list.add(array[i]);
        }
        return list;
    }

    //排序算法是时间逆序的
    @Override
    public int compareTo(TemplateData other) {
        return (int)(((other.createTimeStamp - this.createTimeStamp) / 1000) & 0xffffffff);
    }
}
