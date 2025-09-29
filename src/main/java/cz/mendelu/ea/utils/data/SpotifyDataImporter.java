package cz.mendelu.ea.utils.data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import cz.mendelu.ea.domain.track.Track;
import cz.mendelu.ea.domain.track.TrackRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SpotifyDataImporter {

    private final TrackRepository trackRepository;

    @Autowired
    public SpotifyDataImporter(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @PostConstruct
    public void importData() {
        if (!trackRepository.findAll().isEmpty()) {
            log.info("Data already imported, skipping import");
            return;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("spotify_dataset.csv").getInputStream()))) {
            // Skip header
            reader.readNext();

            String[] line;
            List<Track> tracks = new ArrayList<>();
            while ((line = reader.readNext()) != null) {
                Track track = new Track();
                track.setTrackId(line[1]);
                track.setArtists(line[2]);
                track.setAlbumName(line[3]);
                track.setTrackName(line[4]);
                track.setPopularity(Integer.parseInt(line[5]));
                track.setDurationMs(Long.parseLong(line[6]));
                track.setExplicit(Boolean.parseBoolean(line[7]));
                track.setDanceability(Double.parseDouble(line[8]));
                track.setEnergy(Double.parseDouble(line[9]));
                track.setKey(Integer.parseInt(line[10]));
                track.setLoudness(Double.parseDouble(line[11]));
                track.setMode(Integer.parseInt(line[12]));
                track.setSpeechiness(Double.parseDouble(line[13]));
                track.setAcousticness(Double.parseDouble(line[14]));
                track.setInstrumentalness(Double.parseDouble(line[15]));
                track.setLiveness(Double.parseDouble(line[16]));
                track.setValence(Double.parseDouble(line[17]));
                track.setTempo(Double.parseDouble(line[18]));
                track.setTimeSignature(Integer.parseInt(line[19]));
                track.setTrackGenre(line[20]);

                tracks.add(track);

                // Save in batches of 1000
                if (tracks.size() >= 1000) {
                    trackRepository.saveAll(tracks);
                    tracks.clear();
                }
            }

            // Save remaining tracks
            if (!tracks.isEmpty()) {
                trackRepository.saveAll(tracks);
            }

            log.info("Successfully imported Spotify dataset");
        } catch (IOException | CsvValidationException e) {
            log.error("Error importing Spotify dataset", e);
        }
    }
} 