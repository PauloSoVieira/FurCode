package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "shelter_theme")
public class ShelterTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accent_color")
    private String accentColor;

    @Column(name = "background_color")
    private String backgroundColor;

    @Column(name = "title_font_name")
    private String titleFontName;

}
