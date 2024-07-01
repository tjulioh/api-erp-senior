package dev.tjulioh.erpsenior.util;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.domain.PedidoItem;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class PredicateConverterTest {

    private final PredicateConverter predicateConverter;

    PredicateConverterTest() {
        this.predicateConverter = new PredicateConverter(Pedido.class);
    }

    @Test
    void testListEntityFilter() {
        Predicate predicate = predicateConverter.toPredicate("itens.id eq '9a5a872c-572f-4883-afbb-5eddd4ba9e6b'");

        PathBuilder<Pedido> pathBuilder = new PathBuilder<>(Pedido.class, "pedido");
        Predicate predicateExpected = pathBuilder.getList("itens", PedidoItem.class).any().getComparable("id", UUID.class).eq(UUID.fromString("9a5a872c-572f-4883-afbb-5eddd4ba9e6b"));

        assert predicate.equals(predicateExpected);
    }

    @Test
    void testSimpleFilter() {
        Predicate predicate = predicateConverter.toPredicate("descricao eq 'teste'");

        PathBuilder<Pedido> pathBuilder = new PathBuilder<>(Pedido.class, "pedido");
        Predicate predicateExpected = pathBuilder.getString("descricao").equalsIgnoreCase("teste");

        assert predicate.equals(predicateExpected);
    }

    @Test
    void testComposedFilter() {
        Predicate predicate = predicateConverter.toPredicate("numero eq 2 or numero lte 2 or numero eq 1 or numero eq 3");

        PathBuilder<Pedido> pathBuilder = new PathBuilder<>(Pedido.class, "pedido");
        Predicate predicateExpected = pathBuilder.getNumber("numero", Integer.class).eq(2)
                .or(pathBuilder.getNumber("numero", Integer.class).loe(2))
                .or(pathBuilder.getNumber("numero", Integer.class).eq(1))
                .or(pathBuilder.getNumber("numero", Integer.class).eq(3));

        assert predicate.equals(predicateExpected);
    }

    @Test
    void testInvalidFilterString() {
        try {
            predicateConverter.toPredicate("numero 2");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid filter string");
        }
    }

}