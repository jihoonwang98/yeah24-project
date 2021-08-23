package com.prac.domain.etc;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public abstract class BaseEntity extends AuditingEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
