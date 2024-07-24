package xyz.goldendupe.bootstrap;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LuckPermsAfterBootstrap extends AfterBootstrap{
	public static String getMetadata(@NotNull UUID uniqueId, @NotNull String node){
		LuckPerms luckPerms = LuckPermsProvider.get();
		return luckPerms.getUserManager().getUser(uniqueId).getCachedData().getMetaData().getMetaValue(node);
	}
	public static void setMetadata(@NotNull UUID uniqueId, @NotNull String node, @NotNull String value){
		LuckPerms luckPerms = LuckPermsProvider.get();
		User user = luckPerms.getUserManager().getUser(uniqueId);
		user.data().clear((n)->n.getKey().equalsIgnoreCase(node));
		MetaNode roleNode = MetaNode.builder(node, value).build();
		user.data().add(roleNode);
		luckPerms.getUserManager().saveUser(user);

	}
}
