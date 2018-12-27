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
        //addTitle(stringXun + " 问 笔 录");
        addTitle("调 查 评 估 笔 录");
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
                .addSpaces(10, false)//.add("人大代表 ").addSpecialChar(isRendadaibiao?"☑":"□")
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
