package com.jcl.android.net;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 图片加载工具类
 * 
 * @author Msz
 * 
 */
public class ImageLoaderUtil {

	private static ImageLoaderUtil imageUtil;

	public static ImageLoader imageLoader;
	public static DisplayImageOptions options;
	private static ImageLoaderConfiguration config;

	private ImageLoaderUtil(Context ctx) {
		initImageLoader(ctx);
	};

	public static ImageLoaderUtil getInstance(Context ctx) {
		if (imageUtil == null) {
			imageUtil = new ImageLoaderUtil(ctx);
		}
		return imageUtil;
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	/**
	 * 图片下载初始化
	 * 
	 * @author Msz
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	private void initImageLoader(Context context) {
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showStubImage(R.drawable.img_d)// 加载开始默认的图片
				.showImageForEmptyUri(R.drawable.img_d) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.img_d) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();
		File cacheDir = StorageUtils.getCacheDirectory(context);
		cacheDir = StorageUtils.getCacheDirectory(context,
				FileUtils.getJCLDataPath() + C.SDCARD_PATH_CAN_CLEAR
						+ "/imageCache");
		config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheExtraOptions(240, 400, CompressFormat.PNG, 40,
				// null)
				.diskCacheExtraOptions(240, 400, null)
				.memoryCacheExtraOptions(240, 400).discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				.writeDebugLogs().defaultDisplayImageOptions(options).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	/**
	 * 
	 * @param uri
	 * @param imageView
	 * @param options
	 */
	public void loadImage(String uri, ImageView imageView) {
		imageLoader.displayImage(uri, imageView, options);
	}

	/**
	 * 
	 * @param uri
	 * @param imageView
	 * @param options
	 * @param listener
	 */
	public void loadImage(String uri, ImageView imageView,
			ImageLoadingListener listener) {
		imageLoader.displayImage(uri, imageView, options, listener);
	}

	public void displayImage(String uri, ImageView imageView,
			DisplayImageOptions options) {
		imageLoader.displayImage(uri, imageView, options);
	}
}
