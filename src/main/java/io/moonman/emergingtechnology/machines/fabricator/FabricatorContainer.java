package io.moonman.emergingtechnology.machines.fabricator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

// Handles inventory and slots
public class FabricatorContainer extends Container {
	private final FabricatorTileEntity tileEntity;

	private int energy;
	private int progress;
	private int selection;
	private int printing;

	public FabricatorContainer(InventoryPlayer player, FabricatorTileEntity tileEntity) {
		this.tileEntity = tileEntity;

		IItemHandler handler = tileEntity.itemHandler;

		this.addSlotToContainer(new SlotItemHandler(handler, 0, 17, 35));
		this.addSlotToContainer(new SlotItemHandler(handler, 1, 123, 35));

		// Inventory
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 104 + y * 18));
			}
		}

		// Hotbar
		for (int x = 0; x < 9; x++) {
			this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 162));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) 
	{
		this.tileEntity.setField(id, data);
	}
	
	@Override
	public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.listeners.size(); ++i) 
		{
			IContainerListener listener = (IContainerListener)this.listeners.get(i);
			if(this.energy != this.tileEntity.getField(0)) listener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
			if(this.progress != this.tileEntity.getField(1)) listener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
			if(this.selection != this.tileEntity.getField(2)) listener.sendWindowProperty(this, 2, this.tileEntity.getField(2));
			if(this.printing != this.tileEntity.getField(3)) listener.sendWindowProperty(this, 3, this.tileEntity.getField(3));
		}
        
		this.energy = this.tileEntity.getField(0);
		this.progress = this.tileEntity.getField(1);
		this.selection = this.tileEntity.getField(2);
		this.printing = this.tileEntity.getField(3);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileEntity.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

		ItemStack stack = ItemStack.EMPTY;
		Slot fromSlot = (Slot) this.inventorySlots.get(index);

		if (fromSlot != null && fromSlot.getHasStack()) {
			ItemStack fromStack = fromSlot.getStack();
			stack = fromStack.copy();

			// If it's from the fabricator, put in player's inventory
			if (index < 2) {
				if (!this.mergeItemStack(fromStack, 2, 38, false)) {
					return ItemStack.EMPTY;
				} else {
					fromSlot.onSlotChanged();
				}
			} else {// Otherwise try to put it in input slot
				if (!this.mergeItemStack(fromStack, 0, 1, false)) {
					return ItemStack.EMPTY;
				} else {
					fromSlot.onSlotChanged();
				}
			}

			fromSlot.onTake(playerIn, fromStack);
		}
		return stack;
	}
}