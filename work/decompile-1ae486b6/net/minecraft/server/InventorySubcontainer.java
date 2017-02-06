package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

public class InventorySubcontainer implements IInventory {

    private String a;
    private final int b;
    public final ItemStack[] items;
    private List<IInventoryListener> d;
    private boolean e;

    public InventorySubcontainer(String s, boolean flag, int i) {
        this.a = s;
        this.e = flag;
        this.b = i;
        this.items = new ItemStack[i];
    }

    public void a(IInventoryListener iinventorylistener) {
        if (this.d == null) {
            this.d = Lists.newArrayList();
        }

        this.d.add(iinventorylistener);
    }

    public void b(IInventoryListener iinventorylistener) {
        this.d.remove(iinventorylistener);
    }

    @Nullable
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.length ? this.items[i] : null;
    }

    @Nullable
    public ItemStack splitStack(int i, int j) {
        ItemStack itemstack = ContainerUtil.a(this.items, i, j);

        if (itemstack != null) {
            this.update();
        }

        return itemstack;
    }

    @Nullable
    public ItemStack a(ItemStack itemstack) {
        ItemStack itemstack1 = itemstack.cloneItemStack();

        for (int i = 0; i < this.b; ++i) {
            ItemStack itemstack2 = this.getItem(i);

            if (itemstack2 == null) {
                this.setItem(i, itemstack1);
                this.update();
                return null;
            }

            if (ItemStack.c(itemstack2, itemstack1)) {
                int j = Math.min(this.getMaxStackSize(), itemstack2.getMaxStackSize());
                int k = Math.min(itemstack1.count, j - itemstack2.count);

                if (k > 0) {
                    itemstack2.count += k;
                    itemstack1.count -= k;
                    if (itemstack1.count <= 0) {
                        this.update();
                        return null;
                    }
                }
            }
        }

        if (itemstack1.count != itemstack.count) {
            this.update();
        }

        return itemstack1;
    }

    @Nullable
    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, @Nullable ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public int getSize() {
        return this.b;
    }

    public String getName() {
        return this.a;
    }

    public boolean hasCustomName() {
        return this.e;
    }

    public void a(String s) {
        this.e = true;
        this.a = s;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return (IChatBaseComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName(), new Object[0]));
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void update() {
        if (this.d != null) {
            for (int i = 0; i < this.d.size(); ++i) {
                ((IInventoryListener) this.d.get(i)).a(this);
            }
        }

    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void startOpen(EntityHuman entityhuman) {}

    public void closeContainer(EntityHuman entityhuman) {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public int getProperty(int i) {
        return 0;
    }

    public void setProperty(int i, int j) {}

    public int g() {
        return 0;
    }

    public void l() {
        for (int i = 0; i < this.items.length; ++i) {
            this.items[i] = null;
        }

    }
}
