package com.girlkun.models.player;

import BoMong.BoMong;
import com.arriety.card.Card;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.skill.PlayerSkill;

import java.util.List;

import com.girlkun.models.clan.Clan;
import com.girlkun.models.intrinsic.IntrinsicPlayer;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.ItemTime;
import com.girlkun.models.npc.specialnpc.MagicTree;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.npc.specialnpc.MabuEgg;
import com.girlkun.models.mob.MobMe;
import com.girlkun.data.DataGame;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.map.TrapMap;
import com.girlkun.models.map.Zone;
import com.arriety.models.map.bdkb.BanDoKhoBauService;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.arriety.models.map.doanhtrai.DoanhTraiService;
//import com.arriety.models.map.khigas.KhiGasService;
import com.arriety.models.map.nguhanhson.nguhs;
import com.girlkun.models.map.MapHiru22h.MapHiru;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.matches.IPVP;
import com.girlkun.models.matches.TYPE_LOSE_PVP;
import com.girlkun.models.matches.TYPE_PVP;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.npc.specialnpc.BillEgg;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.server.io.MySession;
import com.girlkun.models.task.TaskPlayer;
import com.girlkun.network.io.Message;
import com.girlkun.server.Client;
import com.girlkun.services.ChatGlobalService;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.FriendAndEnemyService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.services.ItemService;
import com.girlkun.services.PetService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.SkillService;
import com.girlkun.services.TaskService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.services.func.ChonAiDay;
import com.girlkun.services.func.CombineNew;
import com.girlkun.services.func.TopService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.Timer;

public class Player {

    public String tt = "";
    public boolean isBot = false;
    public byte countBDKB;
    public boolean firstJoinBDKB;
    public long lastimeJoinBDKB;
    public int goldChallenge;
    public boolean receivedWoodChest;
    public int levelWoodChest;
    public List<String> textRuongGo = new ArrayList<>();

    public MySession session;

    public boolean beforeDispose;
    public byte role;

    public boolean isPet;
    public boolean isNewPet;
    public boolean isNewPet1;
    public int isqua;
    public int pointboss;
    public int isquavip;
    public int isqua1;
    public int isqua2;
    public int isqua3;
    public int isqua4;
    public int isqua5;
    public String Hppl = "\n";
//    public boolean isNewPet1;
    public boolean isBoss;
    public boolean isTrieuhoipet;
    public int NguHanhSonPoint = 0;
    public IPVP pvp;
    public int pointPvp;
    public int khigas = 0;
    public byte maxTime = 30;
    public byte type = 0;
    public int pointevent = 0;

    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public boolean ThoThanTd;
    public boolean ThoThanNm;
    public boolean ThoThanXd;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    public boolean autodau = false;

    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public BillEgg billEgg;
    public BillEgg trunglinhthu;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public SkillSpecial skillSpecial;
    public  PlayerDAO active;

    public BoMong achievement;
    public Clan clan;
    public ClanMember clanMember;

    public List<Friend> friends;
    public List<Enemy> enemies;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;

    public boolean banv = false;
    public boolean muav = false;
    public long timeudbv = 0;
    public long timeudmv = 0;
    public long lasttimebanv;
    public long lasttimemuav;

    public byte typePk;

    public byte cFlag;
    public int goldTai;
    public int goldXiu;

    public boolean haveTennisSpaceShip;

    public boolean justRevived;
    public long lastTimeRevived;

    public int violate;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public long lastTimeWin;
    public boolean isWin;
    public List<Card> Cards = new ArrayList<>();
    public short idAura = -1;

    public byte countKG;
    public boolean firstJoinKG;
    public long lastimeJoinKG;
    public long rankSieuHang;
    public long numKillSieuHang;

    public int bdkb_countPerDay;
    public long bdkb_lastTimeJoin;
    public boolean bdkb_isJoinBdkb;
    public boolean lockPK;
    public Timer timerDHVT;
    public Player _friendGiaoDich;

