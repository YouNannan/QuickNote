package com.younannan.tool;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
用于处理pdf的工具类
 */
public class PdfProcessor {

    private static final String SPECAL_CHAR_TTF_PATH = "com/itextpdf/text/pdf/fonts/cmaps/seguisym.ttf";
/*
    public static void test(String dest){
        PdfProcessor pdfProcessor = null;
        try {
            pdfProcessor = new PdfProcessor(dest);
            ArrayList<String> qaList = new ArrayList<String>();
            qaList.add("问：我们是贵阳市公安局公交分局民警，根据《中华人民共和国刑事诉讼法》第一百一十八之规定，现依法对你进行讯问，你是否有犯罪\r\n" +
                    "行为？并如实陈述有罪的情节或者作无罪的辩解，听清楚了吗？");
            qaList.add("答：我听清楚了。");
            qaList.add("问：你的姓名、别名、曾用名、性别、出生年月日、户籍所在地、现\r\n" +
                    "住地、籍贯、出生地、民族、职业、文化程度、身份证号码？");
            qaList.add("答：这部分内容是讯问笔录记录的重点。根据讯问次数的不同，记录内容也有所不同。第一次讯问时，要在第一部分依项讯问和记录清楚犯罪嫌疑人的基本情况，包括姓名、曾用名、化名、年龄或出生年月日、民族、籍贯、文化程度、现住址、工作单位、职务与职业、家庭情况、社会经历、是否受过刑事处罚或行政处分等情况。要与原案件材料认真核对，严防错拘错捕。另外，还要问清记明犯罪嫌疑人是否知道为什么被拘留或被逮捕。\r\n" +
                    "除第一次讯问外，以后的系列讯问可不再问基本情况。可直接进行第二部分讯问内容。\r\n" +
                    "第二部分，要问清记明讯问的全部过程，记录人首先要记清讯问人的提问，根据提问的中心问题，全面准确地记载犯罪嫌疑人关于犯罪事实的供辩。"
                    + "这一部分内容要根据讯问的原过程准确清楚地证明犯罪的时间、地点、动机、目的、手段、起因、后果、证据、涉及到的人和事等，"
                    + "尤其是其中能说明案件性质的关键情节、有关的证据、有明显矛盾的地方等重要情况，要注意准确清楚地记录下来。如果犯罪嫌疑人进行无罪辩解，"
                    + "要注意记清其陈述的理由和依据。此外，犯罪嫌疑人的认罪态度如何，也要准确地记录下来。");
            qaList.add("问：现向你宣读《犯罪嫌疑人诉讼权利义务告知书》和《法律援助权\r\n" +
                    "利义务告知书》，并交给你，你是否收到，是否有阅读能力？  ");
            qaList.add("答：《中华人民共和国刑事诉讼法》第116条 [1]  规定，讯问犯罪嫌疑人必须由人民检察院或者公安机关的侦查人员负责进行。"
                    + "讯问时，侦查人员不得少于二人。犯罪嫌疑人被送交看守所羁押以后，侦查人员对其进行讯问，应当在看守所内进行。\r\n" +
                    "讯问笔录的书写应当用钢笔、毛笔或其他能长期保持字迹的书写工具。\r\n" +
                    "讯问笔录首部内容的填写要内容齐全，不得漏填。\r\n" +
                    "笔录记录内容要清楚、全面、准确。对犯罪嫌疑人的供述，不仅要记“七何”要素，还应该尽可能完整地再现原始犯罪过程；"
                    + "对犯罪嫌疑人供述认罪的情况要记，翻供辩解的也要记；态度老实的要记，态度顽固等不老实的也要记；"
                    + "有回答的要记，拒绝回答、沉默的场面也要记；纪录要如实反映犯罪嫌疑人供述的原意，不能随意夸大、缩小或改变原意。"
                    + "特别是对于涉及定罪定性的重要情节、重要供词，应尽可能地记录原话。对于涉及黑话、方言、特殊内容的词语也要用括号作说明解释；"
                    + "对于讯问过程中犯罪嫌疑人的表情、语气、体态语等也要用括号作准确适当的描写。\r\n" +
                    "讯问笔录结尾核对手续一定要认真履行，以保证笔录的法律有效性。\r\n" +
                    "讯问笔录在整个刑事诉讼中占有重要地位，侦查结束时，讯问笔录存入侦查案卷(主卷)。");
            qaList.add("问：还有什么要交代的？");
            qaList.add("没有了");
            qaList.add("");
            qaList.add("真的没有了，没有了啊，确实没有了。真的没有了，没有了啊，确实没有了。真的没有了，没有了啊，确实没有了。真的没有了，没有了啊，确实没有了。真的没有了，没有了啊，确实没有了。");
            pdfProcessor.fillInfo("1", "2018", "12", "13", "02", "31",
                    "2018", "12", "14", "12", "05",
                    "湖北省武汉市东湖高新技术开发区紫菘街道",//紫菘小区123号23栋16层1603室",
                    "张三", "李四", "武汉市气象局",
                    "王五", "湖北省地震局",
                    "赵二", "男", "37", "1981", "03", "17", "420106198103172695",
                    true,
                    true,
                    true,
                    "湖北省武汉市东湖高新技术开发区",//紫菘街道紫菘",//小区123号23栋16层1604室",
                    "13971987654",
                    "湖北省武汉市东湖高新技术开发区紫菘街道紫菘",//小区123号23栋16层1604室",
                    qaList);

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
*/

