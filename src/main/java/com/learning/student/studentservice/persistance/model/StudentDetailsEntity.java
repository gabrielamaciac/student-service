package com.learning.student.studentservice.persistance.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "student")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class StudentDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String studentJson;
    @Column(name = "valid")
    private boolean isValid;
}
