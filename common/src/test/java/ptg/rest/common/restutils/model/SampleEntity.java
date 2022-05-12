package ptg.rest.common.restutils.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sample_entity")
public class SampleEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String stringField;

    private LocalDate dateField;

    private Integer intField;

    private Long flagsField;

    @Enumerated(EnumType.STRING)
    private SampleEnum enumField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public LocalDate getDateField() {
        return dateField;
    }

    public void setDateField(LocalDate dateField) {
        this.dateField = dateField;
    }

    public Integer getIntField() {
        return intField;
    }

    public void setIntField(Integer intField) {
        this.intField = intField;
    }

    public Long getFlagsField() {
        return flagsField;
    }

    public void setFlagsField(Long flagsField) {
        this.flagsField = flagsField;
    }

    public SampleEnum getEnumField() {
        return enumField;
    }

    public void setEnumField(SampleEnum enumField) {
        this.enumField = enumField;
    }

    public enum SampleEnum {
        FIRST, SECOND
    }

    public enum FlagsEnum {
        FLAG_ONE, FLAG_TWO
    }
}
