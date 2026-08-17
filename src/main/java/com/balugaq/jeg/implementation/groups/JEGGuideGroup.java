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

package com.balugaq.jeg.implementation.groups;

import com.balugaq.jeg.api.groups.ClassicGuideGroup;
import com.balugaq.jeg.api.interfaces.JEGSlimefunGuideImplementation;
import com.balugaq.jeg.api.interfaces.NotDisplayInCheatMode;
import com.balugaq.jeg.api.objects.enums.FilterType;
import com.balugaq.jeg.api.objects.exceptions.ArgumentMissingException;
import com.balugaq.jeg.implementation.JustEnoughGuide;
import com.balugaq.jeg.implementation.option.BeginnersGuideOption;
import com.balugaq.jeg.utils.Debug;
import com.balugaq.jeg.utils.GuideUtil;
import com.balugaq.jeg.utils.compatibility.Converter;
import com.balugaq.jeg.utils.formatter.Formats;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An implementation of the ClassicGuideGroup for JEG.
 *
 * @author balugaq
 * @since 1.3
 */
@Getter
@NotDisplayInCheatMode
@NullMarked
public class JEGGuideGroup extends ClassicGuideGroup {
    public static final ItemStack HEADER = Converter.getItem(
        Material.BEACON, "&bJEG Guía del usuario", "&bautor: plátano grande", "&bJEG Se optimizó la guía de tecnología de limo para hacerla más fácil de usar.。", "&bConsulte las siguientes guías para comenzar rápidamente JEG funcionalidad agregada。");
    public static final int[] GUIDE_SLOTS =
        Formats.helper.getChars('h').stream().mapToInt(i -> i).toArray();

    public static final int[] BORDER_SLOTS =
        Formats.helper.getChars('B').stream().mapToInt(i -> i).toArray();

