package com.girlkun.models.player;

import com.arriety.card.Card;
import com.arriety.card.OptionCard;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstRatio;
import com.girlkun.models.intrinsic.Intrinsic;
import com.girlkun.models.item.Item;
import com.girlkun.models.npc.Npc;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.MapService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class NPoint {

    public static final byte MAX_LIMIT = 15;

    private Player player;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
        this.tlSpeed = new ArrayList<>();
    }

    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;

    public long hp, hpMax, hpg;
    public long mp, mpMax, mpg;
    public long dame, dameg;
    public int def, defg;
    public int crit, critg;
    public byte speed = 8;

    public boolean teleport;

    public boolean CoCaiTrangHoaDa;
    public boolean IsBiHoaDa;

    public boolean khangTDHS;

    /**
     * Chỉ số cộng thêm
     */
    public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public long hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;
    public short tlSubHP;
    public short tlSubMP;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;
    public short test;

    public short multicationChuong;
    public long lastTimeMultiChuong;

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        if (this.player.pet != null) {
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    private void setPointWhenWearClothes() {
        resetPoint();

        boolean foundItem921 = false;
        for (Item item : this.player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 921)) {
                if (!foundItem921 && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                    foundItem921 = true;
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 14: //Chi mang
                                this.critAdd += io.param;
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 215:
                                this.tlDame.add(io.param);
                                this.tlMp.add(io.param);
                                this.tlHp.add(io.param);
                                break;
                            case 216:
                                this.tlHp.add(io.param);
                                break;
                            case 217:
                                this.tlDame.add(io.param);
                                break;
                            case 218:
                                this.tlMp.add(io.param);
                                break;
                            case 219:
                                this.tlHp.add(io.param);
                                break;
                            case 220:
                                this.tlMp.add(io.param);
                                break;
                            case 188:
                                this.tlDame.add(io.param);
                                this.tlMp.add(io.param);
                                this.tlHp.add(io.param);
                            case 221:
                                this.tlDame.add(io.param);
                                break;
                            case 222:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 224: //HP, KI+#000
                                this.mpAdd += io.param * 100000;
                                break;
                            case 225: //HP, KI+#000
                                this.hpAdd += io.param * 100000;
                                this.isKhongLanh = true;
                                break;
                            case 226: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 228: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 230: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 231: //HP, KI+#000
                                this.hpAdd += io.param * 10000;
                                this.tlHp.add(io.param);
                                this.mpAdd += io.param * 10000;
                                this.tlMp.add(io.param);
                                this.isKhongLanh = true;
                                break;
                        }
                    }
                }
            }
        }
        if (player.isPl() && player.ThoThanTd) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1443) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 49: //Tấn công+#%
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 101:
                                this.tlTNSM.add(io.param);
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (player.isPl() && player.ThoThanNm) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1444) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 49: //Tấn công+#%
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 101:
                                this.tlTNSM.add(io.param);
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (player.isPl() && player.ThoThanXd) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1445) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 49: //Tấn công+#%
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 101:
                                this.tlTNSM.add(io.param);
                                break;
                        }
                    }
                    break;
                }
            }
        }

        boolean foundItem1155 = false;
        for (Item item : this.player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 1155)) {
                if (!foundItem1155 && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                    foundItem1155 = true;
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 215:
                                this.tlDame.add(io.param);
                                this.tlMp.add(io.param);
                                this.tlHp.add(io.param);
                                break;
                            case 216:
                                this.tlHp.add(io.param);
                                break;
                            case 217:
                                this.tlDame.add(io.param);
                                break;
                            case 218:
                                this.tlMp.add(io.param);
                                break;
                            case 219:
                                this.tlHp.add(io.param);
                                break;
                            case 220:
                                this.tlMp.add(io.param);
                                break;
                            case 221:
                                this.tlDame.add(io.param);
                                break;
                            case 222:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 224: //HP, KI+#000
                                this.mpAdd += io.param * 100000;
                                break;
                            case 225: //HP, KI+#000
                                this.hpAdd += io.param * 100000;
                                this.isKhongLanh = true;
                                break;
                            case 226: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 228: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 230: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 231: //HP, KI+#000
                                this.hpAdd += io.param * 10000;
                                this.tlHp.add(io.param);
                                this.mpAdd += io.param * 10000;
                                this.tlMp.add(io.param);
                                this.isKhongLanh = true;
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 14: //Chi mang
                                this.critAdd += io.param;
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                }
            }
        }

        for (Item item : this.player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 1156)) {
                if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 215:
                                this.tlDame.add(io.param);
                                this.tlMp.add(io.param);
                                this.tlHp.add(io.param);
                                break;
                            case 216:
                                this.tlHp.add(io.param);
                                break;
                            case 217:
                                this.tlDame.add(io.param);
                                break;
                            case 218:
                                this.tlMp.add(io.param);
                                break;
                            case 219:
                                this.tlHp.add(io.param);
                                break;
                            case 220:
                                this.tlMp.add(io.param);
                                break;
                            case 221:
                                this.tlDame.add(io.param);
                                break;
                            case 222:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 224: //HP, KI+#000
                                this.mpAdd += io.param * 100000;
                                break;
                            case 225: //HP, KI+#000
                                this.hpAdd += io.param * 100000;
                                this.isKhongLanh = true;
                                break;
                            case 226: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 228: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 230: //HP, KI+#000
                                this.dameAdd += io.param * 10000;
                                this.isKhongLanh = true;
                                break;
                            case 231: //HP, KI+#000
                                this.hpAdd += io.param * 10000;
                                this.tlHp.add(io.param);
                                this.mpAdd += io.param * 10000;
                                this.tlMp.add(io.param);
                                this.isKhongLanh = true;
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 14: //chi mang
                                this.critAdd += io.param;
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
//                        System.out.println("Cộng chỉ số cấp 2");
                    }
                }
            }
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            tlHutMp += RewardBlackBall.R2S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            tlDameAttMob.add(RewardBlackBall.R4S_2);
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R5S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R6S_1;
            tlNeDon += RewardBlackBall.R6S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            tlHpHoi += RewardBlackBall.R7S_1;
            tlHutHp += RewardBlackBall.R7S_2;
        }

        this.player.setClothes.worldcup = 0;
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                switch (item.template.id) {
                    case 966:
                    case 982:
                    case 983:
                    case 883:
                    case 904:
                        player.setClothes.worldcup++;
                }
                if (item.template.id >= 592 && item.template.id <= 594) {
                    teleport = true;
                }
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 0: //Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 2: //HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// fake
                            this.voHieuChuong += io.param;
                            break;
                        case 5: //+#% sức đánh chí mạng
                            this.tlDameCrit.add(io.param);
                            break;
                        case 6: //HP+#
                            this.hpAdd += io.param;
                            break;
                        case 7: //KI+#
                            this.mpAdd += io.param;
                            break;
                        case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 14: //Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 19: //Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add(io.param);
                            break;
                        case 22: //HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: //MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        // case 26:// hóa đá
                        //    this.CoCaiTrangHoaDa = true;
                        //    break;
                        case 27: //+# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: //+# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: //dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 47: //Giáp+#
                            this.defAdd += io.param;
                            break;
                        case 48: //HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 49: //Tấn công+#%
                        case 50: //Sức đánh+#%
                            this.tlDame.add(io.param);
                            break;
                        case 77: //HP+#%
                            this.tlHp.add(io.param);
                            break;
                        case 80: //HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: //Cộng #% exp khi đánh quái
                            this.tlTNSM.add(io.param);
                            break;
                        case 94: //Giáp #%
                            this.tlDef.add(io.param);
                            break;
                        case 95: //Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: //Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: //Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 100: //+#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: //+#% TN,SM
                            this.tlTNSM.add(io.param);
                            break;
                        case 103: //KI +#%
                            this.tlMp.add(io.param);
                            break;
                        case 104: //Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;
                        case 105: //Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;
                        case 106: //Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 108: //#% Né đòn
                            this.tlNeDon += io.param;// đối nghịch
                            break;
                        case 109: //Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;

                        //    case 116: //Kháng thái dương hạ san
                        //        this.khangTDHS = true;
                        //        break;
                        case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                            this.tlSDDep.add(io.param);
                            break;
                        case 147: //+#% sức đánh
                            this.tlDame.add(io.param);
                            break;
                        case 75: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                            this.tlSubSD += 50;
                            this.tlTNSM.add(io.param);
                            this.tlGold += io.param;
                            break;
                        case 159: // x# chưởng mỗi phút
                            this.multicationChuong += io.param;
                            break;
                        case 162: //Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;
                        case 173: //Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;
                        case 211: //test
                            this.test += io.param;
                            break;
                        case 215:
                            this.tlDame.add(io.param);
                            this.tlMp.add(io.param);
                            this.tlHp.add(io.param);
                            break;
                        case 216:
                            this.tlHp.add(io.param);
                            break;
                        case 217:
                            this.tlDame.add(io.param);
                            break;
                        case 218:
                            this.tlMp.add(io.param);
                            break;
                        case 219:
                            this.tlHp.add(io.param);
                            break;
                        case 220:
                            this.tlMp.add(io.param);
                            break;
                        case 221:
                            this.tlDame.add(io.param);
                            break;
                        case 222:
                            this.tlDameCrit.add(io.param);
                            break;
                        case 224: //HP, KI+#000
                            this.mpAdd += io.param * 100000;
                            break;
                        case 225: //HP, KI+#000
                            this.hpAdd += io.param * 100000;
                            this.isKhongLanh = true;
                            break;
                        case 226: //HP, KI+#000
                            this.dameAdd += io.param * 10000;
                            this.isKhongLanh = true;
                            break;
                        case 228: //HP, KI+#000
                            this.dameAdd += io.param * 10000;
                            this.isKhongLanh = true;
                            break;
                        case 230: //HP, KI+#000
                            this.dameAdd += io.param * 10000;
                            this.isKhongLanh = true;
                            break;
                        case 231: //HP, KI+#000
                            this.hpAdd += io.param * 10000;
                            this.tlHp.add(io.param);
                            this.mpAdd += io.param * 10000;
                            this.tlMp.add(io.param);
                            this.isKhongLanh = true;
                            break;
                    }
                }
            }
        }
        for (Item item : this.player.inventory.itemsBag) {
            if (item.isNotNullItem()) {
                if (item.template.type == 101) {
                    if (this.player.isdh1 == 1 || this.player.isdh2 == 1 || this.player.isdh3 == 1 || this.player.isdh4 == 1 || this.player.isdh5 == 1 || this.player.isdh6 == 1) {
                        for (Item.ItemOption io : item.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 0: //Tấn công +#
                                    this.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.hpAdd += io.param * 1000;
                                    this.mpAdd += io.param * 1000;
                                    break;
                                case 3:// fake
                                    this.voHieuChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.critAdd += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.mpAdd += io.param * 1000;
                                    break;
                                // case 26:// hóa đá
                                //    this.CoCaiTrangHoaDa = true;
                                //    break;
                                case 27: //+# HP/30s
                                    this.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.hpAdd += io.param;
                                    this.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.tlPST += io.param;
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.tlHutHpMob += io.param;
                                    break;
                                case 105: //Vô hình khi không đánh quái và boss
                                    this.wearingVoHinh = true;
                                    break;
                                case 106: //Không ảnh hưởng bởi cái lạnh
                                    this.isKhongLanh = true;
                                    break;
                                case 108: //#% Né đòn
                                    this.tlNeDon += io.param;// đối nghịch
                                    break;
                                case 109: //Hôi, giảm #% HP
                                    this.tlHpGiamODo += io.param;
                                    break;

                                //    case 116: //Kháng thái dương hạ san
                                //        this.khangTDHS = true;
                                //        break;
                                case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                    this.tlSDDep.add(io.param);
                                    break;
                                case 147: //+#% sức đánh
                                    this.tlDame.add(io.param);
                                    break;
                                case 75: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                    this.tlSubSD += 50;
                                    this.tlTNSM.add(io.param);
                                    this.tlGold += io.param;
                                    break;
                                case 159: // x# chưởng mỗi phút
                                    this.multicationChuong += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                                case 211: //test
                                    this.test += io.param;
                                    break;
                                case 215:
                                    this.tlDame.add(io.param);
                                    this.tlMp.add(io.param);
                                    this.tlHp.add(io.param);
                                    break;
                                case 216:
                                    this.tlHp.add(io.param);
                                    break;
                                case 217:
                                    this.tlDame.add(io.param);
                                    break;
                                case 218:
                                    this.tlMp.add(io.param);
                                    break;
                                case 219:
                                    this.tlHp.add(io.param);
                                    break;
                                case 220:
                                    this.tlMp.add(io.param);
                                    break;
                                case 221:
                                    this.tlDame.add(io.param);
                                    break;
                                case 222:
                                    this.tlDameCrit.add(io.param);
                                    break;
                                case 224: //HP, KI+#000
                                    this.mpAdd += io.param * 100000;
                                    break;
                                case 225: //HP, KI+#000
                                    this.hpAdd += io.param * 100000;
                                    this.isKhongLanh = true;
                                    break;
                                case 226: //HP, KI+#000
                                    this.dameAdd += io.param * 10000;
                                    this.isKhongLanh = true;
                                    break;
                                case 228: //HP, KI+#000
                                    this.dameAdd += io.param * 10000;
                                    this.isKhongLanh = true;
                                    break;
                                case 230: //HP, KI+#000
                                    this.dameAdd += io.param * 10000;
                                    this.isKhongLanh = true;
                                    break;
                                case 231: //HP, KI+#000
                                    this.hpAdd += io.param * 10000;
                                    this.tlHp.add(io.param);
                                    this.mpAdd += io.param * 10000;
                                    this.tlMp.add(io.param);
                                    this.isKhongLanh = true;
                                    break;
                            }
                        }
                    }
                }
            }
        }
        Card card = player.Cards.stream().filter(r -> r != null && r.Used == 1).findFirst().orElse(null);
        if (card != null) {
            for (OptionCard io : card.Options) {
                if (io.active == card.Level || (card.Level == -1 && io.active == 0)) {
                    switch (io.id) {
                        case 0: //Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 2: //HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// fake
                            this.voHieuChuong += io.param;
                            break;
                        case 5: //+#% sức đánh chí mạng
                            this.tlDameCrit.add(io.param);
                            break;
                        case 6: //HP+#
                            this.hpAdd += io.param;
                            break;
                        case 7: //KI+#
                            this.mpAdd += io.param;
                            break;
                        case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 14: //Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 19: //Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add(io.param);
                            break;
                        case 22: //HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: //MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        case 27: //+# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: //+# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: //dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 47: //Giáp+#
                            this.defAdd += io.param;
                            break;
                        case 48: //HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 49: //Tấn công+#%
                        case 50: //Sức đánh+#%
                            this.tlDame.add(io.param);
                            break;
                        case 77: //HP+#%
                            this.tlHp.add(io.param);
                            break;
                        case 80: //HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: //Cộng #% exp khi đánh quái
                            this.tlTNSM.add(io.param);
                            break;
                        case 94: //Giáp #%
                            this.tlDef.add(io.param);
                            break;
                        case 95: //Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: //Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: //Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 100: //+#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: //+#% TN,SM
                            this.tlTNSM.add(io.param);
                            break;
                        case 103: //KI +#%
                            this.tlMp.add(io.param);
                            break;
                        case 104: //Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;

                        case 147: //+#% sức đánh
                            this.tlDame.add(io.param);
                            break;
                        case 215:
                            this.tlDame.add(io.param);
                            this.tlMp.add(io.param);
                            this.tlHp.add(io.param);
                            break;
                        case 216:
                            this.tlHp.add(io.param);
                            break;
                        case 217:
                            this.tlDame.add(io.param);
                            break;
                        case 218:
                            this.tlMp.add(io.param);
                            break;
                        case 219:
                            this.tlHp.add(io.param);
                            break;
                        case 220:
                            this.tlMp.add(io.param);
                            break;
                        case 221:
                            this.tlDame.add(io.param);
                            break;
                        case 222:
                            this.tlDameCrit.add(io.param);
                            break;
                        case 224: //HP, KI+#000
                            this.mpAdd += io.param * 100000;
                            break;
                        case 225: //HP, KI+#000
                            this.hpAdd += io.param * 100000;
                            this.isKhongLanh = true;
                            break;
                        case 226: //HP, KI+#000
                            this.dameAdd += io.param * 10000;
                            this.isKhongLanh = true;
                            break;
                        case 228: //HP, KI+#000
                            this.dameAdd += io.param * 10000;
                            this.isKhongLanh = true;
                            break;
                        case 230: //HP, KI+#000
                            this.dameAdd += io.param * 10000;
                            this.isKhongLanh = true;
                            break;
                        case 231: //HP, KI+#000
                            this.hpAdd += io.param * 10000;
                            this.tlHp.add(io.param);
                            this.mpAdd += io.param * 10000;
                            this.tlMp.add(io.param);
                            this.isKhongLanh = true;
                            break;
                    }
                }
            }
        }
        setDameTrainArmor();
        setDameTrainArmor1();
        setBasePoint();
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;
                        for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                            if (io.optionTemplate.id == 9 && io.param > 0) {
                                this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
            }
        }
    }

    private void setDameTrainArmor1() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor1(gtl);
                    this.tlSubHP += ItemService.gI().getPercentTrainArmor1(gtl);
                    this.tlSubMP += ItemService.gI().getPercentTrainArmor1(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;
                        for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                            if (io.optionTemplate.id == 9 && io.param > 0) {
                                this.tlDame.add(ItemService.gI().getPercentTrainArmor1(this.player.inventory.trainArmor));
                                this.tlMp.add(ItemService.gI().getPercentTrainArmor1(this.player.inventory.trainArmor));
                                this.tlHp.add(ItemService.gI().getPercentTrainArmor1(this.player.inventory.trainArmor));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
            }
        }
    }

    public void setBasePoint() {
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        settlGold();
    }
    public List<Integer> tlSpeed;

    private void setSpeed() {
        for (Integer tl : this.tlSpeed) {
            this.speed += calPercent(this.speed, tl);
        }
        if (this.player.effectSkin.isVoHinh) {
            this.speed = (byte) 0.5;
        }
    }

    private void setNeDon() {
    }

    private void setHpHoi() {
        this.hpHoi = this.hpMax / 100;
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);
        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {
        this.mpHoi = this.mpMax / 100;
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);
        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

    private void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        //đồ
        for (Integer tl : this.tlHp) {
            this.hpMax += ((long) this.hpMax * tl / 100);
        }
        //set nappa
        if (this.player.setClothes.nappa == 5) {
            this.hpMax += ((long) this.hpMax * 80 / 100);
        }      
        if (this.player.setClothes.nappa_level_1 == 5) {
            this.hpMax += ((long) this.hpMax * 100 / 100);
        }
        if (this.player.setClothes.nappa_level_2 == 5) {
            this.hpMax += ((long) this.hpMax * 120 / 100);
        }
        if (this.player.setClothes.demo == 5 && this.player.effectSkill.isMonkey) {
            this.hpMax += ((long) this.hpMax * 50 / 100);
        }
        if (this.player.setClothes.spl >= 5) {
            this.hpMax += ((long) this.hpMax * 10 / 100);
        }
        if (this.player.setClothes.lv5 >= 5) {
            this.hpMax += ((long) this.hpMax * 5 / 100);
        }

        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.hpMax += ((long) this.hpMax * 10 / 100);
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.hpMax += ((long) this.hpMax * RewardBlackBall.R2S_1 / 100);
        }
        //khỉ
        if (this.player.effectSkill.isbroly) {
            this.hpMax += (this.hpMax * 0.25);
        }

        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.hpMax += ((long) this.hpMax * percent / 100);
            }
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long) this.hpMax * 15 / 100);
        }
        //pet berus
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long) this.hpMax * 20 / 100);//chi so hp
        }
        //pet zamasu star
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long) this.hpMax * 30 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long) this.hpMax * 50 / 100);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //+hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //btc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            if (this.player.head == 1210) {
                this.hpMax *= 1.0;
            } else {
                this.hpMax *= 1.0;
            }
        }
        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (this.player.head == 1210) {
                this.hpMax *= 1.2;
            } else {
                this.hpMax *= 1.05;
            }
        }
        //btc4
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            if (this.player.head == 1210) {
                this.hpMax *= 1.2;
            } else {
                this.hpMax *= 1.1;
            }
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += ((long) this.hpMax * this.player.effectSkill.tiLeHPHuytSao / 100L);

            }
        }
        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax *= 2;
        }// item sieu cawsp
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
            this.hpMax *= 2.2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isWish1Bingo) {
            this.hpMax *= 1.3;
        }
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax /= 2;
        }
        if (this.player.itemTime.isdkhi) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = 100;
                this.hpMax += ((long) this.hpMax * percent / 100);
                //              this.tlDameCrit.add(5);
            }
        }
