package net.mcreator.minefinitygauntlet.entity;

import org.checkerframework.checker.units.qual.t;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.UUID;
import java.util.List;

public class BlackholeEntity extends PathfinderMob {
	public static final EntityDataAccessor<Integer> DATA_size = SynchedEntityData.defineId(BlackholeEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> DATA_charging = SynchedEntityData.defineId(BlackholeEntity.class, EntityDataSerializers.BOOLEAN);
	private UUID ownerUUID;
	private Vec3 targetDirection;
	private Vec3 initialVelocity;
	private int mergeCooldown = 0;
	private int deletionCooldown = 0;

	public BlackholeEntity(EntityType<BlackholeEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
		this.moveControl = new FlyingMoveControl(this, 10, true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_size, 0);
		builder.define(DATA_charging, true);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.death"));
	}

	@Override
	public boolean causeFallDamage(float l, float d, DamageSource source) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		if (damagesource.is(DamageTypes.IN_FIRE))
			return false;
		if (damagesource.getDirectEntity() instanceof AbstractArrow)
			return false;
		if (damagesource.getDirectEntity() instanceof ThrownPotion || damagesource.getDirectEntity() instanceof AreaEffectCloud || damagesource.typeHolder().is(NeoForgeMod.POISON_DAMAGE))
			return false;
		if (damagesource.is(DamageTypes.FALL))
			return false;
		if (damagesource.is(DamageTypes.CACTUS))
			return false;
		if (damagesource.is(DamageTypes.DROWN))
			return false;
		if (damagesource.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION))
			return false;
		if (damagesource.is(DamageTypes.TRIDENT))
			return false;
		if (damagesource.is(DamageTypes.FALLING_ANVIL))
			return false;
		if (damagesource.is(DamageTypes.DRAGON_BREATH))
			return false;
		if (damagesource.is(DamageTypes.WITHER) || damagesource.is(DamageTypes.WITHER_SKULL))
			return false;
		return super.hurt(damagesource, amount);
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Datasize", this.getEntityData().get(DATA_size));
		compound.putBoolean("Charging", this.getEntityData().get(DATA_charging));
		compound.putInt("MergeCooldown", this.mergeCooldown);
		compound.putInt("DeletionCooldown", this.deletionCooldown);
		if (ownerUUID != null) {
			compound.putUUID("OwnerUUID", ownerUUID);
		}
		if (targetDirection != null) {
			compound.putDouble("TargetDirX", targetDirection.x);
			compound.putDouble("TargetDirY", targetDirection.y);
			compound.putDouble("TargetDirZ", targetDirection.z);
		}
		if (initialVelocity != null) {
			compound.putDouble("InitVelX", initialVelocity.x);
			compound.putDouble("InitVelY", initialVelocity.y);
			compound.putDouble("InitVelZ", initialVelocity.z);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Datasize"))
			this.getEntityData().set(DATA_size, compound.getInt("Datasize"));
		if (compound.contains("Charging"))
			this.getEntityData().set(DATA_charging, compound.getBoolean("Charging"));
		if (compound.contains("MergeCooldown"))
			this.mergeCooldown = compound.getInt("MergeCooldown");
		if (compound.contains("DeletionCooldown"))
			this.deletionCooldown = compound.getInt("DeletionCooldown");
		if (compound.hasUUID("OwnerUUID")) {
			ownerUUID = compound.getUUID("OwnerUUID");
		}
		if (compound.contains("TargetDirX")) {
			targetDirection = new Vec3(compound.getDouble("TargetDirX"), compound.getDouble("TargetDirY"), compound.getDouble("TargetDirZ"));
		}
		if (compound.contains("InitVelX")) {
			initialVelocity = new Vec3(compound.getDouble("InitVelX"), compound.getDouble("InitVelY"), compound.getDouble("InitVelZ"));
		}
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	public void setOwner(Entity owner) {
		if (owner != null) {
			this.ownerUUID = owner.getUUID();
		}
	}

	public Entity getOwner() {
		if (ownerUUID != null && level() != null) {
			return level().getPlayerByUUID(ownerUUID);
		}
		return null;
	}

	public void setTargetDirection(Vec3 direction) {
		this.targetDirection = direction.normalize();
	}

	public void setInitialVelocity(Vec3 velocity) {
		this.initialVelocity = velocity;
	}

	public void setCharging(boolean charging) {
		this.getEntityData().set(DATA_charging, charging);
	}

	public boolean isCharging() {
		return this.getEntityData().get(DATA_charging);
	}

	// moves towards target direction and attracts other blackholes
	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
		if (level().isClientSide())
			return;
		// decrement cooldowns
		if (mergeCooldown > 0) {
			mergeCooldown--;
		}
		if (deletionCooldown > 0) {
			deletionCooldown--;
		}
		// maintain minimum hover height
		maintainMinimumHeight();
		boolean charging = getEntityData().get(DATA_charging);
		if (charging) {
			// while charging, stay still in place
			setDeltaMovement(Vec3.ZERO);
		} else {
			// passive shrinking over time (hawking radiation)
			int currentSize = getEntityData().get(DATA_size);
			if (currentSize > 0 && tickCount % 20 == 0) {
				// larger blackholes decay slower
				double decayRate = 1.0 / Math.max(1.0, Math.sqrt(currentSize));
				if (random.nextDouble() < decayRate) {
					getEntityData().set(DATA_size, Math.max(0, currentSize - 1));
				}
			}
			// remove blackhole if it shrinks to nothing
			if (currentSize <= 0) {
				discard();
				return;
			}
			// released - move with initial velocity
			if (initialVelocity != null) {
				Vec3 currentVel = getDeltaMovement();
				Vec3 newVel = new Vec3(lerp(currentVel.x, initialVelocity.x, 0.1), currentVel.y, lerp(currentVel.z, initialVelocity.z, 0.1));
				setDeltaMovement(newVel);
			}
			// blackhole attraction and merging with bidirectional gravity
			int mySize = getEntityData().get(DATA_size);
			double searchRadius = Math.min(50.0 + (mySize * 0.5), 100.0);
			AABB searchBox = getBoundingBox().inflate(searchRadius);
			List<BlackholeEntity> nearbyHoles = level().getEntitiesOfClass(BlackholeEntity.class, searchBox, e -> e != this && e.isAlive() && !e.isCharging());
			for (BlackholeEntity other : nearbyHoles) {
				if (!other.isAlive() || other.isRemoved()) {
					continue;
				}
				int otherSize = other.getEntityData().get(DATA_size);
				double distance = distanceTo(other);
				// safety check for very close blackholes
				if (distance < 0.1) {
					distance = 0.1;
				}
				if (distance < 4.0 && mergeCooldown <= 0) {
					// merge blackholes - only larger one does the merging
					if (mySize > otherSize) {
						int newSize = mySize + otherSize;
						getEntityData().set(DATA_size, newSize);
						other.discard();
						mergeCooldown = 10;
					} else if (mySize == otherSize && getId() > other.getId()) {
						int newSize = mySize + otherSize;
						getEntityData().set(DATA_size, newSize);
						other.discard();
						mergeCooldown = 10;
					}
				} else if (distance < searchRadius) {
					// bidirectional gravitational attraction with safety limits
					Vec3 toOther = other.position().subtract(position());
					Vec3 direction = toOther.normalize();
					double distanceSquared = Math.max(1.0, distance * distance);
					double myMass = mySize * mySize;
					double otherMass = otherSize * otherSize;
					// force strength based on both masses
					double baseForce = (myMass * otherMass) / (distanceSquared * 10000.0);
					// cap maximum force to prevent physics explosion
					baseForce = Math.min(baseForce, 0.5);
					// apply force to this blackhole (pulled toward other)
					double myAcceleration = baseForce / Math.max(1.0, myMass);
					Vec3 myForce = direction.scale(myAcceleration);
					Vec3 myVel = getDeltaMovement();
					Vec3 newMyVel = myVel.add(myForce);
					// cap velocity to prevent runaway acceleration
					double mySpeed = newMyVel.length();
					if (mySpeed > 2.0) {
						newMyVel = newMyVel.normalize().scale(2.0);
					}
					setDeltaMovement(newMyVel);
					// apply force to other blackhole (pulled toward this)
					double otherAcceleration = baseForce / Math.max(1.0, otherMass);
					Vec3 otherForce = direction.scale(-otherAcceleration);
					Vec3 otherVel = other.getDeltaMovement();
					Vec3 newOtherVel = otherVel.add(otherForce);
					// cap velocity to prevent runaway acceleration
					double otherSpeed = newOtherVel.length();
					if (otherSpeed > 2.0) {
						newOtherVel = newOtherVel.normalize().scale(2.0);
					}
					other.setDeltaMovement(newOtherVel);
				}
			}
			// attract nearby entities (mobs, items, players)
			attractNearbyEntities();
			// delete entities only every 5 ticks to reduce lag
			if (deletionCooldown <= 0) {
				deleteNearbyEntities();
				deletionCooldown = 5;
			}
		}
	}

