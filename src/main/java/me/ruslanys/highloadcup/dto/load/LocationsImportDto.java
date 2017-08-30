package me.ruslanys.highloadcup.dto.load;

import lombok.Data;
import me.ruslanys.highloadcup.model.Location;

import java.util.List;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Data
public class LocationsImportDto {

    private List<Location> locations;

}
