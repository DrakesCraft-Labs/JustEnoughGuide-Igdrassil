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

import com.balugaq.jeg.api.patches.Priorities;
import com.balugaq.jeg.utils.compatibility.Converter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * @author balugaq
 * @since 1.9
 */
@SuppressWarnings({"UnnecessaryUnicodeEscape", "SameReturnValue"})
@NullMarked
public class CerPatchGuideOption extends AbstractBooleanGuideOption {
    private static final CerPatchGuideOption instance = new CerPatchGuideOption();

    public static CerPatchGuideOption instance() {
        return instance;
    }

    @Override
    public int priority() {
        return Priorities.CerPathGuideOption;
    }

    @Override
    public ItemStack getDisplayItem(Player p, ItemStack guide, boolean enabled) {
        return Converter.getItem(
            isEnabled(p) ? Material.EMERALD : Material.REDSTONE,
            "&bVisualización de rendimiento de costos: &" + (enabled ? "apermitir" : "4Desactivar"),
            "",
            "&7Ahora puedes elegir si",
            "&7Al buscar el nombre completo de un artículo",
            "&7Se muestra en las máquinas relevantes encontradas.",
            "&7Rentabilidad de utilizar esta máquina para fabricar este artículo.",
            "",
            "&7\u21E8 &eHacer clic " + (enabled ? "Desactivar" : "permitir") + " Visualización de rendimiento de costos"
        );
    }

    public String key0() {
        return "cer_patch";
    }
}
