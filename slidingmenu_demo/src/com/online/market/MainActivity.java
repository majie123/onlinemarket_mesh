package com.online.market;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.online.market.R;
import com.online.market.fragment.CommodityFragment;
import com.online.market.fragment.LeftFragment;
import com.online.market.popop.CategoryPopup;
import com.online.market.utils.ProgressUtil;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	
	/**
	 * SDK初始化建议放在启动页
	 */
	public static String APPID = "bb9c8700c4d1821c09bfebaf1ba006b1";

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private TextView tvRight;
	private View headView;
	
	private LeftFragment leftFragment;
	private CategoryPopup pop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bmob.initialize(getApplicationContext(),APPID);

		headView=findViewById(R.id.head);
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		
		tvRight=(TextView) findViewById(R.id.tv_right);
		tvRight.setOnClickListener(this);
		tvRight.setText("分类");
		tvRight.setVisibility(View.GONE);
		
		initSlidingMenu(savedInstanceState);
		
		updateVersion();
	}
	
	private void updateVersion(){
//		BmobUpdateAgent.initAppVersion(this);
		BmobUpdateAgent.update(this);
		BmobUpdateAgent.setUpdateOnlyWifi(false);
		 BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

			 @Override
			 public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			     // TODO Auto-generated method stub
			     //根据updateStatus来判断更新是否成功
//				 Log.d("majie","updateStatus "+ updateStatus);
			 }
			});
	}

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new CommodityFragment();
			switchContent(mContent, "超市");
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		leftFragment=new LeftFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, leftFragment).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	/**
	 * 切换Fragment
	 * 
	 * @param fragment
	 */
	public void switchContent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton:
			toggle();
			break;
//		case R.id.tv_right:
//			if(pop==null){
//				pop=new CategoryPopup(this);
//				pop.setOnClickListener(new CategoryPopup.OnClickListener() {
//					
//					@Override
//					public void onClick(String str) {
//						CommodityFragment cf=(CommodityFragment) mContent;
//						cf.reinit();
//						ProgressUtil.showProgress(MainActivity.this, "");
//						cf.queryCommoditys(CommodityFragment.FINISH_REFRESHING,"category", str);
//					}
//				});
//			}
//			
//			pop.showAsDropDown(headView);
//			break;
		}
	}
}
