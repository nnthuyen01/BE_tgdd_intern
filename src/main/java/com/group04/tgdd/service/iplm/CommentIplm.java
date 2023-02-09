package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.*;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.model.*;
import com.group04.tgdd.repository.CommentRepo;
import com.group04.tgdd.repository.ProductRepo;
import com.group04.tgdd.repository.UsersRepo;
import com.group04.tgdd.service.CommentService;
import com.group04.tgdd.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.DataOutput;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentIplm implements CommentService {
    private final CommentRepo commentRepo;
    private final UsersRepo usersRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;


    @Override
    public Comment findBy_Id(Long id) {
        Optional<Comment> comment = commentRepo.findById(id);
        return comment.orElse(null);
    }

    @Override
    public CommentResp findById(Long id) {
        Optional<Comment> comment = commentRepo.findById(id);
        if (comment.isPresent()) {
//            CommentResp commentResp = new CommentResp();

            Users users = comment.get().getUsers();
            //UserResp userResp = new UserResp();
            UserResp userResp = modelMapper.map(users,UserResp.class);
//            userResp.setId(users.getId());
//            userResp.setEmail(users.getEmail());
//            userResp.setGender(users.getGender());
//            userResp.setName(users.getName());
//            userResp.setPhone(users.getPhone());
//            userResp.setAddresses(users.getAddresses());

            Product product = comment.get().getProducts();
//            ProductDetailResp productResp = new ProductDetailResp();
            ProductDetailResp productResp = modelMapper.map(product,ProductDetailResp.class);
//            productResp.setId(product.getId());
//            productResp.setName(product.getName());
//            productResp.setCategory(product.getCategory());
//            productResp.setDescription(product.getDescription());
//            productResp.setManufacturer(product.getManufacturer());
//            productResp.setSubcategory(product.getSubcategory());

            CommentResp commentResp = modelMapper.map(comment,CommentResp.class);
//            commentResp.setId(comment.get().getId());
//            commentResp.setContent(comment.get().getContent());
//            commentResp.setCreate_time(comment.get().getCreateTime());
            commentResp.setProduct(productResp);
            commentResp.setUser(userResp);
//            commentResp.setRate(comment.get().getRate());
            return commentResp;
        }
        return null;
    }

    @Override
    public List<Comment> getAllComment(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Product product = productRepo.getReferenceById(productId);
        List<Comment> comments = commentRepo.findCommentByProducts(product, pageable);
        return comments;
    }
    @Override
    public List<Comment> getAllRateComment(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Product product = productRepo.findById(productId).orElseThrow(()->new NotFoundException("Product not found"));
        if(product.getCountRate()>0) {
            List<Comment> comments = commentRepo.findRateCommentByProducts(product ,pageable);
            return comments;
        }
        else{
            return null;
        }
    }
    @Override
    public List<Comment> getAllNonRateComment(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Product product = productRepo.getReferenceById(productId);
        List<Comment> comments = commentRepo.findNonRateCommentByProducts(product, pageable);
        return comments;
    }

    @Override
    public Comment saveComment(CommentReq commentReq) {
        Long userId = Utils.getIdCurrentUser();
        boolean check = usersRepo.existsById(userId) && productRepo.existsById(commentReq.getProductId());

        if (check) {
            if ( commentReq.getRate() == 0) {
                Comment comment = modelMapper.map(commentReq, Comment.class);
                Users users = usersRepo.getReferenceById(userId);
                Product product = productRepo.findById(commentReq.getProductId()).orElse(null);
                comment.setUsers(users);
                comment.setProducts(product);
                comment.setNumReply(0L);
                comment.setCreateTime(new Date());

                return commentRepo.save(comment);
            }
            else {
                Comment comment = modelMapper.map(commentReq, Comment.class);
                Users users = usersRepo.getReferenceById(userId);
                Product product = productRepo.findById(commentReq.getProductId()).orElse(null);
                comment.setUsers(users);
                comment.setProducts(product);
                comment.setNumReply(0L);
                comment.setCreateTime(new Date());
                product.setRate(rateOfProduct(product.getRate(), product.getCountRate(), commentReq.getRate()));
                product.increaseCountRate();
                return commentRepo.save(comment);
            }
        } else {
            throw new AppException(404, "Product or Comment not exits.");
        }
    }
    @Override
    public Comment updateComment(CommentReq commentReq) {
        Comment commentUpdate = findBy_Id(commentReq.getId());
        if (commentUpdate != null) {
            if (commentUpdate.getRate()!=0) {
                commentUpdate.setContent(commentReq.getContent());
                if (commentReq.getRate() != 0 && commentUpdate.getRate() != commentReq.getRate()) {
                    Product product = commentUpdate.getProducts();
                    product.setRate(updateRate(product, commentReq.getRate(), commentUpdate.getRate()));
                    commentUpdate.setRate(commentReq.getRate());
                    productRepo.save(product);
                }
            }
            else {
                commentUpdate.setContent(commentReq.getContent());
            }
            return commentUpdate;
        } else throw new AppException(404, "Comment ID not found");
    }

    @Override
    public boolean deleteComment(Long id) {
        Comment comment = commentRepo.findById(id).orElse(null);
        if (comment!=null) {
            Product product = comment.getProducts();
            int countRate = commentRepo.countCommentByProducts(product);
            double ratePrevious = (product.getRate() * countRate - comment.getRate()) / (countRate - 1);
            if (!Double.isNaN(ratePrevious)) {
                product.setRate(ratePrevious);
            } else
                product.setRate(0);
            commentRepo.deleteById(id);
            return true;
        } else {
            throw new AppException(404, "Comment ID not found");
        }
    }

    private double updateRate(Product product, double updateRate, double oldRate) {
        int countRate = commentRepo.countCommentByProducts(product);
        double ratePrevious = (product.getRate() * countRate - oldRate) / (countRate - 1);
        double rate;
        if (!Double.isNaN(ratePrevious)) {
            rate = rateOfProduct(ratePrevious, countRate - 1, updateRate);
        } else
            rate = updateRate;
        return rate;
    }

    private double rateOfProduct(double currentRate, double countRate, double rate) {
        return (currentRate * countRate + rate) / (countRate + 1);
    }

    @Override
    public List<Double> countRateProduct(Long id) {
        List<Double> result = new ArrayList<>();
        List<Comment> comments = commentRepo.findAllRateComment(id);
        int allRate = comments.size();

        List<Comment> rate5 = comments.stream().filter(comment -> comment.getRate() == 5).collect(Collectors.toList());
        int rate5count = rate5.size();
        double result5 = ratePercent(rate5count,allRate);
        result.add(Math.ceil(result5));

        List<Comment> rate4 = comments.stream().filter(comment -> comment.getRate() == 4).collect(Collectors.toList());
        int rate4count = rate4.size();
        double result4 = ratePercent(rate4count,allRate);
        result.add(Math.ceil(result4));

        List<Comment> rate3 = comments.stream().filter(comment -> comment.getRate() == 3).collect(Collectors.toList());
        int rate3count = rate3.size();
        double result3 = ratePercent(rate3count,allRate);
        result.add(Math.ceil(result3));

        List<Comment> rate2 = comments.stream().filter(comment -> comment.getRate() == 2).collect(Collectors.toList());
        int rate2count = rate2.size();
        double result2 = ratePercent(rate2count,allRate);
        result.add(Math.ceil(result2));

        List<Comment> rate1 = comments.stream().filter(comment -> comment.getRate() == 1).collect(Collectors.toList());
        int rate1count = rate1.size();
        double result1 = ratePercent(rate1count,allRate);
        result.add(Math.ceil(result1));

        return result;
    }

    private double ratePercent(int rate, int allRate){
        return (double) rate/allRate *100;
    }
}
