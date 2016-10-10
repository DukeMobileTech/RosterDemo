package org.chpir.android.roster.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Centers")
public class Center extends Model {
    @Column(name = "Identifier")
    private String identifier;
    @Column(name = "Name")
    private String name;

    public Center() {
        super();
    }

    public Center(String identifier, String name) {
        super();
        this.identifier = identifier;
        this.name = name;
    }

    public static List<Center> findAll() {
        return new Select().from(Center.class).orderBy("Name ASC").execute();
    }

    public static Center findByIdentifier(String identifier) {
        return new Select().from(Center.class).where("Identifier = ?", identifier).executeSingle();
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public List<Participant> participants() {
        return new Select().from(Participant.class).where("Center = ?", getId()).execute();
    }
}