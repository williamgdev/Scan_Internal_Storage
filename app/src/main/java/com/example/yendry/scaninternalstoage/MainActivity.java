package com.example.yendry.scaninternalstoage;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.yendry.scaninternalstoage.model.Element;
import com.example.yendry.scaninternalstoage.model.ElementFile;
import com.example.yendry.scaninternalstoage.model.ElementType;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lll";
    ScanManager scanManager;
    TextView dirCount, fileCount, mostCommonExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dirCount = findViewById(R.id.dir_count_id);
        fileCount = findViewById(R.id.file_count_id);
        mostCommonExt = findViewById(R.id.common_ext_id);
        scanManager = new ScanManager();

        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance

        // Must be done during an initialization phase like onCreate
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        doMagic();
                    } else {
                        // Oups permission denied
                    }
                });


    }



    private void doMagic() {

        ArrayList<HashMap<ElementType.type, Element>> elemList = scanManager.getList();
        List<Element> fileList = new ArrayList<>();
        List<Element> dirList = new ArrayList<>();

        for (HashMap<ElementType.type, Element> item : elemList) {
            for (ElementType.type key : item.keySet()) {
                if (ElementType.type.FILE == key) {
                    fileList.add(item.get(key));
                }
            }
        }
        for (HashMap<ElementType.type, Element> item : elemList) {
            for (ElementType.type key : item.keySet()) {
                if (ElementType.type.DIR == key) {
                    dirList.add(item.get(key));
                }
            }
        }

        //5 most common ext
        showMostCommonExt(fileList);
        //show 10 biggest files
        show10Biggest(fileList);
    }

    private void show10Biggest(List<Element> fileList) {
        dirCount.setText("10 biggest files");
        List<ElementFile> tenBiggest = scanManager.getTenBiggest(fileList);
        storeInDatabase(tenBiggest);
        int num = 1;
        for (ElementFile item : tenBiggest) {
            fileCount.append(num++ + "- " + item.name + " - " + item.getSize() / 1024 + " KB" + "\n");
        }
    }

    private void storeInDatabase(List<ElementFile> tenBiggest) {
        //convert in map and store in realm
    }


    private void showMostCommonExt(List<Element> list) {
        mostCommonExt.setText("");
        List<String> extList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (((ElementFile) list.get(i)).getExt() != null) {
                extList.add(((ElementFile) list.get(i)).getExt());
            }
        }
        //update UI
        Map<String, Integer> mostCommon = scanManager.get5MostCommon(extList);
        int count = 1;
        for (Map.Entry<String, Integer> item : mostCommon.entrySet()) {
            Log.d(TAG, "showMostCommonExt: " + item.getValue() + " - " + item.getKey());
            mostCommonExt.append(count + "- " + item.getKey() + " - " + item.getValue() + " times" + "\n");
            if (count == 5)
                break;
            count++;
        }

    }



}
