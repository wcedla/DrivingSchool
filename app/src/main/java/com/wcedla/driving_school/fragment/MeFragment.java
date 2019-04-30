package com.wcedla.driving_school.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.R;
import com.wcedla.driving_school.activity.AboutActivity;
import com.wcedla.driving_school.activity.ChangePasswordActivity;
import com.wcedla.driving_school.activity.LoginActivity;
import com.wcedla.driving_school.activity.UserInfoActivity;
import com.wcedla.driving_school.tool.HttpUtils;
import com.wcedla.driving_school.tool.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.wcedla.driving_school.constant.Config.SET_USER_HEAD;
import static com.wcedla.driving_school.constant.Config.UPLOAD_USER_HEAD;

public class MeFragment extends Fragment {

    Activity myActivity;
    float denisty;
    CircleImageView userHead;
    String imagePath = "";


    public static MeFragment getInstance(Bundle bundle) {
        MeFragment meFragment = new MeFragment();
        meFragment.setArguments(bundle);
        return meFragment;
    }

    @Override
    public void onAttach(Context context) {
        myActivity = (Activity) context;
        denisty = context.getResources().getDisplayMetrics().density;
        if (getArguments() != null) {
//            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        userHead = view.findViewById(R.id.user_img);
        TextView userNickName = view.findViewById(R.id.user_nick_name);
        ConstraintLayout userInfo = view.findViewById(R.id.user_info_root);
        ConstraintLayout passwordRoot = view.findViewById(R.id.password_root);
        ConstraintLayout aboutRoot = view.findViewById(R.id.about_root);
        Button loginOut = view.findViewById(R.id.login_out_btn);
        File headFile = Hawk.get("newHead", null);
        Logger.d("图片" + headFile);
        String headImg = Hawk.get("headImg", "");
        String loginUser = Hawk.get("loginUser", "");
        if (headFile == null) {
            if (headImg.length() > 0) {
                Glide.with(myActivity).load(headImg).apply(new RequestOptions().dontAnimate().placeholder(R.drawable.bsj).signature(new ObjectKey(System.currentTimeMillis()))).into(userHead);
            }
        } else {
            Glide.with(myActivity).load(BitmapFactory.decodeFile(headFile.getPath())).apply(new RequestOptions()
                    .dontAnimate()
                    .placeholder(R.drawable.bsj)
                    .override((int) (80 * denisty), (int) (80 * denisty))).into(userHead);

        }
        userNickName.setText(loginUser);
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albmIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albmIntent.setType("image/*");
                startActivityForResult(albmIntent, 1);
            }
        });
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userInfoIntent = new Intent(myActivity, UserInfoActivity.class);
                startActivity(userInfoIntent);
            }
        });
        passwordRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasswordIntent = new Intent(myActivity, ChangePasswordActivity.class);
                startActivity(resetPasswordIntent);
            }
        });

        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.delete("loginType");
                Hawk.delete("newHead");
                Hawk.delete("loginNo");
                Hawk.delete("loginUser");
                Intent loginIntent = new Intent(myActivity, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                myActivity.finish();
                startActivity(loginIntent);
            }
        });

        aboutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(myActivity, AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Logger.d("选择的图片的uri地址:" + uri);

                if (DocumentsContract.isDocumentUri(myActivity, uri)) {
                    // 如果是document类型的Uri，则通过document id处理
                    String docId = DocumentsContract.getDocumentId(uri);
                    if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String id = docId.split(":")[1]; // 解析出数字格式的id
                        String selection = MediaStore.Images.Media._ID + "=" + id;
                        imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                        imagePath = getImagePath(contentUri, null);
                    }
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    // 如果是content类型的Uri，则使用普通方式处理
                    imagePath = getImagePath(uri, null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    // 如果是file类型的Uri，直接获取图片路径即可
                    imagePath = uri.getPath();
                }
                Logger.d("选择的图片地址:" + imagePath);
                File file = new File(imagePath);
                List<File> fileList = new ArrayList<>();
                fileList.add(file);
                HttpUtils.uploadFile(UPLOAD_USER_HEAD, fileList, Hawk.get("loginNo", "") + ".jpg", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Logger.d("头像上传失败" + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int result = JsonUtils.getResetStatus(response.body().string());
                        if (result == -1) {
                            setImage();
                        }
                    }
                });

            }
        }
    }

    private void setImage() {
        String url = HttpUtils.setParameterForUrl(SET_USER_HEAD, "no", Hawk.get("loginNo", ""));
        HttpUtils.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d("头像上传失败" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result = JsonUtils.getResetStatus(response.body().string());
                if (result == -1) {
                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(myActivity).load(BitmapFactory.decodeFile(imagePath))
                                    .apply(new RequestOptions().override((int) (80 * denisty), (int) (80 * denisty)).dontAnimate())
                                    .into(userHead);

                        }
                    });
                    Hawk.put("newHead", new File(imagePath));
                    Logger.d("头像上传成功");
                }

            }
        });
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = myActivity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
