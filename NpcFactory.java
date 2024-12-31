package com.girlkun.models.npc;

//import com.arriety.MaQuaTang.MaQuaTangManager;
import com.arriety.kygui.ItemKyGui;
import com.arriety.kygui.ShopKyGuiService;
import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.Zone;
import com.arriety.models.map.bdkb.BanDoKhoBau;
import com.arriety.models.map.bdkb.BanDoKhoBauService;
import com.girlkun.models.map.blackball.BlackBallWar;
import com.girlkun.models.map.challenge.MartialCongressService;
import com.arriety.models.map.doanhtrai.DoanhTrai;
import com.arriety.models.map.doanhtrai.DoanhTraiService;
//import com.arriety.models.map.khigas.KhiGas;
//import com.arriety.models.map.khigas.KhiGasService;
import com.arriety.models.map.nguhanhson.nguhs;
import com.girlkun.data.DataGame;
import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.boss.list_boss.TrainOffline.MeoThan;
import com.girlkun.models.map.ConDuongRanDoc.ConDuongRanDoc;
import com.girlkun.models.map.ConDuongRanDoc.ConDuongRanDocService;
import com.girlkun.models.map.MapHiru22h.MapHiru;
import com.girlkun.models.map.daihoi.DaiHoiManager;
import com.girlkun.models.map.gas.Gas;
import com.girlkun.models.map.gas.GasService;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.matches.TOP;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.npc.specialnpc.BillEgg;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.*;
import com.girlkun.services.func.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static com.girlkun.services.func.SummonDragon.*;
import java.sql.Connection;
import java.time.DayOfWeek;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import java.time.LocalDate;
import java.util.Random;
import jdk.internal.org.jline.utils.ShutdownHooks;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    private static Npc TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu Hạng\n\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật\nHãy thi đấu để khẳng định đẳng cấp của mình nhé", "Top 100\nCao thủ\n", "Hướng\ndẫn\nthêm", "Đấu ngay\n", "Về\nĐại Hội\nVõ Thuật");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    try (Connection con = GirlkunDB.getConnection()) {
//                                        Manager.topSieuHang = Manager.realTopSieuHang(con);
                                } catch (Exception ignored) {
                                    Logger.error("Lỗi đọc top");
                                }
