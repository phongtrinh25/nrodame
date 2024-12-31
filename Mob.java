package com.girlkun.models.mob;

import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstMob;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;

import java.util.List;

import com.girlkun.models.map.Zone;
import com.arriety.models.map.bdkb.BanDoKhoBau;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.map.MapHiru22h.MapHiru;
import com.girlkun.models.player.Location;
import com.girlkun.models.player.Pet;
import com.girlkun.models.player.Player;
import com.girlkun.models.reward.ItemMobReward;
import com.girlkun.models.reward.MobReward;
import com.girlkun.models.skill.PlayerSkill;
import com.girlkun.models.skill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.server.ServerManager;
import com.girlkun.services.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import lombok.Data;

@Data
public class Mob {

    public int id;
    public int param;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;
    private short cx;
    private short cy;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;
    private int action = 0;
    private int hiruAction = 0;
    private final int TIME_START_HIRU = 22;

    public boolean isMobMe;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public boolean isSieuQuai() {
        return this.lvMob > 0;
    }

    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (this.zone.map.mapId == 122 || this.zone.map.mapId == 123 || this.zone.map.mapId == 124) {
                plAtt.NguHanhSonPoint++;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10 && this.zone.map.mapId != 179) {
                    damage = 10;
                }
                if (this.tempId == 70 && damage > 10000 && this.zone.map.mapId == 212) {
                    damage = 10000;
                }
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                            damage *= plAtt.nPoint.multicationChuong;
                            plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                        }

                }
            }
            this.point.hp -= damage;
            if (this.tempId == 70 && damage > 1) {
                ItemMap it = new ItemMap(this.zone, 457, 1, plAtt.location.x + (Util.nextInt(-100, 100)), this.zone.map.yPhysicInTop(this.location.x,
                        this.location.y - 24), plAtt.id);
                if (Util.isTrue(50, 100)) {
                    it = new ItemMap(this.zone, Util.nextInt(220, 224), 4, plAtt.location.x + (Util.nextInt(-100, 100)), this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), plAtt.id);
                    if (Util.isTrue(50, 100)) {
                        it = new ItemMap(this.zone, Util.nextInt(17, 20), 2, plAtt.location.x + (Util.nextInt(-100, 100)), this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24), plAtt.id);
                    }
                    if (Util.isTrue(50, 100)) {
                        it = new ItemMap(this.zone, Util.nextInt(987, 987), 1, plAtt.location.x + (Util.nextInt(-100, 100)), this.zone.map.yPhysicInTop(this.location.x,
                                this.location.y - 24), plAtt.id);
                    }
                }
                Service.gI().dropItemMap(this.zone, it);
            }
            if (this.isDie()) {
                this.lvMob = 0;
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
                if (this.id == 13) {
                    this.zone.isbulon13Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon14Alive = false;
                }
                if (this.isSieuQuai()) {
                    plAtt.achievement.plusCount(12);
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
            }

            if (plAtt != null) {
                Service.getInstance().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, Util.CuongGH(damage)), true);
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = Util.CuongGH(dame * 100 / point.getHpFull());
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124) {
            tiemNang *= 200 / 100;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.zone.map.mapId == 253 || pl.zone.map.mapId == 252 || pl.zone.map.mapId == 254) {
            tiemNang *= 400 / 100;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.zone.map.mapId == 135 || pl.zone.map.mapId == 136 || pl.zone.map.mapId == 137 || pl.zone.map.mapId == 138) {
            tiemNang *= 400 / 100;
        }
        return tiemNang;
    }

    public void update() {
        if (this.tempId == 77) {
            this.tempId = 70;
            try {
                Message msg = new Message(101);
                msg.writer().writeByte(1);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
        if (this.isDie()) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    if (this.zone.isTrungUyTrangAlive && this.tempId == 22 && this.zone.map.mapId == 59) {
                        if (Util.canDoWithTime(lastTimeDie, 5000)) {
                            if (this.id == 13) {
                                this.zone.isbulon13Alive = true;
                            }
                            if (this.id == 14) {
                                this.zone.isbulon14Alive = true;
                            }
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }

                    }
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    if (this.tempId == 72 || this.tempId == 71) {//ro bot bao ve
                        if (System.currentTimeMillis() - this.lastTimeDie > 3000) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte((this.tempId == 71 ? 7 : 6));
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {
                            }
                        }
                    }
                    break;
                case ConstMap.MAP_KHI_GAS:
                    break;
                case ConstMap.MAP_CON_DUONG_RAN_DOC:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000) && this.tempId != 70) {
                        this.randomSieuQuai();
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }
            }
        }
        if (isDie() && this.tempId == 70 && (System.currentTimeMillis() - lastTimeDie) > 5000 && level <= 3) {
            switch (level) {
                case 1:
                    level = 2;
                    action = 6;
                    this.point.sethp(this.point.getMaxHp());
                    break;
                case 2:
                    level = 3;
                    action = 5;
                    this.point.sethp(this.point.getMaxHp());
                    break;
                case 3:
                    level = 1;
                    action = 9;
                    this.zone.mobs.remove(0);
                    //           this.zone.finishMap22h = true;
//            for (Player player : this.zone.getPlayers()) {
//                MapHiru.gI().ketthuc22h(player);
//                
//            }

                    break;
                default:
                    break;
            }
            int trai = 0;
            int phai = 1;
            int next = 0;
            ItemMap GOLD = new ItemMap(this.zone, 190, Util.nextInt(10000, 20000), this.location.x, this.location.y, -1);
            for (int i = 0; i < 30; i++) {
                int X = next == 0 ? -5 * trai : 5 * phai;
                if (next == 0) {
                    trai++;
                } else {
                    phai++;
                }
                next = next == 0 ? 1 : 0;
                if (trai > 10) {
                    trai = 0;
                }
                if (phai > 10) {
                    phai = 1;
                }
//                Service.gI().dropItemMap(zone, GOLD);
            }
            Service.gI().sendBigBoss2(zone, action, this);
            if (level <= 2) {
                Message msg = null;
                try {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeInt((int) this.point.gethp());
                    msg.writer().writeInt(1);
                    Service.gI().sendMessAllPlayerInMap(zone, msg);
                } catch (Exception e) {
                } finally {
                    if (msg != null) {
                        msg.cleanup();
                    }
                }
            } else {
                cx = -1000;
                cy = -1000;
            }
        }
        if (this.tempId == 70) {
            BigbossAttack();
        } else {
            attackPlayer();
        }
        effectSkill.update();
    }

    private void BigbossAttack() {
        if (!isDie() && !this.effectSkill.isHaveEffectSkill() && Util.canDoWithTime(lastTimeAttackPlayer, 1000)) {
            Message msg = null;
            try {
                switch (this.tempId) {
                    case 70: // Hirudegarn 
                        if (!Util.canDoWithTime(lastTimeAttackPlayer, 1000)) {
                            return;
                        }
                        // 0: bắn - 1: Quật đuôi - 2: dậm chân - 3: Bay - 4: tấn công - 5: Biến hình - 6: Biến hình lên cấp
                        // 7: vận chiêu - 8: Di chuyển - 9: Die
                        int[] idAction = new int[]{0, 2, 3, 7, 8};
                        if (this.level >= 3) {
                            idAction = new int[]{1, 8};
                        }
                        hiruAction = idAction[Util.nextInt(0, idAction.length - 1)];
                        int index = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player = zone.getPlayers().get(index);
                        if (player == null || player.isDie()) {
                            return;
                        }
                        if (hiruAction == 1) {
                            cx = (short) player.location.x;
                            Service.gI().sendBigBoss2(zone, 8, this);
                        }
                        msg = new Message(101);
                        msg.writer().writeByte(hiruAction);
                        if (hiruAction >= 0 && hiruAction <= 4) {
                            switch (hiruAction) {
                                case 1:
                                    msg.writer().writeByte(1);
                                    int dame = (int) player.injured2(player, (int) this.point.getDameAttack(), false, true);
                                    msg.writer().writeInt((int) player.id);
                                    msg.writer().writeInt(dame);
                                    break;
                                case 3:
                                    cx = (short) player.location.x;
                                    msg.writer().writeShort(cx);
                                    msg.writer().writeShort(this.location.y);
                                    break;
                                default:
                                    msg.writer().writeByte(zone.getNotBosses().size());
                                    for (int i = 0; i < zone.getNotBosses().size(); i++) {
                                        Player pl = zone.getNotBosses().get(i);
                                        dame = (int) player.injured2(player, (int) this.point.getDameAttack(), false, true);
                                        msg.writer().writeInt((int) pl.id);
                                        msg.writer().writeInt(dame);
                                    }
                                    break;
                            }
                        } else {
                            if (hiruAction == 6 || hiruAction == 8) {
                                cx = (short) player.location.x;
                                msg.writer().writeShort(cx);
                                msg.writer().writeShort(this.location.y);
                            }
                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        Service.getInstance().sendThongBao(player, "Cấp " + level);
                        break;
                }
            } catch (Exception e) {
            } finally {
                if (msg != null) {
                    msg.cleanup();
                }
            }
        }
    }

    public void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && !(tempId == 82) && !(tempId == 83) && !(tempId == 84)) {

            if ((this.tempId == 72 || this.tempId == 71) && Util.canDoWithTime(lastTimeAttackPlayer, 300)) {
                List<Player> pl = getListPlayerCanAttack();
                if (!pl.isEmpty()) {
                    this.sendMobBossBdkbAttack(pl, this.point.getDameAttack());
                } else {
                    if (this.tempId == 71) {
                        Player plA = getPlayerCanAttack();
                        if (plA != null) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte(5);
                                t.writer().writeByte(plA.location.x);
                                this.location.x = plA.location.x;
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {
                            }
                        }

                    }
                }
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            } else if (Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                Player pl = getPlayerCanAttack();
                if (pl != null) {
                    this.mobAttackPlayer(pl);
                }
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            }

        }
    }

    private void sendMobBossBdkbAttack(List<Player> players, long dame) {
        if (this.tempId == 72) {
            try {
                Message t = new Message(102);
                int action = Util.nextInt(0, 2);
                t.writer().writeByte(action);
                if (action != 1) {
                    this.location.x = players.get(Util.nextInt(0, players.size() - 1)).location.x;
                }
                t.writer().writeByte(players.size());
                for (Byte i = 0; i < players.size(); i++) {
                    t.writer().writeInt((int) players.get(i).id);
                    t.writer().writeInt((int) players.get(i).injured2(null, (int) dame, false, true));
                }
                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                t.cleanup();
            } catch (IOException e) {
            }
        } else if (this.tempId == 71) {
            try {
                Message t = new Message(102);
                t.writer().writeByte(Util.getOne(3, 4));
                t.writer().writeByte(players.size());
                for (Byte i = 0; i < players.size(); i++) {
                    t.writer().writeInt((int) players.get(i).id);
                    t.writer().writeInt((int) players.get(i).injured2(null, (int) dame, false, true));
                }
                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                t.cleanup();
            } catch (IOException e) {
            }
        }
    }

    private List<Player> getListPlayerCanAttack() {
        List<Player> plAttack = new ArrayList<>();
        int distance = (this.tempId == 71 ? 250 : 600);
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack.add(pl);
                    }
                }
            }
        } catch (Exception e) {
        }

        return plAttack;
    }

    public static void initMopbKhiGas(Mob mob, int level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void initMobConDuongRanDoc(Mob mob, int level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writer().writeInt((mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh && !pl.isNewPet) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
    public void mobAttackPlayer(Player player) {
        double dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (this.isSieuQuai()) {
            dameMob = player.nPoint.hpMax / 10;
        }
        double dame = player.injured2(null, dameMob, false, true);

        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, double dame) {
        if (!player.isPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(Util.CuongGH(dame)); //dam
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(Util.CuongGH(player.nPoint.hp));
            Service.getInstance().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void randomSieuQuai() {
        if (this.tempId != 0 && MapService.gI().isMapKhongCoSieuQuai(this.zone.map.mapId) && Util.nextInt(0, 150) < 1) {
            this.lvMob = 1;
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, double dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(Util.CuongGH(dameHit));
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    public void sendMobDieAfterMobMeAttacked(Player plKill, long dameHit) {
        this.status = 0;
        Message msg;
        try {
            if (this.id == 13) {
                this.zone.isbulon13Alive = false;
            }
            if (this.id == 14) {
                this.zone.isbulon14Alive = false;
            }
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeLong(dameHit);
            msg.writer().writeBoolean(false); // crit

            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (IOException e) {
            Logger.logException(Mob.class, e);
        }
//        if (plKill.isPl()) {
//            if (TaskService.gI().IsTaskDoWithMemClan(plKill.playerTask.taskMain.id)) {
//                TaskService.gI().checkDoneTaskKillMob(plKill, this, true);
//            } else {
//                TaskService.gI().checkDoneTaskKillMob(plKill, this, false);
//            }
//
//        }
        this.lastTimeDie = System.currentTimeMillis();
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (player != null && player.isPl() && items != null) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
//        nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {
//            if ((!player.isPet && player.getSession().actived && player.setClothes.setDHD == 5) || (player.isPet && ((Pet) player).master.getSession().actived && ((Pet) player).setClothes.setDHD == 5)) {
//                byte random = 1;
//                if (Util.isTrue(5, 100)) {
//                    random = 2;
//                }
//                Item i = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
//                i.quantity = random;
//                InventoryServiceNew.gI().addItemBag(player, i);
//                InventoryServiceNew.gI().sendItemBags(player);
//                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + random + " hồng ngọc");
//            }
            if (player.itemTime.isUsesk && Util.isTrue(7.1f, 100) && this.tempId > 87 && this.tempId < 90) {
                ArrietyDrop.DropItemReWard(player, 1243, 1, player.location.x, player.location.y);
            }
            if (Util.isTrue(1, 100) && this.tempId > 87 && this.tempId < 90) {
                ArrietyDrop.DropItemReWard(player, 1247, 1, player.location.x, player.location.y);
            }//            if (Util.isTrue(1.0f, 100)) {
            //                if (player.setClothes.thienSuClothes && MapService.gI().isMapCold(player.zone.map)) {
            //                    ArrietyDrop.DropItemReWardDoHuyDietKichHoat(player, 1, this.location.x, this.location.y);
            //                }
            //            } 

            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        // Map 2 toi map 4 roi 100
        if (this.zone.map.mapId >= 1 && this.zone.map.mapId <= 3) {
            if (Util.isTrue(2, 100)) {    //hộp skh
                list.add(ItemService.gI().dropSKH_level_0(player, x, yEnd));
            }
        }

        // Map 8 toi map 10 roi 100
        if (this.zone.map.mapId >= 8 && this.zone.map.mapId <= 11) {
            if (Util.isTrue(2, 100)) {    //hộp skh
                list.add(ItemService.gI().dropSKH_level_0(player, x, yEnd));
            }
        }

        // Map 16 toi map 18 roi 100
        if (this.zone.map.mapId >= 15 && this.zone.map.mapId <= 17) {
            if (Util.isTrue(2, 100)) {    //hộp skh
                list.add(ItemService.gI().dropSKH_level_0(player, x, yEnd));
            }
        }

        if (this.tempId == 70) {
            for (int i = 0; i < 10; i++) {
                byte randomDo = (byte) new Random().nextInt(Manager.roibosshiru.length - 1);
                ItemMap it = new ItemMap(this.zone, Manager.roibosshiru[randomDo], 1, this.location.x + (Util.nextInt(-200, 200)), this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1);

                if (Util.isTrue(30, 100)) {
                    it = new ItemMap(this.zone, Util.nextInt(1066, 1070), 1, this.location.x + (Util.nextInt(-200, 200)), this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24), -1);

                }
                Service.gI().dropItemMap(this.zone, it);

                list.add(it);
            }
        }//up cskb có kiểm tra máy dò
        if (player.itemTime.isUseMayDo && Util.isTrue(100, 100) && this.tempId > 92 && this.tempId < 100) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
        }
//        if (player.itemTime.isUseMayDo2 && Util.isTrue(1, 100) && this.tempId > 1 && this.tempId < 81) {
//            list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));// cai nay sua sau nha
//        }
        if (player.cFlag >= 1 && Util.isTrue(100, 100) && this.tempId == 0 && hour != 1 && hour != 3 && hour != 5 && hour != 7 && hour != 9 && hour != 11 && hour != 13 && hour != 15 && hour != 17 && hour != 19 && hour != 21 && hour != 23) {    //up bí kíp
            list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));// cai nay sua sau nha
            if (Util.isTrue(50, 100) && this.tempId == 0) {    //up bí kíp
                list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                if (Util.isTrue(50, 100) && this.tempId == 0) {    //up bí kíp
                    list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                    if (Util.isTrue(50, 100) && this.tempId == 0) {    //up bí kíp
                        list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId == 173) {
            if (Util.isTrue(50, 100)) {    //mảnh vở bông tai cấp 1 2 
                list.add(new ItemMap(zone, 1487, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(80, 100)) {    //mảnh vở bông tai cấp 1 2 
                list.add(new ItemMap(zone, Util.nextInt(933, 934), 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(80, 100)) {    //mảnh vở bông tai cấp3 4
                list.add(new ItemMap(zone, Util.nextInt(1445, 1446), 1, x, player.location.y, player.id));
            }
        }
        if (player.setClothes.sethe() && this.zone.map.mapId >= 29 && this.zone.map.mapId <= 30) { // up map đảo bulong và namkame ms ra dc vpsk
            if (Util.isTrue(100f, 100)) {    //up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
            }
        }

        if (this.tempId == 860 && this.zone.map.mapId >= 105 && this.zone.map.mapId <= 111) {
            if (Util.isTrue(0.00003f, 100)) {   //1446 Đá Sinh mệnh 
                list.add(new ItemMap(zone, 1446, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (Util.isTrue(60, 100)) {    // đá xanh làm
                list.add(new ItemMap(zone, 935, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159 && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (Util.isTrue(0.5f, 100)) {    //Đá Gallery
                list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));
            }
        }

        if (player.setClothes.setGod() && this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
            if (Util.isTrue(0.9f, 100)) {    //thức ăn
                list.add(new ItemMap(zone, Util.nextInt(663, 667), 1, x, player.location.y, player.id));
            }
        }
        if (player.setClothes.setGod1() && this.zone.map.mapId >= 0 && this.zone.map.mapId <= 255) {
            if (Util.isTrue(0.7f, 100)) {    //ngọc rồng
                list.add(new ItemMap(zone, Util.nextInt(18, 20), 1, x, player.location.y, player.id));
            }
        }
        if (player.setClothes.setGod() && this.zone.map.mapId >= 105 && this.zone.map.mapId <= 111) {
            if (Util.isTrue(0.6f, 100)) {    //đồ thần linh
                list.add(new ItemMap(zone, Util.nextInt(555, 567), 1, x, player.location.y, player.id));
            }
        }
        if (this.zone.map.mapId >= 0 && this.zone.map.mapId <= 18) {
            if (player.getSession().actived = true) {
                int vg = Util.nextInt(10, 1000);
                list.add(new ItemMap(zone, 188, vg, x, player.location.y, player.id));

            }

        }
        if (this.zone.map.mapId >= 92 && this.zone.map.mapId <= 100) {
            if (player.getSession().actived == true) {
                int vg = Util.nextInt(10, 1000);
                list.add(new ItemMap(zone, 188, vg, x, player.location.y, player.id));
            }

        }

        if (player.setClothes.setGod() && this.zone.map.mapId >= 105 && this.zone.map.mapId <= 111) {
            if (Util.isTrue(100, 100)) {    //rương đồ thần
                list.add(new ItemMap(zone, Util.nextInt(1258, 1258), 1, x, player.location.y, player.id));
            }
        }
//        if (player.setClothes.setGod() && this.zone.map.mapId >= 105 && this.zone.map.mapId <= 111) {
//            if (Util.isTrue(0.0000000000000009f, 100)) {    //up bí kíp
//                list.add(new ItemMap(zone, Util.nextInt(1259, 1259), 1, x, player.location.y, player.id));
//            }
//        }
//        if (player.setClothes.setGod14() && this.zone.map.mapId == 155) {
//            if (Util.isTrue(0.1f, 100)) {    //up bí kíp
//                list.add(new ItemMap(zone, Util.nextInt(1066, 1070), 1, x, player.location.y, player.id));
//            }
//        }
        if (player.setClothes.setGod() && this.zone.map.mapId == 155) {
            if (Util.isTrue(80, 100)) {    //đá nâng cấp đồ
                list.add(new ItemMap(zone, Util.nextInt(220, 224), 1, x, player.location.y, player.id));
            }
        }
        if (player.setClothes.setGod14() && this.zone.map.mapId == 155) {
            if (Util.isTrue(4.5f, 100)) {    //Đá nâng cấp cấp 1-5
                list.add(new ItemMap(zone, Util.nextInt(1074, 1083), 1, x, player.location.y, player.id));
            }
        }
        Item item = player.inventory.itemsBody.get(1);
        if (this.zone.map.mapId > 0) {
            if (item.isNotNullItem()) {
                if (item.template.id == 691) {
                    if (Util.isTrue(10, 100)) {    //up bí kíp
                        list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId >= 0) {
            if (item.isNotNullItem()) {
                if (item.template.id == 692) {
                    if (Util.isTrue(10, 100)) {    //up bí kíp
                        list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId > 0) {
            if (item.isNotNullItem()) {
                if (item.template.id == 693) {
                    if (Util.isTrue(10, 100)) {    //up bí kíp
                        list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y, player.id));
                    }
                } else if (item.template.id != 691 && item.template.id != 692 && item.template.id != 693) {
                    if (Util.isTrue(0, 1)) {
                        list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
                    }
                }
            }
        }
        if (this.zone.map.mapId == 122) {
            if (Util.isTrue(0.000000003f, 1000)) {
                list.add(new ItemMap(zone, 861, 1, x, player.location.y, player.id));
            }
        }
        if (this.zone.map.mapId >= 1 && this.zone.map.mapId <= 212) {
            if (Util.isTrue(100, 1000)) {
                list.add(new ItemMap(zone, 861, 1, x, player.location.y, player.id));
            }
        }
        if (this.zone.map.mapId == 124) {
            if (Util.isTrue(0.000000003f, 1000)) {
                list.add(new ItemMap(zone, 861, 1, x, player.location.y, player.id));
            }
        }

        if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
            if (player.bdkb_isJoinBdkb) {
                int level = player.clan.banDoKhoBau.level;
                int slhn = Util.nextInt(1, 3) * (level / 10);
                if (Util.nextInt(0, 100) < 100) {

                    list.add(new ItemMap(zone, 861, slhn, x, player.location.y, player.id));
                }
            }

        }
        if (this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159) {
            if (player.isPl() && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                list.add(new ItemMap(zone, 933, 1, x, player.location.y, player.id));
            } else if (player.isPl() && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                list.add(new ItemMap(zone, 1455, 1, x, player.location.y, player.id));
            } else if (player.isPl() && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                list.add(new ItemMap(zone, 1456, 1, x, player.location.y, player.id));
            }

        }
        if (this.zone.map.mapId > 100) { // Kiểm tra map có ID lớn hơn 100
            if (player.getSession().actived = true) {
//                int level = player.clan.banDoKhoBau.level;
                int slvg = Util.nextInt(1, 3);
                if (Util.nextInt(0, 100) < 100) {

                    list.add(new ItemMap(zone, 188, slvg, x, player.location.y, player.id));
                }
            }
        }
        return list;

    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(double dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(Util.CuongGH(this.point.gethp()));
            msg.writer().writeInt(Util.CuongGH(dameHit));
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemRandom() {
        if (this.zone.map.mapId > 100) { // Kiểm tra map có ID lớn hơn 100
            int randomValue = Util.nextInt(100, 1000); // Tạo giá trị ngẫu nhiên từ 100 đến 1000
            ItemMap droppedItem = new ItemMap(
                    this.zone, // Zone hiện tại
                    188, // ID vật phẩm
                    randomValue, // Giá trị ngẫu nhiên
                    this.location.x + Util.nextInt(-50, 50), // Tọa độ x ngẫu nhiên gần vị trí mob
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y), // Tọa độ y theo map
                    -1 // ID người chơi nhận (không chỉ định)
            );
            Service.gI().dropItemMap(this.zone, droppedItem); // Thả vật phẩm xuống map
        }
    }
}
