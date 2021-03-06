package newhorizon.block.special;

import arc.func.Cons;
import arc.math.geom.Position;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.EntityGroup;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.logic.Ranged;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import newhorizon.interfaces.BeforeLoadc;
import newhorizon.interfaces.ServerInitc;
import newhorizon.vars.NHWorldVars;
import org.jetbrains.annotations.NotNull;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public abstract class CommandableBlock extends Block{
	public CommandableBlock(String name){
		super(name);
		update = configurable = true;
	}
	
	public abstract class CommandableBlockBuild extends Building implements BeforeLoadc, Ranged, ServerInitc{
		@NotNull public abstract CommandableBlockType getType();
		
		public abstract void triggered(Integer point2);
		
		public abstract boolean canCommand();
		public abstract boolean overlap();
		public abstract boolean isCharging();
		public abstract boolean isPreparing();
		public abstract void setPreparing();
		
		@Override
		public void remove(){
			super.remove();
			NHWorldVars.commandables.remove(this);
		}
		
		@Override
		public void placed(){
			super.placed();
			beforeLoad();
		}
		
		@Override
		public Building init(Tile tile, Team team, boolean shouldAdd, int rotation){
			NHWorldVars.advancedLoad.add(this);
			return super.init(tile, team, shouldAdd, rotation);
		}
		
		@Override
		public void loadAfterConnect(){NHWorldVars.commandables.add(this); }
		
		@Override
		public void beforeLoad(){ NHWorldVars.commandables.add(this); }
	}
	
	public enum CommandableBlockType{
		defender, attacker
	}
	
	public class CommandEntity implements Drawc, Timedc, Teamc{
		public Cons<Teamc> act;
		
		public boolean added;
		public float damage, radius;
		public transient int id = EntityGroup.nextId();
		public transient float time, lifetime;
		public transient float x, y;
		public transient Team team;
		
		@Override public float clipSize(){return 500f;}
		
		@Override public void draw(){}
		
		@Override public void update(){
			time = Math.min(time + Time.delta, lifetime);
			if (time >= lifetime) {
				remove();
			}
		}
		
		@Override
		public void remove(){
			Groups.draw.remove(this);
			Groups.all.remove(this);
			added = false;
		}
		
		@Override
		public void add(){
			if(added)return;
			Groups.all.add(this);
			Groups.draw.add(this);
			added = true;
		}
		
		@Override public boolean isLocal(){
			return this instanceof Unitc && ((Unitc)this).controller() == player;
		}
		@Override public boolean isRemote(){
			return this instanceof Unitc && ((Unitc)this).isPlayer() && !isLocal();
		}
		@Override public float fin(){return time / lifetime;}
		@Override public float time(){return time;}
		@Override public void time(float time){this.time = time;}
		@Override public float lifetime(){return lifetime;}
		@Override public void lifetime(float lifetime){this.lifetime = lifetime;}
		@Override public boolean isNull(){ return false; }
		@Override public <T extends Entityc> T self(){ return (T)this; }
		@Override public <T> T as(){ return (T)this; }
		@Override public void set(float x, float y){
			this.x = x;
			this.y = y;
		}
		@Override public void set(Position pos){set(pos.getX(), pos.getY());}
		@Override public void trns(float x, float y){set(this.x + x, this.y + y);}
		@Override public void trns(Position pos){trns(pos.getX(), pos.getY());}
		@Override public int tileX(){return 0;}
		@Override public int tileY(){return 0; }
		@Override public Floor floorOn(){ return null; }
		@Override public Block blockOn(){ return null; }
		@Override public boolean onSolid(){ return false; }
		@Override public Tile tileOn(){ return null; }
		@Override public float getX(){ return 0; }
		@Override public float getY(){ return y; }
		@Override public float x(){ return x; }
		@Override public void x(float x){ this.x = x; }
		@Override public float y(){ return y; }
		@Override public void y(float y){ this.y = y; }
		@Override public boolean isAdded(){ return added; }
		@Override public <T> T with(Cons<T> cons) {
			cons.get((T)this);
			return (T)this;
		}
		@Override public int classId(){ return 1001; }
		@Override public boolean serialize(){ return false; }
		@Override public void read(Reads read){ }
		@Override public void write(Writes write){ }
		@Override public void afterRead(){ }
		@Override public int id(){return id; }
		@Override public void id(int id){ this.id = id; }
		@Override public String toString(){
			return "CommandEntity{" + "added=" + added + ", id=" + id + ", x=" + x + ", y=" + y + ", lifetime=" + lifetime + '}';
		}
		@Override public boolean cheating(){
			return team.rules().cheat;
		}
		@Override public Building core(){
			return team.core();
		}
		@Override public Building closestCore(){
			return team.core();
		}
		@Override public Building closestEnemyCore(){
			return state.teams.closestEnemyCore(x, y, team);
		}
		@Override public Team team(){
			return team;
		}
		@Override public void team(Team team){
			this.team = team;
		}
	}
}
