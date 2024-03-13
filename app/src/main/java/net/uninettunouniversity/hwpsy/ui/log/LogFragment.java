package net.uninettunouniversity.hwpsy.ui.log;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.uninettunouniversity.hwpsy.MainActivity;
import net.uninettunouniversity.hwpsy.OtpProvider;
import net.uninettunouniversity.hwpsy.databinding.FragmentLogBinding;

public class LogFragment extends Fragment {

    private FragmentLogBinding binding;

    TextView textViewLogArea;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogViewModel logViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);

        binding = FragmentLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLog;
        logViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        textViewLogArea = binding.textview3;

        textViewLogArea.setSingleLine(false);
        textViewLogArea.setMovementMethod(new ScrollingMovementMethod());

        MainActivity activity = (MainActivity) getActivity();
        String myDataFromActivity = activity.retrieveOtps();

        textViewLogArea.setText(myDataFromActivity);

        return root;
    }

    public void setTextViewLogs(String value){
        if(textViewLogArea != null) textViewLogArea.setText(value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}