	// pulls nearby entities toward the blackhole with gravity based on size and distance
	private void attractNearbyEntities() {
		int size = getEntityData().get(DATA_size);
		// attraction radius scales with size
		double attractionRadius = Math.min(10.0 + (size * 0.3), 50.0);
		AABB attractionBox = getBoundingBox().inflate(attractionRadius);
		List<Entity> nearbyEntities = level().getEntities(this, attractionBox, e -> e.isAlive() && !(e instanceof BlackholeEntity));
		Vec3 blackholePos = position().add(0, getBbHeight() * 0.5, 0);
		for (Entity entity : nearbyEntities) {
			// check if entity should be attracted
			boolean attract = shouldAttractEntity(entity);
			if (!attract) {
				continue;
			}
			double distance = distanceTo(entity);
			// skip if too close (will be deleted anyway)
			if (distance < 1.0) {
				continue;
			}
			// calculate pull strength based on blackhole size and distance
			double mass = size * size * size;
			double distanceSquared = distance * distance;
			double pullStrength = (mass * 0.0003) / distanceSquared;
			// entities very close experience exponentially stronger pull
			if (distance < 5.0) {
				pullStrength *= (5.0 / distance);
			}
			// larger entities resist pull slightly (but not much)
			float entityWidth = entity.getBbWidth();
			float entityHeight = entity.getBbHeight();
			double entityVolume = entityWidth * entityWidth * entityHeight;
			double resistance = Math.max(0.5, Math.sqrt(entityVolume));
			pullStrength = pullStrength / resistance;
			// cap maximum pull strength but allow it to be very high
			pullStrength = Math.min(pullStrength, 2.0);
			// calculate direction to blackhole center (includes vertical component)
			Vec3 entityPos = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
			Vec3 toBlackhole = blackholePos.subtract(entityPos);
			Vec3 direction = toBlackhole.normalize();
			// apply pull force with full 3D direction
			Vec3 currentVel = entity.getDeltaMovement();
			Vec3 pullForce = direction.scale(pullStrength);
			// override current velocity more aggressively for strong pulls
			Vec3 newVel;
			if (pullStrength > 0.5) {
				// strong pull - blend heavily toward blackhole
				newVel = currentVel.scale(0.3).add(pullForce);
			} else {
				// weak pull - additive
				newVel = currentVel.add(pullForce);
			}
			// higher velocity cap for stronger blackholes
			double maxSpeed = Math.min(5.0, 3.0 + (size * 0.01));
			double speed = newVel.length();
			if (speed > maxSpeed) {
				newVel = newVel.normalize().scale(maxSpeed);
			}
			entity.setDeltaMovement(newVel);
			entity.hurtMarked = true;
			// cancel fall damage for entities being pulled
			entity.fallDistance = 0;
		}
	}

