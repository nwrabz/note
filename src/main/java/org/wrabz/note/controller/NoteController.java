package org.wrabz.note.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.wrabz.note.model.Note;
import org.wrabz.note.payload.NoteRequest;
import org.wrabz.note.service.NoteService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public Note createNote(@RequestBody NoteRequest request,
                           @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        System.out.println("USER DETAILS: " + username);

        return noteService.createNoteForUser(username, request.getContent());
    }

    @GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("USER DETAILS: " + username);
        return noteService.showMyNotes(username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@RequestBody String content, @PathVariable Long noteId, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return noteService.updateNoteForUser(noteId,content, username);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        noteService.deleteNote(noteId, username);
    }
}
