package com.eric.network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eric.network.client.RetrofitGenerator;
import com.eric.network.model.RequestModel;
import com.eric.network.response.IDownLoadSuccess;
import com.eric.network.response.IError;
import com.eric.network.response.ISuccess;
import com.eric.network.service.ApiService;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
    }

    // http://127.0.0.1:8088/api/girls
    public void netRequest(View view) {

        String url = "http://192.168.1.10:8088/api/girls";
        Map<String, Object> param = new HashMap<>();
        param.put("id", "123");


        AndHttp.getInstance()
                .url(url)
                .params(param)
                .success(new ISuccess() {
                    @Override
                    public void success(String result) {
                        Log.e("CCMTV", "success  " + result);
                    }
                })
                .error(new IError() {
                    @Override
                    public void error(int code, String error) {
                        Log.e("CCMTV", "error  " + error);
                    }
                })
                .get();

    }

    public void Post(View view) {

        String url = "http://192.168.1.10:8088/api/newGirls";
        Map<String, Object> param = new HashMap<>();
        param.put("age", "20");
        param.put("cupSize", "F");

        AndHttp.getInstance()
                .url(url)
                .params(param)
                .success(new ISuccess() {
                    @Override
                    public void success(String result) {
                        Log.e("CCMTV", "success  " + result);
                    }
                })
                .error(new IError() {
                    @Override
                    public void error(int code, String error) {
                    }
                })
                .post();
    }

    public void PostRow(View view) {

        // 测试接口
        String url ="http://192.168.1.10:8088/api/postRow";
        AndHttp.getInstance()
                .url(url)
                .params("id","1")
                .success(new ISuccess() {
                    @Override
                    public void success(String result) {
                        Log.e("CCMTV", "success  " + result);
                    }
                })
                .error(new IError() {
                    @Override
                    public void error(int code, String error) {
                        Log.e("CCMTV", "error   " + error);
                    }
                })
                .postRow();
    }

    public void Upload(View view) {

        Album.image(this) // Image selection.
                .multipleChoice()
                .columnCount(3)
                .selectCount(1)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {

                        String url = "http://192.168.1.10:8088/api/newUpload";
                        AlbumFile albumFile = result.get(0);

                        AndHttp.getInstance()
                                .url(url)
                                .fileParams("file", new File(albumFile.getPath()))
                                .success(new ISuccess() {
                                    @Override
                                    public void success(String result) {
                                        Log.e("CCMTV", "success  " + result);
                                    }
                                })
                                .error(new IError() {
                                    @Override
                                    public void error(int code, String error) {

                                    }
                                })
                                .uploadFile();

                    }
                })
                .start();
    }

    public void loadImg(View view) {

        String url = "http://192.168.1.10:8088/api/image";

        AndHttp.getInstance()
                .url(url)
                .success(new IDownLoadSuccess() {
                    @Override
                    public void success(File file) {
                        Glide.with(MainActivity.this)
                                .load(file.getAbsoluteFile())
                                .error(R.mipmap.ic_launcher)
                                .into(iv);
                    }
                })
                .error(new IError() {
                    @Override
                    public void error(int code, String error) {

                    }
                })
                .downLoad();

    }


    //根据指定的二进制流字符串保存文件并返回保存路径
    public static String saveFileByBinary(String str, String attName, Context context) {                   /***加载附件***/
        //获取存储卡路径、构成保存文件的目标路径
        String dirName = "";
        //SD卡具有读写权限、指定附件存储路径为SD卡上指定的文件夹
        dirName = Environment.getExternalStorageDirectory() + "/MyDownload/";
        File f = new File(dirName);
        if (!f.exists()) {      //判断文件夹是否存在
            f.mkdir();        //如果不存在、则创建一个新的文件夹
        }
        //准备拼接新的文件名
        String fileName = "";
        fileName = attName;
        fileName = dirName + fileName;
        File file = new File(fileName);
        if (file.exists()) {    //如果目标文件已经存在
            file.delete();    //则删除旧文件
        }
        byte[] byteFile = str.getBytes();
        try {
            InputStream is = new ByteArrayInputStream(byteFile);
            FileOutputStream os = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len = 0;
            //开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            //完毕关闭所有连接
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}
