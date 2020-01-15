package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.ContactRecord;
import com.jingliang.mall.repository.ContactRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.jingliang.mall.service.ContactRecordService;

import java.util.List;

/**
 * 商户联系记录ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
@Service
@Slf4j
public class ContactRecordServiceImpl implements ContactRecordService {

	private final ContactRecordRepository contactRecordRepository;

	public ContactRecordServiceImpl (ContactRecordRepository contactRecordRepository) {
		this.contactRecordRepository = contactRecordRepository;
	}

	@Override
	public ContactRecord save(ContactRecord contactRecord) {
		return contactRecordRepository.save(contactRecord);
	}

	@Override
	public List<ContactRecord> findByBuyerId(Long buyerId) {
		return contactRecordRepository.findAllByBuyerIdAndIsAvailable(buyerId,true);
	}
}