package com.example.yendry.scaninternalstoage;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.example.yendry.scaninternalstoage.model.Element;
import com.example.yendry.scaninternalstoage.model.ElementDirectory;
import com.example.yendry.scaninternalstoage.model.ElementFile;
import com.example.yendry.scaninternalstoage.model.ElementType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ScanManager {
    private static final String TAG = ScanManager.class.getSimpleName();
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private ArrayList<HashMap<ElementType.type, Element>> elementList = new ArrayList<>();

    // Constructor
    public ScanManager() {

    }


    public ArrayList<HashMap<ElementType.type, Element>> getList() {
        File home = new File(MEDIA_PATH);
        File[] listFiles = home.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                Log.d("lll", file.getAbsolutePath());
                if (file.isDirectory()) {
                    addFileToList(file, ElementType.type.DIR);
                    scanDirectory(file);
                } else {
                    addFileToList(file, ElementType.type.FILE);
                }
            }
        }

        // return list array
        return elementList;
    }

    private void scanDirectory(File directory) {

        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        addFileToList(file, ElementType.type.DIR);
                        scanDirectory(file);
                    } else {
                        addFileToList(file, ElementType.type.FILE);
                    }

                }
            }
        }
    }

    private void addFileToList(File file, ElementType.type type) {


        HashMap<ElementType.type, Element> fileMap = new HashMap<>();
        if (type == ElementType.type.DIR) {
            ElementDirectory elementDirectory = new ElementDirectory();
            elementDirectory.parent = file.getParentFile().getName();
            elementDirectory.parentPath = file.getParentFile().getAbsolutePath();
            elementDirectory.name = file.getName();
            elementDirectory.type = type;
            fileMap.put(type, elementDirectory);
        } else if (type == ElementType.type.FILE) {
            ElementFile elementFile = new ElementFile();
            elementFile.parent = file.getParentFile().getName();
            elementFile.parentPath = file.getParentFile().getAbsolutePath();
            elementFile.name = file.getName();
            elementFile.type = type;

            Uri selectedUri = Uri.fromFile(file);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
            elementFile.setExt(mimeType);

            elementFile.setSize(file.length());
            fileMap.put(type, elementFile);
        }


        elementList.add(fileMap);

    }

    private List<String> getChildren(File directory) {
        List<String> list = new ArrayList<>();
        File[] listFiles = directory.listFiles();

        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {

                list.add(file.getName());

            }
        }
        return list;

    }

    //most common extension
    public <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        assert max != null;
        return max.getKey();
    }

    public List<ElementFile> getTenBiggest(List<Element> list) {
        Collections.sort(list, (o1, o2) -> ((Long)((ElementFile)o2).getSize()).compareTo(((ElementFile)o1).getSize()));
        List<ElementFile> result = new ArrayList<>();
        int count = 0;
        for (Element item :list) {
            if (count==10)
                break;
            count++;
            result.add((ElementFile) item);
        }
        return result;
    }

    public Map<String, Integer> get5MostCommon(List<String> list) {
        Map<String, Integer> map = new HashMap<>();

        for (String t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }
        return sortByValue(map);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (e1, e2) -> (e2.getValue()).compareTo(e1.getValue()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
