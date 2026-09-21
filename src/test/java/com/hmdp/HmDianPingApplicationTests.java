package com.hmdp;

import com.hmdp.entity.Shop;
import com.hmdp.service.IShopService;
import com.hmdp.utils.RedisConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class HmDianPingApplicationTests {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private IShopService shopService;

    @Test
    public void test() {
        redisTemplate.opsForValue().set("key", "value");
    }

    @Test
    public void loadGeo() {
        List<Shop> shops = shopService.list();
        Map<Long, List<Shop>> map = shops.stream()
                .collect(Collectors.groupingBy(Shop::getTypeId));
        for (Map.Entry<Long, List<Shop>> longListEntry : map.entrySet()) {
            List<Shop> value = longListEntry.getValue();
            Long typeId = longListEntry.getKey();
            String key = RedisConstants.SHOP_GEO_KEY + typeId;
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(value.size());
            for (Shop shop : value) {
                locations.add(new RedisGeoCommands.GeoLocation<>(
                        shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())
                ));
            }
            redisTemplate.opsForGeo().add(key, locations);
        }
    }

}
