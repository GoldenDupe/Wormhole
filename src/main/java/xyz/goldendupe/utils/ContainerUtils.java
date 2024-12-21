package xyz.goldendupe.utils;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ContainerUtils {
	private ContainerUtils() {}


	public static boolean isShulkerBox(ItemStack itemStack){
		return itemStack.getType() == Material.SHULKER_BOX || itemStack.getType().name().endsWith("_SHULKER_BOX");
	}

	public static boolean isBundle(ItemStack itemStack){
		return itemStack.getType() == Material.BUNDLE || itemStack.getType().name().endsWith("_BUNDLE");
	}

	public static boolean isCarryableContainer(ItemStack itemStack){
		return isShulkerBox(itemStack) || isBundle(itemStack);
	}

	@NotNull
	public static List<ItemStack> getCarryableContainer(ItemStack itemStack){
		if (isBundle(itemStack)){
			//noinspection UnstableApiUsage
			return ((BundleMeta) itemStack.getItemMeta()).getItems();
		} else if (isShulkerBox(itemStack)){
			return Arrays.stream(((ShulkerBox) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState()).getInventory().getContents()).toList();
		}
		return Collections.emptyList();
	}

	@Nullable
	public static Inventory getShulkerInventory(ItemStack itemStack){
		if (isShulkerBox(itemStack)){
			return ((ShulkerBox) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState()).getInventory();
		}
		return null;
	}
}
