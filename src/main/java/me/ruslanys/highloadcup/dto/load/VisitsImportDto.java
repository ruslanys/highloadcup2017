package me.ruslanys.highloadcup.dto.load;

import lombok.Data;
import me.ruslanys.highloadcup.model.Visit;

import java.util.List;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Data
public class VisitsImportDto {

    private List<Visit> visits;

}
