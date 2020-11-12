package Gensokyo.minions;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.MinionMove;

public class YingYangFox extends AbstractPet {
    public static String ID = GensokyoMod.makeID("YinYangFox");
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    private AbstractMonster target;
    private static final int damage = 8;
    private static final int block = 6;
    private static final int heal = 4;

    public YingYangFox(int HP, float x, float y) {
        super(NAME, ID, HP, -8.0F, 10.0F, 130.0F, 140.0F, "GensokyoResources/images/monsters/Animals/Intents/blank.png", x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Animals/Spriter/AnimalAnimation.scml");
        setAnimal("Crow");
        addMoves();
    }

    private void addMoves(){
        moves.addMove(new MinionMove(DIALOG[0], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/attack move.png"), MOVES[0] + damage + MOVES[1], () -> {
            target = AbstractDungeon.getRandomMonster();
            DamageInfo info = new DamageInfo(this, damage, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, target); // <--- This lets powers effect minions attacks
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
        }));
        moves.addMove(new MinionMove(DIALOG[1], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/defend move.png"),MOVES[2] + block + MOVES[3], () -> {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, this, block));
        }));
        moves.addMove(new MinionMove(DIALOG[2], this, new Texture("GensokyoResources/images/monsters/Animals/Intents/attack move.png"),MOVES[4] + heal + MOVES[5], () -> {
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, heal));
        }));
    }
}