	// checks if entity should be attracted based on gamemode and inventory
	private boolean shouldAttractEntity(Entity entity) {
		// non-players are always attracted
		if (!(entity instanceof Player)) {
			return true;
		}
		// check gamemode - creative and spectator are immune
		boolean isSurvivalOrAdventure = checkGamemode(entity, net.minecraft.world.level.GameType.SURVIVAL) || checkGamemode(entity, net.minecraft.world.level.GameType.ADVENTURE);
		if (!isSurvivalOrAdventure) {
			return false;
		}
		// check if player has infinity gauntlet
		if (entity instanceof Player player) {
			boolean hasGauntlet = player.getInventory().contains(new net.minecraft.world.item.ItemStack(net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems.INFINITY_GAUNTLET.get()));
			if (hasGauntlet) {
				return false;
			}
		}
		return true;
	}

	// checks if entity is in specified gamemode
	private boolean checkGamemode(Entity entity, net.minecraft.world.level.GameType gameType) {
		if (entity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer() == gameType;
		} else if (entity.level().isClientSide() && entity instanceof Player player) {
			net.minecraft.client.multiplayer.PlayerInfo info = net.minecraft.client.Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
			return info != null && info.getGameMode() == gameType;
		}
		return false;
	}

	// deletes any entity that comes into contact with the blackhole and absorbs their mass
	private void deleteNearbyEntities() {
		int size = getEntityData().get(DATA_size);
		float visualRadius = 0.5f + (size * 0.115f);
		double deletionRadius = visualRadius * 0.5;
		// cap deletion radius to prevent performance issues
		deletionRadius = Math.min(deletionRadius, 15.0);
		// limit entity checks per tick to prevent lag
		int maxEntitiesPerTick = 20;
		int deletedCount = 0;
		int massGained = 0;
		AABB deletionBox = getBoundingBox().inflate(deletionRadius);
		List<Entity> nearbyEntities = level().getEntities(this, deletionBox, e -> e.isAlive() && !(e instanceof BlackholeEntity));
		for (Entity entity : nearbyEntities) {
			if (deletedCount >= maxEntitiesPerTick) {
				break;
			}
			double distance = distanceTo(entity);
			if (distance <= deletionRadius) {
				try {
					// check if entity should be killed/deleted
					boolean shouldKill = shouldAttractEntity(entity);
					if (!shouldKill && entity instanceof Player) {
						// player is protected, skip them
						continue;
					}
					// calculate mass based on entity dimensions
					float width = entity.getBbWidth();
					float height = entity.getBbHeight();
					// mass = volume approximation (width² × height)
					double volume = width * width * height;
					// convert volume to size points (scale factor to make it reasonable)
					int massToAdd = Math.max(1, (int) (volume * 2.0));
					// cap maximum mass gain per entity to prevent exploits
					massToAdd = Math.min(massToAdd, 50);
					massGained += massToAdd;
					// kill players instead of just discarding
					if (entity instanceof Player player) {
						player.hurt(level().damageSources().fellOutOfWorld(), Float.MAX_VALUE);
					}
					entity.discard();
					deletedCount++;
				} catch (Exception e) {
					// silently ignore deletion errors
				}
			}
		}
		// apply mass gained from consumed entities
		if (massGained > 0) {
			int currentSize = getEntityData().get(DATA_size);
			getEntityData().set(DATA_size, currentSize + massGained);
		}
	}

