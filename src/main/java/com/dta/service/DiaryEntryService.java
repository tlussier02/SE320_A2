package com.dta.service;

import com.dta.entity.DiaryEntry;
import com.dta.entity.User;
import com.dta.repository.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiaryEntryService {

    @Autowired
    private DiaryEntryRepository diaryEntryRepository;

    // Logic to save a new entry for a specific user
    public DiaryEntry createEntry(DiaryEntry entry) {
        return diaryEntryRepository.save(entry);
    }

    // Logic to get all entries belonging to one user
    public List<DiaryEntry> getUserEntries(User user) {
        return diaryEntryRepository.findByUser(user, null).getContent();
    }

    // Logic to delete an entry only if it belongs to the user
    public void deleteEntry(Long entryId, Long userId) {
        diaryEntryRepository.findByIdAndUserId(entryId, userId)
            .ifPresent(entry -> diaryEntryRepository.delete(entry));
    }
}
