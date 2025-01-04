package com.pricewagon.pricewagon.domain.product.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.pricewagon.pricewagon.domain.history.entity.ProductHistory;

public record ProductHistoryLists(
	List<ProductHistory> oneWeekList,
	List<ProductHistory> oneMonthList,
	List<ProductHistory> threeMonthList
) {
	public static ProductHistoryLists createFrom(List<ProductHistory> allHistories) {
		LocalDateTime now = LocalDateTime.now();

		List<ProductHistory> oneWeek = filterByDateRange(allHistories, now.minusWeeks(1), now, 1);
		List<ProductHistory> oneMonth = filterByDateRange(allHistories, now.minusMonths(1), now, 4);
		List<ProductHistory> threeMonth = filterByDateRange(allHistories, now.minusMonths(3), now, 12);

		return new ProductHistoryLists(oneWeek, oneMonth, threeMonth);
	}

	private static List<ProductHistory> filterByDateRange(List<ProductHistory> histories, LocalDateTime start,
		LocalDateTime end, int interval) {
		LocalDate startDate = start.toLocalDate();
		LocalDate endDate = end.toLocalDate();
		List<ProductHistory> filtered = histories.stream()
			.filter(history -> !history.getCreatedAt().isBefore(startDate) && !history.getCreatedAt().isAfter(endDate))
			.sorted(Comparator.comparing(ProductHistory::getCreatedAt))
			.toList();

		List<ProductHistory> result = new ArrayList<>();
		for (int i = 0; i < filtered.size(); i += interval) {
			result.add(filtered.get(i));
		}
		return result;
	}
}