//                                    Service.gI().showListTop(player, Manager.topSieuHang, (byte) 1);
                                break;
                                case 2:
                                    List<TOP> tops = new ArrayList<>();
                                    tops.addAll(Manager.realTopSieuHang(player));
                                    Service.gI().showListTop(player, tops, (byte) 1);
                                    tops.clear();
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng", "Đổi Trứng\nLinh thú", "Nâng Chiến Linh", "Mở chỉ số ẩn\nChiến Linh", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                        //       npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }

                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.Nang_Chien_Linh);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_Chien_Linh);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.Nang_Chien_Linh:
                                case CombineServiceNew.MO_CHI_SO_Chien_Linh:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nshop", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            if (pl.nPoint.power < 17000000000L) {
                                Service.gI().sendThongBao(pl, "Vui lòng đạt 17 tỷ sức mạnh và đạt nhiệm vụ 22");
                            } else if (pl.playerTask.taskMain.id < 17) {
                                Service.gI().sendThongBao(pl, "Vui lòng đạt nhiệm vụ 30");
                            } else {
                                ShopKyGuiService.gI().openShopKyGui(pl);
                            }
                            break;

                    }
                }
            }
        };
    }

    public static Npc Nang(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Chào Mừng Các Bạn Đến Với Ngày 20/10 Bạn Muốn Gì?", "Đổi Đá Bảo Vệ", "Đổi Cải Trang", "Đổi Pet", "Random Cải Trang Vip 50 Thỏi Vàng", "Random Ván Bay vip 50 Thỏi Vàng", "Ramdom Pet đi theo 50 Thỏi Vàng", "RanDom Danh Hiệu Vip 100 Thỏi Vàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                            switch (select) {
                                case 0:
                                    if (honma != null && binhphep != null && lonuoc != null
                                            && honma.quantity >= 10) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item sphonma = ItemService.gI().createNewItem((short) 987, 1);
                                                InventoryServiceNew.gI().addItemBag(player, sphonma);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 10);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá Bảo Vệ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 10 Bông Hồng");
                                    }
                                    break;
                                case 1:
                                    if (honma != null && binhphep != null && lonuoc != null && buahs != null
                                            && honma.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 681, 1);
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 95) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(25, 25)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(207, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(25, 25)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(207, Util.nextInt(0, 0)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 99);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được Cải Trang");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 99 Bông Hồng");
                                    }
                                    break;
                                case 3:
                                    if (tv != null && tv != null && tv != null && tv != null
                                            && tv.quantity >= 50) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 681, 1);
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 95) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 33)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 33)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 33)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(25, 25)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(207, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 33)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 33)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 33)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(25, 25)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(207, Util.nextInt(0, 0)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 50);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được Cải Trang");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 Thỏi Vàng");
                                    }
                                    break;
                                case 4:
                                    if (tv != null && tv != null && tv != null && tv != null
                                            && tv.quantity >= 50) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 897, 1);
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 95) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 7)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 50);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được Ván Bay");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 Thỏi Vàng");
                                    }
                                    break;
                                case 5:
                                    if (tv != null && tv != null && tv != null && tv != null
                                            && tv.quantity >= 50) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) Util.nextInt(1311, 1313));
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 95) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 50);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 Thỏi Vàng");
                                    }
                                    break;
                                case 6:
                                    if (tv != null && tv != null && tv != null && tv != null
                                            && tv.quantity >= 100) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) Util.nextInt(1369, 1369));
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 90) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 7)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 7)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 100);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 Thỏi Vàng");
                                    }
                                    break;
                                case 2:
                                    int id = Util.nextInt(0, 100);
                                    if (honma != null && binhphep != null && lonuoc != null
                                            && honma.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item pet = ItemService.gI().createNewItem((short) Util.nextInt(1311, 1313));
                                                if (id <= 95) {
                                                    pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(3, 7)));
                                                    pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(3, 7)));
                                                    pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(3, 7)));
                                                    pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(3, 7)));
                                                    pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(3, 7)));
                                                    pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(3, 7)));
                                                }
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().addItemBag(player, pet);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 99);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + pet.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 99 Bông Hồng");
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc Valentin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn Muốn Gì", "Tặng Hoa", "+10k free", "VQUAY MAY MAN");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 1279);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 1279);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 1279);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 1279);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 1279);
                            Item honma1 = InventoryServiceNew.gI().findItemBag(player, 1275);
                            Item binhphep1 = InventoryServiceNew.gI().findItemBag(player, 1276);
                            Item lonuoc1 = InventoryServiceNew.gI().findItemBag(player, 1277);
                            Item buahs1 = InventoryServiceNew.gI().findItemBag(player, 1278);
                            Item tv1 = InventoryServiceNew.gI().findItemBag(player, 1279);
                            switch (select) {
                                case 0:
                                    if (honma != null && binhphep != null && lonuoc != null
                                            && honma.quantity >= 1) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item sukien = ItemService.gI().createNewItem((short) Util.nextInt(1275, 1278));
                                                InventoryServiceNew.gI().addItemBag(player, sukien);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 1);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 Điểm Sự kiện và " + sukien.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn Không Đủ Điều Kiện");
                                    }
                                    break;
                                case 1:
                                    if (honma1 != null && buahs1 != null && tv1 != null && binhphep1 != null && lonuoc1 != null
                                            && honma1.quantity >= 100 && buahs1.quantity >= 100 && tv1.quantity >= 100 && binhphep1.quantity >= 100 && lonuoc1.quantity >= 100) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                player.inventory.gold -= 0;
                                                PlayerDAO.addcoinBar(player, 10000);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma1, 20);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, buahs1, 20);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, 20);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, binhphep1, 20);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, lonuoc1, 20);
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 Điểm Sự kiện");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Cần "
                                                + "x100 Hộp Nhẫn \n"
                                                + "x100 Socola \n"
                                                + "x100 Mik \n"
                                                + "x100 Nước Hoa \n"
                                                + "x100 Hoa Hồng \n"
                                        );
                                    }
                                    break;
                                // code sự kiện thì để sau còn menu của fide ntn thì nt a
                                case 2:
                                    if (player.getSession().coinBar >= 20000) {
                                        PlayerDAO.subcoinBar(player, 20000);
                                        Item sukien = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                        sukien.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 15)));
                                        sukien.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 15)));
                                        sukien.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 15)));
                                        sukien.itemOptions.add(new Item.ItemOption(5, Util.nextInt(5, 15)));
                                        sukien.itemOptions.add(new Item.ItemOption(30, Util.nextInt(0, 1)));
                                        InventoryServiceNew.gI().addItemBag(player, sukien);
                                        Service.gI().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được " + sukien.template.name);
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Cần "
                                                + "20k coin \n");
                                        break;
                                    }
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 3:
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc Valentin1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn Đổi Quà Không", "Đổi", "Không");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 1275);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 1276);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 1277);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 1278);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 1279);
                            switch (select) {
                                case 0:
                                    if (tv != null && honma != null && buahs != null && binhphep != null && lonuoc != null
                                            && tv.quantity >= 30 && honma.quantity >= 30 && buahs.quantity >= 30 && binhphep.quantity >= 30 && lonuoc.quantity >= 30) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item sukien = ItemService.gI().createNewItem((short) Util.nextInt(1274, 1274));
                                                InventoryServiceNew.gI().addItemBag(player, sukien);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 30);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 30);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, buahs, 30);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, binhphep, 30);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, lonuoc, 30);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 Điểm Sự kiện và " + sukien.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Cần "
                                                + "x30 Hộp Nhẫn \n"
                                                + "x30 Socola \n"
                                                + "x30 Mik \n"
                                                + "x30 Nước Hoa \n"
                                                + "x30 Hoa Hồng \n"
                                        );
                                    }
                                    break;
                                case 1:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 2:
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc SK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Con Đang Có " + player.pointevent + " Điểm Sự Kiện" + "\n|7|Con Có quy đổi điểm sự kiện không?", "350 Điểm", "500 Điểm", "700 Điểm", "1000 Điểm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 1275);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 1276);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 1277);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 1278);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 1279);
                            switch (select) {
                                case 0:
                                    if (player.pointevent >= 350) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 2) {
                                                player.pointevent -= 350;
                                                ItemService.gI().Open30days(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Cải Trang" + " Và 10 Thỏi Vàng");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Không Đủ Điểm Sự kiện"
                                        );
                                    }
                                    break;
                                case 1:
                                    if (player.pointevent >= 500) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 4) {
                                                player.pointevent -= 500;
                                                ItemService.gI().Open70days(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Cải Trang" + " Và 20 Thỏi Vàng" + " Và Đá Bảo Vệ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Không Đủ Điểm Sự kiện"
                                        );
                                    }
                                    break;
                                case 3:
                                    if (player.pointevent >= 1000) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                                player.pointevent -= 1000;
                                                ItemService.gI().Openvv(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn Nhận Được Quà");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Không Đủ Điểm Sự kiện"
                                        );
                                    }
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 2:
                                    if (player.pointevent >= 700) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 5) {
                                                player.pointevent -= 700;
                                                ItemService.gI().Open100days(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn Nhận Được Quà");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Không Đủ Điểm Sự kiện"
                                        );
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc Tien(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Con Đang Có " + player.getSession().coinBar + " Coin" + "\n|7|Con có Muốn Quay Vòng Quay hay đổi quà sự Kiện Không?", "Quay 10k", "Ốc", "Sò", "Cua", "Sao Biển");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item oc = InventoryServiceNew.gI().findItemBag(player, 695);
                            Item so = InventoryServiceNew.gI().findItemBag(player, 696);
                            Item cua = InventoryServiceNew.gI().findItemBag(player, 697);
                            Item saobien = InventoryServiceNew.gI().findItemBag(player, 698);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 1279);
                            switch (select) {
                                case 0:
                                    if (player.getSession().coinBar >= 10000) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                PlayerDAO.subcoinBar(player, 10000);
                                                ItemService.gI().Open10k(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Cải Trang");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Không Đủ Điểm Sự kiện"
                                        );
                                    }
                                    break;
                                case 1:
                                    if (oc != null && oc.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, oc, 99);
                                                ItemService.gI().Opensk(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Pet ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Chưa Đủ x99 Ốc"
                                        );
                                    }
                                    break;
                                case 3:
                                    if (cua != null && cua.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, cua, 99);
                                                ItemService.gI().Opensk(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Pet ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Chưa Đủ x99 Cua"
                                        );
                                    }
                                    break;
                                case 4:
                                    if (saobien != null && saobien.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, saobien, 99);
                                                ItemService.gI().Opensk(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Pet ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Chưa Đủ x99 Sao Biển"
                                        );
                                    }
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 2:
                                    if (so != null && so.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, so, 99);
                                                ItemService.gI().Opensk(player);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa Nhận được Pet ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBaoOK(player.getSession(), "Bạn Chưa Đủ x99 Sò"
                                        );
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc Moc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Con Đã Nạp Được " + player.getSession().vnd + "\n|7|Con Có Muốn Nhận Mốc Nạp Không?", "100k", "200k", "500k", "1 Triệu", "2 Triệu");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 1275);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 1276);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 1277);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 1278);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 1279);
                            switch (select) {
                                case 0:
                                    if (player.getSession().vnd >= 100000) {
                                        if (player.isqua1 == 0) {
                                            Item MTV = ItemService.gI().createNewItem((short) Util.nextInt(457, 457));
                                            Item MTV3 = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                            Item MTV2 = ItemService.gI().createNewItem((short) 1109);
                                            MTV2.quantity = Util.nextInt(10, 10);
                                            MTV.quantity = Util.nextInt(50, 50);
                                            InventoryServiceNew.gI().addItemBag(player, MTV);
                                            InventoryServiceNew.gI().addItemBag(player, MTV2);
                                            InventoryServiceNew.gI().addItemBag(player, MTV3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn Vừa Nhận Đuợc 50 " + MTV.template.name + " và 10 " + MTV2.template.name + " Và " + MTV3.template.name);
                                            player.isqua1 = 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn Chưa Nạp Đủ");
                                    }
                                    break;
                                case 1:
                                    if (player.getSession().vnd >= 200000) {
                                        if (player.isqua2 == 0) {
                                            Item MTV = ItemService.gI().createNewItem((short) Util.nextInt(457, 457));
                                            Item MTV3 = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                            Item MTV2 = ItemService.gI().createNewItem((short) 1109);
                                            MTV2.quantity = Util.nextInt(20, 20);
                                            MTV.quantity = Util.nextInt(150, 150);
                                            MTV3.quantity = Util.nextInt(1, 3);
                                            InventoryServiceNew.gI().addItemBag(player, MTV);
                                            InventoryServiceNew.gI().addItemBag(player, MTV2);
                                            InventoryServiceNew.gI().addItemBag(player, MTV3);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn Vừa Nhận Đuợc 150 " + MTV.template.name + " và 20 " + MTV2.template.name + " Và " + MTV3.template.name);
                                            player.isqua2 = 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn Chưa Nạp Đủ");
                                    }
                                    break;
                                case 3:
                                    if (player.getSession().vnd >= 1000000) {
                                        if (player.isqua4 == 0) {
                                            Item MTV = ItemService.gI().createNewItem((short) Util.nextInt(457, 457));
                                            Item MTV4 = ItemService.gI().createNewItem((short) Util.nextInt(1377, 1378));
                                            Item MTV5 = ItemService.gI().createNewItem((short) Util.nextInt(1438, 1439));
                                            Item MTV3 = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                            Item MTV2 = ItemService.gI().createNewItem((short) 1109);
                                            MTV2.quantity = Util.nextInt(100, 100);
                                            MTV4.itemOptions.add(new Item.ItemOption(215, 15));
                                            MTV5.itemOptions.add(new Item.ItemOption(215, 15));
                                            MTV.quantity = Util.nextInt(1000, 1000);
                                            MTV3.quantity = Util.nextInt(15, 15);
                                            InventoryServiceNew.gI().addItemBag(player, MTV);
                                            InventoryServiceNew.gI().addItemBag(player, MTV2);
                                            InventoryServiceNew.gI().addItemBag(player, MTV3);
                                            InventoryServiceNew.gI().addItemBag(player, MTV4);
                                            InventoryServiceNew.gI().addItemBag(player, MTV5);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn Vừa Nhận Đuợc 1000 " + MTV.template.name + " và 100 " + MTV2.template.name + " Và 15 " + MTV3.template.name + " Và " + MTV4.template.name + " Và " + MTV5.template.name);
                                            player.isqua4 = 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn Chưa Nạp Đủ");
                                    }
                                    break;
                                case 4:
                                    if (player.getSession().vnd >= 2000000) {
                                        if (player.isqua5 == 0) {
                                            Item MTV = ItemService.gI().createNewItem((short) Util.nextInt(457, 457));
                                            Item MTV4 = ItemService.gI().createNewItem((short) Util.nextInt(1377, 1378));
                                            Item MTV5 = ItemService.gI().createNewItem((short) Util.nextInt(1438, 1439));
                                            Item MTV6 = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                                            Item MTV7 = ItemService.gI().createNewItem((short) Util.nextInt(1236, 1236));
                                            Item MTV3 = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                            Item MTV2 = ItemService.gI().createNewItem((short) 1109);
                                            MTV2.quantity = Util.nextInt(500, 500);
                                            MTV4.itemOptions.add(new Item.ItemOption(215, 20));
                                            MTV5.itemOptions.add(new Item.ItemOption(215, 20));
                                            MTV6.itemOptions.add(new Item.ItemOption(215, 20));
                                            MTV7.itemOptions.add(new Item.ItemOption(50, 45));
                                            MTV7.itemOptions.add(new Item.ItemOption(77, 45));
                                            MTV7.itemOptions.add(new Item.ItemOption(103, 45));
                                            MTV7.itemOptions.add(new Item.ItemOption(5, 100));
                                            MTV7.itemOptions.add(new Item.ItemOption(106, 1));
                                            MTV7.itemOptions.add(new Item.ItemOption(30, 1));
                                            MTV.quantity = Util.nextInt(3000, 3000);
                                            MTV3.quantity = Util.nextInt(50, 50);
                                            InventoryServiceNew.gI().addItemBag(player, MTV);
                                            InventoryServiceNew.gI().addItemBag(player, MTV2);
                                            InventoryServiceNew.gI().addItemBag(player, MTV3);
                                            InventoryServiceNew.gI().addItemBag(player, MTV4);
                                            InventoryServiceNew.gI().addItemBag(player, MTV5);
                                            InventoryServiceNew.gI().addItemBag(player, MTV6);
                                            InventoryServiceNew.gI().addItemBag(player, MTV7);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn Đã dc nhận quà tích nạp");
                                            player.isqua5 = 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn Chưa Nạp Đủ");
                                    }
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 2:
                                    if (player.getSession().vnd >= 500000) {
                                        if (player.isqua3 == 0) {
                                            Item MTV = ItemService.gI().createNewItem((short) Util.nextInt(457, 457));
                                            Item MTV4 = ItemService.gI().createNewItem((short) Util.nextInt(1377, 1378));
                                            Item MTV3 = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                            Item MTV2 = ItemService.gI().createNewItem((short) 1109);
                                            MTV2.quantity = Util.nextInt(50, 50);
                                            MTV4.itemOptions.add(new Item.ItemOption(215, 10));
                                            MTV.quantity = Util.nextInt(500, 500);
                                            MTV3.quantity = Util.nextInt(5, 5);
                                            InventoryServiceNew.gI().addItemBag(player, MTV);
                                            InventoryServiceNew.gI().addItemBag(player, MTV2);
                                            InventoryServiceNew.gI().addItemBag(player, MTV3);
                                            InventoryServiceNew.gI().addItemBag(player, MTV4);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.gI().sendThongBao(player, "Bạn Vừa Nhận Đuợc 500 " + MTV.template.name + " và 50 " + MTV2.template.name + " Và 5 " + MTV3.template.name + " Và " + MTV4.template.name);
                                            player.isqua3 = 1;
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn đã nhận rồi");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn Chưa Nạp Đủ");
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc Noel(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Chào Mừng Các Bạn Đến Với Sự Kiện Noel Bạn Muốn Gì?", "Đổi Đá Bảo Vệ", "Đổi cải trang cùi", "Đổi ván bay", "Đổi cải trang vip");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                        case 0: //shop
//                 //          npcChat(player, "Giáng sinh anh lành!");
//                           NpcManager.addPlayerToNpcListUser(this.tempId,player);
//
//                            break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 533);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 649);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                            switch (select) {
                                case 0:
                                    if (honma != null && honma != null && honma != null
                                            && honma.quantity >= 90) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item sphonma = ItemService.gI().createNewItem((short) 987, 1);
                                                InventoryServiceNew.gI().addItemBag(player, sphonma);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 90);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá Bảo Vệ");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 90 kẹo");
                                    }
                                    break;
                                case 1:
                                    if (honma != null && honma != null && honma != null && honma != null
                                            && honma.quantity >= 999) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 1260, 1);
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 999);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được Hộp quà cải trang");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 999 kẹo");
                                    }
                                    break;
                                case 3:
                                    if (tv != null && binhphep != null && tv != null && tv != null
                                            && tv.quantity >= 50 && binhphep.quantity >= 30) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 648, 1);
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 50);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, binhphep, 30);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được hộp quà giáng sinh");
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 Thỏi Vàng và 30 tất giáng sinh");
                                    }
                                    break;

                                case 2:
                                    int id = Util.nextInt(0, 100);
                                    if (binhphep != null && binhphep != null && binhphep != null
                                            && binhphep.quantity >= 50) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item pet = ItemService.gI().createNewItem((short) Util.nextInt(746, 746));
                                                if (id <= 95) {
                                                    pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 5)));
                                                    pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 5)));
                                                    pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 5)));
                                                    pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 5)));
                                                    pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 5)));
                                                    pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 5)));
                                                }
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().addItemBag(player, pet);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, binhphep, 50);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + pet.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 tất giáng sinh");
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc BR(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn Muốn Gì?", "Đến Map UP VÀNG", "Đến Map Săn Boss", "Không");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 1396);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 1397);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                            Item dns = InventoryServiceNew.gI().findItemBag(player, 674);
                            switch (select) {
                                case 0:
                                    if (honma != null && dns != null && dns != null
                                            && honma.quantity >= 1
                                            && dns.quantity >= 300) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 0);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 300);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                if (!player.getSession().actived) {
                                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                                } else {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 253, -1, 123);
                                                }
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn cần có 300 đá ngũ sắc và 1 vé vàng ");
                                    }
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;

                                case 1:
                                    if (binhphep != null && binhphep != null && binhphep != null
                                            && binhphep.quantity >= 1) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, binhphep, 0);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                if (!player.getSession().actived) {
                                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                                } else {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 254, -1, 123);
                                                }
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa có vé boss");
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc broly(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn muốn gì", "Mở shop thường", "Shop mở thành viên", "Không");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                      //         npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 610);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_NGU_SAC", false);
                                    break;
                                case 1:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", false);
                                    }
                                    break;
                                case 2:
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc bi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU, "Chào Mừng Các Bạn Đến Với SK halowin Bạn Muốn Gì?", "random bí ngô 10tv", "random bí ngô 20tv", "đổi bí ngô", "đổi cải trang", "cải trang chỉ số max vip", "đổi quà", "Danh Hiệu Max Vip", "Đổi Thỏi Vàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                        //       npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            Item honma = InventoryServiceNew.gI().findItemBag(player, 1044);
                            Item binhphep = InventoryServiceNew.gI().findItemBag(player, 1044);
                            Item lonuoc = InventoryServiceNew.gI().findItemBag(player, 1044);
                            Item buahs = InventoryServiceNew.gI().findItemBag(player, 1044);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                            Item co = InventoryServiceNew.gI().findItemBag(player, 850);
                            switch (select) {
                                case 1:
                                    if (tv != null && tv != null && tv != null
                                            && tv.quantity >= 20) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item sphonma = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                                sphonma.quantity = Util.nextInt(10, 15);
                                                InventoryServiceNew.gI().addItemBag(player, sphonma);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 20);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được" + sphonma.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 20 Thỏi Vàng");
                                    }
                                    break;
                                case 0:
                                    if (tv != null && tv != null && tv != null
                                            && tv.quantity >= 10) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item sphonma = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                                sphonma.quantity = Util.nextInt(5, 10);
                                                InventoryServiceNew.gI().addItemBag(player, sphonma);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 10);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được" + sphonma.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 10 Thỏi Vàng");
                                    }
                                    break;
                                case 2:
                                    if (honma != null && honma != null && honma != null && honma != null
                                            && honma.quantity >= 2) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) Util.nextInt(702, 708));
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 90) {
                                                    ct.itemOptions.add(new Item.ItemOption(73, Util.nextInt(0, 0)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(73, Util.nextInt(0, 0)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 2);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 2 bí ngô");
                                    }
                                    break;
                                case 3:
                                    if (honma != null && honma != null && honma != null && honma != null
                                            && honma.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 739, 1);
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 90) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 32)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 32)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 32)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(15, 15)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(7, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 32)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 32)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 32)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(15, 15)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 30)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 99);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 99 bí ngô");
                                    }
                                    break;
                                case 4:
                                    if (tv != null && tv != null && tv != null && tv != null
                                            && tv.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) 742, 1);
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 80) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 37)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 37)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 37)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(20, 20)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(50, 50)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(7, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 37)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 37)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 37)));
                                                    ct.itemOptions.add(new Item.ItemOption(14, Util.nextInt(20, 20)));
                                                    ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(50, 50)));
                                                    ct.itemOptions.add(new Item.ItemOption(106, Util.nextInt(0, 0)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 99);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 99 Thỏi Vàng");
                                    }
                                    break;
                                case 5:
                                    if (honma != null && honma != null && honma != null && honma != null
                                            && honma.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) Util.nextInt(1370, 1370));
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 90) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 5)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 5)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 5)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 5)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 5)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 5)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honma, 99);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 99 Bí");
                                    }
                                    break;
                                case 6:
                                    if (tv != null && tv != null && tv != null && tv != null
                                            && tv.quantity >= 100) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item ct = ItemService.gI().createNewItem((short) Util.nextInt(1370, 1370));
                                                int id = Util.nextInt(0, 100);
                                                if (id <= 80) {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 30)));
                                                } else {
                                                    ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                                                    ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                                                }
                                                InventoryServiceNew.gI().addItemBag(player, ct);
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 100);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + ct.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 50 Thỏi Vàng");
                                    }
                                    break;
                                case 7:
                                    int id = Util.nextInt(0, 100);
                                    if (co != null && co != null && co != null
                                            && co.quantity >= 99) {
                                        if (player.inventory.gold >= 0) {
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= 1) {
                                                Item pet = ItemService.gI().createNewItem((short) Util.nextInt(457, 457));
                                                pet.quantity = Util.nextInt(1, 5);
                                                if (id <= 95) {
                                                    pet.itemOptions.add(new Item.ItemOption(73, Util.nextInt(0, 0)));
                                                } else {
                                                    pet.itemOptions.add(new Item.ItemOption(73, Util.nextInt(0, 0)));
                                                }
                                                player.inventory.gold -= 0;
                                                Service.gI().sendMoney(player);
                                                InventoryServiceNew.gI().addItemBag(player, pet);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, co, 99);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.gI().sendThongBao(player, "Bạn vừa nhận được " + pet.template.name);
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn chưa đủ ô trống trong hành trang");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10tr vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn chưa đủ 99 cỏ tươi");
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                        //       npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()},
                                                player.nPoint.dame,
                                                new long[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    MapHiru.gI().setTimeJoinMap22h();
                }
                if (this.mapId == 19) {
                    long now = System.currentTimeMillis();
                    if (now > MapHiru.TIME_OPEN_22h && now < MapHiru.TIME_CLOSE_22h) {
                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Phong ấn đã bị phá vỡ, "
                                + "Xin hãy cứu lấy người dân",
                                "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                    } else {
                        this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                "Ác quỷ truyền thuyết Hirudegarn đã thoát khỏi phong ấn ngàn năm nHãy giúp tôi chế ngự nó?",
                                "Hướng dẫn", "Từ chối");
                    }
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn bỏ chạy? ",
                            "Quay về", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                    //           npcChat(player, "Giáng sinh anh lành!");
