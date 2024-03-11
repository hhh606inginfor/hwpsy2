package net.uninettunouniversity.hwpsy.ui.otp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.uninettunouniversity.hwpsy.databinding.FragmentOtpBinding;

public class OtpFragment extends Fragment {

    private FragmentOtpBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OtpViewModel otpViewModel =
                new ViewModelProvider(this).get(OtpViewModel.class);

        binding = FragmentOtpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textOtp;
        otpViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}