    //将诸多信息塞到pdf里
    public void fillInfo(String times,
                       String beginYear,
                       String beginMonth,
                       String beginDay,
                       String beginHour,
                       String beginMinute,
                       String endYear,
                       String endMonth,
                       String endDay,
                       String endHour,
                       String endMinute,
                       String location,
                       String askMan1,
                       String askMan2,
                       String askDepartment,
                       String recordMan,
                       String recordDepartment,
                       String answerMan,
                       String sex,
                       String age,
                       String birthYear,
                       String birthMonth,
                       String birthDay,
                       String idNum,
                       boolean isRendadaibiao,
                       boolean isCriminal,//是否是刑事嫌疑人
                       boolean isIllegal,//是否是违法嫌疑人
                       String nowAddress,
                       String phoneNum,
                       String residenceAddress,
                       ArrayList<String> qaList)
    {
        String stringXun = "询";
        if(isCriminal){
            stringXun = "讯";
        }
        addHeader(times);
        addTitle(stringXun + " 问 笔 录");
        add("时间 ").add(beginYear, true).add("年")
                .add(beginMonth, true).add("月")
                .add(beginDay, true).add("日")
                .add(beginHour, true).add("时")
                .add(beginMinute, true).add("分 至 ")
                .add(endYear, true).add("年")
                .add(endMonth, true).add("月")
                .add(endDay, true).add("日")
                .add(endHour, true).add("时")
                .add(endMinute, true).add("分")
                .endParagraph();
        add(stringXun + "问地点 ").add(location, true, 146)
                .endParagraph();
        add(stringXun + "问人 ").add(askMan1, true, 18).add(" 、").add(askMan2, true, 18)
                .add(" 工作单位 ").add(askDepartment, true, 86)
                .endParagraph();
        add("记录人 ").add(recordMan, true, 43).add(" 工作单位 ").add(recordDepartment, true, 86)
                .endParagraph();
        add("被" + stringXun + "问人 ").add(answerMan, true, 18).addSpaces(6, false).add("性别").add(sex, true, 6)
                .addSpaces(6, false).add("年龄").add(age, true, 8)
                .addSpaces(6, false).add("出生日期").add(birthYear, true).add("年")
                .add(birthMonth, true).add("月").add(birthDay, true).add("日")
                .endParagraph();
        add("身份证种类及号码 ").add("居民身份证 " + idNum, true, 84)
                .addSpaces(10, false).add("人大代表 ").addSpecialChar(isRendadaibiao?"☑":"□")
                .endParagraph();
        add("现居地址 ").add(nowAddress, true, 89)
                .addSpaces(6, false).add("联系方式 ").add(phoneNum, true, 30)
                .endParagraph();
        add("户籍地址 ").add(residenceAddress, true, 146)
                .endParagraph();
        if(isCriminal || isIllegal){
            add("（口头传唤 / 被扭送 / 自动投案的被" + stringXun + "问人于   ___月___日___时___分到达，")
                    .endParagraph();
            add("___月___日___时___分离开， 本人签名：").addSpaces(40, true).add("  ）")
                    .endParagraph();
        }
        for(int i = 0; i < qaList.size(); ++i) {
            String content = qaList.get(i).replaceAll("\\r\\n", " ");
            content = content.replaceAll("\\n", " ");
            if(content.length() < 2) {
                addSpaces(168, true).endParagraph();
                continue;
            }
            String headStr = content.substring(0, 2);
            if("问：".equals(headStr)) {
                add(content, false, 168).endParagraph();
            } else if("答：".equals(headStr)){
                add(headStr).add(content.substring(2), true, 156).endParagraph();
            } else {
                add(content, true, 168).endParagraph();
            }
        }
    }


    private Document document;
    private String savePath;
    private PdfWriter writer;
    private Paragraph paragraph;
    private int fontSize;

    // savePath:保存pdf的路径
    public PdfProcessor(String theSavePath) throws FileNotFoundException, DocumentException {
        savePath = theSavePath;
        //创建新的PDF文档：A4大小，左右上下边框均为0
        document = new Document(PageSize.A4,70,70,70,70);
        writer = PdfWriter.getInstance(document, new FileOutputStream(savePath + ".tmp"));
        document.open();
        fontSize = 13;
        paragraph = null;
    }


