package cz.mendelu.ea.domain.favorite;

import cz.mendelu.ea.utils.exceptions.NotFoundException;
import cz.mendelu.ea.utils.response.ArrayResponse;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@Tag(name = "Favorite Management", description = "APIs for managing user favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("")
    @Operation(
        summary = "Create a new favorite",
        description = "Creates a new favorite for a user and track",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Create Favorite",
                        value = """
                        {
                            "userId": 1,
                            "trackId": "track123",
                            "rating": 5,
                            "comment": "Great song!",
                            "isPublic": true
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Favorite created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ObjectResponse<Favorite> createFavorite(@Valid @RequestBody CreateFavoriteRequest request) {
        return ObjectResponse.of(
                favoriteService.createFavorite(
                    request.getUserId(),
                    request.getTrackId(),
                    request.getRating(),
                    request.getComment(),
                    request.getIsPublic()
                ),
                favorite -> favorite
        );
    }

    @GetMapping("")
    @Operation(summary = "Get all favorites", description = "Retrieves a list of all favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of favorites retrieved successfully")
    })
    public ArrayResponse<Favorite> getAllFavorites() {
        return ArrayResponse.of(favoriteService.getAllFavorites(), favorite -> favorite);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get favorite by ID",
        description = "Retrieves a favorite by its ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Favorite found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Favorite.class),
                    examples = {
                        @ExampleObject(
                            name = "Favorite Response",
                            value = """
                            {
                                "id": 1,
                                "user": {
                                    "id": 1,
                                    "username": "john_doe"
                                },
                                "track": {
                                    "trackId": "track123",
                                    "name": "Example Song"
                                },
                                "rating": 5,
                                "comment": "Great song!",
                                "isPublic": true
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "404", description = "Favorite not found")
        }
    )
    public ObjectResponse<Favorite> getFavoriteById(
            @Parameter(description = "ID of the favorite to retrieve", example = "1") @PathVariable Long id) {
        return ObjectResponse.of(favoriteService.getFavoriteById(id), favorite -> favorite);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update favorite",
        description = "Updates an existing favorite's details",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Update Favorite",
                        value = """
                        {
                            "rating": 4,
                            "comment": "Updated comment",
                            "isPublic": false
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorite updated successfully"),
        @ApiResponse(responseCode = "404", description = "Favorite not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ObjectResponse<Favorite> updateFavorite(
            @Parameter(description = "ID of the favorite to update", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateFavoriteRequest request) {
        Favorite favoriteToUpdate = favoriteService.getFavoriteById(id);
        favoriteToUpdate.setRating(request.getRating());
        favoriteToUpdate.setComment(request.getComment());
        if (request.getIsPublic() != null) {
            favoriteToUpdate.setIsPublic(request.getIsPublic());
        }
        return ObjectResponse.of(favoriteService.updateFavorite(favoriteToUpdate), fav -> fav);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete favorite", description = "Deletes a favorite by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Favorite deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Favorite not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(
            @Parameter(description = "ID of the favorite to delete", example = "1") @PathVariable Long id) {
        try {
            favoriteService.deleteFavorite(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found");
        }
    }
} 