package com.js.traffic.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LIGHTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lights {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Junction ID cannot be null")
	@Min(value = 1, message = "Junction ID must be greater than 0")
	@Column(name = "JID", nullable = false)
	private Integer jid;

	@NotBlank(message = "Direction cannot be blank")
	@Size(max = 5, message = "Direction must be maximum 5 characters")
	@Column(name = "DIRECTION", nullable = false, length = 10)
	private String direction;

	@NotBlank(message = "Code cannot be blank")
	@Size(min = 1, max = 2, message = "Code must be 1-2 characters")
	@Column(name = "CODE", nullable = false, length = 2)
	private String code;

	@NotBlank(message = "Color cannot be blank")
	@Pattern(regexp = "(?i)RED|GREEN|YELLOW", message = "Color must be RED, GREEN, or YELLOW")
	@Column(name = "COLOR", nullable = false, length = 10)
	private String color;

	@NotBlank(message = "Status cannot be blank")
	@Pattern(regexp = "(?i)(NS|EW)-(RED|GREEN|YELLOW)", message = "Status must be in format NS-GREEN, EW-RED, etc.")
	@Column(name = "CSTATUS", nullable = false, length = 10)
	private String cstatus;
	@NotNull(message = "Duration cannot be null")
	@Min(value = 1, message = "Duration must be greater than 0")
	@Max(value = 30, message = "Duration cannot exceed 30 seconds")
	@Column(name = "DURATION", nullable = false)
	private Integer duration;

	@Column(name = "CREATED_AT", updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
