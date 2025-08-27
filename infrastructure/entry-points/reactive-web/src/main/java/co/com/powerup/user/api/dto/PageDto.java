package co.com.powerup.user.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public record PageDto(int pageNumber,
                      int pageSize,
                      long totalElements,
                      int totalPages,
                      boolean hasNext,
                      boolean hasPrevious) {


}
