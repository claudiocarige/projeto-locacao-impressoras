package br.com.copyimagem.mspersistence.core.domain;

import br.com.copyimagem.mspersistence.core.exceptions.DomainException;


public interface AssertionConcern {

    default <T> void assertionArgumentNotNull( T val, String aMessage) {
        if (val == null) {
            throw DomainException.with(aMessage);
        }
    }

    default void assertionArgumentNotEmpty( String val, String aMessage) {
        if (val == null || val.isBlank()) {
            throw DomainException.with(aMessage);
        }
    }

    default void assertionArgumentLength( String val, int length, String aMessage) {
        if (val == null || val.length() != length) {
            throw DomainException.with(aMessage);
        }
    }

    default void assertionConditionTrue( Boolean val, String aMessage) {
        if (Boolean.FALSE.equals(val)) {
            throw DomainException.with(aMessage);
        }
    }

    default void assertionArgumentMaxLength( String val, int length, String aMessage) {
        if (val != null && val.length() > length) {
            throw DomainException.with(aMessage);
        }
    }
}
