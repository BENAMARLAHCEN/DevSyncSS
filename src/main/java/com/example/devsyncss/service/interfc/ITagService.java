package com.example.devsyncss.service.interfc;

import com.example.devsyncss.entities.Tag;

import java.util.List;

public interface ITagService {
    List<Tag> getAllTags();
    Tag getTagById(Long id);
}
