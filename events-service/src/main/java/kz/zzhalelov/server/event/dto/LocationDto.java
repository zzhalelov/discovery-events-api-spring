package kz.zzhalelov.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationDto {
    private Double lat;
    private Double lon;
}