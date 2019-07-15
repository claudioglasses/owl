// code by gjoel
package ch.ethz.idsc.owl.rrts;

import java.util.Objects;
import java.util.stream.Collectors;

import ch.ethz.idsc.owl.math.StateSpaceModel;
import ch.ethz.idsc.owl.math.lane.LaneConsumer;
import ch.ethz.idsc.owl.math.lane.LaneInterface;
import ch.ethz.idsc.owl.math.lane.LaneRandomSample;
import ch.ethz.idsc.owl.math.sample.ConstantRandomSample;
import ch.ethz.idsc.owl.math.sample.RandomSampleInterface;
import ch.ethz.idsc.owl.rrts.core.TransitionCostFunction;
import ch.ethz.idsc.owl.rrts.core.TransitionRegionQuery;
import ch.ethz.idsc.owl.rrts.core.TransitionSpace;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

public abstract class LaneRrtsPlannerServer extends DefaultRrtsPlannerServer implements LaneConsumer {
  private final boolean greedy;
  private LaneRandomSample laneSampler;

  public LaneRrtsPlannerServer( //
      TransitionSpace transitionSpace, //
      TransitionRegionQuery obstacleQuery, //
      Scalar resolution, //
      StateSpaceModel stateSpaceModel, //
      boolean greedy) {
    super(transitionSpace, obstacleQuery, resolution, stateSpaceModel);
    this.greedy = greedy;
  }

  public LaneRrtsPlannerServer( //
      TransitionSpace transitionSpace, //
      TransitionRegionQuery obstacleQuery, //
      Scalar resolution, //
      StateSpaceModel stateSpaceModel, //
      TransitionCostFunction costFunction, //
      boolean greedy) {
    super(transitionSpace, obstacleQuery, resolution, stateSpaceModel, costFunction);
    this.greedy = greedy;
  }

  @Override // from DefaultRrtsPlannerServer
  protected RandomSampleInterface spaceSampler(Tensor state) {
    if (Objects.nonNull(laneSampler))
      return laneSampler;
    return new ConstantRandomSample(state);
  }

  @Override // from DefaultRrtsPlannerServer
  protected RandomSampleInterface goalSampler(Tensor state) {
    if (Objects.nonNull(laneSampler))
      return laneSampler.endSample();
    return new ConstantRandomSample(state);
  }

  @Override // from Consumer
  public void accept(LaneInterface lane) {
    laneSampler = LaneRandomSample.along(lane);
    if (greedy)
      setGreeds(lane.controlPoints().stream().collect(Collectors.toList()));
  }
}
