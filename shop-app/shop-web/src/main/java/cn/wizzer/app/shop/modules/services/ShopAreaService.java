package cn.wizzer.app.shop.modules.services;

import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.app.shop.modules.models.Shop_area;

public interface ShopAreaService extends BaseService<Shop_area>{
    void save(Shop_area shoparea, String pid);
    void deleteAndChild(Shop_area shoparea);
    String getNameByCode(String code);
}
