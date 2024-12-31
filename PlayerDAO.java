package com.girlkun.jdbc.daos;

import com.arriety.card.Card;
import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.item.ItemTime;
import com.girlkun.models.player.Friend;
import com.girlkun.models.player.Fusion;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Manager;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.MapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PlayerDAO {

    public static boolean createNewPlayer(int userId, String name, byte gender, int hair) {
        try {
            JSONArray dataArray = new JSONArray();

            dataArray.add(2000000000L); //vàng
            dataArray.add(200000000); //ngọc xanh
            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event

            String inventory = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //vàng
            dataArray.add(0); //ngọc xanh
            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event
            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event
            String data_nap = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //hồng ngọc
            dataArray.add(1); //point
            dataArray.add(1); //event
            String data_dh = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event
            String isdh = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //hồng ngọc
            dataArray.add(1); //point
            dataArray.add(1); //event
            String data_dh1 = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event
            String isdh1 = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(21 + gender); //map
            dataArray.add(145); //x
            dataArray.add(336); //y
            String location = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //giới hạn sức mạnh
            dataArray.add(2000); //sức mạnh
            dataArray.add(2000); //tiềm năng
            dataArray.add(1000); //thể lực
            dataArray.add(1000); //thể lực đầy
            dataArray.add(gender == 0 ? 200 : 100); //hp gốc
            dataArray.add(gender == 1 ? 200 : 100); //ki gốc
            dataArray.add(gender == 2 ? 200 : 20); //sức đánh gốc
            dataArray.add(0); //giáp gốc
            dataArray.add(0); //chí mạng gốc
            dataArray.add(0); //năng động
            dataArray.add(gender == 0 ? 200 : 100); //hp hiện tại
            dataArray.add(gender == 1 ? 200 : 100); //ki hiện tại
            String point = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //level
            dataArray.add(5); //curent pea
            dataArray.add(0); //is upgrade
            dataArray.add(new Date().getTime()); //last time harvest
            dataArray.add(new Date().getTime()); //last time upgrade
            String magicTree = dataArray.toJSONString();
            dataArray.clear();
            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */

            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int idgang = gender == 0 ? 21 : gender == 1 ? 22 : 23;
            int idgiay = gender == 0 ? 27 : gender == 1 ? 28 : 29;
            int idct = gender == 0 ? 1408 : gender == 1 ? 1408 : 1408;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;

            JSONArray item = new JSONArray();
            JSONArray options = new JSONArray();
            JSONArray opt = new JSONArray();
            for (int i = 0; i < 16; i++) {
                if (i == 0) {
                    opt.add(107);
                    opt.add(3);
                    opt.add(40);
                    opt.add(3);
                    item.add(idAo);
                    item.add(1);
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 1) {
                    opt.add(107);
                    opt.add(3);
                    opt.add(6);
                    opt.add(10);
                    item.add(idQuan);
                    item.add(1);
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 2) {
                    opt.add(107);
                    opt.add(3);
                    opt.add(0);
                    opt.add(5);
                    item.add(idgang);
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 3) { //giày
                    opt.add(107); //id option
                    opt.add(3); //param option
                    opt.add(6);
                    opt.add(10);
                    item.add(idgiay); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBody = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 80; i++) {
                if (i == 0) {
                    opt.add(30);
                    opt.add(1);
                    item.add(457);
                    item.add(20);
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 1) {
                    opt.add(101);
                    opt.add(5);
                    item.add(447);
                    item.add(30);
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBag = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 80; i++) {
                if (i == 0) { //rada
                    opt.add(107); //id option
                    opt.add(3); //param option
                    item.add(12); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBox = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            String dataoff = dataArray.toJSONString();
            dataArray.clear();
            for (int i = 0; i < 80; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBoxLuckyRound = dataArray.toJSONString();
            dataArray.clear();

            String friends = dataArray.toJSONString();
            String enemies = dataArray.toJSONString();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            dataArray.add(0); //chỉ số 2
            dataArray.add(0); //số lần mở
            String intrinsic = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //mở giới hạn sức mạnh
            dataArray.add(0); //máy dò
            dataArray.add(0); //thức ăn cold
            dataArray.add(0); //icon thức ăn cold
            dataArray.add(0); // tdlt
            String itemTime = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết2
            dataArray.add(0); //bổ khí2
            dataArray.add(0); //giáp xên2
            dataArray.add(0); //cuồng nộ2
            dataArray.add(0); //cuồng nộ2
            dataArray.add(0); //ẩn danh2
            String itemTimeSC = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết2
            dataArray.add(0); //bổ khí2
            dataArray.add(0); //giáp xên2            
            String itemBanh = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0);
            dataArray.add(0);
            dataArray.add(0);
            String data_bingo = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết2
            dataArray.add(0); //bổ khí2
            dataArray.add(0); //giáp xên2            
            String itemSP = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết2                   
            String itemSK = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); // máy dò cua
            dataArray.add(0); //Tôm
            dataArray.add(0); //Oocs
            dataArray.add(0); //Cá
            String itemMayDo = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //Oocs
            dataArray.add(0); //Cá
            String dkhi = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //id nhiệm vụ
            dataArray.add(0); //index nhiệm vụ con
            dataArray.add(0); //số lượng đã làm
            String task = dataArray.toJSONString();
            dataArray.clear();

            String mabuEgg = dataArray.toJSONString();
            String billEgg = dataArray.toJSONString();

            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ
            dataArray.add(System.currentTimeMillis()); //bùa mạnh mẽ
            dataArray.add(System.currentTimeMillis()); //bùa da trâu
            dataArray.add(System.currentTimeMillis()); //bùa oai hùng
            dataArray.add(System.currentTimeMillis()); //bùa bất tử
            dataArray.add(System.currentTimeMillis()); //bùa dẻo dai
            dataArray.add(System.currentTimeMillis()); //bùa thu hút
            dataArray.add(System.currentTimeMillis()); //bùa đệ tử
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x3
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x4
            String charms = dataArray.toJSONString();
            dataArray.clear();

            int[] skillsArr = gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 19, 24}
                    : gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 19, 26}
                    : new int[]{4, 5, 8, 13, 14, 21, 23, 19, 25};
            //[{"temp_id":"4","point":0,"last_time_use":0},]
            JSONArray skill = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                skill.add(skillsArr[i]); //id skill
                if (i == 0) {
                    skill.add(1); //level skill
                } else {
                    skill.add(0); //level skill
                }
                skill.add(0); //thời gian sử dụng trước đó
                dataArray.add(skill.toString());
                skill.clear();
            }
            String skills = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            String skillsShortcut = dataArray.toJSONString();
            dataArray.clear();

            String petData = dataArray.toJSONString();

            JSONArray blackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                blackBall.add(0);
                blackBall.add(0);
                blackBall.add(0);
                dataArray.add(blackBall.toJSONString());
                blackBall.clear();
            }
            String dataBlackBall = dataArray.toString();
            dataArray.clear();

            dataArray.add(-1); //id side task
            dataArray.add(0); //thời gian nhận
            dataArray.add(0); //số lượng đã làm
            dataArray.add(0); //số lượng cần làm
            dataArray.add(20); //số nhiệm vụ còn lại có thể nhận
            dataArray.add(0); //mức độ nhiệm vụ
            String dataSideTask = dataArray.toJSONString();
            dataArray.clear();

            String data_card = dataArray.toJSONString();
            String bill_data = dataArray.toJSONString();
            JSONObject achievementObject = new JSONObject();
            achievementObject.put("numPvpWin", 0);
            achievementObject.put("numSkillChuong", 0);
            achievementObject.put("numFly", 0);
            achievementObject.put("numKillMobFly", 0);
            achievementObject.put("numKillNguoiRom", 0);
            achievementObject.put("numHourOnline", 0);
            achievementObject.put("numGivePea", 0);
            achievementObject.put("numSellItem", 0);
            achievementObject.put("numPayMoney", 0);
            achievementObject.put("numKillSieuQuai", 0);
            achievementObject.put("numHoiSinh", 0);
            achievementObject.put("numSkillDacBiet", 0);
            achievementObject.put("numPickGem", 0);

            List<Boolean> list = new ArrayList<>(Arrays.asList(new Boolean[Manager.ACHIEVEMENTS.size()]));
            Collections.fill(list, Boolean.FALSE);
            dataArray.addAll(list);
            achievementObject.put("listReceiveGem", dataArray);
            String info_achive = achievementObject.toJSONString();

            String info_phoban = "[]";
            String info_day = "[]";
            GirlkunDB.executeUpdate("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time, data_item_time_sieu_cap,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet,"
                    + "data_black_ball, data_side_task, data_card, bill_data, info_phoban, data_may_do, info_achievement,data_duoi_khi,data_banh, data_offtrain, data_item_super, data_sk, data_nap,data_dh,isdh,data_dh1,isdh1,info_day,data_bingo,event,quavip,pointboss) "
                    + "values ()", userId, name, hair, gender, 0, -1, inventory, location, point, magicTree,
                    itemsBody, itemsBag, itemsBox, itemsBoxLuckyRound, friends, enemies, intrinsic,
                    itemTime, itemTimeSC, task, mabuEgg, charms, skills, skillsShortcut, petData, dataBlackBall, dataSideTask, data_card, bill_data, info_phoban, itemMayDo, info_achive, dkhi, itemBanh, dataoff, itemSP, itemSK, data_nap, data_dh, isdh, data_dh1, isdh1, info_day, data_bingo, 0, 0, 0);
            Logger.success("Tạo player mới thành công!");
            return true;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi tạo player mới");
            return false;
        }
    }

    // bảo nó tạo nv mới
    public static void updatePlayer(Player player) {
        if (player.isBot) {
            return;
        }
        if (player.iDMark.isLoadedAllDataPlayer()) {
            long st = System.currentTimeMillis();
            try {
                JSONArray dataArray = new JSONArray();

                //data kim lượng
                dataArray.add(player.inventory.gold > Inventory.LIMIT_GOLD
                        ? Inventory.LIMIT_GOLD : player.inventory.gold);
                dataArray.add(player.inventory.gem);
                dataArray.add(player.inventory.ruby);
                dataArray.add(player.inventory.coupon);//để im nào
                dataArray.add(player.inventory.event);
                String inventory = dataArray.toJSONString();
                dataArray.clear();

                //data nạp
                dataArray.add(player.isnapt2);
                dataArray.add(player.isnapt3);
                dataArray.add(player.isnapt4);
                dataArray.add(player.isnapt5);
                dataArray.add(player.isnapt6);
                dataArray.add(player.isnapt7);
                dataArray.add(player.isnapCN);
                dataArray.add(player.isnapTUAN);
                String data_nap = dataArray.toJSONString();
                dataArray.clear();
                int mapId = -1;
                mapId = player.mapIdBeforeLogout;
                int x = player.location.x;
                int y = player.location.y;
                int hp = Util.CuongGH(player.nPoint.hp);
                int mp = Util.CuongGH(player.nPoint.mp);
                if (player.isDie()) {
                    mapId = player.gender + 21;
                    x = 300;
                    y = 336;
                    hp = 1;
                    mp = 1;
                } else {
                    if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                            || MapService.gI().isMapBanDoKhoBau(mapId)
                            || MapService.gI().isMapKhiGas(mapId) || MapService.gI().isMapConDuongRanDoc(mapId) || MapService.gI().isMapMaBu(mapId) || MapService.gI().isnguhs(mapId)) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                    }
                }

                //data vị trí
                dataArray.add(mapId);
                dataArray.add(x);
                dataArray.add(y);
                String location = dataArray.toJSONString();
                dataArray.clear();

                //data chỉ số
                dataArray.add(player.nPoint.limitPower);
                dataArray.add(player.nPoint.power);
                dataArray.add(player.nPoint.tiemNang);
                dataArray.add(player.nPoint.stamina);
                dataArray.add(player.nPoint.maxStamina);
                dataArray.add(player.nPoint.hpg);
                dataArray.add(player.nPoint.mpg);
                dataArray.add(player.nPoint.dameg);
                dataArray.add(player.nPoint.defg);
                dataArray.add(player.nPoint.critg);
                dataArray.add(0);
                dataArray.add(hp);
                dataArray.add(mp);
                dataArray.add(player.numKillSieuHang);
                dataArray.add(player.rankSieuHang);

                String point = dataArray.toJSONString();
                dataArray.clear();

                //data đậu thần
                dataArray.add(player.magicTree.level);
                dataArray.add(player.magicTree.currPeas);
                dataArray.add(player.magicTree.isUpgrade ? 1 : 0);
                dataArray.add(player.magicTree.lastTimeHarvest);
                dataArray.add(player.magicTree.lastTimeUpgrade);
                String magicTree = dataArray.toJSONString();
                dataArray.clear();

                //data body
                JSONArray dataItem = new JSONArray();
                for (Item item : player.inventory.itemsBody) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBody = dataArray.toJSONString();
                dataArray.clear();

                //data bag
                for (Item item : player.inventory.itemsBag) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBag = dataArray.toJSONString();
                dataArray.clear();

                //data card
                //data box
                for (Item item : player.inventory.itemsBox) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBox = dataArray.toJSONString();
                dataArray.clear();

                //data box crack ball
                for (Item item : player.inventory.itemsBoxCrackBall) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBoxLuckyRound = dataArray.toJSONString();
                dataArray.clear();

                //data bạn bè
                JSONArray dataFE = new JSONArray();
                for (Friend f : player.friends) {
                    dataFE.add(f.id);
                    dataFE.add(f.name);
                    dataFE.add(f.head);
                    dataFE.add(f.body);
                    dataFE.add(f.leg);
                    dataFE.add(f.bag);
                    dataFE.add(f.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String friend = dataArray.toJSONString();
                dataArray.clear();

                //data kẻ thù
                for (Friend e : player.enemies) {
                    dataFE.add(e.id);
                    dataFE.add(e.name);
                    dataFE.add(e.head);
                    dataFE.add(e.body);
                    dataFE.add(e.leg);
                    dataFE.add(e.bag);
                    dataFE.add(e.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String enemy = dataArray.toJSONString();
                dataArray.clear();

                //data nội tại
                dataArray.add(player.playerIntrinsic.intrinsic.id);
                dataArray.add(player.playerIntrinsic.intrinsic.param1);
                dataArray.add(player.playerIntrinsic.intrinsic.param2);
                dataArray.add(player.playerIntrinsic.countOpen);
                String intrinsic = dataArray.toJSONString();
                dataArray.clear();

                //data item time
                dataArray.add((player.itemTime.isUseBoHuyet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) : 0));
                dataArray.add((player.itemTime.isOpenPower ? (ItemTime.TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) : 0));
                dataArray.add((player.itemTime.isUseMayDo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) : 0));
                dataArray.add((player.itemTime.isEatMeal ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) : 0));
                dataArray.add(player.itemTime.iconMeal);
                dataArray.add((player.itemTime.isUseTDLT ? ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000) : 0));

                String itemTime = dataArray.toJSONString();
                dataArray.clear();

                //data item time sc
                dataArray.add((player.itemTime.isUseBoHuyet2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh2)) : 0));
                String itemTimeSC = dataArray.toJSONString();
                dataArray.clear();

                //data item time sc
                dataArray.add((player.itemTime.isUse1trung ? (ItemTime.TIME_BANH - (System.currentTimeMillis() - player.itemTime.lastTime1trung)) : 0));
                dataArray.add((player.itemTime.isUse2trung ? (ItemTime.TIME_BANH - (System.currentTimeMillis() - player.itemTime.lastTime2trung)) : 0));
                dataArray.add((player.itemTime.isUseDacbiet ? (ItemTime.TIME_BANH - (System.currentTimeMillis() - player.itemTime.lastTimeDacbiet)) : 0));
                String itemBanh = dataArray.toJSONString();
                dataArray.clear();

                //data item time sp
                dataArray.add((player.itemTime.isUsesp1 ? (ItemTime.TIME_SUPER - (System.currentTimeMillis() - player.itemTime.lastTimesp1)) : 0));
                dataArray.add((player.itemTime.isUsesp3 ? (ItemTime.TIME_SUPER - (System.currentTimeMillis() - player.itemTime.lastTimesp2)) : 0));
                dataArray.add((player.itemTime.isUsesp3 ? (ItemTime.TIME_SUPER - (System.currentTimeMillis() - player.itemTime.lastTimesp3)) : 0));
                String itemSP = dataArray.toJSONString();
                dataArray.clear();

                //data item time sp
                dataArray.add(player.dh1);
                dataArray.add(player.dh2);
                dataArray.add(player.dh3);
                String dh = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.dh4);
                dataArray.add(player.dh5);
                dataArray.add(player.dh6);
                String dh1 = dataArray.toJSONString();
                dataArray.clear();

                //data item time sp
                dataArray.add(player.isdh1);
                dataArray.add(player.isdh2);
                dataArray.add(player.isdh3);
                String isdh = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.isdh4);
                dataArray.add(player.isdh5);
                dataArray.add(player.isdh6);
                String isdh1 = dataArray.toJSONString();
                dataArray.clear();

                //data item SK
                dataArray.add((player.itemTime.isUsesk ? (ItemTime.TIME_SK - (System.currentTimeMillis() - player.itemTime.lastTimesk)) : 0));
                String itemSK = dataArray.toJSONString();
                dataArray.clear();
                //data duoi khi
                dataArray.add((player.itemTime.isdkhi ? (ItemTime.TIME_DUOI_KHI - (System.currentTimeMillis() - player.itemTime.lastTimedkhi)) : 0));
                dataArray.add(player.itemTime.icondkhi);
                String timeduoikhi = dataArray.toJSONString();
                dataArray.clear();
                //data item may dò
                dataArray.add((player.itemTime.isUseMaydoCua ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoCua)) : 0));
                dataArray.add((player.itemTime.isUseMaydoTom ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoTom)) : 0));
                dataArray.add((player.itemTime.isUseMaydoOc ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoOc)) : 0));
                dataArray.add((player.itemTime.isUseMaydoCa ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeMaydoCa)) : 0));
                String itemMayDo = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ
                dataArray.add(player.playerTask.taskMain.id);
                dataArray.add(player.playerTask.taskMain.index);
                dataArray.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                String task = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ hàng ngày
                dataArray.add(player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                dataArray.add(player.playerTask.sideTask.receivedTime);
                dataArray.add(player.playerTask.sideTask.count);
                dataArray.add(player.playerTask.sideTask.maxCount);
                dataArray.add(player.playerTask.sideTask.leftTask);
                dataArray.add(player.playerTask.sideTask.level);
                String sideTask = dataArray.toJSONString();
                dataArray.clear();

                //data trứng bư
                if (player.mabuEgg != null) {
                    dataArray.add(player.mabuEgg.lastTimeCreate);
                    dataArray.add(player.mabuEgg.timeDone);
                }
                String mabuEgg = dataArray.toJSONString();
                dataArray.clear();

                //data trứng bill
                if (player.billEgg != null) {
                    dataArray.add(player.billEgg.lastTimeCreate);
                    dataArray.add(player.billEgg.timeDone);
                }
                String billEgg = dataArray.toJSONString();
                dataArray.clear();

                //data trung linh thu
                if (player.trunglinhthu != null) {
                    dataArray.add(player.trunglinhthu.lastTimeCreate);
                    dataArray.add(player.trunglinhthu.timeDone);
                }
                String trunglinhthu = dataArray.toJSONString();
                dataArray.clear();

                //data bùa
                dataArray.add(player.charms.tdTriTue);
                dataArray.add(player.charms.tdManhMe);
                dataArray.add(player.charms.tdDaTrau);
                dataArray.add(player.charms.tdOaiHung);
                dataArray.add(player.charms.tdBatTu);
                dataArray.add(player.charms.tdDeoDai);
                dataArray.add(player.charms.tdThuHut);
                dataArray.add(player.charms.tdDeTu);
                dataArray.add(player.charms.tdTriTue3);
                dataArray.add(player.charms.tdTriTue4);
                String charm = dataArray.toJSONString();
                dataArray.clear();

                //data skill
                JSONArray dataSkill = new JSONArray();
                for (Skill skill : player.playerSkill.skills) {
                    dataSkill.add(skill.template.id);
                    dataSkill.add(skill.point);
                    dataSkill.add(skill.lastTimeUseThisSkill);
                    dataArray.add(dataSkill.toJSONString());
                    dataSkill.clear();
                }
                String skills = dataArray.toJSONString();
                dataArray.clear();
                dataArray.clear();

                //data skill shortcut
                for (int skillId : player.playerSkill.skillShortCut) {
                    dataArray.add(skillId);
                }
                String skillShortcut = dataArray.toJSONString();
                dataArray.clear();

                String pet = dataArray.toJSONString();
                String petInfo = dataArray.toJSONString();
                String petPoint = dataArray.toJSONString();
                String petBody = dataArray.toJSONString();
                String petSkill = dataArray.toJSONString();

                //data vị trí
                dataArray.add(player.typetrain);
                dataArray.add(player.istrain ? 1 : 0);
                String dataoff = dataArray.toJSONString();
                dataArray.clear();
                //data pet
                if (player.pet != null) {
                    dataArray.add(player.pet.typePet);
                    dataArray.add(player.pet.gender);
                    dataArray.add(player.pet.name);
                    dataArray.add(player.fusion.typeFusion);
                    int timeLeftFusion = (int) (Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                    dataArray.add(timeLeftFusion < 0 ? 0 : timeLeftFusion);
                    dataArray.add(player.pet.status);
                    petInfo = dataArray.toJSONString();
                    dataArray.clear();

                    dataArray.add(player.pet.nPoint.limitPower);
                    dataArray.add(player.pet.nPoint.power);
                    dataArray.add(player.pet.nPoint.tiemNang);
                    dataArray.add(player.pet.nPoint.stamina);
                    dataArray.add(player.pet.nPoint.maxStamina);
                    dataArray.add(player.pet.nPoint.hpg);
                    dataArray.add(player.pet.nPoint.mpg);
                    dataArray.add(player.pet.nPoint.dameg);
                    dataArray.add(player.pet.nPoint.defg);
                    dataArray.add(player.pet.nPoint.critg);
                    dataArray.add(player.pet.nPoint.hp);
                    dataArray.add(player.pet.nPoint.mp);
                    petPoint = dataArray.toJSONString();
                    dataArray.clear();

                    JSONArray items = new JSONArray();
                    JSONArray options = new JSONArray();
                    JSONArray opt = new JSONArray();
                    for (Item item : player.pet.inventory.itemsBody) {
                        if (item.isNotNullItem()) {
                            dataItem.add(item.template.id);
                            dataItem.add(item.quantity);
                            for (Item.ItemOption io : item.itemOptions) {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                                options.add(opt.toJSONString());
                                opt.clear();
                            }
                            dataItem.add(options.toJSONString());
                        } else {
                            dataItem.add(-1);
                            dataItem.add(0);
                            dataItem.add(options.toJSONString());
                        }

                        dataItem.add(item.createTime);

                        items.add(dataItem.toJSONString());
                        dataItem.clear();
                        options.clear();
                    }
                    petBody = items.toJSONString();

                    JSONArray petSkills = new JSONArray();
                    for (Skill s : player.pet.playerSkill.skills) {
                        JSONArray pskill = new JSONArray();
                        if (s.skillId != -1) {
                            pskill.add(s.template.id);
                            pskill.add(s.point);
                        } else {
                            pskill.add(-1);
                            pskill.add(0);
                        }
                        petSkills.add(pskill.toJSONString());
                    }
                    petSkill = petSkills.toJSONString();

                    dataArray.add(petInfo);
                    dataArray.add(petPoint);
                    dataArray.add(petBody);
                    dataArray.add(petSkill);
                    pet = dataArray.toJSONString();
                }
                dataArray.clear();
                String info_day = "[" + player.lastTimeNhanQua + "," + player.nhanqua + "]";
                //data thưởng ngọc rồng đen
                for (int i = 0; i < player.rewardBlackBall.timeOutOfDateReward.length; i++) {
                    JSONArray dataBlackBall = new JSONArray();
                    dataBlackBall.add(player.rewardBlackBall.timeOutOfDateReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.lastTimeGetReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.quantilyBlackBall[i]);
                    dataArray.add(dataBlackBall.toJSONString());
                    dataBlackBall.clear();
                }
                String dataBlackBall = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add((player.itemTime.isWish1Bingo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lasttimewish1Bingo)) : 0));
                dataArray.add((player.itemTime.isWish2Bingo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimewish2biNgo)) : 0));
                dataArray.add((player.itemTime.isWish3Bingo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lasttimeWish3Bingo)) : 0));
                String bingo_time = dataArray.toJSONString();
                dataArray.clear();

                JSONObject achievementObject = new JSONObject();
                achievementObject.put("numPvpWin", player.achievement.numPvpWin);
                achievementObject.put("numSkillChuong", player.achievement.numSkillChuong);
                achievementObject.put("numFly", player.achievement.numFly);
                achievementObject.put("numKillMobFly", player.achievement.numKillMobFly);
                achievementObject.put("numKillNguoiRom", player.achievement.numKillNguoiRom);
                achievementObject.put("numHourOnline", player.achievement.numHourOnline);
                achievementObject.put("numGivePea", player.achievement.numGivePea);
                achievementObject.put("numSellItem", player.achievement.numSellItem);
                achievementObject.put("numPayMoney", player.achievement.numPayMoney);
                achievementObject.put("numKillSieuQuai", player.achievement.numKillSieuQuai);
                achievementObject.put("numHoiSinh", player.achievement.numHoiSinh);
                achievementObject.put("numSkillDacBiet", player.achievement.numSkillDacBiet);
                achievementObject.put("numPickGem", player.achievement.numPickGem);

                dataArray.addAll(player.achievement.listReceiveGem);
                achievementObject.put("listReceiveGem", dataArray);
                String info_achive = achievementObject.toJSONString();

                String info_phoban = "[" + player.bdkb_lastTimeJoin + "," + player.bdkb_countPerDay + "]";

                String query = " update player set  data_bingo = ? ,info_achievement =?, data_may_do = ?, khi_gas =?, data_item_time_sieu_cap = ?,head = ?, have_tennis_space_ship = ?,"
                        + "clan_id_sv" + Manager.SERVER + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                        + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, friends = ?,"
                        + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_item_time_sieu_cap = ?, data_task = ?, data_mabu_egg = ?, pet = ?,"
                        + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?,"
                        + " skills_shortcut = ?, pointPvp=?, NguHanhSonPoint=?,data_card=?,bill_data =? ,info_phoban =? ,data_duoi_khi =?, data_banh =?, data_offtrain =?, data_item_super =?, data_sk =?, data_nap =?, data_dh =?, isdh =?,info_day = ?, nhanqua = ?, event=?, quavip = ?, pointboss = ?, rank_sieu_hang = ?, nhanqua1 = ?, nhanqua2 = ?, nhanqua3 = ?,nhanqua4 = ?, nhanqua5 = ?, data_dh1 = ?, isdh1 = ?, trunglinhthu = ? where id = ?";
                GirlkunDB.executeUpdate(query,
                        bingo_time,
                        info_achive,
                        itemMayDo,
                        player.khigas,
                        itemTimeSC,
                        player.head,
                        player.haveTennisSpaceShip,
                        (player.clan != null ? player.clan.id : -1),
                        inventory,
                        location,
                        point,
                        magicTree,
                        itemsBody,
                        itemsBag,
                        itemsBox,
                        itemsBoxLuckyRound,
                        friend,
                        enemy,
                        intrinsic,
                        itemTime,
                        itemTimeSC,
                        task,
                        mabuEgg,
                        pet,
                        dataBlackBall,
                        sideTask,
                        charm,
                        skills,
                        skillShortcut,
                        player.pointPvp,
                        player.NguHanhSonPoint,
                        JSONValue.toJSONString(player.Cards),
                        billEgg,
                        info_phoban,
                        timeduoikhi,
                        itemBanh,
                        dataoff,
                        itemSP,
                        "[" + (player.itemTime.isUsesk ? (ItemTime.TIME_SK - (System.currentTimeMillis() - player.itemTime.lastTimesk)) : 0) + "]",
                        data_nap,
                        dh,
                        isdh,
                        info_day,
                        player.isqua,
                        player.pointevent,
                        player.isquavip,
                        player.pointboss,
                        player.rankSieuHang,
                        player.isqua1,
                        player.isqua2,
                        player.isqua3,
                        player.isqua4,
                        player.isqua5,
                        dh1,
                        isdh1,
                        trunglinhthu,
                        player.id);
                Logger.success("Total time save player " + player.name + " thành công! " + (System.currentTimeMillis() - st) + "\n");
            } catch (Exception e) {
                Logger.logException(PlayerDAO.class, e, "Lỗi save player " + player.name);
            }
        }
    }

    public static boolean subGoldBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().goldBar -= num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
            return false;
        } finally {
        }
        return false;
    }

    public static boolean subcoinBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set coin = (coin - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().coinBar -= num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
            return false;
        } finally {
        }
        if (num > 1000) {
            insertHistoryGold(player, num);
        }
        return true;
    }

    public static boolean addcoinBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set coin = (coin + ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().coinBar += num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
            return false;
        } finally {
        }
        if (num > 1000) {
            insertHistoryGold(player, num);
        }
        return true;
    }

    public static boolean addvnd(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set tongnap = (tongnap + ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().vnd += num;
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update tongnap " + player.name);
            return false;
        } finally {
        }
        if (num > 1000) {
            insertHistoryGold(player, num);
        }
        return true;
    }

