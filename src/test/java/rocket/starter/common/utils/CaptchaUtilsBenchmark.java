package rocket.starter.common.utils;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author ron 2022/10/24
 */

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CaptchaUtilsBenchmark {

  @Benchmark
  public void generateImage(Blackhole blackhole) {
    blackhole.consume(CaptchaUtils.generateImage(CaptchaUtils.generateText()));
  }


  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(CaptchaUtilsBenchmark.class.getSimpleName())
        .result("target/result.json").resultFormat(ResultFormatType.JSON).build();
    new Runner(opt).run();
  }
}