    public void close(){
        if (document.isOpen()) {
            document.close();
            try {
                addPageSize();
            } catch (DocumentException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new File(savePath + ".tmp").delete();
        }
    }

    public PdfProcessor setFontSize(int size){
        fontSize = size;
        return this;
    }
    public PdfProcessor endParagraph() {
        try {
            if(paragraph != null) {
                document.add(paragraph);
            }
        } catch (DocumentException e) {
        }
        paragraph = null;
        return this;
    }

    //添加spaceNum个空格的空白位置，注意：此处必须使用StringBuffer，而不能使用String的构造方法来拼装大片空格
    public PdfProcessor addSpaces(int spaceNum, boolean withUnderline){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < spaceNum; ++i) {
            sb.append(' ');
        }
        return add(sb.toString(), withUnderline, 0);
    }
    public PdfProcessor add(String content){
        return add(content, false);
    }
    public PdfProcessor add(String content, boolean withUnderline){
        if(withUnderline) {
            content = " " + content + " ";
        } else {
            content = content + " ";
        }
        return add(content, withUnderline, 0);
    }

    //向段落中写入content，其中minWidth规定了该文字至少有多少个空格的宽度
    public PdfProcessor add(String content, boolean withUnderline, int minWidth){
        if(paragraph == null) {
            paragraph = new Paragraph("", getChineseFont(fontSize));
            paragraph.setAlignment(Element.ALIGN_BASELINE);
            paragraph.setSpacingBefore(10);
        }

        Chunk chunk = new Chunk(content, getChineseFont(fontSize, withUnderline, false));
        if(chunk.getWidthPoint() < getOneSpaceWidth() * minWidth) {
            int filledSpaceNum = minWidth - (int)(chunk.getWidthPoint() / getOneSpaceWidth());
            chunk = new Chunk(getSpaces(1) + content + getSpaces(filledSpaceNum - 1),
                    getChineseFont(fontSize, withUnderline, false));
        }
        paragraph.add(chunk);
        return this;
    }

    public PdfProcessor addSpecialChar(String content) {
        BaseFont bf;
        try {
            bf = BaseFont.createFont(SPECAL_CHAR_TTF_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, fontSize);
            Chunk chunk = new Chunk(content, font);
            paragraph.add(chunk);
        } catch (DocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }


    //添加右上角的次数标注
    private PdfProcessor addHeader(String times){
        Phrase phrase = new Phrase("第", getChineseFont(10));
        Chunk chunk = new Chunk(" " + times + " ", getChineseFont(12));
        chunk.setUnderline(0.5f, -4f);
        phrase.add(chunk);
        phrase.add(new Chunk("次", getChineseFont(10)));
        PdfContentByte pcb = writer.getDirectContentUnder();
        ColumnText.showTextAligned(pcb, Element.ALIGN_RIGHT, phrase, 505, 795, 0);
        return this;
    }

    // 给pdf添加个标题，居中黑体
    public PdfProcessor addTitle(String title){
        paragraph = new Paragraph(title, getChineseFont(28, false, true));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(30);
        endParagraph();
        return this;
    }



    private float getOneSpaceWidth() {
        Chunk chunk = new Chunk(" ", getChineseFont(fontSize, true, false));
        return chunk.getWidthPoint();
    }
    private static String getSpaces(int spaceNum){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < spaceNum; ++i) {
            sb.append(' ');
        }
        return sb.toString();
    }
    private static Font getChineseFont(int size) {
        return getChineseFont(size, false, false);
    }
    private static Font getChineseFont(int size, boolean withUnderline, boolean isBold) {
        BaseFont bf;
        Font fontChinese = null;
        try {
            // STSong-Light : Adobe的字体
            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, size,
                    (withUnderline?Font.UNDERLINE:Font.NORMAL)|(isBold?Font.BOLD:Font.NORMAL));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }


    //重新生成一个加上了总页码数字的pdf
    private void addPageSize() throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(savePath));
        // step 3
        document.open();
        // step 4
        // reader for document 1
        PdfReader reader = new PdfReader(savePath + ".tmp");
        int n = reader.getNumberOfPages();
        // initializations
        PdfImportedPage page;
        PdfCopy.PageStamp stamp;
        // Loop over the pages of document 1
        for (int i = 0; i < n; ) {
            page = copy.getImportedPage(reader, ++i);
            stamp = copy.createPageStamp(page);
            // add page numbers
            ColumnText.showTextAligned(
                    stamp.getUnderContent(), Element.ALIGN_CENTER,
                    new Phrase(String.format("第 %d 页  /  共 %d 页", i, n), getChineseFont(12)),
                    486f, 38f, 0);
            stamp.alterContents();
            copy.addPage(page);
        }
        // step 5
        document.close();
        reader.close();
    }
}
