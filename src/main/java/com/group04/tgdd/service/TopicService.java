package com.group04.tgdd.service;

import com.group04.tgdd.model.Topic;

import java.util.List;

public interface TopicService {
    Topic findById(Long id);
    List<Topic> findAll();
    Topic save(Topic topic);
    Topic updateTopic(Topic topic);
    Boolean deleteTopic(Long topicId);
}
