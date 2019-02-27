package cc.lovezhy.netease.sale.service;

import cc.lovezhy.netease.sale.common.UserInfo;
import cc.lovezhy.netease.sale.entity.Good;
import cc.lovezhy.netease.sale.entity.TransRecord;
import cc.lovezhy.netease.sale.model.BuyingGoodModel;
import cc.lovezhy.netease.sale.model.RecordModel;
import cc.lovezhy.netease.sale.repository.TransRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransRecordService {

    private TransRecordRepository transRecordRepository;

    private GoodService goodService;

    @Autowired
    public TransRecordService(TransRecordRepository transRecordRepository, GoodService goodService) {
        this.transRecordRepository = transRecordRepository;
        this.goodService = goodService;
    }

    @Transactional
    public boolean insert(UserInfo userInfo, List<BuyingGoodModel> buyingGoodModels) {
        buyingGoodModels.forEach(buyingGoodModel -> {
            TransRecord transRecord = new TransRecord();
            transRecord.setGoodId(buyingGoodModel.getId());
            transRecord.setUserId(userInfo.getUserId());
            transRecord.setNum(buyingGoodModel.getNumber());

            Good good = goodService.queryGoodById(buyingGoodModel.getId());
            transRecord.setPrice(good.getPrice());

            transRecord.setCreateTime(new Date());

            transRecordRepository.save(transRecord);
        });
        return true;
    }

    public List<RecordModel> queryAllRecord(UserInfo userInfo) {
        List<TransRecord> transRecordList = transRecordRepository.findAllByUserId(userInfo.getUserId());
        return transRecordList.stream().map(transRecord -> {
            Good good = goodService.queryGoodById(transRecord.getGoodId());
            RecordModel recordModel = new RecordModel();
            recordModel.setId(transRecord.getGoodId());
            recordModel.setTitle(good.getTitle());
            recordModel.setImage(good.getImage());
            recordModel.setNumber(transRecord.getNum());
            recordModel.setPrice(transRecord.getPrice());
            recordModel.setCreateTime(transRecord.getCreateTime());
            return recordModel;
        }).collect(Collectors.toList());
    }


}
