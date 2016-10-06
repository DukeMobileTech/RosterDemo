package org.chpir.android.roster.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Centers")
public class Center extends Model{
    @Column(name = "Identifier")
    private String identifier;
    @Column(name = "Name")
    private String name;

    public Center(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}