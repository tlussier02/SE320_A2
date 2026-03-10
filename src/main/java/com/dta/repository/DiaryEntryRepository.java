package com.dta.repository;

import com.dta.entity.DiaryEntry;
import java.util.List;

public interface DiaryEntryRepository {
    // TODO [Timmy]: Return entries per user with filters (range/status/distortion) and paging.
    List<DiaryEntry> findAll();
    // TODO [Timmy]: Persist diary entry and attach distortion classifications.
    DiaryEntry save(DiaryEntry entry);
    // TODO [Timmy]: Enforce owner checks before deleting the diary entry.
    void deleteById(Long id);
}
