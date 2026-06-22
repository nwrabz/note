package org.wrabz.note.service;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;
import org.wrabz.note.exception.ResourceNotFoundException;
import org.wrabz.note.model.Note;
import org.wrabz.note.repository.NoteRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public Note createNoteForUser(String username, String content) {
        Note note = new Note();
        note.setOwnerUsername(username);
        note.setContent(content);
        return noteRepository.save(note);
    }

    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')") @PostAuthorize
    //@Secured({"ROLE_ADMIN", "ROLE_USER"})
    //@PreFilter("filterObject.owner == authentication.name")
    //@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @Override
    public List<Note> showMyNotes(String username) {
        return noteRepository.findNoteByOwnerUsername(username);
    }


    //@PreAuthorize("#users.username == authentication.name")
    @Override
    public Note updateNoteForUser(Long noteId, String content, String username) {
        Note noteFromDB = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "noteId", noteId));

        noteFromDB.setContent(content);
        noteFromDB.setOwnerUsername(username);

        return noteRepository.save(noteFromDB);
    }

    @Override
    public void deleteNote(Long noteId, String username) {
        noteRepository.deleteById(noteId);
    }
}
