package xyz.wisecraft.smp.modules.heirloom.heirlooms;

import java.util.Date;
import java.util.UUID;

public class HoeHeirloom extends BaseHeirloom {
    private String size;

    public HoeHeirloom(int level, float xp, Date created, UUID id, UUID creator) {
        super(level, xp, HeirloomType.HOEHEIRLOOM, created, id, creator);
    }
}
