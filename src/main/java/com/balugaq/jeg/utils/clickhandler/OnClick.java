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

package com.balugaq.jeg.utils.clickhandler;

import com.balugaq.jeg.api.editor.GroupResorter;
import com.balugaq.jeg.api.groups.BookmarkGroup;
import com.balugaq.jeg.api.interfaces.JEGSlimefunGuideImplementation;
import com.balugaq.jeg.api.multiblock.MultiBlockBuilder;
import com.balugaq.jeg.api.objects.collection.cooldown.FrequencyWatcher;
import com.balugaq.jeg.api.objects.enums.FilterType;
import com.balugaq.jeg.api.objects.events.GuideEvents;
import com.balugaq.jeg.core.listeners.RecipeCompletableListener;
import com.balugaq.jeg.implementation.JustEnoughGuide;
import com.balugaq.jeg.implementation.option.ShareInGuideOption;
import com.balugaq.jeg.implementation.option.ShareOutGuideOption;
import com.balugaq.jeg.utils.ClipboardUtil;
import com.balugaq.jeg.utils.Debug;
import com.balugaq.jeg.utils.EventUtil;
import com.balugaq.jeg.utils.GuideUtil;
import com.balugaq.jeg.utils.KeyUtil;
import com.balugaq.jeg.utils.StackUtils;
import com.balugaq.jeg.utils.compatibility.Converter;
import com.balugaq.jeg.utils.compatibility.Sounds;
import com.balugaq.jeg.utils.platform.PlatformUtil;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.inventory.ItemStackHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author balugaq
 * @since 2.0
 */
@SuppressWarnings({"deprecation"})
@NullMarked
public interface OnClick {
    // @formatter:off
    MessageFormat SHARED_ITEM_MESSAGE = new MessageFormat(ChatColors.color("&a{0} &ecompartido &7[{1}&r&7]&e <Haga clic para buscar>"));
    String CLICK_TO_SEARCH = ChatColors.color("&eHaga clic para buscar");
    FrequencyWatcher<UUID> SHARING_WATCHER = new FrequencyWatcher<>(1, TimeUnit.MINUTES, 10, 5000);
    ObjectImmutableList<? extends OnClick> keybindSets = ObjectImmutableList.of(
            Holder.Item, Holder.ItemGroup, Holder.RecipeType
    );

