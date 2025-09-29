package cz.mendelu.ea.domain.track;

import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
@Tag(name = "Track Management", description = "APIs for managing music tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    @Operation(summary = "Get all tracks", description = "Retrieves a list of all tracks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of tracks retrieved successfully")
    })
    public ArrayResponse<Track> getAllTracks() {
        return ArrayResponse.of(trackService.getAllTracks(), track -> track);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get track by ID", description = "Retrieves a track by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Track found"),
        @ApiResponse(responseCode = "404", description = "Track not found")
    })
    public ObjectResponse<Track> getTrackById(
            @Parameter(description = "ID of the track to retrieve") @PathVariable String id) {
        return ObjectResponse.of(trackService.getTrackById(id), track -> track);
    }
} 