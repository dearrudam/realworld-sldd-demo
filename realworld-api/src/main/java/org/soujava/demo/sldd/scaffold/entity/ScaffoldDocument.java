package org.soujava.demo.sldd.scaffold.entity;

import jakarta.nosql.Entity;
import jakarta.nosql.Id;

/**
 * Minimal non-domain document used only to keep the JNoSQL MongoDB scaffold buildable.
 */
@Entity
public record ScaffoldDocument(@Id String id) {
}
