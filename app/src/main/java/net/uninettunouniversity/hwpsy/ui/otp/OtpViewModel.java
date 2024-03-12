package net.uninettunouniversity.hwpsy.ui.otp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OtpViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OtpViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is otp fragment");
    }


    public LiveData<String> getText() {
        return mText;
    }
}