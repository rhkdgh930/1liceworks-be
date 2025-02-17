package com.elice.iliceworksbe.team.entity;

import com.elice.iliceworksbe.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_TYPE")
@AuditOverride(forClass = BaseEntity.class)
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
