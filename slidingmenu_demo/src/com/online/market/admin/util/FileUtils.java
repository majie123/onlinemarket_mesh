package com.online.market.admin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtils {
	
	public static String _PATH=FileUtils.getSDCardRoot()+ "onlinemarket" + File.separator + "image"+ File.separator;

	public static String getSDCardRoot() {
		String SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		return SDCardRoot;
	}

	public static boolean isFileExist(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		File file = new File(path);
		return file.exists();
	}

	public static boolean isfileExist(String dirpath, String fileName) {
		File dir = new File(dirpath);
		if (!dir.exists())
			dir.mkdirs();
		File file = new File(dirpath + fileName);
		return file.exists();
	}

	public static void copyFile(String fromStr, String toStr) {

		File fromFile = new File(fromStr);
		File toFile = new File(toStr);
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists()) {
			toFile.delete();
		}
		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
		} catch (Exception e) {
			Log.e("file", e.getMessage());
		}
	}

	public static void delete(String filepath) {
		File file = new File(filepath);
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i].getAbsolutePath());
			}
			file.delete();
		}
	}

	/**
	 * ����ļ�����չ��
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �����ļ�����չ��
	 */
	public static String getExt(String fileName) {
		String ext = "";
		int pointIndex = fileName.lastIndexOf(".");
		if (pointIndex > -1) {
			ext = fileName.substring(pointIndex + 1).toLowerCase();
		}
		return ext;
	}

	public static void persistFileToSdcard(byte[] buff, String filePath,
			String fileName) {
		File dir = new File(filePath);
		if (!dir.exists())
			dir.mkdirs();
		File target = new File(dir, fileName);
		OutputStream os = null;
		try {
			os = new FileOutputStream(target);
			os.write(buff);
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void renameFile(String oldFileName, String newFileName) {
		File file = new File(oldFileName);
		file.renameTo(new File(newFileName));
	}

	public static void mkdirs(String dir) {
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
	}
}
