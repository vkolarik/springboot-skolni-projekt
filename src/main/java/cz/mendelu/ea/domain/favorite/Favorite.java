package cz.mendelu.ea.domain.favorite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cz.mendelu.ea.domain.track.Track;
import cz.mendelu.ea.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
@Data
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonBackReference
    @NotNull
    private Track track;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;

    private String comment;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private LocalDateTime lastPlayed = LocalDateTime.now();
} 