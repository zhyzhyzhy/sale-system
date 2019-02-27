package cc.lovezhy.netease.sale.service;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.entity.Good;
import cc.lovezhy.netease.sale.exception.HttpException;
import cc.lovezhy.netease.sale.exception.ResponseCodeEnum;
import cc.lovezhy.netease.sale.model.GoodModel;
import cc.lovezhy.netease.sale.repository.GoodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GoodService {

    private GoodRepository goodRepository;

    @Autowired
    public GoodService(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    public List<Good> listAll() {
        return goodRepository.findAll();
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

    public GoodModel queryGoodModel(UserInfo userInfo, Integer goodId) {
        Optional<Good> good = goodRepository.findById(goodId);
        if (!good.isPresent()) {
            throw HttpException.create(ResponseCodeEnum.GOOD_NOT_FOUND);
        }
        GoodModel goodModel = new GoodModel();
        BeanUtils.copyProperties(good.get(), goodModel);
        goodModel.setHasBuy(false);
        return goodModel;
    }
}
