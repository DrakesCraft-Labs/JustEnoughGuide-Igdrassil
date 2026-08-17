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

import com.balugaq.jeg.implementation.JustEnoughGuide;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author balugaq
 * @since 1.2
 */
@SuppressWarnings({"deprecation", "ExtractMethodRecommender", "unused", "ConstantValue"})
@NullMarked
public class LocalHelper {
    public static final String def = "Afiliación desconocida";
    public static final Map<String, Map<String, SlimefunItemStack>> rscItems = new HashMap<>();
    // default language is zh-CN
    // support color symbol
    public static final Map<String, String> addonLocals = new HashMap<>();
    // depends on rsc addons' info.yml
    public static final Map<String, Set<String>> rscLocals = new HashMap<>();

    static {
        loadDefault();
        for (Map.Entry<String, String> entry :
            JustEnoughGuide.getConfigManager().getLocalTranslate().entrySet()) {
            addonLocals.put(entry.getKey(), ChatColors.color(entry.getValue()));
        }
    }

    public static void loadDefault() {
        addonLocals.put("Slimefun", "Tecnología de limo");
        addonLocals.put("ColoredEnderChests", "Cofre de ender de color");
        addonLocals.put("DyedBackpacks", "mochila teñida");
        addonLocals.put("EnderCargo", "Interfaz de carga de Ender");
        addonLocals.put("EcoPower", "Energía respetuosa con el medio ambiente");
        addonLocals.put("ElectricSpawners", "Generador de monstruos eléctricos");
        addonLocals.put("ExoticGarden", "jardín exótico");
        addonLocals.put("ExtraGear", "Más equipamiento");
        addonLocals.put("ExtraHeads", "mas cabezas");
        addonLocals.put("HotbarPets", "mochila mascota");
        addonLocals.put("luckyblocks-sf", "cubo de la suerte"); // Same as SlimefunLuckyBlocks
        addonLocals.put("RedstoneConnector", "conector de piedra roja");
        addonLocals.put("PrivateStorage", "almacenamiento privado");
        addonLocals.put("SlimefunOreChunks", "Más bloques de mineral");
        addonLocals.put("SlimyTreeTaps", "grifo de madera de limo");
        addonLocals.put("SoulJars", "tarro de alma");
        addonLocals.put("MoreTools", "Más herramientas");
        addonLocals.put("LiteXpansion", "industria");
        addonLocals.put("MobCapturer", "Biocaptura");
        addonLocals.put("SoundMuffler", "silenciador");
        addonLocals.put("ExtraTools", "herramientas adicionales");
        addonLocals.put("TranscEndence", "tecnología final");
        addonLocals.put("Liquid", "líquido");
        addonLocals.put("SlimefunWarfare", "nave de guerra");
        addonLocals.put("InfernalExpansion", "nave abisal");
        addonLocals.put("FluffyMachines", "máquina esponjadora");
        addonLocals.put("SlimyRepair", "Arreglo de elementos de limo");
        addonLocals.put("InfinityExpansion", "avaricia sin fin"); // Avoid conflict with InfinityExpansion-Changed
        addonLocals.put("FoxyMachines", "Tecnología misteriosa");
        addonLocals.put("GlobalWarming", "calentamiento global");
        addonLocals.put("GlobiaMachines", "maquina global");
        addonLocals.put("DynaTech", "Tecnología energética");
        addonLocals.put("GeneticChickengineering", "ingeniería de pollos"); // Same as GeneticChickengineering-Reborn
        addonLocals.put("GeneticChickengineering-Reborn", "ingeniería de pollos"); // Same as GeneticChickengineering
        addonLocals.put("ClayTech", "Tecnología de arcilla"); // Same as ClayTech-Fixed
        addonLocals.put("ClayTech-Fixed", "Tecnología de arcilla"); // Same as ClayTech
        addonLocals.put("SpaceTech", "tecnología espacial"); // Same as SpaceTech-Fixed
        addonLocals.put("SpaceTech-Fixed", "tecnología espacial"); // Same as SpaceTech
        addonLocals.put("FNAmplifications", "FNciencia y tecnologia");
        addonLocals.put("SimpleMaterialGenerators", "Generador de materiales sencillo");
        addonLocals.put("Netheopoiesis", "Utopía inferior");
        addonLocals.put("Networks", "red"); // Avoid conflict with Networks-Changed, (sometimes it is NetworksExpansion)
        addonLocals.put("EMC2", "Intercambio equivalente(EMC2)"); // Avoid conflict with EquivalencyTech
        addonLocals.put("Nexcavate", "Renacimiento de la civilización");
        addonLocals.put("SimpleStorage", "Fácil almacenamiento");
        addonLocals.put("SimpleUtils", "herramienta sencilla");
        addonLocals.put("AlchimiaVitae", "autobiografía alquímica");
        addonLocals.put("SlimeTinker", "Alma del fabricante de limo");
        addonLocals.put("PotionExpansion", "Tecnología de farmacia");
        addonLocals.put("FlowerPower", "flor de fuente");
        addonLocals.put("Galactifun", "interestelar");
        addonLocals.put("Galactifun2", "interestelar2");
        addonLocals.put("ElementManipulation", "ingeniería química");
        addonLocals.put("CrystamaeHistoria", "Crónicas de cristal mágico");
        addonLocals.put("DankTech", "almacenamiento sin fondo");
        addonLocals.put("DankTech2", "almacenamiento sin fondo2");
        addonLocals.put("Networks-Changed", "red"); // Avoid conflict with Networks
        addonLocals.put("VillagerUtil", "Herramientas del aldeano");
        addonLocals.put("MissileWarfare", "tecnología de misiles");
        addonLocals.put("SensibleToolbox", "STB/tecnología del futuro");
        addonLocals.put("Endrex", "finalizar la expansión");
        addonLocals.put("Bump", "Bumpmagia");
        addonLocals.put("FinalTech", "Habilidades fuera de servicio"); // Same as FinalTECH
        addonLocals.put("FinalTECH", "Habilidades fuera de servicio"); // Same as FinalTech
        addonLocals.put("SlimefunLuckyBlocks", "cubo de la suerte"); // Same as luckyblocks-sf
        addonLocals.put("FutureTech", "tecnología del futuro");
        addonLocals.put("DemonicExpansion", "Expansión de encantamiento");
        addonLocals.put("BedrockTechnology", "Tecnología de base");
        addonLocals.put("SlimefunItemExpansion", "Más artículos");
        addonLocals.put("SupplementalServiceableness", "Más necesidades diarias");
        addonLocals.put("GuizhanCraft", "Tecnología asesina de fantasmas");
        addonLocals.put("Magmanimous", "Aliento de lava");
        addonLocals.put("UltimateGenerators-RC27", "El generador definitivo"); // Same as UltimateGenerators
        addonLocals.put("UltimateGenerators", "El generador definitivo"); // Same as UltimateGenerators-RC27
        addonLocals.put("UltimateGenerators2", "El generador definitivo2");
        addonLocals.put("CrispyMachine", "Tecnología nítida");
        addonLocals.put("Chocoholics", "Artesanía del Valle del Fuego de Insectos"); // Same as ChocoHills
        addonLocals.put("ChocoHills", "Artesanía del Valle del Fuego de Insectos"); // Same as Chocoholics
        addonLocals.put("draconic", "Estudios de dragones"); // Same as DracFun
        addonLocals.put("DracFun", "Estudios de dragones"); // Same as draconic
        addonLocals.put("EzSFAddon", "EZciencia y tecnologia"); // Same as EzTech, EzSlimeFunAddon
        addonLocals.put("EzTech", "EZciencia y tecnologia"); // Same as EzSFAddon, EzSlimeFunAddon
        addonLocals.put("EzSlimeFunAddon", "EZciencia y tecnologia"); // Same as EzSFAddon, EzTech
        addonLocals.put("RandomExpansion", "Expansión aleatoria");
        addonLocals.put("SlimyBees", "abejas forestales");
        addonLocals.put("ObsidianExpansion", "Tecnología de obsidiana");
        addonLocals.put("EMCTech", "EMCciencia y tecnologia");
        addonLocals.put("RelicsOfCthonia", "Reliquias cthunianas");
        addonLocals.put("Supreme", "Instituto Supremo de Investigaciones");
        addonLocals.put("DyeBench", "Tecnología de teñido");
        addonLocals.put("MiniBlocks", "mini cubos");
        addonLocals.put("SpiritsUnchained", "Artesano del alma");
        addonLocals.put("Cultivation", "técnicas agrícolas");
        addonLocals.put("Gastronomicon", "Conocedor gourmet");
        addonLocals.put("SmallSpace", "pequeño mundo");
        addonLocals.put("BetterReactor", "reactor industrial"); // Avoid conflict with Fusion
        addonLocals.put("VillagerTrade", "Comercio de aldeanos");
        addonLocals.put("SlimeFrame", "Armadura de limo");
        addonLocals.put("AdvancedTech", "Tecnología avanzada");
        addonLocals.put("Quaptics", "Óptica cuántica");
        addonLocals.put("CompressionCraft", "Proceso de compresión");
        addonLocals.put("ThermalFun", "Tecnología Zhuoyan");
        addonLocals.put("FastMachines", "maquina rapida");
        addonLocals.put("MomoTech", "Tecnología confusa");
        addonLocals.put("LogicTech", "nave lógica"); // Same as LogicTECH, a SlimefunCustomizer configuration
        addonLocals.put("LogiTech", "nave lógica"); // Same as LogiTECH, a Slimefun addon
        addonLocals.put("LogicTECH", "nave lógica"); // Same as LogicTech
        addonLocals.put("LogiTECH", "nave lógica"); // Same as LogiTech
        addonLocals.put("SlimeAEPlugin", "Energía y Aplicaciones2");
        addonLocals.put("SlimeChem", "Química del limo");
        addonLocals.put("WilderNether", "la vida del infierno");
        addonLocals.put("MapJammers", "Interferencia del mapa");
        addonLocals.put("Cakecraft", "artesanía de pastel"); // Same as MyFirstAddon
        addonLocals.put("SFMobDrops", "Gotas de criaturas personalizadas");
        addonLocals.put("Drugfun", "Suministros médicos personalizados");
        addonLocals.put("SlimefunNukes", "bomba nuclear de limo");
        addonLocals.put(
            "SlimeCustomizer",
            "Accesorio de limo personalizado"
        ); // Avoid conflict with RaySlimefunAddon, RykenSlimefunCustomizer, RykenSlimeCustomizer
        addonLocals.put(
            "RykenSlimeCustomizer",
            "RykenArchivo adjunto personalizado"
        ); // Same as RykenSlimefunCustomizer, avoid conflict with RaySlimefunAddon
        addonLocals.put(
            "RykenSlimefunCustomizer",
            "RykenArchivo adjunto personalizado"
        ); // Same as RykenSlimeCustomizer, avoid conflict with RaySlimefunAddon
        addonLocals.put("FinalTECH-Changed", "Habilidades fuera de servicio-Revisión");
        addonLocals.put("BloodAlchemy", "Artesanía de alquimia de sangre"); // Same as BloodyAlchemy
        addonLocals.put("Laboratory", "laboratorio");
        addonLocals.put("MobEngineering", "Bioingeniería");
        addonLocals.put("TsingshanTechnology", "Tecnología Qingshan"); // Same as TsingshanTechnology-Fixed
        addonLocals.put("TsingshanTechnology-Fixed", "Tecnología Qingshan"); // Same as TsingshanTechnology
        addonLocals.put("PomaExpansion", "Robot Android avanzado");
        addonLocals.put("BuildingStaff", "varita de construcción");
        addonLocals.put("IDreamOfEasy", "yi meng");
        addonLocals.put("Magic8Ball", "magia8bola numérica");
        addonLocals.put("InfinityExpansionAutomation", "Automatización sin fin");
        addonLocals.put("ZeroTech", "Mio Tecnología");
        addonLocals.put("Ex-Limus", "Herramientas para principiantes");
        addonLocals.put("NotEnoughAddons", "Tecnología colorida");
        addonLocals.put("SFWorldEdit", "Creador de limo[SW]"); // Avoid conflict with SlimefunWorldedit
        addonLocals.put("RSCEditor", "RSCEditor");
        addonLocals.put("JustEnoughGuide", "mejor libro de limo");
        addonLocals.put("SummaryHelper", "Gestión de la puntuación de moco");
        addonLocals.put("HardcoreSlimefun", "limo duro");
        addonLocals.put("SFCalc", "calculadora de limo");
        addonLocals.put("SfChunkInfo", "Información del bloque");
        addonLocals.put("SlimefunAdvancements", "Tareas de limo personalizadas");
        addonLocals.put("SlimeHUD", "Visualización de información del bloque"); // Same as SlimeHUDPlus
        addonLocals.put("SlimeHUDPlus", "Visualización de información del bloque"); // Same as SlimeHUD
        addonLocals.put(
            "RaySlimefunAddon",
            "Accesorio avanzado de limo personalizado"
        ); // Avoid conflict with SlimeCustomizer, RykenSlimefunCustomizer, RykenSlimeCustomizer
        addonLocals.put("SCrafter", "SCciencia y tecnologia"); // Same as SlimefunZT
        addonLocals.put("CrispyMachines", "máquina crujiente");
        addonLocals.put("DimensionTraveler", "Viajero de dimensión");
        addonLocals.put("HardlessMachine", "herramienta de rebote");
        addonLocals.put("XingChengCraft", "Artesanía estelar"); // Same as XingChenCraft, XingCheng_Craft
        addonLocals.put("XingChenCraft", "Artesanía estelar"); // Same as XingChengCraft, XingCheng_Craft
        addonLocals.put("DefoLiationTech", "Tecnología Luoye"); // Same as DefoliationTech
        addonLocals.put("HaimanTech2", "Instituto Haiman de Ciencia y Tecnología");
        addonLocals.put("HaimanTech", "Tecnología Haiman");
        addonLocals.put("InfiniteExtensionV2", "Expansión sin finV2");
        addonLocals.put("InfiniteExtension", "Expansión sin fin");
        addonLocals.put("OrangeTech", "Tecnología naranja");
        addonLocals.put("GreedAndCreation", "La codicia y la creación");
        addonLocals.put("BocchiTechnology", "Tecnología Boqi"); // Same as Bocchi_Technology
        addonLocals.put("Bocchi_Technology", "Tecnología Boqi"); // Same as BocchiTechnology
        addonLocals.put("OreTech", "Tecnología Mineral");
        addonLocals.put("HLGtech", "Biotecnología"); // Avoid conflict with MobTech
        addonLocals.put("InfiniteExtensionV2-Reconfiguration", "Expansión sin finV2-Revisión");
        addonLocals.put("BigSnakeTech", "Tecnología Orochi"); // Same as BigSnake-Tech
        addonLocals.put("BigSnake-Tech", "Tecnología Orochi"); // Same as BigSnakeTech
        addonLocals.put("EpoTech", "Tecnología de época");
        addonLocals.put("EnchanterLimit", "Limitar la máquina de encantamiento");
        addonLocals.put("BlockLimiter", "límite de bloque");
        addonLocals.put("SfItemsExporter", "Exportación de artículos de limo");
        addonLocals.put("SlimeGlue", "pegamento de limo");
        addonLocals.put("KeepSoulbound", "Unión de alma avanzada");
        addonLocals.put("SlimeFunItemBanned", "Artículos prohibidos");
        addonLocals.put("Azap", "sentencia de prisión");
        addonLocals.put("CringleBosses", "caosBoss");
        addonLocals.put("SlimefunNotchApple", "limoNotchpatrón de bandera");
        addonLocals.put("Huolaiy", "Nave de fuego Rai");
        addonLocals.put("WonderfulTransmitter", "lanzador de maravillas");
        addonLocals.put("OreGeneration", "generador de minerales"); // Avoid conflict with Mineralgenerator
        addonLocals.put("SlimeSec", "limo seguro");
        addonLocals.put("Paradoxium", "Tecnología Fénix");
        addonLocals.put("LuckyPandas", "panda afortunado");
        addonLocals.put("PhoenixSciences", "Ciencia Fénix");
        addonLocals.put("DarkMatter", "encanto nocturno");
        addonLocals.put("GeneticManipulation", "gen genético");
        addonLocals.put("MoneyAndThings", "moneda sólida");
        addonLocals.put("BeyondHorizons", "éter");
        addonLocals.put("ChestTerminal", "terminal de caja");
        addonLocals.put("Hohenheim", "proceso de transmutación"); // Same as hohenheim
        addonLocals.put("BetterFarming", "granja mayor"); // Same as betterfarming
        addonLocals.put("NewBeginnings", "recién nacido"); // Same as New-Beginnings
        addonLocals.put("EndCombat", "Eventualmente");
        addonLocals.put("EnderPanda", "fin panda");
        addonLocals.put("SlimeVoid", "limo vacío"); // Same as SlimefunVoid
        addonLocals.put("ArcaneExploration", "Fortalecimiento de monstruos");
        addonLocals.put("MagicXpansion", "sueño fantasma");
        addonLocals.put("SlimeQuest", "misión de limo");
        addonLocals.put("CompressedMachines", "Compresor");
        addonLocals.put("DisguiseCookie", "disfrazar galletas");
        addonLocals.put("FireSlime", "Tecnología de lodo de carbono");
        addonLocals.put("NetherEnough", "Capítulo de fantasía del abismo");
        addonLocals.put("BarrelWiper", "Removedor de cubos esponjosos");
        addonLocals.put("BearFluidTanks", "Embalse tipo oso");
        addonLocals.put("Tofu-Addons", "artesanía de tofu");
        addonLocals.put("AdditionalWeaponry", "fábrica de armas");
        addonLocals.put("BoxOfChocolates", "artesanía de chocolate");
        addonLocals.put("MagicPowder", "artesanía konjac"); // Same as magic-powder
        addonLocals.put("XpCreator", "Artesanía del creador");
        addonLocals.put("SlimefunCombat", "modelo de bomba atómica");
        addonLocals.put("ObsidianArmor", "Armadura de aleación de obsidiana."); // Same as Obsidian-Armor
        addonLocals.put("FinalGenerations", "Generaciones bajo un mismo techo");
        addonLocals.put("Fusion", "reactor industrial Fusion"); // Avoid conflict with BetterReactor
        addonLocals.put("Slimedustry", "industria del limo");
        addonLocals.put("Spikes", "Apuñalar más");
        addonLocals.put("SlimeRP", "fábrica moderna");
        addonLocals.put("Brewery", "vinificación"); // Avoid conflict with BreweryMenu
        addonLocals.put("EquivalencyTech", "Intercambio equivalente(ET)"); // Avoid conflict with EMC2
        addonLocals.put("GeyserHeads", "Materiales de cráneo interoperables");
        addonLocals.put("VariousClutter", "desorden");
        addonLocals.put("Mineralgenerator", "Mineral generador de minerales"); // Avoid conflict with OreGeneration
        addonLocals.put("CivilizationEvolution", "AGciencia y tecnologia"); // Avoid conflict with AgTech, ProductState
        addonLocals.put("RemiliasUtilities", "Tecnología Remi");
        addonLocals.put("BetterChests", "mejor caja");
        addonLocals.put("SlimeFood", "Delicias de limo");
        addonLocals.put("SlimeVision", "visualización de moco");
        addonLocals.put("WorldeditSlimefun", "Creador de limo[WS]"); // Avoid conflict with SFWorldedit
        addonLocals.put("MinimizeFactory", "Fábrica minimizada");
        addonLocals.put("InfinityCompress", "Compresión sin fin");
        addonLocals.put("SlimeFrameExtension", "Expansión de Warframe de limo");
        addonLocals.put("BreweryMenu", "vinificaciónGUI"); // Avoid conflict with Brewery
        addonLocals.put("MySlimefunAddon", "Ampliación casera");
        addonLocals.put("MyFirstAddon", "artesanía de pastel"); // Same as Cakecraft
        addonLocals.put("StackMachine", "máquinas apiladoras"); // Avoid conflict with SlimefunStackMachine
        addonLocals.put("SlimefunStackMachine", "máquina apiladora de limo"); // Avoid conflict with StackMachine
        addonLocals.put("CraftableEnchantments", "Artesanía encantadora");
        addonLocals.put("sj_Expansion", "sjのexpansión de limo");
        addonLocals.put("SlimefunZT", "SCciencia y tecnologia"); // Same as SCrafter
        addonLocals.put("SlimefunAddon", "CAPTAINchad12Ampliación casera"); // Unbelievable...
        addonLocals.put("AngleTech", "Tecnología de inclinación");
        addonLocals.put("magicexpansion", "Expansión mágica"); // Same as MagicExpansion, avoid conflict with Magic
        addonLocals.put("MagicExpansion", "Expansión mágica"); // Same as magicexpansion, avoid conflict with Magic
        addonLocals.put("SlimefunHopper", "embudo de limo");
        addonLocals.put("SlimefunAccessor", "accesor remoto");
        addonLocals.put("ExoticGardenComplex", "jardín exótico"); // Same as ExoticGarden
        addonLocals.put("magic-powder", "artesanía konjac"); // Same as MagicPowder
        addonLocals.put("Obsidian-Armor", "Armadura de aleación de obsidiana."); // Same as ObsidianArmor
        addonLocals.put("BloodyAlchemy", "Artesanía de alquimia de sangre"); // Same as BloodAlchemy
        addonLocals.put("hohenheim", "proceso de transmutación"); // Same as Hohenheim
        addonLocals.put("HALsAddon", "el lugar final"); // Same as slimestack
        addonLocals.put("slimestack", "el lugar final"); // Same as HALsAddon
        addonLocals.put("SlimefunVoid", "limo vacío"); // Same as SlimeVoid
        addonLocals.put("betterfarming", "granja mayor"); // Same as BetterFarming
        addonLocals.put("New-Beginnings", "recién nacido"); // Same as NewBeginnings
        addonLocals.put("ExLimus", "Herramientas para principiantes"); // Same as Ex-Limus
        addonLocals.put("Aeterum", "Caballo de los dioses");
        addonLocals.put("PoseidonAddon", "tecnología de sobretensión");
        addonLocals.put("Aircraft", "avión de limo");
        addonLocals.put("InfinityExpansion2", "avaricia sin fin2");
        addonLocals.put("EtherTech", "Tecnología Xusu");
        addonLocals.put("SlimefunTimeit", "monitor de rendimiento");
        addonLocals.put("AgTech", "AGciencia y tecnologia - RSC"); // Avoid conflict with CivilizationEvolution, ProductState
        addonLocals.put("CavernTech", "tecnología de cuevas");
        addonLocals.put("Creation", "Creación");
        addonLocals.put("Greed", "avaro");
        addonLocals.put("HoosierTech", "Tecnología Husier");
        addonLocals.put("HorizonsGears", "armadura del horizonte");
        addonLocals.put("langui", "tecnología perezosa");
        addonLocals.put("Magic", "magia"); // Same as MagicExpansion, magicexpansion
        addonLocals.put("MetaCoin", "moneda digital");
        addonLocals.put("MobSimulationPlus", "Ampliación de biochips");
        addonLocals.put("MoreUniqueTools", "Herramientas más sorprendentes");
        addonLocals.put("PinksheepTech", "Tecnología Fenyang");
        addonLocals.put("PinksheepTech_EpoTech", "Expansión de época");
        addonLocals.put("RepairStation", "SCestación de reparación"); // Same as SlimeCustomizerRepairStation
        addonLocals.put("SlimeCustomizerRepairStation", "SCestación de reparación"); // Same as RepairStation
        addonLocals.put("SlimefunNetherTech2", "Tecnología inferior2");
        addonLocals.put("snion", "accionamiento industrial");
        addonLocals.put("SuperFood", "superalimento");
        addonLocals.put("Typhfun", "Typhfun");
        addonLocals.put("WolfyMachines", "máquina de lobo");
        addonLocals.put("XingCheng_Craft", "Artesanía estelar"); // Same as XingChengCraft, XingChenCraft
        addonLocals.put("WorldTaste", "Sabor del mundo");
        addonLocals.put("Automation", "computadora limo");
        addonLocals.put("MobTech", "Biotecnología(MT)"); // Avoid conflict with HLGTech
        addonLocals.put("Strophodungeons", "laberinto de mazmorra");
        addonLocals.put("SFTeacher", "Tutorial de tecnología de limo");
        addonLocals.put("Slimefunexpansion", "Expansión de la tecnología Slime");
        addonLocals.put("HiveCorporation", "Hcompañía");
        addonLocals.put("BlackFishTech", "Tecnología de pez negro");
        addonLocals.put("MerakTech", "Tecnología Tianxuan");
        addonLocals.put("TinselStar", "Chao Lanxing");
        addonLocals.put("Annihilation_Tech", "Tecnología de aniquilación"); // Same as AnnihilationTech
        addonLocals.put("AnnihilationTech", "Tecnología de aniquilación"); // Same as Annihilation_Tech
        addonLocals.put("HiServerTech", "Hitecnología mundial");
        addonLocals.put("DefoliationTech", "Tecnología Luoye"); // Same as DefoLiationTech
        addonLocals.put("HeadQuantumStorage", "Versión calavera del almacenamiento cuántico");
        addonLocals.put("LengShangTech", "Tecnología Leng Shang");
        addonLocals.put("MuzhouTech", "Tecnología Muzhou");
        addonLocals.put("DFD_InfiniteExtensionV2", "DFD - Sin finV2Versión refactorizada");
        addonLocals.put("DFD_Expand", "DFD - Tecnología Yuanluo");
        addonLocals.put("Blocktreetech", "árbol de bloques");
        addonLocals.put("TinCraft", "&kabc &fTinCraft &kabc&r");
        addonLocals.put("InfinityExpansion-Changed", "avaricia sin fin(Versión revisada de Xinzi)"); // Avoid conflict with InfinityExpansion
        addonLocals.put("fm_tech", "Tecnología de madera flotante");
        addonLocals.put("Supermarket", "máquina de superpotencia");
        addonLocals.put("HseerTech", "HseerMCciencia y tecnologia");
        addonLocals.put("ZeroSequenceTechnique", "Tecnología de secuencia cero");
        addonLocals.put("OriginTech", "Artesanía de origen");
        addonLocals.put("SimpleTech", "tecnología simple");
        addonLocals.put("Komutech", "Tecnología Koumu");
        addonLocals.put("SlimefunInfiniteBlocks", "Bloques sin fin de limo");
        addonLocals.put("RSC_YunQi_History", "civilización yunqi");
        addonLocals.put("fvvtech", "fvvciencia y tecnologia");
        addonLocals.put("AeroDragonTech", "Tecnología Feilong");
        addonLocals.put("REGS", "Historia de Mu Lai");
        addonLocals.put("FengQi_Tech", "Tecnología Fengqi");
        addonLocals.put("YINGMO", "Tecnología Yingmo"); // Same as SakuraLoveTech
        addonLocals.put("PandaTech", "Tecnología Panda");
        addonLocals.put("GLTC121", "GLTCacuerdo conjunto");
        addonLocals.put("SLTech", "SLciencia y tecnologia");
        addonLocals.put("EpoTech_Branch", "EpoTechversión de rama");
        addonLocals.put("SakuraLoveTech", "Tecnología Yingmo"); // Same as YINGMO
        addonLocals.put("SlimeBotania", "magia vegetal");
        addonLocals.put("ProductState", "AGciencia y tecnologia - PS"); // Avoid conflict with CivilizationEvolution, AgTech
    }