    @SuppressWarnings("RedundantIfStatement")
    static void preset(ChestMenu menu) {
        menu.setEmptySlotsClickable(false);
        menu.addPlayerInventoryClickHandler((p, s, i, a) ->
                                                    p.isOp() || p.hasPermission("slimefun.cheat.items")
        );
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sounds.GUIDE_BUTTON_CLICK_SOUND, 1, 1));
        menu.addMenuClickHandler(-999, (p, s, i, a) -> {
            // it called when the player clicks outside the inventory
            if (p.isOp() || p.hasPermission("slimefun.cheat.items")) {
                // op or permissible players are allowed to drop item
                return true;
            }
            return false;
        });
    }

    static void share(Player player, String itemName) {
        if (!checkShareCooldown(player)) return;
        if (!ShareOutGuideOption.instance().isEnabled(player)) return;

        String s = itemName;
        while (s.contains(" ")) s = s.substring(0, itemName.indexOf(" "));
        if (s.isEmpty()) return;
        String playerName = player.getName();

        String sharedMessage = SHARED_ITEM_MESSAGE.format(new Object[] {playerName, ChatColors.color(itemName)});
        if (PlatformUtil.isPaper()) {

            Component base = LegacyComponentSerializer.legacySection().deserialize(sharedMessage)
                    .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text(CLICK_TO_SEARCH)));
            Component clickToSearch =
                    base.clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, "/sf search " + ChatColor.stripColor(s)));
            Component clickToCopy =
                    base.clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.COPY_TO_CLIPBOARD, itemName));
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (ShareInGuideOption.instance().isEnabled(p)) {
                    if (p.hasPermission("slimefun.command.search")) {
                        p.sendMessage(clickToSearch);
                    } else {
                        p.sendMessage(clickToCopy);
                    }
                }
            });
        } else {
            TextComponent msg = new TextComponent(sharedMessage);
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(CLICK_TO_SEARCH)));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sf search " + ChatColor.stripColor(s)));

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (ShareInGuideOption.instance().isEnabled(p)) {
                    if (p.hasPermission("slimefun.command.search")) {
                        ClipboardUtil.send(p, msg);
                    } else {
                        ClipboardUtil.send(p, ClipboardUtil.makeComponent(sharedMessage, CLICK_TO_SEARCH, itemName));
                    }
                }
            });
        }
    }

    static boolean checkShareCooldown(Player player) {
        FrequencyWatcher.Result result = SHARING_WATCHER.checkCooldown(player.getUniqueId());
        if (result == FrequencyWatcher.Result.TOO_FREQUENT) {
            player.sendMessage(ChatColor.RED + "Su frecuencia de uso es demasiado alta, úsela más tarde!");
            return false;
        }

        if (result == FrequencyWatcher.Result.CANCEL) {
            player.sendMessage(ChatColor.RED + "Esta característica se está enfriando....");
            return false;
        }

        return true;
    }

    @SuppressWarnings("SameReturnValue")
    static ObjectImmutableList<? extends OnClick> keybindSets() {
        return keybindSets;
    }

    static <T extends BaseAction> ObjectImmutableList<T> merge(ObjectImmutableList<T> overridable,
                                                               ObjectImmutableList<T> list) {
        ArrayList<T> merge = new ArrayList<>(overridable);
        for (T action : list) {
            boolean set = false;
            for (int i = 0; i < merge.size(); i++) {
                T o = merge.get(i);
                if (action.getKey().equals(o.getKey())) {
                    merge.set(i, action);
                    set = true;
                    break;
                }
            }

            if (!set) merge.add(action);
        }

        return new ObjectImmutableList<>(merge);
    }

    Material material();

    String name();

    <T extends BaseAction> T findAction(Player player, String key);

    ObjectImmutableList<? extends OnClick> subKeybinds();

    ObjectImmutableList<? extends BaseAction> listActions();

    /**
     * Al hacer clic en un grupo de elementos:
     *   - (huevos de pascua) si esta en 4 luna 1 día, hay 114 / 514 La probabilidad de apertura.Never gonna give you upPágina (el enlace aparece en la barra de chat, solo aparecerá una vez ese día)）
     *   - en marcadores:
     *     - clic izquierdo: Abrir grupo de artículos
     *     - clic derecho: Desmarcar
     *   - en marcadores:
     *     - clic izquierdo: Marcar marcador
     *   - Al intercambiar conjuntos de artículos:
     *     - Haga clic en el grupo de artículos especiales: (FlexItemGroup)
     *       - clic izquierdo: Abrir grupo de artículos
     *       - clic derecho: Seleccionar grupo de artículos
     *     - Haga clic en el grupo de artículos normal: (!FlexItemGroup)
     *       - clic izquierdo: Seleccionar grupo de artículos
     *   - OPhora:
     *     - Shift+clic izquierdo: Grupo de artículos duplicadoskey (namespace:key)
     *     - Si está instalado RSCE:
     *       - Shift+clic derecho: Obtenga el marcador de posición del grupo de artículos correspondiente
     *   - clic izquierdo: Abrir grupo de artículos
     *   - clic derecho: grupo de artículos coleccionables
     *
     * @author balugaq
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    @NullMarked
    interface ItemGroup extends OnClick {
        ActionKey ACTION_KEY = ActionKey.of(() -> Holder.ItemGroup, "item-group");

        ItemGroup Normal = new Normal();
        ItemGroup Bookmark = new Bookmark();
        Set<UUID> easterredPlayer = ConcurrentHashMap.newKeySet();

        ObjectImmutableList<ItemGroup> subKeybinds = ObjectImmutableList.of(Normal, Bookmark);

        static ClickHandler withGroup(io.github.thebusybiscuit.slimefun4.api.items.ItemGroup group, BaseClickHandler base) {
            return new ClickHandler() {
                @Override
                public io.github.thebusybiscuit.slimefun4.api.items.ItemGroup getItemGroup() {
                    return group;
                }

                @Override
                public boolean onClick(final InventoryClickEvent inventoryClickEvent, final Player player, final int i, final ItemStack itemStack, final ClickAction clickAction) {
                    return base.onClick(inventoryClickEvent, player, i, itemStack, clickAction);
                }
            };
        }

        @Override
        default String name() {
            return "grupo de artículos";
        }

        default Action findAction(Player player, String key) {
            for (Action action : listActions()) {
                String k = action.getKey().getKey();
                if (k.equals(key)) {
                    if (JustEnoughGuide.getConfigManager().isAllowActionRedirect()) {
                        String remap = ACTION_KEY.get(player, k);
                        if (remap != null) {
                            for (Action act : listActions()) {
                                 if (act.getKey().getKey().equals(remap)) {
                                     return act;
                                 }
                            }
                        }
                    }
                    return action;
                }
            }

            return new Action() {
                @Override
                public Material material() {
                    return Material.BARRIER;
                }

                @Override
                public String name() {
                    return "empty";
                }

                @Override
                public boolean click(JEGSlimefunGuideImplementation guide, InventoryClickEvent event, Player player,
                                     int slot, io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup,
                                     ClickAction clickAction, ChestMenu menu, int page) {
                    player.sendMessage(ChatColors.color("&cClave no encontrada: " + key));
                    return false;
                }

                @Override
                public NamespacedKey getKey() {
                    return KeyUtil.newKey(key);
                }
            };
        }

        default ObjectImmutableList<? extends OnClick> subKeybinds() {
            return subKeybinds;
        }

        default ObjectImmutableList<Action> listActions() {
            return ObjectImmutableList.of();
        }

        default ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu,
                                    io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup) {
            return withGroup(itemGroup, (event, player, slot, cursor, action) -> EventUtil.callEvent(new GuideEvents.RecipeTypeButtonClickEvent(player, event.getCurrentItem(), slot, action, menu, guide)).ifSuccess(() -> {
                if (!easterredPlayer.contains(player.getUniqueId())) {
                    LocalDate date = LocalDate.now();
                    if (date.getMonth() == Month.APRIL && date.getDayOfMonth() == 1) {
                        if (ThreadLocalRandom.current().nextInt(514) < 114) {
                            ChatUtils.sendURL(player, "https://www.bilibili.com/video/BV1GJ411x7h7");
                            player.closeInventory();
                            easterredPlayer.add(player.getUniqueId());
                        }
                    }
                }

                ClickType clickType = event.getClick();
                if (clickType == ClickType.RIGHT) {
                    return findAction(player, "right-click").click(
                            guide, event, player, slot, itemGroup, action,
                            menu, 1
                    );
                }

                if (clickType == ClickType.SHIFT_LEFT) {
                    return findAction(player, "shift-left-click").click(
                            guide, event, player, slot, itemGroup, action
                            , menu, 1
                    );
                }

                if (clickType == ClickType.SHIFT_RIGHT) {
                    return findAction(player, "shift-right-click").click(
                            guide, event, player, slot, itemGroup,
                            action, menu, 1
                    );
                }

                return findAction(player, "default").click(guide, event, player, slot, itemGroup, action, menu, 1);
            }));
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @SuppressWarnings("unused")
        @FunctionalInterface
        @NullMarked
        interface ActionHandle {
            void click(JEGSlimefunGuideImplementation guide, InventoryClickEvent event, Player player, int slot,
                       io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup, ClickAction clickAction,
                       ChestMenu menu, int page);
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        interface OpAction extends Action, PermissibleAction {
            static OpAction of(String key, String name, Material material, ActionHandle handle) {
                return new OpAction() {
                    @Override
                    public Material material() {
                        return material;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public NamespacedKey getKey() {
                        return KeyUtil.newKey(key);
                    }

                    @Override
                    public boolean click(JEGSlimefunGuideImplementation guide, InventoryClickEvent event,
                                         Player player, int slot,
                                         io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup,
                                         ClickAction clickAction, ChestMenu menu, int page) {
                        handle.click(guide, event, player, slot, itemGroup, clickAction, menu, page);
                        return false;
                    }
                };
            }

            @Override
            default boolean hasPermission(Player player) {
                return player.isOp() || player.hasPermission("slimefun.cheat.items");
            }
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @SuppressWarnings("unused")
        @NullMarked
        interface Action extends BaseAction {
            static Action of(String key, String name, Material material, ActionHandle handle) {
                return new Action() {
                    @Override
                    public Material material() {
                        return material;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public NamespacedKey getKey() {
                        return KeyUtil.newKey(key);
                    }

                    @Override
                    public boolean click(JEGSlimefunGuideImplementation guide, InventoryClickEvent event,
                                         Player player, int slot,
                                         io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup,
                                         ClickAction clickAction, ChestMenu menu, int page) {
                        handle.click(guide, event, player, slot, itemGroup, clickAction, menu, page);
                        return false;
                    }
                };
            }

            @Override
            default ActionKey parent() {
                return ACTION_KEY;
            }

            @SuppressWarnings("SameReturnValue")
            boolean click(JEGSlimefunGuideImplementation guide, InventoryClickEvent event, Player player, int slot,
                          io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup, ClickAction clickAction,
                          ChestMenu menu, int page);
        }

        /**
         * @author balugaq
         * @since 2.1
         */
        @NullMarked
        interface ClickHandler extends BaseClickHandler {
            @SuppressWarnings("unused") io.github.thebusybiscuit.slimefun4.api.items.ItemGroup getItemGroup();
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        class Normal implements ItemGroup {
            final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    OpAction.of(
                            "shift-right-click", "modo trampa - Obtenga el marcador de posición del grupo de artículos correspondiente", Material.DECORATED_POT, (guide, event, player,
                                                                                                        slot,
                                                                                                        itemGroup,
                                                                                                        action, menu,
                                                                                                        page) -> {
                                if (!player.isOp()) {
                                    return;
                                }

                                if (!JustEnoughGuide.getIntegrationManager().isEnabledRSCEditor()) {
                                    return;
                                }

                                NamespacedKey key = itemGroup.getKey();
                                String id =
                                        "RSC_EDITOR_ITEM_GROUP_" + key.getNamespace().toUpperCase(Locale.ROOT) + "_" + key.getKey().toUpperCase(Locale.ROOT);
                                SlimefunItem slimefunItem = SlimefunItem.getById(id);
                                if (slimefunItem == null) {
                                    return;
                                }

                                player.getInventory().addItem(Converter.getItem(slimefunItem.getItem()));
                            }
                    ),
                    OpAction.of(
                            "shift-left-click", "modo trampa - Grupo de artículos duplicadoskey", Material.TRIPWIRE_HOOK, (guide, event, player, slot, itemGroup, action, menu, page) -> {
                                if (!player.isOp()) {
                                    return;
                                }

                                NamespacedKey key = itemGroup.getKey();
                                String s = key.toString();
                                ClipboardUtil.send(player, "&eHaga clic para copiar el grupo de artículoskey", s, s);
                            }
                    ),
                    OpAction.of(
                            "copy-full-class", "modo trampa - Grupo de artículos duplicadosclass", Material.COMMAND_BLOCK, (guide, event, player, slot,
                                                                                                itemGroup, action,
                                                                                                menu, page) -> {
                                if (!player.isOp()) {
                                    return;
                                }

                                String s = itemGroup.getClass().getName();
                                ClipboardUtil.send(player, "&eHaga clic para copiar el grupo de artículosclass", s, s);
                            }
                    ),
                    Action.of(
                            "right-click", "grupo de artículos coleccionables/Seleccione el grupo de artículos a intercambiar", Material.KNOWLEDGE_BOOK, (guide, event, player, slot,
                                                                                                      itemGroup,
                                                                                                      action, menu,
                                                                                                      page) -> {
                                if (GroupResorter.isSelecting(player)) {
                                    if (itemGroup instanceof FlexItemGroup) {
                                        io.github.thebusybiscuit.slimefun4.api.items.ItemGroup selected =
                                                GroupResorter.getSelectedGroup(player);
                                        if (selected == null) {
                                            player.sendMessage(ChatColors.color("&aGrupo de artículos seleccionado para intercambio: &e" + itemGroup.getDisplayName(player)));
                                            GroupResorter.setSelectedGroup(player, itemGroup);
                                        } else {
                                            GroupResorter.swap(selected, itemGroup);
                                            GroupResorter.setSelectedGroup(player, null);
                                            player.sendMessage(ChatColors.color("&aClasificación de grupos de artículos intercambiados: &e" + selected.getDisplayName(player) + " &7<-> &e" + itemGroup.getDisplayName(player)));
                                            GuideUtil.refreshCurrentPage(player);
                                        }
                                        return;
                                    }
                                }

                                JustEnoughGuide.getBookmarkManager().addBookmark(player, itemGroup);
                                player.sendMessage(ChatColors.color("&aGrupo de artículos recopilados: &e" + itemGroup.getDisplayName(player)));
                            }
                    ),
                    Action.of(
                            "default", "por defecto", Material.COMPASS, (guide, event, player, slot, itemGroup, action, menu,
                                                                  page) -> {
                                PlayerProfile profile = PlayerProfile.find(player).orElse(null);
                                if (profile == null) return;

                                if (GroupResorter.isSelecting(player)) {
                                    if (!(itemGroup instanceof FlexItemGroup)) {
                                        io.github.thebusybiscuit.slimefun4.api.items.ItemGroup selected =
                                                GroupResorter.getSelectedGroup(player);
                                        if (selected == null) {
                                            player.sendMessage(ChatColors.color("&aGrupo de artículos seleccionado para intercambio: &e" + itemGroup.getDisplayName(player)));
                                            GroupResorter.setSelectedGroup(player, itemGroup);
                                        } else {
                                            GroupResorter.swap(selected, itemGroup);
                                            GroupResorter.setSelectedGroup(player, null);
                                            player.sendMessage(ChatColors.color("&aClasificación de grupos de artículos intercambiados: &e" + selected.getDisplayName(player) + " &7<-> &e" + itemGroup.getDisplayName(player)));
                                            GuideUtil.refreshCurrentPage(player);
                                        }
                                    }
                                    return;
                                }

                                guide.openItemGroup(profile, itemGroup, page);
                            }
                    ),
                    Action.of("none", "Ninguna acción", Material.BARRIER, (guide, event, player, slot, group, clickAction, menu, page) -> {
                    })
            );

            @Override
            public Material material() {
                return Material.MOSS_BLOCK;
            }

            @Override
            public String name() {
                return "convencional";
            }

            @Override
            public ObjectImmutableList<Action> listActions() {
                return listActions;
            }
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        class Bookmark extends Normal {
            final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    Action.of(
                            "right-click", "Eliminar grupo de elementos marcados", Material.BARREL, (guide, event, player, slot, itemGroup,
                                                                                 action, menu, page) -> EventUtil.callEvent(new GuideEvents.CollectItemGroupEvent(player, itemGroup, slot, action, menu, guide)).ifSuccess(() -> {
                                PlayerProfile playerProfile = PlayerProfile.find(player).orElse(null);
                                if (playerProfile == null) return;
                                GuideUtil.removeLastEntry(playerProfile.getGuideHistory());
                                JustEnoughGuide.getBookmarkManager().removeBookmark(player, itemGroup);

                                List<com.balugaq.jeg.api.objects.collection.data.Bookmark> items =
                                        JustEnoughGuide.getBookmarkManager().getBookmarkedItems(player);
                                if (items == null || items.isEmpty()) {
                                    player.closeInventory();
                                    return;
                                }
                                new BookmarkGroup(guide, items).open(player, playerProfile, guide.getMode());
                            })
                    )
            );

            @Override
            public Material material() {
                return Material.BOOKSHELF;
            }

            @Override
            public String name() {
                return "marcador";
            }

            @Override
            public ObjectImmutableList<Action> listActions() {
                return merge(super.listActions(), listActions);
            }

            @Override
            public ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu,
                                       io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup) {
                return withGroup(itemGroup, (event, player, slot, cursor, action) -> EventUtil.callEvent(new GuideEvents.ItemGroupButtonClickEvent(player, event.getCurrentItem(), slot, action, menu, guide)).ifSuccess(() -> {
                    ClickType clickType = event.getClick();
                    // Inyectar clic derecho
                    if (clickType == ClickType.RIGHT) {
                        return findAction(player, "right-click").click(
                                guide, event, player, slot, itemGroup, action,
                                menu, 1
                        );
                    }

                    return super.create(guide, menu, itemGroup).onClick(event, player, slot, cursor, action);
                }));
            }
        }
    }

    /**
     * Al hacer clic en un tipo de receta:
     *   - Qestablecer: Compartir tipo de receta
     *   - clic derecho: Encuentra artículos usando este tipo de receta: buscar: $nombre
     *   - Shiftclic izquierdo: Abra el grupo de artículos donde se encuentra el tipo de receta (si lo hay).）
     *   - Shiftclic derecho: Encuentra artículos relacionados/máquina: buscar: nombre
     *
     * @author balugaq
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    @NullMarked
    interface RecipeType extends OnClick {
        ActionKey ACTION_KEY = ActionKey.of(() -> Holder.RecipeType, "recipe-type");
        RecipeType Normal = new Normal();

        ObjectImmutableList<RecipeType> subKeybinds = ObjectImmutableList.of(Normal);

        static ClickHandler withType(io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType type, BaseClickHandler base) {
            return new ClickHandler() {
                @Override
                public io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType getRecipeType() {
                    return type;
                }

                @Override
                public boolean onClick(final InventoryClickEvent inventoryClickEvent, final Player player, final int i, final ItemStack itemStack, final ClickAction clickAction) {
                    return base.onClick(inventoryClickEvent, player, i, itemStack, clickAction);
                }
            };
        }

        @Override
        default String name() {
            return "Tipo de receta";
        }

        default Action findAction(Player player, String key) {
            for (Action action : listActions()) {
                String k = action.getKey().getKey();
                if (k.equals(key)) {
                    if (JustEnoughGuide.getConfigManager().isAllowActionRedirect()) {
                        String remap = ACTION_KEY.get(player, k);
                        if (remap != null) {
                            for (Action act : listActions()) {
                                if (act.getKey().getKey().equals(remap)) {
                                    return act;
                                }
                            }
                        }
                    }
                    return action;
                }
            }

            return new Action() {
                @Override
                public Material material() {
                    return Material.BARRIER;
                }

                @Override
                public String name() {
                    return "empty";
                }

                @Override
                public boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                                     io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType,
                                     ClickAction clickAction, ChestMenu menu, int page) {
                    player.sendMessage(ChatColors.color("&cClave no encontrada: " + key));
                    return false;
                }

                @Override
                public NamespacedKey getKey() {
                    return KeyUtil.newKey(key);
                }
            };
        }

        default ObjectImmutableList<? extends OnClick> subKeybinds() {
            return subKeybinds;
        }

        default ObjectImmutableList<Action> listActions() {
            return ObjectImmutableList.of();
        }

        default ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu,
                                    io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType) {
            return withType(recipeType, (event, player, slot, cursor, action) -> EventUtil.callEvent(new GuideEvents.RecipeTypeButtonClickEvent(player, event.getCurrentItem(), slot, action, menu, guide)).ifSuccess(() -> {
                ItemStack item = event.getCurrentItem();
                if (item == null) return false;
                if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
                    return findAction(player, "q").click(guide, player, slot, recipeType, action, menu, 1);
                }

                if (event.getClick() == ClickType.RIGHT) {
                    return findAction(player, "right-click").click(guide, player, slot, recipeType, action, menu, 1);
                }

                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    return findAction(player, "shift-left").click(guide, player, slot, recipeType, action, menu, 1);
                }

                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    return findAction(player, "shift-right").click(guide, player, slot, recipeType, action, menu, 1);
                }

                return findAction(player, "default").click(guide, player, slot, recipeType, action, menu, 1);
            }));
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        @SuppressWarnings("unused")
        @FunctionalInterface
        interface ActionHandle {
            void click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                       io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType, ClickAction clickAction,
                       ChestMenu menu, int page);
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        @SuppressWarnings("unused")
        interface Action extends BaseAction {
            static Action of(String key, String name, Material material, ActionHandle handle) {
                return new Action() {
                    @Override
                    public Material material() {
                        return material;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public NamespacedKey getKey() {
                        return KeyUtil.newKey(key);
                    }

                    @Override
                    public boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                                         io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType,
                                         ClickAction clickAction, ChestMenu menu, int page) {
                        handle.click(guide, player, slot, recipeType, clickAction, menu, page);
                        return false;
                    }
                };
            }

            @Override
            default ActionKey parent() {
                return ACTION_KEY;
            }

            @SuppressWarnings("SameReturnValue")
            boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                          io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType,
                          ClickAction clickAction, ChestMenu menu, int page);
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        interface OpAction extends Action, PermissibleAction {
            static OpAction of(String key, String name, Material material, ActionHandle handle) {
                return new OpAction() {
                    @Override
                    public Material material() {
                        return material;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public NamespacedKey getKey() {
                        return KeyUtil.newKey(key);
                    }

                    @Override
                    public boolean click(JEGSlimefunGuideImplementation guide,
                                         Player player, int slot,
                                         io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType,
                                         ClickAction clickAction, ChestMenu menu, int page) {
                        handle.click(guide, player, slot, recipeType, clickAction, menu, page);
                        return false;
                    }
                };
            }

            @Override
            default boolean hasPermission(Player player) {
                return player.isOp() || player.hasPermission("slimefun.cheat.items");
            }
        }

        /**
         * @author balugaq
         * @since 2.1
         */
        @NullMarked
        interface ClickHandler extends BaseClickHandler {
            @SuppressWarnings("unused") io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType getRecipeType();
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @SuppressWarnings("removal")@NullMarked
        class Normal implements RecipeType {
            final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    Action.of(
                            "q", "Compartir tipo de receta", Material.CLOCK, (guide, player, slot, recipeType, action, menu, page) -> {
                                String recipeTypeName = ItemStackHelper.getDisplayName(recipeType.getItem(player));
                                share(player, recipeTypeName);
                            }
                    ),
                    Action.of(
                            "right-click", "Encuentra artículos usando este tipo de receta", Material.NAME_TAG, (guide, player, slot, recipeType,
                                                                                           action, menu, page) -> {
                                String recipeTypeName = ItemStackHelper.getDisplayName(recipeType.getItem(player));
                                player.chat("/sf search " + FilterType.BY_RECIPE_TYPE_NAME.apply(ChatColor.stripColor(recipeTypeName)));
                            }
                    ),
                    Action.of(
                            "shift-left", "Abra el grupo de artículos que contiene el tipo de receta.", Material.CAULDRON, (guide, player, slot, recipeType, action,
                                                                                        menu, page) -> {
                                SlimefunItem machine = recipeType.getMachine();
                                if (machine != null) {
                                    PlayerProfile profile = PlayerProfile.find(player).orElse(null);
                                    if (profile == null) return;
                                    guide.openItemGroup(profile, machine.getItemGroup(), 1);
                                }
                            }
                    ),
                    Action.of(
                            "shift-right", "Encuentra artículos relacionados/máquina", Material.ANVIL, (guide, player, slot, recipeType, action,
                                                                                 menu, page) -> {
                                String recipeTypeName = ItemStackHelper.getDisplayName(recipeType.getItem(player));
                                player.chat("/sf search " + ChatColor.stripColor(recipeTypeName));
                            }
                    ),
                    OpAction.of(
                            "copy-id", "modo trampa - Copiar tipo de recetaID", Material.MAGENTA_GLAZED_TERRACOTTA, (guide, player, slot, recipeType, action, menu, page) -> {
                                if (!player.isOp()) {
                                    return;
                                }

                                String s = recipeType.getKey().asString();
                                ClipboardUtil.send(player, "&eHaga clic para copiar el tipo de recetaID", s, s);
                            }
                    ),
                    Action.of(
                            "default", "por defecto", Material.COMPASS,
                            (guide, player, slot, recipeType, action, menu, page) -> {
                            }
                    ),
                    Action.of("none", "Ninguna acción", Material.BARRIER, (guide, player, slot, recipeType, clickAction, menu, page) -> {
                    })
            );

            @Override
            public Material material() {
                return Material.MOSS_BLOCK;
            }

            @Override
            public String name() {
                return "convencional";
            }

            @Override
            @SuppressWarnings("SameReturnValue")
            public ObjectImmutableList<Action> listActions() {
                return listActions;
            }
        }
    }

    /**
     * Al hacer clic en un elemento:
     *   - Cuando el artículo no está desbloqueado: Desbloquear elementos
     *   - Fllave: Buscar receta Mostrar nombre del elemento Elementos relacionados con el nombre de este elemento: buscar: %nombre
     *   - Qllave: Compartir elementos
     *   - en marcadores:
     *     clic izquierdo:
     *     - en el libro de trucos: dar artículos
     *     - en el libro de supervivencia: Mostrar interfaz de receta
     *     clic derecho: Desmarcar
     *   - en marcadores:
     *     - clic izquierdo: Marcar marcador
     *     - clic derecho: Encuentra el uso del artículo: buscar: #nombre
     *     - Shiftclic izquierdo: Abra el grupo de artículos donde se encuentra el artículo.
     *     - Shiftclic derecho: Encuentra artículos relacionados/máquina: buscar: nombre
     *     - tener permiso para hacer trampa:
     *       - Haga clic con el botón central y el cursor está vacío.: poner cursor
     *       - El libro de trucos se está abriendo o hay un elemento debajo del cursor: poner en la mochila
     *   - Mostrar interfaz de receta
     *
     * @author balugaq
     * @since 2.0
     */
    @NullMarked
    @SuppressWarnings({"ConstantValue", "unchecked"})
    interface Item extends OnClick {
        ActionKey ACTION_KEY = ActionKey.of(() -> Holder.Item, "item");
        Normal Normal = new Normal();
        ItemMark ItemMark = new ItemMark();
        Bookmark Bookmark = new Bookmark();
        Research Research = new Research();

        ObjectImmutableList<Item> subKeybinds = ObjectImmutableList.of(Normal, ItemMark, Bookmark, Research);

        static ClickHandler withItem(@Nullable SlimefunItem sf, BaseClickHandler base) {
            return new ClickHandler() {
                @Override
                @Nullable
                public SlimefunItem getSlimefunItem() {
                    return sf;
                }

                @Override
                public boolean onClick(final InventoryClickEvent inventoryClickEvent, final Player player, final int i, final ItemStack itemStack, final ClickAction clickAction) {
                    return base.onClick(inventoryClickEvent, player, i, itemStack, clickAction);
                }
            };
        }

        @Override
        default String name() {
            return "cosa";
        }

        default Action findAction(Player player, String key) {
            for (Action action : listActions()) {
                String k = action.getKey().getKey();
                if (k.equals(key)) {
                    if (JustEnoughGuide.getConfigManager().isAllowActionRedirect()) {
                        String remap = ACTION_KEY.get(player, k);
                        if (remap != null) {
                            for (Action act : listActions()) {
                                if (act.getKey().getKey().equals(remap)) {
                                    return act;
                                }
                            }
                        }
                    }
                    return action;
                }
            }

            return new Action() {
                @Override
                public Material material() {
                    return Material.BARRIER;
                }

                @Override
                public String name() {
                    return "empty";
                }

                @Override
                public boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                                     @Nullable SlimefunItem slimefunItem, ItemStack itemStack,
                                     ClickAction clickAction, ChestMenu menu, int page) {
                    return false;
                }

                @Override
                public NamespacedKey getKey() {
                    return KeyUtil.newKey(key);
                }
            };
        }

        default ObjectImmutableList<? extends OnClick> subKeybinds() {
            return subKeybinds;
        }

        default ObjectImmutableList<Action> listActions() {
            return ObjectImmutableList.of();
        }

        default ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu, int page) {
            return create(guide, menu, page, null);
        }

        default ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu, int page,
                                    @Nullable SlimefunItem sf) {
            return withItem(sf, (event, player, slot, s, action) -> EventUtil.callEvent(new GuideEvents.ItemButtonClickEvent(player, event.getCurrentItem(), slot, action, menu, guide)).ifSuccess(() -> {
                ItemStack item = event.getCurrentItem();
                if (item == null) return false;
                SlimefunItem slimefunItem = sf == null ? SlimefunItem.getByItem(item) : sf;
                ClickType clickType = event.getClick();
                if (clickType == ClickType.DOUBLE_CLICK) return false;
                // Fllave
                if (clickType == ClickType.SWAP_OFFHAND) {
                    return findAction(player, "f").click(guide, player, slot, slimefunItem, item, action, menu, page);
                }
                // Qllave
                if (clickType == ClickType.DROP || clickType == ClickType.CONTROL_DROP) {
                    return findAction(player, "q").click(guide, player, slot, slimefunItem, item, action, menu, page);
                }
                // clic derecho
                if (clickType == ClickType.RIGHT) {
                    return findAction(player, "right-click").click(
                            guide, player, slot, slimefunItem, item, action,
                            menu, page
                    );
                }
                // Shift+clic izquierdo
                if (clickType == ClickType.SHIFT_LEFT && !RecipeCompletableListener.isSelectingItemStackToRecipeComplete(player.getUniqueId())) {
                    return findAction(player, "shift-left-click").click(
                            guide, player, slot, slimefunItem, item,
                            action, menu, page
                    );
                }
                // Shift+clic derecho
                if (clickType == ClickType.SHIFT_RIGHT) {
                    return findAction(player, "shift-right-click").click(
                            guide, player, slot, slimefunItem, item,
                            action, menu, page
                    );
                }
                // tenercheatPermisos
                if (!RecipeCompletableListener.isSelectingItemStackToRecipeComplete(player.getUniqueId()) && (player.isOp() || player.hasPermission("slimefun.cheat.items"))) {
                    ItemStack cursor = event.getCursor();
                    if (event.getClick() == ClickType.MIDDLE && (cursor == null || cursor.getType() == Material.AIR)) {
                        return findAction(player, "clone-item").click(
                                guide, player, slot, slimefunItem, item, action
                                , menu, page
                        );
                    }
                    if (guide.getMode() == SlimefunGuideMode.CHEAT_MODE || (cursor != null && cursor.getType() != Material.AIR)) {
                        return findAction(player, "take-item").click(
                                guide, player, slot, slimefunItem, item, action,
                                menu, page
                        );
                    }
                }

                return findAction(player, "default").click(guide, player, slot, slimefunItem, item, action, menu, page);
            }));
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        @FunctionalInterface
        interface ActionHandle {
            @SuppressWarnings("unused") void click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                       @Nullable SlimefunItem slimefunItem, ItemStack itemStack, ClickAction clickAction,
                       ChestMenu menu, int page);
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        interface OpAction extends Action, PermissibleAction {
            static OpAction of(String key, String name, Material material, ActionHandle handle) {
                return new OpAction() {
                    @Override
                    public Material material() {
                        return material;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public NamespacedKey getKey() {
                        return KeyUtil.newKey(key);
                    }

                    @Override
                    public boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                                         @Nullable SlimefunItem slimefunItem, ItemStack itemStack,
                                         ClickAction clickAction, ChestMenu menu, int page) {
                        handle.click(guide, player, slot, slimefunItem, itemStack, clickAction, menu, page);
                        return false;
                    }
                };
            }

            @Override
            default boolean hasPermission(Player player) {
                return player.isOp() || player.hasPermission("slimefun.cheat.items");
            }
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        @SuppressWarnings("unused")
        interface Action extends BaseAction {
            static Action of(String key, String name, Material material, ActionHandle handle) {
                return new Action() {
                    @Override
                    public Material material() {
                        return material;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public NamespacedKey getKey() {
                        return KeyUtil.newKey(key);
                    }

                    @Override
                    public boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                                         @Nullable SlimefunItem slimefunItem, ItemStack itemStack,
                                         ClickAction clickAction, ChestMenu menu, int page) {
                        handle.click(guide, player, slot, slimefunItem, itemStack, clickAction, menu, page);
                        return false;
                    }
                };
            }

            @Override
            default ActionKey parent() {
                return ACTION_KEY;
            }

            @SuppressWarnings("SameReturnValue")
            boolean click(JEGSlimefunGuideImplementation guide, Player player, int slot,
                          @Nullable SlimefunItem slimefunItem, ItemStack itemStack, ClickAction clickAction,
                          ChestMenu menu, int page);
        }

        /**
         * @author balugaq
         * @since 2.1
         */
        @NullMarked
        interface ClickHandler extends BaseClickHandler {
            @Nullable SlimefunItem getSlimefunItem();
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        class Bookmark extends Normal {
            public static final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    Action.of(
                            "right-click", "Eliminar elementos marcados", Material.BARREL, (guide, player, slot, slimefunItem, item,
                                                                               action, menu, page) -> {
                                PlayerProfile playerProfile = PlayerProfile.find(player).orElse(null);
                                if (playerProfile == null) return;
                                if (slimefunItem == null) slimefunItem = SlimefunItem.getByItem(item);
                                if (slimefunItem == null) return;

                                GuideUtil.removeLastEntry(playerProfile.getGuideHistory());
                                JustEnoughGuide.getBookmarkManager().removeBookmark(player, slimefunItem);

                                List<com.balugaq.jeg.api.objects.collection.data.Bookmark> items =
                                        JustEnoughGuide.getBookmarkManager().getBookmarkedItems(player);
                                if (items == null || items.isEmpty()) {
                                    player.closeInventory();
                                    return;
                                }
                                new BookmarkGroup(guide, items).open(player, playerProfile, guide.getMode());
                            }
                    )
            );

            @Override
            public Material material() {
                return Material.BOOKSHELF;
            }

            @Override
            public String name() {
                return "marcador";
            }

            @Override
            public ObjectImmutableList<Action> listActions() {
                return merge(super.listActions(), listActions);
            }

            @Override
            public ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu, int page,
                                       @Nullable SlimefunItem slimefunItem) {
                return withItem(slimefunItem, (event, player, slot, s, action) -> {
                    ItemStack item = event.getCurrentItem();
                    if (item == null) return false;
                    ClickType clickType = event.getClick();
                    if (clickType == ClickType.DOUBLE_CLICK) return false;
                    // Inyectar clic derecho
                    if (clickType == ClickType.RIGHT) {
                        return findAction(player, "right-click").click(
                                guide, player, slot, slimefunItem, item,
                                action, menu, page
                        );
                    }

                    return super.create(guide, menu, page, slimefunItem).onClick(event, player, slot, item, action);
                });
            }
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        class ItemMark extends Normal {
            public static final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    Action.of(
                            "left-click", "Etiqueta de artículo", Material.WRITABLE_BOOK, (guide, player, slot, slimefunItem, item,
                                                                               action, menu, page) -> {
                                if (slimefunItem == null) slimefunItem = SlimefunItem.getByItem(item);
                                if (slimefunItem == null) return;
                                SlimefunItem finalSlimefunItem = slimefunItem;
                                EventUtil.callEvent(new GuideEvents.CollectItemEvent(
                                        player, item, slot, action, menu
                                        , guide
                                )).ifSuccess(() -> {
                                    JustEnoughGuide.getBookmarkManager().addBookmark(player, finalSlimefunItem);
                                    player.sendMessage(ChatColor.GREEN + "Agregado a la lista de favoritos!");
                                    player.playSound(player.getLocation(), Sounds.COLLECTED_ITEM, 1f, 1f);

                                    return false;
                                });
                            }
                    )
            );

            @Override
            public Material material() {
                return Material.PAPER;
            }

            @Override
            public String name() {
                return "marcar elementos";
            }

            @Override
            public ObjectImmutableList<Action> listActions() {
                return merge(super.listActions(), listActions);
            }

            @Override
            public ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu, int page,
                                       @Nullable SlimefunItem slimefunItem) {
                return withItem(slimefunItem, (event, player, slot, cursor, action) -> {
                    ItemStack item = event.getCurrentItem();
                    if (item == null) return false;
                    ClickType clickType = event.getClick();
                    if (clickType == ClickType.DOUBLE_CLICK) return false;
                    // Inyectar botón izquierdo
                    if (clickType == ClickType.LEFT || clickType == ClickType.NUMBER_KEY) {
                        return findAction(player, "left-click").click(
                                guide, player, slot, slimefunItem, item, action
                                , menu, page
                        );
                    }

                    return Normal.create(guide, menu, page, slimefunItem).onClick(event, player, slot, item, action);
                });
            }
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        class Research implements Item {
            public static final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    Action.of(
                            "default", "artículos de investigación", Material.ENCHANTED_BOOK, (guide, player, slot, sf, item, action, menu,
                                                                             page) -> {
                                String id =
                                        item.getItemMeta().getPersistentDataContainer().get(JEGSlimefunGuideImplementation.UNLOCK_ITEM_KEY, PersistentDataType.STRING);
                                if (id == null) return;
                                SlimefunItem slimefunItem = SlimefunItem.getById(id);
                                if (slimefunItem == null) return;
                                io.github.thebusybiscuit.slimefun4.api.researches.Research research =
                                        slimefunItem.getResearch();
                                if (research == null) return;
                                PlayerProfile playerProfile = PlayerProfile.find(player).orElse(null);
                                if (playerProfile == null) return;

                                research.unlockFromGuide(
                                        guide,
                                        player,
                                        playerProfile,
                                        slimefunItem,
                                        slimefunItem.getItemGroup(),
                                        findPage(slimefunItem)
                                );
                            }
                    )
            );

            private static int findPage(SlimefunItem slimefunItem) {
                var group = slimefunItem.getItemGroup();
                if (!(group instanceof FlexItemGroup)) {
                    var items = group.getItems();
                    int idx = items.indexOf(slimefunItem);
                    if (idx == -1) return 1;
                    return idx / 36 + 1;
                }

                return 1;
            }

            @Override
            public Material material() {
                return Material.BARRIER;
            }

            @Override
            public String name() {
                return "Investigación";
            }

            @Override
            public ObjectImmutableList<Action> listActions() {
                return listActions;
            }

            @Override
            public ClickHandler create(JEGSlimefunGuideImplementation guide, ChestMenu menu, int page,
                                       @Nullable SlimefunItem slimefunItem) {
                return withItem(slimefunItem, (event, player, slot, cursor, action) -> EventUtil.callEvent(new GuideEvents.ResearchItemEvent(player, event.getCurrentItem(), slot, action, menu, guide)).ifSuccess(() -> {
                    ItemStack item = event.getCurrentItem();
                    if (item == null) return false;
                    if (event.getClick() == ClickType.DOUBLE_CLICK) return false;
                    return findAction(player, "default").click(
                            guide, player, slot, slimefunItem, item, action, menu,
                            page
                    );
                }));
            }
        }

        /**
         * @author balugaq
         * @since 2.0
         */
        @NullMarked
        @SuppressWarnings({"CodeBlock2Expr", "removal"})
        class Normal implements Item {
            public static final ObjectImmutableList<Action> listActions = ObjectImmutableList.of(
                    Action.of(
                            "f", "Buscar receta Mostrar nombre del elemento Elementos relacionados con el nombre de este elemento", Material.FURNACE, (guide, player, slot, slimefunItem, item
                                    , clickAction, menu, page) -> {
                                String itemName = ItemStackHelper.getDisplayName(item).trim();
                                while (itemName.contains(" ")) itemName = itemName.substring(0, itemName.indexOf(" "));

                                player.chat("/sf search " + FilterType.BY_DISPLAY_ITEM_NAME.apply(ChatColor.stripColor(itemName)));
                            }
                    ),
                    Action.of(
                            "q", "Compartir elementos", Material.CLOCK, (guide, player, slot, slimefunItem, item, clickAction, menu,
                                                              page) -> {
                                share(player, ItemStackHelper.getDisplayName(item).trim());
                            }
                    ),
                    Action.of(
                            "right-click", "Función de búsqueda de elementos", Material.LIGHT, (guide, player, slot, slimefunItem, item,
                                                                            clickAction, menu, page) -> {
                                String itemName = ItemStackHelper.getDisplayName(item).trim();
                                while (itemName.contains(" ")) itemName = itemName.substring(0, itemName.indexOf(" "));

                                player.chat("/sf search " + FilterType.BY_RECIPE_ITEM_NAME.apply(ChatColor.stripColor(itemName)));
                            }
                    ),
                    Action.of(
                            "shift-left-click", "Abra el grupo de artículos donde se encuentra el artículo./OP: Eliminar elementos", Material.CAULDRON, (guide, player, slot, slimefunItem,
                                                                                          item, clickAction, menu,
                                                                                          p2) -> {
                                if (slimefunItem == null) slimefunItem = SlimefunItem.getByItem(item);
                                if (slimefunItem == null) return;

                                if (player.isOp() || player.hasPermission("slimefun.cheat.items")) {
                                    int amount = 1;
                                    if (clickAction.isShiftClicked()) amount = item.getMaxStackSize();

                                    ItemStack itemStack = MultiBlockBuilder.getItem(slimefunItem);
                                    player.getInventory().addItem(StackUtils.getAsQuantity(itemStack, amount));
                                    return;
                                }

                                final io.github.thebusybiscuit.slimefun4.api.items.ItemGroup itemGroup =
                                        slimefunItem.getItemGroup();
                                AtomicInteger page = new AtomicInteger(1);
                                if (GuideUtil.isTaggedGroupType(itemGroup)) {
                                    page.set((itemGroup.getItems().indexOf(slimefunItem) / 36) + 1);
                                }
                                EventUtil.callEvent(new GuideEvents.GroupLinkButtonClickEvent(
                                        player, item, slot,
                                        clickAction, menu,
                                        guide
                                )).ifSuccess(() -> {
                                    PlayerProfile.get(
                                            player, profile -> guide.openItemGroup(
                                                    profile, itemGroup,
                                                    page.get()
                                            )
                                    );
                                    return false;
                                });
                            }
                    ),
                    Action.of(
                            "shift-right-click", "Encuentra artículos relacionados", Material.NAME_TAG, (guide, player, slot, slimefunItem,
                                                                                     item, clickAction, menu, page) -> {
                                String itemName = ItemStackHelper.getDisplayName(item).trim();
                                while (itemName.contains(" ")) itemName = itemName.substring(0, itemName.indexOf(" "));
                                player.chat("/sf search " + ChatColor.stripColor(itemName));
                            }
                    ),
                    OpAction.of(
                            "clone-item", "modo trampa - Artículos duplicados", Material.COMMAND_BLOCK, (guide, player, slot, slimefunItem,
                                                                                          item, clickAction, menu,
                                                                                          page) -> {
                                ItemStack cursor = player.getItemOnCursor();
                                if (cursor == null || cursor.getType() == Material.AIR) {
                                    ItemStack itemStack = MultiBlockBuilder.getItem(item);

                                    player.setItemOnCursor(StackUtils.getAsQuantity(
                                            itemStack,
                                            itemStack.getMaxStackSize()
                                    ));
                                }
                            }
                    ),
                    OpAction.of(
                            "take-item", "modo trampa - Sacar artículos", Material.STRUCTURE_BLOCK, (guide, player, slot, slimefunItem,
                                                                                           item, clickAction, menu,
                                                                                           page) -> {
                                int amount = 1;
                                if (clickAction.isShiftClicked()) amount = item.getMaxStackSize();

                                ItemStack itemStack = MultiBlockBuilder.getItem(item);
                                player.getInventory().addItem(StackUtils.getAsQuantity(itemStack, amount));
                            }
                    ),
                    OpAction.of(
                            "copy-sf-id", "modo trampa - Copiar objetos de limoID", Material.MAGENTA_GLAZED_TERRACOTTA, (guide, player, slot, slimefunItem, item, clickAction, menu, page) -> {
                                if (slimefunItem == null) {
                                    return;
                                }
                                String s = slimefunItem.getId();
                                ClipboardUtil.send(player, "&eHaga clic para copiar el elemento de limo.ID", s, s);
                            }
                    ),
                    Action.of(
                            "default", "por defecto", Material.COMPASS, (guide, player, slot, slimefunItem, item, clickAction,
                                                                  menu, page) -> {
                                PlayerProfile profile = PlayerProfile.find(player).orElse(null);
                                if (profile == null) return;
                                if (slimefunItem != null) {
                                    guide.displayItem(profile, slimefunItem, true);
                                } else {
                                    guide.displayItem0(profile, item, 1, true);
                                }
                            }
                    ),
                    Action.of("none", "Ninguna acción", Material.BARRIER, (guide, player, slot, slimefunItem, item, clickAction, menu, page) -> {
                    })
            );

            @Override
            public Material material() {
                return Material.MOSS_BLOCK;
            }

            @Override
            public String name() {
                return "convencional";
            }

            @Override
            public ObjectImmutableList<Action> listActions() {
                return listActions;
            }
        }
    }

    /**
     * @author balugaq
     * @since 2.0
     */
    @NullMarked
    @FunctionalInterface
    interface BaseClickHandler extends ChestMenu.AdvancedMenuClickHandler {
        static BaseClickHandler deny() {
            return (event, player, slot, item, action) -> false;
        }

        @Override
        default boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
            tryPrintWarning();
            return false;
        }

        static void tryPrintWarning() {
            if (JustEnoughGuide.getConfigManager().isClickPrintWarning()) {
                Debug.warn("El método se ha utilizado indebidamente; comuníquese con los desarrolladores afiliados correspondientes o desactívelo en el archivo de configuración. click-print-warning para cancelar la advertencia");
                Debug.dumpStack();
            }
        }
    }

    final class Holder {
        public static final Item Item = () -> Material.ITEM_FRAME;
        public static final ItemGroup ItemGroup = () -> Material.CAMPFIRE;
        public static final RecipeType RecipeType = () -> Material.CRAFTING_TABLE;
    }
    // @formatter:on
}
