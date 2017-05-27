package com.jcl.android.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.jcl.android.C;
import com.jcl.android.application.JCLApplication;

public class FileUtils {
	
	@SuppressWarnings("deprecation")
	public static String getAbsoluteImagePath(Uri uri,Activity activity)
	   {
	       // can post image
	       String [] proj={MediaStore.Images.Media.DATA};
	       Cursor cursor = activity.managedQuery( uri,
	                       proj,                 // Which columns to return
	                       null,       // WHERE clause; which rows to return (all rows)
	                       null,       // WHERE clause selection arguments (none)
	                       null);                 // Order-by clause (ascending by name)
	      
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       cursor.moveToFirst();
	        
	       return cursor.getString(column_index);
	   }
	
	/**
	 * 获取文件大小
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSizes(File f)throws Exception {
		long s = 0;

		if (f.exists()) {
			FileInputStream fis = null;

			fis = new FileInputStream(f);
			s = fis.available();
			fis.close();
		} else {
			f.createNewFile();
			System.out.println("文件夹不存在");
		}
		return s;
	}
	
	/**
	 * 是否存在内存卡判断
	 * 
	 * @return
	 */
	public static boolean existSDcard() {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			return true;
		} else
			return false;
	}
	
	/**
	 * 获取缓存文件保存路径
	 * @return
	 */
	public static  String getJCLCachePath(){
	      String path = getJCLDataPath()+C.JCL_CACHE_PATH+"/";
	      File file=new File(path);
			if(!file.exists())
				file.mkdirs();
			return path;
	}
	
	public static  String getJCLDataPath(){
	       Context context=JCLApplication.getContext();
			File file;
			if(existSDcard()){
				file=context.getExternalCacheDir();
			}else{
				file=context.getCacheDir();
			}
			if(file==null)
				return "";
			return file.getPath();
	}
	
	public static String getVollyCachePath(){
		String path=getJCLDataPath()+C.JCL_VOLLY_CACHE+"/";
		File file=new File(path);
		if(!file.exists())
			file.mkdirs();
		return path;
	}
	
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
		    return;
		}

		if (!isSymlink(directory)) {
		    cleanDirectory(directory);
		}

		if (!directory.delete()) {
		    String message = "Unable to delete directory " + directory + ".";
		    throw new IOException(message);
		}
	    }

	    /**
	     * Determines whether the specified file is a Symbolic Link rather than an
	     * actual file.
	     * <p>
	     * Will not return true if there is a Symbolic Link anywhere in the path,
	     * only if the specific file is.
	     * 
	     * @param file
	     *            the file to check
	     * @return true if the file is a Symbolic Link
	     * @throws IOException
	     *             if an IO error occurs while checking the file
	     * @since Commons IO 2.0
	     */
	    public static boolean isSymlink(File file) throws IOException {
		if (file == null) {
		    throw new NullPointerException("File must not be null");
		}
		File fileInCanonicalDir = null;
		if (file.getParent() == null) {
		    fileInCanonicalDir = file;
		} else {
		    File canonicalDir = file.getParentFile().getCanonicalFile();
		    fileInCanonicalDir = new File(canonicalDir, file.getName());
		}

		if (fileInCanonicalDir.getCanonicalFile().equals(
			fileInCanonicalDir.getAbsoluteFile())) {
		    return false;
		} else {
		    return true;
		}
	    }

	    /**
	     * Cleans a directory without deleting it.
	     * 
	     * @param directory
	     *            directory to clean
	     * @throws IOException
	     *             in case cleaning is unsuccessful
	     */
	    public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
		    String message = directory + " does not exist";
		    throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
		    String message = directory + " is not a directory";
		    throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
		    throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (File file : files) {
		    try {
			forceDelete(file);
		    } catch (IOException ioe) {
			exception = ioe;
		    }
		}

		if (null != exception) {
		    throw exception;
		}
	    }
	
	 public static void forceDelete(File file) throws IOException {
			if (file.isDirectory()) {
			    deleteDirectory(file);
			} else {
			    boolean filePresent = file.exists();
			    if (!file.delete()) {
				if (!filePresent) {
				    throw new FileNotFoundException("File does not exist: "
					    + file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			    }
			}
		    }
	

	@SuppressLint("NewApi")
	public static void saveBitmap(Bitmap bitmap, File file, Context context)
			throws IOException {
		int rate = 100;
		if (file.length() > 1024 * 4) {
			rate = 65;
		} else if (file.length() > 1024 * 3) {
			rate = 75;
		} else if (file.length() > 1024 * 2) {
			rate = 85;
		} else if (file.length() > 1024) {
			rate = 95;
		}
		BufferedOutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, rate, os);
		} finally {
			insertToAlbum(file.getAbsolutePath(), context);
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}

	}

	@SuppressLint("NewApi")
	public static void saveBitmap2(Context context, String fileName,
			String filePath, Bitmap bitmap, File file) throws IOException {
		int rate = 100;
		BufferedOutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, rate, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
		insertToAlbum(filePath, context);

	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 缩放图片
	 * 
	 * @param imagePath
	 * @return 一个缩放好的bitmap
	 */
	public static Bitmap getZoomBitmap(String imagePath) {
		// 解决图片内存溢出问题
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 这样就只返回图片参数
		// 获取这个图片的宽和高
		Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;// 上面操作完后,要设回来,不然下面同样获取不到实际图片
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 500);
		// 上面算完后一下如果比300大,那就be就大于1,那么就压缩be,如果比300小,那图片肯定很小了,直接按原图比例显示就行
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;// be=2.表示压缩为原来的1/2,以此类推
		// 重新读入图片，注意在这之前要把options.inJustDecodeBounds 设为 false!
		bm = BitmapFactory.decodeFile(imagePath, options);

		return bm;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(Context context, String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options,
				MyApplicationUtils.getDeviceWidth(context) / 5,
				MyApplicationUtils.getDeviceWidth(context) / 5);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	public static void insertToAlbum(String filePath, Context context) {
		if (filePath == null || filePath.length() == 0) {
			return;
		}
		final String fileName = filePath
				.substring(filePath.lastIndexOf('/') + 1);
		// 系统时间
		final long dateTaken = System.currentTimeMillis();
		ContentValues values = new ContentValues(5);
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
		values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.Images.Media.DATA, filePath);
		context.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	public static void deleteToAlbum(String filePath, Context context) {
		if (filePath == null || filePath.length() == 0) {
			return;
		}
		try {
			final String fileName = filePath.substring(filePath
					.lastIndexOf('/') + 1);
			// 系统时间
			final long dateTaken = System.currentTimeMillis();
			ContentValues values = new ContentValues(5);
			values.put(MediaStore.Images.Media.TITLE, fileName);
			values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
			values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			values.put(MediaStore.Images.Media.DATA, filePath);
			context.getContentResolver().delete(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					MediaStore.Images.Media.DATA + "=?",
					new String[] { filePath });
		} catch (Exception e) {

		}
	}
	
	/** 
	 * 通过Base32将Bitmap转换成Base64字符串 
	 * @param bit 
	 * @return 
	 */  
	@SuppressLint("NewApi")
	public static String Bitmap2StrByBase64(Bitmap bit){  
	   ByteArrayOutputStream bos=new ByteArrayOutputStream();  
	   bit.compress(CompressFormat.JPEG, 40, bos);//参数100表示不压缩  
	   byte[] bytes=bos.toByteArray();  
	   return Base64.encodeToString(bytes, Base64.DEFAULT);  
	}
	
	 /**
     * 
     * @param base64Data
     * @param imgName
     * @param imgFormat 图片格式
     */
    public static void base64ToBitmap(String base64Data,String imgName,String imgFormat) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
 
        File myCaptureFile = new File("/sdcard/", imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (isTu) {
            // fos.notifyAll();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static File updateDir = null;
	public static File updateFile = null;

	/***
	 * 创建文件
	 */
	public static void createFile(String name) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + JCLApplication.downloadDir);
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
