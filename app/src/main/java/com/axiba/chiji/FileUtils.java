package com.axiba.chiji;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by 李宏阳 on 2017/12/3.
 */

public class FileUtils {
    public static File getAvalableImagePath(Context context) {
        File dir = null;
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        } else {

        }
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String getRealPathByUri(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                String s = getDataColumn(context, contentUri, selection, selectionArgs);
                return s;
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Toast.makeText(context, "请允许应用程序使用存储权限", Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static class DownloadResult {
        public boolean success;
        public String fileName;
        public String realpath;
        public String noticeStr;
    }

    //Glide保存图片
    public static void savePicture(final Context context, final String fileName, String url) {
//        new AsyncTask<String, String, DownloadResult>() {
//
//            @Override
//            protected DownloadResult doInBackground(String... strings) {
//                String fileName = strings[0];
//                String urlString = strings[1];
//                DownloadResult downloadResult = new DownloadResult();
//                downloadResult.fileName = fileName;
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    try {
//                        String filePath = Environment.getExternalStorageDirectory()
//                                + File.separator + Environment.DIRECTORY_DCIM
//                                + File.separator + "Camera" + File.separator;
//                        File dir1 = new File(filePath);
//                        if (!dir1.exists()) {
//                            dir1.mkdirs();
//                        }
//                        String realPath = new File(filePath, fileName + ".jpg").getPath();
//                        URL url = new URL(urlString);
//                        DataInputStream dataInputStream = new DataInputStream(url.openStream());
//
//                        FileOutputStream fileOutputStream = new FileOutputStream(new File(realPath));
//                        ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//                        byte[] buffer = new byte[1024];
//                        int length;
//
//                        while ((length = dataInputStream.read(buffer)) > 0) {
//                            output.write(buffer, 0, length);
//                        }
//                        fileOutputStream.write(output.toByteArray());
//                        dataInputStream.close();
//                        fileOutputStream.close();
//                        downloadResult.success = true;
//                        downloadResult.realpath = realPath;
//                        downloadResult.noticeStr = "图片已成功保存到相册" + realPath;
//                    } catch (Exception e) {
//                        downloadResult.noticeStr = "保存失败！";
//                    }
//                } else {
//                    downloadResult.noticeStr = "SD卡不存在或者不可读写";
//                }
//                return downloadResult;
//            }
//
//            @Override
//            protected void onPostExecute(DownloadResult retsult) {
//                Toast.makeText(context, retsult.noticeStr, Toast.LENGTH_SHORT).show();
//                if (retsult.success) {
//                    try {
//                        MediaScannerConnection.scanFile(context,new String[]{retsult.realpath},null,null);
//                        MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                                retsult.realpath, retsult.fileName, null);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                            Uri.fromFile(new File(retsult.realpath))));
//                }
//            }
//        }.execute(fileName, url);

        Glide.with(context).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    savaFileToSD(context, fileName, bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //往SD卡写入文件的方法
    public static void savaFileToSD(Context context, String filename, byte[] bytes) {
//如果手机已插入sd卡,且app具有读写sd卡的权限

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String filePath = Environment.getExternalStorageDirectory()
                        + File.separator + Environment.DIRECTORY_DCIM
                        + File.separator + "Camera" + File.separator;
                File dir1 = new File(filePath);
                if (!dir1.exists()) {
                    dir1.mkdirs();
                }
                String filename1 = filePath + "/" + filename + ".jpg";
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(filename1);
                output.write(bytes);
                //将bytes写入到输出流中
                output.close();
                //关闭输出流
                Toast.makeText(context, "图片已成功保存到相册" + filePath, Toast.LENGTH_SHORT).show();
// 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        filename1, filename, null);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(filename1))));
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
            } catch (IOException e) {
//                e.printStackTrace();
            }
// 最后通知图库更新
        } else Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

}
