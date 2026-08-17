/*
 * Copyright (c) 2024-2026 balugaq
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.balugaq.jeg.implementation.option;

import com.balugaq.jeg.api.patches.JEGGuideSettings;
import com.balugaq.jeg.api.patches.Priorities;
import com.balugaq.jeg.api.patches.PrioritySlimefunGuideOption;
import com.balugaq.jeg.implementation.JustEnoughGuide;
import com.balugaq.jeg.utils.Calculator;
import com.balugaq.jeg.utils.KeyUtil;
import com.balugaq.jeg.utils.compatibility.Converter;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.chat.ChatInput;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

import static com.balugaq.jeg.api.recipe_complete.source.base.Source.RECIPE_DEPTH_THRESHOLD;

/**
 * @author balugaq
 * @since 1.9
 */
@SuppressWarnings({"UnnecessaryUnicodeEscape", "SameReturnValue"})
@NullMarked
public class RecursiveRecipeFillingGuideOption implements PrioritySlimefunGuideOption<Integer> {
    private static final RecursiveRecipeFillingGuideOption instance = new RecursiveRecipeFillingGuideOption();

    public static RecursiveRecipeFillingGuideOption instance() {
        return instance;
    }

    @Override
    public int priority() {
        return Priorities.RecursiveRecipeFillingGuideOption;
    }

    public static NamespacedKey key0() {
        return KeyUtil.newKey("recursive_recipe_filling");
    }

    public static int getDepth(Player p) {
        return PersistentDataAPI.getInt(p, key0(), 1);
    }

    @Override
    public SlimefunAddon getAddon() {
        return JustEnoughGuide.getInstance();
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        int value = getSelectedOption(p, guide).orElse(1);
        if (value > RECIPE_DEPTH_THRESHOLD) {
            value = RECIPE_DEPTH_THRESHOLD;
            PersistentDataAPI.setInt(p, key0(), value);
        }

        ItemStack item = Converter.getItem(
            Material.FURNACE,
            "&aProfundidad de finalización de la receta",
            "&7Cuanto mayor sea el nivel de finalización de la receta, más tiempo llevará.",
            "&7Si encuentra un material que no existe, intentará completarlo.",
            "&7El material de este material, etc., este proceso se considera como una capa de profundidad.",
            "&e&lEsta característica es experimental, utilícela con precaución.",
            "&c&lEsta característica es propensa a errores.",
            "",
            "&7profundidad actual: " + value + " (rango límite: 1~" + RECIPE_DEPTH_THRESHOLD + ")",
            "&7\u21E8 &eHaga clic para establecer la profundidad"
        );
        return Optional.of(item);
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        p.closeInventory();
        p.sendMessage(ChatColors.color("&aIngrese la profundidad de finalización de la receta"));
        ChatInput.waitForPlayer(
            JustEnoughGuide.getInstance(), p, s -> {
                try {
                    int value = Calculator.calculate(s).intValue();
                    if (value < 1 || value > RECIPE_DEPTH_THRESHOLD) {
                        p.sendMessage("Por favor ingresa 1 ~ " + RECIPE_DEPTH_THRESHOLD + " entero positivo entre");
                        return;
                    }

                    setSelectedOption(p, guide, value);
                    JEGGuideSettings.openSettings(p, guide);
                } catch (NumberFormatException ignored) {
                    p.sendMessage("Por favor ingresa 1 ~ " + RECIPE_DEPTH_THRESHOLD + " entero positivo entre");
                }
            }
        );
    }

    @Override
    public NamespacedKey getKey() {
        return key0();
    }

    @Override
    public Optional<Integer> getSelectedOption(Player p, ItemStack guide) {
        return Optional.of(getDepth(p));
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, Integer value) {
        PersistentDataAPI.setInt(p, getKey(), value);
    }
}
