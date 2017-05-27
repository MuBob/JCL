package com.jcl.android.activity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.view.MyToast;
/**
 * 上传图片选择类
 * @author msz
 *
 */
public class SelectPicActivity extends BaseActivity implements OnClickListener{  
	  
    private Button btn_take_photo, btn_pick_photo, btn_cancel;  
    private LinearLayout layout;  
    /***
    * 使用照相机拍照获取图片
    */
    private static final int CAMERA_WITH_DATA = 3023;
    /***
    * 使用相册中的图片
    */
    private static final int PHOTO_ALBUM_WITH_DATA = 3021;
    /***
     * 图片剪切
     */
     public static final int PHOTO_REQUEST_CUT = 3;
    /***
    * 从Intent获取图片路径的KEY
    */
    public static final String KEY_PHOTO_PATH = "photo_path";

    private static final String TAG = "SelectPicActivity";


    private Uri photoUri;
    
    protected String pHOTO_DIR;
	protected String imagePath;
	private File imageFile;
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo);
        
        pHOTO_DIR = C.SDCARD_PATH_CAN_CLEAR + "/authentication";
        initView();
        
    }  
    String pic_key;
    
    ImageView img_test;
    /**
    * 初始化加载View
    */
    private void initView() {
    	img_test=(ImageView)findViewById(R.id.img_test);
    	btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);  
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);  
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);  
        layout=(LinearLayout)findViewById(R.id.pop_layout);  
          
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity  
        layout.setOnClickListener(new OnClickListener() {  
              
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",   
                        Toast.LENGTH_SHORT).show();   
            }  
        });  
        //添加按钮监听  
        btn_cancel.setOnClickListener(this);  
        btn_pick_photo.setOnClickListener(this);  
        btn_take_photo.setOnClickListener(this); 
    }
    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity  
    @Override  
    public boolean onTouchEvent(MotionEvent event){  
        finish();  
        return true;  
    }  
    public void onClick(View v) {  
    	switch (v.getId()) {
		case R.id.btn_take_photo:
			if (!FileUtils.existSDcard()) {
    			MyToast.showToast(SelectPicActivity.this, "您的sd卡暂时不可用");
    			return;
    		}
        	takePhoto();
			break;
		case R.id.btn_pick_photo:
			if (!FileUtils.existSDcard()) {
    			MyToast.showToast(SelectPicActivity.this, "您的sd卡暂时不可用");
    			return;
    		}
        	pickPhoto();
			break;
		case R.id.btn_cancel:
			setResult(RESULT_OK);
        	finish(); 
			break;

		default:
			break;
		}
        
    }  
    /**
    * 拍照获取图片
    */
    private void takePhoto() {
	    //执行拍照前，应该先判断SD卡是否存在
    	try {
			String name = DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			imagePath = Environment.getExternalStorageDirectory().getPath()
					+ "/DCIM/Camera/" + name;
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File mPhotoFile = new File(imagePath);
			if (!mPhotoFile.exists()) {
				mPhotoFile.getParentFile().mkdirs();
			}
			ContentValues values = new ContentValues();
			values.put(Media.TITLE, imagePath);
			photoUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			if (!FileUtils.existSDcard()) {
				MyToast.showToast(this, "您的sd卡暂时不可用");
				return;
			}
		}
    }
    /***
    * 从相册中取图片
    */
    private void pickPhoto() {
    	if (!FileUtils.existSDcard()) {
			MyToast.showToast(this, "您的sd卡暂时不可用");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_ALBUM_WITH_DATA);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	/*String path;
    	if (data != null) {      
    	    Uri uri = data.getData();      
    	    if (uri!=null&&!TextUtils.isEmpty(uri.getAuthority())) {      
    	        Cursor cursor = getContentResolver().query(uri,    
    	                new String[] { MediaStore.Images.Media.DATA },null, null, null);      
    	        if (null == cursor) {      
    	            Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();      
    	            return;      
    	        }      
    	        cursor.moveToFirst();      
    	        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));    
    	        cursor.close();      
    	    } else {      
    	        path = uri.getPath();      
    	    }      
    	}else{      
    	    Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();      
    	    return;      
    	}     
		// 给新照的照片文件命名
		switch (requestCode) {
		case PHOTO_ALBUM_WITH_DATA: // 调用Gallery返回的
			if (resultCode == RESULT_OK) {
				handleAlbumPhoto(path);
			}
			break;
		case CAMERA_WITH_DATA:
			if (RESULT_OK == resultCode) {
				handleCameraPhoto(path);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);*/
		
		// 给新照的照片文件命名
		switch (requestCode) {
		case PHOTO_ALBUM_WITH_DATA: // 调用Gallery返回的
			if (resultCode == RESULT_OK) {
				handleAlbumPhoto(data);
			}
			break;
		case CAMERA_WITH_DATA:
			if (RESULT_OK == resultCode) {
				handleCameraPhoto(data);
			}
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void handleAlbumPhoto(Intent data) {
		if (!FileUtils.existSDcard()) {
			MyToast.showToast(this, "您的sd卡暂时不可用");
			return;
		}
		if (data == null)
			return;
		Uri originalUri = data.getData(); // 获得图片的uri
		String[] proj = { MediaStore.Images.Media.DATA };

		// 好像是android多媒体数据库的封装接口，具体的看Android文档 (游标)
		Cursor cursor = managedQuery(originalUri, proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		handleAlbumPhoto(path);
	}

	private void handleAlbumPhoto(final String path) {
		showLD("正在处理中请稍候...");
		new Thread() {
			public void run() {
				try {
					Bitmap cameraBitmap = BitmapFactory.decodeFile(path);
					if (cameraBitmap != null) {
						// // 新路径
						imagePath = pHOTO_DIR
								+ "/"
								+ DateFormat.format("yyyyMMdd_hhmmss",
										Calendar.getInstance(Locale.CHINA))
								+ ".jpg";
						imageFile = new File(imagePath);
						if (!imageFile.exists()) {
							imageFile.getParentFile().mkdirs();
						}
						try {
							// revitionImageSize(imagePath);
							FileUtils.saveBitmap(cameraBitmap, imageFile,
									SelectPicActivity.this);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (cameraBitmap != null) {
							//根据路径压缩
//							Bitmap smallBitmap = FileUtils.getSmallBitmap(
//									SelectPicActivity.this, imagePath);
							// long size2=imageFile.length();
							Message msg = new Message();
							msg.what = MSG_WHAT_SHOWIMG;
							msg.obj = imagePath;
							handler.sendMessage(msg);
						}
					}
				} catch (Exception e) {

				}
			}
		}.start();
	}

	private void handleCameraPhoto(final Intent data) {
		showLD("正在处理中请稍候...");
		new Thread() {
			public void run() {
				/**
				 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
				 * 
				 */
				try {
					if (data == null || data.getData() == null) {
						// 需要查询的列名
						String[] PROJECTION = new String[] { Media.TITLE };
						Cursor c = getContentResolver().query(photoUri,
								PROJECTION, null, null, null);
						if (c.moveToFirst()) {
							for (int i = 0; i < c.getCount(); i++) {
								c.moveToPosition(i);
								imagePath = c.getString(0);
							}
						}
					}
					handleCameraPhoto(imagePath);
				} catch (Exception e) {

				}
			}
		}.start();
	}

	private void handleCameraPhoto(String path) {
		Bitmap cameraBitmap = FileUtils.getZoomBitmap(path);// BitmapFactory.decodeFile(path);
		int degree = FileUtils.readPictureDegree(path);
		if (cameraBitmap != null) {
			/**
			 * 把图片旋转为正的方向
			 */
			if (degree != 0) {
				cameraBitmap = FileUtils.rotaingImageView(degree, cameraBitmap);
				try {
					FileUtils.forceDelete(new File(path));
					FileUtils.deleteToAlbum(path, this);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (cameraBitmap != null) {
					imageFile = new File(path);
					if (!imageFile.exists()) {
						imageFile.getParentFile().mkdirs();
					}
					try {
						FileUtils.saveBitmap2(this,
								imageFile.getName(),
								imageFile.getAbsolutePath(), cameraBitmap,
								imageFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// // 新路径
			imagePath = pHOTO_DIR
					+ "/"
					+ DateFormat.format("yyyyMMdd_hhmmss",
							Calendar.getInstance(Locale.CHINA)) + ".jpg";
			imageFile = new File(imagePath);
			if (!imageFile.exists()) {
				imageFile.getParentFile().mkdirs();
			}
			try {
				// revitionImageSize(imagePath);
				FileUtils.saveBitmap(cameraBitmap, imageFile,
						this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (cameraBitmap != null) {
//				Bitmap smallBitmap = FileUtils.getSmallBitmap(
//						this, imagePath);
				// long size2=imageFile.length();
				Message msg = new Message();
				msg.what = MSG_WHAT_SHOWIMG;
				msg.obj = imageFile;
				handler.sendMessage(msg);
			}
		}
	}


	private static final int MSG_WHAT_SHOWIMG = 10;
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			cancelLD();
			switch (msg.what) {
			case MSG_WHAT_SHOWIMG:
				if (!TextUtils.isEmpty(imagePath)) {

					Intent intent = getIntent();
					intent.putExtra("imagePath", imagePath);
				    setResult(C.SELECT_PIC_RESULT, intent);
					finish();
				}
				break;
			}
		}

	};
	
	
    
      
} 
