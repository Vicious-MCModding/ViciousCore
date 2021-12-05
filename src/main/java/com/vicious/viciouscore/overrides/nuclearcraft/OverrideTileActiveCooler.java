package com.vicious.viciouscore.overrides.nuclearcraft;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.tile.INotifiable;
import com.vicious.viciouscore.common.tile.INotifier;
import com.vicious.viciouscore.overrides.IFieldCloner;
import nc.tile.fluid.TileActiveCooler;

import java.util.List;

public class OverrideTileActiveCooler extends TileActiveCooler implements INotifier<Object>, IFieldCloner {
    private INotifiable<Object> parent;
    private String lastFluid = "nullFluid";
    public OverrideTileActiveCooler(){
        super();
    }
    public OverrideTileActiveCooler(Object og){
        clone(og);
    }

    @Override
    public void notifyParent() {
        if(parent != null) parent.notify(this);
    }

    @Override
    public void addParent(INotifiable<Object> parent) {
        this.parent=parent;
    }

    @Override
    public List<INotifiable<Object>> getParents() {
        return Lists.newArrayList(parent);
    }

    @Override
    public void setParents(List<INotifiable<Object>> parents) {
        if(parents == null || parents.isEmpty()) return;
        this.parent=parents.get(0);
    }
    @Override
    public void update() {
        String prevFluid = this.lastFluid;
        this.lastFluid= this.getTanks().get(0).getFluid() != null ? this.getTanks().get(0).getFluidName() : this.lastFluid;
        //Comparing mem location because they should be the same.
        if(prevFluid != lastFluid){
            notifyParent();
        }
        super.update();
    }
}