//                   //            NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (this.mapId) {
                            case 19:
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MENU_REWARD_MMB:
                                    case ConstNpc.MENU_OPEN_MMB:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_CHI22H);
                                        }
                                        if (select == 1) {
                                            ChangeMapService.gI().changeMap(player, 212, -1, 100, 336);
                                        }
                                        break;
                                    case ConstNpc.MENU_NOT_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_2H);
                                        }
                                        break;
                                }
                                break;
                            case 212:

                                if (select == 0) {
                                    ChangeMapService.gI().changeMap(player, 19, -1, 100, 336);
                                }

                                break;
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?"
                                + "\n" + "|7| Số Người Đang online Là " + Client.gI().getPlayers().size(), "Giải tán bang hội", "Lãnh địa Bang Hội", "Kho báu dưới biển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
////                               npcChat(player, "Giáng sinh anh lành!");
////                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.BASE_MENU:
                                switch (select) {
                                    case 0:
                                        Clan clan = player.clan;
                                        if (clan != null) {
                                            ClanMember cm = clan.getClanMember((int) player.id);
                                            if (cm != null) {
                                                if (clan.members.size() > 1) {
                                                    Service.gI().sendThongBao(player, "Bang phải còn một người");
                                                    break;
                                                }
                                                if (!clan.isLeader(player)) {
                                                    Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                                    break;
                                                }
//                                        
                                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                        "Yes you do!", "Từ chối!");
                                            }
                                            break;
                                        }
                                        Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
                                        break;
                                    case 1:
                                        if (player.getSession().player.nPoint.power >= 40000000000L) {

                                            ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                        } else {
                                            this.npcChat(player, "Bạn chưa đủ 40 tỷ sức mạnh để vào");
                                        }
                                        break; // qua lanh dia
                                    case 2:
                                        if (player.clan == null) {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Vào bang hội trước", "Đóng");
                                            break;
                                        }
                                        if (player.clan.getMembers().size() < BanDoKhoBau.N_PLAYER_CLAN) {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                                            break;
                                        }
                                        if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Bản đồ kho báu chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                                    "OK");
                                            break;
                                        }

                                        if (player.bdkb_countPerDay >= 3) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Con đã đạt giới hạn lượt đi trong ngày",
                                                    "OK");
                                            break;
                                        }

                                        player.clan.banDoKhoBau_haveGone = !(System.currentTimeMillis() - player.clan.banDoKhoBau_lastTimeOpen > 300000);
                                        if (player.clan.banDoKhoBau_haveGone) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Bang hội của con đã đi Bản Đồ lúc " + TimeUtil.formatTime(player.clan.banDoKhoBau_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                    + "(" + player.clan.banDoKhoBau_playerOpen + "). Hẹn con sau 5 phút nữa", "OK");
                                            break;
                                        }
                                        if (player.clan.banDoKhoBau != null) {
                                            createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                    "Bang hội của con đang tham gia Bản đồ kho báu cấp độ " + player.clan.banDoKhoBau.level + "\n"
                                                    + "Thời gian còn lại là "
                                                    + TimeUtil.getSecondLeft(player.clan.banDoKhoBau.getLastTimeOpen(), BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000)
                                                    + " giây. Con có muốn tham gia không?",
                                                    "Tham gia", "Không");
                                            break;
                                        }
                                        Input.gI().createFormChooseLevelBDKB(player);
                                        break;
                                    case 3:
                                        if (player.nhanqua != 0) {
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Con đã nhận quà của hôm nay rồi",
                                                    "OK");
                                            break;
                                        }
                                        int[] random = new int[]{5, 14, 94, 108, 97, 106, 107};
                                        Random rnd = new Random();
                                        int index = rnd.nextInt(random.length);
                                        int result = random[index];
                                        if (!player.getSession().actived) {
                                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                        } else {
                                            Item ct = ItemService.gI().createNewItem((short) 457);
                                            ct.quantity = Util.nextInt(1, 1);
                                            Item HN = ItemService.gI().createNewItem((short) 861);
                                            HN.quantity = Util.nextInt(10000000, 10000000);
                                            ct.itemOptions.add(new Item.ItemOption(30, Util.nextInt(0, 1)));
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            player.nhanqua++;
                                            player.lastTimeNhanQua = System.currentTimeMillis();
                                            Service.gI().sendThongBao(player, "Bạn Nhận được quà");
                                        }
                                        break;
                                    case 4:
                                        Service.gI().showListTop(player, Manager.topEvent);
                                        break;
                                    case 200:
                                        //Service.gI().sendThongBaoOK(player,"Bú");
                                        if (player.getSession().player.nPoint.power < 180000000000L) {
                                            Service.gI().sendThongBaoOK(player, "Cần 180 Tỉ Sức Mạnh!");
                                        } else if (player.getSession().player.nPoint.hpg < 600000) {
                                            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (600000 - player.nPoint.hpg) + " HP Nữa");
                                        } else if (player.getSession().player.nPoint.mpg < 600000) {
                                            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (600000 - player.nPoint.mpg) + " KI Nữa");
                                        } else if (player.getSession().player.nPoint.dameg < 32000) {
                                            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (32000 - player.nPoint.dameg) + " SD Nữa");
                                        } else if (player.getSession().player.inventory.ruby < 20000) {
                                            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + (20000 - player.inventory.ruby) + " Hồng Ngọc");
                                        } else if (player.getSession().player.nPoint.dameg == 32000) {
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(40, 100)) {
                                                player.nPoint.power = 180000000000L;
                                                player.inventory.ruby = 3000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;

                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(40, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 3000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(15, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 20000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(15, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 20000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(10, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 20000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(10, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 20000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(5, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 20000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.getSession().player.nPoint.dameg == 32500) {
                                            Service.gI().sendMoney(player);
                                            player.inventory.ruby = 20000;
                                            Service.gI().sendThongBaoOK(player, "Chuyển Sinh không Thành Công!");
                                            if (Util.isTrue(5, 100)) {
                                                player.nPoint.power = 18000000000L;
                                                player.inventory.ruby = 3000;
                                                player.nPoint.dameg = 32500;
                                                player.nPoint.hpg = 660000;
                                                player.nPoint.mpg = 660000;
                                                Client.gI().kickSession(player.getSession());
                                            }
                                        } else if (player.nPoint.dameg == 32500) {
                                            Service.gI().sendThongBaoOK(player, "Đã Chuyển Sinh Mốc Tối Đã!!!");
                                        }
                                        break;
                                }
                                break;
                            case ConstNpc.MENU_OPENED_DBKB:
                                if (select == 0) {
                                    BanDoKhoBauService.gI().joinBDKB(player);
                                }
                                break;
                            case ConstNpc.MENU_ACCEPT_GO_TO_BDKB:
                                if (select == 0) {
                                    Object level = PLAYERID_OBJECT.get(player.id);
                                    BanDoKhoBauService.gI().openBDKB(player, (int) level);
                                }
                                break;

                        }
                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
////                               npcChat(player, "Giáng sinh anh lành!");
////                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    }

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    }

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi Thích Gì?"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "mở thành viên", "giftcode ", "nhận ngọc xanh", "next nhiệm vụ", "nhận đệ tử", "mốc nạp");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {

                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "mở rồi ngáo à");
                                } else {
                                    if (player.getSession().vnd >= 10000) {
                                        if (PlayerDAO.addcoinBar(player, 10000)) {
                                            player.getSession().actived = true;
                                            Service.gI().sendThongBao(player, "Bạn đã mở thành viên");
                                             PlayerDAO.subcoinBar(player, 10000);
                                        }
                                    } else {
                                        this.npcChat(player, "Bạn còn thiếu " + (10000 - player.getSession().vnd) + " để mở thành viên");
                                    }
                                }
                                break;
                            case 2:
                                if (player.inventory.gem == 200000000) {
                                    this.npcChat(player, "Bú ít thôi con");
                                    break;
                                }
                                player.inventory.gem = 200000000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 200K ngọc xanh");
                                break;
                            case 3:
                                if (player.playerTask.taskMain.id == 11) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_0);
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_1);
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_2);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Chỉ Hỗ Trợ NHiệm Vụ: Kết giao, gia nhập bang hội, nv bang hội đầu tiên, nhiệm vụ bang hội thứ 2");
                                }

                                break;
                            case 1:
                                Input.gI().createFormGiftCode(player);
                                break;

                            case 4:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                } else {
                                    this.npcChat(player, "Bạn đã có rồi");
                                }
                                break;

                            case 5:
//                                if (player.getSession().coinBar >= 250000) {
//                                    PlayerDAO.subcoinBar(player, 250000);
//                                    Item moi1 = ItemService.gI().createNewItem((short) Util.nextInt(1155, 1155));
//                                    moi1.itemOptions.add(new Item.ItemOption(140, 15));
////                                    Item cu2 = ItemService.gI().createNewItem((short) Util.nextInt(1402, 1402));
////                                    cu2.quantity = Util.nextInt(1, 1);
//                                    Item cu3 = ItemService.gI().createNewItem((short) Util.nextInt(722, 722));
//                                    cu3.quantity = Util.nextInt(1, 1);
//                                    Item cu1 = ItemService.gI().createNewItem((short) Util.nextInt(674, 674));
//                                    cu1.quantity = Util.nextInt(1000, 1000);
//                                    InventoryServiceNew.gI().addItemBag(player, moi1);
////                                    InventoryServiceNew.gI().addItemBag(player, cu2);
//                                    InventoryServiceNew.gI().addItemBag(player, cu3);
//                                    InventoryServiceNew.gI().addItemBag(player, cu1);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    Service.gI().sendThongBao(player, "Bạn Nhận được quà");
//                                } else {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 250k coin");
//                                }
//                                break;

                        }

                    }

                }
                ;
            }
        ;

    }

    ;
        }

    ;
    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {

                        if (player.clan == null) {
                            Service.gI().sendThongBao(player, "Không có bang hội");
                            return;
                        }
                        if (player.idNRNM != 353) {
                            Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            return;
                        }

                        byte numChar = 0;
                        for (Player pl : player.zone.getPlayers()) {
                            if (pl.clan.id == player.clan.id && pl.id != player.id) {
                                if (pl.idNRNM != -1) {
                                    numChar++;
                                }
                            }
                        }
                        if (numChar < 6) {
                            Service.gI().sendThongBao(player, "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck đi");
                            return;
                        }

                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc tx(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đưa cho ta thỏi vàng và ngươi sẽ mua đc oto\nĐây không phải chẵn lẻ tài xỉu đâu=)))",
                            "Tài", "Xỉu");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Input.gI().XIU(player);
                                case 1:
                                    Input.gI().TAI(player);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                            case 3: {
                                if (player.playerTask.taskMain.id == 11) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_0);
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_1);
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_2);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Chỉ Hỗ Trợ NHiệm Vụ: Kết giao, gia nhập bang hội, nv bang hội đầu tiên, nhiệm vụ bang hội thứ 2");
                                }

                                break;
                            }

                        }
                    }
                }
            }

        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_20_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_20_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_20_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().getIdTask < ConstTask.TASK_21_4) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } else {
                                        //    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng",
                            "tiệm hồng ngọc",
                            "tiệm thỏi vàng",
                            "Quy đổi");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        Item fancung = InventoryServiceNew.gI().findItemBag(player, 1368);
                        Item thientu = InventoryServiceNew.gI().findItemBag(player, 1369);
                        Item trumcuoi = InventoryServiceNew.gI().findItemBag(player, 1370);
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA_RUBY", false);
                                    break;
                                case 2: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA_EVENT", false);
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI, "|7|Số tiền của bạn còn lại " + player.getSession().coinBar
                                            + " Muốn quy đổi không\n"
                                            + "tỉ lệ quy đổi thỏi vàng 1000vnd = 4 thỏi vàng",
                                            "Quy đổi\n Thỏi vàng", "Quy đổi\n hồng ngọc", "không");
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                case 1:
                                    Input.gI().createFormQDHN(player);
                                    break;
                            }
                        }

                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    }

                }
            }
        };
    }

    public static Npc gohannn(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        this.createOtherMenu(player, 0, "Tiến vào map hỗ trợ tân thủ\nNơi up set kích hoạt và nhiều phần quà hấp dẫn\nChỉ dành cho người chơi từ 2k đến 60 tỷ sức mạnh!", "Đến\nRừng Aurura", "Từ chối");

                    } else {
                        this.createOtherMenu(player, 0, "Ngươi muốn quay về?", "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (select) {
                            case 0:
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {

                                    if (player.session.actived) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 295);
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở thành viên Để sử dụng");
                                    }

                                } else {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 295);
                                }
                            case 1:

                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc Gano(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, 0, "Mệt rồi à? Ngươi muốn quay về?", "Quay về", "Từ chối");

                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (select) {
                            case 0:

                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 295);
                                break;
                            case 1:

                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc Hatchi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 30) {
                        this.createOtherMenu(player, 0, "Ngươi muốn đến Địa ngục?", "Đến\nĐịa ngục", "Từ chối");
                    } else {
                        this.createOtherMenu(player, 0, "Sợ rồi muốn về à?", "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 30) {
                        switch (select) {
                            case 0:
                                if (!player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                } else {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 177, -1, 295);
                                }
                                break;
                            case 1:
                                break;
                        }
                    } else {
                        switch (select) {
                            case 0:
                                if (!player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                } else {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 30, -1, 295);
                                }
                                break;
                            case 1:
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc WHIS1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 252) {
                        this.createOtherMenu(player, 0, "Ngươi muốn đến Hành Tinh Ngục Tù 2?", "ĐI", "Từ chối");
                    } else {
                        this.createOtherMenu(player, 0, "Sợ rồi muốn về à?", "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 252) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 253, -1, 123);
                                break;
                            case 1:
                                break;
                        }
                    } else {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 252, -1, 295);
                                break;
                            case 1:
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc WHIS2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 253) {
                        this.createOtherMenu(player, 0, "Ngươi muốn Về Map Hủy Diệt?", "Về", "Từ chối");
                    } else {
                        this.createOtherMenu(player, 0, "Sợ rồi muốn về à?", "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 253) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 252, -1, 726);
                                break;
                            case 1:
                                break;
                        }
                    } else {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 252, -1, 295);
                                break;
                            case 1:
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Pha lê\nhóa\ntrang bị",
                                "Ép sao\ntrang bị",
                                "Nâng\nSKH",
                                "Nâng\nSKH\nmới",
                                "map\nkim thượng",
                                "chế tạo sách");
                    } else if (this.mapId == 173) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa",
                                "nâng cấp\nchân mệnh");

                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 21:
                                    this.createOtherMenu(player, ConstNpc.VU_LAN, "Ngươi muốn muốn Địa ngục Lích Tên à?\nNhớ mang theo bình phép chứa linh hồn\nTìm đủ 99 linh hồn thì tới gặp ta", "Xuống\nĐịa ngục",
                                            "Hồi sinh\nLích Tên", "Shop\nsự kiện");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_KICH_HOAT);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_KICH_HOAT_NEW);
                                    break;
                                case 4:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 173, -1, -1);
                                    break;
                                case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_SACH);
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.PHAP_SU_HOA:
                                case CombineServiceNew.TAY_PHAP_SU:
                                case CombineServiceNew.CHE_TAO_SACH:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_DO_KICH_HOAT:
                                case CombineServiceNew.NANG_CAP_DO_KICH_HOAT_NEW:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.NANG_CAP_NGOC_BOI:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;
                                        case 3:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1000;
                                            }
                                            break;
                                    }
                                    CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    } else if (this.mapId == 173) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    }

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        nguhs.gI().setTimeJoinnguhs();
                        long now = System.currentTimeMillis();
                        if (now > nguhs.TIME_OPEN_NHS && now < nguhs.TIME_CLOSE_NHS) {
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x10 Tnsm\nHỗ trợ cho Ae trên 80 Tỷ SM?\nThời gian từ 17h - 23h", "OK");
                        } else {
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x10 Tnsm\nHỗ trợ cho Ae trên 80 Tỷ SM?\nThời gian từ 17h - 23h", "ok");
                        }
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Ảru?", "OK", "Từ chối");

                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.NguHanhSonPoint + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?", "Âu kê", "Top Ngu Hanh Son", "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (mapId == 0) {
                        switch (select) {
                            case 100:
                                break;
                            case 0:
                                if (player.nPoint.power >= 80000000000L) {
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    ChangeMapService.gI().changeMapInYard(player, 123, -1, -1);
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ sức mạnh để vào");
                                    return;
                                }
                                break;
                        }
                    }
                    if (mapId == 123) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                        }
                    }
                    if (mapId == 122) {
                        if (select == 0) {
                            if (player.NguHanhSonPoint >= 500) {
                                player.NguHanhSonPoint -= 500;
                                Item item = ItemService.gI().createNewItem((short) (711));
                                item.itemOptions.add(new Item.ItemOption(49, 25));
                                item.itemOptions.add(new Item.ItemOption(77, 25));
                                item.itemOptions.add(new Item.ItemOption(103, 25));
                                item.itemOptions.add(new Item.ItemOption(207, 0));
                                item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                            } else {
                                Service.gI().sendThongBao(player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");
                            }

                        }
                        if (select == 1) {
//                            Service.gI().showListTop(player, Manager.topNHS);

                        }
                    }

                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                                if (select == 0) {
                                    player.magicTree.harvestPea();
                                } else if (select == 1) {
                                    if (player.magicTree.level == 11) {
                                        player.magicTree.fastRespawnPea();
                                    } else {
                                        player.magicTree.showConfirmUpgradeMagicTree();
                                    }
                                } else if (select == 2) {
                                    player.magicTree.fastRespawnPea();
                                }
                                break;
                            case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                                if (select == 0) {
                                    player.magicTree.harvestPea();
                                } else if (select == 1) {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                                break;
                            case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                                if (select == 0) {
                                    player.magicTree.upgradeMagicTree();
                                }
                                break;
                            case ConstNpc.MAGIC_TREE_UPGRADE:
                                if (select == 0) {
                                    player.magicTree.fastUpgradeMagicTree();
                                } else if (select == 1) {
                                    player.magicTree.showConfirmUnuppgradeMagicTree();
                                }
                                break;
                            case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                                if (select == 0) {
                                    player.magicTree.unupgradeMagicTree();
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 800000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
//                                về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, 138);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    //public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 149) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "tét", "Gọi nhân bản");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                   if (select == 0){
//                        BossManager.gI().createBoss(-214);
//                   }
//                }
//            }
//        };
//    }
    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nThỏi vàng");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
                        } else if (select == 1) {
                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldNormar += 20;
                                            ChonAiDay.gI().goldNormar += 20;
                                            ChonAiDay.gI().addPlayerNormar(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");

                                        }
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                                case 2: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldVIP += 200;
                                            ChonAiDay.gI().goldVip += 200;
                                            ChonAiDay.gI().addPlayerVIP(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                        }
                                    } catch (Exception ex) {
//                                            java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    } else if (this.mapId == 141) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy nắm lấy tay ta mau!", "Về\nthần điện");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;

                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 141) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapInYard(player, 45, 0, 408);
                                    Service.gI().sendThongBao(player, "Hãy xuống dưới gặp thần\nmèo Karin");
                                    player.clan.gobosscdrd = true;
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    if (player.clan != null) {
                                        if (player.clan.ConDuongRanDoc != null) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
                                                    "Bang hội của con đang đi con đường rắn độc cấp độ "
                                                    + player.clan.ConDuongRanDoc.level + "\nCon có muốn đi theo không?",
                                                    "Đồng ý", "Từ chối");
                                        } else {

                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_CDRD,
                                                    "Đây là Con đường rắn độc \nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                    "Chọn\ncấp độ", "Từ chối");
                                        }
                                    } else {
                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.clanMember.getNumDateFromJoinTimeToToday() >= 2 && player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        ChangeMapService.gI().goToCDRD(player);
                                    }
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            switch (select) {
                                case 0:

                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc.name + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                    } else if (player.nPoint.power < ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    } else {
                                        Input.gI().createFormChooseLevelCDRD(player);
                                    }
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            switch (select) {
                                case 0:
                                    ConDuongRanDocService.gI().openConDuongRanDoc(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 290_000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_JOIN_DOANH_TRAI:
                                if (select == 0) {
                                    DoanhTraiService.gI().joinDoanhTrai(player);
                                } else if (select == 2) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                                }
                                break;
                            case ConstNpc.IGNORE_MENU:
                                if (select == 1) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getSecondLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }

                    if (!player.clan.doanhTrai_haveGone) {
                        player.clan.doanhTrai_haveGone = (new java.sql.Date(player.clan.doanhTrai_lastTimeOpen)).getDay() == (new java.sql.Date(System.currentTimeMillis())).getDay();
                    }

                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.doanhTrai_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.doanhTrai_playerOpen + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_JOIN_DOANH_TRAI:
                                if (select == 0) {
                                    DoanhTraiService.gI().joinDoanhTrai(player);
                                } else if (select == 2) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                                }
                                break;
                            case ConstNpc.IGNORE_MENU:
                                if (select == 1) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
