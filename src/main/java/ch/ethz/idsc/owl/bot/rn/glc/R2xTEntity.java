// code by jph
package ch.ethz.idsc.owl.bot.rn.glc;

import java.util.Collection;

import ch.ethz.idsc.owl.ani.adapter.TemporalTrajectoryControl;
import ch.ethz.idsc.owl.glc.adapter.EtaRaster;
import ch.ethz.idsc.owl.glc.core.StateTimeRaster;
import ch.ethz.idsc.owl.math.flow.Flow;
import ch.ethz.idsc.owl.math.state.EpisodeIntegrator;
import ch.ethz.idsc.owl.math.state.StateTime;
import ch.ethz.idsc.tensor.Scalar;

/* package */ class R2xTEntity extends R2Entity {
  private final Scalar delay;

  public R2xTEntity(EpisodeIntegrator episodeIntegrator, Scalar delay) {
    super(episodeIntegrator, TemporalTrajectoryControl.createInstance());
    this.delay = delay;
  }

  @Override
  public Scalar delayHint() {
    return delay;
  }

  @Override
  protected StateTimeRaster stateTimeRaster() {
    return EtaRaster.timeDependent(PARTITION_SCALE, FIXEDSTATEINTEGRATOR.getTimeStepTrajectory(), StateTime::joined);
  }

  @Override
  Collection<Flow> createControls() {
    /** 36 corresponds to 10[Degree] resolution */
    Collection<Flow> collection = super.createControls();
    collection.add(r2Flows.stayPut()); // <- does not go well with min-dist cost function
    return collection;
  }
}
