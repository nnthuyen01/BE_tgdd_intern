package com.group04.tgdd.service.iplm;


import com.group04.tgdd.dto.CurrentEventResp;
import com.group04.tgdd.dto.EventReq;
import com.group04.tgdd.dto.ProductCurrentEventResp;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.model.Event;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductOption;
import com.group04.tgdd.repository.EventRepo;
import com.group04.tgdd.repository.ProductOptionRepo;
import com.group04.tgdd.repository.ProductRepo;
import com.group04.tgdd.service.EventService;
import com.group04.tgdd.utils.MoneyConvert;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional
@RequiredArgsConstructor
public class EventIplm implements EventService {

    private final EventRepo eventRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final ProductOptionRepo productOptionRepo;

    @Override
    public Event save(EventReq eventReq) {

        List<Event> checkList = eventRepo.findEventByName(eventReq.getName());
        if(checkList.size() > 0){
            throw new AppException(400,"Event already exists");
        }
        String name = eventReq.getName();
        String banner = eventReq.getBanner();
        LocalDateTime expireTime = eventReq.getExpireTime();
        List<Long> id_list = eventReq.getProductIdList();
        List<Product> productList = getProductList(id_list);

        Event event = new Event();
        event.setName(name);
        event.setBanner(banner);
        event.setColor(eventReq.getColor());
        event.setAward(eventReq.getAward());
        event.setExpireTime(expireTime);
        event.setProductList(productList);

        return eventRepo.save(event);
    }
    private List<Product> getProductList(List<Long> product_id_list){
        List<Product> productList = new ArrayList<>();
        for(Long id: product_id_list){
            try{
                Product product = productRepo.findById(id).get();
                if(product != null){
                    productList.add(product);
                }
            }
            catch (Exception e){

            }

        }
        return productList;
    }
    @Override
    public List<Event> get(String name) {
        List<Event> events = eventRepo.findEventByName(name);
        for(Event event: events){
            event.setProductList(eventRepo.findEventProductList(name));
        }

        return events;
    }

    @Transactional
    @Override
    public void delete(String name) {
        eventRepo.deleteEventByName(name);
    }

    @Override
    public Event update(EventReq eventReq) {
        Long id = eventReq.getId();
        String name = eventReq.getName();
        String banner = eventReq.getBanner();
        LocalDateTime expireTime = eventReq.getExpireTime();
        List<Long> id_list = eventReq.getProductIdList();
        List<Product> productList = getProductList(id_list);
        Event updatedEvent = new Event();
        if(id != 0){
            updatedEvent = eventRepo.findById(id).get();
        }

        if (updatedEvent != null) {

            if (name == null && name.trim().equals(""))
                updatedEvent.setName(name.trim().replaceAll("  "," "));
            else
                updatedEvent.setName(name);
            if (banner == null && banner.trim().equals(""))
                updatedEvent.setBanner(banner.trim().replaceAll("  "," "));
            else
                updatedEvent.setBanner(banner);
            if (expireTime == null && expireTime.toString().trim().equals(""))
                updatedEvent.setExpireTime(null);
            else
                updatedEvent.setExpireTime(expireTime);

            updatedEvent.setProductList(productList);

            return eventRepo.save(updatedEvent);

        }
        else
            return null;
    }

    @Override
    public List<CurrentEventResp> getCurrentEvent() {
        List<Event> events = eventRepo.findEventByExpireTimeGreaterThan(LocalDateTime.now(TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId()));
        List<CurrentEventResp> currentEventRespList = new ArrayList<>();
        for(Event event: events){
            CurrentEventResp eventResp = modelMapper.map(event,CurrentEventResp.class);
            List<ProductCurrentEventResp> productCurrentEventResps = eventResp.getProductList();
            for(Product product: event.getProductList()){
                ProductCurrentEventResp productCurrentEventResp = productCurrentEventResps
                        .stream().filter(productft -> productft.getId().equals(product.getId())).findFirst()
                                .orElseThrow(()-> new NotFoundException("Product not found"));
                productCurrentEventResp.setPrice(product.getProductOptions().get(0).getPrice());
                productCurrentEventResp.setMarketPrice(
                        MoneyConvert.calculaterPrice(productCurrentEventResp.getPrice(),product.getProductOptions().get(0).getPromotion()));
                productCurrentEventResp.setPromotion(product.getProductOptions().get(0).getPromotion());
            }
            currentEventRespList.add(eventResp);
        }
        return currentEventRespList;
    }



    @Override
    public Event addProductToEvent(Long eventId, Long productId) {
        /*Find product and Event*/
       Product product = productRepo.findById(productId).get();

       if(product == null){
           return null;
       }
       Event updatedEvent = eventRepo.findById(eventId).get();

        if(updatedEvent == null){
            return null;
        }

       /*Check if the event already contains the product*/
        Boolean existed = false;
        for(Product item: updatedEvent.getProductList()){
            if(item.getId() == productId){
                existed = true;
            }
        }
        /*If not in the list*/
       if(existed == false){
            updatedEvent.getProductList().add(product);
       }

       return eventRepo.save(updatedEvent);

    }
}
