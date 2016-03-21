package com.jelvix.savingview;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.ViewGroup;

public class ChildrenViewStateHelper {
    public static final String DEFAULT_CHILDREN_STATE_KEY =  ChildrenViewStateHelper.class.getSimpleName() + ".childrenState";
    private ViewGroup viewGroup;

    public static ChildrenViewStateHelper newInstance(@NonNull final ViewGroup viewGroup) {
        final ChildrenViewStateHelper handler = new ChildrenViewStateHelper();
        handler.viewGroup = viewGroup;
        return handler;
    }

    private ChildrenViewStateHelper() {
    }

    public SparseArray<Parcelable> saveChildrenState() {
        final SparseArray<Parcelable> array = new SparseArray<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            final Bundle bundle = new Bundle();
            final SparseArray<Parcelable> childArray = new SparseArray<>(); //create independent SparseArray for each child (View or ViewGroup)
            viewGroup.getChildAt(i).saveHierarchyState(childArray);
            bundle.putSparseParcelableArray(DEFAULT_CHILDREN_STATE_KEY, childArray);
            array.append(i, bundle);
        }
        return array;
    }

    public void restoreChildrenState(@Nullable final SparseArray<Parcelable> childrenState) {
        if (null == childrenState) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            final Bundle bundle = (Bundle) childrenState.get(i);
            final SparseArray<Parcelable> childState = bundle.getSparseParcelableArray(DEFAULT_CHILDREN_STATE_KEY);
            viewGroup.getChildAt(i).restoreHierarchyState(childState);
        }
    }
}