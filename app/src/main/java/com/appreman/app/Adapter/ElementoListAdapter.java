package com.appreman.app.Adapter;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.appreman.app.Models.Grupo;


public class ElementoListAdapter extends ListAdapter<Grupo, GrupoViewHolder> {

    public ElementoListAdapter(@NonNull DiffUtil.ItemCallback<Grupo> diffCallback) {
        super(diffCallback);
    }

    @Override
    public GrupoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return GrupoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(GrupoViewHolder holder, int position) {
        Grupo current = getItem(position);
        holder.bind(current);


    }

    public static class GrupoDiff extends DiffUtil.ItemCallback<Grupo> {

        @Override
        public boolean areItemsTheSame(@NonNull Grupo oldItem, @NonNull Grupo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grupo oldItem, @NonNull Grupo newItem) {
            return oldItem.getNombre().equals(newItem.getNombre());
        }
    }
}