//                if (player.clanMember.getNumDateFromJoinTimeToToday() < 1 && player.clan != null) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Map Khí Gas chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", "Thông Tin Chi Tiết", "OK", "Từ Chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 1:
                                if (player.clan != null) {
                                    if (player.clan.khiGas != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_GAS,
                                                "Bang hội của con đang đi DesTroy Gas cấp độ "
                                                + player.clan.khiGas.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_GAS,
                                                "Khí Gas Huỷ Diệt đã chuẩn bị tiếp nhận các đợt tấn công của quái vật\n"
                                                + "các con hãy giúp chúng ta tiêu diệt quái vật \n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
//                            case 2:
//                                Clan clan = player.clan;
//                                if (clan != null) {
//                                    ClanMember cm = clan.getClanMember((int) player.id);
//                                    if (cm != null) {
//                                        if (clan.members.size() > 1) {
//                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
//                                            break;
//                                        }
//                                        if (!clan.isLeader(player)) {
//                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
//                                            break;
//                                        }
////                                        
//                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
//                                                "Yes you do!", "Từ chối!");
//                                    }
//                                    break;
//                                }
//                                Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
//                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    ChangeMapService.gI().goToGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_GAS) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= Gas.POWER_CAN_GO_TO_GAS) {
                                    Input.gI().createFormChooseLevelGas(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(Gas.POWER_CAN_GO_TO_GAS));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCPET_GO_TO_GAS) {
                        switch (select) {
                            case 0:
                                GasService.gI().openBanDoKhoBau(player, Integer.parseInt(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    } else if (this.mapId == 154) {
                        player.billEgg.sendBillEgg();
                        if (player.billEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        player.trunglinhthu.sendBillEgg();
                        if (player.trunglinhthu.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Zzz zzz ...",
                                    "Hủy\ntrứng", "Ấp nhanh\n24 thỏi vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Zzz zzz ...", "Nở", "Hủy\ntrứng", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    } else if (this.mapId == 154) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng ZAMASU?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ ZAMASU",
                                                "Đệ ZAMASU\nTrái Đất", "Đệ ZAMASU\nNamếc", "Đệ ZAMASU\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng ZAMASU?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg(false);
                                }
                                break;
                        }
                    } else if (this.mapId == 104) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng linh thú?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                    if (thoivang != null && thoivang.isNotNullItem() && thoivang.quantity >= 24) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 24);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        player.trunglinhthu.timeDone = 0;
                                        player.trunglinhthu.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để thực hiện");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?",
                                                "Đồng ý",
                                                "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.trunglinhthu.openEggLinhThu();
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.trunglinhthu.destroyEgg(true);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player,
                                ConstNpc.BASE_MENU,
                                "Kính chào Ngài Linh thú sư!",
                                "Cửa hàng",
                                "Ấp trứng",
                                "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {

                        }
                    } else if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", false);
                            } else if (select == 1) {
                                // x99 hồn thú và đá ma thuật + x1 trứng linh thú gặp npc ấp trứng để ấp trong 24h
                                this.createOtherMenu(player, 0, "Ngươi có muốn ấp trứng linh thú không?\n"
                                        + "Cần 99 hồn linh thú\n"
                                        + "99 đá ma thuật\n"
                                        + "1 trứng linh thú\n"
                                        + "Tốn 10 thỏi vàng\n"
                                        + "Vào việc luôn chứ?", "Ấp ngay", "Từ chối");
                            }

                        } else if (player.iDMark.getIndexMenu() == 0) {

                            if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ chỗ trống", "OK");
                                return;
                            }

                            Item hon = InventoryServiceNew.gI().findItemBag(player, 2029);
                            Item da = InventoryServiceNew.gI().findItemBag(player, 2030);
                            Item trung = InventoryServiceNew.gI().findItemBag(player, 2028);
                            Item tv = InventoryServiceNew.gI().findItemBag(player, 457);

                            if (hon == null || !hon.isNotNullItem() || hon.quantity < 99) {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu hồn linh thú", "OK");
                                return;
                            }

                            if (da == null || !da.isNotNullItem() || da.quantity < 99) {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá ma thuật", "OK");
                                return;
                            }

                            if (trung == null || !trung.isNotNullItem()) {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu trứng linh thú", "OK");
                                return;
                            }

                            if (tv == null || !tv.isNotNullItem() || tv.quantity < 10) {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu thỏi vàng", "OK");
                                return;
                            }

                            // OK
                            InventoryServiceNew.gI().subQuantityItemsBag(player, hon, 99);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, da, 99);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, trung, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 10);

                            Item linhthu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026)); // Random Id o day
                            linhthu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 5))); // option o day
                            linhthu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 5)));
                            linhthu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 5)));
                            linhthu.itemOptions.add(new Item.ItemOption(47, Util.nextInt(1, 5)));
                            linhthu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(1, 5)));
                            linhthu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(1, 5)));

                            InventoryServiceNew.gI().addItemBag(player, linhthu);
                            InventoryServiceNew.gI().sendItemBags(player);

                            Service.gI().sendThongBao(player, "Bạn nhận được " + linhthu.template.name);

                            // player.trunglinhthu = new BillEgg(player, System.currentTimeMillis(), BillEgg.DEFAULT_TIME_DONE);
                            // ChangeMapService.gI().changeMap(player, player.zone, player.location.y, player.location.y);
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_BDW:
                                player.rewardBlackBall.getRewardSelect((byte) select);
                                break;
                            case ConstNpc.MENU_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                } else if (select == 1) {
//                                if (!player.getSession().actived) {
//                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                    player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                    ChangeMapService.gI().openChangeMapTab(player);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                }
                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);

