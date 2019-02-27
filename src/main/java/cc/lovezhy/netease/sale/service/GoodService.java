package cc.lovezhy.netease.sale.service;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.entity.Good;
import cc.lovezhy.netease.sale.entity.TransRecord;
import cc.lovezhy.netease.sale.exception.HttpException;
import cc.lovezhy.netease.sale.exception.ResponseCodeEnum;
import cc.lovezhy.netease.sale.model.GoodModel;
import cc.lovezhy.netease.sale.model.SellerGoodModel;
import cc.lovezhy.netease.sale.repository.GoodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class GoodService {

    private GoodRepository goodRepository;

    @Autowired
    private TransRecordService transRecordService;

    @Autowired
    public void setGoodRepository(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    public List<SellerGoodModel> sellerListAll(UserInfo userInfo) {
         return goodRepository.findAll().stream().map(good -> {
             SellerGoodModel sellerGoodModel = new SellerGoodModel();
             sellerGoodModel.setId(good.getId());
             sellerGoodModel.setImage(good.getImage());
             sellerGoodModel.setPrice(good.getPrice());
             sellerGoodModel.setTitle(good.getTitle());
             sellerGoodModel.setHasBeenSold(transRecordService.checkHasBeenSold(good.getId()));
             return sellerGoodModel;
         }).collect(Collectors.toList());
    }

    public List<GoodModel> list(UserInfo userInfo, Integer type) {
        List<GoodModel> goodModels = goodRepository.findAll().stream().map(good -> {
            GoodModel goodModel = new GoodModel();
            BeanUtils.copyProperties(good, goodModel);
            TransRecord transRecord = transRecordService.queryRecordByGoodId(userInfo, good.getId());
            if (Objects.nonNull(transRecord)) {
                goodModel.setHasBuy(true);
            } else {
                goodModel.setHasBuy(false);
            }
            return goodModel;
        }).collect(Collectors.toList());
        if (type == 1) {
            goodModels = goodModels.stream().filter(goodModel -> {
                return !goodModel.getHasBuy();
            }).collect(Collectors.toList());
        }
        return goodModels;

    }

    public void insert(UserInfo userInfo, Good good) {
        good.setUserId(userInfo.getUserId());
        goodRepository.save(good);
    }

    @Transactional
    public void update(UserInfo userInfo, Good newGood) {
        Good good = goodRepository.getOne(newGood.getId());
        BeanUtils.copyProperties(newGood, good);
    }

    public Good queryGoodById(Integer goodId) {
        Optional<Good> good = goodRepository.findById(goodId);
        if (!good.isPresent()) {
            throw HttpException.create(ResponseCodeEnum.GOOD_NOT_FOUND);
        }
        return good.get();
    }


    public GoodModel queryGoodModel(UserInfo userInfo, Integer goodId) {
        Optional<Good> good = goodRepository.findById(goodId);
        if (!good.isPresent()) {
            throw HttpException.create(ResponseCodeEnum.GOOD_NOT_FOUND);
        }
        GoodModel goodModel = new GoodModel();
        BeanUtils.copyProperties(good.get(), goodModel);

        TransRecord transRecord = transRecordService.queryRecordByGoodId(userInfo, goodId);

        if (Objects.nonNull(transRecord)) {
            goodModel.setHasBuy(true);
            goodModel.setBuyNum(transRecord.getNum());
            goodModel.setBuyPrice(transRecord.getPrice());
        } else {
            goodModel.setHasBuy(false);
        }
        return goodModel;
    }

    public void deleteById(Integer id) {
        goodRepository.deleteById(id);
    }
}
