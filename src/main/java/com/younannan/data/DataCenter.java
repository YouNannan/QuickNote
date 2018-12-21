package com.younannan.data;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.younannan.fun.quicknote.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DataCenter {

	private String saveDir;
	private static final String INFO_FILENAME = "data_info";
	private static final String TEMPLATE_FILENAME = "data_template";
	private static final String ENCRYPT_PASSWORD = "iloveyouILOVEYOU";
	private static final String EMAIL_ACCOUNT = "bilu2017@126.com";


    public DataCenter(String theSaveDir) {
        saveDir = theSaveDir;
    }


	public void saveInfoDataList(ArrayList<InfoData> infoDataList) {
		saveObject(infoDataList, saveDir + "/" + INFO_FILENAME);
	}

    @SuppressWarnings("unchecked")
	public ArrayList<InfoData> loadInfoDataList() {
        try{
			ArrayList<InfoData> list = (ArrayList<InfoData>)loadObject(saveDir + "/" + INFO_FILENAME);
			if(list == null){
				return new ArrayList<InfoData>();
			}
            return list;
        } catch (ClassCastException e){
            return new ArrayList<InfoData>();
        }
    }

    public ArrayList<String> getTemplateNameList(){
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<TemplateData> templateList = loadTemplateList();
        for(int i = 0; i < templateList.size(); ++i){
            nameList.add(templateList.get(i).name);
        }
        return nameList;
    }

	public void saveTemplateList(ArrayList<TemplateData> list) {
		saveObject(list, saveDir + "/" + TEMPLATE_FILENAME);
	}

    @SuppressWarnings("unchecked")
	public ArrayList<TemplateData> loadTemplateList() {
		try{
			ArrayList<TemplateData> list = (ArrayList<TemplateData>)loadObject(saveDir + "/" + TEMPLATE_FILENAME);
			if(list == null){
				return new ArrayList<TemplateData>();
			}
			return list;
		} catch (ClassCastException e){
			return new ArrayList<TemplateData>();
		}
    }
    
    

   	public static String getEmailAccount() {
   		return EMAIL_ACCOUNT;
   	}
       
	public static String getEmailPassword(Context context) {
		InputStream in = context.getResources().openRawResource(R.raw.mailfrom);
		String encrypted = (String)loadObject(in);
		return decrypt(encrypted, ENCRYPT_PASSWORD);
	}
    
	public static void saveObject(Object obj, String savePath) {
        File file =new File(savePath);
        FileOutputStream out = null;
        ObjectOutputStream objOut = null;
        try {
            out = new FileOutputStream(file);
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if(objOut != null) {
        		try {
					objOut.close();
				} catch (IOException e) {
				}
        	}
        	if(out != null) {
        		try {
					out.close();
				} catch (IOException e) {
				}
        	}
        }
	}

	public static Object loadObject(String savePath) {
		File file = new File(savePath);
		try {
			FileInputStream in = new FileInputStream(file);
			return loadObject(in);
		} catch(FileNotFoundException e){
			return null;
		}
	}
	private static Object loadObject(InputStream in) {
    	Object obj = null;
        ObjectInputStream objIn = null;
        try {
            objIn = new ObjectInputStream(in);
            obj = objIn.readObject();
            objIn.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        	if(objIn != null) {
        		try {
					objIn.close();
				} catch (IOException e) {
				}
        	}
        	if(in != null) {
        		try {
					in.close();
				} catch (IOException e) {
				}
        	}
        }
    }
    
    
    
    
    
	public static final String VIPARA = "0102030405060708";
	public static final String bm = "GBK";
	private static String encrypt(String cleartext, String dataPassword) {
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
			Cipher cipher;
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));
			return CompatibleBase64.encode(encryptedData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "";
	}

	private static String decrypt(String encrypted, String dataPassword){
		try {
			byte[] byteMi = CompatibleBase64.decode(encrypted);
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte[] decryptedData = cipher.doFinal(byteMi);
			return new String(decryptedData,bm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "";
	} 
    
}
