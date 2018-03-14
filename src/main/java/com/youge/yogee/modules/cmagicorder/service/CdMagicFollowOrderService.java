/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.youge.yogee.modules.cmagicorder.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.youge.yogee.common.persistence.Page;
import com.youge.yogee.common.service.BaseService;
import com.youge.yogee.common.utils.StringUtils;
import com.youge.yogee.common.utils.DateUtils;
import com.youge.yogee.common.utils.IdGen;
import com.youge.yogee.modules.cmagicorder.entity.CdMagicFollowOrder;
import com.youge.yogee.modules.cmagicorder.dao.CdMagicFollowOrderDao;

import java.util.List;

/**
 * 神单跟买Service
 *
 * @author ZhaoYiFeng
 * @version 2018-03-08
 */
@Component
@Transactional(readOnly = true)
public class CdMagicFollowOrderService extends BaseService {

    @Autowired
    private CdMagicFollowOrderDao cdMagicFollowOrderDao;

    public CdMagicFollowOrder get(String id) {
        return cdMagicFollowOrderDao.get(id);
    }

    public Page<CdMagicFollowOrder> find(Page<CdMagicFollowOrder> page, CdMagicFollowOrder cdMagicFollowOrder) {
        DetachedCriteria dc = cdMagicFollowOrderDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(cdMagicFollowOrder.getName())) {
            dc.add(Restrictions.like("name", "%" + cdMagicFollowOrder.getName() + "%"));
        }
        dc.add(Restrictions.eq(CdMagicFollowOrder.FIELD_DEL_FLAG, CdMagicFollowOrder.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return cdMagicFollowOrderDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(CdMagicFollowOrder cdMagicFollowOrder) {

        if (StringUtils.isEmpty(cdMagicFollowOrder.getId())) {
            cdMagicFollowOrder.setId(IdGen.uuid());
            cdMagicFollowOrder.setCreateDate(DateUtils.getDateTime());
            cdMagicFollowOrder.setDelFlag(CdMagicFollowOrder.DEL_FLAG_NORMAL);
        }
        cdMagicFollowOrderDao.save(cdMagicFollowOrder);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        cdMagicFollowOrderDao.deleteById(id);
    }

    public List<CdMagicFollowOrder> findByMid(String mid) {
        DetachedCriteria dc = cdMagicFollowOrderDao.createDetachedCriteria();

        dc.add(Restrictions.eq("magicOrderId", mid));

        dc.add(Restrictions.eq(CdMagicFollowOrder.FIELD_DEL_FLAG, CdMagicFollowOrder.DEL_FLAG_NORMAL));

        return cdMagicFollowOrderDao.find(dc);
    }

}