    public byte typetrain;
    public int expoff;
    public boolean istrain;
    public boolean istry;
    public boolean istry1;
    public boolean isfight;
    public boolean isfight1;
    public boolean seebossnotnpc;

    public byte isnapt2;
    public byte isnapt3;
    public byte isnapt4;
    public byte isnapt5;
    public byte isnapt6;
    public byte isnapt7;
    public byte isnapCN;
    public byte isnapTUAN;

    public int dh1;
    public int dh2;
    public int dh3;

    public int dh4;
    public int dh5;
    public int dh6;

    public int isdh1;
    public int isdh2;
    public int isdh3;

    public int isdh4;
    public int isdh5;
    public int isdh6;

    public byte nhanqua;
    public long lastTimeNhanQua;

    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        achievement = new BoMong(this);
//        this.typePk = 5; trạng thái pk toàn server
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet;
    }

    public void update() {
        if (isBot) {
            active();
        }
        if (!this.beforeDispose && !isBot) {
            try {
                if (!iDMark.isBan()) {

                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
                    if (this.isPl() && this.clan != null && this.clan.khiGas != null) {
                        GasService.gI().update(this);
                    }

                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                        if (this.itemTime.isdkhi = false) {
                            Service.gI().Send_Caitrang(this);
                            Service.gI().point(this);
                            PlayerService.gI().sendInfoHpMp(this);
                            Service.gI().Send_Info_NV(this);
                            Service.gI().sendInfoPlayerEatPea(this);
                            // updateEff(this);
                        }
                    }
                    long now = System.currentTimeMillis();
                    if (banv && this != null && Util.canDoWithTime(lasttimebanv, 1000) && (now >= timeudbv + 1000)) {
                        banv(this);
                        timeudbv = System.currentTimeMillis();
                    }
                    if (muav && this != null && Util.canDoWithTime(lasttimemuav, 2000) && (now >= timeudmv + 10000)) {
                        muav(this);
                        timeudmv = System.currentTimeMillis();
                    }
//                    KhiGasService.gI().update(this);
                    nguhs.gI().update(this);
                    BlackBallWar.gI().update(this);
                    MapMaBu.gI().update(this);
                    BanDoKhoBauService.gI().updatePlayer(this);
                    DoanhTraiService.gI().updatePlayer(this);
                    MapHiru.gI().update(this);
                    updateEff(this);
                    if (!isBoss && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    }
                    if (this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.iDMark.setGoToBDKB(false);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (this.iDMark.isGoToGas() && Util.canDoWithTime(this.iDMark.getLastTimeGotoGas(), 6000)) {
//                        ChangeMapService.gI().changeMapBySpaceShip(this, 149, -1, 163);
                        ChangeMapService.gI().changeMapBySpaceShip(this, 149, -1, 163);
                        this.iDMark.setGoToGas(false);
                    }
                    if (!isBoss && this.iDMark.isGoToCDRD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToCDRD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 143, -1, 1090);
                        this.iDMark.setGoToCDRD(false);
                    }
                    if (this.isPl() && this.inventory.itemsBody.get(7) != null) {
                        Item it = this.inventory.itemsBody.get(7);
                        if (it != null && it.isNotNullItem() && this.newpet == null) {
                            PetService.Pet2(this, it.template.head, it.template.body, it.template.leg, it.template.name);
                            Service.getInstance().point(this);
                        }
                    } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                        newpet.dispose();
                        newpet = null;
                    }
                    if (this.isPl() && isWin && this.zone.map.mapId == 51 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
                    if (location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(getSession());
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    public void updateEff(Player player) {
        try {
            if (player.nPoint != null) {
                Item fancung = InventoryServiceNew.gI().findItemBag(player, 1368);
                Item thientu = InventoryServiceNew.gI().findItemBag(player, 1369);
                Item trumcuoi = InventoryServiceNew.gI().findItemBag(player, 1370);
                if (player.setClothes.ts >= 5) {
                    Service.gI().addEffectChar(player, 84, 1, -1, -1, 1);
                    player.isdh1 = 1;
                } else {
                    player.isdh1 = 0;
                }
                if (player.setClothes.ts >= 5) {
                    Service.gI().addEffectChar(player, 307, 1, -1, -1, 1);
                    player.isdh2 = 1;
                } else {
                    player.isdh2 = 0;
                }
                if (player.setClothes.ts >= 5) {
                    Service.gI().addEffectChar(player, 247, 1, -1, -1, 1);
                    player.isdh3 = 1;
                } else {
                    player.isdh3 = 0;
                }
            }
            if (player.isPl() && player.inventory != null && player.inventory.itemsBody.size() > 12) {
                Service.gI().removeTitle(player);

                Item danhhieu = player.inventory.itemsBody.get(12);
                if (danhhieu != null && danhhieu.isNotNullItem()) {
                    Service.gI().addEffectChar(player, danhhieu.template.part, 0, -1, 1, -1);
                }

                Item chanmenh = player.inventory.itemsBody.get(13);
                if (chanmenh != null && chanmenh.isNotNullItem()) {
                    Service.gI().addEffectChar(player, chanmenh.template.part, 0, -1, 1, -1);
                }
                
                Item eff2 = player.inventory.itemsBody.get(14);
                if (eff2 != null && eff2.isNotNullItem()) {
                    Service.gI().addEffectChar(player, eff2.template.part, 0, -1, 1, -1);
                }
                
                Item eff3 = player.inventory.itemsBody.get(15);
                if (eff3 != null && eff3.isNotNullItem()) {
                    Service.gI().addEffectChar(player, eff3.template.part, 0, -1, 1, -1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     * {2033,2034,2035}: ht c3 td
     * {2030,2031,2032}: ht c3 nm   
     * {2027,2028,2029}: ht c3 xd*/
    private static final short[][] idOutfitFusion = {
        {380, 381, 382}, {383, 384, 385}, {391, 392, 393},
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869},
        {2048, 2049, 2050}, {2051, 2052, 2053}, {2063, 2064, 2065},
        {2057, 2058, 2059}, {2060, 2061, 2062}, {2054, 2055, 2056},};

    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        int levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        int levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        int levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        int levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        int levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    public short getHead() {
        if (nPoint.IsBiHoaDa) {
            return 454;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];

        } else if (effectSkill != null && effectSkill.isbroly) {
            return 1437;
        } else if (this.itemTime.isUsesp1 || (effectSkill != null && effectSkill.isbroly2)) {
            return 1437;
        }
        if (effectSkill != null && itemTime.isdkhi) {
            return (short) 1437;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else {
            if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
                if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];

                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];

                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[3 + this.gender][0];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[6 + this.gender][0];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                    return idOutfitFusion[9 + this.gender][0];
                }
            } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                int head = inventory.itemsBody.get(5).template.head;
                if (head != -1) {
                    return (short) head;
                }
            }
        }
        return this.head;
    }

    public short getBody() {
        if (nPoint.IsBiHoaDa) {
            return 455;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isbroly) {
            return 1438;
        } else if (this.itemTime.isUsesp1 || (effectSkill != null && effectSkill.isbroly2)) {
            return 1438;
        }
        if (effectSkill != null && itemTime.isdkhi) {
            return (short) 1438;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else {
            if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
                if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];

                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[3 + this.gender][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[6 + this.gender][1];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                    return idOutfitFusion[9 + this.gender][1];
                }
            } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                int body = inventory.itemsBody.get(5).template.body;
                if (body != -1) {
                    return (short) body;
                }
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (nPoint.IsBiHoaDa) {
            return 456;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isbroly) {
            return 1439;
        } else if (this.itemTime.isUsesp1 || (effectSkill != null && effectSkill.isbroly2)) {
            return 1439;
        }
        if (effectSkill != null && itemTime.isdkhi) {
            return (short) 1439;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else {
            if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
                if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];

                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[3 + this.gender][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[6 + this.gender][2];
                } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                    return idOutfitFusion[9 + this.gender][2];
                }
            } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                int leg = inventory.itemsBody.get(5).template.leg;
                if (leg != -1) {
                    return (short) leg;
                }
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getAura() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 12) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5);
        Item item1 = this.inventory.itemsBody.get(11);

        if (!item1.isNotNullItem()) {
            return -1;
        }
        if (item1.template.type == 80) {
            if (item1.template.gender == 3 || item1.template.gender == this.gender) {
                return item1.template.part;
            } else {
                return -1;
            }
        }
        if (item.template.id == 1281) {
            return 27;
        } else if (item.template.id == 1282) {
            return 26;
        } else if (item.template.id == 1284) {
            return 25;
        } else if (item.template.id == 1290) {
            return 24;
        } else if (item.template.id == 1291) {
            return 23;
        } else if (item.template.id == 1295) {
            return 22;
        } else if (item.template.id == 1296) {
            return 21;
        } else if (item.template.id == 1297) {
            return 20;
        } else if (item.template.id == 1298) {
            return 17;
        } else if (item.template.id == 1300) {
            return 16;
        } else if (item.template.id == 1301) {
            return 15;
        } else if (item.template.id == 1302) {
            return 14;
        } else if (item.template.id == 1306) {
            return 13;
        } else if (item.template.id == 1308) {
            return 12;
        } else if (item.template.id == 1309) {
            return 11;
        } else if (item.template.id == 1310) {
            return 10;
        } else {
            return -1;
        }

    }

    public short getFlagBag() {
        if (this.isBot) {
            return (short) Util.nextInt(19, 85);
        }
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if ((this.isPl() || this.isPet) && this.inventory.itemsBody.size() >= 11) {
            if (ThoThanTd) {
                return 109;
            }
            if (ThoThanNm) {
                return 110;
            }
            if (ThoThanXd) {
                return 111;
            }
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }

    }

    //--------------------------------------------------------------------------
    public double injured2(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            PlayerService.gI().hoiPhuc2(this, 0, damage * this.nPoint.voHieuChuong / 100);
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }

            this.nPoint.subHP2(damage);
            if (isDie()) {
                if (this.zone.map.mapId == 112) {
                    plAtt.pointPvp++;
                }
                if (this.isfight || this.isfight1) {
                    this.isfight = false;
                    this.isfight1 = false;
                    this.seebossnotnpc = false;
                    this.zone.load_Me_To_Another(this);
                    this.zone.load_Another_To_Me(this);
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    public boolean iscannotFight() {
        Player pl = this.zone.getPlayerInMapGanNhat(this.location.x, this.location.y, 1000, 1000);
        if (pl != null) {
            if (pl.istry || pl.istry1 || pl.isfight || pl.isfight1) {
                return true;
            }
        }
        return false;
    }

    protected void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        if (this.isPl() && plAtt != null && plAtt.isPl()) {
            plAtt.achievement.plusCount(3);
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
//        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void preparedToDispose() {

    }

    public void setfight(byte typeFight, byte typeTatget) {

        try {
            if (typeFight == (byte) 0 && typeTatget == (byte) 0) {
                this.istry = true;
            }
            if (typeFight == (byte) 0 && typeTatget == (byte) 1) {
                this.istry1 = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 0) {
                this.isfight = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 1) {
                this.isfight1 = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean IsActiveMaster() {

        if (this.istry || this.isfight) {
            this.istry = true;
        }

        return false;
    }

    public void rsfight() {
        if (this.istry) {
            this.istry = false;
        }
        if (this.istry1) {
            this.istry1 = false;
        }
        if (this.isfight) {
            this.isfight = false;
        }
        if (this.isfight1) {
            this.isfight1 = false;
        }
    }

    public boolean IsTry0() {
        if (this.istry && this.isfight) {
            return true;
        }
        return false;
    }

    public boolean IsTry1() {
        if (this.istry && this.isfight1) {
            return true;
        }
        return false;
    }

    public boolean IsFigh0() {
        if (this.istry && this.isfight1) {
            return true;
        }
        return false;
    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }

        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        if (trunglinhthu != null) {
            trunglinhthu.dispose();
            trunglinhthu = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    public void banv(Player player) {
        try {
            if (this.banv && player.inventory.gold <= 100000000000L && player != null) {
                Item tv = null;
                for (Item item : player.inventory.itemsBag) {
                    if (item.isNotNullItem() && item.template.id == 457 || item.template.id == 1322) {
                        tv = item;
                        break;
                    }
                }
                if (tv != null) {
                    if (player.inventory.gold <= 10000000000L) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                        player.inventory.gold += 500000000;
                        lasttimebanv = System.currentTimeMillis();
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "không được vượt quá 2 tỷ vàng");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "hết thỏi vàng rồi, đã tắt lệnh bán vàng");
                    this.banv = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muav(Player player) {
        try {
            if (this.muav && player != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                    if (player.inventory.gold >= 1000000000) {
                        player.inventory.gold -= 1000000000;
                        Item tv = ItemService.gI().createNewItem((short) 457, 1322);
                        InventoryServiceNew.gI().addItemBag(player, tv);
                        lasttimemuav = System.currentTimeMillis();
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                    }
                } else {
                    this.muav = false;
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang, đã tắt tự mua tv");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mob mobTarget;

    public long lastTimeTargetMob;

    public long timeTargetMob;

    public long lastTimeAttack;

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        if (isBot) {
            move = (byte) (move * (byte) 2);
        }
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    public Mob getMobAttack() {
        if (this.mobTarget != null && (this.mobTarget.isDie() || !this.zone.equals(this.mobTarget.zone))) {
            this.mobTarget = null;
        }
        if (this.mobTarget == null && Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
            this.mobTarget = this.zone.getRandomMobInMap();
            this.lastTimeTargetMob = System.currentTimeMillis();
            this.timeTargetMob = 500;
        }
        return this.mobTarget;
    }

    public void active() {
        if (this.isBot) {
            if (this.isDie()) {
                Service.getInstance().hsChar(this, nPoint.hpMax, nPoint.mpMax);
            }
            for (Player boss : this.zone.getBosses()) {
                if (!boss.isDie()) {
                    ChangeMapService.gI().changeMapBySpaceShip(this, Util.nextInt(0, 40), Util.nextInt(this.zone.map.zones.size() - 1), 250);
                }
            }
            if (this.nPoint.mp <= 0) {
                this.nPoint.mp = this.nPoint.mpMax;
            }
            this.attack();
            this.chattg();
        }
    }

    public int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 752002;
    }

    public void chattg() {
        if (this.isBot) {
            //this.mobTarget = this.getMobAttack();
            if (Util.canDoWithTime(Manager.lasttimebotchat, Manager.timechatbot)) {
                if (Util.isTrue(10, 100)) {
                    Manager.lasttimebotchat = System.currentTimeMillis();
                    try {
                        ChatGlobalService.gI().chat(this, Manager.BOTCHATTG.get(Util.nextInt(0, Manager.BOTCHATTG.size() - 1)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public int getRangeCanAttackWithSkillSelect1() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 752002;
    }

    public void attack() {
        if (this.isBot) {
            //this.mobTarget = this.getMobAttack();
            if (Util.canDoWithTime(lastTimeAttack, 100) && this.mobTarget != null) {

                this.lastTimeAttack = System.currentTimeMillis();
                try {
                    Mob m = this.getMobAttack();
                    if (m == null || m.isDie()) {
                        return;
                    }

                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    //System.out.println(m.name);
                    if (Util.nextInt(100) < 70) {
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(0);
                    }
                    if (Util.getDistance(this, m) <= this.getRangeCanAttackWithSkillSelect1()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
                            } else {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, null, m, null);
                    } else {
                        this.moveTo(m.location.x, m.location.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.mobTarget = getMobAttack();
            }
        }
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

   

    

}
