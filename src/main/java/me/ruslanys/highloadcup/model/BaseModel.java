package me.ruslanys.highloadcup.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Data
@NoArgsConstructor

public abstract class BaseModel implements Serializable {

    protected int id;

    BaseModel(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
