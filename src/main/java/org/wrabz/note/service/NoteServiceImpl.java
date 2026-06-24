package org.wrabz.note.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wrabz.note.exception.ResourceNotFoundException;
import org.wrabz.note.model.Note;
import org.wrabz.note.repository.NoteRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final AuditLogService auditLogService;

    @Override
    public Note createNoteForUser(String username, String content) {
        Note note = new Note();
        note.setOwnerUsername(username);
        note.setContent(content);
        Note saved = noteRepository.save(note);
        auditLogService.logNoteCreation(username, note);
        return saved;
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
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "noteId", noteId));

        note.setContent(content);
        note.setOwnerUsername(username);

        Note updatedNote = noteRepository.save(note);
        auditLogService.logNoteUpdate(username, note);
        return updatedNote;
    }

    @Override
    public void deleteNote(Long noteId, String username) {
        Note note = noteRepository.findById(noteId)
                        .orElseThrow(() -> new RuntimeException("Note not found"));
        auditLogService.logNoteDeletion(username, noteId);
        noteRepository.delete(note);
    }
}
