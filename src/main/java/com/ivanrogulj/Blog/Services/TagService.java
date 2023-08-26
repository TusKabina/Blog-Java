package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.Entities.Tag;
import com.ivanrogulj.Blog.Repositories.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag updateTag(Long tagId, Tag tag) {
        if (tagRepository.existsById(tagId)) {
            tag.setId(tagId);
            return tagRepository.save(tag);
        }
        throw new EntityNotFoundException("Tag not found");
    }

    public void deleteTag(Long tagId) {
        if (tagRepository.existsById(tagId)) {
            tagRepository.deleteById(tagId);
        } else {
            throw new EntityNotFoundException("Tag not found");
        }
    }
}