	// keeps blackhole above minimum height
	private void maintainMinimumHeight() {
		int size = getEntityData().get(DATA_size);
		double minHeight = 2.0 + (size * 0.02);
		BlockPos groundPos = findGroundBelow();
		if (groundPos != null) {
			double minY = groundPos.getY() + minHeight;
			double currentY = position().y;
			if (currentY < minY) {
				Vec3 currentVel = getDeltaMovement();
				double yDiff = minY - currentY;
				double yVel = Math.min(0.3, yDiff * 0.15);
				setDeltaMovement(currentVel.x, yVel, currentVel.z);
			}
		}
	}

	// finds ground block below blackhole
	private BlockPos findGroundBelow() {
		BlockPos currentPos = blockPosition();
		for (int i = 0; i < 256; i++) {
			BlockPos checkPos = currentPos.below(i);
			if (checkPos.getY() < level().getMinBuildHeight()) {
				break;
			}
			BlockState state = level().getBlockState(checkPos);
			if (!state.isAir() && state.isSolid()) {
				return checkPos;
			}
		}
		return new BlockPos(currentPos.getX(), level().getMinBuildHeight(), currentPos.getZ());
	}

	private double lerp(double a, double b, double t) {
		return a + (b - a) * t;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.FLYING_SPEED, 0.3);
		return builder;
	}
}
