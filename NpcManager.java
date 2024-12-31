package com.girlkun.models.npc;

import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstTask;
import com.girlkun.database.GirlkunDB;
import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.result.GirlkunResultSet;
import com.girlkun.server.Manager;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;


public class NpcManager {

    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }
    public static void addPlayerToNpcListUser(int npcId, Player player) {
    GirlkunResultSet npcResultSet = null;
    try {
        // Thực hiện truy vấn SQL để lấy thông tin của NPC dựa trên npcId
        npcResultSet = GirlkunDB.executeQuery("SELECT * FROM `npc_template` WHERE `id` = " + npcId + " LIMIT 1;");
        if (npcResultSet != null && npcResultSet.first()) {
            // Lấy danh sách người chơi từ cột listuser của NPC
            final JSONArray npcListUser = (JSONArray) JSONValue.parseWithException(npcResultSet.getString("listuser"));

            // Kiểm tra xem playerId đã tồn tại trong danh sách chưa
            if (!npcListUser.contains(player.id)) {
                // Thêm playerId vào danh sách nếu chưa tồn tại
                npcListUser.add(player.id);
                Item qua = null;
                int[] linhthu = {16,17,18,19,20,16,17,18,19,20};  
                    qua = ItemService.gI().createNewItem((short)930);
                
                Service.gI().sendThongBao(player, "Bạn đã nhận được " + qua.template.name);
                InventoryServiceNew.gI().addItemBag(player,qua); 
               
                    qua.itemOptions.add(new Item.ItemOption(30, 0));
                
                InventoryServiceNew.gI().sendItemBags(player);            

                // Cập nhật thông tin vào cơ sở dữ liệu
                GirlkunDB.executeUpdate("UPDATE `npc_template` SET `listuser` = '" + npcListUser.toJSONString() + "' WHERE `id` = " + npcId + ";");
            }
        } else {
            // Xử lý nếu không tìm thấy NPC với id tương ứng
            System.out.println("Không tìm thấy NPC với id: " + npcId);
        }
    } catch (Exception e) {
        // Xử lý nếu có lỗi xảy ra và thông báo lỗi
        e.printStackTrace();
    } finally {
        // Giải phóng tài nguyên sau khi thực hiện xong
        if (npcResultSet != null) {
            npcResultSet.dispose();
        }
    }
}
public static boolean isPlayerInNpcListUser(Player player, int npcid) {
    GirlkunResultSet npcResultSet = null;
    try {
        npcResultSet = GirlkunDB.executeQuery("SELECT * FROM `npc_template` WHERE `id` = " + npcid + " LIMIT 1;");
        if (npcResultSet != null && npcResultSet.first()) {
            // Lấy danh sách người chơi từ cột listuser của NPC
            final JSONArray npcListUser = (JSONArray) JSONValue.parseWithException(npcResultSet.getString("listuser"));
            // Kiểm tra xem player có trong danh sách hay không
            return npcListUser.contains(player.id);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (npcResultSet != null) {
            npcResultSet.dispose();
        }
    }
    // Mặc định trả về false nếu có lỗi xảy ra
    return false;
}
    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null && player.zone.map.mapId == (21 + player.gender)) {
                    continue;
                } else if(npc.tempId == ConstNpc.QUA_TRUNG && player.billEgg == null && player.zone.map.mapId == 154) {
                    continue;    
                } else if(npc.tempId == ConstNpc.QUA_TRUNG && player.trunglinhthu == null && player.zone.map.mapId == 104) {
                    continue;    
                } else if(npc.tempId == ConstNpc.THAN_MEO_KARIN && (player.istry || player.istry1) && player.zone.map.mapId == 46 ||npc.tempId == ConstNpc.THAN_MEO_KARIN && (player.istry || player.istry1 ||  player.isfight ||  player.isfight1)) {
                    continue;    
                } else if(npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0){
                    continue;
                }
                list.add(npc);
            }
        }
        return list;
    }
    
}