//        //mèo mun
//        if (this.player.effectFlagBag.useMeoMun) {
//            this.hpMax += ((long) this.hpMax * 15 / 100);
//        }
    }

    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        //đồ
        for (Integer tl : this.tlMp) {
            this.mpMax += ((long) this.mpMax * tl / 100);
        }
        if (this.player.setClothes.picolo == 5) {
            this.mpMax += ((long) this.mpMax * 800 / 100);
        }       
        if (this.player.setClothes.picolo_level_1 == 5) {
            this.mpMax += ((long) this.mpMax * 100 / 100);
        }
        if (this.player.setClothes.picolo_level_2 == 5) {
            this.mpMax += ((long) this.mpMax * 120 / 100);
        }
        if (this.player.setClothes.demo == 5 && this.player.effectSkill.isMonkey) {
            this.mpMax += ((long) this.mpMax * 50 / 100);
        }
        if (this.player.setClothes.spl >= 5) {
            this.mpMax += ((long) this.mpMax * 10 / 100);
        }
        if (this.player.setClothes.lv5 >= 5) {
            this.mpMax += ((long) this.mpMax * 5 / 100);
        }
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += ((long) this.mpMax * RewardBlackBall.R3S_1 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.mpMax += ((long) this.mpMax * 10 / 100);
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long) this.mpMax * 15 / 100);
        }
        //pet br
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//MP berus
        }
        //pet Zamasu star
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//MP Zamasu star
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long) this.mpMax * 50 / 100);//MP Zamasu star
        }
        //hợp thể
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        //BTc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            if (this.player.head == 1210) {
                this.mpMax *= 1.0;
            } else {
                this.mpMax *= 1.0;
            }
        }

        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (this.player.head == 1210) {
                this.mpMax *= 1.2;
            } else {
                this.mpMax *= 1.05;
            }
        }
        //btc4
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            if (this.player.head == 1210) {
                this.mpMax *= 1.2;
            } else {
                this.mpMax *= 1.1;
            }
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
            this.mpMax *= 2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
            this.mpMax *= 2.2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isWish2Bingo) {
            this.mpMax *= 1.3;
        }
        if (this.player.effectSkill.isbroly) {
            this.mpMax += (this.mpMax * 0.25);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
//        //xiên cá
//        if (this.player.effectFlagBag.useXienCa) {
//            this.mpMax += ((long) this.mpMax * 15 / 100);
//        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;
        //đồ
        for (Integer tl : this.tlDame) {
            this.dame += ((long) this.dame * tl / 100);
        }
        for (Integer tl : this.tlSDDep) {
            this.dame += ((long) this.dame * tl / 100);
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long) this.dame * 15 / 100);
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long) this.dame * 30 / 100);
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long) this.dame * 30 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long) this.dame * 50 / 100);
        }
        //thức ăn
        if (this.player.isPet && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += ((long) this.dame * 10 / 100);
        }
        //hợp thể
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.dame += this.player.pet.nPoint.dame;
        }
        //btc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            if (this.player.head == 1210) {
                this.dame *= 1.0;
            } else {
                this.dame *= 1.0;
            }
        }
        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (this.player.head == 1210) {
                this.dame *= 1.2;
            } else {
                this.dame *= 1.05;
            }
        }
        //btc4
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            if (this.player.head == 1210) {
                this.dame *= 1.2;
            } else {
                this.dame *= 1.1;
            }
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += ((long) this.dame * RewardBlackBall.R1S_1 / 100);
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
            this.dame *= 2;
        }
        if (this.player.setClothes.spl >= 5) {
            this.dame += ((long) this.dame * 10 / 100);
        }
        if (this.player.setClothes.lv5 >= 5) {
            this.dame += ((long) this.dame * 5 / 100);
        }
        if (this.player.setClothes.demo == 5 && this.player.effectSkill.isMonkey) {
            this.dame += ((long) this.dame * 50 / 100);
            this.defAdd += ((long) this.defAdd + 1500);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
            this.dame *= 2.2;
        }
        if (this.player.itemTime != null && this.player.itemTime.isWish3Bingo) {
            this.dame *= 1.3;
        }
        //giảm dame
        this.dame -= ((long) this.dame * tlSubSD / 100);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
        //ngọc rồng đen 1 sao
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.dame += ((long) this.dame * 10 / 100);
            this.tlDameCrit.add(20);
        }

