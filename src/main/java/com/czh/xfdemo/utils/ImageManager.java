package com.czh.xfdemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.czh.xfdemo.base.App;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************************************************
 * @desc 管理本地图像
 * @auther LiJianfei
 * @time 2016/7/27 15:29
 ************************************/
public class ImageManager {
    private static ImageManager instance;//单例
    private static Context context = App.getInstance();//application
    //拍照时指定保存图片的路径
    private String CameraImgPath;

    /*以下 这两个函数基本不用*/
    public List<ImageModel> getImageModelList() {
        return imageModelList;
    }

    public void setImageModelList(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }

    /* 以上这两个函数基本不用*/
    private List<ImageModel> imageModelList;// 已经选择的图片集合

    /**
     * 添加到集合中
     *
     * @param m
     */
    public void addModel(ImageModel m) {
        if (!imageModelList.contains(m)) {
            imageModelList.add(m);
        }
    }

    /**
     * 从集合中移除
     *
     * @param m
     */
    public void delModel(ImageModel m) {
        if (imageModelList.contains(m)) {
            imageModelList.remove(m);
        }
    }

    private ArrayList<Object> folderNames;// 用于扫描返回

    public String getCameraImgPath() {
        return CameraImgPath;
    }

    public String setCameraImgPath() {
//        String foloder = MyApplication.getInstance().getCachePath() + "/PostPicture/";

        String foloder = "/storage/emulated/0/DCIM/Camera/";
        File savedir = new File(foloder);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        // 照片命名
        String picName = "pick" + timeStamp + ".jpg";
        //  裁剪头像的绝对路径
        CameraImgPath = foloder + picName;
        return CameraImgPath;
    }

    //大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    static final List<ImageModel> paths = new ArrayList<>();

    static final Map<String, List<ImageModel>> folders = new HashMap<>();

    /**
     * 构造器
     *
     * @param context
     */
    public ImageManager(Context context) {
        imageModelList = new ArrayList<>();
        this.context = context;
    }

    public Map<String, List<ImageModel>> getFolderMap() {
        return folders;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager(context);
        }
        return instance;
    }


    public static boolean isInited() {
        return paths.size() > 0;
    }


    private boolean resultOk;

    public boolean isResultOk() {
        return resultOk;
    }

    public void setResultOk(boolean ok) {
        resultOk = ok;
    }

    private static boolean isRunning = false;

    /**
     * 重要的初始化
     */
    public static synchronized void initImage() {

        if (isRunning)
            return;
        isRunning = true;
        if (isInited())
            return;
        //获取大图的游标
//        MainActivity.getMainActivity().permionDeal();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
                STORE_IMAGES,   // 字段
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);//大图ID
            String path = cursor.getString(1);//大图路径
            File file = new File(path);
            //判断大图是否存在
            if (file.exists()) {
                //小图URI
                String thumbUri = getThumbnail(id, path);
                //获取大图URI
                String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(id)).build().toString();
                if (StringUtils.isEmpty(uri))
                    continue;
                if (StringUtils.isEmpty(thumbUri))
                    thumbUri = uri;
                //获取目录名
                String folder = file.getParentFile().getName();

                ImageModel ImageModel = new ImageModel();
                ImageModel.setOriginalUri(uri);
                ImageModel.setThumbnailUri(thumbUri);

                int degree = cursor.getInt(2);
                if (degree != 0) {
                    degree = degree + 180;
                }
                ImageModel.setOrientation(360 - degree);

                paths.add(ImageModel);
                //判断文件夹是否已经存在
                if (folders.containsKey(folder)) {
                    folders.get(folder).add(ImageModel);
                } else {
                    List<ImageModel> files = new ArrayList<>();
                    files.add(ImageModel);
                    folders.put(folder, files);
                }
            }
        }
        folders.put("所有图片", paths);
        cursor.close();
        isRunning = false;
    }

    public String getPaths(Context context, String uriStr) {
        Uri uri = Uri.parse(uriStr);
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        //        for (int i = 0; i < 2; i++) {

//            System.out.println(i+"-----------------"+cursor.getString(i));
//        }
        return cursor.getString(1);
    }

    private static String getThumbnail(int id, String path) {
        //获取大图的缩略图
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                    appendPath(Integer.toString(thumId)).build().toString();
            cursor.close();
            return uri;
        }
        cursor.close();
        return null;
    }

    public List<ImageModel> getFolder(String folder) {
        return folders.get(folder);
    }

    /**
     * 此方法很重要，退出上传界面时，询问是否保存已经选择的照片  如果不保存  必须·调用clear 清除刚才选择的图片
     */
    public void clear() {
        imageModelList.clear();
    }

    /**
     * 递归删除
     *
     * @param file
     */
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFile(file1);
                }
            }
        } else {
        }
    }

    /*************************************************
     * @desc 本地图片
     * @auther LiJianfei
     * @time 2016/7/27 15:31
     ************************************/
    public static class ImageModel {
        private String originalUri;//原图URI

        public String getOriginalUri() {
            return originalUri;
        }

        public void setOriginalUri(String originalUri) {
            this.originalUri = originalUri;
        }

        public String getThumbnailUri() {
            return thumbnailUri;
        }

        public void setThumbnailUri(String thumbnailUri) {
            this.thumbnailUri = thumbnailUri;
        }

        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        private String thumbnailUri;//缩略图URI
        private int orientation;//图片旋转角度
        private boolean isChecked;// 相册详情中的图片是否处于被选中状态 用于多次进入相册  判断是否选择过了
        private String path;//文件路径,经过思考和假设，文件路径只在选定图片时进行获取并保存，如果把扫描的图片都保存路径，会浪费很多内存
    }


    public ArrayList<String> getFolders() {
        folderNames = new ArrayList<>();
        Map<String, List<ImageModel>> imagemap = getInstance().getFolderMap();
//        DisplayImageOptions optionss = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(false)
//                .bitmapConfig(Bitmap.Config.RGB_565).setImageSize(new ImageSize(ImageUtils.getQuarterWidth(), 0))
//                .displayer(new SimpleBitmapDisplayer()).build();
        ArrayList<String> folderNamess = new ArrayList<>();
        for (Object o : imagemap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            assert folderNamess != null;
            folderNamess.add(key);
        }
        //根据文件夹内的图片数量降序显示
        if (folderNamess != null) {
            Collections.sort(folderNamess, new Comparator<String>() {
                public int compare(String arg0, String arg1) {
                    Integer num1 = getInstance().getFolder(arg0).size();
                    Integer num2 = getInstance().getFolder(arg1).size();
                    return num2.compareTo(num1);
                }
            });
        }
        return folderNamess;
    }

    private void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
