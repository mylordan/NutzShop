package cn.wizzer.app.shop.modules.services.impl;

import cn.wizzer.app.shop.modules.models.Shop_goods_spec_values;
import cn.wizzer.app.shop.modules.services.ShopGoodsSpecValuesService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.shop.modules.models.Shop_goods_spec;
import cn.wizzer.app.shop.modules.services.ShopGoodsSpecService;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean(args = {"refer:dao"})
public class ShopGoodsSpecServiceImpl extends BaseServiceImpl<Shop_goods_spec> implements ShopGoodsSpecService {
    public ShopGoodsSpecServiceImpl(Dao dao) {
        super(dao);
    }
    private static final Log log = Logs.get();
    @Inject
    private ShopGoodsSpecValuesService shopGoodsSpecValuesService;
    /**
     * 添加规格及规格值
     *
     * @param shopGoodsSpec
     * @param spec_value
     * @param spec_picurl
     */
    @Aop(TransAop.READ_COMMITTED)
    public void add(Shop_goods_spec shopGoodsSpec, String[] spec_value, String[] spec_picurl) {
        this.insert(shopGoodsSpec);
        for (int i = 0; i < spec_value.length; i++) {
            Shop_goods_spec_values values = new Shop_goods_spec_values();
            values.setSpecId(shopGoodsSpec.getId());
            values.setSpec_value(Strings.sNull(spec_value[i]));
            if (shopGoodsSpec.getType() == 1) {
                values.setSpec_picurl(Strings.sNull(spec_picurl[i]));
            }
            values.setLocation(i);
            shopGoodsSpecValuesService.insert(values);
        }
    }

    /**
     * 修改规格及规格值
     *
     * @param shopGoodsSpec
     * @param spec_value
     * @param spec_picurl
     * @param uid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void update(Shop_goods_spec shopGoodsSpec, String[] spec_value, String[] spec_picurl,String[] spec_value_id,String uid) {
        shopGoodsSpec.setOpAt((int) (System.currentTimeMillis() / 1000));
        shopGoodsSpec.setOpBy(uid);
        this.updateIgnoreNull(shopGoodsSpec);
        shopGoodsSpecValuesService.clear(Cnd.where("specId", "=", shopGoodsSpec.getId()).and("id","not in",spec_value_id));
        for (int i = 0; i < spec_value.length; i++) {
            Shop_goods_spec_values values = new Shop_goods_spec_values();
            values.setId(Strings.sNull(spec_value_id[i]));
            values.setSpecId(shopGoodsSpec.getId());
            values.setSpec_value(Strings.sNull(spec_value[i]));
            values.setLocation(i);
            if (shopGoodsSpec.getType() == 1) {
                values.setSpec_picurl(Strings.sNull(spec_picurl[i]));
            }
            if(!Strings.isBlank(values.getId())){
                shopGoodsSpecValuesService.update(values);
            }else {
                shopGoodsSpecValuesService.insert(values);
            }
        }
    }

    /**
     * 删除规格的同时删除其值,事务支持
     *
     * @param id
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteSpec(String id) {
        shopGoodsSpecValuesService.clear(Cnd.where("specId", "=", id));
        this.delete(id);
    }

    /**
     * 删除规格的同时删除其值,事务支持
     *
     * @param ids
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteSpec(String[] ids) {
        shopGoodsSpecValuesService.clear(Cnd.where("specId", "in", ids));
        this.delete(ids);
    }
}