//        //phóng heo
//        if (this.player.effectFlagBag.usePhongHeo) {
//            this.dame += ((long) this.dame * 15 / 100);
//        }
        //khỉ
        if (this.player.effectSkill.isbroly) {
            this.dame += (this.dame * 0.15);
        }
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                this.dame += ((long) this.dame * percent / 100);
            }
        }
        if (this.player.itemTime.isdkhi) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = 100;
                this.dame += ((long) this.dame * percent / 100);
                this.tlDameCrit.add(5);
            }
        }

    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        //đồ
        for (Integer tl : this.tlDef) {
            this.def += ((long) this.def * tl / 100);
        }
        //ngọc rồng đen 2 sao
    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        //ngọc rồng đen 3 sao
        //biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
        if (this.player.effectSkill.isbroly) {
            this.crit += 15;
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUse1trung) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUse2trung) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseDacbiet) {
            this.crit += 20;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUsesp1) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
            this.mpMax += ((long) this.mpMax * 20 / 100);
            this.dame += ((long) this.dame * 20 / 100);
        }
        this.hpMax -= ((long) this.dame * tlSubHP / 100);
        this.mpMax -= ((long) this.dame * tlSubMP / 100);
        if (this.player.itemTime != null && this.player.itemTime.isUseDacbiet) {
            this.dame += ((long) this.dame * 15 / 100);
        }
    }

    private void resetPoint() {
        this.voHieuChuong = 0;
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlSubHP = 0;
        this.tlSubMP = 0;
        this.tlHpGiamODo = 0;
        this.test = 0;
        this.tlSpeed.clear();
        this.IsBiHoaDa = false;
        this.CoCaiTrangHoaDa = false;
        this.teleport = false;
        this.tlGold = 0;
        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
    }

    public void addHp(int hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp(int mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void addMp2(double mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void addHp2(double hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void setHp(long hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = (int) hp;
        }
    }

    public void setMp(long mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = (int) mp;
        }
    }

    public void settlGold() {
        if (intrinsic != null && intrinsic.id == 23) {
            this.tlGold += intrinsic.param1;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public long getDameAttack(boolean isAttackMob) {
        setIsCrit();
        long dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku >= 5) {
                    percentXDame = 80;
                }               
                if (this.player.setClothes.songoku_level_1 >= 5) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.songoku_level_2>= 5) {
                    percentXDame = 120;
                }
                break;

            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame = 80;
                }                
                if (this.player.setClothes.kakarot_level_1 == 5) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.kakarot_level_2 == 5) {
                    percentXDame = 120;
                }
                if (this.player.setClothes.demo == 5 && this.player.effectSkill.isMonkey) {
                    percentXDame = 100;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {
                    percentDameIntrinsic = intrinsic.param1;
                }                
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kirin == 5) {
                    percentXDame = 80;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kirin_level_1 == 5) {
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kirin_level_2 == 5) {
                    percentXDame = 120;
                }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 80;
                    break;
                }               
                if (this.player.setClothes.ocTieu_level_1 == 5) {
                    percentXDame = 100;
                    break;
                }
                if (this.player.setClothes.ocTieu_level_2 == 5) {
                    percentXDame = 120;
                    break;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 1;
                dameAttack = Util.Tamkjllnext(dameAttack - (dameAttack * 5 / 100),
                        dameAttack + (dameAttack * 5 / 100));
                return dameAttack;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                int dameSkill = (int) ((long) this.mpMax * percentDameSkill / 100);
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long dame = this.dame * 40;
                if (this.player.setClothes.kirin == 5) {
                    dame *= 2;
                }
                dame = dame + (Util.nextInt(-5, 5) * dame / 100);
                return dame;
        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }

