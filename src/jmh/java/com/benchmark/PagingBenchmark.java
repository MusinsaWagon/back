package com.benchmark;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;
import com.pricewagon.pricewagon.domain.product.service.ProductService;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;

import jakarta.persistence.EntityManager;

@BenchmarkMode(Mode.AverageTime) // 평균 시간 측정 모드
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 시간 단위 설정
@Warmup(iterations = 3) // 워밍업 설정
@Measurement(iterations = 5) // 실제 측정 횟수
@State(Scope.Benchmark) // 벤치마크 상태 정의
@Fork(1) // fork 수 설정
public class PagingBenchmark {

	private EntityManager entityManager;
	private ProductService productService;
	private static final ShopType shopType = ShopType.MUSINSA;
	private static final int pageSize = 10;

	private static final CustomUserDetails userDetails = new CustomUserDetails(1L, "test", "test", null);

	@TearDown(Level.Invocation) // 밴치마크 메서드 각 호출 시
	public void clearCache() {
		if (entityManager != null) {
			entityManager.clear();
		}
	}

	@Setup(Level.Trial) // 테스트 도중 1번 실행
	public void setUp() {
		ApplicationContext context = SpringApplication.run(PagingBenchmarkConfig.class);
		entityManager = context.getBean(EntityManager.class);
		productService = context.getBean(ProductService.class);
	}

	// Offset 방식 첫페이지 조회 성능 테스트
	@Benchmark
	public void testOffsetFirstPaging(Blackhole blackhole) {
		Pageable pageable = PageRequest.of(0, pageSize);
		// List<BasicProductInfo> offsetProducts = productService.getProductsByShopTypeByOffSet(shopType, pageable);
		// blackhole.consume(offsetProducts);
	}

	// Offset 방식 마지막 페이지 조회 성능 테스트
	@Benchmark
	public void testOffsetLastPaging(Blackhole blackhole) {
		Pageable pageable = PageRequest.of(14999, pageSize);
		// List<BasicProductInfo> offsetProducts = productService.getProductsByShopTypeByOffSet(shopType, pageable);
		// blackhole.consume(offsetProducts);
	}

	// No-Offset 방식 마지막 페이지 조회 성능 테스트
	@Benchmark
	public void testNoOffsetPaging(Blackhole blackhole) {
		Integer lastId = 149990;
		List<BasicProductInfo> noOffsetProducts = productService.getProductsByShopType(shopType, lastId, pageSize,
			null);
		blackhole.consume(noOffsetProducts);
	}
}