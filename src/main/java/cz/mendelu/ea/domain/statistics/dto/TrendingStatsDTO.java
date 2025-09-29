package cz.mendelu.ea.domain.statistics.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class TrendingStatsDTO {
    private List<TrackWithCount> recentlyPopularTracks;
    private Map<String, Long> emergingGenres;
    private Map<String, Long> userActivityByCountry;

    @Data
    public static class TrackWithCount {
        private String id;
        private String name;
        private String artist;
        private String genre;
        private Integer popularity;
        private Long count;
    }
} 