//        if (dameAttack > 1) {
//            if (skillSelect.template.id == Skill.KAMEJOKO && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.LIEN_HOAN && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.GALICK && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.KAIOKEN && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.DE_TRUNG && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.MAKANKOSAPPO && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.TU_SAT && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            } else if (skillSelect.template.id == Skill.QUA_CAU_KENH_KHI && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh 1 " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.getMoneys(dameAttack));
//            }
//        }
        if (percentDameSkill != 0) {
            dameAttack = dameAttack * percentDameSkill / 100;
        }
        dameAttack += ((long) dameAttack * percentDameIntrinsic / 100);
        dameAttack += ((long) dameAttack * dameAfter / 100);

        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += ((long) dameAttack * tl / 100);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (this.player.isPet && ((Pet) this.player).itemTime.isUseItemDeTu) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += ((long) dameAttack * tl / 100);
            }
        }
        dameAttack += ((long) dameAttack * percentXDame / 100);
        dameAttack = Util.Tamkjllnext(dameAttack - (dameAttack * 5 / 100), dameAttack + (dameAttack * 5 / 100));
        if (player.isPl()) {
            if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {
            }
        }
        //check activation set
        if (dameAttack > 100000_000000_000000L) {
            if (skillSelect.template.id == Skill.KAMEJOKO && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.LIEN_HOAN && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.GALICK && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.KAIOKEN && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.DE_TRUNG && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.MAKANKOSAPPO && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.TU_SAT && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.QUA_CAU_KENH_KHI && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            }
            Logger.log(Logger.PURPLE, "Player: " + player.name + " Id: " + player.id + " Skill: " + player.playerSkill.skillSelect.template.name + "Dame: " + Util.numberToMoney(dameAttack) + " \n");
        }
        return dameAttack;
    }

    public int getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return (int) ((long) this.hp * 100 / this.hpMax);
    }

    public int getCurrPercentMP() {
        return (int) ((long) this.mp * 100 / this.mpMax);
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;

    }

    public void subHP(int sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subHP2(double sub) {
        this.hp -= sub;
//        System.out.println("Hp đã trừ " + sub);
//        System.err.println("Hp còn lại " + this.hp);
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(int sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calPercent(long param, int percent) {
        return param * percent / 100;
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += ((long) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += ((long) tiemNang * 10 / 100);
                } else {
                    tiemNang += ((long) tiemNang * 5 / 100);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 1;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (player.getSession() != null) {
                if (player.getSession().actived) {
                    tiemNang += tn * 10 / 100;
                }
                if (player.getSession().vnd >= 100000) {
                    tiemNang += tn * 5 / 100;
                }
                if (player.getSession().vnd >= 200000) {
                    tiemNang += tn * 10 / 100;
                }
                if (player.getSession().vnd >= 500000) {
                    tiemNang += tn * 20 / 100;
                }
                if (player.getSession().vnd >= 1000000) {
                    tiemNang += tn * 40 / 100;
                }
                if (player.getSession().actived) {
                    if (this.player.isPet) {
                        tiemNang += tn * 10 / 100;
                    }
                }
                if (player.getSession().vnd >= 100000) {
                    {
                        if (this.player.isPet) {
                            tiemNang += tn * 5 / 100;
                        }
                    }
                    if (player.getSession().vnd >= 200000) {
                        if (this.player.isPet) {
                            tiemNang += tn * 10 / 100;
                        }
                    }
                    if (player.getSession().vnd >= 500000) {
                        if (this.player.isPet) {
                        }
                        tiemNang += tn * 20 / 100;
                    }
                }
                if (player.getSession().vnd >= 1000000) {
                    if (this.player.isPet) {
                        tiemNang += tn * 40 / 100;
                    }
                }
            }
            if (this.player.setClothes.cadic == 5) {
                tiemNang += tn * 2;
            }
            if (this.player.setClothes.pikkoroDaimao == 5) {
                tiemNang += tn * 2;
            }
            if (this.player.setClothes.thienXinHang == 5) {
                tiemNang += tn * 2;
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += ((long) tiemNang * this.intrinsic.param1 / 100);
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            if (this.player.isPet) {
                if (this.player.isPet && ((Pet) this.player).itemTime.isUseItemDeTu) {
                    tiemNang *= 2;
                }
            }
            if (MapService.gI().isnguhs(this.player.zone.map.mapId)) {
                tiemNang *= 50;
            }
            if (MapService.gI().isHTNT2(this.player.zone.map.mapId)) {
                tiemNang *= 1.5;
            }
            if (MapService.gI().isHTNT(this.player.zone.map.mapId)) {
                tiemNang *= 2;
            }
            if (MapService.gI().isMapBanDoKhoBau(this.player.zone.map.mapId)) {
                tiemNang *= 100;
            }
            if (MapService.gI().iscold1(this.player.zone.map.mapId)) {
                tiemNang *= 10;
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
//            tiemNang = 10;
        }
        return tiemNang;
    }

    public long calSubTNSM(long tiemNang) {
        if (power >= 110000000000L) {
            tiemNang /= 200;
        } else if (power >= 200000000000L) {
            tiemNang /= 210;
        } else if (power >= 300000000000L) {
            tiemNang /= 250;
        } else if (power >= 400000000000L) {
            tiemNang /= 300;
        } else if (power >= 500000000000L) {
            tiemNang /= 660;
        } else if (power >= 1000000000000L) {
            tiemNang /= 1000;
        } else if (power >= 1500000000000L) {
            tiemNang /= 2000;
        } else if (power >= 2500000000000L) {
            tiemNang /= 3000;
        } else if (power >= 3500000000000L) {
            tiemNang /= 4000;
        } else if (power >= 4500000000000L) {
            tiemNang /= 5000;
        } else if (power >= 5500000000000L) {
            tiemNang /= 50000;
        } else if (power >= 6500000000000L) {
            tiemNang /= 55000;
        } else if (power >= 7500000000000L) {
            tiemNang /= 60000;
        } else if (power >= 8500000000000L) {
            tiemNang /= 80000;
        } else if (power >= 9500000000000L) {
            tiemNang /= 90000;
        } else if (power >= 10000000000000L) {
            tiemNang /= 150000;
        } else if (power >= 20000000000000L) {
            tiemNang /= 650000;
        } else if (power >= 30000000000000L) {
            tiemNang /= 750000;
        } else if (power >= 40000000000000L) {
            tiemNang /= 850000;
        } else if (power >= 50000000000000L) {
            tiemNang /= 950000;
        } else if (power >= 60000000000000L) {
            tiemNang /= 1150000;
        } else if (power >= 70000000000000L) {
            tiemNang /= 1500000;
        } else if (power >= 80000000000000L) {
            tiemNang /= 5500000;
        } else if (power >= 90000000000000L) {
            tiemNang /= 11500000;
        } else if (power >= 100000000000000L) {
            tiemNang /= 23500000;
        } else if (power >= 110000000000000L) {
            tiemNang /= 55500000;
        } else if (power >= 100000000000L) {
            tiemNang /= 140;
        } else if (power >= 90000000000L) {
            tiemNang /= 100;
        } else if (power >= 80000000000L) {
            tiemNang /= 55;
        } else if (power >= 70000000000L) {
            tiemNang /= 45;
        } else if (power >= 60000000000L) {
            tiemNang /= 18;
        } else if (power >= 50000000000L) {
            tiemNang /= 8;
        } else if (power >= 40000000000L) {
            tiemNang /= 4;
        } else if (power >= 1) {
            tiemNang /= 2;
        }
        return tiemNang;
    }

    public short getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {
        return this.tlHutMp;
    }

    public short getTlGold() {
        return this.tlGold;
    }

    public double subDameInjureWithDeff(double dame) {
        long def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (this.player.itemTime.isUseGiapXen2) {
            dame /= 1.10;
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        switch (limitPower) {
            case 0:
                return 17999999999L;
            case 1:
                return 18999999999L;
            case 2:
                return 20999999999L;
            case 3:
                return 24999999999L;
            case 4:
                return 30999999999L;
            case 5:
                return 40999999999L;
            case 6:
                return 60999999999L;
            case 7:
                return 80999999999L;
            case 8:
                return 109999999999L;
            case 9:
                return 120999999999L;
            case 10:
                return 150999999999L;
            case 11:
                return 199999999999L;
            case 12:
                return 229999999999L;
            case 13:
                return 309999999999L;
            case 14:
                return 409999999999L;
            case 15:
                return 1599999999999999L;
            default:
                return 0;
        }
    }

    public long getPowerNextLimit() {
        switch (limitPower + 1) {
            case 0:
                return 17999999999L;
            case 1:
                return 18999999999L;
            case 2:
                return 20999999999L;
            case 3:
                return 24999999999L;
            case 4:
                return 30999999999L;
            case 5:
                return 40999999999L;
            case 6:
                return 60999999999L;
            case 7:
                return 80999999999L;
            case 8:
                return 109999999999L;
            case 9:
                return 120999999999L;
            case 10:
                return 150999999999L;
            case 11:
                return 199999999999L;
            case 12:
                return 229999999999L;
            case 13:
                return 309999999999L;
            case 14:
                return 409999999999L;
            case 15:
                return 1599999999999999L;
            default:
                return 0;
        }
    }

    public int getHpMpLimit() {
        if (limitPower == 0) {
            return 220000;
        }
        if (limitPower == 1) {
            return 240000;
        }
        if (limitPower == 2) {
            return 300000;
        }
        if (limitPower == 3) {
            return 350000;
        }
        if (limitPower == 4) {
            return 400000;
        }
        if (limitPower == 5) {
            return 450000;
        }
        if (limitPower == 6) {
            return 500000;
        }
        if (limitPower == 7) {
            return 550000;
        }
        if (limitPower == 8) {
            return 560000;
        }
        if (limitPower == 9) {
            return 600000;
        }
        if (limitPower == 10) {
            return 610000;
        }
        if (limitPower == 11) {
            return 620000;
        }
        if (limitPower == 12) {
            return 630000;
        }
        if (limitPower == 13) {
            return 640000;
        }
        if (limitPower == 14) {
            return 650000;
        }
        if (limitPower == 15) {
            return 650000;
        }
        return 0;
    }

    public int getDameLimit() {
        if (limitPower == 0) {
            return 11000;
        }
        if (limitPower == 1) {
            return 12000;
        }
        if (limitPower == 2) {
            return 15000;
        }
        if (limitPower == 3) {
            return 18000;
        }
        if (limitPower == 4) {
            return 20000;
        }
        if (limitPower == 5) {
            return 22000;
        }
        if (limitPower == 6) {
            return 25000;
        }
        if (limitPower == 7) {
            return 25500;
        }
        if (limitPower == 8) {
            return 30000;
        }
        if (limitPower == 9) {
            return 32000;
        }
        if (limitPower == 10) {
            return 32000;
        }
        if (limitPower == 11) {
            return 32000;
        }
        if (limitPower == 12) {
            return 32000;
        }
        if (limitPower == 13) {
            return 32000;
        }
        if (limitPower == 14) {
            return 32000;
        }
        if (limitPower == 15) {
            return 32000;
        }
        return 0;
    }

    public short getDefLimit() {
        if (limitPower == 0) {
            return 550;
        }
        if (limitPower == 1) {
            return 600;
        }
        if (limitPower == 2) {
            return 700;
        }
        if (limitPower == 3) {
            return 800;
        }
        if (limitPower == 4) {
            return 1000;
        }
        if (limitPower == 5) {
            return 1200;
        }
        if (limitPower == 6) {
            return 1400;
        }
        if (limitPower == 7) {
            return 1600;
        }
        if (limitPower == 8) {
            return 1700;
        }
        if (limitPower == 9) {
            return 1800;
        }
        if (limitPower == 10) {
            return 1800;
        }
        if (limitPower == 11) {
            return 1800;
        }
        if (limitPower == 12) {
            return 1800;
        }
        if (limitPower == 13) {
            return 1800;
        }
        if (limitPower == 14) {
            return 1800;
        }
        if (limitPower == 15) {
            return 1800;
        }
        return 0;
    }

    public byte getCritLimit() {
        if (limitPower == 0) {
            return 5;
        }
        if (limitPower == 1) {
            return 6;
        }
        if (limitPower == 2) {
            return 7;
        }
        if (limitPower == 3) {
            return 8;
        }
        if (limitPower == 4) {
            return 9;
        }
        if (limitPower == 5) {
            return 10;
        }
        if (limitPower == 6) {
            return 10;
        }
        if (limitPower == 7) {
            return 10;
        }
        if (limitPower == 8) {
            return 10;
        }
        if (limitPower == 9) {
            return 10;
        }
        if (limitPower == 10) {
            return 10;
        }
        if (limitPower == 11) {
            return 10;
        }
        if (limitPower == 12) {
            return 10;
        }
        if (limitPower == 13) {
            return 10;
        }
        if (limitPower == 14) {
            return 10;
        }
        if (limitPower == 15) {
            return 10;
        }
        return 0;
    }

    public int getexp() {
        int[] expTable = {5000, 10000, 20000, 40000, 80000, 120000, 240000, 500000};
        if (player.typetrain >= 0 && player.typetrain < expTable.length) {
            return expTable[player.typetrain];
        } else {
            return 0;
        }
    }

    public String getNameNPC(Player player, Npc npc, byte type) {
        if (type == 2) {
            switch (npc.tempId) {
                case ConstNpc.THAN_MEO_KARIN:
                case ConstNpc.THUONG_DE:
                case ConstNpc.THAN_VU_TRU:
                case ConstNpc.TO_SU_KAIO:
                case ConstNpc.BILL:
                    return "ta";
            }
        } else if (type == 1) {
            switch (npc.tempId) {
                case ConstNpc.THAN_MEO_KARIN:
                    return "Yajirô";
                case ConstNpc.THUONG_DE:
                    return "Mr.PôPô";
                case ConstNpc.THAN_VU_TRU:
                    return "Khỉ Bubbles";
                case ConstNpc.TO_SU_KAIO:
                    return "ta";
                case ConstNpc.BILL:
                    return "Whis";
            }
        }
        return "NRO";
    }

    public int getExpbyNPC(Player player, Npc npc, byte type) {
        if (type == 2) {
            switch (npc.tempId) {
                case ConstNpc.THAN_MEO_KARIN:
                    return 5000;
                case ConstNpc.THUONG_DE:
                    return 20000;
                case ConstNpc.THAN_VU_TRU:
                    return 80000;
                case ConstNpc.TO_SU_KAIO:
                    return 120000;
                case ConstNpc.BILL:
                    return 500000;
            }
        } else if (type == 1) {
            switch (npc.tempId) {
                case ConstNpc.THAN_MEO_KARIN:
                    return 5000;
                case ConstNpc.THUONG_DE:
                    return 10000;
                case ConstNpc.THAN_VU_TRU:
                    return 40000;
                case ConstNpc.TO_SU_KAIO:
                    return 120000;
                case ConstNpc.BILL:
                    return 240000;
            }
        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point) {
        if (point <= 0 || point > 100) {
            return;
        }
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= getDameLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= getDefLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 3L;
            }
            if ((this.critg + point) <= getCritLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        Service.gI().point(player);
    }

    private boolean doUseTiemNang(long tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.gI().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc2(player, hpMax / 100 * tiLeHoiPhuc,
                            mpMax / 100 * tiLeHoiPhuc);
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.gI().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc2(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
        this.tlSpeed = null;
    }
}
