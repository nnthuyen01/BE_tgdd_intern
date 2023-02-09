package com.group04.tgdd.service.iplm;

import com.group04.tgdd.model.Topic;
import com.group04.tgdd.repository.TopicRepo;
import com.group04.tgdd.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicIplm implements TopicService {

    private final TopicRepo topicRepo;

    @Override
    public Topic findById(Long id) {
        var topic = topicRepo.findById(id);
        return topic.orElse(null);
    }

    @Override
    public List<Topic> findAll() {
        return topicRepo.findAll();
    }

    @Override
    public Topic save(Topic topic) {
        return topicRepo.save(topic);
    }

    @Override
    public Topic updateTopic(Topic topic) {
        Topic topicUpdate = findById(topic.getId_topic());
        if (topicUpdate!=null) {
            topicUpdate.setName(topic.getName());
            topicUpdate.setContent(topic.getContent());
            return topicUpdate;
        }
        else return null;
    }

    @Override
    public Boolean deleteTopic(Long topicId) {
        boolean check = topicRepo.existsById(topicId);
        if (check){
            topicRepo.deleteById(topicId);
            return true;
        }else {
            return false;
        }
    }
}
