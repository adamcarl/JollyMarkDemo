package com.example.sydney.jollymarkdemo;

/** 热敏打印机机  演示程序
 * @author YLiang
 * @date 2014 09 17
 * @amend 2015 04 10
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//驱动 API使用方法：加载jar
//使用eclipse开发平台
//加载jar流程：菜单Project->Properties->Java Build Path->Libraries->Add JAEs->选择libs目录下的文件PosPrinter.jar
//并且在Order and Export界面选中使用。
//在代码中建立 热敏驱动控制类的实例 

public class RMPrintActivity extends Activity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rmmain);
		mContext = RMPrintActivity.this;
		// 注册按钮监听
		this.findViewById(R.id.btn_backPaper).setOnClickListener(
				new ClickListener());
		this.findViewById(R.id.btn_feedButton).setOnClickListener(
				new ClickListener());
		this.findViewById(R.id.btn_Kill)
				.setOnClickListener(new ClickListener());
		this.findViewById(R.id.btn_setting_BID).setOnClickListener(
				new ClickListener());
		this.findViewById(R.id.btn_Elock).setOnClickListener(
				new ClickListener());
		this.findViewById(R.id.btn_Printtext).setOnClickListener(
				new ClickListener());
		this.findViewById(R.id.btn_open_cashbox).setOnClickListener(
				new ClickListener());

		setTitle();

	}


	// 按钮监听
	public class ClickListener implements View.OnClickListener {

		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.btn_Kill: { //退出 按钮
				if (Utils.isFastDoubleClick()) {
					return;
				}
				ExitAlertDialog(RMPrintActivity.this, "提示", "是否退出程序?");
				break;
			}

			case R.id.btn_feedButton: { // 进纸
				boolean ret = RMPrinter.feedOfLine(1);
				if (!ret) {
					viewPrintErr();
				}
				break;
			}

			case R.id.btn_Printtext: { // 自定义文本内容打印示�?
				if (Utils.isFastDoubleClick()) {
					return;
				}
				// 自定义本内容打印
				boolean ret = RMPrinter.printTextTest();
				if (!ret) {
					viewPrintErr();
					break;
				}
				ret = RMPrinter.feedOfLine(6); // 进纸若干6
				if (!ret) {
					viewPrintErr();
				}
				break;
			}

			case R.id.btn_setting_BID: { // 图形打印示例
				if (Utils.isFastDoubleClick()) {
					return;
				}
				// 图形打印
				boolean ret = RMPrinter.printPicTest(mContext, "code.png");
				if (!ret) {
					viewPrintErr();
					break;
				}
				ret = RMPrinter.feedOfLine(6); // 进纸若干行）
				if (!ret) {
					viewPrintErr();
				}
				break;
			}

			case R.id.btn_Elock: { // 开电子锁
				if (Utils.isFastDoubleClick()) {
					return;
				}
				boolean retnVale;
				retnVale = RMPrinter.UnLock();
				if (!retnVale) {
					Toast.makeText(RMPrintActivity.this, "开锁异常",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}

			case R.id.btn_open_cashbox: { // 开钱箱
				if (Utils.isFastDoubleClick()) {
					return;
				}
				boolean retnVale;
				retnVale = RMPrinter.UnLockOfCashBox();
				if (!retnVale) {
					Toast.makeText(RMPrintActivity.this, "开钱箱异常",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			}// end switch
		}

	}

	private void viewPrintErr() {

		Toast.makeText(mContext, RMPrinter.getErrorStr(), Toast.LENGTH_SHORT)
				.show();

	}

	/*********************************************************************
	 * * 功能: 避免连续或同时触发点击事�?
	 ********************************************************************* */
	public static class Utils {
		private static long lastClickTime;

		public static boolean isFastDoubleClick() {
			long time = System.currentTimeMillis();
			long timeD = time - lastClickTime;
			if (0 < timeD && timeD < 800) {
				return true;
			}
			lastClickTime = time;
			return false;
		}
	}


	/*********************************************************************
	 * * 功能: 设置程序标题 参数:无 返回: 无.
	 ********************************************************************* */
	private void setTitle() {
		TextView title_tv = (TextView) this.findViewById(R.id.title_tv);

		String version = getVersion();
		String old = (String) title_tv.getText();
		String title = old + "V" + version;
		title_tv.setText(title);

	}

	/*********************************************************************
	 * * 功能: 获取版本号 参数:无 返回: 当前应用的版本号.
	 ********************************************************************* */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version; // return this.getString(R.string.version_name)
							// +version;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/*********************************************************************
	 * * 功能: 重写返回键
	 ********************************************************************* */
	@Override
	public void onBackPressed() {
		if (Utils.isFastDoubleClick()) {
			return;
		}
		ExitAlertDialog(RMPrintActivity.this, "提示", "是否退出程序?");
	}

	private void ExitAlertDialog(Context context, String title, String message) {

		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(message)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface builder, int which) {
						// TODO Auto-generated method stub
						builder.cancel();
						// System.exit(0);
						finish();

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface builder, int which) {
						builder.cancel();
					}
				}).create();
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}
