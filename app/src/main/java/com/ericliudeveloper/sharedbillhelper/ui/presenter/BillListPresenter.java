package com.ericliudeveloper.sharedbillhelper.ui.presenter;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ericliudeveloper.sharedbillhelper.R;
import com.ericliudeveloper.sharedbillhelper.model.Bill;
import com.ericliudeveloper.sharedbillhelper.model.BillDAO;
import com.ericliudeveloper.sharedbillhelper.util.CustomEvents;
import com.ericliudeveloper.sharedbillhelper.util.DigitUtils;
import com.ericliudeveloper.sharedbillhelper.util.ResouceUtils;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by liu on 8/06/15.
 */
public class BillListPresenter implements ListPresenter {
    private boolean isListSelectionMode = false;

    public static HashMap<Long, Bill> mSelection = new HashMap<>();


    public BillListPresenter(boolean isSelectionMode) {
        isListSelectionMode = isSelectionMode;
    }


    @Override
    public RecyclerView.ViewHolder getCustomViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_row_layout, parent, false);
        return new BillViewHolder(itemView, isListSelectionMode);
    }


    @Override
    public void setViewHolderData(RecyclerView.ViewHolder holder, Cursor cursor) {

        Bill bill = BillDAO.getBillFromCursor(cursor);
        if (bill != null) {
            ((BillViewHolder) holder).setItem(bill);
        }
    }


    public static class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private Bill mBill;

        private boolean isSelectionMode = false;

        private CardView cardView;
        private TextView tvType;
        private TextView tvAmount;
        private TextView tvPaid;
        private TextView tvDueDay;
        public final CheckBox checkBox;

        public BillViewHolder(View itemView, boolean isSelectionMode) {
            super(itemView);
            this.isSelectionMode = isSelectionMode;
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvPaid = (TextView) itemView.findViewById(R.id.tvPaid);
            tvDueDay = (TextView) itemView.findViewById(R.id.tvDueDay);
            checkBox = (CheckBox) itemView.findViewById(android.R.id.checkbox);

            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);

            if (isSelectionMode) {
                tvPaid.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
                int pixels = ResouceUtils.getAppResources().getDimensionPixelSize(R.dimen.selectionModeRowItemHeight);
                cardView.getLayoutParams().height = pixels;
            } else {
                tvPaid.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.GONE);
            }
        }


        public void setItem(Bill bill) {
            mBill = bill;

            refreshDisplay(bill);
        }

        public void refreshDisplay(Bill bill) {
            String type = bill.getType();
            String amount = DigitUtils.convertToDollarFormat(bill.getAmount());
            String isPaid = bill.getPaid() > 0 ? tvAmount.getResources().getString(R.string.paid)
                    : tvAmount.getResources().getString(R.string.unpaid);

            String dueDay = bill.getDueDate();


            tvType.setText(type);
            tvAmount.setText(amount);
            tvPaid.setText(isPaid);
            tvDueDay.setText(dueDay);


            checkBox.setChecked(mSelection.containsKey(bill.getId()));

        }


        @Override
        public void onClick(View v) {

            if (isSelectionMode) {

                checkBox.setChecked(!checkBox.isChecked());

            } else {

                CustomEvents.EventViewBill eventViewBill = new CustomEvents.EventViewBill(mBill);

                // send an sticky event to the view detail Fragment
                EventBus.getDefault().postSticky(eventViewBill);

            }
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (!mSelection.containsKey(mBill.getId())) {
                    mSelection.put(mBill.getId(), mBill);
                }
            } else {
                if (mSelection.containsKey(mBill.getId())) {
                    mSelection.remove(mBill.getId());
                }
            }
        }
    }
}
