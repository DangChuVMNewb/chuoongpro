package com.dangchuvmnewb;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;
import android.widget.Toast;

public class DownloadUtility {

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
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            // Get the download service and enqueue the download
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);

            Toast.makeText(context, "Download started: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}