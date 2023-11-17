package com.appreman.app.Repository;

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

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.appreman.app.Dao.GrupoDao;
import com.appreman.app.Database.RemanRoomDatabase;
import com.appreman.app.Models.Grupo;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class GrupoRepository {

    private final GrupoDao mGrupoDao;
    private final LiveData<List<Grupo>> mAllGrupos;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public GrupoRepository(Application application) {
        RemanRoomDatabase db = RemanRoomDatabase.getDatabase(application);
        mGrupoDao = db.grupoDao();
        mAllGrupos = mGrupoDao.getGrupos();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Grupo>> getAllGrupos() {
        return mAllGrupos;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
   public void insert(Grupo grupo) {
        RemanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGrupoDao.insert(grupo);
        });
    }
}
