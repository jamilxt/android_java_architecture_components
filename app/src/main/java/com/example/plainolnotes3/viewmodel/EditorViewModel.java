package com.example.plainolnotes3.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.example.plainolnotes3.database.AppRepository;
import com.example.plainolnotes3.database.NoteEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<NoteEntity> mLiveNote = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void loadData(int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity note = mRepository.getNoteById(noteId);
                mLiveNote.postValue(note);
            }
        });
    }

    public void saveNote(String noteText) {
        NoteEntity note = mLiveNote.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }

            note = new NoteEntity(new Date(), noteText.trim());

        } else {
            note.setText(noteText.trim());
        }

        mRepository.insertNote(note);
    }

    public void deleteNote() {
        mRepository.deleteNote(mLiveNote.getValue());
    }
}