    public static String getOfficialAddonName(ItemGroup itemGroup, String itemId) {
        return getOfficialAddonName(itemGroup.getAddon(), itemId, def);
    }

    public static String getOfficialAddonName(
        @Nullable SlimefunAddon addon, String itemId, String callback) {
        return getOfficialAddonName(addon == null ? "Slimefun" : addon.getName(), itemId, callback);
    }

    public static String getOfficialAddonName(
        String addonName, String itemId, String callback) {
        return getAddonName(addonName, itemId, callback) + " (" + addonName + ")";
    }

    public static String getAddonName(String addonName, String itemId, String callback) {
        if (addonName == null) {
            return ChatColors.color(callback);
        }

        if ("RykenSlimefunCustomizer".equalsIgnoreCase(addonName)
            || "RykenSlimeCustomizer".equalsIgnoreCase(addonName)) {
            return getRSCLocalName(itemId);
        }
        String localName = addonLocals.get(addonName);
        return ChatColors.color(localName == null ? callback : localName);
    }

    // get a rsc addon name by item id
    public static String getRSCLocalName(String itemId) {
        for (Map.Entry<String, Set<String>> entry : rscLocals.entrySet()) {
            if (entry.getValue().contains(itemId)) {
                return ChatColors.color(entry.getKey());
            }
        }

        String def = addonLocals.get("RykenSlimefunCustomizer");
        if (def == null) {
            def = addonLocals.get("RykenSlimeCustomizer");
        }

        if (rscItems.isEmpty()) {
            try {
                Plugin rsc1 = Bukkit.getPluginManager().getPlugin("RykenSlimefunCustomizer");
                Plugin rsc2 = null;
                if (rsc1 == null) {
                    rsc2 = Bukkit.getPluginManager().getPlugin("RykenSlimeCustomizer");
                    if (rsc2 == null) {
                        return def;
                    }
                }

                Plugin rsc = rsc1 == null ? rsc2 : rsc1;
                if (rsc == null) {
                    return def;
                }
                Object addonManager = ReflectionUtil.getValue(rsc, "addonManager");
                if (addonManager != null) {
                    Object projectAddons = ReflectionUtil.getValue(addonManager, "projectAddons");
                    @SuppressWarnings("unchecked")
                    Map<Object, Object> map = (Map<Object, Object>) projectAddons;
                    if (map != null) {
                        for (Map.Entry<Object, Object> entry : map.entrySet()) {
                            Object addon = entry.getValue();
                            Object addonName = ReflectionUtil.getValue(addon, "addonName");
                            String name = (String) addonName;
                            if (name == null) {
                                continue;
                            }
                            Object preloadItems = ReflectionUtil.getValue(addon, "preloadItems");
                            @SuppressWarnings("unchecked")
                            Map<Object, Object> items = (Map<Object, Object>) preloadItems;
                            Map<String, SlimefunItemStack> read = new HashMap<>();
                            if (items != null) {
                                for (Map.Entry<Object, Object> itemEntry : items.entrySet()) {
                                    String id = (String) itemEntry.getKey();
                                    SlimefunItemStack item = (SlimefunItemStack) itemEntry.getValue();
                                    read.put(id, item);
                                }
                            }
                            rscItems.put(name, read);
                        }
                    }
                }
            } catch (Exception e) {
                Debug.trace(e);
            }
        }

        for (Map.Entry<String, Map<String, SlimefunItemStack>> entry : rscItems.entrySet()) {
            Map<String, SlimefunItemStack> items = entry.getValue();
            if (items.containsKey(itemId)) {
                return ChatColors.color(entry.getKey());
            }
        }

        return ChatColors.color(def);
    }

