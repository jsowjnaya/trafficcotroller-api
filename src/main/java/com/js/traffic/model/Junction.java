package com.js.traffic.model;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "JUNCTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Junction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JID")
    private Integer jid;
    @Size(max = 20, message = "Title must be maximum 20 characters")
    @Column(name = "TITLE", nullable = false, length = 10)
    private String title;
    
    @NotBlank(message = "Command cannot be blank")
    @Pattern(regexp = "(?i)PAUSE|RESUME",
             message = "Command must be PAUSE or RESUME")
    @Column(name = "CMD", nullable = false, length = 10)
    private String cmd;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
