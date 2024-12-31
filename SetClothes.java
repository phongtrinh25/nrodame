package com.girlkun.models.player;

import com.girlkun.models.item.Item;
import javax.swing.text.html.HTMLDocument;

public class SetClothes {

    private Player player;
    private boolean huydietClothers;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;

    public byte songoku_level_1;
    public byte thienXinHang_level_1;
    public byte kirin_level_1;

    public byte ocTieu_level_1;
    public byte pikkoroDaimao_level_1;
    public byte picolo_level_1;

    public byte kakarot_level_1;
    public byte cadic_level_1;
    public byte nappa_level_1;

    public byte songoku_level_2;
    public byte thienXinHang_level_2;
    public byte kirin_level_2;

    public byte ocTieu_level_2;
    public byte pikkoroDaimao_level_2;
    public byte picolo_level_2;

    public byte kakarot_level_2;
    public byte cadic_level_2;
    public byte nappa_level_2;

    public byte worldcup;
    public byte setDHD;
    public byte spl;
    public byte kame;
    public byte lh;
    public byte bom;
    public byte laze;
    public byte kkr;
    public byte x3;
    public byte x4;
    public byte ts;
    public byte nap;
    public byte sm;
    public byte lv5;
    public byte demo;

    public byte setlv8;
    public boolean lv8;
    public boolean godClothes;
    public boolean sethe;
    public int ctHaiTac = -1;

    public void setup() {
        setDefault();
        setupSKT();
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        this.lv8 = true;
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.type > 6 || item.template.type < -1) {
                    i++;
                } else if (i == 5) {
                    this.lv8 = false;
                    break;
                }
            } else {
                this.lv8 = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 673:
                    this.ctHaiTac = ct.template.id;
                    break;

            }
        }
    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 220:
                            isActSet = true;
                            songoku_level_1++;
                            break;
                        case 46:
                            isActSet = true;
                            songoku_level_2++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 44:
                            isActSet = true;
                            thienXinHang_level_1++;
                        case 169:
                            isActSet = true;
                            thienXinHang_level_2++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 219:
                            isActSet = true;
                            kirin_level_1++;
                            break;
                        case 45:
                            isActSet = true;
                            kirin_level_2++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 222:
                            isActSet = true;
                            ocTieu_level_1++;
                            break;
                        case 190:
                            isActSet = true;
                            ocTieu_level_2++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 223:
                            isActSet = true;
                            pikkoroDaimao_level_1++;
                            break;
                        case 191:
                            isActSet = true;
                            pikkoroDaimao_level_2++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 221:
                            isActSet = true;
                            picolo_level_1++;
                            break;
                        case 189:
                            isActSet = true;
                            picolo_level_2++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 217:
                            isActSet = true;
                            nappa_level_1++;
                            break;
                        case 43:
                            isActSet = true;
                            nappa_level_2++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 215:
                            isActSet = true;
                            kakarot_level_1++;
                            break;
                        case 41:
                            isActSet = true;
                            kakarot_level_2++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 216:
                            isActSet = true;
                            cadic_level_1++;
                            break;
                        case 42:
                            isActSet = true;
                            cadic_level_2++;
                            break;
                        case 232:
                            isActSet = true;
                            kame++;
                            break;
                        case 233:
                            isActSet = true;
                            kkr++;
                            break;
                        case 234:
                            isActSet = true;
                            lh++;
                            break;
                        case 235:
                            isActSet = true;
                            bom++;
                            break;
                        case 236:
                            isActSet = true;
                            laze++;
                            break;
                        case 237:
                            isActSet = true;
                            x4++;
                            break;
                        case 238:
                            isActSet = true;
                            x3++;
                            break;
                        case 239:
                        case 240:
                            isActSet = true;
                            ts++;
                            break;
                        case 241:
                        case 242:
                            isActSet = true;
                            nap++;
                            break;
                        case 248:
                        case 249:
                            isActSet = true;
                            demo++;
                            break;
                        case 243:
                        case 244:
                            isActSet = true;
                            sm++;
                            break;
                        case 102:
                        case 107:
                            isActSet = true;
                            if (io.param >= 7) {
                                spl++;
                            }
                            break;
                        case 72:
                            isActSet = true;
                            if (io.param >= 5) {
                                lv5++;
                            }
                            break;
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }
                }
            }
        }
    }

    //checksetthanlinh
    public boolean setGod() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 555 && item.template.id <= 567) {
                    i++;
                } else if (i == 5) {
                    this.godClothes = true;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        return this.godClothes ? true : false;
    }

    public boolean sethe() {
        for (int i = 0; i <= 2; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 691 && item.template.id <= 693) {
                    i++;
                } else if (i >= 1) {
                    this.sethe = true;
                    break;
                }
            } else {
                this.sethe = false;
                break;
            }
        }
        return this.sethe ? true : false;
    }

    public boolean setGod1() {
        for (int i = 0; i < 1; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 0 && item.template.id <= 999) {
                    i++;
                } else if (i == 1) {
                    this.godClothes = true;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        return this.godClothes ? true : false;
    }
    





    // check set huy diet
    public boolean setGod14() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 650 && item.template.id <= 663) {
                    i++;
                } else if (i == 5) {
                    this.huydietClothers = true;
                    break;
                }
            } else {
                this.huydietClothers = false;
                break;
            }
        }
        return this.huydietClothers ? true : false;
    }

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;

        this.songoku_level_1 = 0;
        this.thienXinHang_level_1 = 0;
        this.kirin_level_1 = 0;
        this.ocTieu_level_1 = 0;
        this.pikkoroDaimao_level_1 = 0;
        this.picolo_level_1 = 0;
        this.kakarot_level_1 = 0;
        this.cadic_level_1 = 0;
        this.nappa_level_1 = 0;

        this.songoku_level_2 = 0;
        this.thienXinHang_level_2 = 0;
        this.kirin_level_2 = 0;
        this.ocTieu_level_2 = 0;
        this.pikkoroDaimao_level_2 = 0;
        this.picolo_level_2 = 0;
        this.kakarot_level_2 = 0;
        this.cadic_level_2 = 0;
        this.nappa_level_2 = 0;

        this.setDHD = 0;
        this.spl = 0;
        this.lv5 = 0;
        this.kame = 0;
        this.kkr = 0;
        this.lh = 0;
        this.bom = 0;
        this.laze = 0;
        this.x4 = 0;
        this.x3 = 0;
        this.ts = 0;
        this.nap = 0;
        this.sm = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.sethe = false;
        this.ctHaiTac = -1;
        this.setlv8 = 0;
        this.lv8 = false;
        this.demo = 0;
    }

    public void dispose() {
        this.player = null;
    }
}