//                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {

                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi SKH VIP", "Từ Chối");
                }
            }

            //if (player.inventory.gold < 500000000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                }
            }

        };
    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!player.setClothes.godClothes) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Mặc full set thần linh và mang 99 thức ăn mới được mở shop",
                                "Đóng");
                    } else {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi muốn gì nào?",
                                "Mua đồ hủy diệt", "Đóng");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (this.mapId) {
                            case 48:
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (select == 0) {
                                            ShopServiceNew.gI().opendShop(player, "BILL", true);
                                            break;
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Học tuyệt kỹ", "Từ chối");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Merry Christmas " + player.name + " - san.\nNgươi đã chuẩn bị đủ 50 chuông đồng, 50 bánh quy, 50 cá tuyết, 50 kẹo đường và 1 kẹo người tuyết cho ta chưa?\nBerrus - sama đang đợi đấy!",
                            "Đổi quà", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Chế tạo", "Từ chối");
                                break;
                            case 1:
                                Item BiKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1320);
                                if (BiKiepTuyetKy != null) {
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Super kamejoko\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/99\n" + "|2|Giá vàng: 500.000.000\n" + "|2|Giá ngọc: 50000",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 1) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Ma phông ba\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/99\n" + "|2|Giá vàng: 500.000.000\n" + "|2|Giá ngọc: 50000",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ "
                                                + "đíc chưởng liên hoàn\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/99\n" + "|2|Giá vàng: 500.000.000\n" + "|2|Giá ngọc: 50000",
                                                "Đồng ý", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Hãy tìm bí kíp rồi quay lại gặp ta!");
                                }
                                break;
                        }
                    } else if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        switch (select) {
                            case 0: {
                                Item[] requiredItems = new Item[5];
                                int[] requiredQuantities = {50, 50, 50, 50, 1};
                                int[] itemIds = {1563, 1564, 1565, 1566, 1567};
                                String[] itemNames = {"Chuông đồng", "Cá tuyết", "Bánh quy", "Kẹo đường", "Kẹo người tuyết"};

                                try {
                                    for (int i = 0; i < requiredItems.length; i++) {
                                        requiredItems[i] = InventoryServiceNew.gI().findItemBag(player, itemIds[i]);
                                        if (requiredItems[i] == null || requiredItems[i].quantity < requiredQuantities[i]) {
                                            this.npcChat(player, "Ngươi không đủ " + itemNames[i]);
                                            return;
                                        }
                                    }
                                } catch (Exception e) {
                                    // Xử lý nếu có lỗi xảy ra khi tìm kiếm item trong túi
                                    // throw new RuntimeException(e);
                                }

                                if (player.inventory.ruby < 200000) {
                                    this.npcChat(player, "Ngươi không đủ 200000 hồng ngọc");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của ngươi không đủ chỗ trống");
                                } else {
                                    player.inventory.ruby -= 200000;
                                    for (int i = 0; i < requiredItems.length; i++) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, requiredItems[i], requiredQuantities[i]);
                                    }
                                    Service.gI().sendMoney(player);

                                    Item hopqua = ItemService.gI().createNewItem((short) 736);
                                    hopqua.itemOptions.add(new Item.ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, hopqua);
                                    InventoryServiceNew.gI().sendItemBags(player);

                                    this.npcChat(player, "Tặng ngươi phần quà nhỏ này");
                                }

                                break;
                            }
                            case 10:
                                this.npcChat(player, "Long đang bảo trì 1 lát để điều chỉnh");
                                break;
                            case 1: {
                                Service.getInstance().sendPopUpMultiLine(player, tempId, avartar, "***Món gà đặc biệt***\b|4|- 20 Thịt gà\b- 30 Dưa chuột\b- 40 Cà chua\b- 50 Hành củ\b- 500tr vàng");
                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            // case 0:
                            //    ShopServiceNew.gI().opendShop(player, "THIEN_SU", false);
                            //   break;
                            case 0:
                                if (!player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                } else {
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                }
                                break;
                        }
                        //   } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        //     if (select == 0) {
                        //       CombineServiceNew.gI().startCombine(player);
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1320);
                                if (sach != null && sach.quantity >= 99 && player.inventory.gold >= 500000000 && player.inventory.ruby > 50000 && player.nPoint.power >= 1000000000L) {

                                    if (player.gender == 2) {
                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    }
                                    if (player.gender == 0) {
                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    }
                                    if (player.gender == 1) {
                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 99);
                                    player.inventory.gold -= 500000000;
                                    player.inventory.ruby -= 50000;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 100000000L) {
                                    Service.getInstance().sendThongBao(player, "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 99) {
                                    int sosach = 99 - sach.quantity;
                                    Service.getInstance().sendThongBao(player, "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 500000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.ruby <= 50000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ hồng ngọc  thì quay lại gặp ta.");
                                    return;
                                }

                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 47 || this.mapId == 84) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chào bạn \btôi có thể giúp bạn làm nhiệm vụ", "Nhiệm vụ\nhàng ngày", "Nhận ngọc\nmiễn phí", "Từ chối");
                        }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                        case 0: //shop
