package com.example.ceilingfloorcalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.ConsoleMessage;
import android.util.Log;
import android.widget.Toast;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import android.util.Base64;

public class MainActivity extends Activity {
    private static final String TAG = "ArmstrongCeiling";
    private static final int CREATE_CSV_FILE = 7001;
    private String pendingCsv = "";
    private WebView web;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onConsoleMessage(ConsoleMessage message) {
                Log.e(TAG, message.message() + " -- line " + message.lineNumber());
                return true;
            }
        });
        web.setBackgroundColor(0xFFF4F6F2);
        web.addJavascriptInterface(new ExportBridge(), "AndroidExport");
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    private class ExportBridge {
        @JavascriptInterface public void printPdf() {
            runOnUiThread(() -> {
                try {
                    PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
                    if (printManager == null) throw new Exception("Android print service is unavailable");
                    web.evaluateJavascript("preparePrintReport();", value -> {
                        try {
                            String jobName = "Ceiling_Floor_Calculator_Report";
                            PrintDocumentAdapter adapter = web.createPrintDocumentAdapter(jobName);
                            PrintAttributes attributes = new PrintAttributes.Builder()
                                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                                    .setResolution(new android.print.PrintAttributes.Resolution("pdf", "PDF", 300, 300))
                                    .setMinMargins(new PrintAttributes.Margins(250, 250, 250, 250))
                                    .build();
                            printManager.print(jobName, adapter, attributes);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Could not create PDF report: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Could not open PDF printing: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface public void saveCsv(String base64Csv) {
            runOnUiThread(() -> {
                try {
                    byte[] decoded = Base64.decode(base64Csv, Base64.DEFAULT);
                    pendingCsv = new String(decoded, StandardCharsets.UTF_8);
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/csv");
                    intent.putExtra(Intent.EXTRA_TITLE, "Ceiling_Floor_Estimate.csv");
                    startActivityForResult(intent, CREATE_CSV_FILE);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Could not prepare report: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_CSV_FILE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                try (OutputStream output = getContentResolver().openOutputStream(uri)) {
                    if (output == null) throw new Exception("Unable to open selected location");
                    output.write(pendingCsv.getBytes(StandardCharsets.UTF_8));
                    output.flush();
                    Toast.makeText(this, "Report exported successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Export cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
}
