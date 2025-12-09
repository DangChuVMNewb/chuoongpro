package com.dangchuvmnewb;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.io.File;

public class DownloadUtility {

    private static final String TAG = "DownloadUtility";

    /**
     * Downloads a file from the given URL
     * @param context The application context
     * @param url The URL of the file to download
     * @param fileName The name of the file to save
     * @param description A description for the download notification
     */
    public static void downloadFile(Context context, String url, String fileName, String description) {
        try {
            // Check if URL is valid
            if (!URLUtil.isValidUrl(url)) {
                Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
                return;
            }

            // If fileName is not provided, extract it from the URL
            if (fileName == null || fileName.isEmpty()) {
                fileName = URLUtil.guessFileName(url, null, null);
            }

            // Create a download request
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle(fileName);
            request.setDescription(description);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Check if DownloadManager is available (can return null on some devices)
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager == null) {
                Toast.makeText(context, "Download manager not available", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Download manager service not available");
                return;
            }

            // Handle scoped storage based on target SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // On Android 10+ (API 29+), use MediaStore to save to public Downloads directory
                // This properly handles scoped storage while placing files where users expect them
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(fileName));
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    request.setDestinationUri(uri);
                } else {
                    // Fallback if MediaStore insertion fails
                    request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
                }
            } else {
                // For older versions, use the traditional public directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            }

            downloadManager.enqueue(request);

            Toast.makeText(context, "Download started: " + fileName, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Download started: " + fileName + " from URL: " + url);
        } catch (Exception e) {
            Log.e(TAG, "Download failed", e);
            Toast.makeText(context, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method to guess MIME type from file extension
     */
    private static String getMimeType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "application/octet-stream"; // generic binary file type
        }

        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else if (fileName.endsWith(".mp3")) {
            return "audio/mpeg";
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".apk")) {
            return "application/vnd.android.package-archive";
        } else {
            // Fallback to URLConnection for MIME type detection
            try {
                return java.net.URLConnection.guessContentTypeFromName(fileName);
            } catch (Exception e) {
                Log.w(TAG, "Could not guess MIME type for file: " + fileName, e);
                return "application/octet-stream";
            }
        }
    }
}