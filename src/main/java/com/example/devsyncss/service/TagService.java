package com.example.devsyncss.service;

import com.example.devsyncss.entities.Tag;
import com.example.devsyncss.repository.TagRepository;
import com.example.devsyncss.repository.interfc.ITagRepository;
import com.example.devsyncss.service.interfc.ITagService;

import java.util.List;

public class TagService implements ITagService {
    public final ITagRepository tagRepository;

    public TagService() {
        this.tagRepository = new TagRepository();
        if (tagRepository.getAllTags().isEmpty())
        {
            tagRepository.createTag(new Tag("Management"));
            tagRepository.createTag(new Tag("Scheduling"));
            tagRepository.createTag(new Tag("Tracking"));
            tagRepository.createTag(new Tag("Assignment"));
            tagRepository.createTag(new Tag("Prioritization"));
            tagRepository.createTag(new Tag("Delegation"));
            tagRepository.createTag(new Tag("Progress Tracking"));
            tagRepository.createTag(new Tag("Completion"));
            tagRepository.createTag(new Tag("Reporting"));
        }
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.getTag(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

}
