package cz.mendelu.ea.domain.track;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.mendelu.ea.domain.favorite.Favorite;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "track")
@Data
public class Track {

    @Id
    @Column(name = "track_id")
    @NotEmpty
    private String trackId;

    @NotEmpty
    private String artists;

    @NotEmpty
    private String albumName;

    @NotEmpty
    private String trackName;

    @NotNull
    private Integer popularity;

    @NotNull
    private Long durationMs;

    @NotNull
    private Boolean explicit;

    @NotNull
    private Double danceability;

    @NotNull
    private Double energy;

    @NotNull
    private Integer key;

    @NotNull
    private Double loudness;

    @NotNull
    private Integer mode;

    @NotNull
    private Double speechiness;

    @NotNull
    private Double acousticness;

    @NotNull
    private Double instrumentalness;

    @NotNull
    private Double liveness;

    @NotNull
    private Double valence;

    @NotNull
    private Double tempo;

    @NotNull
    private Integer timeSignature;

    @NotEmpty
    private String trackGenre;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Favorite> favorites;
} 