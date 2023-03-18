package ru.sigsegv.dopamine.util;

import org.jooq.DSLContext;
import org.jooq.SelectLimitStep;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class Paginated<T> {
    public List<T> data;
    public int currentPage;
    public int pageCount;

    public Paginated(List<T> data, int currentPage, int pageCount) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
    }

    public static <R> Mono<Paginated<R>> fromQuery(DSLContext ctx, SelectLimitStep<?> query, int curPage, int perPage, Class<R> type) {
        return Flux.from(query)
                .count()
                .flatMap(numEntries -> {
                    var pageCount = (int) Math.ceil((float) numEntries / perPage);
                    if (pageCount == 0) pageCount = 1;
                    var currentPage = curPage;
                    if (currentPage >= pageCount) currentPage = pageCount - 1;
                    if (currentPage < 0) currentPage = 0;

                    int finalCurrentPage = currentPage;
                    int finalPageCount = pageCount;
                    return Flux.from(query
                                    .offset(currentPage * perPage)
                                    .limit(perPage))
                            .map(r -> r.into(type))
                            .collectList()
                            .map(data -> new Paginated<>(data, finalCurrentPage, finalPageCount));
                });
    }
}
