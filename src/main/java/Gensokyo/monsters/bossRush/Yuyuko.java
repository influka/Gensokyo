package Gensokyo.monsters.bossRush;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.actions.YeetPlayerAction;
import Gensokyo.cards.Butterfly;
import Gensokyo.powers.DeathTouch;
import Gensokyo.powers.Reflowering;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Yuyuko extends CustomMonster
{
    private static final Texture FAN = new Texture("GensokyoResources/images/monsters/Yuyuko/Fan.png");
    private TextureRegion FAN_REGION;
    public static final String ID = "Gensokyo:Yuyuko";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private static final byte GHOSTLY_BUTTERFLY = 0;
    private static final byte GHASTLY_DREAM = 1;
    private static final byte LAW_OF_MORTALITY = 2;
    private static final byte RESURRECTION_BUTTERFLY = 3;
    private static final byte SAIGYOUJI_PARINIRVANA = 4;
    private static final int COOLDOWN = 2;
    private static final int GHOSTLY_BUTTERFLY_DAMAGE = 20;
    private static final int A4_GHOSTLY_BUTTERFLY_DAMAGE = 22;
    private static final int GHASTLY_DREAM_DAMAGE = 26;
    private static final int A4_GHASTLY_DREAM_DAMAGE = 28;
    private static final int RESURRECTION_BUTTERFLY_DAMAGE = 16;
    private static final int A4_RESURRECTION_BUTTERFLY_DAMAGE = 18;
    private static final int BLOCK = 20;
    private static final int A9_BLOCK = 22;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int STATUS_COUNT = 2;
    private static final int A19_STATUS_COUNT = 3;
    private static final int FAN_INCREMENT = 1;
    private static final int A19_FAN_INCREMENT = 2;
    public static final int FAN_THRESHOLD = 10;
    private static final int HP = 500;
    private static final int A9_HP = 530;
    private int ghostlyButterflyDamage;
    private int ghastlyDreamDamage;
    private int resurrectionButterflyDamage;
    private int statusCount;
    private int fanIncrement;
    private int block;
    public int fanCounter;
    private int turnCounter;
    private Map<Byte, EnemyMoveInfo> moves;

    public Yuyuko() {
        this(0.0f, 0.0f);
    }

    public Yuyuko(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 295.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Yuyuko/Spriter/YuyukoAnimation.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.statusCount = A19_STATUS_COUNT;
            this.fanIncrement = A19_FAN_INCREMENT;
        } else {
            this.statusCount = STATUS_COUNT;
            this.fanIncrement = FAN_INCREMENT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.ghostlyButterflyDamage = A4_GHOSTLY_BUTTERFLY_DAMAGE;
            this.ghastlyDreamDamage = A4_GHASTLY_DREAM_DAMAGE;
            this.resurrectionButterflyDamage = A4_RESURRECTION_BUTTERFLY_DAMAGE;
        } else {
            this.ghostlyButterflyDamage = GHOSTLY_BUTTERFLY_DAMAGE;
            this.ghastlyDreamDamage = GHASTLY_DREAM_DAMAGE;
            this.resurrectionButterflyDamage = RESURRECTION_BUTTERFLY_DAMAGE;
        }

        this.moves = new HashMap<>();
        this.moves.put(GHOSTLY_BUTTERFLY, new EnemyMoveInfo(GHOSTLY_BUTTERFLY, Intent.ATTACK_DEBUFF, this.ghostlyButterflyDamage, 0, false));
        this.moves.put(GHASTLY_DREAM, new EnemyMoveInfo(GHASTLY_DREAM, Intent.ATTACK, this.ghastlyDreamDamage, 0, false));
        this.moves.put(LAW_OF_MORTALITY, new EnemyMoveInfo(LAW_OF_MORTALITY, Intent.DEBUFF, -1, 0, true));
        this.moves.put(RESURRECTION_BUTTERFLY, new EnemyMoveInfo(RESURRECTION_BUTTERFLY, Intent.ATTACK_DEFEND, this.resurrectionButterflyDamage, 0, false));
        this.moves.put(SAIGYOUJI_PARINIRVANA, new EnemyMoveInfo(SAIGYOUJI_PARINIRVANA, Intent.UNKNOWN, -1, 0, false));

        this.FAN_REGION = new TextureRegion(FAN);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new DeathTouch(this)));
        this.addToBot(new ApplyPowerAction(this, this, new Reflowering(this, this)));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            this.firstMove = false;
        }
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch (this.nextMove) {
            case GHOSTLY_BUTTERFLY: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Butterfly(), this.statusCount));
                turnCounter++;
                break;
            }
            case GHASTLY_DREAM: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                turnCounter++;
                break;
            }
            case LAW_OF_MORTALITY: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF_AMOUNT, true), DEBUFF_AMOUNT));
                turnCounter++;
                break;
            }
            case RESURRECTION_BUTTERFLY: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                incrementFan();
                turnCounter = 0;
                break;
            }
            case SAIGYOUJI_PARINIRVANA: {
                addToBot(new YeetPlayerAction());
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void incrementFan() {
        fanCounter += fanIncrement;
        if (fanCounter > FAN_THRESHOLD) {
            fanCounter = FAN_THRESHOLD;
        }
        if (this.hasPower(Reflowering.POWER_ID)) {
            this.getPower(Reflowering.POWER_ID).flash();
            this.getPower(Reflowering.POWER_ID).amount = fanCounter;
        }
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMoveShortcut(GHOSTLY_BUTTERFLY);
        } else if (this.fanCounter >= FAN_THRESHOLD) {
            this.setMoveShortcut(SAIGYOUJI_PARINIRVANA);
        } else if (turnCounter >= COOLDOWN) {
            this.setMoveShortcut(RESURRECTION_BUTTERFLY);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if (!this.lastMove(GHOSTLY_BUTTERFLY)) {
                possibilities.add(GHOSTLY_BUTTERFLY);
            }
            if (!this.lastMove(GHASTLY_DREAM)) {
                possibilities.add(GHASTLY_DREAM);
            }
            if (!this.lastMove(LAW_OF_MORTALITY)) {
                possibilities.add(LAW_OF_MORTALITY);
            }
            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float scaleWidth = 1.0F * Settings.scale;
        float scaleHeight = Settings.scale;
        sb.setColor(Color.WHITE);
        sb.draw(FAN_REGION, this.drawX - this.FAN_REGION.getRegionWidth() * scaleWidth, this.drawY + (this.FAN_REGION.getRegionHeight() * scaleHeight) / 2, 0.0F, 0.0F, this.FAN_REGION.getRegionWidth(), this.FAN_REGION.getRegionHeight(), scaleWidth, scaleHeight, 0.0F);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yuyuko");
        NAME = Yuyuko.monsterStrings.NAME;
        MOVES = Yuyuko.monsterStrings.MOVES;
        DIALOG = Yuyuko.monsterStrings.DIALOG;
    }
}