package com.yang.chathelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * @author: ASUS
 * @date: 2019/4/3
 * @description:
 */
public class AdContentAdapter  extends RecyclerView.Adapter<AdContentAdapter.MyViewHolder> {
    private ArrayList<String> mData;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private Context mContext;

    private onTextChangeListener mTextListener;

    public AdContentAdapter(ArrayList<String> data, Context context) {
        mData = data;
        mContext = context;
    }

    public interface onTextChangeListener{
        void onTextChanged(int position, String string);
    }

    public void setOnTextChangeListener(onTextChangeListener onTextChangeListener){
        this.mTextListener=onTextChangeListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    private boolean onBind;
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mEditText.setHint(mData.get(i));
        myViewHolder.mEditText.setTag(i);

        //添加EditText的监听事件
        myViewHolder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mTextListener.onTextChanged(i,myViewHolder.mEditText.getText().toString());
            }
        });

        myViewHolder.mCheckBox.setTag(i);

        myViewHolder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = (int) buttonView.getTag();
            if (isChecked) {
                mCheckStates.put(pos, true);
            } else {
                mCheckStates.put(pos, false);
            }
            if (!onBind) {
                notifyItemChanged(pos);
            }
            mTextListener.onTextChanged(i,myViewHolder.mEditText.getText().toString());
        });
        onBind = true;
        myViewHolder.mCheckBox.setChecked(mCheckStates.get(i, false));

    }

    public SparseBooleanArray getCheckStates() {
        return mCheckStates;
    }



    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox mCheckBox;
        EditText mEditText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.check);
            mEditText = itemView.findViewById(R.id.content);

        }
    }



}
