package topica.edu.vn.betapp_demo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountFragment extends Fragment {
    FragmentActivity listener;

    Button btnConfirm;
    EditText txtFullname, txtUsername, txtPhone;
    RadioGroup radioGroupYesNo;
    RadioButton rdbYes, rdbNo;
    static ProgressDialog pdialog;

    private View v;
    public static String fullname, username, phone, ynq;
    final static String LINK = "https://script.google.com/macros/s/AKfycbxJjJ2XnDO0NeKnEtXz4OFqPtO-XGmfjaSvpOrR81Uoq8erZhk/exec";
    final static String PHONE_REGEX = "(0+[1-9])+([0-9]{8})\\b";

    public DiscountFragment() {
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
        v = inflater.inflate(R.layout.fragment_discount, container, false);
        getActivity().setTitle("ĐĂNG KÝ KHUYẾN MÃI");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        radioGroupYesNo = view.findViewById(R.id.radioGroupYesNo);
        rdbYes = view.findViewById(R.id.rdbYes);
        rdbNo = view.findViewById(R.id.rdbNo);
        txtFullname = view.findViewById(R.id.txtFullName);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtUsername = view.findViewById(R.id.txtUserName);
        pdialog = new ProgressDialog(getActivity());
        pdialog.setCanceledOnTouchOutside(false);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = txtFullname.getText().toString().trim();
                phone = txtPhone.getText().toString().trim();
                username = txtUsername.getText().toString().trim();
                if(rdbYes.isChecked()) {
                    ynq = "YES";
                } else if(rdbNo.isChecked()) {
                    ynq = "NO";
                } else {
                    ynq = "";
                }
                if(fullname.matches("") || phone.matches("")) {
                    Toast.makeText(getActivity(), "Quý khách vui lòng điền đầy đủ vào ô trống!", Toast.LENGTH_SHORT).show();
                }
                else if(!phone.matches(PHONE_REGEX)) {
                    Toast.makeText(getActivity(), "Số điện thoại chưa hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else {
                    new SendRequestTask(DiscountFragment.this).execute(LINK);
                }

            }
        });
    }

    private static class SendRequestTask extends AsyncTask<String, Void, Boolean> {

        private WeakReference<DiscountFragment> mRef;
        public SendRequestTask(DiscountFragment activity){
            mRef = new WeakReference<DiscountFragment>(activity);
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
            DiscountFragment asyncTaskLeak = mRef.get();
            if (asyncTaskLeak != null) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(asyncTaskLeak.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                if(aBoolean == true) {
                    alertDialog.setTitle("Thông báo");
                    alertDialog.setMessage("Quý khách đã đăng ký khuyến mãi thành công");
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
                    alertDialog.setMessage("Quý khách CHƯA đăng ký khuyến mãi thành công. Xin kiểm tra lại!");
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

                DiscountFragment asyncTaskLeak = mRef.get();

                postDataParams.put("fullname", fullname);
                postDataParams.put("phone", "'"+phone);
                postDataParams.put("username", username);
                postDataParams.put("ynq", ynq);

                Log.e("params", postDataParams.toString());

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000 /* milliseconds */);
                con.setConnectTimeout(15000 /* milliseconds */);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writter.write(asyncTaskLeak.getPostDataString(postDataParams));

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

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

}
