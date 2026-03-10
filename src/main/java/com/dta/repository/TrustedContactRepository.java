package com.dta.repository;

import com.dta.entity.TrustedContact;

public interface TrustedContactRepository {
    // TODO [Timmy]: Load primary trusted contact for active user, with fallback by relation type.
    TrustedContact findPrimary();
    // TODO [Timmy]: Persist trusted-contact relationships with encryption at rest where needed.
    TrustedContact save(TrustedContact contact);
}
