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
    private static final String[] systemQuestionArray= {
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
    private static final String[] systemAnswerArray= {
            "我清楚了。",
            "在。",
            "是。",
            "没有了。",
            "属实。",
            "以上笔录我已看过，与我所说的相符。",
            "{空白}",
    };

    private static final String[] TEMPLATE_NAMES = {
            "被告人模板",
            "被害人模板",
            "干部模板",
            "家属模板",
    };
    private static final int ARRAYS_SIZE = TEMPLATE_NAMES.length;
    private static final String[][] preQuestionArrays= {
            {
                    "我们是{工作单位}的工作人员（出示证件，证件名称及编号），依据《社区矫正实施办法》及《浙江省社区矫正实施细则（试行）》等相关规定，受{委托单位}的委托，依法对你的个人表现、现实状况和社会背景等情况进行调查。你应当如实回答我们的询问并协助调查，不得提供虚假证言，不得伪造、隐匿、毁灭证据，否则将承担法律责任。你有权就被询问的事项自行提供书面材料，有权核对调查笔录，对记载有误或遗漏之处，可提出更正或补充意见。如所回答的问题涉及国家或商业秘密，我们将予以保密。本调查内容，我们会如实反馈给委托机关，并将作为你是否适用社区矫正的依据。以上内容你是否已听清楚？",
                    "你对本次调查的工作人员需不需要提出回避申请？ ",
                    "你目前居住在哪里，目前的居住地与户籍地是否一致？",
                    "居住地的房东是谁，能否提供相应的凭证？（目前居住的房屋所有权人是谁？）",
                    "打算居住多少时间，和谁居住在一起？",
                    "你目前从事什么职业，经济收入如何？",
                    "能否提供劳动合同或相关凭证？",
                    "你身体状况怎么样，有没有什么疾病？",
                    "你的性格类型属于哪一类（外向/内向），脾气如何？",
                    "你业余时间一般做什么，有没有什么特长或爱好？",
                    "平时与哪些人交往？",
                    "平时喝不喝酒，有没有酒后失常或误事的情况？",
                    "有没有其他不良嗜好？",
                    "你工作、学习或生活环境中，邻里、同事/同学之间关系如何？有无矛盾？",
                    "你与他人有没有经济纠纷（债务问题）或感情纠纷？",
                    "你在此之前有没有受过什么处罚，具体情况怎样？",
                    "你简要叙述一下此次涉嫌犯罪的事情经过？",
                    "你对此次涉嫌犯罪有什么认识？",
                    "对本案被害人的损失有没有作出赔偿，有无取得对方的谅解？",
                    "你的家庭基本情况说一下？（家庭成员的关系、姓名、年龄、职业、住址、经济状况、联系方式等）",
                    "你的家庭关系如何？",
                    "你家人/亲友对你涉嫌犯罪有何评价？",
                    "你的家庭经济状况如何？",
                    "你的保证人/监护人是否落实？保证人/监护人的情况说一下？（保证人/监护人的关系、姓名、年龄、职业、住址、经济状况、联系方式等）",
                    "如你被依法实施社区矫正，必须遵守以下规定:一、积极配合监管、帮教工作，按时到司法所报到，进行思想汇报和参加集中教育、社区服务；二、行踪实施监控；三、社区矫正开始的三个月内不准请假，特殊情况外出必须请假，未经批准不得外出等；四、如违反社区矫正相关监管规定，将依法给予警告、拘留、收监等处罚。你愿意接受司法所的监管和帮教吗？",
                    "你还有什么补充？",
                    "你以上所讲的是否属实？",
                    "{空白}",},
            {
                    "我们是{工作单位}的工作人员（出示证件），依据《社区矫正实施办法》及《浙江省社区矫正实施细则（试行）》等相关规定，受{委托单位}的委托，依法对{当事人}是否适用社区矫正的情况对你进行调查，希望你如实反映，是否已听清楚？",
                    "请你谈谈当时案件的具体情况？（包括你的受害情况） ",
                    "事后是如何处理？有无对你进行赔偿/补偿？",
                    "你现在身体状况如何？",
                    "你现在有无工作？",
                    "现在你和{当事人}关系如何？",
                    "现在法院/监狱拟对{当事人}社区矫正，你对这件事是怎么看的？",
                    "你还有无补充？",
                    "以上所说是否属实？",
                    "{空白}",
            },
            {
                    "我们是{工作单位}的工作人员（出示证件），依据《社区矫正实施办法》及《浙江省社区矫正实施细则（试行）》等相关规定，受{委托单位}的委托，依法对{当事人}是否适用社区矫正的情况对你进行调查，希望你如实反映，是否已听清楚？",
                    "你认识{当事人}吗？",
                    "{当事人}现在的居住地在哪里？目前和谁一起居住？",
                    "{当事人}家里有哪些人？（基本情况） ",
                    "{当事人}目前从事什么工作？",
                    "{当事人}家庭经济条件怎么样？",
                    "{当事人}性格如何？",
                    "{当事人}身体状况如何？",
                    "{当事人}平时生活习惯如何，有无特殊爱好？",
                    "{当事人}社会交往情况如何?",
                    "{当事人}与家庭成员、邻居、亲戚朋友相处关系如何？",
                    "{当事人}目前有无固定生活来源或者有无他人、有关单位提供生活保障？",
                    "{当事人}以前在社区/村里的表现如何？是否有过其他违法违纪行为？",
                    "此次犯罪的情况你是否清楚吗？ ",
                    "如{当事人}被实行社区矫正，你觉得周围邻居是否会对他有意见？",
                    "根据社区矫正的有关规定，如{当事人}被依法实施社区矫正，必须遵守以下规定:一、按时到司法所报到，进行思想汇报和参加集中教育、社区服务；二、行踪实施监控；三、社区矫正开始的三个月内不准请假，特殊情况外出必须请假，未经批准不得外出等；四、如违反社区矫正相关监管规定，将依法给予警告、拘留、收监等处罚。你是否愿意协助司法所落实相关监管帮教措施？ ",
                    "你还有无补充？ ",
                    "以上所说是否属实？",
                    "{空白}",
            },
            {
                    "我们是{工作单位}的工作人员（出示证件，证件名称及编号），依据《社区矫正实施办法》及《浙江省社区矫正实施细则（试行）》等相关规定，受{委托单位}的委托，依法对{当事人}是否适用社区矫正的情况进行调查，你应当如实回答我们的询问并协助调查，不得提供虚假证言，不得伪造、隐匿、毁灭证据，否则将承担法律责任。你有权核对调查笔录，对记载有误或遗漏之处，可提出更正或补充意见。如所回答的问题涉及国家或商业秘密，我们将予以保密。以上内容你是否已听清楚？",
                    "你对本次调查的工作人员需不需要提出回避申请？ ",
                    "你和{当事人}是什么关系？",
                    "{当事人}打算居住在何处，该处房屋的所有权人是谁，能否提供相应的凭证？",
                    "{当事人}目前有无固定生活来源，有无他人、有关单位提供生活保障？",
                    "{当事人}的性格脾气如何？",
                    "{当事人}的身体状况如何？",
                    "{当事人}的平时生活习惯如何？",
                    "{当事人}的社会交往情况如何?",
                    "{当事人}涉及犯罪的情况？（包括犯罪原因、涉及人员、简要案情、有无其他前科劣迹等）",
                    "{当事人}对此次犯罪有何想法？",
                    "对本案被害人的损失有没有作出赔偿，有无取得对方的谅解？",
                    "你们家庭的经济状况如何，对判决、裁定的罚金有无履行？",
                    "{当事人}的家里有哪些人？（基本情况）",
                    "{当事人}与家庭成员、邻居、朋友相处关系如何？　",
                    "如{当事人}被依法实行社区矫正，你觉得周围的邻居和社区是否会有意见？",
                    "如{当事人}被依法实行社区矫正，你是否愿意担任保证人/监护人？你是否具备固定的住处和生活来源？",
                    "根据社区矫正的有关规定，如{当事人}被依法实施社区矫正，必须遵守以下规定:一、积极配合监管、帮教工作，按时到司法所报到，进行思想汇报和参加集中教育、社区服务；二、行踪实施监控；三、社区矫正开始的三个月内不准请假，特殊情况外出必须请假，未经批准不得外出等；四、如违反社区矫正相关监管规定，将依法给予警告、拘留、收监等处罚。你愿意配合司法所对其进行监管和帮教吗？",
                    "你还有无补充？",
                    "以上所说是否属实？",
                    "{空白}",
            },
    };
    private static final String[][] preAnswerArrays= {
            {
                    "我清楚了。",
                    "在。",
                    "是。",
                    "没有了。",
                    "属实。",
                    "以上笔录我已看过，与我所说的相符。",
                    "{空白}",
            },
            {
                    "我清楚了。",
                    "在。",
                    "是。",
                    "没有了。",
                    "属实。",
                    "以上笔录我已看过，与我所说的相符。",
                    "{空白}",
            },
            {
                    "我清楚了。",
                    "在。",
                    "是。",
                    "没有了。",
                    "属实。",
                    "以上笔录我已看过，与我所说的相符。",
                    "{空白}",
            },
            {
                    "我清楚了。",
                    "在。",
                    "是。",
                    "没有了。",
                    "属实。",
                    "以上笔录我已看过，与我所说的相符。",
                    "{空白}",
            },
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

    public static ArrayList<TemplateData> getPreTemplateInstance(){
        ArrayList<TemplateData> list = new ArrayList<TemplateData>();
        for(int i = 0; i < ARRAYS_SIZE; ++i){
            TemplateData preTemplate = new TemplateData();
            preTemplate.createTimeStamp = Long.MAX_VALUE - i - 1;//让该类预设模板有除了系统模板外的最高排序优先级
            preTemplate.name = TEMPLATE_NAMES[i];
            for(int j = 0; j < preQuestionArrays[i].length; ++j){
                preTemplate.questionList.add(preQuestionArrays[i][j]);
            }
            for(int j = 0; j < preAnswerArrays[i].length; ++j){
                preTemplate.answerList.add(preAnswerArrays[i][j]);
            }
            list.add(preTemplate);
        }
        return list;
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
