package Gensokyo.powers.act2;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.Butterfly;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;


public class DeathTouch extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("DeathTouch");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DeathTouch84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DeathTouch32.png"));

    public DeathTouch(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == this.owner && info.type == DamageInfo.DamageType.NORMAL && target == AbstractDungeon.player && damageAmount > 0) {
            this.flash();
            this.addToBot(new MakeTempCardInDiscardAction(new Butterfly(), 2));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
