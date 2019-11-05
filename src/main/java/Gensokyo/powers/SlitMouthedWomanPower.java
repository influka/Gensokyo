package Gensokyo.powers;

import Gensokyo.GensokyoMod;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;


public class SlitMouthedWomanPower extends TwoAmountPower implements NonStackablePower {

    public static final String POWER_ID = GensokyoMod.makeID("SlitMouthedWomanPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int NUM_PROCS = 2;

    public SlitMouthedWomanPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = NUM_PROCS;
        this.amount2 = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("phantasmal");

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.flash();
            if (amount == NUM_PROCS) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, this.owner, new VulnerablePower(info.owner, this.amount2, true), this.amount2, true, AbstractGameAction.AttackEffect.NONE));
            } else if (amount == NUM_PROCS - 1) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, this.owner, new StrengthPower(info.owner, -this.amount2), -this.amount2));
            }
            if (1 < this.amount) {
                this.reducePower(1);
                this.updateDescription();
                AbstractDungeon.onModifyPower();
            } else {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
    }
}