    public static String getOfficialAddonName(
        ItemGroup itemGroup, String itemId, String callback) {
        return itemGroup.getAddon() == null ? def : getOfficialAddonName(itemGroup.getAddon(), itemId, callback);
    }

    public static String getOfficialAddonName(@Nullable SlimefunAddon addon, String itemId) {
        return getOfficialAddonName(addon, itemId, def);
    }

    public static String getOfficialAddonName(String addonName, String itemId) {
        return getOfficialAddonName(addonName, itemId, def);
    }

    public static String getAddonName(ItemGroup itemGroup, String itemId) {
        return getAddonName(itemGroup, itemId, def);
    }

    public static String getAddonName(ItemGroup itemGroup, String itemId, String callback) {
        return itemGroup.getAddon() == null
            ? def
            : getAddonName(itemGroup.getAddon().getName(), itemId, callback);
    }

    public static String getAddonName(@Nullable SlimefunAddon addon) {
        if (addon == null) return def;
        return ChatColors.color(addonLocals.getOrDefault(addon.getName(), def));
    }

    public static String getAddonName(@Nullable SlimefunAddon addon, String itemId) {
        return getAddonName(addon, itemId, def);
    }

    public static String getAddonName(@Nullable SlimefunAddon addon, String itemId, String callback) {
        return getAddonName(addon == null ? addonLocals.get("Slimefun") : addon.getName(), itemId, callback);
    }

    public static String getAddonName(String addonName, String itemId) {
        return getAddonName(addonName, itemId, def);
    }

    public static void addRSCLocal(String rscAddonName, String itemId) {
        if (!rscLocals.containsKey(rscAddonName)) {
            rscLocals.put(rscAddonName, new HashSet<>());
        }

        rscLocals.get(rscAddonName).add(itemId);
    }

    public static String getDisplayName(ItemGroup itemGroup, Player player) {
        ItemMeta meta = itemGroup.getItem(player).getItemMeta();
        if (meta == null) {
            return def;
        }

        return meta.getDisplayName();
    }
}
