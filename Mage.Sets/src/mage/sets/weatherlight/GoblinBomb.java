/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.weatherlight;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.RemoveCountersSourceCost;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.effects.common.counter.RemoveCounterSourceEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.TargetPlayer;

/**
 *
 * @author fireshoes
 */
public class GoblinBomb extends CardImpl {

    public GoblinBomb(UUID ownerId) {
        super(ownerId, 103, "Goblin Bomb", Rarity.RARE, new CardType[]{CardType.ENCHANTMENT}, "{1}{R}");
        this.expansionSetCode = "WTH";

        // At the beginning of your upkeep, you may flip a coin. If you win the flip, put a fuse counter on Goblin Bomb. If you lose the flip, remove a fuse counter from Goblin Bomb.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(Zone.BATTLEFIELD, new GoblinBombEffect(), TargetController.YOU, true));
        
        // Remove five fuse counters from Goblin Bomb, Sacrifice Goblin Bomb: Goblin Bomb deals 20 damage to target player.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new DamageTargetEffect(20), new RemoveCountersSourceCost(CounterType.FUSE.createInstance(5)));
        ability.addCost(new SacrificeSourceCost());
        ability.addTarget(new TargetPlayer());
        this.addAbility(ability);
    }

    public GoblinBomb(final GoblinBomb card) {
        super(card);
    }

    @Override
    public GoblinBomb copy() {
        return new GoblinBomb(this);
    }
}

class GoblinBombEffect extends OneShotEffect {

    public GoblinBombEffect() {
        super(Outcome.Damage);
        staticText = "flip a coin. If you win the flip, put a fuse counter on {this}. If you lose the flip, remove a fuse counter from {this}";
    }

    public GoblinBombEffect(GoblinBombEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Permanent permanent = game.getPermanent(source.getSourceId());
        if (controller != null && permanent != null) {
            if (controller.flipCoin(game)) {
                game.informPlayers("Goblin Bomb: Won flip. Put a fuse counter on Goblin Bomb.");
                new AddCountersSourceEffect(CounterType.FUSE.createInstance(1)).apply(game, source);
                return true;
            } else {
                game.informPlayers("Goblin Bomb: Lost flip. Remove a fuse counter from Goblin Bomb.");
                new RemoveCounterSourceEffect(CounterType.FUSE.createInstance(1)).apply(game, source);
                return true;
            }
        }
        return false;
    }

    @Override
    public GoblinBombEffect copy() {
        return new GoblinBombEffect(this);
    }
}