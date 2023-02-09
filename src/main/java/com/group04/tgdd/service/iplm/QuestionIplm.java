package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.QuestionReq;
import com.group04.tgdd.dto.QuestionResp;
import com.group04.tgdd.dto.UserResp;
import com.group04.tgdd.model.Question;

import com.group04.tgdd.model.Topic;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.QuestionRepo;
import com.group04.tgdd.repository.TopicRepo;
import com.group04.tgdd.repository.UsersRepo;
import com.group04.tgdd.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionIplm implements QuestionService {
    private final QuestionRepo questionRepo;
    private final UsersRepo usersRepo;
    private final TopicRepo topicRepo;
    @Override
    public QuestionResp findById(Long id) {
        Optional<Question> question = questionRepo.findById(id);
        if (question.isPresent()){
            QuestionResp questionResp = new QuestionResp();

            Users users = question.get().getUsers();
            UserResp userResp = new UserResp();
            userResp.setId(users.getId());
            userResp.setEmail(users.getEmail());
            userResp.setGender(users.getGender());
            userResp.setName(users.getName());
            userResp.setPhone(users.getPhone());
            userResp.setAddresses(users.getAddresses());
            questionResp.setId(question.get().getId());
            questionResp.setUser(userResp);
            questionResp.setContent(question.get().getContent());
            questionResp.setImage(question.get().getImage());
            questionResp.setId(question.get().getId());
            questionResp.setTopic(question.get().getTopic());
            return questionResp;
        }
        return null;
    }

    @Override
    public List<Question> findAll() {
        return questionRepo.findAll();
    }

    @Override
    public Question save(QuestionReq questionReq) {
        boolean checkUser = usersRepo.existsById(questionReq.getUserId());
        boolean checkTopic = topicRepo.existsById(questionReq.getTopicId());
        if (checkUser && checkTopic) {
            Question question = new Question();
            question.setContent(questionReq.getContent());
            Users users = usersRepo.getReferenceById(questionReq.getUserId());
            Topic topic = topicRepo.getReferenceById(questionReq.getTopicId());
            question.setTopic(topic);
            question.setUsers(users);
            return  questionRepo.save(question);
        }
        else {
            return null;
        }

    }

    @Override
    public Boolean deleteQuestion(Long questionId) {
        boolean check = questionRepo.existsById(questionId);
        if (check){
            questionRepo.deleteById(questionId);
            return true;
        }else {
            return false;
        }
    }
}