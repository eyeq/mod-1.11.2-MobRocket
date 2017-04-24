package eyeq.mobrocket;

import eyeq.mobrocket.item.ItemRocket;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

import static eyeq.mobrocket.MobRocket.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class MobRocket {
    public static final String MOD_ID = "eyeq_mobrocket";

    @Mod.Instance(MOD_ID)
    public static MobRocket instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static Item rocket;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        rocket = new ItemRocket().setUnlocalizedName("rocket").setCreativeTab(CreativeTabs.TOOLS);

        GameRegistry.register(rocket, resource.createResourceLocation("rocket"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(new ItemStack(rocket), "T", "F",
                'T', Blocks.TNT, 'F', Items.FIREWORKS);
    }

	@SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(rocket);
    }
	
    public static void createFiles() {
    	File project = new File("../1.11.2-MobRocket");
    	
        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, rocket, "Mob Rocket");
        language.register(LanguageResourceManager.JA_JP, rocket, "モブ花火");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createItemJson(project, rocket, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
    }
}
