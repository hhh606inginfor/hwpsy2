package net.uninettunouniversity.hwpsy.ui.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import net.uninettunouniversity.hwpsy.MainActivity;
import net.uninettunouniversity.hwpsy.databinding.FragmentLogBinding;

public class LogFragment extends Fragment {

    private FragmentLogBinding binding;

    TextView textViewLogArea;

    BroadcastReceiver br;
    Context _context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogViewModel logViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);

        binding = FragmentLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setup();
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

    private void setup()
    {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i)
            {
                MainActivity activity = (MainActivity) getActivity();
                String myDataFromActivity = activity.retrieveOtps();

                textViewLogArea.setText(myDataFromActivity);
            }
        };
        _context.registerReceiver(br, new IntentFilter("net.uninettunouniversity.hwpsy.REFRESH_INTENT"), Context.RECEIVER_NOT_EXPORTED);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        _context.unregisterReceiver(br);
    }
}