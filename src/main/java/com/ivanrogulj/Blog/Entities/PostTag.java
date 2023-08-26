package com.ivanrogulj.Blog.Entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "post_tag")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}