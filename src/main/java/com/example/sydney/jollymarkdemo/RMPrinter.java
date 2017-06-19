package com.example.sydney.jollymarkdemo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jolimark.Data_Bmp;
import com.jolimark.UsbPrinter;

public class RMPrinter {
	
	private static String Error ;

	/*********************************************************************
	 * * 函数功能：热敏机芯字符打印示例
	 *   如果需要打印小票，发送排版好的字符串即可
	 ********************************************************************* */
	public static boolean printTextTest() {
		
		String testStr1 = "字符打印示例:\r\n";
		String chinese = "中文字型测试"+ "\r\n";
		String english = "ENGLISH TEST"+ "\r\n";
		Inialize();//初始化
		setChineseNormal();//爱普生pos指令，标准大小中文字
		
		try {
			boolean retnVale = printData(testStr1 );
			if (!retnVale) {			 
				return retnVale;
			}
			retnVale = printPos( get20to7f() );//打印ASCII码 从0x20到0x7F
			if (!retnVale) {			 
				return retnVale;
			}
			feedOfLine(1);
			retnVale = printPos( getHanZiA() );//打印汉子码表一小段汉子
			if (!retnVale) {			 
				return retnVale;
			}
			feedOfLine(1);
			
			setEnglishSmall(); // 8*16 英文字体
			retnVale = printData(english);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setEnglishNormal();  //12*24 英文字体
			retnVale = printData(english);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setChineseWide(); //倍宽字体
			retnVale = printData(chinese);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setChineseHigh(); //倍高字体
			retnVale = printData(chinese);
			if (!retnVale) {			 
				return retnVale;
			}

			setChineseWideHigh(); //倍宽倍高字体
			retnVale = printData(chinese);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setChineseNormal(); //恢复汉子标准字体
			setEnglishNormal(); // 12*24 英文字体
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	//pos指令，初始化打印机
	public static void Inialize() {

		byte SData[] = {(byte)0x1B,(byte)0x40}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//pos指令

	/*********************************************************************
	 * * 函数功能：设置打印机打印中文字体模式为标准大小中文字（常规字体） //标准pos指令
	 ********************************************************************* */

	public static void setChineseNormal() {
		byte SData[] = {(byte)28,(byte)33,(byte) 0}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*********************************************************************
	 * * 函数功能：设置打印机打印中文字体模式为中文倍宽字体（宽字体）//标准pos指令
	 ********************************************************************* */

	public static void setChineseWide() {
		byte SData[] = {(byte)28,(byte)33,(byte)4}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*********************************************************************
	 * * 函数功能：设置打印机打印中文字体模式为倍高字体（长字体）//标准pos指令
	 ********************************************************************* */

	public static void setChineseHigh() {
		byte SData[] = {(byte)28,(byte)33,(byte)8}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*********************************************************************
	 * * 函数功能：设置打印机打印中文字体模式为倍宽、倍高字体（大字体）//标准pos指令
	 ********************************************************************* */

	public static void setChineseWideHigh() {
		byte SData[] = {(byte)28,(byte)33,(byte)12}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*********************************************************************
	 * * 函数功能：设置打印机打印英文字体模式为标准字体  12*24  //标准pos指令
	 ********************************************************************* */

	public static void setEnglishNormal() {
		byte SData[] = {(byte)27,(byte)77,(byte)0}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	/*********************************************************************
	 * * 函数功能：设置打印机打印英文字体模式为小号字体 8*16   //标准pos指令
	 ********************************************************************* */
	
	public static void setEnglishSmall() {
		byte SData[] = {(byte)27,(byte)77,(byte)1}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*********************************************************************
	 * * 函数功能：热敏机 字符打印 
	 * * 入口参数：printData 排版后的字符串
	 * * 返回值：    ret =true 成功
	 * * 注释：如果需要打印小票，发送排版好的字符串即可
	 ********************************************************************* */
	public static boolean printData(String printData)
			throws UnsupportedEncodingException {
		UsbPrinter tmpUsbDev = new UsbPrinter();
		boolean ret = false;
		byte[] SData = null;
		ret = tmpUsbDev.Open();

		if (ret) {
			SData = printData.getBytes("GB2312");
			ret = tmpUsbDev.WriteBuf(SData, SData.length);
			//	ret = tmpUsbDev.WriteString(printData);//也可直接使用WriteString(String str)

			if (!ret) {
				getErr(tmpUsbDev);
			}
			tmpUsbDev.Close();
		}
		tmpUsbDev = null ;
		return ret;
	}
	/*********************************************************************
	 * * 函数功能：热敏机 字符打印 
	 * * 入口参数：printData 排版后的字符串
	 * * 返回值：    ret =true 成功
	 ********************************************************************* */
	public static boolean printPos(byte[] sData)throws UnsupportedEncodingException {
		UsbPrinter tmpUsbDev = new UsbPrinter(); 
		boolean ret = false;
		ret = tmpUsbDev.Open();		
		if (ret) {
			ret = tmpUsbDev.WriteBuf(sData, sData.length);
			if (!ret) {
				getErr(tmpUsbDev);
			}
			tmpUsbDev.Close();
		}
		tmpUsbDev = null ;
		return ret;
	}
	/*********************************************************************
	 * * 函数功能：走纸若干行    //标准pos指令
	 * @return 
	 ********************************************************************* */
	public static boolean feedOfLine(int lineNumber) {
		boolean retnVale = false;
		byte SData[] = {(byte)27,(byte)100,(byte)1}; 
		if (lineNumber >=255) {
			return retnVale;
		}
		SData[2] = (byte)lineNumber;

		try {
			retnVale = printPos(SData);			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retnVale;
	}
	/*********************************************************************
	 * * 函数功能：走纸若干行     使用enter代替pos指令  另一种换行方式
	 ********************************************************************* */
	public static boolean feedOfEnter(int line) {
		String enter = "\r\n";
		for (int i = 1; i < line; i++) {
			enter = enter + "\r\n";
		}
		boolean retnVale = false;
		try {
			retnVale = printData(enter);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retnVale;
	}
	
	/*********************************************************************
	 * * 函数功能：图形打印函数
	 ********************************************************************* */
	public static boolean printPicTest( Context mContext,String fileName ) {
		UsbPrinter tmpUsbDev = new UsbPrinter(); 
		byte[] BmpDataBuf = null;
		InputStream in = null;
		try {
			in = mContext.getResources().getAssets().open(fileName); // 载入 图形文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 把图形文件转换成热门打印机能够识别的数据
		Data_Bmp tmpDataBmp = null;
		Bitmap bitmap = null;

		bitmap = BitmapFactory.decodeStream(in);
		tmpDataBmp = new Data_Bmp(bitmap);
		bitmap.recycle();
		bitmap = null;

		ByteArrayOutputStream destStream = new ByteArrayOutputStream();
		int ret = tmpDataBmp.SavePrintDataToStream(destStream);
		if (ret > 0) {
			BmpDataBuf = destStream.toByteArray();
		}
		// 打印图形
		boolean retnVale = false;
		if (BmpDataBuf != null) {
			
			retnVale = tmpUsbDev.Open();
			if (retnVale) {
				 
				retnVale = tmpUsbDev.WriteBuf(BmpDataBuf, BmpDataBuf.length); // 打印命令
				if (!retnVale) {
					getErr(tmpUsbDev);
				}
				tmpUsbDev.Close();
			}
 
		}
		return retnVale;

	}
	/*********************************************************************
	 * * 函数功能：显示打印机错误函数
	 ********************************************************************* */
	private static void getErr(UsbPrinter tmpUsbDev) {	
		Error = tmpUsbDev.GetLastPrintErr();		
	}
	
	//获取错误字符串
	public static String getErrorStr() {			 
		return Error;		
	}
	//打印ASCII码 0x20到0x7f   作为演示使用
	private static byte[]  get20to7f(){
		 
		byte[] SData = new byte[0x7f-0x20];
		for(int i = 0x20, j = 0; i<0x7f; i++,j++){ 
			SData[j] = (byte) i;				
		}		
		return SData;	

	}
	//打印GB字库 部分内容   作为演示使用
	private  static byte[] getHanZiA(){
		byte[] SData = new byte[(0xfe - 0xA1)*2];

		for(int i = 0xA1, j = 0;  i<0xfe ; i++,j++){
			SData[j] = (byte) 0xB0;
			//j++;
			SData[++j] = (byte) i;	
		}		
		return SData;			
	}
	
    //开纸斗盖
	public static boolean UnLock() {
		
		boolean retnVale = false;
		UsbPrinter tmpUsbDev = new UsbPrinter();
		retnVale = tmpUsbDev.UnLock();

		return retnVale;
	}
	
    //开钱箱
	public static boolean UnLockOfCashBox() {
		
		boolean retnVale = false;
		UsbPrinter tmpUsbDev = new UsbPrinter();
		retnVale = tmpUsbDev.UnLockOfCashBox();

		return retnVale;
	}
}