//    public static boolean subvnd(Player player, int num) {
//        PreparedStatement ps = null;
//        try (Connection con = GirlkunDB.getConnection();) {
//            ps = con.prepareStatement("update account set coin = (coin - ?), active = ? where id = ?");
//            ps.setInt(1, num);
//            ps.setInt(2, player.getSession().actived ? 1 : 0);
//            ps.setInt(3, player.getSession().userId);
//            ps.executeUpdate();
//            ps.close();
//            con.close();
//            player.getSession().vnd -= num;
//        } catch (Exception e) {
//            Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
//            return false;
//        } finally {
//        }
//        if (num > 1000) {
//            insertHistoryGold(player, num);
//        }
//        return true;
//    }
    public static boolean subvnd(Player player, int num) {
    if (player.getSession() == null) {
        Logger.log(PlayerDAO.class.getName(), "Session is null for player: " + player.name);
        return false;
    }

    try (Connection con = GirlkunDB.getConnection();
         PreparedStatement ps = con.prepareStatement("update account set coin = (coin - ?), active = ? where id = ?")) {
        ps.setInt(1, num);
        ps.setInt(2, player.getSession().actived ? 1 : 0);
        ps.setInt(3, player.getSession().userId);

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            player.getSession().vnd -= num;
            if (num > 1000) {
                insertHistoryGold(player, num);
            }
            return true;
        } else {
            Logger.log(PlayerDAO.class.getName(), "No rows updated for player: " + player.name);
            return false;
        }
    } catch (Exception e) {
        Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
        return false;
    }
}
//    public static boolean subvnd(Player player, int num) {//Zalo: 0358124452//Name: EMTI 
//        try {//Zalo: 0358124452//Name: EMTI 
//            PreparedStatement ps = null;
//            Connection con = GirlkunDB.getConnection();
//            ps = con.prepareStatement("update account set coin = (coin - ?) where id = ?");
//            ps.setInt(1, num);
//            ps.setInt(2, player.getSession().userId);
//            ps.executeUpdate();
//            player.getSession().vnd -= num;
//            ps.close();
//            con.close();
//            return true;
//        } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
//            e.printStackTrace();
//            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
//            return false;
//        }
//
//    }

    public static boolean setIs_gift_box(Player player) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set is_gift_box = 0 where id = ?");
            ps.setInt(1, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update new_reg " + player.name);
            return false;
        }
        return true;
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{" + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (Item.ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean insertHistoryGold(Player player, int quantily) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into history_gold(name,gold) values (?,?)");
            ps.setString(1, player.name);
            ps.setInt(2, quantily);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(PlayerDAO.class, e, "Lỗi insert history_gold " + player.name);
            return false;
        }
        return true;
    }

    public static boolean checkLogout(Connection con, Player player) {
        long lastTimeLogout = 0;
        long lastTimeLogin = 0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from account where id = ? limit 1");
            ps.setInt(1, player.getSession().userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            return false;
        }
        return lastTimeLogout > lastTimeLogin;
    }

    public static void LogNapTIen(String uid, String menhgia, String seri, String code, String tranid) {
        String UPDATE_PASS = "INSERT INTO naptien(uid,sotien,seri,code,loaithe,time,noidung,tinhtrang,tranid,magioithieu) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection conn = GirlkunDB.getConnection();
            PreparedStatement ps = null;
            //UPDATE NRSD,
            ps = conn.prepareStatement(UPDATE_PASS);
            conn.setAutoCommit(false);
            //NGOC RONG SAO DEN
            ps.setString(1, uid);
            ps.setString(2, menhgia);
            ps.setString(3, seri);
            ps.setString(4, code);

            ps.setString(5, "VIETTEL");
            ps.setString(6, "123123123123");
            ps.setString(7, "dang nap the");
            ps.setString(8, "0");
            ps.setString(9, tranid);
            ps.setString(10, "0");
            if (ps.executeUpdate() == 1) {
            }

            conn.commit();
            //UPDATE NRSD
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

}