//                           npcChat(player, "Giáng sinh anh lành!");
//                           NpcManager.addPlayerToNpcListUser(this.tempId,player);
//
//                            break;

                        }
                    } else if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    player.achievement.Show();
                                    break;
                                case 2:
                                    Input.gI().createFormGiftCode(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc mabunoel(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Giáng sinh an lành!\nNhững hoạt động thú vị của trong kiện Giáng sinh của Ngọc rồng \nđang đón chờ bạn tham gia đấy! ", "Cửa hàng", "Top\nsăn boss");
                    }
                    if (this.mapId == 250) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi có vẻ câu được khá đấy, hãy trở thành cần thủ giỏi nhất và giành lấy phần thưởng nhé!", "Quay về", "TOP\ncâu cá");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                                break;

                        }
                    } else //                    if (player.iDMark.isEventMenu()) {
                    //                            switch (select) {
                    //                                case 0: //shop
                    //                                   npcChat(player, "Giáng sinh anh lành!");
                    //                                   NpcManager.addPlayerToNpcListUser(this.tempId,player);
                    //                                   
                    //                                    break;
                    //                               
                    //                            }
                    //                         } else
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) { // 
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SHOP_EVENT", false);
                                    break;
                                case 10:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 465);
                                    break; //
                                case 1:
                                    Service.gI().showListTop(player, Manager.topEvent);
                                    break; //    
//                                case 3:
//                                    Service.gI().showListTop(player, Manager.topCauca);
//                                    break; //
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        return new Npc(mapId, status, cx, cy, tempId, avatar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        String message;
                        if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy cầm lấy hai hạt đậu cuối cùng của ta đây\nCố giữ mình nhé " + player.name, "Cám ơn\nsư phụ");
                            Service.gI().sendThongBao(player, "Hãy mau bay xuống\nchân tháp Karin");
                        } else if (player.istrain) {
                            message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Hủy đăng ký tập tự động", "Tập luyện với\nThần Mèo", "Thách đấu với\nThần Mèo");
                        } else {
                            message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message, "Đăng ký tập tự động", "Tập luyện với\nThần Mèo", "Thách đấu với\nThần Mèo");
                        }

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player) && this.mapId == 46) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            if (player.clan != null && player.clan.ConDuongRanDoc != null && player.clan.gobosscdrd) {
                                player.nPoint.setFullHpMp();
                                //                           ChangeMapService.gI().changeMapInYard(player, 144, 0, 131);
                            } else {
                                if (!player.istrain) {
                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE, "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ " + player.nPoint.getexp() + " sức mạnh mỗi phút", "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
                                } else {
                                    player.istrain = false;
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                }
                            }
                        } else if (select == 1) {
                            if (player.playerTask.taskMain.id == 5 && player.playerTask.taskMain.index == 5) {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với mèo thần Karin?", "Đồng ý luyện tập", "Không đồng ý");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY0, "Con có chắc muốn tập luyện?\nTập luyện với mèo thần Karin?", "Đồng ý luyện tập", "Không đồng ý");
                            }
                        } else if (select == 2) {

                            this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE_TRY1, "Con có chắc muốn thách đấu?\nThách đấu với mèo thần Karin?", "Đồng ý thách đấu", "Không đồng ý");

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, this.avartar, ConstNpc.INFOR_TRAIN_OFFLINE);
                                break;
                            case 1:
                                player.istrain = true;
                                NpcService.gI().createTutorial(player, this.avartar, "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                break;
                            case 3:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY0) {
                        switch (select) {
                            case 0:

                                player.setfight((byte) 0, (byte) 1);
                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);
                                player.zone.mapInfo(player);
                                DataGame.updateMap(player.getSession());
                                try {
                                    new MeoThan(BossID.MEO_THAN, BossesData.THAN_MEO, player.zone, this.cx, this.cy, player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE_TRY1) {
                        switch (select) {
                            case 0:

                                player.setfight((byte) 1, (byte) 1);

                                player.zone.load_Me_To_Another(player);
                                player.zone.load_Another_To_Me(player);
                                player.zone.mapInfo(player);
                                DataGame.updateMap(player.getSession());
                                try {
                                    new MeoThan(BossID.MEO_THAN, BossesData.THAN_MEO, player.zone, this.cx, this.cy, player);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                        }
                    }
                } else if (this.mapId == 104) {
                    if (player.iDMark.isBaseMenu() && select == 0) {
                        ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                    }
                }

            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Pvp", "Tóp Ngũ Hành Sơn", "Top Sức đánh", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (this.mapId) {
                            case 5:
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (select == 0) {
                                            Service.gI().showListTop(player, Manager.topSM);
                                            break;
                                        }
                                        if (select == 1) {
//                                        Service.gI().showListTop(player, Manager.topNV);
                                            break;
                                        }
                                        if (select == 2) {
//                                        Service.gI().showListTop(player, Manager.topPVP);
                                            break;
                                        }
                                        if (select == 3) {
//                                        Service.gI().showListTop(player, Manager.topNHS);
                                            break;
                                        }
                                        if (select == 4) {
                                            Service.gI().showListTop(player, Manager.topSD);
                                            break;
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.BASE_MENU:
                                if (this.mapId == 131) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 153) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                InventoryServiceNew.gI().addItemBag(player, yardart);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        };
    }

    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nâng cấp khỉ ư?", "Nâng cấp\nkhỉ", "Shop của Khỉ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cần Khỉ Lv1,2,3,4,5,6,7 để nâng cấp lên ct khỉ cấp cao hơn\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm 5 đá ngũ sắc",
                                            "Nâng cấp",
                                            "Từ chối");
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "KHI", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_KHI);
                                    break;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_KHI) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_KHI:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.map.mapId == 52) {
                        if (DaiHoiManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                            String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                            this.createOtherMenu(pl, ConstNpc.MENU_DHVT, "Hiện đang có giải đấu " + nameDH + " bạn có muốn đăng ký không? \nSố người đã đăng ký :" + DaiHoiManager.gI().lstIDPlayers.size(), new String[]{"Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")", "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23"});
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau", new String[]{"Thông tin\bChi tiết", "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23"});
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.map.mapId == 52) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Nhớ Đến Đúng Giờ nhé");
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                            switch (select) {
                                case 0:
//                                    if (DaiHoiService.gI().canRegisDHVT(player.nPoint.power)) {
                                    if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                        if (DaiHoiManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gold -= 10000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else if (DaiHoiManager.gI().typeDHVT > (byte) 0 && DaiHoiManager.gI().typeDHVT < (byte) 5 && player.inventory.gem >= (int) (2 * DaiHoiManager.gI().typeDHVT)) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gem -= (int) (2 * DaiHoiManager.gI().typeDHVT);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng ngọc để đăng ký thi đấu");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                    }

//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Bạn không đủ điều kiện tham gia giải này, hãy quay lại vào giải phù hợp");
//                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                            }
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "Éc éc Bạn muốn gì ở tôi :3?", "Đến Võ đài Unknow");
                    }
                    if (this.mapId == 112) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Về đảo Kame", "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 1000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 1 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                                case 1:
                                    BossManager.gI().showListBoss(player);
                                    break; // qua vo dai
                            }
                        }
                    }

                    if (this.mapId == 112) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
                                    break; // ve dao kame
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Goku SSJ3\n với chỉ số random từ 20 > 30% \n ", "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
//                                    Service.gI().showListTop(player, Manager.topPVP);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1362)); // 49
                                        item.itemOptions.add(new Item.ItemOption(49, Util.nextInt(20, 25)));
                                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 25)));
                                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 25)));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    break; // den hanh tinh cereal
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 171) {
                        this.createOtherMenu(player, 0,
                                "Ngươi!\n Hãy cầm đủ 7 viên ngọc rồng \n Monaito đến đây gặp ta ta sẽ ban cho ngươi\n 1 điều ước ", "Gọi rồng", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isEventMenu()) {
                        switch (select) {
//                            case 0: //shop
//                               npcChat(player, "Giáng sinh anh lành!");
//                               NpcManager.addPlayerToNpcListUser(this.tempId,player);
//                               
//                                break;

                        }
                    } else if (this.mapId == 171) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    this.npcChat(player, "Chức Năng Đang Được Update!");
                                    break; // goi rong

                            }
                        }
                    }
                }
            }
        };
    }
