package de.thexxturboxx.megaquarry.recipes;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import de.thexxturboxx.megaquarry.proxy.CommonProxy;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class HardmodeCondition implements IConditionFactory {

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> CommonProxy.hardmode;
	}

}