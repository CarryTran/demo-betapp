package com.ku.vn.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroPlayersFragment extends Fragment {
    FragmentActivity listener;

    Button btnConfirm;
    EditText txtFullname, txtUsername, txtPhone, txtCode;
    //TextView txtLinkTC;
    static ProgressDialog pdialog;

    private View v;

    public static String fullname, username, phone, code;
    final static String LINK = "https://script.google.com/macros/s/AKfycbw6xkv596HEXJq7Dv2yioz4Nazlhk_eu6mNs3KvkJJCa94d4Dxu/exec";
    final static String PHONE_REGEX = "(0+[1-9])+([0-9]{8})\\b";
    final static String LINK_TrangChu = "https://kubet9.org/";

    public IntroPlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_intro_players, container, false);
        getActivity().setTitle("GIỚI THIỆU NGƯỜI CHƠI");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtFullname = view.findViewById(R.id.txtFullName_intro);
        txtUsername = view.findViewById(R.id.txtUserName_intro);
        txtPhone = view.findViewById(R.id.txtPhone_intro);
        txtCode = view.findViewById(R.id.txtCode_intro);
        btnConfirm = view.findViewById(R.id.btnConfirm_intro);
        //txtLinkTC = view.findViewById(R.id.txtLinkTrangChu);
        pdialog = new ProgressDialog(getActivity());
        pdialog.setCanceledOnTouchOutside(false);

        // handle intro link.
        /*String text1 = "Link giới thiệu: ";
        String text2 = text1 + LINK_TrangChu;

        Spannable spannable = new SpannableString(text2);

        spannable.setSpan(new ForegroundColorSpan(Color.rgb(255, 140, 0)), text1.length(), (text1 + LINK_TrangChu).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtLinkTC.setText(spannable, TextView.BufferType.SPANNABLE);*/

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = txtFullname.getText().toString().trim();
                phone = txtPhone.getText().toString().trim();
                username = txtUsername.getText().toString().trim();
                code = txtCode.getText().toString().trim();
                if(fullname.matches("") || phone.matches("")) {
                    Toast.makeText(getActivity(), "Quý khách vui lòng điền đầy đủ vào ô trống!", Toast.LENGTH_SHORT).show();
                }
                else if(!phone.matches(PHONE_REGEX)) {
                    Toast.makeText(getActivity(), "Số điện thoại chưa hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else {
                    new sendRequestTask(IntroPlayersFragment.this).execute(LINK);
                }
            }
        });

        /*txtLinkTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("msg", LINK_TrangChu);
                startActivity(intent);
            }
        });*/
    }

    private static class sendRequestTask extends AsyncTask<String, Void, Boolean> {

        private WeakReference<IntroPlayersFragment> mRef;
        public sendRequestTask(IntroPlayersFragment activity){
            mRef = new WeakReference<IntroPlayersFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setTitle("Đang xử lý");
            pdialog.setMessage("Quý khách vui lòng chờ trong giấy lát");
            pdialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pdialog.dismiss();
            IntroPlayersFragment asyncTaskLeak = mRef.get();
            if (asyncTaskLeak != null) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(asyncTaskLeak.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                if(aBoolean == true) {
                    alertDialog.setTitle("Thông báo");
                    alertDialog.setMessage("Giới thiệu thành công. Liên hệ hỗ trợ để được nhận HOA HỒNG GIỚI THIỆU");
                    alertDialog.setCancelable(false);
                    alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
                else {
                    alertDialog.setTitle("Thông báo!!!");
                    alertDialog.setMessage("Quý khách giới thiệu KHÔNG thành công. Xin kiểm tra lại!");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                JSONObject postDataParams = new JSONObject();

                IntroPlayersFragment asyncTaskLeak = mRef.get();

                postDataParams.put("fullname", fullname);
                postDataParams.put("phone", "'"+phone);
                postDataParams.put("username", username);
                postDataParams.put("code", code);

                Log.e("params", postDataParams.toString());

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000 /* milliseconds */);
                con.setConnectTimeout(15000 /* milliseconds */);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writter.write(DiscountFragment.getPostDataString(postDataParams));

                writter.flush();
                writter.close();
                os.close();

                int responseCode=con.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in= new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    //return true;

                }
                else {
                    return false;
                }
                return true;
            }
            catch (Exception ex) {
                Log.e("Error: ", ex.toString());
                return false;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
