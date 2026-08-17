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

package com.balugaq.jeg.utils;

import com.balugaq.jeg.utils.compatibility.Converter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author balugaq
 * @since 1.3
 */
public class Models {
    public static final String RECIPE_COMPLETE_BOOK_MECHANISM_1 = "&aclic izquierdo&eHaga clic para abrir el libro de recetas.";
    public static final String RECIPE_COMPLETE_BOOK_MECHANISM_2 = "&aclic derecho&eHaga clic para completar nuevamente";
    public static final String RECIPE_COMPLETE_BOOK_MECHANISM_3 = "&eCompletar de nuevo&aShift+clic derecho&eHaz clic en el recetario para completar64De segunda categoría";
    public static final String RECIPE_COMPLETE_GUI_MECHANISM_1 = "&aclic izquierdo&eHaga clic en el elemento para completar1De segunda categoría";
    public static final String RECIPE_COMPLETE_GUI_MECHANISM_2 = "&aclic derecho&eHaga clic en el elemento para completar64De segunda categoría";
    public static final ItemStack RTS_ITEM =
        Converter.getItem(new SlimefunItemStack("_UI_RTS_ICON", Converter.getItem(Material.ANVIL, "&bbúsqueda en tiempo real", "")));
    public static final ItemStack SPECIAL_MENU_ITEM = Converter.getItem(new SlimefunItemStack(
        "_UI_SPECIAL_MENU_ICON", Converter.getItem(Material.COMPASS, "&bFórmula extra grande", "", "&aHaga clic para abrir la receta súper grande.(En ese caso)")));
    public static final ItemStack INPUT_TEXT_ICON = Converter.getItem(new SlimefunItemStack(
        "_UI_RTS_INPUT_TEXT_ICON",
        Converter.getItem(
            Material.PAPER,
            "&fbuscar: &7Introduzca el término de búsqueda arriba",
            "&fTips:",
            "&7 - &eEl elemento de la izquierda es la clave de retorno.",
            "&7 - &eEl elemento del medio es el botón de la página anterior.",
            "&7 - &eLos elementos de la derecha son botones para la página siguiente."
        )
    ));
    public static final ItemStack JEG_GUIDE_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_JEG_GUIDE_GROUP", Converter.getItem(Material.KNOWLEDGE_BOOK, "&bJEG Guía de usuario de la guía avanzada.")));
    public static final ItemStack HIDDEN_ITEMS_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_HIDDEN_ITEMS_GROUP", Converter.getItem(Material.BARRIER, "&cObjetos ocultos")));
    public static final ItemStack NEXCAVATE_ITEMS_GROUP = Converter.getItem(new SlimefunItemStack(
        "JEG_NEXCAVATE_ITEMS_GROUP_ICON", Converter.getItem(Material.BLACKSTONE, "&bArtículos de renacimiento de la civilización")));
    public static final ItemStack VANILLA_ITEMS_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_VANILLA_ITEMS_GROUP", Converter.getItem(Material.CRAFTING_TABLE, "&7artículo original")));
    public static final ItemStack RECIPE_COMPLETABLE_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_RECIPE_COMPLETABLE_GROUP", Converter.getItem(Material.CRAFTING_TABLE, "&bMáquinas que apoyan la finalización de recetas.")));
    public static final ItemStack JEG_ITEMS_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_JEG_ITEMS_GROUP", Converter.getItem(Material.BOOK, "&bLibro para completar recetas")));
    public static final ItemStack REPLACEMENT_CARDS_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_REPLACEMENT_CARDS_GROUP", Converter.getItem(Material.PAPER, "&btarjeta de reemplazo - artículos complementarios")));
    public static final ItemStack BANNED_ITEMS_GROUP = Converter.getItem(
        new SlimefunItemStack("JEG_BANNED_ITEMS_GROUP", Converter.getItem(Material.COMMAND_BLOCK, "&cArtículos prohibidos")));
    public static final ItemStack MULTI_BLOCK_BUILDER_ITEMS_GROUP = Converter.getItem(
        new SlimefunItemStack("MULTI_BLOCK_BUILDER_ITEMS_GROUP", Converter.getItem(Material.BRICKS, "&bElementos de construcción de bloques múltiples.")));
    public static final ItemStack KEYBIND_ACTION_BORDER = Converter.getItem(
        Material.YELLOW_STAINED_GLASS_PANE, " ",
        " "
    );
    public static final SlimefunItemStack RECIPE_COMPLETE_GUIDE = new SlimefunItemStack(
        "JEG_RECIPE_COMPLETE_BOOK",
        Converter.getItem(
            Material.SLIME_BALL,
            "&bLibro para completar recetas",
            "",
            "&fHaz clic para completar la receta (ver instrucciones de uso)）",
            RECIPE_COMPLETE_BOOK_MECHANISM_1,
            RECIPE_COMPLETE_BOOK_MECHANISM_2,
            RECIPE_COMPLETE_BOOK_MECHANISM_3
        )
    );
    public static final SlimefunItemStack USAGE_INFO = new SlimefunItemStack(
        "JEG_RECIPE_COMPLETE_USAGE_INFO",
        Converter.getItem(
            Material.PAPER,
            "&acomo usar",
            "",
            "&f1. &ePon el Libro para completar recetas en tu inventario",
            "&f2. &eHaga clic derecho para abrir cualquier interfaz de máquina que se adapte a la finalización de fórmulas (como la máquina de acceso directo）",
            "&f3. &eLuego haga clic izquierdo en el libro de finalización de recetas.",
            "&f4. &eSeleccione el elemento que desea completar"
        )
    );
    public static final SlimefunItemStack MECHANISM = new SlimefunItemStack(
        "JEG_RECIPE_COMPLETE_MECHANISM",
        Converter.getItem(
            Material.PAPER,
            "&amecanismo",
            "",
            "&7Prioriza el uso de los elementos en la mochila del jugador para completar la receta.",
            "&7Si la red está conectada, intentará obtener materiales de recetas de la red (solo válido para expansión de red)）",
            "&7Si está conectadoAEred, intentaráAEObtenga materiales de fórmula de Internet",
            "&7Explicación de la conexión: ",
            "&7Los bloques que se encuentran próximos entre sí en cualquier dirección de la máquina correspondiente a la interfaz de la máquina para la que se está completando la receta están conectados a la red./AEred",
            "&7Se considera que la máquina correspondiente a la interfaz de máquina para completar la receta está conectada a la red./AERed (no ocupa la red/AEnodo de red）",
            "",
            "&9===Mecanismo de clic del libro de recetas.===",
            RECIPE_COMPLETE_BOOK_MECHANISM_1,
            RECIPE_COMPLETE_BOOK_MECHANISM_2,
            RECIPE_COMPLETE_BOOK_MECHANISM_3,
            "&9===Mecanismo de clic de la interfaz de finalización===",
            RECIPE_COMPLETE_GUI_MECHANISM_1,
            RECIPE_COMPLETE_GUI_MECHANISM_2
        )
    );
    public static final SlimefunItemStack SUPPORTED_ADDONS_INFO = new SlimefunItemStack(
        "JEG_RECIPE_COMPLETE_SUPPORTED_ADDONS_INFO",
        Converter.getItem(
            Material.PAPER,
            "&aLa finalización de recetas se ha adaptado a algunas de las siguientes máquinas adjuntas.",
            "&7Si necesitas adaptarte más, puedes JustEnoughGuide GitHub entregar issue",
            "",
            "&7- &aestructura multibloque",
            "&7- &amaquina rapida",
            "&7- &aHabilidades fuera de servicio 2.0-Preview",
            "&7- &aHabilidades fuera de servicio 2.0",
            "&7- &aHabilidades fuera de servicio 2.0 Revisión",
            "&7- &aavaricia sin fin",
            "&7- &aavaricia sin fin2",
            "&7- &anave lógica",
            "&7- &ared",
            "&7- &aexpansión de la red",
            "&7- &aTecnología de obsidiana",
            "&7- &alimoAE",
            "&7- &amáquina esponjadora",
            "&7- &aAlma del fabricante de limo",
            "&7- &agalaxia",
            "&7- &aConocedor gourmet",
            "&7- &aRykenArchivo adjunto personalizado",
            "&7- &aTecnología de base",
            "&7- &aautobiografía alquímica",
            "&7- &aTecnología de arcilla",
            "&7- &aAlmacenamiento sin fondo",
            "&7- &aherramienta sencilla",
            "&7- &atécnicas agrícolas",
            "&7- &aingeniería química",
            "&7- &aCompresión sin fin",
            "&7- &amagia",
            "&7- &aTecnología Qingshan",
            "&7- &ala vida del infierno"
        )
    );

    public static final SlimefunItemStack JEG_RECIPE_COMPLETE_BUTTON = new SlimefunItemStack(
        "JEG_RECIPE_COMPLETE_BUTTON",
        Material.KNOWLEDGE_BOOK,
        "&6Finalización de recetas",
        "&7Haga clic para abrir la interfaz de finalización de recetas."
    );

    public static final ItemStack ITEM_MARK_BACKGROUND = Converter.getItem(
        Material.GREEN_STAINED_GLASS_PANE,
        "&a&lagregar favoritos",
        "",
        "&7Haga clic izquierdo en el elemento para agregarlo a la colección."
    );

    public static final ItemStack SLIMEFUN_RECIPE_EDIT = Converter.getItem(
        Material.DIAMOND,
        "&a&lSlimeFunRecipe editor de recetas",
        "",
        "&eHaga clic para abrir el editor de recetas."
    );
}
