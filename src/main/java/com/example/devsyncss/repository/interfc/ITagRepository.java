package com.example.devsyncss.repository.interfc;

import com.example.devsyncss.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface ITagRepository {
    void createTag(Tag tag);
    void deleteTag(Long id);
    List<Tag> getAllTags();
    Tag getTag(Long id);
    Optional<Tag> getTag(String name);
    void updateTag(Tag tag);
}
