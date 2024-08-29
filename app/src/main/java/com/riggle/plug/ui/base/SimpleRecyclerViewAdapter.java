package com.riggle.plug.ui.base;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.riggle.plug.BR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleRecyclerViewAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.SimpleViewHolder<B>> {

    @LayoutRes
    private final int layoutResId;
    private final int modelVariableId;
    private final SimpleCallback<M> callback;
    private final List<M> dataList = new ArrayList<>();
    private final boolean loading = false;

    public void removeItem(int i) {
        Log.i("TAG", "removeItem: " + i);
        try {
            if (i != -1) {
                dataList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, dataList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteObject(M bean) {
        try {
            if (bean != null) {
                dataList.remove(bean);
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(int i, M scanResult) {
        if (scanResult == null) return;
        dataList.add(i, scanResult);
        notifyItemChanged(i);
    }


    public interface SimpleCallback<M> {
        void onItemClick(View v, M m, int pos);
    }

    public SimpleRecyclerViewAdapter(@LayoutRes int layoutResId, int modelVariableId, SimpleCallback<M> callback) {
        this.layoutResId = layoutResId;
        this.modelVariableId = modelVariableId;
        this.callback = callback;
    }

    @NonNull
    @Override
    public SimpleViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
        return new SimpleViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {

        if (!loading && position < dataList.size()) {
            holder.binding.setVariable(BR.callback, callback);
            holder.binding.setVariable(modelVariableId, dataList.get(position));
            holder.binding.setVariable(BR.pos, position);
           // holder.binding.setVariable(BR.listsize, dataList.size());
        } else {
            holder.binding.setVariable(BR.callback, null);
            holder.binding.setVariable(modelVariableId, null);
            holder.binding.setVariable(BR.pos, 0);
          //  holder.binding.setVariable(BR.listsize, 0);
        }
        holder.binding.executePendingBindings();
    }

 /*   public void onBind(B binding, M bean, int position) {
        binding.setVariable(BR.size, dataList.size());
        if (!loading && position < dataList.size()) {
            binding.setVariable(BR.callback, callback);
            binding.setVariable(modelVariableId, dataList.get(position));
            binding.setVariable(BR.pos, position);
        } else {
            binding.setVariable(BR.callback, null);
            binding.setVariable(modelVariableId, null);
            binding.setVariable(BR.pos, 0);
        }
    }*/

    @Override
    public int getItemCount() {
        if (loading) return 10;
        else return dataList.size();
    }

    public void setList(@Nullable List<M> newDataList) {
        dataList.clear();
        if (newDataList != null)
            dataList.addAll(newDataList);
        notifyDataSetChanged();
    }

    public void filterList(@Nullable List<M> filteredNames) {
        dataList.clear();
        if (filteredNames != null)
        dataList.addAll(filteredNames);
        notifyDataSetChanged();
    }

    public List<M> getList() {
        return dataList;

    }

    public void addToList(@Nullable List<M> newDataList) {
        if (newDataList == null) {
            newDataList = Collections.emptyList();
        }
        int positionStart = dataList.size();
        int itemCount = newDataList.size();
        dataList.addAll(newDataList);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void clearList() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void addData(@NonNull M data) {
        int positionStart = dataList.size();
        dataList.add(data);
        notifyItemInserted(positionStart);
    }

    /**
     * Simple view holder for this adapter
     *
     * @param <S>
     */
    static class SimpleViewHolder<S extends ViewDataBinding> extends RecyclerView.ViewHolder {
        final S binding;

        public SimpleViewHolder(S binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
