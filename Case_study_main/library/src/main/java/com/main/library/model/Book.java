package com.main.library.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long isbn;

    @Column(name = "publication_year")
    private Long publicationYear;

    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    Author author;

}
