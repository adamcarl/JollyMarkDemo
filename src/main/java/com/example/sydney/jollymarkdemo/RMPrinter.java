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
	 * *Function Function: Thermal movement character printing example
	* If you need to print a small ticket, send a good layout can be a string
	 ********************************************************************* */
	public static boolean printTextTest() {
		
		String testStr1 = "Character print example:\r\n";
		String chinese = "Chinese font test"+ "\r\n";
		String english = "ENGLISH TEST"+ "\r\n";
		Inialize();//initialization
		setChineseNormal();//Epson pos instruction, standard size Chinese characters
		
		try {
			boolean retnVale = printData(testStr1 );
			if (!retnVale) {			 
				return retnVale;
			}
			retnVale = printPos( get20to7f() );//Print ASCII code from 0x20 to 0x7F
			if (!retnVale) {			 
				return retnVale;
			}
			feedOfLine(1);
			retnVale = printPos( getHanZiA() );//Print the Chinese character code table for a short man
			if (!retnVale) {			 
				return retnVale;
			}
			feedOfLine(1);
			
			setEnglishSmall(); // 8*16 English Fonts
			retnVale = printData(english);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setEnglishNormal();  //12*24 English Fonts
			retnVale = printData(english);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setChineseWide(); //Times wide font
			retnVale = printData(chinese);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setChineseHigh(); //Times high font
			retnVale = printData(chinese);
			if (!retnVale) {			 
				return retnVale;
			}

			setChineseWideHigh(); //Times the width of the high font
			retnVale = printData(chinese);
			if (!retnVale) {			 
				return retnVale;
			}
			
			setChineseNormal(); //Restore Chinese standard font
			setEnglishNormal(); // 12*24 English Fonts
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	//Pos instruction to initialize the printer
	public static void Inialize() {

		byte SData[] = {(byte)0x1B,(byte)0x40}; 
		try {
			printPos(SData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Pos instruction

	/*********************************************************************
	 * * Function function: set the printer to print Chinese font mode for the 
	 standard size of the Chinese characters (regular font) / / standard pos instruction
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
	 * * Function function: set the printer to print Chinese font mode for the Chinese 
	 double width font (wide font) / / standard pos instruction
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
	 * * Function function: set the printer to print Chinese font mode for the high font (long font) 
	 / / standard pos instruction
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
	 * *Function function: set the printer to print Chinese font mode for the width, 
	 double the font (large font) / / standard pos instruction
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
	 * * Function function: set the printer to print English font mode for the standard 
	 font 12 * 24 // standard pos instruction
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
	 * * Function function: set the printer to print English font mode for the small 
	 font 8 * 16 / / standard pos instruction
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
	 * *Function Function: Thermal character character printing
	 * * Entry parameters: printData typed string
	 * * Return value: ret = true successful
	 * * Note: If you need to print a small ticket, send a good layout can be a string
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
			//	ret = tmpUsbDev.WriteString(printData);//You can also use WriteString (String str)

			if (!ret) {
				getErr(tmpUsbDev);
			}
			tmpUsbDev.Close();
		}
		tmpUsbDev = null ;
		return ret;
	}
	/*********************************************************************
	 * * Function Function: Thermal character character printing
	 * * Entry parameters: printData typed string
	 * * Return value: ret = true successful
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
	 * * Function function: take a number of lines / standard pos instruction
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
	 * * Function function: take a number of lines using enter instead of the pos command another line
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
	 * * Function Function: Graphic print function
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
		// The graphics file into a popular printer can identify the data
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
		// Print graphics
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
	 * * Function: Displays the printer error function
	 ********************************************************************* */
	private static void getErr(UsbPrinter tmpUsbDev) {	
		Error = tmpUsbDev.GetLastPrintErr();		
	}
	
	//Get the wrong string
	public static String getErrorStr() {			 
		return Error;		
	}
	//Print ASCII code 0x20 to 0x7f for use as a demo
	private static byte[]  get20to7f(){
		 
		byte[] SData = new byte[0x7f-0x20];
		for(int i = 0x20, j = 0; i<0x7f; i++,j++){ 
			SData[j] = (byte) i;				
		}		
		return SData;	

	}
	//Print the contents of the GB font section as a demo
	private  static byte[] getHanZiA(){
		byte[] SData = new byte[(0xfe - 0xA1)*2];

		for(int i = 0xA1, j = 0;  i<0xfe ; i++,j++){
			SData[j] = (byte) 0xB0;
			//j++;
			SData[++j] = (byte) i;	
		}		
		return SData;			
	}
	
    //Open the paper cover
	public static boolean UnLock() {
		
		boolean retnVale = false;
		UsbPrinter tmpUsbDev = new UsbPrinter();
		retnVale = tmpUsbDev.UnLock();

		return retnVale;
	}
	
    //Open money box
	public static boolean UnLockOfCashBox() {
		
		boolean retnVale = false;
		UsbPrinter tmpUsbDev = new UsbPrinter();
		retnVale = tmpUsbDev.UnLockOfCashBox();

		return retnVale;
	}
}
