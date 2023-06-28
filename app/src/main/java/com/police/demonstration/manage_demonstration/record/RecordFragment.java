package com.police.demonstration.manage_demonstration.record;

import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementDataBase;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.database.notification.NotificationDataBase;
import com.police.demonstration.database.notification.NotificationInfo;
import com.police.demonstration.databinding.FragmentRecordBinding;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecordFragment extends Fragment {

    private DemonstrationInfo demonstrationInfo;
    private FragmentRecordBinding binding;

    private ArrayList<MeasurementInfo> MesurementList;
    private ArrayList<NotificationInfo> NotificationList;

    private Button record;
    private Button notify;

    private TextView demonstration_name;

    private MeasurementAdapter measurementAdapter;
    private NotifyAdaptor notifyAdaptor;

    private boolean isCheck = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        record = binding.MesuareRecordButton;
        notify = binding.taxSendList;
        demonstration_name = binding.demstartionName;

        binding.backButton2.setOnClickListener(e -> requireActivity().finish());

        record.setOnClickListener(this::onclick);
        notify.setOnClickListener(this::onclick);

        demonstrationInfo = requireActivity().getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        demonstration_name.setText("[" + demonstrationInfo.getName() + "]"); // 시위 이름 설정
        readMeasurement(requireContext(), demonstrationInfo.getId(), 2);
        readMeasurement(requireContext(), demonstrationInfo.getId(), 1);

        return root;
    }

    private void initRecyclerView(int code) {
        if (code == 1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            binding.recordRecycle1.setLayoutManager(linearLayoutManager);
            measurementAdapter = new MeasurementAdapter(
                    MesurementList, demonstrationInfo
            );
            binding.recordRecycle1.setAdapter(measurementAdapter);
        }

        if (code == 2) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            binding.recordRecycle2.setLayoutManager(linearLayoutManager);
            notifyAdaptor = new NotifyAdaptor(NotificationList);
            binding.recordRecycle2.setAdapter(notifyAdaptor);
        }
    }

    public void onclick(View view) {
        int num = view.getId();

        if (num == record.getId()) {
            record.setEnabled(false);
            notify.setEnabled(true);
            binding.recordRecycle1.setVisibility(View.VISIBLE);
            binding.recordRecycle2.setVisibility(View.GONE);
        }

        if (num == notify.getId()) {
            record.setEnabled(true);
            notify.setEnabled(false);
            binding.recordRecycle2.setVisibility(View.VISIBLE);
            binding.recordRecycle1.setVisibility(View.GONE);
        }
    }

    public void readMeasurement(Context context, int demonstrationId, int code) {
        // DB 에서 read (Rxjava 비동기) 후 listener 를 통해 변경 알림 (onChanged())

        if (code == 1) {
            MeasurementDataBase.getInstance(context)
                    .measurementDao()
                    .getMeasurement(demonstrationId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(list -> {
                        MesurementList = new ArrayList<>(list);
                        initRecyclerView(code);
                    }).subscribe();
        }

        if (code == 2) {
            NotificationDataBase.getInstance(context)
                    .notificationDao()
                    .getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(list -> {
                        MeasurementDataBase.getInstance(context)
                                .measurementDao()
                                .getMeasurement(demonstrationId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSuccess(measureList -> {
                                    ArrayList<NotificationInfo> notificationInfoList = new ArrayList<>();
                                    for (int i = 0; i < list.size(); i++) {
                                        for (int j = 0; j < measureList.size(); j++) {
                                            if (list.get(i).getMeasurementId() == measureList.get(j).getId()
                                            && measureList.get(j).getDemonstrationId() == demonstrationId) {
                                                notificationInfoList.add(list.get(i));
                                            }
                                        }
                                    }

                                    NotificationList = notificationInfoList;

                                    initRecyclerView(code);
                                    if (!isCheck) {
                                        Checker(NotificationList);
                                        isCheck = true;
                                    }
                                }).subscribe();

                    }).subscribe();

        }

    }

    private void Checker(ArrayList<NotificationInfo> NotificationList) {
        for (int i = 0; i < NotificationList.size(); i++) {
            if (NotificationList.get(i).getNotificationTypeName().charAt(0) == '등') {
                binding.MaintainRow1Col1.setText(Integer.toString(Integer.parseInt(binding.MaintainRow1Col1.getText().toString()) + 1));

            } else if (NotificationList.get(i).getNotificationTypeName().charAt(0) == '최') {
                binding.MaintainRow2Col1.setText(Integer.toString(Integer.parseInt(binding.MaintainRow1Col1.getText().toString()) + 1));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
