package org.wrabz.note.service;

import org.wrabz.note.model.Note;

import java.util.List;

public interface NoteService {

    Note createNoteForUser(String username, String content);

    List<Note> showMyNotes(String username);

    Note updateNoteForUser(Long noteId, String content, String username);

    void deleteNote(Long noteId, String username);

}
