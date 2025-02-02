package com.pt.productinventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class LockVersion {

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