//    Service.gI().showListTop(player, Manager.topNV);

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Valentin:
                    return Valentin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Valentin1:
                    return Valentin1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SK:
                    return SK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return Tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TX:
                    return tx(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return TrongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Granola:
                    return Gano(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SGK:
                    return Tien(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOHAN_NHAT_NGUYET:
                    return gohannn(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Hatchiyack:
                    return Hatchi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS2:
                    return WHIS2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.WHIS1:
                    return WHIS1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Nang_Tien_Ca:
                    return Nang(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Moc:
                    return Moc(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Broly:
                    return broly(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Noel:
                    return Noel(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BR:
                    return BR(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.bi_ngo:
                    return bi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_TAI:
//                    return NpcTichNap(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOEL:
                    return mabunoel(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };

            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class,
                    e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else {
//                            Service.gI().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                            break;
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_ICE_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonIceShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_HALLOWEEN_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronHalloWeen(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        switch (select) {
                            case 0:
                                IntrinsicService.gI().sattd(player);
                                break;
                            case 1:
                                IntrinsicService.gI().satnm(player);
                                break;
                            case 2:
                                IntrinsicService.gI().setxd(player);
                                break;
                            default:
                                break;
                        }
                        break;

                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        if (select == 0) {
                            if (player.inventory.gem >= 50 && player.iDMark.getIdItemUpTop() != -1) {
                                ItemKyGui it = ShopKyGuiService.gI().getItemBuy(player.iDMark.getIdItemUpTop());
                                if (it == null || it.isBuy) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                                    return;
                                }
                                if (it.player_sell != player.id) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
                                    ShopKyGuiService.gI().openShopKyGui(player);
                                    return;
                                }
                                player.inventory.gem -= 50;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Thành công");
                                it.isUpTop += 1;
                                ShopKyGuiService.gI().openShopKyGui(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                player.iDMark.setIdItemUpTop(-1);
                            }
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 99:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 91:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {

                                    PetService.gI().changeZenoPet(player);

                                }
                                break;
                            case 0:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
//                                PlayerService.gI().baoTri();
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 1:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 98:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "GIÁNG\nSINH", "CÀY\nVÀNG");
                                break;
                            case 2:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 3:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 4:
                                Input.gI().createVnd(player);
                                break;
                            case 5:
                                Input.gI().createMoc(player);
                                break;
//                            case 8:
//                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
//                                break;
                        }
                        break;
                    case ConstNpc.BAN_NHIEU_THOI_VANG:
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                        switch (select) {
                            case 0:
                                if (select == 0 && (thoivang == null || thoivang.quantity < 1) && player.inventory.gold <= 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Cần có đủ 1 Thỏi\nvàng để thực hiện");
                                    return;
                                }
                                if (select == 0 && thoivang != null && thoivang.quantity >= 1 && player.inventory.gold > 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 1000 tỷ");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 1 && player.inventory.gold <= 1000_000_000_000L) {
                                    player.inventory.gold += 500_000_000;
                                    Service.gI().sendThongBao(player, "|4|Bạn nhận được 500 Triệu Vàng");
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    break;
                                }
                                break;
                            case 1:
                                if ((thoivang == null || thoivang.quantity < 5) && player.inventory.gold <= 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Cần có đủ 5 Thỏi\nvàng để thực hiện");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 5 && player.inventory.gold > 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 1000 tỷ");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 5 && player.inventory.gold <= 1000_000_000_000L) {
                                    player.inventory.gold += 2_500_000_000L;
                                    Service.gI().sendThongBao(player, "|4|Bạn nhận được 2,5 Tỷ Vàng");
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    break;
                                }
                                break;
                            case 2:
                                if ((thoivang == null || thoivang.quantity < 10) && player.inventory.gold < 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Cần có đủ 10 Thỏi\nvàng để thực hiện");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 10 && player.inventory.gold > 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 1000 tỷ");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 10 && player.inventory.gold <= 1000_000_000_000L) {
                                    player.inventory.gold += 5_000_000_000L;
                                    Service.gI().sendThongBao(player, "|4|Bạn nhận được 5 Tỷ Vàng");
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    break;
                                }
                                break;
                            case 3:
                                if ((thoivang == null || thoivang.quantity < 100) && player.inventory.gold <= 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Cần có đủ 100 Thỏi\nvàng để thực hiện");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 100 && player.inventory.gold > 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 1000 tỷ");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 100 && player.inventory.gold <= 1000_000_000_000L) {
                                    player.inventory.gold += 50_000_000_000L;
                                    Service.gI().sendThongBao(player, "|4|Bạn nhận được 50 Tỷ Vàng");
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                }
                                break;
                            case 4:
                                if ((thoivang == null || thoivang.quantity < 200) && player.inventory.gold <= 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Cần có đủ 200 Thỏi\nvàng để thực hiện");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 200 && player.inventory.gold > 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 1000 tỷ");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 200 && player.inventory.gold <= 1000_000_000_000L) {
                                    player.inventory.gold += 100_000_000_000L;
                                    Service.gI().sendThongBao(player, "|4|Bạn nhận được 100 Tỷ Vàng");
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 200);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    break;
                                }
                            case 5:
                                if ((thoivang == null || thoivang.quantity < 500) && player.inventory.gold <= 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Cần có đủ 500 Thỏi\nvàng để thực hiện");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 500 && player.inventory.gold > 1000_000_000_000L) {
                                    Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 1000 tỷ");
                                    return;
                                }
                                if (thoivang != null && thoivang.quantity >= 500 && player.inventory.gold <= 1000_000_000_000L) {
                                    player.inventory.gold += 250_000_000_000L;
                                    Service.gI().sendThongBao(player, "|4|Bạn nhận được 250 Tỷ Vàng");
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 500);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    break;
                                }
                        }
                        break;

                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossManager.gI().createBoss(BossID.ANDROID_13);
                                BossManager.gI().createBoss(BossID.ANDROID_14);
                                BossManager.gI().createBoss(BossID.ANDROID_15);
                                BossManager.gI().createBoss(BossID.ANDROID_19);
                                BossManager.gI().createBoss(BossID.DR_KORE);
                                BossManager.gI().createBoss(BossID.KING_KONG);
                                BossManager.gI().createBoss(BossID.PIC);
                                BossManager.gI().createBoss(BossID.POC);
                                break;
                            case 1:
                                BossManager.gI().createBoss(BossID.BLACK);
                                break;
                            case 2:
                                BossManager.gI().createBoss(BossID.BROLY);
                                break;
                            case 3:
                                BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
                                //                    BossManager.gI().createBoss(BossID.XEN_CON);
                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                BossManager.gI().createBoss(BossID.CHAIEN);
                                BossManager.gI().createBoss(BossID.XEKO);
                                BossManager.gI().createBoss(BossID.XUKA);
                                BossManager.gI().createBoss(BossID.NOBITA);
                                BossManager.gI().createBoss(BossID.DORAEMON);
                                break;
                            case 6:
                                BossManager.gI().createBoss(BossID.FIDE);
                                break;
                            case 7:
                                BossManager.gI().createBoss(BossID.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossID.VUA_COLD);
                                break;
                            case 8:
                                BossManager.gI().createBoss(BossID.SO_1);
                                BossManager.gI().createBoss(BossID.SO_2);
                                BossManager.gI().createBoss(BossID.SO_3);
                                BossManager.gI().createBoss(BossID.SO_4);
                                BossManager.gI().createBoss(BossID.TIEU_DOI_TRUONG);
                                break;
                            case 9:
                                BossManager.gI().createBoss(BossID.KUKU);
                                BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                                BossManager.gI().createBoss(BossID.RAMBO);
                                break;
                            case 10:
                                BossManager.gI().createBoss(BossID.NOEL);
                                BossManager.gI().createBoss(BossID.MABUNOEL);
                                break;
                            case 11:
                                BossManager.gI().createBoss(BossID.SU);
                                BossManager.gI().createBoss(BossID.MAI);
                                BossManager.gI().createBoss(BossID.PHILAP);
                                break;
                        }
                        break;

                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 0:
                                if (player.getSession().goldBar >= 10000) {
                                    player.getSession().actived = true;
                                    if (PlayerDAO.subGoldBar(player, 10000)) {
                                        Service.gI().sendThongBao(player, "Đã mở thành viên thành công!");
                                        break;
                                    } else {
                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
                                    }
                                }
                                Service.gI().sendThongBao(player, "Bạn không có vàng\n Vui lòng IB KEY để nạp thỏi vàng");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
//                                Service.gI().showListTop(player, Manager.topSK);
                                break;
                            case 2:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