    @SuppressWarnings("SameParameterValue")
    protected JEGGuideGroup(NamespacedKey key, ItemStack icon) {
        super(key, icon, Integer.MAX_VALUE);
        for (int slot : BORDER_SLOTS) {
            addGuide(slot, ChestMenuUtils.getBackground());
        }
        boolean loaded = false;
        for (int s : Formats.helper.getChars('A')) {
            addGuide(s, HEADER);
            loaded = true;
        }

        if (!loaded) {
            // Well... the user removed my author information
            throw new ArgumentMissingException(
                "You're not supposed to remove symbol 'A'... Which means Author Information. " + "format="
                    + Formats.helper);
        }

        final AtomicInteger index = new AtomicInteger(0);
        doIf(
            JustEnoughGuide.getConfigManager().isPinyinSearch(),
            () -> addGuide(
                GUIDE_SLOTS[index.getAndIncrement()],
                Converter.getItem(Material.CLOCK, "&bFunción: búsqueda pinyin", "&bintroducir: Puede encontrar rápidamente los elementos que desee a través de la guía de búsqueda de Pinyin。", "&bHaga clic para probar la función。"),
                (p, s, i, a) -> {
                    try {
                        p.performCommand("sf search ding");
                    } catch (Exception e) {
                        p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                        Debug.trace(e);
                    }
                    return false;
                }
            )
        );

        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(Material.NAME_TAG, "&bFunción: Pasar página de búsqueda", "&bintroducir: Puedes desplazarte por los resultados de búsqueda para ver más resultados.。", "&bHaga clic para probar la función。"),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search a");
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        doIf(
            JustEnoughGuide.getConfigManager().isBookmark(),
            () -> addGuide(
                GUIDE_SLOTS[index.getAndIncrement()],
                Converter.getItem(
                    Material.BOOK,
                    "&bFunción: marcar elementos",
                    "&bintroducir: Puede abrir un grupo de artículos para archivos adjuntos admitidos。",
                    "&b      Puede hacer clic en la interfaz del grupo de artículos a continuación“Libro”icono para ingresar al estado marcado。",
                    "&a      Haga clic en el botón Atrás para salir del estado marcado.。",
                    "&bHaga clic para probar la función。"
                ),
                (p, s, i, a) -> {
                    try {
                        if (Slimefun.instance() == null) {
                            p.sendMessage("§cNo se puede obtener Slimefun Por ejemplo, esta característica no se puede utilizar。");
                        }

                        SlimefunGuideImplementation guide =
                            GuideUtil.getGuide(p, SlimefunGuideMode.SURVIVAL_MODE);

                        if (!(guide instanceof JEGSlimefunGuideImplementation jegGuide)) {
                            p.sendMessage("§cLa función no está habilitada y no se puede utilizar。");
                            return false;
                        }

                        PlayerProfile profile = PlayerProfile.find(p).orElse(null);
                        if (profile == null) {
                            p.sendMessage("§cNo se puede obtener información del reproductor, verifique si está instalado correctamente. Slimefun。");
                            return false;
                        }

                        for (ItemGroup itemGroup :
                            new ArrayList<>(Slimefun.getRegistry().getAllItemGroups())) {
                            if (itemGroup
                                .getKey()
                                .equals(new NamespacedKey(Slimefun.instance(), "basic_machines"))) {
                                jegGuide.openItemMarkGroup(itemGroup, p, profile);
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                        Debug.trace(e);
                    }
                    return false;
                }
            )
        );

        doIf(
            JustEnoughGuide.getConfigManager().isBookmark(),
            () -> addGuide(
                GUIDE_SLOTS[index.getAndIncrement()],
                Converter.getItem(
                    Material.NETHER_STAR,
                    "&bFunción: Ver elementos etiquetados",
                    "&bintroducir: Puedes ver los elementos que has etiquetado。",
                    "&b      Puede hacer clic en la interfaz del grupo de artículos a continuación“estrella inferior”icono para ver elementos etiquetados。",
                    "&a      Haga clic en el botón Atrás para salir del estado de visualización.。",
                    "&bHaga clic para probar la función。"
                ),
                (p, s, i, a) -> {
                    try {
                        if (Slimefun.instance() == null) {
                            p.sendMessage("§cNo se puede obtener Slimefun Por ejemplo, esta característica no se puede utilizar。");
                        }

                        SlimefunGuideImplementation guide =
                            GuideUtil.getGuide(p, SlimefunGuideMode.SURVIVAL_MODE);
                        if (!(guide instanceof JEGSlimefunGuideImplementation jegGuide)) {
                            p.sendMessage("§cLa función no está habilitada y no se puede utilizar。");
                            return false;
                        }

                        PlayerProfile profile = PlayerProfile.find(p).orElse(null);
                        if (profile == null) {
                            p.sendMessage("§cNo se puede obtener información del reproductor, verifique si está instalado correctamente. Slimefun。");
                            return false;
                        }

                        jegGuide.openBookMarkGroup(p, profile);
                    } catch (Exception e) {
                        p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                        Debug.trace(e);
                    }
                    return false;
                }
            )
        );

        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.CRAFTING_TABLE,
                "&bFunción: Saltar grupo de elementos",
                "&bintroducir: Cuando busca la receta de un artículo, puede saltar rápidamente al grupo de artículos al que pertenece el artículo deseado.。",
                "&b      puede Shift + Haga clic izquierdo en el elemento deseado para saltar rápidamente al grupo de elementos al que pertenece el elemento.。",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    if (Slimefun.instance() == null) {
                        p.sendMessage("§cNo se puede obtener Slimefun Por ejemplo, esta característica no se puede utilizar。");
                        return false;
                    }

                    SlimefunGuideImplementation guide = GuideUtil.getGuide(p, SlimefunGuideMode.SURVIVAL_MODE);
                    if (!(guide instanceof JEGSlimefunGuideImplementation jegGuide)) {
                        p.sendMessage("§cLa función no está habilitada y no se puede utilizar。");
                        return false;
                    }

                    PlayerProfile profile = PlayerProfile.find(p).orElse(null);
                    if (profile == null) {
                        p.sendMessage("§cNo se puede obtener información del reproductor, verifique si está instalado correctamente. Slimefun。");
                        return false;
                    }

                    SlimefunItem exampleItem = SlimefunItems.ELECTRIC_DUST_WASHER_3.getItem();
                    if (exampleItem == null) {
                        p.sendMessage("§cNo se puede obtener el artículo de muestra, verifique si está instalado correctamente Slimefun。");
                        return false;
                    }

                    if (exampleItem.isDisabledIn(p.getWorld())) {
                        p.sendMessage("§cEste elemento ha sido deshabilitado y no se pueden mostrar ejemplos.");
                        return false;
                    }

                    jegGuide.displayItem(profile, exampleItem, true);
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.NAME_TAG,
                "&bFunción: búsqueda rápida",
                "&bintroducir: Cuando busca la receta de un artículo, puede buscar rápidamente el nombre del artículo, el material y el tipo de receta.",
                "&b      puede Shift + Haga clic derecho en el elemento deseado y podrá buscar rápidamente los nombres de los elementos, materiales y tipos de recetas.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    if (Slimefun.instance() == null) {
                        p.sendMessage("§cNo se puede obtener Slimefun Por ejemplo, esta característica no se puede utilizar。");
                        return false;
                    }

                    SlimefunGuideImplementation guide = GuideUtil.getGuide(p, SlimefunGuideMode.SURVIVAL_MODE);
                    if (!(guide instanceof JEGSlimefunGuideImplementation jegGuide)) {
                        p.sendMessage("§cLa función no está habilitada y no se puede utilizar。");
                        return false;
                    }

                    PlayerProfile profile = PlayerProfile.find(p).orElse(null);
                    if (profile == null) {
                        p.sendMessage("§cNo se puede obtener información del reproductor, verifique si está instalado correctamente. Slimefun。");
                        return false;
                    }

                    if (!BeginnersGuideOption.instance().isEnabled(p)) {
                        p.sendMessage("§cEsta función requiere que habilites la incorporación en la configuración。");
                        return false;
                    }

                    SlimefunItem exampleItem = SlimefunItems.ELECTRIC_DUST_WASHER_3.getItem();
                    if (exampleItem == null) {
                        p.sendMessage("§cNo se puede obtener el artículo de muestra, verifique si está instalado correctamente Slimefun。");
                        return false;
                    }

                    if (exampleItem.isDisabledIn(p.getWorld())) {
                        p.sendMessage("§cEste elemento ha sido deshabilitado y no se pueden mostrar ejemplos.");
                        return false;
                    }

                    jegGuide.displayItem(profile, exampleItem, true);
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        doIf(
            Slimefun.getConfigManager().isResearchingEnabled(),
            () -> addGuide(
                GUIDE_SLOTS[index.getAndIncrement()],
                Converter.getItem(
                    Material.ENCHANTED_BOOK,
                    "&bFunción: Investigación portátil",
                    "&bintroducir: Cuando estás viendo la receta de un artículo, si hay un artículo que no ha sido desbloqueado, puedes hacer clic para desbloquearlo rápidamente.。",
                    "&bHaga clic para probar la función。"
                ),
                (p, s, i, a) -> {
                    try {
                        if (Slimefun.instance() == null) {
                            p.sendMessage("§cNo se puede obtener Slimefun Por ejemplo, esta característica no se puede utilizar。");
                            return false;
                        }

                        SlimefunGuideImplementation guide =
                            GuideUtil.getGuide(p, SlimefunGuideMode.SURVIVAL_MODE);
                        if (!(guide instanceof JEGSlimefunGuideImplementation jegGuide)) {
                            p.sendMessage("§cLa función no está habilitada y no se puede utilizar。");
                            return false;
                        }

                        PlayerProfile profile = PlayerProfile.find(p).orElse(null);
                        if (profile == null) {
                            p.sendMessage("§cNo se puede obtener información del reproductor, verifique si está instalado correctamente. Slimefun。");
                            return false;
                        }

                        SlimefunItem exampleItem = SlimefunItems.ELECTRIC_DUST_WASHER_3.getItem();
                        if (exampleItem == null) {
                            p.sendMessage("§cNo se puede obtener el artículo de muestra, verifique si está instalado correctamente Slimefun。");
                            return false;
                        }

                        if (exampleItem.isDisabledIn(p.getWorld())) {
                            p.sendMessage("§cEste elemento ha sido deshabilitado y no se pueden mostrar ejemplos.");
                            return false;
                        }

                        jegGuide.displayItem(profile, exampleItem, true);
                    } catch (Exception e) {
                        p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                        Debug.trace(e);
                    }
                    return false;
                }
            )
        );

        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.COMPARATOR,
                "&bFunción: búsqueda inteligente",
                "&bintroducir: Cuando utiliza la búsqueda, las máquinas relacionadas se buscan automáticamente y se agregan a la lista de visualización.",
                "&c     La búsqueda Pinyin no es compatible。",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search Sulfato");
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_recipe_item_name = FilterType.BY_RECIPE_ITEM_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes hacer esto agregando al principio o al final " + flag_recipe_item_name + "<recipe_item_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_RECIPE_ITEM_NAME.apply("Batería") + " Búsquedas adicionales de recetas utilizando elementos cuyos nombres contienen \"Batería\" elementos",
                "&c      La búsqueda Pinyin no es compatible。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_RECIPE_ITEM_NAME.apply("Batería"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_recipe_type_name = FilterType.BY_RECIPE_TYPE_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_recipe_type_name + "<recipe_type_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_RECIPE_TYPE_NAME.apply("banco de trabajo") + " Búsquedas adicionales de nombres de tipos de recetas que contengan \"banco de trabajo\" elementos",
                "&c      La búsqueda Pinyin no es compatible。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_RECIPE_TYPE_NAME.apply("banco de trabajo"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_display_item_name = FilterType.BY_DISPLAY_ITEM_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_display_item_name + "<display_item_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_DISPLAY_ITEM_NAME.apply("polvo de cobre") + " Búsqueda adicional Los nombres de los elementos involucrados en la visualización de recetas incluyen \"polvo de cobre\" elementos",
                "&c      La búsqueda Pinyin no es compatible。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_DISPLAY_ITEM_NAME.apply("polvo de cobre"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_addon_name = FilterType.BY_ADDON_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_addon_name + "<addon_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_ADDON_NAME.apply("Tecnología de limo") + " Las búsquedas adicionales de nombres de afiliados incluyen \"Tecnología de limo\" elementos",
                "&c      La búsqueda Pinyin no es compatible。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_ADDON_NAME.apply("Tecnología de limo"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_item_name = FilterType.BY_ITEM_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_item_name + "<item_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_ITEM_NAME.apply("Batería") + " Las búsquedas adicionales de nombres de artículos incluyen \"Batería\" elementos",
                "&b      Soporte de búsqueda pinyin。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_ITEM_NAME.apply("Batería"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_item_lore = FilterType.BY_ITEM_LORE.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_item_lore + "<item_lore> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_ITEM_LORE.apply("zanahoria") + " Búsquedas adicionales La descripción del artículo contiene \"zanahoria\" elementos",
                "&b      Soporte de búsqueda pinyin。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_ITEM_LORE.apply("zanahoria"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_material_name = FilterType.BY_MATERIAL_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_material_name + "<material_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_MATERIAL_NAME.apply("iron") + " Búsqueda adicional El nombre del material del artículo contiene \"iron\" elementos",
                "&c      La búsqueda Pinyin no es compatible。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_MATERIAL_NAME.apply("iron"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        String flag_full_name = FilterType.BY_FULL_NAME.getFirstSymbol();
        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.LODESTONE,
                "&bFunción: Ampliación de búsqueda",
                "&bintroducir: Puedes agregar al principio o al final. " + flag_full_name + "<item_name> para especificar el alcance de la búsqueda",
                "&b      Por ejemplo: " + FilterType.BY_MATERIAL_NAME.apply("lingote de aluminio") + " Búsquedas adicionales de nombres exactamente \"lingote de aluminio\" elementos",
                "&c      La búsqueda Pinyin no es compatible。",
                "&c      Las búsquedas adicionales se combinarán para que surtan efecto.",
                "&bHaga clic para probar la función。"
            ),
            (p, s, i, a) -> {
                try {
                    p.performCommand("sf search " + FilterType.BY_MATERIAL_NAME.apply("lingote de aluminio"));
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        addGuide(
            GUIDE_SLOTS[index.getAndIncrement()],
            Converter.getItem(
                Material.STONE_PICKAXE, "&bFunción: Impresión de nombre", "&bintroducir: Puedes hacer clic en cualquier elemento. Q Botón para compartir este elemento con otros jugadores.", "&bHaga clic para probar la función"),
            (p, s, i, a) -> {
                try {
                    if (Slimefun.instance() == null) {
                        p.sendMessage("§cNo se puede obtener Slimefun Por ejemplo, esta característica no se puede utilizar。");
                        return false;
                    }

                    SlimefunGuideImplementation guide = GuideUtil.getGuide(p, SlimefunGuideMode.SURVIVAL_MODE);
                    if (!(guide instanceof JEGSlimefunGuideImplementation jegGuide)) {
                        p.sendMessage("§cLa función no está habilitada y no se puede utilizar。");
                        return false;
                    }

                    PlayerProfile profile = PlayerProfile.find(p).orElse(null);
                    if (profile == null) {
                        p.sendMessage("§cNo se puede obtener información del reproductor, verifique si está instalado correctamente. Slimefun。");
                        return false;
                    }

                    if (!BeginnersGuideOption.instance().isEnabled(p)) {
                        p.sendMessage("§cEsta función requiere que habilites la incorporación en la configuración。");
                        return false;
                    }

                    SlimefunItem exampleItem = SlimefunItems.ELECTRIC_DUST_WASHER_3.getItem();
                    if (exampleItem == null) {
                        p.sendMessage("§cNo se puede obtener el artículo de muestra, verifique si está instalado correctamente Slimefun。");
                        return false;
                    }

                    if (exampleItem.isDisabledIn(p.getWorld())) {
                        p.sendMessage("§cEste elemento ha sido deshabilitado y no se pueden mostrar ejemplos.");
                        return false;
                    }

                    jegGuide.displayItem(profile, exampleItem, true);
                } catch (Exception e) {
                    p.sendMessage("§cNo se puede realizar la operación, verifique Slimefun ¿Está instalado correctamente?。");
                    Debug.trace(e);
                }
                return false;
            }
        );

        Formats.helper.renderCustom(this);
    }

    public static void doIf(boolean expression, Runnable runnable) {
        if (expression) {
            try {
                runnable.run();
            } catch (Exception e) {
                Debug.trace(e, "loading guide group");
            }
        }
    }
}
