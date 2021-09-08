package com.learning.student.studentservice.persistance.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table(name = "student")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "json", typeClass = JsonType.class)
public class StudentDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private String studentJson;
    @Column(name = "valid")
    private boolean isValid;
}
