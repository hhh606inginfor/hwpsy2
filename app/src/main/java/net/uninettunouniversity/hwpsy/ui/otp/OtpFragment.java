package net.uninettunouniversity.hwpsy.ui.otp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.uninettunouniversity.hwpsy.OtpProvider;
import net.uninettunouniversity.hwpsy.databinding.FragmentOtpBinding;

public class OtpFragment extends Fragment {

    private FragmentOtpBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OtpViewModel otpViewModel =
                new ViewModelProvider(this).get(OtpViewModel.class);

        binding = FragmentOtpBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void addOtp(View v) {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        ContentValues values = new ContentValues();

        values.put(OtpProvider.OTP, "12345");
        values.put(OtpProvider.TIMESTAMP, ts);

        Uri uri = getActivity().getContentResolver().insert(
                OtpProvider.CONTENT_URI, values
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}