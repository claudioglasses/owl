// code by jph, gjoel
package ch.ethz.idsc.owl.bot.rn;

import ch.ethz.idsc.owl.rrts.core.Transition;
import ch.ethz.idsc.owl.rrts.core.TransitionSamplesWrap;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.qty.Quantity;
import junit.framework.TestCase;

public class RnTransitionSpaceTest extends TestCase {
  public void testLength() {
    Transition transition = RnTransitionSpace.INSTANCE.connect( //
        Tensors.fromString("{1[m],2[m]}"), //
        Tensors.fromString("{1[m],6[m]}"));
    assertEquals(transition.length(), Quantity.of(4, "m"));
    ExactScalarQ.require(transition.length());
  }

  public void testSamples() {
    Tensor start = Tensors.fromString("{1[m],2[m]}");
    Tensor end = Tensors.fromString("{1[m],6[m]}");
    Transition transition = RnTransitionSpace.INSTANCE.connect(start, end);
    {
      Scalar res = Quantity.of(.5, "m");
      TransitionSamplesWrap wrap = transition.sampled(res);
      assertEquals(8, wrap.samples().length());
      assertEquals(start, wrap.samples().get(0));
      assertNotSame(end, Last.of(wrap.samples()));
      assertEquals(Quantity.of(0, "m"), wrap.spacing().Get(0));
      assertEquals(res, wrap.spacing().Get(1));
    }
    {
      TransitionSamplesWrap wrap = transition.sampled(8);
      assertEquals(8, wrap.samples().length());
      assertEquals(start, wrap.samples().get(0));
      assertNotSame(end, Last.of(wrap.samples()));
      assertEquals(Quantity.of(0, "m"), wrap.spacing().Get(0));
      assertEquals(transition.length().divide(RealScalar.of(8)), wrap.spacing().Get(1));
    }
  }
}
