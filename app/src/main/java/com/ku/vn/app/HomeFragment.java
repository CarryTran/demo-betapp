package com.ku.vn.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button btnDiscount, btnIntroPlayers, btnHOME;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        btnDiscount = v.findViewById(R.id.btnDiscount);
        btnIntroPlayers = v.findViewById(R.id.btnIntroPlayers);
        btnHOME = v.findViewById(R.id.btnHOME);

        getActivity().setTitle("TRANG CHỦ");

        btnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscountFragment fragment = new DiscountFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "ĐĂNG KÝ KHUYẾN MÃI");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnIntroPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroPlayersFragment fragment = new IntroPlayersFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "GIỚI THIỆU NGƯỜI CHƠI");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnHOME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("msg", "https://ku9vn.com/index.php/category/home/");
                startActivity(intent);
            }
        });

        return v;
    }

}
